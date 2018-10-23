package org.csc.utils;

import org.csc.ml.Question;
import org.csc.ml.QuestionType;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

public class InputTestParser {
    public static List<Question> parseInputTest(String inputText) {
        List<Question> result = new ArrayList<>();
        String[] lines = inputText.split("\n");
        String questionText = null;
        QuestionType questionType = null;
        List<String> options = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            switch (line.charAt(0)) {
                case '?':
                    if (i != 0) {
                        result.add(new Question(questionText, questionType, new ArrayList<>(options)));
                    }
                    questionText = line.substring(1);
                    questionType = null;
                    options.clear();
                    break;
                case '!':
                    questionType = QuestionType.single;
                    options.add(line.substring(1));
                    break;
                case  '+':
                    questionType = QuestionType.multi;
                    options.add(line.substring(1));
                    break;
                default: throw new IllegalArgumentException("Тестовые данные введены в некорректном формате");
            }
        }
        result.add(new Question(questionText, questionType, new ArrayList<>(options)));
        return result;
    }
}
