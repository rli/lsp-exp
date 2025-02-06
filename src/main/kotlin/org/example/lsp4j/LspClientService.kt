package org.example.lsp4j

import com.intellij.execution.impl.ExecutionManagerImpl
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessOutputType
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.redhat.devtools.lsp4ij.server.LanguageServerLogErrorHandler
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.Launcher
import org.eclipse.lsp4j.launch.LSPLauncher
import org.example.LSP_COMMAND
import org.example.lsp.model.BearerCredentials
import org.example.lsp.model.UpdateCredentialsPayload
import org.example.lsp.model.UpdateCredentialsPayloadData
import java.io.*
import java.nio.charset.StandardCharsets

// https://github.com/redhat-developer/lsp4ij/blob/main/src/main/java/com/redhat/devtools/lsp4ij/server/LSPProcessListener.java
// JB impl and redhat both use a wrapper to handle input buffering issue
internal class LSPProcessListener(private val errorHandlers: MutableList<LanguageServerLogErrorHandler>) : ProcessListener {
    private val outputStream = PipedOutputStream()
    private val outputStreamWriter: OutputStreamWriter = OutputStreamWriter(this.outputStream, StandardCharsets.UTF_8)
    private val inputStream: PipedInputStream = PipedInputStream(this.outputStream)

    fun getInputStream(): InputStream {
        return this.inputStream
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        if (ProcessOutputType.isStdout(outputType)) {
            try {
                this.outputStreamWriter.write(event.text)
                this.outputStreamWriter.flush()
            } catch (var5: IOException) {
                ExecutionManagerImpl.stopProcess(event.processHandler)
            }
        } else if (ProcessOutputType.isStderr(outputType)) {
            for (handler in this.errorHandlers) {
                handler.logError(removeEndLine(event.text))
            }
        }
    }

    override fun processTerminated(event: ProcessEvent) {
        try {
            this.outputStreamWriter.close()
            this.outputStream.close()
        } catch (var3: IOException) {
        }
    }

    companion object {
        private fun removeEndLine(text: String): String {
            if (text.endsWith("\r\n")) {
                return text.substring(0, text.length - 2)
            } else {
                return if (text.endsWith("\n")) text.substring(0, text.length - 1) else text
            }
        }
    }
}

@Service(Service.Level.PROJECT)
class LspClientService(project: Project) : Disposable {
    private val launcher: Launcher<Lsp4JService>

    private val languageServer: Lsp4JService
        get() = launcher.remoteProxy

    init {
        val cmd = LSP_COMMAND
        val handler = KillableColoredProcessHandler.Silent(cmd)
        handler.addProcessListener(object : ProcessListener {
            override fun onTextAvailable(
                event: ProcessEvent,
                outputType: Key<*>
            ) {
                if (outputType != ProcessOutputTypes.STDERR) return
                println("$outputType: ${event.text}")
            }
        })
        val inputWrapper = LSPProcessListener(mutableListOf())
        handler.addProcessListener(inputWrapper)

        handler.startNotify()

        launcher = LSPLauncher.Builder<Lsp4JService>()
            .setLocalService(Lsp4JClientImpl())
            .setRemoteInterface(Lsp4JService::class.java)
            .traceMessages(PrintWriter(System.out))
            .setInput(inputWrapper.getInputStream())
            .setOutput(handler.process.outputStream)
//            .wrapMessages { consumer ->
//                MessageConsumer { message -> println(message) }
//            }
            .create()

        launcher.startListening()

        languageServer.workspaceService.didChangeConfiguration(DidChangeConfigurationParams())
        languageServer.updateTokenCredentials(
            UpdateCredentialsPayload.Companion.fromPayloadData(
                UpdateCredentialsPayloadData(BearerCredentials("adfafddddd"))
            )
        )

        project.messageBus.connect(this).subscribe(
            FileEditorManagerListener.FILE_EDITOR_MANAGER,
            object: FileEditorManagerListener {
                override fun fileOpened(
                    source: FileEditorManager,
                    file: VirtualFile
                ) {
                    languageServer.textDocumentService.didOpen(
                        DidOpenTextDocumentParams().apply {
                            textDocument = TextDocumentItem().apply {
                                uri = file.url
                                text = file.inputStream.readAllBytes().decodeToString()
                            }
                        }
                    )
                }

                override fun fileClosed(
                    source: FileEditorManager,
                    file: VirtualFile
                ) {
                    languageServer.textDocumentService.didClose(
                        DidCloseTextDocumentParams().apply {
                            textDocument = TextDocumentIdentifier().apply {
                                uri = file.url
                            }
                        }
                    )
                }
            }
        )
    }

    override fun dispose() {
        languageServer.apply {
            shutdown().thenRun { exit() }
        }
    }

    companion object {
        fun getInstance(project: Project): LspClientService {
            return project.service()
        }
    }
}