package org.csc.pdf

import org.apache.pdfbox.io.RandomAccessBuffer
import org.apache.pdfbox.io.RandomAccessFile
import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.nio.ByteBuffer

object PdfBoxRecognizer : PDFRecognizer {
    override fun recognize(file: File): String {
        val parser = PDFParser(RandomAccessFile(file, "r"))
        parser.parse()
        val result = PDFTextStripper().getText(PDDocument(parser.document))
        print(result)
        return result
    }

    override fun recognize(content: ByteArray): String {
        val parser = PDFParser(RandomAccessBuffer(content))
        parser.parse()
        val result = PDFTextStripper().getText(PDDocument(parser.document))
        print(result)
        return result
    }
}