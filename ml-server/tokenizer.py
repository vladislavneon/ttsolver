import re
import json

def get_tokenized_text(text):
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
    return get_tokenized_text(text)

print(get_tokenized_text_from_file('text4.txt'))


def export_test_questions(filename):
    with open('ml-server/test_data/' + filename + '.json', 'r') as inf, \
         open('ml-server/test_data/' + filename + '_qo.txt', 'w') as ouf:
        j = json.load(inf)
        for q in j:
            ouf.write(q["question"] + '\n')
