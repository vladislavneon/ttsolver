package org.csc.pdf

import org.apache.pdfbox.io.RandomAccessBuffer
import org.apache.pdfbox.io.RandomAccessFile
import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File

object PdfBoxRecognizer : PDFRecognizer {
    override fun recognize(file: File): String {
        val parser = PDFParser(RandomAccessFile(file, "r"))
        parser.parse()
        return PDFTextStripper().getText(PDDocument(parser.document))
    }

    override fun recognize(content: ByteArray): String {
        val parser = PDFParser(RandomAccessBuffer(content))
        parser.parse()
        return PDFTextStripper().getText(PDDocument(parser.document))
    }
}