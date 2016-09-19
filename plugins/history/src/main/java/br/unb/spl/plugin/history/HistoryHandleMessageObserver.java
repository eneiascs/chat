package br.unb.spl.plugin.history;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import br.unb.spl.server.config.ApplicationContextHolder;
import br.unb.spl.server.message.IHandleMessageObserver;
import br.unb.spl.server.message.IMessage;

@Service
@Configurable
public class HistoryHandleMessageObserver implements IHandleMessageObserver {
	private static final Logger messagesLog = Logger.getLogger("messagesLogger");

	public void beforeHandleMessage(String message) {

	}

	public void afterHandleMessage(IMessage message) {
		HistoryService historyService = ApplicationContextHolder.getContext().getBean(HistoryService.class);
		messagesLog.info(String.format("Message received: %s%n", message.getText()));
		historyService.addToHistory(message);
	}

}
