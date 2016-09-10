angular.module('chat', []).controller(
		"chatController",
		
		function($scope, $http) {
			
			
			
			
			
			$scope.isConnecting=function(){
				return $scope.ws.readyState == 0;
			}
			$scope.isOpen=function(){
				return $scope.ws.readyState == 1;
			}
			$scope.isClosing=function(){
				return $scope.ws.readyState == 2;
			}
			$scope.isClosed=function(){
				return typeof ($scope.ws) == 'undefined'
					|| $scope.ws.readyState == 3;
			}
			// appends logText to log text area
			$scope.appendLog = function(logText) {
				var log = document.getElementById("log");
				log.value = log.value + logText + "\n"
			}

			// sends msg to the server over websocket
			$scope.sendToServer = function(msg) {
				$scope.ws.send(msg);
				$scope.message="";
			}
			$scope.connect = function() {

				if ($scope.isClosed()) {

					// establish the communication channel over a websocket
					$scope.ws = new WebSocket("ws://localhost:8090/chat");
					// called when socket connection established
					$scope.ws.onopen = function() {
						$scope.appendLog("Connected to chat service!")

					};

					// called when a message received from server
					$scope.ws.onmessage = function(evt) {
						$scope.appendLog(evt.data)
					};

					// called when socket connection closed
					$scope.ws.onclose = function() {
						$scope.appendLog("Disconnected from chat service!")

					};

					// called in case of an error
					$scope.ws.onerror = function(err) {
						console.log("ERROR!", err)
					};
				}
				
			}

			$scope.disconnect = function() {
				if ($scope.isOpen()) {
					$scope.ws.close();
				}
			

			}

			$scope.connect();
			$scope.message="";
			
		});
