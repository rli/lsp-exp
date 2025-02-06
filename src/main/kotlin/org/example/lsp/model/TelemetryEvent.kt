package org.example.lsp.model

data class TelemetryEvent(
    val name: String,
    val result: String,
    val data: Map<String, Object>,
    val errorData: ErrorData
)

data class ErrorData(
    val reason: String,
    val errorCode: String,
    val httpStatusCode: Int,
)
