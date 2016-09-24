package br.unb.spl.module.encryption;

public class EncryptionModule implements IEncryptionModule {
	private static EncryptionModule encryptionModule = new EncryptionModule();

	private IEncryptorStrategy encryptor;

	public EncryptionModule() {
		super();
		EncryptorFactory.setInstanceClass(Encryptor.class);
		encryptor = EncryptorFactory.getInstance();
	}

	public static EncryptionModule getInstance() {
		return encryptionModule;
	}

	public String encrypt(String text) {

		return encryptor.encrypt(text);
	}

	public String decrypt(String text) {

		return encryptor.decrypt(text);
	}
}
