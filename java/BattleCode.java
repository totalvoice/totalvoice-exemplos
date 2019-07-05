package br.com.totalvoice;

import br.com.totalvoice.api.Sms;
import br.com.totalvoice.api.Tts;
import org.json.JSONObject;

public class Main {
    
    public static void main(String args[]) {
        
        try {
            TotalVoiceClient client = new TotalVoiceClient("carregar_do_env");
            Tts tts = new Tts(client);
	    String numero = "numero_destino";
            JSONObject result = tts.enviar(numero, "Olá! Seja bem vindo a banca da Zênvia. Aproveite o evento e boa sorte no quízz, você vai receber um sms com o link do quízz.");
            System.out.println(result);

	    Sms sms = new Sms(client);
	    String mensagem = "Olá, o link do quizz é:+carregar_do_env";
            JSONObject result = sms.enviar(numero, mensagem);
            System.out.println(result);

        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
