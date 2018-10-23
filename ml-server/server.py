from enum import Enum

from flask import app, request, abort
from flask.json import jsonify

from solve import solve


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


@app.route('/solve', methods=['POST'])
def create_task():
    if not request.json or "questions" not in request.json:
        abort(500)
    questions = map(lambda j: Question(j["question"], QuestionType[j["type"]], j["options"]), request.json["questions"])
    text = request.json["text"]
    answers = solve(questions, text)
    return jsonify({'answers': answers}), 200
