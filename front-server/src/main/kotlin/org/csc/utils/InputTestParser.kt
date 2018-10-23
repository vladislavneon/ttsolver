package org.csc.utils

import org.csc.ml.Question
import org.csc.ml.QuestionType

import java.util.ArrayList

object InputTestParser {
    fun parseInputTest(inputText: String): List<Question> {
        val result = ArrayList<Question>()
        val lines = inputText.split("\n").dropLastWhile { it.isEmpty() }.toTypedArray()
        var questionText: String? = null
        var questionType: QuestionType? = null
        val options = ArrayList<String>()

        for (line in lines) {
            when (line.first()) {
                '?' -> {
                    if (questionText != null && questionType != null ) {
                        result.add(Question(questionText, questionType, ArrayList(options)))
                    }
                    questionText = line.substring(1).trim()
                    questionType = null
                    options.clear()
                }
                '!' -> {
                    questionType = QuestionType.single
                    options.add(line.substring(1).trim())
                }
                '+' -> {
                    questionType = QuestionType.multi
                    options.add(line.substring(1).trim())
                }
                else -> {}
            }
        }
        result.add(Question(questionText!!, questionType!!, ArrayList(options)))
        return result
    }
}
