package br.unb.spl.plugin.messageencryption;

public interface IEncryptorStrategy {
	public String encrypt(String text);

	public String decrypt(String text);
}
