package br.unb.spl.server.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import br.unb.spl.server.servlet.ChatWebSocket;
import br.unb.spl.server.servlet.ChatWebSocketServlet;


public class StartWebSocketServer {

    public static void main(String[] args) throws Exception {
        new StartWebSocketServer().startJetty();
    }

	private void startJetty() {
		
		
		Server server = new Server(8090);
		 
        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");
        ctx.addServlet(ChatWebSocketServlet.class, "/chat"); 
        server.setHandler(ctx);
 
        try {
			server.start();
			server.join();
        } catch (Exception e) {
			
			e.printStackTrace();
		}
        
		
	}
}
