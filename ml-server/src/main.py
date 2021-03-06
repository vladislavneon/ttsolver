from solve import solve
from model import Question, QuestionType
from aux import json_to_question, answers_to_json, export_tokenized_text, json_to_human
from tokenizer import get_text_from_file

questions = json_to_question('3_tests.json')
text = get_text_from_file('text3.txt')

a = solve(questions, text)

answers_to_json(a, 'out3.json')

#json_to_human('1_tests.json')

#export_tokenized_text('text2.txt')