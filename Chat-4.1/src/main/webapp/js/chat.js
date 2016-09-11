angular.module('chat', []).controller("chatController",

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
		return $scope.ws.readyState == 0;
	}
	$scope.isOpen = function() {
		return $scope.ws.readyState == 1;
	}
	$scope.isClosing = function() {
		return $scope.ws.readyState == 2;
	}
	$scope.isClosed = function() {
		return typeof ($scope.ws) == 'undefined' || $scope.ws.readyState == 3;
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

		if ($scope.isClosed()) {

			// establish the communication channel over a websocket
			$scope.ws = new WebSocket("ws://localhost:8090/chat");

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

	}

	$scope.disconnect = function() {
		if ($scope.isOpen()) {
			$scope.ws.close();
		}

	}

	$scope.connect();
	$scope.message = {};

	$scope.message.textColor = "#000000";
	$scope.message.backgroundColor = "#ffffff";
	$scope.messages = [];

	$scope.messageStyle=function(textColor,bakcgroundColor){
		return {
				'color' : textColor,
				'background-color' : bakcgroundColor
			};
	}
});
