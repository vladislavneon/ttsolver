package org.csc

import java.io.File
import java.nio.file.Path

object Resources {
    fun getText(file: String) = getText(File(file).toPath())
    fun getText(file: Path) = Resources::class.java.classLoader.getResource(file.toString()).readText()
}