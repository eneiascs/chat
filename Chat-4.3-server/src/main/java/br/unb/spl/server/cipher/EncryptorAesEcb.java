package br.unb.spl.server.cipher;

public class EncryptorAesEcb extends Encryptor {

	@Override
	public String encrypt(String text) {
		debugLog.info(String.format("Encrypting message %s with AES/ECB", text));

		return text;
	}

	@Override
	public String decrypt(String text) {
		debugLog.info(String.format("Decrypting message with AES/ECB"));
		return text;
	}

}