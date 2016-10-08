import java.util.List;

import br.unb.spl.server.config.ApplicationContextHolder;
import br.unb.spl.server.history.HistoryLoader;
import br.unb.spl.server.history.HistoryService;
import br.unb.spl.server.main.HtmlLoader;
import br.unb.spl.server.message.Message;
public aspect History {
String around(): execution(String HistoryLoader.getDivHistory()){
		
		return HtmlLoader.getHtml("history.html");
		
	}

List<Message> around(): execution(List<Message> HistoryLoader.getHistory()){
	HistoryService historyService = ApplicationContextHolder.getContext().getBean(HistoryService.class);
	return historyService.getHistory();
	
	
	}


void around(): execution(void handleMessageServer()){

	Message message =(Message) thisJoinPoint.getThis();
	proceed();
	HistoryService historyService = ApplicationContextHolder.getContext().getBean(HistoryService.class);
	 historyService.addToHistory(message);
	return;
	
	
	}
}