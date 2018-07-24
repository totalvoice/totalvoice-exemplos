/**
 * Primeiramente, estamos utilizando a lib da TotalVoice para executar o script
 * Você precisa ter o Maven pra instalar a biblioteca pelo gerenciador de dependências
 * 
 * Vamos simular o seguinte cenário....
 * 4 Ramais -
 * Um deles (4101) será a URA de atendimento
 *
 * Esse script irá criar os ramais e configurar a URA..
 *
 * Você terá 4 ramais (4101, 4102, 4103, 4104):
 *
 * 1º Passo: criar os 4 ramais
 *
 * 2º Passo: criar a URA
 *
 * 3º Passo: vincular a URA ao Ramal 4101
 *
 * Como ficou o cenário do Exemplo:
 *
 * Você está com 4 ramais
 * 4101 - é o ramal da URA
 * 4102 - será utilizado apenas pra teste, vamos ligar com ele
 * 4103 - será o Ramal do time de COMERCIAL
 * 4104 - será o Ramal do time de SUPORTE
 *
 * Passos pro teste:
 *
 * 1º - Abra 3 sessões diferentes do navegador
 *  - Abra um Mozila, Internet Explorer e o Chrome (ou em computadores diferentes ou abas anônimas) por exemplo
 *  - Vamos logar com os ramais: 4102, 4103 e o 4104
 *  - Acesse com os respectivos: email e senha de cada Ramal (criado no JSON acima)
 *  - Com isso, você conseguirá abrir 3 Webphones (4102, 4103 e 4104)
 *
 * 2º - Agora no Webphone do Ramal 4102
 *  - Digite e ligue para o ramal 4101 e aperte pra discar
 *  - Você deverá cair na URA
 *  - Ela vai tocar o TTS e te dar as opções de digitar 1 para o Comercial e 2 para o suporte
 *  - Depois de ter feito isso, a ligação será direcionada para o Ramal
 *
 *
 * Isso é um exemplo básico... agora você consegue ir brincando com a URA e criando novas opções
 *
 * Com URA dinâmica
 * Nesse artigo explicamos como funciona:
 * http://www.totalvoice.com.br/blog/como-automatizar-atendimento-e-integrar-telefonia-com-seu-sistema/
 *
 * Depois você poderá ter um número DID (48328111111 por exemplo) que irá apontar para uma URA da mesma forma que exemplifiquei com o Ramal...
 * Você consegue direcionar o seu número para os nossos servidores depois
 *
 * Com a URA você consegue:
 *  - Processar um TTS - Text to Speech
 *  - Processar um audio
 *  - Fazer uma transferência - para um ramal ou número externo
 *  - Processar uma URA dinâmica - ou seja, nosso sistema vai buscar um JSON com uma URA no seu sistema, em uma URA configurada no seu lado
 *  - Processar um STT (Beta ainda) - Mas processamos o que o usuário falou no telefone e te mandamos em forma de Texto - Speech to Text
 *  - Enviar para uma fila de Atendimento - Exemplo: Você poderia ter uma fila de atendimento no seu setor Comercial, onde teria vários ramais conectados
 * e eles poderiam tocar um de cada vez ou todos ao mesmo tempo.
 */
package project;

