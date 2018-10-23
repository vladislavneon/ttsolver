package org.csc.ml

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.csc.utils.Json
import java.net.URL
import kotlin.reflect.KClass


object MlSolverServerConnector : MlSolver {
    private val url = URL("http://localhost:5000/solve").toURI()
    private val client = HttpClientBuilder.create().build()

    override fun solve(text: String, questions: List<Question>): List<Answer> {
        val post = HttpPost(url).also {
            it.entity = ByteArrayEntity(Json.writeValueAsString(mapOf("text" to text, "questions" to questions)).toByteArray(Charsets.UTF_8))
        }
        val response = client.execute(post)
        val responseBytes = response.entity.content.readBytes()
        val answers = Json.readCollectionValue(String(responseBytes), List::class as KClass<List<Answer>>, Answer::class)
        return answers
    }
}