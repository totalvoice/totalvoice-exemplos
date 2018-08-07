angular.module('starter.controllers', [])

.controller('ChatCtrl', function($scope, $stateParams, $state, $firebaseObject,$firebaseArray,$ionicScrollDelegate,$ionicModal,$timeout,$interval) {

	// controle da janela Modal da ligação
  	$ionicModal.fromTemplateUrl('templates/callScreen.html', {
    	scope: $scope
  	}).then(function(modal) {
    	$scope.modal = modal;
  	});

  	$ionicModal.fromTemplateUrl('templates/callReceiveScreen.html', {
    	scope: $scope
  	}).then(function(modal) {
    	$scope.receiveModal = modal;
  	});

  	// variáveis de controle
  	$scope.statusCalling = 'chamando...';
  	$scope.universalStatus = '';
  	$scope.onCallBtn = true;
  	$scope.onMic = true;
  	$scope.onCall = false;
  	

	//remover isso
	$scope.token = location.href.split('?ionicplatform=ios&ionicstatusbarpadding=true&=')[1];
	////////////////
  	

  	// **  Não editar essa área  ///// --->
	$scope.contSeg = 0;
	$scope.contMin = 0;
	$scope.runID = ''; 
	$scope.avv = '' //  Recebe o Valor da ligação, para ser adicionado no final da corrida a quem fez a ligação ( Motorista ou passageiro )

  	/// <--- //////////////////////////////// **


  	//Variaveis que devem ser preenchidas de acordo com o fluxo da API /// --->

	$scope.clientName = ''; // Retornar aqui o nome do usuario logado
	$scope.anotherName = ""; // Retornar aqui o nome do contato
	$scope.presetID = ''; // Retornar aqui o ID do usuario logado
	$scope.anotherID = ''; // Retornar aqui o ID do contato
	$scope.avatar = ''; // Retornar aqui a foto do usuário logado
	$scope.anotherAvatar = ''; // Retornar aqui a foto do usuário contato
	$scope.userRamal = ''; // Retornar ramal do usuário logado
	$scope.anotherRamal = ''; // Retornar ramal do contato
	$scope.userUrlRamal = ''; // url cadastrada no perfil do usuário.
	$scope.runID = ''; // retornar ID da corrida ex: a3cfb1fa-6697-483c-8fde-778cc3ef0483
	
	/// <---   //////////////////////////////

 	 //remover isso /// Sample data // ---> ///

	  if($scope.token == '001')
	  {

		  $scope.clientName = 'Weverton';
		  $scope.anotherName = "Alberto";
		  $scope.presetID = '001';
		  $scope.anotherID = '015';
		  $scope.avatar = '../img/avataruser.png';
		  $scope.anotherAvatar = '../img/avatarmoto.png';
		  $scope.runID = 'a3cfb1fa-6697-483c-8fde-778cc3ef0483';
		  $scope.userRamal = '1000';
		  $scope.anotherRamal = '1001';
		  $scope.userUrlRamal = "https://api.totalvoice.com.br/w3?key=c241c4d0b7ff61c60a1fb296b1dca539&tipo=hidden&ver=2";

	  }

	  if($scope.token == '015')
	  {

		  $scope.clientName = 'Alberto';
		  $scope.anotherName = "Weverton";
		  $scope.presetID = '015';
		  $scope.anotherID = '001';
		  $scope.avatar = '../img/avatarmoto.png';
		  $scope.anotherAvatar = '../img/avataruser.png';
		  $scope.runID = 'a3cfb1fa-6697-483c-8fde-778cc3ef0483';
		  $scope.userRamal = '1001'; 
		  $scope.anotherRamal = '1000';
		  $scope.userUrlRamal = "https://api.totalvoice.com.br/w3?key=9e817fc913771e8cfdb73b29068b35b8&tipo=hidden&ver=2"; 

	  }
	  

  /// <---  Remover até aqui /////////////


	// retorna o ID do usuário logado para o repeat de mensagens do chat
	$scope.msgIN = () =>{ return $scope.presetID }
	// retorna o ID do contato para o repeat de mensagens do chat
	$scope.msgAT = () =>{ return $scope.anotherID }

	// Adiciona a URL do ramal para criar o WEB Phone // Não editar aqui !		
	let Tagscript = document.createElement('script');
	Tagscript.setAttribute('src', $scope.userUrlRamal);
	document.head.appendChild(Tagscript);


	// Recebe as mensagens do Firebase não editar
	$scope.msg = "";
	let ref = firebase.database().ref().child($scope.runID);
	$scope.mensagens = $firebaseArray(ref);

	// Envia a mensagem do usuário  e salva no banco de dados
	$scope.sendMessenger = ()=>{
		let params = { messages: $scope.msg, user: $scope.clientName, id: $scope.presetID }
		if($scope.msg != ""){
			$scope.mensagens.$add(params, "data").then((ref)=> {});
			$scope.msg = "";  	
			$scope.scrollMsg();
		}
	}

	/// Rola as mensagens novas que chegam
	$scope.scrollMsg = ()=> {
  		setTimeout(()=>{
	  	   let delegate = $ionicScrollDelegate.$getByHandle('chatScroll');
	       delegate.scrollBottom(true);
       },100)
	}


  	/// ----------------------------------------------------------------///
  							// ***  ***//
  				// --> !!! FUNÇÕES DE CRIAÇÃO DE RAMAL <-- !!! <-- // 
  	/// ----------------------------------------------------------------///


  	/// ----------------------------------------------------------------///

  						 	// !!! OBSERVAÇÕES !!!//
  	// *** Mover a função ramalCreate e generateUrl para o cadastro do usuário ***//
  	// --> Essa função cria um ramal e uma URL para o usuário fazer e receber ligações <-- //
  	// --> Necessário estar no cadastro do usuário e no cadastro do motorista <-- //
  	// --> Os ramais criados devem começar a partir do 1001 ex: 1002 , 1003 etc... <-- //
  	// --> !!! CRIAR UM CAMPO DE RAMAL E URL NO BANCO DE DADOS <-- !!! <-- //

  	/// ----------------------------------------------------------------///

	$scope.ramalCreate = () =>{

  	  	// !!! IMPORTANTE !!! //
  	  	$scope.lastRamal =  1001 + 1 ; // trocar o 1001 pela variável com o ultimo ramal cadastrado no banco e não remover o + 1.
  	  	//--------------------//

	  $.ajax({
		  type: "POST",
		  url: "https://api.totalvoice.com.br/ramal",
		  headers : {
		  	//token gerado na pagina principal do total voice ( esse token é de teste , gerar novo no nome da empresa )
		  	"Access-Token" : "eeee70b8387a3296d45e18c283115ab8",
		  	 },
		  data: {
			"ramal": $scope.lastRamal,
			"login":"",
			"senha":"",
			"bina":"",
			"ligacao_externa":false,
			"ligacao_celular":false,
			"gravar_audio":false,
			"acesso_gravacoes":false,
			"ura_id":0,
			"voicemail":false,	
		  },
		  success: (response)=> {
		    $scope.generateUrl(response.dados.ramal)
		    },
		  error: (e)=>{
		      console.log(e);
		 	}
		});
	}

  	$scope.generateUrl = (ramal) =>{

	  $.ajax({
		  type: "GET",
		  url: "https://api.totalvoice.com.br/webphone?tipo=hidden&ramal=" + ramal,
		  headers : {
		  	//token gerado na pagina principal do total voice ( esse token é de teste , gerar novo no nome da empresa )
		  	"Access-Token" : "eeee70b8387a3296d45e18c283115ab8",
		  	 },
		  success: (response)=> {

		    console.log(response)
		    /// salvar response.dados.ramal  e URL no perfil do usuário que está sendo criado.
		    // ex: 
		    // let url = response.dados.url;  
		    /// $scope.cadastro(ramal,url);

		    //$scope.cadastro = (r,u) =>{
		    	 //...continuar código de cadastro do usuário
		    //}
		   
		    },
		  error: (e)=>{

		  	/// chamar erro de cadastro ou aqui 
		      console.log(e);
		 	}
		});


  	}

	//----------- Fim das funções de criação de ramal  ----------------- //
	//------------***********************************--------------------//


  	/// ----------------------------------------------------------------///
  							// ***  ***//
  				// --> !!! FUNÇÕES DE LIGAÇÃO <-- !!! <-- // 
  	/// ----------------------------------------------------------------///

  	// Inicia uma ligação
  	$scope.callTocall = () =>{
 		$scope.modal.show();
 		$scope.callNumber($scope.anotherRamal)
  	}

  	// Encerra uma ligação
  	$scope.closeCall = () =>{
  	  	$scope.modal.hide()
  	  	$scope.receiveModal.hide()
  	  	$scope.setAvv();
		$scope.contCall = null;
		$scope.contSeg = 0;
		$scope.contMin = 0;
  	  	$timeout(()=>{ $scope.statusCalling = 'chamando...'; },1000)
  	}
  	
  	// Função de cobrança / tarifa de ligação pra quem realizou ( motorista ou passageiro )
  	  $scope.setAvv = () =>{

  	  	if($scope.contCall < 60){$scope.contCall = 60}
  	  	let calcCall =  $scope.contCall / 60;
  	  	$scope.avv = calcCall * 0.35;

  	  	//chamar aqui a função que soma o valor final da corrida + ($scope.avv.toFixed(2)
  	  	console.log($scope.avv.toFixed(2));
  	}

  	// Retorno onmessage para controle da API de ligação , só editar caso necessário

  	window.onmessage = (e)=> {

  	  	    if (e.data.message == 'status') {
                    
                if(e.data.status == 'conversando'){

			  		$scope.contCall = 0;
			  	  	$scope.onCallBtn = false;

			  	  	inCall = $interval(()=>{

			  	  		/// Máximo de tempo de ligação setado para 10 minutos  mas pode alterar trocando o 600 por um número que quiser
			  	  		 // após 0 tempo máximo a ligação desliga sozinha e chama a função $scope.closeCall().

			  	  		if($scope.contCall < 600 && $scope.contCall != null){

			  	  			if($scope.contSeg < 59){ 
			  	  				$scope.contSeg =+ $scope.contSeg + 1;
			  	  				let sliceSeg = ("0" + $scope.contSeg).slice(-2)
			  	  				let sliceMin = ("0" + $scope.contMin).slice(-2)
			  	  				$scope.statusCalling = sliceMin + ':' + sliceSeg;
			  	  			}else{
			  	  				$scope.contSeg = 0;
			  	  				$scope.contMin =+ $scope.contMin + 1;
			  	  				let sliceSeg = ("0" + $scope.contSeg).slice(-2);
			  	  				let sliceMin = ("0" + $scope.contMin).slice(-2)
			  	  				$scope.statusCalling = sliceMin + ':' + sliceSeg;
			  	  			}

			  	  			$scope.contCall =+ $scope.contCall + 1;

			  	  		}else{

				  	  		$scope.turnOFFCall()
			  	  		}
			  	  	
			  	  	},1100)
		  	  	}

		  	  	if(e.data.status == 'encerrada'){ 
		  	  	
		  	  		$interval.cancel(inCall);
		  	  		$scope.closeCall();
		  	  		$scope.onCallBtn = true;
		  	  	}
		  	  	if(e.data.status == 'conectado'){}
            }

                if (e.data.message == 'stats_webphone') {

                	$timeout(()=>{
	                	if(!$scope.onCall)
	                	{
	                		$scope.onCall = true;
	                	}
                	},3000);

                }

                if (e.data.message == 'chegandoChamada') {

                	$scope.receiveModal.show();
                }
  	  	}

	//----------- Fim das funções de ligação  ----------------- //
	//------------***********************************--------------------//


  	/// ----------------------------------------------------------------///
  							// ***  ***//
  		// --> !!! FUNÇÕES ORIUNDAS DA API DE LIGAÇÃO  <-- !!! <-- // 

  				//!!!!!!!!!!!!! NÃO EDITAR !!!!!!!!!!!!!!!!!!!!!
  	/// ----------------------------------------------------------------///

  	         //telefona para um número
            $scope.callNumber = (numero) => {
                webphone.contentWindow.postMessage({
                    message: 'chamaNumero',
                    'numero': numero
                }, '*');
            }

             //atender
            $scope.turnONCall = () => { webphone.contentWindow.postMessage({message: 'answer'}, '*')}

            //encerra chamada ativa
            $scope.turnOFFCall = () => { webphone.contentWindow.postMessage({ message: 'hangup'}, '*')}

            //mute microfone
            $scope.onOffMic = () => { webphone.contentWindow.postMessage({message: 'mute'}, '*'); if($scope.onMic){ $scope.onMic = false; }else{ $scope.onMic = true }}

            //Ativa recebimento de chamadas ( por padrão essa função é chamada quando abre o app do melleve )
            $scope.connect = () => { webphone.contentWindow.postMessage({message : 'conectar'}, '*')}

            //Desativa recebimento de chamadas
            $scope.unConnect = () => { webphone.contentWindow.postMessage({message : 'desconectar'}, '*')}

            //transferencia blind - encerra a ligação e transfere para o numero solicitado
            $scope.tranferCall = (numeroTelefone) =>  { webphone.contentWindow.postMessage({message: 'transferir', 'numeroTelefone': numeroTelefone }, '*')}

  	 	//----------- Fim das funções origundas   ----------------- //
	//------------***********************************--------------------//
})
