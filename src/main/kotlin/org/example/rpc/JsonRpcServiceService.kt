package org.example.rpc

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import org.eclipse.lsp4j.jsonrpc.Launcher
import org.example.LSP_COMMAND
import org.example.lsp.model.BearerCredentials
import org.example.lsp.model.UpdateCredentialsPayload
import org.example.lsp.model.UpdateCredentialsPayloadData
import java.io.PrintWriter

@Service
class JsonRpcServiceService {
    init {
        val cmd = LSP_COMMAND
        val process = cmd.createProcess()
        val launcher = Launcher.createLauncher(
            RpcClientImpl(),
            RpcService::class.java,
            process.inputStream,
            process.outputStream,
            false,
            object : PrintWriter(System.out) {})

        launcher.startListening()

        launcher.remoteProxy.updateTokenCredentials(
            UpdateCredentialsPayload.Companion.fromPayloadData(
                UpdateCredentialsPayloadData(BearerCredentials("adfafddddd"))
            )
        )
    }

    companion object {
        fun getInstance(): JsonRpcServiceService {
            return service()
        }
    }
}