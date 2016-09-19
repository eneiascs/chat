package br.unb.spl.server.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/message")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class MessageController {
	private static final Logger debugLog = Logger.getLogger("debugLogger");

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/create", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public IMessage createMessage(@RequestBody Message msg) {
		debugLog.info(String.format("Creating message"));
		IMessage message = MessageCreateDecoratorFactory.getInstance();

		message = message.createMessage(msg.getText());
		message.setTextColor(msg.getTextColor());

		message.setBackgroundColor(msg.getBackgroundColor());
		return message;
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/handle", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public IMessage handleMessage(@RequestBody Message msg) {
		debugLog.info(String.format("Handling message"));
		IMessage message = MessageHandleDecoratorFactory.getInstance();

		message = message.handleMessage(msg.getText());
		message.setBackgroundColor(msg.getBackgroundColor());
		message.setTextColor(msg.getTextColor());
		return message;
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/history", method = RequestMethod.GET, headers = { "Content-type=application/json" })
	@ResponseBody
	public List<IMessage> getHistory() {
		debugLog.info(String.format("History"));
		List<IMessage> history = new ArrayList<IMessage>();
		Message message = new Message();
		message.setText("Message");
		history.add(message);
		return history;
	}
}