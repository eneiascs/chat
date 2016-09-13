var chat = angular.module('chat', [ 'angularjs-crypto' ]);

chat.factory('configFactory', [ '$http', function($http) {

	var urlBase = 'http://localhost:8090/config';
	var configFactory = {};

	configFactory.getConfig = function() {
		return $http.get(urlBase);
	};

	return configFactory;
} ]);
chat.factory('userFactory', [ '$http', function($http) {

	var urlBase = 'http://localhost:8090';
	var userFactory = {};

	userFactory.registerUser = function(user) {
		return $http.post(urlBase + '/register', user);
	};
	userFactory.registerUser = function(user) {
		return $http.post(urlBase + '/authenticate', user);
	};

	return userFactory;
} ]);
chat.run(function(cfCryptoHttpInterceptor, $rootScope) {
	// $rootScope.base64Key = CryptoJS.enc.Base64
	// .parse("2b7e151628aed2a6abf7158809cf4f3c");
	// $rootScope.iv = CryptoJS.enc.Base64
	// .parse("3ad77bb40d7a3660a89ecaf32466ef97");

	$rootScope.base64Key = CryptoJS.enc.Base64.parse("Secret Passphrase");
	
	$rootScope.iv = CryptoJS.enc.Base64.parse("RandomInitVector");
	$(document).ready(function() {
		$("#input-message").keypress(function(event) {
			if (event.which == 13) {
				$('#btnSend').click();
			}
		});
	});
});
chat.controller("chatController",

function($rootScope, $scope, $http, $location, configFactory) {
	$scope.server = 'localhost:8090'

	$scope.encryptAesCbc = function(message) {

		var encrypted = CryptoJS.AES.encrypt(message, $rootScope.base64Key, {
			iv : $rootScope.iv,
			mode: CryptoJS.mode.CBC, 
			padding: CryptoJS.pad.Pkcs7
		});
		
		return encrypted.ciphertext.toString(CryptoJS.enc.Base64);

	}
	
	
	$scope.encryptAesEcb = function(message) {

	
		return CryptoJS.AES.encrypt(message,"Secret Passphrase", {
			
			//mode: CryptoJS.mode.ECB, 
			//padding: CryptoJS.pad.Pkcs7
		}).toString();

	}
	$scope.decryptAesCbc = function(ciphertext) {
		var cipherParams = CryptoJS.lib.CipherParams.create({
			ciphertext : CryptoJS.enc.Base64.parse(ciphertext)
		});
		var decrypted = CryptoJS.AES.decrypt(cipherParams,
				$rootScope.base64Key, {
					iv : $rootScope.iv,
					mode: CryptoJS.mode.CBC, 
					padding: CryptoJS.pad.Pkcs7
				});
		
		return decrypted.toString(CryptoJS.enc.Utf8);
	}

	$scope.decryptAesEcb= function(message) {


        return CryptoJS.AES.decrypt(message, "Secret Passphrase", {
			
			//mode: CryptoJS.mode.ECB, 
			//padding: CryptoJS.pad.Pkcs7
		}).toString(CryptoJS.enc.Utf8)
    }
	
	$scope.safeApply = function(fn) {
		if (this.$root) {
			var phase = this.$root.$$phase;
			if (phase == '$apply' || phase == '$digest') {
				if (fn && (typeof (fn) === 'function')) {
					fn();
				}
			} else {
				this.$apply(fn);
			}
		}
	};

	$scope.isConnecting = function() {
		if (typeof ($scope.ws) == 'undefined') {
			return false;
		} else {
			return $scope.ws.readyState == 0;
		}
	}
	$scope.isOpen = function() {
		if (typeof ($scope.ws) == 'undefined') {
			return false;
		} else {
			return $scope.ws.readyState == 1;
		}
	}
	$scope.isClosing = function() {
		if (typeof ($scope.ws) == 'undefined') {
			return false;
		} else {
			return $scope.ws.readyState == 2;
		}
	}
	$scope.isClosed = function() {
		return typeof ($scope.ws) == 'undefined' || $scope.ws.readyState == 3;
	}
	// appends logText to log text area
	$scope.appendMessage = function(message) {
		$scope.safeApply(function() {

			// Feature History
			if ($scope.config.ClientMessagesHistory == undefined) {
				$scope.messages = []

			} else {

				if ($scope.messages.length == 10) {
					$scope.messages.splice(0, 1);
				}

			}

			$scope.messages.push(message);

		});

	}

	// sends msg to the server over websocket
	$scope.sendToServer = function() {
		$scope.message.text = $scope.username + ": " + $scope.message.text;

		// Feature Encrypt
		if ($scope.config.ClientAES_CBCEncryption !== undefined) {
			$scope.message.text = $scope.encryptAesCbc($scope.message.text);
		}

		if ($scope.config.ClientAES_ECBEncryption !== undefined) {
			$scope.message.text = $scope.encryptAesEcb($scope.message.text);
		}
		$scope.ws.send(JSON.stringify($scope.message));
		$scope.message.text = "";
	}
	$scope.connectToWs = function(username) {
		if ($scope.isClosed()) {

			// establish the
			// communication channel
			// over a websocket
			$scope.ws = new WebSocket("ws://" + $scope.server
					+ "/chat?username=" + $scope.username);
			$scope.password = "";
			// called when socket
			// connection established
			$scope.ws.onopen = function() {
				var message = {};
				message.text = "Connected to chat service!";
				console.log(message);
				$scope.appendMessage(message);

			};

			// called when a message
			// received from server
			$scope.ws.onmessage = function(evt) {
				var message = JSON.parse(evt.data)

				// Feature Decrypt
				if ($scope.config.ClientAES_CBCDecryption !== undefined) {
					message.text = $scope.decryptAesCbc(message.text);
				}
				if ($scope.config.ClientAES_ECBDecryption !== undefined) {
					message.text = $scope.decryptAesEcb(message.text);
				}
				$scope.appendMessage(message);
			};

			// called when socket
			// connection closed
			$scope.ws.onclose = function() {
				
				var message = {};
				message.text = "Disconnected from chat service!";
				$scope.appendMessage(message);

			};

			// called in case of an
			// error
			$scope.ws.onerror = function(err) {
				console.log("ERROR!", err)
			};
		}
	}

	$scope.connect = function() {
		$scope.messages = [];
		$scope.error = "";

		var req = {
			method : 'POST',
			url : 'http://' + $scope.server + '/users/authenticate',
			headers : {
				'Content-Type' : 'application/json'
			},
			data : {

				username : $scope.username,
				password : $scope.password
			}
		}

		$http(req).success(function(data) {

			console.log("Success " + data);
			if (data == "OK") {
				$scope.connectToWs();

			} else {
				$scope.error = data;
				$scope.confirmPassword = "";
				$scope.password = "";
			}

		}).error(function(status) {
			$scope.error = "Error connecting to the server";
		});

	}

	$scope.disconnect = function() {
		if ($scope.isOpen()) {
			$scope.ws.close();
			$scope.messages = [];
		}

	}

	$scope.messageStyle = function(textColor, backgroundColor) {
		// Feature Colors
		if (!angular.isUndefined($scope.config.Colors)) {

			return {
				'color' : textColor,
				'background-color' : backgroundColor
			};
		}
	}
	$scope.register = function() {
		$scope.error = "";
		if (!$scope.isRegister) {
			$scope.isRegister = true;
		} else {

			var req = {
				method : 'POST',
				url : 'http://' + $scope.server + '/users/register',
				headers : {
					'Content-Type' : 'application/json'
				},
				data : {
					username : $scope.username,
					password : $scope.password
				}
			}

			$http(req).success(function(data) {


				if (data == "OK") {
					$scope.isRegister = false;
					$scope.connect();
					$scope.confirmPassword = "";
				} else {
					$scope.error = data;
					$scope.confirmPassword = "";
					$scope.password = "";
				}

			}).error(function(status) {
				$scope.error = "Error connecting to the server";
			});

		}
	}
	$scope.cancel = function() {
		$scope.error = "";
		$scope.isRegister = false;
		$scope.username = "";
		$scope.password = "";
		$scope.confirmPassword = "";

	}
	
	$scope.config = {}
	configFactory.getConfig().then(
			function(response) {
				$scope.config = response.data;
				console.log($scope.config);

				$scope.config.encryptMessages = !angular
						.isUndefined($scope.config.ClientAES_CBCEncryption);

				$scope.message = {};

				$scope.message.textColor = "#000000";
				$scope.message.backgroundColor = "#ffffff";
				$scope.messages = [];

				$scope.username = "";
				$scope.password = "";
				$scope.confirmPassword = "";
				$scope.isRegister = false;

			}, function(error) {
				console.log('Unable to load config data: ' + error.message);
			});

});