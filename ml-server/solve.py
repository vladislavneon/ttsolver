from model import Answer, AnswerVerdict
from tokenizer import get_tokenized_text
import random

def solve(questions, text):
    random.seed()

    tokenized_text = get_tokenized_text(text)
    max_line = tokenized_text[-1][1]

    answer = []
    for q in questions:
        options = []
        answers = []
        for option in q.options:
            options.append(option)
            answers.append(random.randint(0, 1))
        answer.append(Answer(q.question, AnswerVerdict.ok, options, answers, random(0, max_line)))

    return answer

