package br.unb.spl.server.message;

import org.apache.log4j.Logger;

public class HistoryHandleMessageObserver implements IHandleMessageObserver {
	private static final Logger messagesLog = Logger.getLogger("messagesLogger");

	public void beforeHandleMessage(String message) {

	}

	public void afterHandleMessage(IMessage message) {
		messagesLog.info(String.format("Message received: %s%n", message.getText()));
	}

}
