package org.csc

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.DefaultHeaders
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import org.csc.session.SessionManager
import org.csc.session.UserSession
import org.csc.wizard.WizardStep
import org.csc.wizard.wizardRouting

object WebServer {
    fun prepare(): NettyApplicationEngine = embeddedServer(Netty, 8080) {
        install(DefaultHeaders)
        install(Locations)
        install(CORS) { anyHost() }
        install(Sessions) {
            cookie<UserSession>(SessionManager.sessionCookie) {
                cookie.path = "/"
            }
        }
        routing {
            static {
                resource("wizard.css", "/css/wizard.css")
                resource("wizard.js", "/js/wizard.js")
            }
            wizardRouting()
            get("/health") {
                call.respondText("Ok")
            }
            get("/") {
                call.respondRedirect("/wizard")
            }
            get("/reset") {
                SessionManager.resetSession(call)
                SessionManager.changeStep(call, SessionManager.getSession(call), WizardStep.UploadData)

                call.respondRedirect("/wizard")
            }
        }
    }
}