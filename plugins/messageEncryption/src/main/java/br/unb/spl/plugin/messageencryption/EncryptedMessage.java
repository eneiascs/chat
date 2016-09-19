package br.unb.spl.plugin.messageencryption;

import br.unb.spl.server.message.IMessage;
import br.unb.spl.server.message.MessageDecorator;

public class EncryptedMessage extends MessageDecorator {
	protected IEncryptorStrategy encryptor;

	public EncryptedMessage(IMessage message) {
		super(message);

		encryptor = EncryptorFactory.getInstance();

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
