package org.csc.pdf

import java.io.File

interface PDFRecognizer {
    companion object : PDFRecognizer by PdfBoxRecognizer

    fun recognize(file: File): String
    fun recognize(content: ByteArray): String
}