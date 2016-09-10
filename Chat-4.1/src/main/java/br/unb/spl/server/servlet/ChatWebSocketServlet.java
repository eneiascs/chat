package br.unb.spl.server.servlet;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;


public class ChatWebSocketServlet extends WebSocketServlet {

	@Override
	public void configure(WebSocketServletFactory factory) {
		//factory.getPolicy().setIdleTimeout(10000);
		factory.register(ChatWebSocket.class);
		
	}
	
	
   }
