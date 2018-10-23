import operator
from tokenizer import get_tokenized_text_from_file, tokenize_string
from normalizer import normalize_text, normalize_string

chunk_size = 10
multichoice_recall_threshold = 0.8
singlechoice_recall_threshold = 0.2

def count_recall(text_fragment, goal):
    n = len(goal)
    m = 0
    for word in goal:
        if word in text_fragment:
            m += 1
    return m / n

def find_best_recall(text_with_lines, question, answer):
    best_recall = 0
    best_line_number = -1
    for i in range(len(text_with_lines) - chunk_size + 1):
        line_number = text_with_lines[i][1]
        text = list(map(operator.itemgetter(0), text_with_lines[i : i + chunk_size]))
        recall = count_recall(set(text), set(question)) * count_recall(set(text), set(answer))
        if recall > best_recall:
            best_recall = recall
            best_line_number = line_number
    return (best_recall, best_line_number)


def find_best_single_choice(text_with_lines, question, answers):
    best_recalls = [find_best_recall(text_with_lines, question, answer) for answer in answers]
    best_recalls = [(br[0], br[1], i) for i, br in enumerate(best_recalls)]
    best_recall, second_best_recall = sorted(best_recalls, reverse=True)[0:2]
    return best_recall \
        if best_recall[0] - second_best_recall[0] > singlechoice_recall_threshold else \
        (0, best_recall[1], -1)


def best_recalls(text_with_lines, question, answers):
    best_recalls = [find_best_recall(text_with_lines, question, answer) for answer in answers]
    best_recalls = [(br[0], br[1], i) for i, br in enumerate(best_recalls)]
    return best_recalls

def find_best_multi_choice(text_with_lines, question, answers):
    best_recalls = [find_best_recall(text_with_lines, question, answer) for answer in answers]
    best_recalls = [(br[0], br[1], i) for i, br in enumerate(best_recalls)]
    return list(filter(lambda x: x[0] > multichoice_recall_threshold, best_recalls))


'''
text_with_lines = normalize_text(get_tokenized_text_from_file('text2.txt'))
#print(text_with_lines)
question = normalize_string(tokenize_string('Основатель йоги?'))
print(question)
answer2 = normalize_string(tokenize_string('Патанджали'))
answer1 = normalize_string(tokenize_string('Ганди'))
answer3 = normalize_string(tokenize_string('Конфуций'))
answers = [answer1, answer2, answer3]
print(answers)
print(find_best_single_choice(text_with_lines, question, answers))
print(find_best_multi_choice(text_with_lines, question, answers))
#print(len(text_with_lines))
#print(find_best_recall(text_with_lines[:2000], question, answer))
'''