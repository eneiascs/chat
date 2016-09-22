package br.unb.spl.server.history;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.unb.spl.server.message.IMessage;

@Component
public class HistoryServiceImpl implements HistoryService {
	private List<IMessage> history = new ArrayList<IMessage>();

	public void addToHistory(IMessage message) {
		if (history.size() == 10) {
			history.remove(0);

		}
		history.add(message);

	}

	public List<IMessage> getHistory() {
		return history;

	}

}
