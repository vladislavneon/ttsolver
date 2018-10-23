import operator

chunk_size = 100

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
