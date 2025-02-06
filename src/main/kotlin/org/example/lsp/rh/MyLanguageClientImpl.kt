package org.example.lsp.rh

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.ServerStatus
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl
import org.eclipse.lsp4j.ConfigurationParams
import org.example.lsp.MyLanguageClient
import org.example.lsp.MyLanguageServer
import org.example.lsp.model.BearerCredentials
import org.example.lsp.model.ConnectionMetadata
import org.example.lsp.model.GetConfigurationFromServerParams
import org.example.lsp.model.SsoProfileData
import org.example.lsp.model.UpdateCredentialsPayload
import org.example.lsp.model.UpdateCredentialsPayloadData
import java.util.concurrent.CompletableFuture

class MyLanguageClientImpl(project: Project) : MyLanguageClient, LanguageClientImpl(project) {
    override fun getConnectionMetadata(): CompletableFuture<ConnectionMetadata> {
        return CompletableFuture.completedFuture(
            ConnectionMetadata(
                SsoProfileData("start")
            )
        )
    }

    override fun telemetryEvent(event: Any) {
        println(event)
    }

    override fun configuration(params: ConfigurationParams): CompletableFuture<List<Any>> {
        if (params.items.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.completedFuture(buildList {
            params.items.forEach { item ->
                when (item.section) {
                    "aws.q" -> {
                        add(
                            mapOf(
                                "customization" to null,
                                "enableTelemetryEventsToDestination" to false,
                                "optOutTelemetry" to true,
                            )
                        )
                    }

                    "aws.codeWhisperer" -> {
                        add(
                            mapOf(
                                "shareCodeWhispererContentWithAWS" to false,
                                "includeSuggestionsWithCodeReferences" to true,
                            )
                        )
                    }

                    else -> {}
                }
            }
        })
    }

    override fun handleServerStatusChanged(serverStatus: ServerStatus) {
        if (serverStatus == ServerStatus.started) {
            triggerChangeConfiguration()
            val server = (languageServer as MyLanguageServer)
//            server.workspaceService.didChangeConfiguration()
            server.updateTokenCredentials(
                UpdateCredentialsPayload.Companion.fromPayloadData(
                    UpdateCredentialsPayloadData(BearerCredentials("adfafddddd"))
                )
            )
                .thenCompose { r ->
                    println(r)
                    server.getConfigurationFromServer(GetConfigurationFromServerParams("aws.q"))
                }.thenAccept { t ->
                    println(t)
                }
        }
    }
}