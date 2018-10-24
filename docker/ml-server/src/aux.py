import json
from model import QuestionType, Question
from tokenizer import get_tokenized_text_from_file
from normalizer import normalize_text

def export_test_questions(filename):
    with open('ml-server/test_data/' + filename + '.json', 'r') as inf, \
            open('ml-server/test_data/' + filename + '_qo.txt', 'w') as ouf:
        j = json.load(inf)
        for q in j:
            ouf.write(q["question"] + '\n')

def json_to_question(filename):
    with open('ml-server/test_data/' + filename, 'r') as inf:
        json_questions = json.load(inf)
        questions = list(map(lambda j: Question(j["question"], QuestionType[j["question_type"]], j["options"]), json_questions))
        return questions


def answers_to_json(answers, filename):
    with open('ml-server/test_data/' + filename, 'w', encoding='utf8') as ouf:
        json.dump(list(map(lambda a: a.__dict__, answers)), ouf, ensure_ascii=False, indent=4)

def export_tokenized_text(filename):
    with open('ml-server/test_data/' + filename + '.tkz', 'w') as ouf:
        for line in normalize_text(get_tokenized_text_from_file(filename)):
            ouf.write(str(line) + '\n')

def json_to_human(filename):
    with open('ml-server/test_data/' + filename, 'r') as inf, \
            open('ml-server/test_data/' + filename + '.human', 'w') as ouf:
        json_questions = json.load(inf)
        for q in json_questions:
            ouf.write('? ' + q["question"] + '\n')
            for o in q['options']:
                ouf.write('! ' + o + '\n')
            ouf.write('\n')

