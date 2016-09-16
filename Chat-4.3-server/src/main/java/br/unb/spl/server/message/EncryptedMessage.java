package br.unb.spl.server.message;

import br.unb.spl.server.cipher.Encryptor;
import br.unb.spl.server.cipher.EncryptorAesCbc;
import br.unb.spl.server.cipher.EncryptorAesEcb;
import br.unb.spl.server.config.ConfigLoader;

public class EncryptedMessage extends MessageDecorator {
	protected Encryptor encryptor;

	public EncryptedMessage(IMessage message) {
		super(message);

		if (ConfigLoader.getConfig().getProperty("ServerAES_CBCEncryption") != null) {
			encryptor = new EncryptorAesCbc();

		}
		if (ConfigLoader.getConfig().getProperty("ServerAES_ECBEncryption") != null) {

			encryptor = new EncryptorAesEcb();

		}

	}

	@Override
	public IMessage handleMessage(String text) {

		IMessage message = super.handleMessage(text);
		message.setText(encryptor.decrypt(message.getText()));
		return message;
	}

	public IMessage createMessage(String text) {
		IMessage message = super.createMessage(text);
		message.setText(encryptor.encrypt(message.getText()));
		return message;
	}
}
