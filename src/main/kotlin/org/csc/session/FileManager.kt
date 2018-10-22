package org.csc.session

import java.io.File
import java.util.*

object FileManager {
    private val tmpDir = File(System.getProperty("java.io.tmpdir"))

    fun getFile(uuid: UUID, type: String): File = File(tmpDir, type)
    fun createFile(uuid: UUID, type: String, content: ByteArray): File = File(tmpDir, type).also {
        it.createNewFile()
        it.writeBytes(content)
    }
}