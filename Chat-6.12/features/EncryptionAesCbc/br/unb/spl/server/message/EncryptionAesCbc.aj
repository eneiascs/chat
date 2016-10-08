import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import br.unb.spl.server.message.Message;

public aspect EncryptionAesCbc{protected static final Logger debugLog=Logger.getLogger("debugLogger");



	void around(): execution(void onCreate()){
		proceed();
		Message message =(Message) thisJoinPoint.getThis();
		Encryptor encryptor=new Encryptor();
		message.setText(encryptor.encrypt(message.getText()));
		
		return;
		
		
		}
	
	void around(): execution(void handleMessage*()){
		System.out.println("Handle Encryption");

		Encryptor encryptor=new Encryptor();
		Message message =(Message) thisJoinPoint.getThis();
		message.setText(encryptor.decrypt(message.getText()));
		proceed();
		
		return;
		
	}
	
	
}