package br.unb.spl.server.history;

import java.util.List;

import br.unb.spl.server.message.IMessage;

public class HistoryFactory {
	private static String div;
	private static List<IMessage> history;

	public static String getDiv() {
		return div;
	}

	public static void setDiv(String divAuthentication) {
		HistoryFactory.div = divAuthentication;
	}

	public static void setHistory(List<IMessage> history) {
		HistoryFactory.history = history;
	}

	public static List<IMessage> getHistory() {

		return history;
	}

}
