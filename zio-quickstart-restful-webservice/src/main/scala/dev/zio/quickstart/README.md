## Новая ручка /score

POST /score -d '{"from": "39", "to": "101", "sum": 10.6}' 

Ручка проверяет from и to на наличие в файле blacklisted.txt. 
Если находит соответствие, то возвращает {"success":false}, если не находит, то добавляет новую транзакцию в in-memory TransactionsRepo 
и возвращает {"success":true}.

GET /score

Возвращает список транзакций