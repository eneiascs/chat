package br.unb.spl.server.history;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unb.spl.plugin.history.HistoryService;
import br.unb.spl.server.message.IMessage;

@RequestMapping("/")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class HistoryController {
	@Autowired
	private HistoryService historyService;

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/html/history", method = RequestMethod.GET, produces = "text/html")

	public @ResponseBody String getDivColor() {

		return HistoryFactory.getDiv();
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/history", method = RequestMethod.GET, produces = "application/json")

	public @ResponseBody List<IMessage> getHistory() {
		return historyService.getHistory();
	}

}