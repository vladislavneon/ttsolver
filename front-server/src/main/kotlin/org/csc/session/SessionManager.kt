package org.csc.session

import io.ktor.application.ApplicationCall
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.csc.wizard.WizardStep
import java.util.*

data class UserSession(val uuid: UUID, val curStep: WizardStep)

object SessionManager {
    const val sessionCookie = "_tts_st"

    fun getSession(call: ApplicationCall): UserSession {
        var userSession = call.sessions.get<UserSession>()
        if (userSession == null) {
            userSession = UserSession(UUID.randomUUID(), WizardStep.UploadData)
            call.sessions.set(userSession)
        }
        return userSession
    }

    fun changeStep(call: ApplicationCall, session: UserSession, step: WizardStep) {
        call.sessions.set(UserSession(session.uuid, step))
    }

    fun resetSession(call: ApplicationCall) {
        val session = getSession(call)
        call.sessions.set(UserSession(session.uuid, WizardStep.UploadData))
        FileManager.removePdfRawFile(session.uuid)
        FileManager.removePdfTextFile(session.uuid)
        FileManager.removeJsonFile(session.uuid)
        FileManager.removeResultFile(session.uuid)
    }

}