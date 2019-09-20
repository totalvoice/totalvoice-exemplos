import requests 
import json


url = 'https://api2.totalvoice.com.br/sms/1234'


headers = {'content-type': 'application/json', 'Access-Token':'seu-token'}

r = requests.get(url, headers=headers)

print(r.content)
