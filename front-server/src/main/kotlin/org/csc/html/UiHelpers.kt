package org.csc.html

import kotlinx.html.BODY
import kotlinx.html.a
import kotlinx.html.style

fun BODY.ttsNavbar() {
    navbar("TT Solver", {
        a(href = "/reset", classes = "navbar-brand") {
            style = "font-size:14"
            +"Reset"
        }
    })

}