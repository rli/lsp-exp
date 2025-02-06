package org.example.lsp.model

data class UpdateCredentialsPayload(
    val data: String,
    val encrypted: String,
) {
    companion object {
        fun fromPayloadData(payloadData: UpdateCredentialsPayloadData) = UpdateCredentialsPayload(
            data = """{"data": "${payloadData.data.token}"}""",
            encrypted = "false"
        )
    }
}

data class UpdateCredentialsPayloadData(
    val data: BearerCredentials
)

data class BearerCredentials(
    val token: String
)