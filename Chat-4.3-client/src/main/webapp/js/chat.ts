/// <reference path="typings/angularjs/angular.d.ts" />
/// <reference path="typings/angularjs/angular-resource.d.ts" />

var chat = angular.module('chat', ['angularjs-crypto']);

chat.factory('configFactory', ['$http', function($http) {

    var urlBase = 'http://localhost:8090/config';
    var configFactory = {};

    configFactory.getConfig = function() {
        return $http.get(urlBase);
    };

    return configFactory;
}]);
chat.factory('userFactory', ['$http', function($http) {

    var urlBase = 'http://localhost:8090';
    var userFactory = {};

    userFactory.registerUser = function(user) {
        return $http.post(urlBase + '/register', user);
    };
    userFactory.registerUser = function(user) {
        return $http.post(urlBase + '/authenticate', user);
    };

    return userFactory;
}]);
chat.run(function(cfCryptoHttpInterceptor, $rootScope) {



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
        $scope.sendToServer = function(text,textColor,backgroundColor) {
            text = $scope.username + ": " + text;
            
            let message:IMessage=new Message();
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
                    let msg = JSON.parse(evt.data)

                    let message: IMessage = new Message();

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
                method: 'POST',
                url: 'http://' + $scope.server + '/users/authenticate',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: {

                    username: $scope.username,
                    password: $scope.password
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
                    'color': textColor,
                    'background-color': backgroundColor
                };
            }
        }
        $scope.register = function() {
            $scope.error = "";
            if (!$scope.isRegister) {
                $scope.isRegister = true;
            } else {

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
                $scope.safeApply(function() {
                    $scope.text = "";
                    $scope.textColor = "black";
                    $scope.backgroundColor = "white";

                }
                


                $scope.config = response.data;
                config = $scope.config;
                console.log($scope.config);

                $scope.config.encryptMessages = !angular
                    .isUndefined($scope.config.ClientAES_CBCEncryption);


                $scope.messages = [];

                if ($scope.config.ClientAuthentication == undefined) {
                    $scope.username = "guest"
                        + Math.floor((Math.random() * 200) + 1);
                } else {
                    $scope.username = ""
                }
                $scope.password = "";
                $scope.confirmPassword = "";
                $scope.isRegister = false;









                let message: IMessage = new Message();
                $scope.message = message;









            }, function(error) {
                console.log('Unable to load config data: ' + error.message);
            });

    });
interface IMessage {

    public handleMessage(text: string): IMessage;
    
     public createMessage(text: string): IMessage;

     public getText(): string;
    
    public setText(text: string): void; 
    public getTextColor(): string;
    public setTextColor(textColor: string): void;
   public getBackgroundColor(): string;

    public setBackgroundColor(backgroundColor: string): void;

}
class Message implements IMessage {
    protected text: string;
    protected textColor: string;
    protected backgroundColor: string;

    constructor() {

    }
    public handleMessage(text: string): IMessage {
        let message: IMessage = new Message();
        message.setText(text);
        return message;
    }

    public createMessage(text: string): IMessage {
        let message: IMessage = new Message();
        message.setText(text);
       
        return message;
    }


    public getText(): string {
        return this.text;
    }

    public setText(text: string): void {
        this.text = text;
    }
    public getTextColor(): string {
        return this.textColor;
    }

    public setTextColor(textColor: string): void {
        this.textColor = textColor;
    }
    public getBackgroundColor(): string {
        return this.backgroundColor;
    }

    public setBackgroundColor(backgroundColor: string): void {
        this.backgroundColor = backgroundColor;
    }
}
abstract class MessageDecorator implements IMessage {
    protected _message: IMessage;

    constructor(message: IMessage) {
        this._message = message;
    }

    public handleMessage(text: string): IMessage {

        let message: IMessage = this._message.handleMessage(text);
        return message;
    }

    public createMessage(text: string): IMessage {
        let message: IMessage = this._message.createMessage(text)
        return message;
    }

    public getText(): string {

        return this._message.getText();
    }

    public setText(text: string): void {
        this._message.setText(text);

    }
    public getTextColor(): string {
        return this.textColor;
    }

    public setTextColor(textColor: string): void {
        this.textColor = textColor;
    }
    public getBackgroundColor(): string {
        return this.backgroundColor;
    }

    public setBackgroundColor(backgroundColor: string): void {
        this.backgroundColor = backgroundColor;
    }
}
class EncryptedMessage extends MessageDecorator {
    protected encryptor: Encryptor;

    constructor(message: IMessage) {
        super(message);

        if (config.ClientAES_ECBEncryption == undefined) {
            this.encryptor = new EncryptorAesEcb();
        }
        if (config.ClientAES_CBCEncryption == undefined) {
            this.encryptor = new EncryptorAesCbc();
        }


    }


    public handleMessage(text: string): IMessage {
        console.log("Handle Encrypted Message");
        let message: IMessage = super.handleMessage(text);
        message.setText(this.encryptor.decrypt(message.getText()));
        return message;
    }

    public createMessage(text: string): IMessage {
        console.log("Create Encrypted Message");
        let message: IMessage = super.createMessage(text);
        message.setText(this.encryptor.encrypt(message.getText()));
        return message;
    }
}
class HistoryMessage extends MessageDecorator {

    constructor(message: IMessage) {
        super(message);

    }


    public handleMessage(text: string): IMessage {
        console.log("Handle Message History");
        let message: IMessage = super.handleMessage(text);
        //message.setText(encryptor.decrypt(message.getText()));
        return message;
    }

    public createMessage(text: string): IMessage {
        console.log("Create Message History");
        let message: IMessage = super.createMessage(text);
        //message.setText(encryptor.encrypt(message.getText()));
        return message;
    }
}
abstract class Encryptor {

    protected key: string = "Secret Passphrase";

    public setKey(key: string): void {
        this.key = key;
    }

    public abstract encrypt(text: string): string;

    public abstract decrypt(text: string): string;

}
class EncryptorAesCbc extends Encryptor {
    protected iv: string = CryptoJS.enc.Base64.parse("RandomInitVector");
    protected key: string = CryptoJS.enc.Base64.parse("Secret Passphrase");



    public encrypt(text: string): string {
        console.log("Encrypting message with AES/CBC");
        let enc = CryptoJS.AES.encrypt(text, this.key, {
            iv: this.iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        });

        return enc.ciphertext.toString(CryptoJS.enc.Base64);

    }


    public decrypt(text: string): string {
        console.log("Decrypting message with AES/CBC");
        let cipherParams = CryptoJS.lib.CipherParams.create({
            ciphertext: CryptoJS.enc.Base64.parse(text)
        });
        let decrypted = CryptoJS.AES.decrypt(cipherParams,
            this.key, {
                iv: this.iv,
                mode: CryptoJS.mode.CBC,
                padding: CryptoJS.pad.Pkcs7
            });

        return decrypted.toString(CryptoJS.enc.Utf8);



    }

}
class EncryptorAesEcb extends Encryptor {


    public encrypt(text: string): string {
        console.log("Encrypting message with AES/ECB");

        return CryptoJS.AES.encrypt(text, this.key).toString();

    }


    public decrypt(text: string): string {
        console.log("Decrypting message with AES/ECB");
        return CryptoJS.AES.decrypt(text, this.key).toString(CryptoJS.enc.Utf8)

        return text;
    }

}




