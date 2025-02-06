package org.example.lsp.model

import org.eclipse.lsp4j.Range
import org.eclipse.lsp4j.TextDocumentPositionAndWorkDoneProgressParams

data class InlineCompletionParams(
    val context: InlineCompletionContext
) : TextDocumentPositionAndWorkDoneProgressParams()

data class InlineCompletionContext(
    val triggerKind: InlineCompletionTriggerKind? = null,
    val selectedCompletionInfo: SelectedCompletionInfo? = null,
)

enum class InlineCompletionTriggerKind(val value: Int) {
    Invoke(0),
    Automatic(1),
}

data class SelectedCompletionInfo(
    val range: Range,
    val text: String
)