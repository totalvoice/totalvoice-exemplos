# VB6 - Post de chamada
Acompanhe abaixo como realizar post de chamada com Visual Basic Studio

## Alterar valores das tags
Altere o valor de "SEU_TOKEN" pelo seu Access-Token que consta na página inicial do painel da TotalVoice.

```c
myMSXML.setRequestHeader "Access-Token", "SEU_TOKEN"
```

Na linha abaixo altere o 'numorigemvalue' pelo Número de origem da chamada e altere 'numdestino_value' pelo número de destino que receberá a chamada.
```c
myMSXML.send "numero_origem=numorigemvalue&numero_destino=numdestino_value"
```

