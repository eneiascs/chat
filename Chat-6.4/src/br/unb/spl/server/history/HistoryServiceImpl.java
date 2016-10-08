package br.unb.spl.server.history;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;


import br.unb.spl.server.message.Message;

@Component
public class HistoryServiceImpl implements HistoryService {
	private List<Message> history = new ArrayList<Message>();

	public void addToHistory(Message message) {
		if (history.size() == 10) {
			history.remove(0);

		}
		history.add(message);
		
	}

	public List<Message> getHistory() {
		return history;

	}

}
