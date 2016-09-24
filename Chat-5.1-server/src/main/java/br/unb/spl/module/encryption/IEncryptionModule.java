package br.unb.spl.module.encryption;

public interface IEncryptionModule {
	public String encrypt(String text);

	public String decrypt(String text);

}
