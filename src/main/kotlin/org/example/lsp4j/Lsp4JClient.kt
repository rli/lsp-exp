package org.example.lsp4j

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import org.eclipse.lsp4j.ConfigurationParams
import org.eclipse.lsp4j.MessageActionItem
import org.eclipse.lsp4j.MessageParams
import org.eclipse.lsp4j.MessageType
import org.eclipse.lsp4j.PublishDiagnosticsParams
import org.eclipse.lsp4j.ShowMessageRequestParams
import org.eclipse.lsp4j.services.LanguageClient
import org.example.lsp.MyLanguageClient
import org.example.lsp.model.ConnectionMetadata
import org.example.lsp.model.SsoProfileData
import java.util.concurrent.CompletableFuture

class Lsp4JClientImpl : LanguageClient, MyLanguageClient {
    override fun telemetryEvent(`object`: Any) {
        println(`object`)
    }

    override fun publishDiagnostics(diagnostics: PublishDiagnosticsParams) {
        println(diagnostics)
    }

    override fun showMessage(messageParams: MessageParams) {
        val type = when (messageParams.type) {
            MessageType.Error -> NotificationType.ERROR
            MessageType.Warning -> NotificationType.WARNING
            MessageType.Info, MessageType.Log -> NotificationType.INFORMATION
        }
        Notifications.Bus.notify(
            Notification(
                "example.notification",
                "Lsp4JClientImpl",
                messageParams.message,
                type
            )
        )
    }

    override fun showMessageRequest(requestParams: ShowMessageRequestParams): CompletableFuture<MessageActionItem?>? {
        println(requestParams)

        return CompletableFuture.completedFuture(null)
    }

    override fun logMessage(message: MessageParams) {
        showMessage(message)
    }

    override fun getConnectionMetadata(): CompletableFuture<ConnectionMetadata> {
        return CompletableFuture.completedFuture(
            ConnectionMetadata(
                SsoProfileData("start")
            )
        )
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
}