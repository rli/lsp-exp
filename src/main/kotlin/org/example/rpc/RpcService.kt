package org.example.rpc

import org.eclipse.lsp4j.jsonrpc.messages.ResponseMessage
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.example.lsp.model.UpdateCredentialsPayload
import java.util.concurrent.CompletableFuture

interface RpcService {
    @JsonRequest("aws/credentials/token/update")
    fun updateTokenCredentials(payload: UpdateCredentialsPayload): CompletableFuture<ResponseMessage>
}