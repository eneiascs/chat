package br.unb.spl.server.message;

public interface IMessage {

	public IMessage handleMessage(String text);

	public IMessage createMessage(String text);

	public String getText();

	public void setText(String text);

	public String getTextColor();

	public void setTextColor(String textColor);

	public String getBackgroundColor();

	public void setBackgroundColor(String backgroundColor);
}
