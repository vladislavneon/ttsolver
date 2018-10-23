package org.csc.session

import java.io.File
import java.util.*

object FileManager {
    private val tmpDir = File(System.getProperty("java.io.tmpdir"))

    fun getPdfRawFile(uuid: UUID) = getFile(uuid, "pdf_raw")
    fun createPdfRawFile(uuid: UUID, content: ByteArray) = createFile(uuid, "pdf_raw", content)

    fun getPdfTextFile(uuid: UUID) = getFile(uuid, "pdf_txt")
    fun createPdfTextFile(uuid: UUID, content: ByteArray) = createFile(uuid, "pdf_txt", content)

    fun getJsonFile(uuid: UUID) = getFile(uuid, "json")
    fun createJsonFile(uuid: UUID, content: ByteArray) = createFile(uuid, "json", content)

    fun getResultFile(uuid: UUID) = getFile(uuid, "res")
    fun createResultFile(uuid: UUID, content: ByteArray) = createFile(uuid, "res", content)

    private fun getFile(uuid: UUID, type: String): File = File(tmpDir, "${uuid}_$type")
    private fun createFile(uuid: UUID, type: String, content: ByteArray): File = File(tmpDir, "${uuid}_$type").also {
        it.createNewFile()
        it.writeBytes(content)
    }
}