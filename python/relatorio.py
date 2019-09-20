import requests 
import json


url = 'https://api2.totalvoice.com.br/sms/relatorio?data_inicio=2019-09-18&data_fim=2019-09-20'


headers = {'content-type': 'application/json', 'Access-Token':'seu-token'}

r = requests.get(url, headers=headers)

print(r.content)
