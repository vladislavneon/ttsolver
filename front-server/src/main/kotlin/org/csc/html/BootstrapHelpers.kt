package org.csc.html

import kotlinx.html.*

fun HEAD.bootstrapLinks() {
    script {
        src = "https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"
    }
    script {
        src = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
    }
    styleLink("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css")
}

fun SPAN.icon(name: String) {
    i("glyphicon glyphicon-$name")
}

fun BODY.navbar(label: String, vararg buttons: NAV.() -> Unit) {
    return nav("navbar navbar-light bg-light") {
        a(href = "#", classes = "navbar-brand") {
            +label
        }
        for (button in buttons) {
            button()
        }
    }
}

fun FlowContent.container(body: DIV.() -> Unit) {
    div("container") {
        body()
    }
}

fun FlowContent.row(body: DIV.() -> Unit) {
    div("row") {
        body()
    }
}

fun SECTION.wizard(body: DIV.() -> Unit) {
    div("wizard") {
        body()
    }
}

fun FlowContent.submitButton(label: String) {
    ul("list-inline pull-right") {
        li {
            button {
                classes = setOf("btn btn-primary next")
                type = ButtonType.submit

                +label
            }
        }
    }
}
fun FlowContent.downloadButton(label: String, href: String) {
    button {
        classes = setOf("btn")
        a {
            attributes["download"] = ""
            this.href = href
            i {
                classes = setOf("download")
            }
            +label
        }
    }
}