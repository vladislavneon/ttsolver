import json

def export_test_questions(filename):
    with open('ml-server/test_data/' + filename + '.json', 'r') as inf, \
            open('ml-server/test_data/' + filename + '_qo.txt', 'w') as ouf:
        j = json.load(inf)
        for q in j:
            ouf.write(q["question"] + '\n')