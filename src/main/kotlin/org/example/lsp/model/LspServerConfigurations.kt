package org.example.lsp.model

data class LspServerConfigurations(
    val customizations: List<Customization>
)

data class Customization(
    val arn: String,
    val name: String,
    val description: String
)