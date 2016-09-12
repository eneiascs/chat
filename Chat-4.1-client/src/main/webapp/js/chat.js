var chat = angular.module('chat', [ 'angularjs-crypto' ]);
chat.run(function(cfCryptoHttpInterceptor, $rootScope) {
	$rootScope.base64Key = CryptoJS.enc.Base64
			.parse("2b7e151628aed2a6abf7158809cf4f3c");
	$rootScope.iv = CryptoJS.enc.Base64
			.parse("3ad77bb40d7a3660a89ecaf32466ef97");

	$(document).ready(function() {
		$("#input-message").keypress(function(event) {
			if (event.which == 13) {
				$('#btnSend').click();
			}
		});
	});
});
chat
		.controller(
				"chatController",

				function($rootScope, $scope, $http) {
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
						return typeof ($scope.ws) == 'undefined'
								|| $scope.ws.readyState == 3;
					}
					// appends logText to log text area
					$scope.appendMessage = function(message) {
						$scope.safeApply(function() {

							console.log(message)
							$scope.messages.push(message);
						});

					}

					// sends msg to the server over websocket
					$scope.sendToServer = function() {
						$scope.message.text=$scope.username+": "+$scope.message.text
						var encrypted = CryptoJS.AES.encrypt(
								$scope.message.text, $rootScope.base64Key, {
									iv : $rootScope.iv
								});
						console.log('encrypted = ' + encrypted);

						$scope.message.text = encrypted.ciphertext
								.toString(CryptoJS.enc.Base64);
						var cipherParams = CryptoJS.lib.CipherParams.create({
							ciphertext : CryptoJS.enc.Base64
									.parse($scope.message.text)
						});
						var decrypted = CryptoJS.AES.decrypt(cipherParams,
								$rootScope.base64Key, {
									iv : $rootScope.iv
								});
						$scope.descrString = decrypted
								.toString(CryptoJS.enc.Utf8);
						console.log('decrypted=' + decrypted);
						// console.log('ciphertext = ' + ciphertext);
						$scope.ws.send(JSON.stringify($scope.message));
						$scope.message.text = "";
					}
					$scope.connect = function() {
						$scope.messages = [];
						$scope.error = "";

						var encrypted = CryptoJS.AES.encrypt($scope.password,
								$rootScope.base64Key, {
									iv : $rootScope.iv
								});
						console.log('encrypted = ' + encrypted);

						var ciphertext = encrypted.ciphertext
								.toString(CryptoJS.enc.Base64);
						console.log('ciphertext = ' + ciphertext);

						var req = {
							method : 'POST',
							url : 'http://localhost:8090/users/authenticate',
							headers : {
								'Content-Type' : 'application/json'
							},
							data : {

								username : $scope.username,
								password : ciphertext
							}
						}

						$http(req)
								.success(
										function(data) {

											console.log("Success " + data);
											if (data == "OK") {
												if ($scope.isClosed()) {

													// establish the
													// communication channel
													// over a websocket
													$scope.ws = new WebSocket(
															"ws://localhost:8090/chat?username="
																	+ $scope.username
																	+ "&password="
																	+ ciphertext);
													$scope.password = "";
													// called when socket
													// connection established
													$scope.ws.onopen = function() {
														var message = {};
														message.text = "Connected to chat service!";
														console.log(message);
														$scope
																.appendMessage(message);

													};

													// called when a message
													// received from server
													$scope.ws.onmessage = function(
															evt) {
														var message = JSON
																.parse(evt.data)

														var cipherParams = CryptoJS.lib.CipherParams
																.create({
																	ciphertext : CryptoJS.enc.Base64
																			.parse(message.text)
																});
														var decrypted = CryptoJS.AES
																.decrypt(
																		cipherParams,
																		$rootScope.base64Key,
																		{
																			iv : $rootScope.iv
																		});
														message.text = decrypted
																.toString(CryptoJS.enc.Utf8);

													
														$scope
																.appendMessage(message);
													};

													// called when socket
													// connection closed
													$scope.ws.onclose = function() {
														var message = {};
														message.text = "Disconnected from chat service!";
														$scope
																.appendMessage(message);

													};

													// called in case of an
													// error
													$scope.ws.onerror = function(
															err) {
														console.log("ERROR!",
																err)
													};
												}

											} else {
												$scope.error = data;
												$scope.confirmPassword = "";
												$scope.password = "";
											}

										})
								.error(
										function(status) {
											$scope.error = "Error connecting to the server";
										});

					}

					$scope.disconnect = function() {
						if ($scope.isOpen()) {
							$scope.ws.close();
							$scope.messages = [];
						}

					}

					$scope.message = {};

					$scope.message.textColor = "#000000";
					$scope.message.backgroundColor = "#ffffff";
					$scope.messages = [];

					$scope.username = "";
					$scope.password = "";
					$scope.confirmPassword = "";
					$scope.isRegister = false;
					$scope.messageStyle = function(textColor, bakcgroundColor) {
						return {
							'color' : textColor,
							'background-color' : bakcgroundColor
						};
					}
					$scope.register = function() {
						$scope.error = "";
						if (!$scope.isRegister) {
							$scope.isRegister = true;
						} else {

							var encrypted = CryptoJS.AES.encrypt(
									$scope.password, $rootScope.base64Key, {
										iv : $rootScope.iv
									});
							console.log('encrypted = ' + encrypted);

							var ciphertext = encrypted.ciphertext
									.toString(CryptoJS.enc.Base64);
							console.log('ciphertext = ' + ciphertext);
							var req = {
								method : 'POST',
								url : 'http://localhost:8090/users/register',
								headers : {
									'Content-Type' : 'application/json'
								},
								data : {
									username : $scope.username,
									password : ciphertext
								}
							}

							$http(req)
									.success(function(data) {

										console.log("Success " + data);
										if (data == "OK") {
											$scope.isRegister = false;
											$scope.connect();
											$scope.confirmPassword = "";
										} else {
											$scope.error = data;
											$scope.confirmPassword = "";
											$scope.password = "";
										}

									})
									.error(
											function(status) {
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
				});