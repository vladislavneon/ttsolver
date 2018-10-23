package org.csc.wizard

import kotlinx.html.*
import org.csc.html.*
import org.csc.ml.Answer
import org.csc.ml.Question
import org.csc.session.FileManager
import org.csc.session.UserSession
import org.csc.utils.Json
import kotlin.reflect.KClass

fun UL.stepHeader(label: String, shortLabel: String, iconName: String, stepNum: Int, isActive: Boolean) {
    li {
        classes = if (isActive) setOf("active") else setOf("disabled")
        role = "presentation"
        div {
            style = "text-align:center"
            span {
                style = if (!isActive) {
                    "font-size:20;color:#A9A9A9"
                } else {
                    "font-size:20;color:#5bc0de"
                }
                +shortLabel
            }
        }
        a(href = "#step$stepNum") {
            title = label
            role = "tab"
            attributes["data-toggle"] = "tab"
            attributes["aria-controls"] = "stepHeader$stepNum"
            span("round-tab") {
                icon(iconName)
            }
        }
    }
}

fun DIV.stepBody(label: String, isActive: Boolean, content: DIV.() -> Unit, button: DIV.() -> Unit) {
    div("tab-content") {
        div("tab-pane" + if (isActive) " active" else "") {
            h3 {
                +label
            }
            content()
            button()
        }
    }
}

fun BODY.wizardSteps(session: UserSession, step: WizardStep) {
    container {
        row {
            section {
                wizard {
                    div("wizard-inner") {
                        div("connecting-line")
                        ul("nav nav-tabs") {
                            role = "tablist"
                            stepHeader("Upload data of the test", "Upload data", "file", 0, step.ordinal == 0)
                            stepHeader("Upload test", "Upload test", "question-sign", 1, step.ordinal == 1)
                            stepHeader("Completed", "Done!", "ok", 2, step.ordinal == 2)
                        }
                    }

                    when (step) {
                        WizardStep.UploadData -> {
                            form("/wizard/uploadData", method = FormMethod.post, encType = FormEncType.multipartFormData) {
                                div("tab-content") {
                                    stepBody("Upload .PDF or .TXT with data", step.ordinal == 0, {
                                        br()
                                        fileInput {
                                            name = step.fileName
                                            size = (30 * 1024 * 1024).toString()
                                            accept = ".pdf, .txt"
                                            required = true
                                        }
                                    }, { submitButton("Save and next") })
                                }
                            }
                        }
                        WizardStep.UploadTests -> {
                            form("/wizard/uploadTest", method = FormMethod.post, encType = FormEncType.multipartFormData) {
                                div("tab-content active") {
                                    stepBody("Upload JSON with test", step.ordinal == 1, {
                                        br()
                                        fileInput {
                                            style = "margin-top: 5px;"
                                            name = step.fileName
                                            size = (30 * 1024 * 1024).toString()
                                            accept = ".json"
                                            required = true
                                            required = true
                                        }
                                    }, { submitButton("Save and next") })
                                }
                            }
                        }
                        WizardStep.Completed -> {
                            stepBody("Results", step.ordinal == 2, { br() }, {
                                val answers = Json.readCollectionValue(FileManager.getResultFile(session.uuid).readText(),
                                        List::class as KClass<List<Answer>>, Answer::class)
                                val questions = Json.readCollectionValue(FileManager.getJsonFile(session.uuid).readText(),
                                        List::class as KClass<List<Question>>, Question::class)
                                val text = FileManager.getPdfTextFile(session.uuid).readText()
                                answers(answers, questions, text)
                            })
                        }
                    }
                }
            }
        }
    }
}

fun FlowContent.answers(answers: List<Answer>, question: List<Question>, text: String) {

    val textSplitted = text.split("\n")
    for ((ind, answer) in answers.withIndex()) {
        div("article") {
            h5 {
                +"Q${ind + 1}. ${answer.question}"
            }
            div("article-content") {
                p {
                    +"Answers: "
                }
                ul {
                    for ((indA, txt) in answer.answers.withIndex()) {
                        if (txt == "1") {
                            li {
                                +question[ind].options[indA]
                            }
                        }
                    }
                }

                if (textSplitted.size > answer.answer_area && answer.answer_area != 0 &&
                        textSplitted[answer.answer_area].isNotBlank()) {
                    p {
                        +"Because of: "
                    }
                    i {
                        style = "font-size:14"
                        +">  "
                        +textSplitted[answer.answer_area]
                    }
                }
            }
        }
    }
}
