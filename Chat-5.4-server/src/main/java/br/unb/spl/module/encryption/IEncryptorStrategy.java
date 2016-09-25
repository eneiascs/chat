package br.unb.spl.module.encryption;

public interface IEncryptorStrategy {
	public String encrypt(String text);

	public String decrypt(String text);
}
