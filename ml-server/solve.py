import json
import random

random.seed()

def solve(questions_json, text):

    answers_json = {"answers": []}

    for q in questions_json["questions"]:
        cur_answer = {
                        "question": "",
                        "verdict": "",
                        "options": [],
                        "answers": [],
                        "answer_area": 0
                    }
        cur_answer["question"] = q["question"]
        cur_answer["verdict"] = "ok"
        cur_answer["answer_area"] = 0
        for option in q["options"]:
            cur_answer["options"].append(option)
            cur_answer["answers"].append(random.randint(0, 1))
        answers_json["answers"].append(cur_answer)

    return answers_json
    