package org.example.lsp4j

import org.eclipse.lsp4j.jsonrpc.messages.ResponseMessage
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.eclipse.lsp4j.services.LanguageServer
import org.example.lsp.model.UpdateCredentialsPayload
import java.util.concurrent.CompletableFuture

interface Lsp4JService : LanguageServer {
    @JsonRequest("aws/credentials/token/update")
    fun updateTokenCredentials(payload: UpdateCredentialsPayload): CompletableFuture<ResponseMessage>
}