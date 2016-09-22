package br.unb.spl.server.history;

import java.util.List;

import br.unb.spl.server.message.IMessage;

public class HistoryModule implements IHistoryModule {
	private static HistoryModule historyModule = new HistoryModule();
	private HistoryService historyService = new HistoryServiceImpl();

	public HistoryModule() {
		super();
	}

	public static HistoryModule getInstance() {
		return historyModule;
	}

	public void addToHistory(IMessage message) {
		historyService.addToHistory(message);

	}

	public List<IMessage> getHistory() {

		return historyService.getHistory();
	}

}
