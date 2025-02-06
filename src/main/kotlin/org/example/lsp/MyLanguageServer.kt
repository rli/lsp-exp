package org.example.lsp

import org.eclipse.lsp4j.jsonrpc.messages.ResponseMessage
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.eclipse.lsp4j.services.LanguageServer
import org.example.lsp.model.GetConfigurationFromServerParams
import org.example.lsp.model.InlineCompletionParams
import org.example.lsp.model.InlineCompletionResponse
import org.example.lsp.model.LspServerConfigurations
import org.example.lsp.model.UpdateCredentialsPayload
import java.util.concurrent.CompletableFuture

interface MyLanguageServer : LanguageServer {
    @JsonRequest("aws/textDocument/inlineCompletionWithReferences")
    fun inlineCompletionWithReferences(params: InlineCompletionParams): CompletableFuture<InlineCompletionResponse>

    @JsonRequest("aws/getConfigurationFromServer")
    fun getConfigurationFromServer(params: GetConfigurationFromServerParams): CompletableFuture<LspServerConfigurations>

    @JsonRequest("aws/credentials/token/update")
    fun updateTokenCredentials(payload: UpdateCredentialsPayload): CompletableFuture<ResponseMessage>
}
