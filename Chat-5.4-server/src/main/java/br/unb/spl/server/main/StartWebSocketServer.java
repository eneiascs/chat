package br.unb.spl.server.main;

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import br.unb.spl.server.servlet.ChatWebSocketServlet;


public class StartWebSocketServer {

	private static final String CONFIG_LOCATION = "br.unb.spl.server.config";
	private static final String MAPPING_URL = "/*";
	private static final String DEFAULT_PROFILE = "dev";

	public static void main(String[] args) throws Exception {
		new StartWebSocketServer().startJetty();
	}

	private void startJetty() {

		/* if[Color]
		 
		 System.out.println("Color...");
		
		 end[Color]*/
		
		
		/* if[Authentication]
		 
		 System.out.println("Authentication...");
		
		 end[Authentication]*/
		try {

			Server server = new Server(8090);
			server.setHandler(getServletContextHandler(getContext()));
			server.start();
			server.join();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setErrorHandler(null);
		contextHandler.setContextPath("/");
		contextHandler.addServlet(ChatWebSocketServlet.class, "/chat");
		contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
		contextHandler.addEventListener(new ContextLoaderListener(context));
		// contextHandler.setResourceBase(new
		// ClassPathResource("webapp").getURI().toString());
		return contextHandler;
	}

	private static WebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation(CONFIG_LOCATION);
		context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE);
		return context;
	}
}
