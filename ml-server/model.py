from enum import Enum


class QuestionType(Enum):
    single = 1
    multi = 2


class Question(object):
    def __init__(self, question, type, options):
        self.question = question
        self.type = type
        self.options = options


class AnswerVerdict(object):
    ok = 1
    found_area = 2
    fail = 3


class Answer(object):
    def __init__(self, question, verdict, options, answers, answer_area):
        self.question = question
        self.verdict = verdict
        self.options = options
        self.answers = answers
        self.answer_area = answer_area