package org.csc

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.locations.Locations
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

object WebServer {
    fun prepare(): NettyApplicationEngine = embeddedServer(Netty, 8080) {
        install(DefaultHeaders)
        install(Locations)
        install(CORS) { anyHost() }
        routing {
            get("/") {
                call.respondText(contentType = ContentType.parse("text/html")) {
                    Resources.getText("index.xhtml")
                }
            }
        }
    }
}