package org.example.lsp.rh

import com.intellij.execution.configurations.GeneralCommandLine
import com.redhat.devtools.lsp4ij.server.OSProcessStreamConnectionProvider

class MyLanguageServerConnectionProvider : OSProcessStreamConnectionProvider() {
    init {
        val commandLine = GeneralCommandLine(
            "/Users/richali/idetools/lsp-exp/contrib/servers/node",
            "/Users/richali/idetools/language-servers/app/aws-lsp-codewhisperer-runtimes/build/aws-lsp-codewhisperer-token-binary.js",
            "--stdio"
        )

        super.setCommandLine(commandLine);
    }
}