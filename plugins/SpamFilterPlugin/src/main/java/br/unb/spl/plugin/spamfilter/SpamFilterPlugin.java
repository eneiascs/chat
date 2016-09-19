package br.unb.spl.plugin.spamfilter;

import br.unb.spl.server.message.MessageHandleDecoratorFactory;
import br.unb.spl.server.plugin.IPlugin;

public class SpamFilterPlugin implements IPlugin {

	public void register() {

		MessageHandleDecoratorFactory.addDecorator(SpamMessage.class);
	}

}
