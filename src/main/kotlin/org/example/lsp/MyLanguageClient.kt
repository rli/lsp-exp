package org.example.lsp

import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.eclipse.lsp4j.services.LanguageClient
import org.example.lsp.model.ConnectionMetadata
import org.example.lsp.model.TelemetryEvent
import java.util.concurrent.CompletableFuture

interface MyLanguageClient : LanguageClient {
    @JsonRequest("aws/credentials/getConnectionMetadata")
    fun getConnectionMetadata(): CompletableFuture<ConnectionMetadata>
}