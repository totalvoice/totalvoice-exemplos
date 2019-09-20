# importing the requests library 
import requests 
import json

url = 'https://api2.totalvoice.com.br/sms'

payload = {'numero_destino': '48996955411', 'mensagem':'Teste Hang'}

headers = {'content-type': 'application/json', 'Access-Token':'seu-token'}

r = requests.post(url, data=json.dumps(payload), headers=headers)

print(r.content)
