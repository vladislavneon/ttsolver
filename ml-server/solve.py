from model import Answer, AnswerVerdict
import random

def solve(questions, text):
    random.seed()

    answer = []
    for q in questions:
        options = []
        answers = []
        for option in q.options:
            options.append(option)
            answers.append(random.randint(0, 1))
        answer.append(Answer(q.question, AnswerVerdict.ok, options, answers, 0))

    return answer

