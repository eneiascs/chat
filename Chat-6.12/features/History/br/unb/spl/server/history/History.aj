package br.unb.spl.server.history;

import java.util.List;

import br.unb.spl.server.config.ApplicationContextHolder;
import br.unb.spl.server.history.HistoryLoader;
import br.unb.spl.server.history.HistoryService;
import br.unb.spl.server.main.HtmlLoader;
import br.unb.spl.server.message.Message;
public aspect History {


void around(): execution(void handleMessageServer()){
	System.out.println("around handleMessageServer");	
	Message message =(Message) thisJoinPoint.getThis();
	proceed();
	HistoryService historyService = ApplicationContextHolder.getContext().getBean(HistoryService.class);
	 historyService.addToHistory(message);
	return;
	
	
	}
}