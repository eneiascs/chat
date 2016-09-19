package br.unb.spl.server.message;

import java.util.ArrayList;
import java.util.List;

public class HandleMessageSubject {
	List<IHandleMessageObserver> observers = new ArrayList<IHandleMessageObserver>();

	public void register(IHandleMessageObserver o) {
		observers.add(o);
	}

	public void unregister(IHandleMessageObserver o) {
		observers.remove(o);
	}

	public void notifyBeforeHandleMessage(String text) {
		for (IHandleMessageObserver observer : observers) {
			observer.beforeHandleMessage(text);
		}
	}

	public void notifyAfterHandleMessage(IMessage message) {
		for (IHandleMessageObserver observer : observers) {
			observer.afterHandleMessage(message);
		}
	}

}