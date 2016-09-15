package br.unb.spl.server.cipher;

import org.apache.log4j.Logger;

public abstract class Encryptor {
	protected static final Logger debugLog = Logger.getLogger("debugLogger");

	protected String key;

	public void setKey(String key) {
		this.key = key;
	}

	public abstract String encrypt(String text);

	public abstract String decrypt(String text);

}