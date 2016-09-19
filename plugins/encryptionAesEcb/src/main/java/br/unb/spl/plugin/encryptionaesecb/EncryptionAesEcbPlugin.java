package br.unb.spl.plugin.encryptionaesecb;

import br.unb.spl.plugin.messageencryption.EncryptedMessage;
import br.unb.spl.plugin.messageencryption.EncryptorFactory;
import br.unb.spl.server.message.MessageCreateDecoratorFactory;
import br.unb.spl.server.message.MessageHandleDecoratorFactory;
import br.unb.spl.server.plugin.IPlugin;

public class EncryptionAesEcbPlugin implements IPlugin {

	public void register() {
		EncryptorFactory.setInstanceClass(EncryptorAesEcb.class);
		MessageCreateDecoratorFactory.addDecoratorFirstElement(EncryptedMessage.class);
		MessageHandleDecoratorFactory.addDecoratorFirstElement(EncryptedMessage.class);
	}

}
