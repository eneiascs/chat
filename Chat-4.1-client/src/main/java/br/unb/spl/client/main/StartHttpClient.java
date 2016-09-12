package br.unb.spl.client.main;


import javax.servlet.http.HttpServletRequest;


import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class StartHttpClient {
	private static final int DEFAULT_PORT = 8080;
    
    public static void main(String[] args) throws Exception {
        new StartHttpClient().startJetty(getPortFromArgs(args));
    }
    private static int getPortFromArgs(String[] args) {
        if (args.length > 0) {
            try {
                return Integer.valueOf(args[0]);
            } catch (NumberFormatException ignore) {
            }
        }
        return DEFAULT_PORT;
    }

    private void startJetty(int port) throws Exception {
    	Server server = new Server();
    	SocketConnector connector = new SocketConnector();
		
		final String contextPath = "/chat";

		// Set some timeout options to make debugging easier.
		connector.setMaxIdleTime(1000 * 60 * 60);
		connector.setSoLingerTime(-1);
		connector.setPort(port);
		server.setConnectors(new Connector[] { connector });

		WebAppContext bb = new WebAppContext();
		bb.setServer(server);
		bb.setContextPath(contextPath);
		bb.setWar("src/main/webapp");

		
		server.addHandler(bb);

		
		
		try {
			server.start();
			System.out
					.println(">> Jetty iniciado. Pressione qualquer tecla para sair");
			System.out.println(">> Abra a URL http://localhost:" + port
					+ contextPath);

			while (System.in.available() == 0) {
				Thread.sleep(1000);
			}

			server.stop();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
    	
//    	Server server = new Server(port);
//        server.setHandler(new ChatHandler());
//        server.start();
//        server.join();
    }

  

}
