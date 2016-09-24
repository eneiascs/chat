package br.unb.spl.server.history;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unb.spl.module.authentication.HtmlLoader;
import br.unb.spl.server.message.IMessage;

@RequestMapping("/")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class HistoryController {

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/html/history", method = RequestMethod.GET, produces = "text/html")

	public @ResponseBody String getDivColor() {

		return HtmlLoader.getHtml("history.html");
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/history", method = RequestMethod.GET, produces = "application/json")

	public @ResponseBody List<IMessage> getHistory() {
		HistoryModule historyModule = HistoryModule.getInstance();
		return historyModule.getHistory();
	}
}