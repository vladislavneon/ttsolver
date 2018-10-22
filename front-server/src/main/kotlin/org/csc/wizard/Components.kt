package org.csc.wizard

import kotlinx.html.*
import org.csc.html.*

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

fun BODY.wizardSteps(step: WizardStep) {
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
                        WizardStep.UploadPDF -> {
                            form("/wizard/uploadPDF", method = FormMethod.post, encType = FormEncType.multipartFormData) {
                                div("tab-content") {
                                    stepBody("Upload .PDF with data", step.ordinal == 0, {
                                        br()
                                        fileInput {
                                            name = step.fileName
                                            size = (30 * 1024 * 1024).toString()
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
                                            name = step.fileName
                                            size = (30 * 1024 * 1024).toString()
                                            required = true
                                        }
                                    }, { submitButton("Save and next") })
                                }
                            }
                        }
                        WizardStep.Completed -> {
                            +"Done!"
                        }
                    }
                }
            }
        }
    }
}
