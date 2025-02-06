package org.example.lsp4j

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import org.eclipse.lsp4j.DidChangeConfigurationParams
import org.eclipse.lsp4j.launch.LSPLauncher
import org.example.LSP_COMMAND
import org.example.lsp.model.BearerCredentials
import org.example.lsp.model.UpdateCredentialsPayload
import org.example.lsp.model.UpdateCredentialsPayloadData
import java.io.PrintWriter

@Service
class LspClientService {
    init {
        val cmd = LSP_COMMAND

        val process = cmd.createProcess()
        val launcher = LSPLauncher.Builder<Lsp4JService>()
            .setLocalService(Lsp4JClientImpl())
            .setRemoteInterface(Lsp4JService::class.java)
            .traceMessages(PrintWriter(System.out))
            .setInput(process.inputStream)
            .setOutput(process.outputStream)
            .create()

        launcher.startListening()

        launcher.remoteProxy.workspaceService.didChangeConfiguration(DidChangeConfigurationParams())
        launcher.remoteProxy.updateTokenCredentials(
            UpdateCredentialsPayload.Companion.fromPayloadData(
                UpdateCredentialsPayloadData(BearerCredentials("adfafddddd"))
            )
        )

//        ApplicationManager.getApplication().executeOnPooledThread {
//            Thread.sleep(20000)
//            launcher.remoteProxy.apply {
//                shutdown().thenRun { exit() }
//            }
//        }
    }

    companion object {
        fun getInstance(): LspClientService {
            return service()
        }
    }
}