angular.module('chat', []).controller(
		"chatController",

		function($scope, $http) {
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

				$scope.ws.send(JSON.stringify($scope.message));
				$scope.message.text = "";
			}
			$scope.connect = function() {
				$scope.messages = [];
				$scope.error="";
				var req = {
						method : 'POST',
						url : 'http://localhost:8090/users/authenticate',
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
							if ($scope.isClosed()) {

								// establish the communication channel over a websocket
								$scope.ws = new WebSocket(
										"ws://localhost:8090/chat?username="
												+ $scope.username + "&password="
												+ $scope.password);
								$scope.password = "";
								// called when socket connection established
								$scope.ws.onopen = function() {
									var message = {};
									message.text = "Connected to chat service!";
									console.log(message);
									$scope.appendMessage(message);

								};

								// called when a message received from server
								$scope.ws.onmessage = function(evt) {

									$scope.appendMessage(JSON.parse(evt.data));
								};

								// called when socket connection closed
								$scope.ws.onclose = function() {
									var message = {};
									message.text = "Disconnected from chat service!";
									$scope.appendMessage(message);

								};

								// called in case of an error
								$scope.ws.onerror = function(err) {
									console.log("ERROR!", err)
								};
							}
							
							
						}else{
							$scope.error=data;
							$scope.confirmPassword = "";
							$scope.password = "";
						}
						
						
					}).error(function(status) {
						$scope.error="Error connecting to the server";
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
				$scope.error="";
				if (!$scope.isRegister) {
					$scope.isRegister = true;
				} else {
					var req = {
						method : 'POST',
						url : 'http://localhost:8090/users/register',
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
							$scope.isRegister = false;
							$scope.connect();
							$scope.confirmPassword = "";
						}else{
							$scope.error=data;
							$scope.confirmPassword = "";
							$scope.password = "";
						}
						
						
					}).error(function(status) {
						$scope.error="Error connecting to the server";
					});

				}
			}
			$scope.cancel = function() {
				$scope.error="";
				$scope.isRegister = false;
				$scope.username = "";
				$scope.password = "";
				$scope.confirmPassword = "";

			}
		});
