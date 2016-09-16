/// <reference path="typings/angularjs/angular.d.ts" />
/// <reference path="typings/angularjs/angular-resource.d.ts" />
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var chat = angular.module('chat', ['angularjs-crypto']);
chat.factory('configFactory', ['$http', function ($http) {
        var urlBase = 'http://localhost:8090/config';
        var configFactory = {};
        configFactory.getConfig = function () {
            return $http.get(urlBase);
        };
        return configFactory;
    }]);
chat.factory('userFactory', ['$http', function ($http) {
        var urlBase = 'http://localhost:8090';
        var userFactory = {};
        userFactory.registerUser = function (user) {
            return $http.post(urlBase + '/register', user);
        };
        userFactory.registerUser = function (user) {
            return $http.post(urlBase + '/authenticate', user);
        };
        return userFactory;
    }]);
chat.run(function (cfCryptoHttpInterceptor, $rootScope) {
    $(document).ready(function () {
        $("#input-message").keypress(function (event) {
            if (event.which == 13) {
                $('#btnSend').click();
            }
        });
    });
});
chat.controller("chatController", function ($rootScope, $scope, $http, $location, configFactory) {
    $scope.server = 'localhost:8090';
    $scope.safeApply = function (fn) {
        if (this.$root) {
            var phase = this.$root.$$phase;
            if (phase == '$apply' || phase == '$digest') {
                if (fn && (typeof (fn) === 'function')) {
                    fn();
                }
            }
            else {
                this.$apply(fn);
            }
        }
    };
    $scope.isConnecting = function () {
        if (typeof ($scope.ws) == 'undefined') {
            return false;
        }
        else {
            return $scope.ws.readyState == 0;
        }
    };
    $scope.isOpen = function () {
        if (typeof ($scope.ws) == 'undefined') {
            return false;
        }
        else {
            return $scope.ws.readyState == 1;
        }
    };
    $scope.isClosing = function () {
        if (typeof ($scope.ws) == 'undefined') {
            return false;
        }
        else {
            return $scope.ws.readyState == 2;
        }
    };
    $scope.isClosed = function () {
        return typeof ($scope.ws) == 'undefined' || $scope.ws.readyState == 3;
    };
    // appends logText to log text area
    $scope.appendMessage = function (message) {
        $scope.safeApply(function () {
            // Feature History
            if ($scope.config.ClientMessagesHistory == undefined) {
                $scope.messages = [];
            }
            else {
                if ($scope.messages.length == 10) {
                    $scope.messages.splice(0, 1);
                }
            }
            $scope.messages.push(message);
        });
    };
    // sends msg to the server over websocket
    $scope.sendToServer = function (text, textColor, backgroundColor) {
        text = $scope.username + ": " + text;
        var message = new Message();
        message.setText(text);
        if ($scope.config.ClientMessageEncryption !== undefined) {
            message = new EncryptedMessage(message);
        }
        message = message.createMessage(text);
        message.setTextColor(textColor);
        message.setBackgroundColor(backgroundColor);
        //           
        $scope.ws.send(JSON.stringify(message));
        $scope.text = "";
    };
    $scope.connectToWs = function (username) {
        if ($scope.isClosed()) {
            // establish the
            // communication channel
            // over a websocket
            $scope.ws = new WebSocket("ws://" + $scope.server
                + "/chat?username=" + $scope.username);
            $scope.password = "";
            // called when socket
            // connection established
            $scope.ws.onopen = function () {
                var message = {};
                message.text = "Connected to chat service!";
                console.log(message);
                $scope.appendMessage(message);
            };
            // called when a message
            // received from server
            $scope.ws.onmessage = function (evt) {
                var msg = JSON.parse(evt.data);
                var message = new Message();
                if ($scope.config.ClientMessageEncryption !== undefined) {
                    message = new EncryptedMessage(message);
                }
                message = message.handleMessage(msg.text);
                message.setTextColor(msg.textColor);
                message.setBackgroundColor(msg.backgroundColor);
                $scope.appendMessage(message);
            };
            // called when socket
            // connection closed
            $scope.ws.onclose = function () {
                var message = {};
                message.text = "Disconnected from chat service!";
                $scope.appendMessage(message);
            };
            // called in case of an
            // error
            $scope.ws.onerror = function (err) {
                console.log("ERROR!", err);
            };
        }
    };
    $scope.connect = function () {
        $scope.messages = [];
        $scope.error = "";
        var req = {
            method: 'POST',
            url: 'http://' + $scope.server + '/users/authenticate',
            headers: {
                'Content-Type': 'application/json'
            },
            data: {
                username: $scope.username,
                password: $scope.password
            }
        };
        $http(req).success(function (data) {
            console.log("Success " + data);
            if (data == "OK") {
                $scope.connectToWs();
            }
            else {
                $scope.error = data;
                $scope.confirmPassword = "";
                $scope.password = "";
            }
        }).error(function (status) {
            $scope.error = "Error connecting to the server";
        });
    };
    $scope.disconnect = function () {
        if ($scope.isOpen()) {
            $scope.ws.close();
            $scope.messages = [];
        }
    };
    $scope.messageStyle = function (textColor, backgroundColor) {
        // Feature Colors
        if (!angular.isUndefined($scope.config.Colors)) {
            return {
                'color': textColor,
                'background-color': backgroundColor
            };
        }
    };
    $scope.register = function () {
        $scope.error = "";
        if (!$scope.isRegister) {
            $scope.isRegister = true;
        }
        else {
            var req = {
                method: 'POST',
                url: 'http://' + $scope.server + '/users/register',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: {
                    username: $scope.username,
                    password: $scope.password
                }
            };
            $http(req).success(function (data) {
                if (data == "OK") {
                    $scope.isRegister = false;
                    $scope.connect();
                    $scope.confirmPassword = "";
                }
                else {
                    $scope.error = data;
                    $scope.confirmPassword = "";
                    $scope.password = "";
                }
            }).error(function (status) {
                $scope.error = "Error connecting to the server";
            });
        }
    };
    $scope.cancel = function () {
        $scope.error = "";
        $scope.isRegister = false;
        $scope.username = "";
        $scope.password = "";
        $scope.confirmPassword = "";
    };
    $scope.config = {};
    configFactory.getConfig().then(function (response) {
        $scope.safeApply(function () {
            $scope.text = "";
            $scope.textColor = "black";
            $scope.backgroundColor = "white";
        }, $scope.config = response.data);
        config = $scope.config;
        console.log($scope.config);
        $scope.config.encryptMessages = !angular
            .isUndefined($scope.config.ClientAES_CBCEncryption);
        $scope.messages = [];
        if ($scope.config.ClientAuthentication == undefined) {
            $scope.username = "guest"
                + Math.floor((Math.random() * 200) + 1);
        }
        else {
            $scope.username = "";
        }
        $scope.password = "";
        $scope.confirmPassword = "";
        $scope.isRegister = false;
        var message = new Message();
        $scope.message = message;
    }, function (error) {
        console.log('Unable to load config data: ' + error.message);
    });
});
var Message = (function () {
    function Message() {
    }
    Message.prototype.handleMessage = function (text) {
        var message = new Message();
        message.setText(text);
        return message;
    };
    Message.prototype.createMessage = function (text) {
        var message = new Message();
        message.setText(text);
        return message;
    };
    Message.prototype.getText = function () {
        return this.text;
    };
    Message.prototype.setText = function (text) {
        this.text = text;
    };
    Message.prototype.getTextColor = function () {
        return this.textColor;
    };
    Message.prototype.setTextColor = function (textColor) {
        this.textColor = textColor;
    };
    Message.prototype.getBackgroundColor = function () {
        return this.backgroundColor;
    };
    Message.prototype.setBackgroundColor = function (backgroundColor) {
        this.backgroundColor = backgroundColor;
    };
    return Message;
}());
var MessageDecorator = (function () {
    function MessageDecorator(message) {
        this._message = message;
    }
    MessageDecorator.prototype.handleMessage = function (text) {
        var message = this._message.handleMessage(text);
        return message;
    };
    MessageDecorator.prototype.createMessage = function (text) {
        var message = this._message.createMessage(text);
        return message;
    };
    MessageDecorator.prototype.getText = function () {
        return this._message.getText();
    };
    MessageDecorator.prototype.setText = function (text) {
        this._message.setText(text);
    };
    MessageDecorator.prototype.getTextColor = function () {
        return this.textColor;
    };
    MessageDecorator.prototype.setTextColor = function (textColor) {
        this.textColor = textColor;
    };
    MessageDecorator.prototype.getBackgroundColor = function () {
        return this.backgroundColor;
    };
    MessageDecorator.prototype.setBackgroundColor = function (backgroundColor) {
        this.backgroundColor = backgroundColor;
    };
    return MessageDecorator;
}());
var EncryptedMessage = (function (_super) {
    __extends(EncryptedMessage, _super);
    function EncryptedMessage(message) {
        _super.call(this, message);
        if (config.ClientAES_ECBEncryption == undefined) {
            this.encryptor = new EncryptorAesEcb();
        }
        if (config.ClientAES_CBCEncryption == undefined) {
            this.encryptor = new EncryptorAesCbc();
        }
    }
    EncryptedMessage.prototype.handleMessage = function (text) {
        console.log("Handle Encrypted Message");
        var message = _super.prototype.handleMessage.call(this, text);
        message.setText(this.encryptor.decrypt(message.getText()));
        return message;
    };
    EncryptedMessage.prototype.createMessage = function (text) {
        console.log("Create Encrypted Message");
        var message = _super.prototype.createMessage.call(this, text);
        message.setText(this.encryptor.encrypt(message.getText()));
        return message;
    };
    return EncryptedMessage;
}(MessageDecorator));
var HistoryMessage = (function (_super) {
    __extends(HistoryMessage, _super);
    function HistoryMessage(message) {
        _super.call(this, message);
    }
    HistoryMessage.prototype.handleMessage = function (text) {
        console.log("Handle Message History");
        var message = _super.prototype.handleMessage.call(this, text);
        //message.setText(encryptor.decrypt(message.getText()));
        return message;
    };
    HistoryMessage.prototype.createMessage = function (text) {
        console.log("Create Message History");
        var message = _super.prototype.createMessage.call(this, text);
        //message.setText(encryptor.encrypt(message.getText()));
        return message;
    };
    return HistoryMessage;
}(MessageDecorator));
var Encryptor = (function () {
    function Encryptor() {
        this.key = "Secret Passphrase";
    }
    Encryptor.prototype.setKey = function (key) {
        this.key = key;
    };
    return Encryptor;
}());
var EncryptorAesCbc = (function (_super) {
    __extends(EncryptorAesCbc, _super);
    function EncryptorAesCbc() {
        _super.apply(this, arguments);
        this.iv = CryptoJS.enc.Base64.parse("RandomInitVector");
        this.key = CryptoJS.enc.Base64.parse("Secret Passphrase");
    }
    EncryptorAesCbc.prototype.encrypt = function (text) {
        console.log("Encrypting message with AES/CBC");
        var enc = CryptoJS.AES.encrypt(text, this.key, {
            iv: this.iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        });
        return enc.ciphertext.toString(CryptoJS.enc.Base64);
    };
    EncryptorAesCbc.prototype.decrypt = function (text) {
        console.log("Decrypting message with AES/CBC");
        var cipherParams = CryptoJS.lib.CipherParams.create({
            ciphertext: CryptoJS.enc.Base64.parse(text)
        });
        var decrypted = CryptoJS.AES.decrypt(cipherParams, this.key, {
            iv: this.iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        });
        return decrypted.toString(CryptoJS.enc.Utf8);
    };
    return EncryptorAesCbc;
}(Encryptor));
var EncryptorAesEcb = (function (_super) {
    __extends(EncryptorAesEcb, _super);
    function EncryptorAesEcb() {
        _super.apply(this, arguments);
    }
    EncryptorAesEcb.prototype.encrypt = function (text) {
        console.log("Encrypting message with AES/ECB");
        return CryptoJS.AES.encrypt(text, this.key).toString();
    };
    EncryptorAesEcb.prototype.decrypt = function (text) {
        console.log("Decrypting message with AES/ECB");
        return CryptoJS.AES.decrypt(text, this.key).toString(CryptoJS.enc.Utf8);
        return text;
    };
    return EncryptorAesEcb;
}(Encryptor));
