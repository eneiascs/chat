package br.unb.spl.server.message;

import java.util.ArrayList;
import java.util.List;

public class HandleMessageSubjectFactory {

	public List<IHandleMessageObserver> observers = new ArrayList<IHandleMessageObserver>();
	private static HandleMessageSubject handleMessageSubject;

	public static HandleMessageSubject getInstance() {
		if (handleMessageSubject == null) {
			handleMessageSubject = new HandleMessageSubject();
		}
		return handleMessageSubject;
	}

	public static void register(IHandleMessageObserver o) {
		getInstance().register(o);
	}

}
