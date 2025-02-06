package org.example

import com.intellij.execution.configurations.GeneralCommandLine

val LSP_COMMAND
    get() = GeneralCommandLine(
        "/path/to/node",
        "/path/to/aws-lsp-codewhisperer-token-binary.js",
        "--stdio"
    )
