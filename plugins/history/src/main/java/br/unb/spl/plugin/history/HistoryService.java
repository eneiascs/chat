package br.unb.spl.plugin.history;

import java.util.List;

import br.unb.spl.server.message.IMessage;

public interface HistoryService {
	public void addToHistory(IMessage message);

	public List<IMessage> getHistory();
}
