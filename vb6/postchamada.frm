Private Sub Command1_Click()
    Dim RetVal
    RetVal = Shell("C:\Program Files (x86)\Google\Chrome\Application\chrome.exe https://api2.totalvoice.com.br/w3?key=KEY&pop=1&ligar_para=NUMERO", vbMaximizedFocus)
    AppActivate RetVal, 1
End Sub

Private Sub PostChamada_Click()

    Set myMSXML = CreateObject("Microsoft.XmlHttp")
    myMSXML.open "POST", "https://api2.totalvoice.com.br/chamada", False
    myMSXML.setRequestHeader "Content-Type", "application/x-www-form-urlencoded"
    myMSXML.setRequestHeader "Access-Token", "SEU_TOKEN"
    myMSXML.send "numero_origem=numorigemvalue&numero_destino=numdestino_value"
    MsgBox myMSXML.responseText

End Sub
