package org.csc.wizard

import io.ktor.application.call
import io.ktor.response.respondRedirect
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.script
import kotlinx.html.styleLink
import org.csc.html.bootstrapLinks
import org.csc.html.getFile
import org.csc.html.html
import org.csc.html.navbar
import org.csc.pdf.PdfBoxRecognizer
import org.csc.session.FileManager
import org.csc.session.SessionManager

enum class WizardStep(val fileName: String) {
    UploadPDF("pdf"),
    UploadTests("json"),
    Completed("result");
}

fun Routing.wizardRouting() {
    get("/wizard") {
        val session = SessionManager.getSession(call)
        html {
            head {
                bootstrapLinks()
                styleLink("wizard.css")
                script(src = "wizard.js") {}
            }

            body {
                navbar("TT Solver")
                wizardSteps(session.curStep)
            }
        }
    }
    post("/wizard/uploadPDF") {
        val session = SessionManager.getSession(call)
        val fileLocal = FileManager.createPdfRawFile(session.uuid, call.getFile("pdf"))
        FileManager.createPdfTextFile(session.uuid, PdfBoxRecognizer.recognize(fileLocal).toByteArray())
        SessionManager.changeStep(call, session, WizardStep.UploadTests)
        call.respondRedirect("/wizard")
    }
    post("/wizard/uploadTest") {
        val session = SessionManager.getSession(call)
        FileManager.createJsonFile(session.uuid,call.getFile("json"))
        SessionManager.changeStep(call, session, WizardStep.Completed)
        call.respondRedirect("/wizard")
    }
}