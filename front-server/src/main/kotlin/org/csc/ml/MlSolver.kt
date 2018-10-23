package org.csc.ml


enum class QuestionType {
    single,
    multi
}

data class Question(val question: String, val type: QuestionType, val options: List<String>)

enum class AnswerVerdict {
    ok,
    found_area,
    fail
}

data class Answer(val question: String, val verdict: AnswerVerdict, val options: List<String>,
                  val answers: List<String>, val answer_area: Int)

interface MlSolver {
    companion object : MlSolver by MlSolverServerConnector

    fun solve(text: String, questions: List<Question>): List<Answer>
}