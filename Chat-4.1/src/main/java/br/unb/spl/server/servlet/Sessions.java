package br.unb.spl.server.servlet;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;

public class Sessions {
private static Sessions instance;

private List<Session> sessions;

public List<Session> getSessions() {
	if(sessions==null){
		sessions= new ArrayList<Session>();
	}
	return sessions;
}

public void setSessions(List<Session> sessions) {
	this.sessions = sessions;
}
public static Sessions getInstance(){
	if(instance==null){
		instance=new Sessions();
		
	}
	return instance;
}
}
