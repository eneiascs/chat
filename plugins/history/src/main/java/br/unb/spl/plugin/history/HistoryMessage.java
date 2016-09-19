package br.unb.spl.plugin.history;

import org.apache.log4j.Logger;

import br.unb.spl.server.message.IMessage;
import br.unb.spl.server.message.MessageDecorator;

public class HistoryMessage extends MessageDecorator {
	private static final Logger messagesLog = Logger.getLogger("messagesLogger");

	public HistoryMessage(IMessage message) {
		super(message);

	}

	@Override
	public IMessage handleMessage(String text) {

		IMessage message = super.handleMessage(text);
		messagesLog.info(String.format("Message received: %s%n", message.getText()));

		return message;
	}

}
