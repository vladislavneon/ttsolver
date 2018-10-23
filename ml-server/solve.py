from model import Answer, AnswerVerdict
from tokenizer import tokenize_text
import random

def solve(questions, text):
    random.seed()

    tokenized_text = tokenize_text(text)
    max_line = tokenized_text[-1][1]

    answer = []
    for q in questions:
        options = []
        answers = []
        for option in q.options:
            options.append(option)
            answers.append(random.randint(0, 1))
        answer.append(Answer(q.question, AnswerVerdict.ok, options, answers, random.randint(0, max_line)))

    return answer

