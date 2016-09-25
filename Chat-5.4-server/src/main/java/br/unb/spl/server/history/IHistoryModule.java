package br.unb.spl.server.history;

import java.util.List;

import br.unb.spl.server.message.IMessage;

public interface IHistoryModule {
	public void addToHistory(IMessage message);

	public List<IMessage> getHistory();
}
