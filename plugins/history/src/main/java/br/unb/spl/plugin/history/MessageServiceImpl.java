package br.unb.spl.plugin.history;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.unb.spl.server.history.HistoryFactory;
import br.unb.spl.server.message.IMessage;

@Component
public class MessageServiceImpl implements HistoryService {
	private List<IMessage> history = new ArrayList<IMessage>();

	public void addToHistory(IMessage message) {
		if (history.size() == 10) {
			history.remove(0);

		}
		history.add(message);
		HistoryFactory.setHistory(history);
	}

	public List<IMessage> getHistory() {
		return history;

	}

}
