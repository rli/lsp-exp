package org.example.lsp.model

data class ConnectionMetadata(
    val sso: SsoProfileData
)

data class SsoProfileData(
    val startUrl: String
)
