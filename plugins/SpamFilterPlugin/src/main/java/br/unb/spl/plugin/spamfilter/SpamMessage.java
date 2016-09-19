package br.unb.spl.plugin.spamfilter;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.unb.spl.server.message.IMessage;
import br.unb.spl.server.message.MessageDecorator;

public class SpamMessage extends MessageDecorator {
	private static final Logger debugLog = Logger.getLogger("messagesLogger");

	public SpamMessage(IMessage message) {
		super(message);

	}

	@Override
	public IMessage handleMessage(String text) {

		IMessage message = super.handleMessage(text);
		List<String> spamList = new ArrayList<String>();
		spamList.add("spam");
		spamList.add("list");
		spamList.add("forbidden");
		debugLog.info(String.format("text %s", text));
		for (String string : spamList) {
			if (text.contains(string.toLowerCase())) {
				message.setText("**Word blocked**");
			}
		}
		return message;
	}

}
