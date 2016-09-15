package br.unb.spl.server.message;

import org.apache.log4j.Logger;

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
