local SSID = "xxxxxx"
local SSID_PASSWORD = "xxxxxx"
-- configure ESP as a station
wifi.setmode(wifi.STATION)
wifi.sta.config(SSID,SSID_PASSWORD)
wifi.sta.autoconnect(1)
local TOTALVOICE_TOKEN = "SEU_TOKEN"
local HOST = "api2.totalvoice.com.br"
local URI = "/sms"
function build_post_request(host, uri, data_table)
     local data = ""
    
     for param,value in pairs(data_table) do
          data = data .. param.."="..value.."&"
     end
     request = "POST "..uri.." HTTP/1.1\r\n"..
     "Access-Token: "..TOTALVOICE_TOKEN.."\r\n"..
	 "Host: "..host.."\r\n"..
     "Connection: close\r\n"..
     "Content-Type: application/x-www-form-urlencoded\r\n"..
     "Content-Length: "..string.len(data).."\r\n"..
     "\r\n"..
     data
     print(request)
    
     return request
end
local function display(sck,response)
     print(response)
end

local function send_sms(to,body)
     local data = {
      mensagem = string.gsub(body," ","+"),
      numero_destino = to
     }
    
     socket = net.createConnection(net.TCP,0)
     socket:on("receive",display)
     socket:connect(80,HOST)
     socket:on("connection",function(sck)
       
          local post_request = build_post_request(HOST,URI,data)
          sck:send(post_request)
     end)    
end
function check_wifi()
local ip = wifi.sta.getip()
if(ip==nil) then
   print("Connecting...")
else
  tmr.stop(0)
  print("Connected to AP!")
  print(ip)
  send_sms("NUMERO_DESTINO","MENSAGEM")
 end
end
tmr.alarm(0,7000,1,check_wifi)