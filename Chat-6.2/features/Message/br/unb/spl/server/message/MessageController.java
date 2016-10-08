package br.unb.spl.server.message;

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
	public Message createMessage(@RequestBody Message msg) {
		debugLog.info(String.format("Creating message"));
		

		msg.onCreate();
		
		return msg;
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/handle", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Message handleMessage(@RequestBody Message msg) {
		debugLog.info(String.format("Handling message"));
		

		msg.handleMessage();
		
		return msg;
	}

}