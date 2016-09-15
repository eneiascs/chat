package br.unb.spl.server.message;

public abstract class MessageDecorator implements IMessage {
	protected IMessage message;

	public MessageDecorator(IMessage message) {
		this.message = message;
	}

	public IMessage handleMessage(String text) {

		return message.handleMessage(text);
	}

	public IMessage createMessage(String text) {

		return message.createMessage(text);
	}

	public String getText() {

		return message.getText();
	}

	public void setText(String text) {
		message.setText(text);

	}

}
