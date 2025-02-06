package org.example.lsp.model

data class InlineCompletionResponse(
    val items: List<InlineCompletionItem>
)

data class InlineCompletionItem(
    val itemId: String,
    val insertText: String,
    val references: List<InlineCompletionReference>
)

data class InlineCompletionReference(
    val referenceName: String,
    val referenceUrl: String,
    val licenseName: String,
    val position: InlineCompletionReferencePosition
)

data class InlineCompletionReferencePosition(
    val startCharacter: Int,
    val endCharacter: Int,
)