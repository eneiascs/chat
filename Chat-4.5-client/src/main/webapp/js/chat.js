/// <reference path="typings/angularjs/angular.d.ts" />
/// <reference path="typings/angularjs/angular-resource.d.ts" />

var chat = angular.module('chat', [ 'angularjs-crypto' ]);

chat.factory('userFactory', [ '$http', function($http) {

	var urlBase = 'http://localhost:8090/user';
	var userFactory = {};

	userFactory.registerUser = function(user) {
		return $http.post(urlBase + '/register', JSON.stringify(user));
	};
	userFactory.authenticateUser = function(user) {
		return $http.post(urlBase + '/authenticate', JSON.stringify(user));
	};

	return userFactory;
} ]);
chat.factory('messageFactory',
		[
				'$http',
				function($http) {

					var urlBase = 'http://localhost:8090/message';
					var messageFactory = {};
					var config = {
						headers : {
							'Content-Type' : 'application/json;'
						}
					}

					messageFactory.createMessage = function(message) {
						return $http.post(urlBase + '/create', JSON
								.stringify(message), config);
					};
					messageFactory.handleMessage = function(message, config) {
						return $http.post(urlBase + '/handle', JSON
								.stringify(message));
					};
					return messageFactory;
				} ]);
chat.factory('historyFactory', [ '$http', function($http) {

	var urlBase = 'http://localhost:8090/history';
	var historyFactory = {};
	var config = {
		headers : {
			'Content-Type' : 'application/json;'
		}
	}

	historyFactory.getHistory = function() {
		return $http.get(urlBase + '/');
	};
	return historyFactory;
} ]);

chat.factory('htmlFactory', [ '$http', function($http) {

	var urlBase = 'http://localhost:8090/html';
	var htmlFactory = {};
	var config = {
		headers : {
			'Content-Type' : 'text/html;'
		}
	}

	htmlFactory.getDiv = function(name) {
		return $http.get(urlBase + '/' + name);
	};

	return htmlFactory;

} ]);

chat.directive('ngFeature', function(htmlFactory, $compile) {
	return {

		restrict : 'A',

		compile : function(element, attributes) {
			return {
				pre : function preLink(scope, element, attributes) {
					htmlFactory.getDiv(attributes.ngFeature).then(
							function(response) {
								console.log(attributes.ngFeature);
								// var template =
								// angular.element($compile(response.data)(scope));
								var template = $compile(response.data)(scope);
								// $compile(template)(scope);
								element.replaceWith(template);

								// element.append(scope.template);

								console.log("Temp link" + scope.template);

							});
				},
				post : function postLink(scope, element, attributes) {

				}

			}

		}
	}

});

chat.run(function(cfCryptoHttpInterceptor, $rootScope) {

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

				function($rootScope, $scope, $document, $http, $location, $sce,
						$compile, userFactory, historyFactory, messageFactory,
						htmlFactory) {
					$scope.server = 'localhost:8090'

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

					// sends msg to the server over websocket
					$scope.sendToServer = function(text, textColor,
							backgroundColor) {
						text = $scope.username + ": " + text;
						message = {};

						message.textColor = textColor;
						message.backgroundColor = backgroundColor;
						message.text = text;
						messageFactory
								.createMessage(message)
								.then(
										function(response) {

											//           
											$scope.ws.send(JSON
													.stringify(response.data));
											$scope.text = "";

										},
										function(error) {
											$scope.error = 'Server error';
											console
													.log('Unable to send message to server: '
															+ error.message);
										});
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
								$scope.lastMessage;

							};

							// called when a message
							// received from server
							$scope.ws.onmessage = function(evt) {
								var msg = JSON.parse(evt.data)

								messageFactory
										.handleMessage(msg)
										.then(
												function(response) {
													$scope
															.safeApply(function() {
																$scope.lastMessage = response.data;
															});
													$scope
															.safeApply(function() {
																historyFactory
																		.getHistory()
																		.then(
																				function(
																						response) {

																					$scope.messages = response.data;

																				},
																				function(
																						error) {
																					$scope.error = 'Server error';
																					console
																							.log('Unable to send message to server: '
																									+ error.message);
																				});

															});

												},
												function(error) {
													$scope.error = 'Server error';
													console
															.log('Unable to send message to server: '
																	+ error.message);
												});

							};

							// called when socket
							// connection closed
							$scope.ws.onclose = function() {

								var message = {};
								message.text = "Disconnected from chat service!";
								$scope.lastMessage;
							};

							// called in case of an
							// error
							$scope.ws.onerror = function(err) {
								$scope.error = 'Server error';
								console.log("ERROR!", err)
							};
						}
					}

					$scope.connect = function() {
						$scope.messages = [];
						$scope.error = "";

						var user = {
							username : $scope.username,
							password : $scope.password
						}
						userFactory
								.authenticateUser(user)
								.then(
										function(response) {
											if (response.data == "OK") {
												$scope.connectToWs();
											} else {
												$scope.error = response.data;
												$scope.confirmPassword = "";
												$scope.password = "";
											}

										},
										function(error) {
											$scope.error = 'Server error';
											console
													.log('Unable to send message to server: '
															+ error.message);
										});
					}

					$scope.disconnect = function() {
						if ($scope.isOpen()) {
							$scope.ws.close();
							$scope.messages = [];
						}

					}

					$scope.messageStyle = function(textColor, backgroundColor) {
						console.log(textColor);
						return {
							'color' : textColor,
							'background-color' : backgroundColor
						};

					}
					$scope.register = function() {
						$scope.error = "";
						if (!$scope.isRegister) {
							$scope.isRegister = true;
						} else {

							var user = {
								username : $scope.username,
								password : $scope.password
							}
							userFactory
									.registerUser(user)
									.then(
											function(response) {
												if (response.data == "OK") {
													$scope.isRegister = false;
													$scope.connect();
													$scope.confirmPassword = "";
												} else {
													$scope.error = response.data;
													$scope.confirmPassword = "";
													$scope.password = "";
												}

											},
											function(error) {
												$scope.error = 'Server error';
												console
														.log('Unable to send message to server: '
																+ error.message);
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

					$scope.safeApply(function() {
						$scope.text = "";
						$scope.textColor = "black";
						$scope.backgroundColor = "white";

					});

					$scope.messages = [];

					$scope.username = "guest"
							+ Math.floor((Math.random() * 200) + 1);

					$scope.password = "";
					$scope.confirmPassword = "";
					$scope.isRegister = false;

					$scope.message = {};

				});
