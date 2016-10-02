package br.unb.spl.server.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/* if[Encryption]
import br.unb.spl.module.encryption.EncryptionModule;
end[Encryption]*/
import br.unb.spl.server.main.Sessions;
import br.unb.spl.server.message.Message;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class ChatWebSocket {
	private static final Logger debugLog = Logger.getLogger("debugLogger");

	// @Autowired
	// private UserService userService;

	private final CountDownLatch closeLatch;

	private Session session;
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	public ChatWebSocket() {
		this.closeLatch = new CountDownLatch(1);

	}

	public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
		return this.closeLatch.await(duration, unit);
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		debugLog.info(String.format("Connection closed: %d - %s%n", statusCode, reason));

		this.closeLatch.countDown(); // trigger latch
		Sessions.getInstance().getSessions().remove(session);
		debugLog.info(String.format("Active sessions: %d ", Sessions.getInstance().getSessions().size()));

	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		String username = getParameterValue(session, "username");

		debugLog.info(String.format("Got connect: %s %n", username));

		this.session = session;
		Sessions.getInstance().getSessions().add(session);
		debugLog.info(String.format("Active sessions: %d ", Sessions.getInstance().getSessions().size()));

		Message messageAll = new Message();
		messageAll.setText(String.format("%s has joined the chat", username));
		messageAll.onCreate();
	
		
		
		
		
		sendAll(messageAll);
	}

	@OnWebSocketMessage
	public void handleMessage(String text) {
		

		ObjectMapper mapper = new ObjectMapper();
		Message msg;
		try {
			msg = (Message) mapper.readValue(text.getBytes(), Message.class);
			sendAll(msg);
			msg.handleMessageServer();
			
			

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// sends message to browser
	private void send(Message message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (session.isOpen()) {

				session.getRemote().sendString(mapper.writeValueAsString(message));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendAll(Message messageDTO) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			for (Session s : Sessions.getInstance().getSessions()) {
				if (s.isOpen()) {
					s.getRemote().sendString(mapper.writeValueAsString(messageDTO));
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getParameterValue(Session session, String key) {
		Map<String, List<String>> parameters = session.getUpgradeRequest().getParameterMap();

		String value = null;
		if (parameters.containsKey(key)) {
			value = parameters.get(key).get(0);
		}
		return value;
	}

	// closes the socket
	private void stop() {
		try {
			session.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
