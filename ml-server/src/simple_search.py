import operator


multichoice_recall_threshold = 0.3
singlechoice_recall_threshold_delta = 0.05
singlechoice_recall_threshold = 0.2


def count_recall(text_fragment, goal):
    if not len(goal):
        return 0
    n = len(goal)
    m = 0
    for word in goal:
        if word in text_fragment:
            m += 1
    return m / n


def find_best_recall(text_with_lines, question, answer, q_weight=1, chunk_size=10):
    a_weight = 2 - q_weight
    best_recall = 0
    best_line_number = -1
    for i in range(len(text_with_lines) - chunk_size + 1):
        line_number = text_with_lines[i][1]
        text = list(map(operator.itemgetter(0), text_with_lines[i : i + chunk_size]))
        recall = \
            (count_recall(set(text), set(question)) ** q_weight) * (count_recall(set(text), set(answer)) ** a_weight)
        if recall > best_recall:
            best_recall = recall
            best_line_number = line_number
    return (best_recall, best_line_number)


def find_best_single_choice(text_with_lines, question, answers):
    best_recalls = [find_best_recall(text_with_lines, question, answer) for answer in answers]
    best_recalls = [(br[0], br[1], i) for i, br in enumerate(best_recalls)]
    best_recall, second_best_recall = sorted(best_recalls, reverse=True)[0:2]
    return best_recall \
        if best_recall[0] - second_best_recall[0] > singlechoice_recall_threshold_delta \
           and best_recall[0] > singlechoice_recall_threshold else \
        (0, best_recall[1], -1)


def best_recalls(text_with_lines, question, answers):
    best_recalls = [find_best_recall(text_with_lines, question, answer) for answer in answers]
    best_recalls = [(br[0], br[1], i) for i, br in enumerate(best_recalls)]
    return best_recalls


def find_best_multi_choice(text_with_lines, question, answers):
    best_recalls = [find_best_recall(text_with_lines, question, answer, q_weight=1.2, chunk_size=50) for answer in answers]
    best_recalls = [(br[0], br[1], i) for i, br in enumerate(best_recalls)]
    return list(filter(lambda x: x[0] > multichoice_recall_threshold, best_recalls))


def find_best_place(text_with_lines, question, answers, chunk_size=30):
    question_summary = question
    for answer in answers:
        question_summary.extend(answer)
    best_recall = 0
    best_line_number = -1
    for i in range(len(text_with_lines) - chunk_size + 1):
        line_number = text_with_lines[i][1]
        text = list(map(operator.itemgetter(0), text_with_lines[i : i + chunk_size]))
        recall = count_recall(set(text), set(question))
        if recall > best_recall:
            best_recall = recall
            best_line_number = line_number
    return (best_recall, best_line_number)
