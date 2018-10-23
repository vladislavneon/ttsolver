import re
import json


def tokenize_text(text):
    text = text.split(sep='\n')
    text_with_lines = []
    for i, line in enumerate(text):
        line = line.split()
        line = list(map(lambda x: (x, i), line))
        text_with_lines.extend(line)

    for i in range(len(text_with_lines) - 1):
        cur_word = text_with_lines[i][0]
        if len(cur_word) > 1 and cur_word[-1] == '-':
            next_word = text_with_lines[i + 1][0]
            text_with_lines[i] = (cur_word[:-1] + next_word, text_with_lines[i][1])
            text_with_lines[i + 1] = ('', text_with_lines[i + 1][1])

    text_words_only = []
    for word, line_number in text_with_lines:
        word = word.lower()
        word = re.sub(r'\W', '', word)
        if not word:
            continue
        text_words_only.append((word.lower(), line_number))
    return text_words_only


def get_tokenized_text_from_file(filename):
    with open('ml-server/test_data/' + filename, 'r') as inf:
        text = ''.join(inf.readlines())
    return tokenize_text(text)

def get_text_from_file(filename):
    with open('ml-server/test_data/' + filename, 'r') as inf:
        text = ''.join(inf.readlines())
    return text

def tokenize_string(qa):
    splitted_question = qa.strip().split()
    tokenized_question = []
    for token in splitted_question:
        token = token.lower()
        token = re.sub(r'\W', '', token)
        if len(token) < 3:
            continue
        tokenized_question.append(token)
    return tokenized_question

