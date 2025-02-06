package org.example.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.platform.lsp.api.LspServerManager
import com.redhat.devtools.lsp4ij.LanguageServerManager
import kotlinx.coroutines.runBlocking
import org.eclipse.lsp4j.Position
import org.eclipse.lsp4j.TextDocumentIdentifier
import org.example.lsp.MyLanguageServer
import org.example.lsp.ij.MyLspServerSupportProvider
import org.example.lsp.model.InlineCompletionContext
import org.example.lsp.model.InlineCompletionParams
import org.example.lsp.model.InlineCompletionTriggerKind
import org.example.lsp4j.LspClientService
import org.example.rpc.JsonRpcServiceService
import java.util.concurrent.CompletableFuture

/**
 * An action that is triggered with alt+C / option+C in an IDE editor
 */
class ManualInvoke : DumbAwareAction("Manual Invoke") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR) ?: return

        // ---- invoke via raw JSON-RPC
//        service<JsonRpcServiceService>()


        // ---- invoke via lsp4j
        project.service<LspClientService>()


        // ---- REDHAT ----
//        val server = LanguageServerManager.getInstance(project)
//            .getLanguageServer("exampleServer")
//            .thenApply { t -> t?.server }
//            .thenApply { t ->
//                if (t == null) {
//                    null
//                }
//
//                (t as MyLanguageServer)
//            }
//        server.thenApply { t ->
//            t.inlineCompletionWithReferences(
//                InlineCompletionParams(
//                    InlineCompletionContext(
//                        triggerKind = InlineCompletionTriggerKind.Invoke
//                    )
//                ).apply {
//                    textDocument = LSPIJUtils.toTextDocumentIdentifier(editor.virtualFile)
//                    position = with(editor.caretModel.primaryCaret.logicalPosition) {
//                        Position(line, column)
//                    }
//                }
//            )
//        }


        // ----- IJ ULTIMATE API ----
        // uncomment relevant block in plugin.xml to invoke
//        ApplicationManager.getApplication().executeOnPooledThread {
//            runBlocking {
//                val ijServer =
//                    LspServerManager.getInstance(project).getServersForProvider(MyLspServerSupportProvider::class.java)
//                        .first()
//                ijServer.sendRequest { it ->
//                    val server = it as MyLanguageServer
//
//                    return@sendRequest CompletableFuture.completedFuture(
//                        server.inlineCompletionWithReferences(
//                            InlineCompletionParams(
//                                InlineCompletionContext(
//                                    triggerKind = InlineCompletionTriggerKind.Invoke
//                                )
//                            ).apply {
//                                textDocument =
//                                    TextDocumentIdentifier(ijServer.descriptor.getFileUri(editor.virtualFile))
//                                position = with(editor.caretModel.primaryCaret.logicalPosition) {
//                                    Position(line, column)
//                                }
//                            }
//                        )
//                    )
//                }
//            }
//        }
    }
}