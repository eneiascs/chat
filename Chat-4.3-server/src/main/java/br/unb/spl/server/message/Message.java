package br.unb.spl.server.message;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message implements IMessage {
	protected String text;

	protected String textColor;
	protected String backgroundColor;

	public IMessage handleMessage(String text) {

		ObjectMapper mapper = new ObjectMapper();
		Message message;
		try {
			message = (Message) mapper.readValue(text.getBytes(), Message.class);
			this.setBackgroundColor(message.getBackgroundColor());
			this.setTextColor(message.getTextColor());
			this.setText(message.getText());

			return message;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
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