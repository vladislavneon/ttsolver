import pymorphy2
from stemmer import Stemmer


morpher = pymorphy2.MorphAnalyzer()
stemmer = Stemmer()


def normalize_text(text):
    normalized_text = []
    for word, line in text:
        if len(word) > 2:
            word = morpher.parse(word)[0].normal_form
            normalized_text.append((word, line))
    return normalized_text


def stem_text(text):
    normalized_text = []
    for word, line in text:
        if len(word) > 2:
            word = stemmer.stem(word)
            normalized_text.append((word, line))
    return normalized_text


def normalize_string(qa):
    normalized_string = []
    for word in qa:
        word = morpher.parse(word)[0].normal_form
        normalized_string.append(word)
    return normalized_string


def stem_string(qa):
    normalized_string = []
    for word in qa:
        word = stemmer.stem(word)
        normalized_string.append(word)
    return normalized_string


'''
print(normalize_text([('клумбы', 861), ('баскетбольная', 861), ('площадка', 861), ('качели', 862), ('скульптуры', 862), ('лужайки', 862), ('шведская', 862), ('стенка', 862), ('плюс', 863), ('минус', 863), ('интересно', 863)]))
print(stem_text([('клумбы', 861), ('баскетбольная', 861), ('площадка', 861), ('качели', 862), ('скульптуры', 862), ('лужайки', 862), ('шведская', 862), ('стенка', 862), ('плюс', 863), ('минус', 863), ('интересно', 863)]))
'''
