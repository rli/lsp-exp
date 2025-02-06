package org.example

import com.intellij.execution.configurations.GeneralCommandLine

val LSP_COMMAND
    get() = GeneralCommandLine(
        "/Users/richali/idetools/lsp-exp/contrib/servers/node",
        "/Users/richali/idetools/lsp-exp/contrib/servers/aws-lsp-codewhisperer.js",
        "--stdio"
    )
