<?php
/**
 * o 2FA é utilizado para realizar o que chamamos de 
 * Autenticação de 2 níveis
 * http://www.totalvoice.com.br/blog/2-factor-authentication-2fa-com-a-totalvoice/
 * 
 * Com a nossa lib você consegue enviar tanto para um telefone fixo, quanto para um móvel - celular
 * Você consegue enviar um TTS (Text to Speech - telefone fixo) ou um SMS (telefone movel)
 *  - TTS: o usuário irá receber um ligação que irá informar o código de verificação por meio de voz
 *  - SMS: o usuário irá recener uma mensagem de texto no seu celular com o código de verificação
 */

 //######################################################################
 // Exemplo com a nova LIB
 // Se vc usa PHP acima do 5.4 e tem o composer instalado
 // sugerimos utilizar este exemplo
 //######################################################################
require 'vendor/autoload.php';

use TotalVoice\Client as TotalVoiceClient;

// NÃO ESQUEÇA DE SUBSTITUIR PELO SEU ACCESS_TOKEN
$client = new TotalVoiceClient('seu-access-token');

// estamos enviando um SMS para o telefone "48111111111" com um código de verificação
// ele receberá um código semelhante a 1234 por exemplo
$response = $client->verificacao->enviar('48111111111', 'minha empresa');
$content = json_decode($response->getContent(), true);
$id = $content['dados']['id']; // aqui temos o ID da verificação

// tendo o código de verificação em mãos você consegue confirmar se este é valido
// Ou seja, nesse caso você terá o ID da verificação e o usuário irá te informar o código de 
// verificação que ele recebeu no telefone
$response = $client->verificacao->buscar($id, '1234'); // passando o ID e o código que o usuário informou
$content = json_decode($response->getContent(), true);
$result = $content['dados']['resultado'];
if($result == "valido") {
    print "OK";
} else {
    print "NOK";
}


//######################################################################
// Exemplo com a LIB Legada - antiga
// Se vc não tem um sistema de autoload bem definido
// sugerimos utilizar este exemplo
// Nesse caso, vc precisa realizar o Download da lib no github
// https://github.com/totalvoice/TotalVoiceAPI-PHP
//######################################################################
require_once "TotalVoiceAPI.class.php";

$api = new TotalVoiceAPI("seu-access-token", true);
$response = $api->enviaVerificacao('48111111111', 'minha empresa');
$id = $response['dados']['id']; // aqui temos o ID da verificação

$response = $api->confirmaVerificacao($id, '1234'); // passando o ID e o código que o usuário informou
$result = $content['dados']['resultado'];
if($result == "valido") {
    print "OK";
} else {
    print "NOK";
}
