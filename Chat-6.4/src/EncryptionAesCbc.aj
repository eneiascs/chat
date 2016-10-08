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
	
	public class Encryptor {

		private static final String CIPHER_METHOD = "AES/CBC/NoPadding";
		String key = "Bar12345Bar12345"; // 128 bit key
		String initVector = "RandomInitVector"; // 16 bytes IV

		public String encrypt(String text) {
			debugLog.info(String.format("Encrypting message with AES/CBC"));

			try {
				IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
				SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

				byte[] encrypted = cipher.doFinal(text.getBytes());
				System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));

				return Base64.encodeBase64String(encrypted);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}

		public String decrypt(String text) {
				debugLog.info(String.format("Decrypting message with AES/CBC"));
				try {
					IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
					SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
					cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

					byte[] original = cipher.doFinal(Base64.decodeBase64(text));

					return new String(original);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				return null;
			}

		}
}