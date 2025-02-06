package org.example.lsp.rh

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.LanguageServerFactory
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl
import com.redhat.devtools.lsp4ij.server.StreamConnectionProvider
import org.eclipse.lsp4j.services.LanguageServer
import org.example.lsp.rh.MyLanguageClientImpl
import org.example.lsp.MyLanguageServer

class MyLanguageServerFactory : LanguageServerFactory {
    override fun createConnectionProvider(project: Project): StreamConnectionProvider {
        return MyLanguageServerConnectionProvider()
    }

    override fun createLanguageClient(project: Project): LanguageClientImpl {
        return MyLanguageClientImpl(project)
    }

    override fun getServerInterface(): Class<out LanguageServer?> {
        return MyLanguageServer::class.java
    }
}