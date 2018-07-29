<?php
/** 
 * PHP +5.4
 * Primeiramente: considero que você já instalou a nossa lib com o Composer
 * Nesse exemplo: SUBSTITUA a string 'seu-access-token' pelo token que tem no seu painel...
 * 
 * Vamos simular o seguinte cenário....
 * 4 Ramais -
 * Um deles (4101) será a URA de atendimento
 * 
 * Esse script faz tudo pra você.. rodando o comando: php exemplo_ura.php
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
require 'vendor/autoload.php';

use TotalVoice\Client as TotalVoiceClient;

// NÃO ESQUEÇA DE SUBSTITUIR PELO SEU ACCESS_TOKEN
$client = new TotalVoiceClient('seu-access-token');

// ################################################################
// ################################################################
// 1º passo vamos criar os 3 ramais
// IMPORTANTE: ALTERE OS E-MAILS E SENHA QUE ESTAO NESTE EXEMPLO PARA SEGURANCA - ESTES SAO APENAS DE EXEMPLO
$ramal1 = [
    'ramal' => 4101,
    'login' => 'e1@empresa.com.br', // será o login do Ramal para acessar o painel
    'senha' => 'senhasecreta1', // e a senha para acessar o painel
    'ligacao_externa' => true,
    'ligacao_celular' => true,
    'gravar_audio' => true,
    'acesso_gravacoes' => true
];
print "Criando o ramal 1 \n";
$response = $client->central->criarRamal($ramal1);
$content = json_decode($response->getContent(), true);
$idDoRamal1 = $content['dados']['id']; // vou armazenar o ID do ramal 1 (4100) para vincular a URA nele depois

$ramal2 = [
    'ramal' => 4102,
    'login' => 'e2@empresa.com.br', // será o login do Ramal para acessar o painel
    'senha' => 'senhasecreta2', // e a senha para acessar o painel
    'ligacao_externa' => true,
    'ligacao_celular' => true,
    'gravar_audio' => true,
    'acesso_gravacoes' => true
];
print "Criando o ramal 2 \n";
$client->central->criarRamal($ramal2);

$ramal3 = [
    'ramal' => 4103,
    'login' => 'e3@empresa.com.br', // será o login do Ramal para acessar o painel
    'senha' => 'senhasecreta3', // e a senha para acessar o painel
    'ligacao_externa' => true,
    'ligacao_celular' => true,
    'gravar_audio' => true,
    'acesso_gravacoes' => true
];
print "Criando o ramal 3 \n";
$client->central->criarRamal($ramal3);

$ramal4 = [
    'ramal' => 4104,
    'login' => 'e4@empresa.com.br', // será o login do Ramal para acessar o painel
    'senha' => 'senhasecreta4', // e a senha para acessar o painel
    'ligacao_externa' => true,
    'ligacao_celular' => true,
    'gravar_audio' => true,
    'acesso_gravacoes' => true
];
print "Criando o ramal 4 \n";
$client->central->criarRamal($ramal4);

// ################################################################
// ################################################################
// 2º passo criar a URA
$dados = array (
  0 => array (
    'acao' => 'tts', // toca um TTS inicialmente, mas poderia ser um arquivo de áudio também
    'menu' => 'menu 1',
    'acao_dados' => array (
        'mensagem' => 'Olá! Seja bem vindo! Você poderia me informar para qual setor você deseja ser transferifo?... Vamos lá! Aperte um para ser transferido para o setor Comercial... ou aperte dois para ser transferido para o setor de Suporte'
    ),
  ),
  1 => array (
    'acao' => 'transferir', // ação de tranferir para um número de telefone ou um ramal
    'menu' => 'menu 1',
    'opcao' => '1', // opção que o usuário irá digitar no telefone
    'acao_dados' => array (
        'numero_telefone' => '4103' // Ramal do setor "COMERCIAL"
    ),
  ),
  2 => array (
    'acao' => 'transferir',
    'menu' => 'menu 1',
    'opcao' => '2', // opção que o usuário irá digitar no telefone
    'acao_dados' => array (
        'numero_telefone' => '4104' // Ramal do setor "SUPORTE"
    ),
  ),
);
print "Criando a URA \n";
$response = $client->central->criarUra('TESTE DA URA', $dados);
$content = json_decode($response->getContent(), true);


// ################################################################
// ################################################################
// 3º Passo: vincular a URA ao Ramal 4101
$ramal1 = [
    'ura_id' => $content['dados']['id']
];
print "Atualizando o ramal 1 \n";
$client->central->atualizarRamal($idDoRamal1, $ramal1);


print "Finalizado!\n";
