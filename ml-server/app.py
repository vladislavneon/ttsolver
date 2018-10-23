import json

from flask import request, abort

from flask import Flask

from model import Question, QuestionType
from solve import solve

app = Flask(__name__)


@app.route('/solve', methods=['POST'])
def create_task():
    json_req = json.loads(request.data.decode())
    if not json_req or "questions" not in json_req:
        abort(500)
    questions = list(map(lambda j: Question(j["question"], QuestionType[j["question_type"]], j["options"]), json_req["questions"]))
    text = json_req["text"]
    answers = solve(questions, text)
    return json.dumps(list(map(lambda a: a.__dict__, answers))), 200
