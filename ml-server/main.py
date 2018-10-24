from solve import solve
from model import Question, QuestionType
from aux import json_to_question, answers_to_json, export_tokenized_text
from tokenizer import get_text_from_file

questions = json_to_question('1_tests.json')
text = get_text_from_file('text1.txt')

a = solve(questions, text)

answers_to_json(a, 'out1.json')

#export_tokenized_text('text1.txt')