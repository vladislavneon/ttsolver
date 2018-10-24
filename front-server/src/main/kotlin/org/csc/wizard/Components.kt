package org.csc.wizard

import kotlinx.html.*
import org.csc.html.*
import org.csc.ml.Answer
import org.csc.ml.AnswerVerdict
import org.csc.ml.Question
import org.csc.session.FileManager
import org.csc.session.UserSession
import org.csc.utils.Json
import java.lang.Integer.max
import java.lang.Integer.min
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
                            form("/wizard/uploadTest", method = FormMethod.post) {
                                div("tab-content active") {
                                    stepBody("Insert conditions of the test", step.ordinal == 1, {
                                        br()
                                        textArea("testData")
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
                        li {
                            style = "color: " + if (answer.verdict == AnswerVerdict.fail) "yellow" else if (txt =="1") "green;" else "#e74c3c;"
                            +question[ind].options[indA]
                        }
                    }
                }

                if (textSplitted.size > answer.answer_area && answer.answer_area != -1 &&
                        textSplitted[answer.answer_area].isNotBlank()) {
                    if (answer.verdict == AnswerVerdict.ok) {
                        p {
                            +"Because of: "
                        }
                        val to = min(answer.answer_area + 2, textSplitted.size - 1)
                        blockQuote("hero") {
                            style = "font-size:14"
                            + "..."
                            for (i in answer.answer_area..to) {
                                +textSplitted[i]
                            }
                            + "..."
                        }
                    }
                    if (answer.verdict == AnswerVerdict.found_area) {
                        p {
                            +"Because of: "
                        }
                        val from = max(answer.answer_area - 1, 0)
                        val to = min(answer.answer_area + 19, textSplitted.size - 1)
                        blockQuote("hero") {
                            style = "font-size:14"
                            + "..."
                            for (i in answer.answer_area..to) {
                                    +textSplitted[i]
                            }
                            + "..."
                        }
                    }
                }
            }
        }
    }
}
