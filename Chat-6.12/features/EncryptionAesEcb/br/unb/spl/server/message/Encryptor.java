/**
 * TODO description
 */

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
public class Encryptor {
	protected static final Logger debugLog = Logger.getLogger("debugLogger");
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