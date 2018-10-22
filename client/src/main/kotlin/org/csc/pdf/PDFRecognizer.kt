package org.csc.pdf

import java.io.File

interface PDFRecognizer {
    fun recognize(file: File): String
    fun recognize(content: ByteArray): String
}