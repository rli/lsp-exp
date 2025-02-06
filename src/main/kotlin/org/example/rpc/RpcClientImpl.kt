package org.example.rpc

class RpcClientImpl : RpcClient {
    override fun telemetryEvent(event: Any?) {
        println(event)
    }
}