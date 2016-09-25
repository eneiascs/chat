package br.unb.spl.client.main;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.Session;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.unb.spl.server.message.IMessage;

public class StartHttpClient {
	final static CountDownLatch messageLatch = new CountDownLatch(1);

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
		/*
		  if[Graphic]
		  
		  	Server server = new Server();
		SocketConnector connector = new SocketConnector();

		final String contextPath = "/chat";

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
			System.out.println(">> Jetty iniciado. Pressione qualquer tecla para sair");
			System.out.println(">> Abra a URL http://localhost:" + port + contextPath);

			while (System.in.available() == 0) {
				Thread.sleep(1000);

			}

			server.stop();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}

		  
		  
		  end[Graphic]*/
		/* if[CommandLine]
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			String uri = "ws://localhost:8090/chat?username=command_line";
			System.out.println("Connecting to " + uri);
			Session session = container.connectToServer(MyClientEndpoint.class, URI.create(uri));
			while (session.isOpen()) {
				Scanner reader = new Scanner(System.in); // Reading from
															// System.in
				System.out.println("Message: ");
				String text = reader.next();

				Message message = new Message();

				message.setText(text);
				System.out.println("Sending message to endpoint: " + message.getText());
				send(session, message);

			}

			messageLatch.await(100, TimeUnit.SECONDS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		end[CommandLine]*/
	}

	private void send(Session session, IMessage message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (session.isOpen()) {
				session.getBasicRemote().sendText(mapper.writeValueAsString(message));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
