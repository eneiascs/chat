package br.unb.spl.server.message;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unb.spl.module.encryption.EncryptionModule;
import br.unb.spl.module.encryption.IEncryptionModule;

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
		IMessage message = new Message();
		IEncryptionModule encryptionModule = EncryptionModule.getInstance();

		message.setText(encryptionModule.encrypt(msg.getText()));
		message.setTextColor(msg.getTextColor());

		message.setBackgroundColor(msg.getBackgroundColor());
		return message;
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/handle", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public IMessage handleMessage(@RequestBody Message msg) {
		IEncryptionModule encryptionModule = EncryptionModule.getInstance();
		msg.setText(encryptionModule.decrypt((msg.getText())));

		return msg;
	}

}