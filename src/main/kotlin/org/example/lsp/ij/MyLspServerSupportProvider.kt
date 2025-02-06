package org.example.lsp.ij

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.Lsp4jClient
import com.intellij.platform.lsp.api.LspServerListener
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.platform.lsp.api.LspServerNotificationsHandler
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import org.eclipse.lsp4j.ConfigurationItem
import org.eclipse.lsp4j.ConfigurationParams
import org.eclipse.lsp4j.InitializeResult
import org.eclipse.lsp4j.services.LanguageServer
import org.example.LSP_COMMAND
import org.example.lsp.MyLanguageClient
import org.example.lsp.MyLanguageServer
import org.example.lsp.model.BearerCredentials
import org.example.lsp.model.ConnectionMetadata
import org.example.lsp.model.GetConfigurationFromServerParams
import org.example.lsp.model.SsoProfileData
import org.example.lsp.model.UpdateCredentialsPayload
import org.example.lsp.model.UpdateCredentialsPayloadData
import java.util.concurrent.CompletableFuture

class MyLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        serverStarter.ensureServerStarted(FooLspServerDescriptor(project))
    }
}


private class FooLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Foo") {
    override fun isSupportedFile(file: VirtualFile) = true
    override fun createCommandLine() = LSP_COMMAND

    override val lsp4jServerClass: Class<out LanguageServer>
        get() = MyLanguageServer::class.java

    override fun createLsp4jClient(handler: LspServerNotificationsHandler): Lsp4jClient {
        return MyLanguageClientJBImpl(JankHandler(handler))
    }

    override val lspServerListener = object : LspServerListener {
        override fun serverInitialized(params: InitializeResult) {
            println(params)
            kotlinx.coroutines.runBlocking {
                val ijServer =
                    LspServerManager.getInstance(project).getServersForProvider(MyLspServerSupportProvider::class.java)
                        .first()
                val server = (ijServer.lsp4jServer as MyLanguageServer)
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

    override fun getWorkspaceConfiguration(item: ConfigurationItem): Any? {
        return super.getWorkspaceConfiguration(item)
    }
}

private class JankHandler(delegate: LspServerNotificationsHandler) : LspServerNotificationsHandler by delegate {
    override fun configuration(params: ConfigurationParams): CompletableFuture<List<Any?>> {
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

}

private class MyLanguageClientJBImpl(handler: LspServerNotificationsHandler) : Lsp4jClient(handler), MyLanguageClient {
    override fun telemetryEvent(event: Any) {
        println(event)
    }

    override fun getConnectionMetadata(): CompletableFuture<ConnectionMetadata> {
        return CompletableFuture.completedFuture(
            ConnectionMetadata(
                SsoProfileData("start")
            )
        )
    }
}