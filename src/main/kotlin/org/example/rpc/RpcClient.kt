package org.example.rpc

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification

interface RpcClient {
    @JsonNotification("telemetry/event")
    fun telemetryEvent(event: Any?)
}