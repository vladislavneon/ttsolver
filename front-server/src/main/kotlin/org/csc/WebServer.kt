package org.csc

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.DefaultHeaders
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import org.csc.session.SessionManager
import org.csc.session.UserSession
import org.csc.wizard.wizardRouting

object WebServer {
    fun prepare(): NettyApplicationEngine = embeddedServer(Netty, 8081) {
        install(DefaultHeaders)
        install(Locations)
        install(CORS) { anyHost() }
        install(Sessions) {
            cookie<UserSession>(SessionManager.sessionCookie)
        }
        routing {
            static {
                resource("wizard.css", "/css/wizard.css")
                resource("wizard.js", "/js/wizard.js")
            }
            wizardRouting()
            get("/") {
                call.respondRedirect("/wizard")
            }

        }
    }
}