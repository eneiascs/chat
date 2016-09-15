package br.unb.spl.server.message;

public interface IHandleMessageObserver {
	public void beforeHandleMessage(String text);

	public void afterHandleMessage(IMessage message);
}
