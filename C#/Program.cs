using System;
using System.Collections.Generic;
using System.Net;
using System.IO;

/*
Exemplo de post no método de criação de sub contas da TotalVoice
DotNet -> 2.2.104
Você deve substituir o arquivo myjson.json pelo seu arquivo json
*/

namespace csharpEx
{
    class Program
    {
        static void Main(string[] args)
        {
            WebRequest request = WebRequest.Create("https://api.totalvoice.com.br" + "/conta");
            request.Method = "POST";
            request.Headers.Add("Access-Token","SEU TOKEN");
            using (StreamReader r = new StreamReader("myjson.json"))
            {
                string body = r.ReadToEnd();
                Console.WriteLine(body);

                if (body != null) {
                    byte[] bytes = System.Text.Encoding.UTF8.GetBytes(body);

                    request.ContentLength = bytes.Length;
                    System.IO.Stream reqStream = request.GetRequestStream();
                    reqStream.Write(bytes, 0, bytes.Length);
                    try {
                        reqStream.Close();
                    }catch(Exception e) {
                        Console.WriteLine(e);
                    }
                }

                HttpWebResponse response = null;
                try {
                    response = (HttpWebResponse)request.GetResponse();
                }
                catch (WebException ex) {
                    if (ex.Status == WebExceptionStatus.ProtocolError && ex.Response != null) {
                        response = (HttpWebResponse)ex.Response;

                    }
                }

                System.IO.Stream resStream = response.GetResponseStream();
                System.IO.StreamReader reader = new System.IO.StreamReader(resStream);

                String responseFromServer = reader.ReadToEnd();

                reader.Close();
                resStream.Close();
                response.Close();

                Console.WriteLine("Evoline Response: " + responseFromServer);
            }  
            
        }
    }
}