import br.com.totalvoice.TotalVoiceClient;
import br.com.totalvoice.api.Central;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Teste {

    public static void main(String args[])
    {
        try {

            /** Versão da lib utilizada 1.0.6 */
            // TROQUE PELO SEU ACCESS-TOKEN
            TotalVoiceClient client = new TotalVoiceClient("access-token");
            Central central = new Central(client);

            System.out.println("Criando ramal 1");
            JSONObject ramal1 = new JSONObject();
            ramal1.put("ramal", 4101);
            ramal1.put("login", "e1@empresa.com.br"); // será o login do Ramal para acessar o painel
            ramal1.put("senha", "senhasecreta1"); // e a senha para acessar o painel
            JSONObject result = central.criarRamal(ramal1);

            JSONObject dados = result.getJSONObject("dados");
            int idRamal = dados.getInt("id");
            System.out.println("ID do RAMAL criado: " + idRamal);

            System.out.println("Criando ramal 2");
            JSONObject ramal2 = new JSONObject();
            ramal2.put("ramal", 4102);
            ramal2.put("login", "e2@empresa.com.br"); // será o login do Ramal para acessar o painel
            ramal2.put("senha", "senhasecreta2"); // e a senha para acessar o painel
            central.criarRamal(ramal2);

            System.out.println("Criando ramal 3");
            JSONObject ramal3 = new JSONObject();
            ramal3.put("ramal", 4103); // será o Ramal do setor "COMERCIAL"
            ramal3.put("login", "e3@empresa.com.br"); // será o login do Ramal para acessar o painel
            ramal3.put("senha", "senhasecreta3"); // e a senha para acessar o painel
            central.criarRamal(ramal3);

            System.out.println("Criando ramal 4");
            JSONObject ramal4 = new JSONObject();
            ramal4.put("ramal", 4104); // será o Ramal do setor "SUPORTE"
            ramal4.put("login", "e4@empresa.com.br"); // será o login do Ramal para acessar o painel
            ramal4.put("senha", "senhasecreta4"); // e a senha para acessar o painel
            central.criarRamal(ramal4);

            /** ####################################################################################################################
             * CRIANDO A URA
             *
             * Nesse trecho enviaremos um JSON seguindo o modelo:
             *
             * {
             *   "nome": "TESTE DE URA",
             *   "dados": [
             *     {
             *       "acao": "tts",
             *       "acao_dados": {
             *         "mensagem": "Olá... obrigado por ligar para a empresa... estou lhe transferindo para o setor responsável!"
             *       }
             *     },
             *     {
             *       "acao": "dinamico",
             *       "acao_dados": {
             *         "url": "https://urldoseusistem.com.br/ura-dinamica"
             *       }
             *     }
             *   ]
             * }
             *
             */
            System.out.println("Criando a URA");

            JSONObject jsonURA = new JSONObject();
            jsonURA.put("nome", "TESTE DE URA");

            List acoes = new ArrayList();

            // TTS
            JSONObject tts = new JSONObject();
            JSONObject acaoDados = new JSONObject();
            acaoDados.put("mensagem", "Olá... obrigado por ligar para a empresa... estou lhe transferindo para o setor responsável!");
            tts.put("acao", "tts");
            tts.put("acao_dados", acaoDados);
            acoes.add(tts);

            // DINAMICO
            JSONObject dinamico = new JSONObject();
            JSONObject acaoDados2 = new JSONObject();
            // Esta URL receberá um POST com os dados da chamada: https://urldoseusistem.com.br/ura-dinamica
            // Ou seja,
            // - você configura esta URL no seu sistema
            // - nós enviamos um POST com os dados a seguir
            // - você nos retorna um JSON com ações da URA para que seja processada aqui do nosso lado...
            //
            // Os dados que enviamos para esta URL são:
            //
            // chamada_recebida_id: ID da Chamada
            // from: numero que está ligando
            // to: numero para qual está ligando
            // ultimo_dtmf: ações do usuário no teclado do telefone
            // tags: Identificação do sistema - ID Externo
            acaoDados2.put("url", "https://urldoseusistem.com.br/ura-dinamica");
            dinamico.put("acao", "dinamico");
            dinamico.put("acao_dados", acaoDados2);
            acoes.add(dinamico);

            jsonURA.put("dados", acoes);

            JSONObject retorno = central.criarUra(jsonURA);
            JSONObject dadosURA = retorno.getJSONObject("dados");
            int idURA = dadosURA.getInt("id");
            System.out.println("ID da URA criada: " + idURA);

            // ####################################################################################################################
            // Atualizamos os dados do primeiro ramal 4101 - com o ID da URA criada
            JSONObject dadosRamal = new JSONObject();
            dadosRamal.put("ura_id", idURA);
            central.atualizarRamal(idRamal, dadosRamal);

            System.out.println("FINALIZADO");

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}