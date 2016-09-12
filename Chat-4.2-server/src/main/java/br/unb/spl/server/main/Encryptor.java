package br.unb.spl.server.main;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Encryptor {
	private static final String CIPHER_INSTANCE = "AES/CBC/PKCS5Padding";
	private static final Charset UTF8 = Charset.forName("UTF-8");

	public static String encrypt(String key, String initVector, String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));

			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String decrypt(String key, String initVector, String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) {

		try {
			String base64Cipher = "mim//3YP5Xbdubmi9FPXBA==";
			byte[] cipherBytes = Base64.decodeBase64(base64Cipher);
			byte[] iv = "RandomInitVector".getBytes(UTF8);
			byte[] keyBytes = "Bar12345Bar12345".getBytes(UTF8);

			SecretKey aesKey = new SecretKeySpec(keyBytes, "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
			cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));

			byte[] result = cipher.doFinal(cipherBytes);
			System.out.println(new String(result));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// String key = "d9beded79d7adbc69e7766ba69b7fbd79f3cd3d71fe1fddc";
		// Base64.encodeBase64String("2b7e151628aed2a6abf7158809cf4f3c".getBytes());
		// // 128
		// bit
		// key
		String key = "1234567812345678";
		String initVector = "1234567812345678"; // 16 bytes IV

		System.out.println(decrypt(key, initVector, encrypt(key, initVector, "t: t")));
		// System.out.println(decrypt(key, initVector,
		// "SkvRw/0pOwZZa9BrR/EUDA=="));

	}
}