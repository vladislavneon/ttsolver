package org.csc.wizard

import io.ktor.application.call
import io.ktor.response.respondFile
import io.ktor.response.respondRedirect
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.script
import kotlinx.html.styleLink
import kotlinx.io.core.toByteArray
import org.csc.html.*
import org.csc.ml.MlSolver
import org.csc.ml.Question
import org.csc.pdf.PDFRecognizer
import org.csc.session.FileManager
import org.csc.session.SessionManager
import org.csc.utils.Json
import kotlin.reflect.KClass

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
                ttsNavbar()
                wizardSteps(session.curStep)
            }
        }
    }
    post("/wizard/uploadPDF") {
        val session = SessionManager.getSession(call)
        val fileLocal = FileManager.createPdfRawFile(session.uuid, call.getFile("pdf"))
        FileManager.createPdfTextFile(session.uuid, PDFRecognizer.recognize(fileLocal).toByteArray())
        SessionManager.changeStep(call, session, WizardStep.UploadTests)
        call.respondRedirect("/wizard")
    }
    post("/wizard/uploadTest") {
        val session = SessionManager.getSession(call)
        FileManager.createJsonFile(session.uuid, call.getFile("json"))
        SessionManager.changeStep(call, session, WizardStep.Completed)
        call.respondRedirect("/wizard")
    }
    get("/wizard/result") {
        val session = SessionManager.getSession(call)
        val text = FileManager.getPdfTextFile(session.uuid).readText()
        val questions = Json.readCollectionValue(FileManager.getJsonFile(session.uuid).readText(),
                List::class as KClass<List<Question>>, Question::class)
        FileManager.createResultFile(session.uuid,
                Json.writeValueAsString(MlSolver.solve(text, questions)).toByteArray()
        )
        call.respondFile(FileManager.getResultFile(session.uuid))
    }
}