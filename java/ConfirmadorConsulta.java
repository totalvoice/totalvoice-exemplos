import br.com.totalvoice.TotalVoiceClient;
import br.com.totalvoice.api.Composto;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfirmadorConsulta
{
    private static final String MEU_TOKEN = "SEU_TOKEN";
    private TotalVoiceClient cliente = new TotalVoiceClient(MEU_TOKEN);

    public int confirmarConsulta(String numero, String nome, String data) throws Exception
    {
		// Cria um Composto da TotalVoice
        Composto composto = new Composto(cliente);
		
		// Criamos um JSON para carregar esse composto
        JSONObject jsonComposto = new JSONObject();

		// Número para qual o composto será enviado
        jsonComposto.put("numero_destino", numero);
		
		// Se quisermos gravar a ligação
        jsonComposto.put("gravar_audio", false);
		
        // Pode colocar tags aqui para utilizar depois
        jsonComposto.put("tags", "TAG_PERSONALIZADA");

		// Criamos a array de passos do nosso composto
        JSONArray dados = new JSONArray();

        // --------------
        // TTS inicial
        // --------------
        JSONObject ttsAcao = new JSONObject();
        JSONObject ttsAcaoDados = new JSONObject();

        ttsAcaoDados.put("mensagem", nome + " você tem um atendimento na data " + data + " , aperte 1 para confirmar ou 2 para cancelar");
        // Resposta usuario = true para a chamada aguardar um digito do usuário
        ttsAcaoDados.put("resposta_usuario", "true");

        ttsAcao.put("acao", "tts");
        ttsAcao.put("acao_dados", ttsAcaoDados);

        dados.put(ttsAcao);

        // --------------
        // Clicou em Confirmar
        // --------------
        JSONObject ttsConfirmou = new JSONObject();
        JSONObject ttsConfirmouDados = new JSONObject();

        ttsConfirmouDados.put("mensagem", "obrigado por confirmar sua consulta");

        // Aqui avisamos que só entra nesse TTS quem discou Opção 1
        ttsConfirmou.put("opcao", 1);
        ttsConfirmou.put("acao", "tts");
        ttsConfirmou.put("acao_dados", ttsConfirmouDados);

        dados.put(ttsConfirmou);

        // --------------
        // Clicou em Cancelar
        // --------------
        JSONObject ttsCancelou = new JSONObject();
        JSONObject ttsCancelouDados = new JSONObject();

        ttsCancelouDados.put("mensagem", "que pena que você cancelou");

        ttsCancelou.put("opcao", 2);
        ttsCancelou.put("acao", "tts");
        ttsCancelou.put("acao_dados", ttsCancelouDados);

        dados.put(ttsCancelou);

		// Coloca todos os passos no composto
        jsonComposto.put("dados", dados);

        // --------------
        // Envia o composto e trata o resultado
        // --------------
        JSONObject result = composto.enviar(jsonComposto);
        System.out.println(result);

        if (result.getBoolean("sucesso"))
        {
            System.out.println("Composto enviado com sucesso");
            return result.getJSONObject("dados").getInt("id");
        }

        return -1;
    }

	// Método de exemplo para recuperar os dados de um composto
    public int conferirConfirmação(int id) throws Exception
    {
        Composto composto = new Composto(cliente);
        JSONObject compostoJson = composto.buscar(id);
		
		// Print do resultado
		System.out.println(compostoJson);
		
		// Se deu sucesso
        if (compostoJson.getBoolean("sucesso"))
        {
			// Pega as informações do Composto
            JSONObject dados = compostoJson.getJSONObject("dados");
			
			// Resposta null se não respondeu
            if (dados.isNull("resposta"))
            {
                return -1;
            }

			// Retorna a resposta
            return dados.getInt("resposta");
        }

        return -1;
    }

    public static void main(String[] args)
    {
        try {
            System.out.println("Iniciando confirmador de consultas");

			// Cria o objeto principal
            ConfirmadorConsulta confirmador = new ConfirmadorConsulta();

			// Chama função que envia um composto
            int idConfirmacao = confirmador.confirmarConsulta("484848484848", "Guilherme", "30 de Março");

			// Verifica se o composto foi enviado com sucesso
            if (idConfirmacao > 0)
            {
                // Espera 30 segundos e confere o número digitado pelo cliente
                Thread.sleep(30 * 1000);

				// Pega a resposta digitada
                int resposta = confirmador.conferirConfirmação(idConfirmacao);
                System.out.println("Resposta: " + resposta);
            }
            else
            {
                throw new Exception("Erro ao enviar confirmação");
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
