from model import Answer, AnswerVerdict, Question, QuestionType
from tokenizer import tokenize_text, tokenize_string
from normalizer import normalize_text, normalize_string
from simple_search import find_best_single_choice, find_best_multi_choice, best_recalls, find_best_place
import random

def solve(questions, text):
    random.seed()

    tokenized_text = tokenize_text(text)
    text_with_lines = normalize_text(tokenized_text)
    answers = []

    for question in questions:
        question_text = normalize_string(tokenize_string(question.question))
        question_answers = list(map(lambda x: normalize_string(tokenize_string(x)), question.options))
        if question.question_type == QuestionType.single:
            best_choice = find_best_single_choice(text_with_lines, question_text, question_answers)
            choices = [0] * len(question.options)
            line_number = best_choice[1]
            #choices = [(br[0], br[1]) for br in best_recalls(text_with_lines, question_text, question_answers)]
            if best_choice[2] != -1:
                choices[best_choice[2]] = 1
                answers.append(Answer(question.question, AnswerVerdict.ok, question.options, choices, line_number))
            else:
                best_place = find_best_place(text_with_lines, question_text, question_answers)[1]
                if best_place != -1:
                    answers.append(Answer(question.question, AnswerVerdict.found_area, question.options, choices, best_place))
                else:
                    answers.append(Answer(question.question, AnswerVerdict.fail, question.options, choices, -1))
        else:
            best_choices = find_best_multi_choice(text_with_lines, question_text, question_answers)
            choices = [0] * len(question.options)
            for choice in best_choices:
                id = choice[2]
                choices[id] = 1
            #choices = [(br[0], br[1]) for br in best_recalls(text_with_lines, question_text, question_answers)]
            best_place = find_best_place(text_with_lines, question_text, question_answers)[1]
            if best_choices:
                answers.append(Answer(question.question, AnswerVerdict.ok, question.options, choices, best_place))
            elif best_place != -1:
                answers.append(Answer(question.question, AnswerVerdict.found_area, question.options, choices, best_place))
            else:
                answers.append(Answer(question.question, AnswerVerdict.fail, question.options, choices, -1))

    return answers

