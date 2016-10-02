package br.unb.spl.server.history;

import java.util.List;

import br.unb.spl.server.message.Message;

public interface HistoryService {
	public void addToHistory(Message message);

	public List<Message> getHistory();
}
