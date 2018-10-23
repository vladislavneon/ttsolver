from enum import Enum


class QuestionType(Enum):
    single = 0
    multi = 1


class Question(object):
    def __init__(self, question, question_type, options):
        self.question = question
        self.question_type = question_type
        self.options = options


class AnswerVerdict(object):
    ok = 0
    found_area = 1
    fail = 2


class Answer(object):
    def __init__(self, question, verdict, options, answers, answer_area):
        self.question = question
        self.verdict = verdict
        self.options = options
        self.answers = answers
        self.answer_area = answer_area