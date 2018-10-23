from solve import solve
from model import Question, QuestionType

a = solve([Question('Wtf?', QuestionType.multi, ['all right', 'fuck you', 'good morning'])], 'text')
print(a[0].question)
print(a[0].verdict)
print(a[0].options)
print(a[0].answers)
print(a[0].answer_area)
