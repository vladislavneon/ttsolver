package org.csc.html

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.content.PartData
import io.ktor.http.content.readAllParts
import io.ktor.http.content.streamProvider
import io.ktor.pipeline.PipelineContext
import io.ktor.request.receiveMultipart
import io.ktor.response.respondText
import kotlinx.html.TagConsumer
import kotlinx.html.stream.createHTML

suspend fun PipelineContext<Unit, ApplicationCall>.html(func: TagConsumer<String>.() -> Unit) {

    call.respondText(contentType = ContentType.parse("text/html")) {
        createHTML().apply {
            func()
        }.finalize()
    }
}

suspend fun ApplicationCall.getFile(name: String): Pair<ByteArray, String> {
    val multipart = receiveMultipart()
    val parts = multipart.readAllParts()
    val file = (parts.single { it.name == name && it is PartData.FileItem } as PartData.FileItem)
    val bytes = file.streamProvider().readBytes()
    parts.forEach { it.dispose() }
    return bytes to (file.originalFileName ?: "")
}