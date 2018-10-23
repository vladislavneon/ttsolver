package org.csc.utils

import org.csc.ml.Question
import org.csc.ml.QuestionType

import java.util.ArrayList

object InputTestParser {
    fun parseInputTest(inputText: String): List<Question> {
        val result = ArrayList<Question>()
        val lines = inputText.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var questionText: String? = null
        var questionType: QuestionType? = null
        val options = ArrayList<String>()

        for (i in lines.indices) {
            val line = lines[i]
            when (line[0]) {
                '?' -> {
                    if (i != 0) {
                        result.add(Question(questionText!!, questionType!!, ArrayList(options)))
                    }
                    questionText = line.substring(1)
                    questionType = null
                    options.clear()
                }
                '!' -> {
                    questionType = QuestionType.single
                    options.add(line.substring(1))
                }
                '+' -> {
                    questionType = QuestionType.multi
                    options.add(line.substring(1))
                }
                else -> throw IllegalArgumentException("Тестовые данные введены в некорректном формате")
            }
        }
        result.add(Question(questionText!!, questionType!!, ArrayList(options)))
        return result
    }
}
