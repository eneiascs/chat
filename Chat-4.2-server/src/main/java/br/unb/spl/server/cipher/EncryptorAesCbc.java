package br.unb.spl.server.cipher;

import static br.unb.spl.server.cipher.Encryptor.debugLog;

public class EncryptorAesCbc extends Encryptor {
	protected String iv;

	@Override
	public String encrypt(String text) {
		debugLog.info(String.format("Encrypting message %s with AES/CBC", text));
		return text;
	}

	@Override
	public String decrypt(String text) {
		debugLog.info(String.format("Decrypting message with AES/CBC"));
		return text;
	}

}