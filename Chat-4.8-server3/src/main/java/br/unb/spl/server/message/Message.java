package br.unb.spl.server.message;

public class Message implements IMessage {
	protected String text;

	protected String textColor;
	protected String backgroundColor;

	public IMessage handleMessage(String text) {
		IMessage message = new Message();
		message.setText(text);
		return message;
	}

	public IMessage createMessage(String text) {
		Message message = new Message();
		message.setText(text);

		return message;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

}