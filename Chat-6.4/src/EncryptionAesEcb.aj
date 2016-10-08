import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import br.unb.spl.server.message.Message;

public aspect EncryptionAesEcb {
	protected static final Logger debugLog = Logger.getLogger("debugLogger");
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
		
		protected String key = "1234567812345678";

		public String encrypt(String text) {
			debugLog.info(String.format("Encrypting message %s with AES/ECB", text));

			try {

				SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

				byte[] encrypted = cipher.doFinal(text.getBytes());
				System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));

				return Base64.encodeBase64String(encrypted);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;

		}

		public String decrypt(String text) {
			debugLog.info(String.format("Decrypting message with AES/ECB/NOPADDING"));
			try {

				SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);

				byte[] original = cipher.doFinal(Base64.decodeBase64(text));

				return new String(original);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;

		}

	}
}