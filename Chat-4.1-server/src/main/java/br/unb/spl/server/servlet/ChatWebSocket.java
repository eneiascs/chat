package br.unb.spl.server.servlet;

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

import br.unb.spl.server.dto.MessageDTO;
import br.unb.spl.server.dto.Sessions;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class ChatWebSocket {

	private static final Logger logger = Logger.getLogger(ChatWebSocket.class);
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
		logger.info(String.format("Connection closed: %d - %s%n", statusCode, reason));
		// for (Session s : Sessions.getInstance().getSessions()) {
		// if (!s.isOpen()) {
		//
		// sendAll(String.format("%s has left the chat",s.getRemoteAddress()));

		// }
		//
		// }

		// this.session = null;
		this.closeLatch.countDown(); // trigger latch
		Sessions.getInstance().getSessions().remove(session);
		logger.info(String.format("Active sessions: %d ", Sessions.getInstance().getSessions().size()));

	}

	@OnWebSocketConnect
	public void onConnect(Session session) {

		String username = getParameterValue(session, "username");

		// String password = getParameterValue(session, "password");
		// User user = new User(username, password);
		//
		// if (!userService.authenticateUser(user).equals("OK")) {
		// session.close();
		// return;
		// }

		logger.info(String.format("Got connect: %s %n", username));

		this.session = session;
		Sessions.getInstance().getSessions().add(session);
		logger.info(String.format("Active sessions: %d ", Sessions.getInstance().getSessions().size()));
		MessageDTO messageOrigin = new MessageDTO();
		messageOrigin.setText(String.format("Connected as %s", username));

		MessageDTO messageAll = new MessageDTO();
		messageAll.setText(String.format("%s has joined the chat", username));

		// send(messageOrigin);
		// sendAll(messageAll);
	}

	@OnWebSocketMessage
	public void handleMessage(String message) {
		ObjectMapper mapper = new ObjectMapper();
		MessageDTO messageDTO;
		try {
			messageDTO = (MessageDTO) mapper.readValue(message.getBytes(), MessageDTO.class);
			logger.info(String.format("Message received from %s: %s%n", getParameterValue(session, "username"),
					messageDTO.getText()));

			// messageDTO.setText(String.format("%s: %s",
			// getParameterValue(session, "username"), messageDTO.getText()));
			sendAll(messageDTO);
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
	private void send(MessageDTO message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (session.isOpen()) {

				session.getRemote().sendString(mapper.writeValueAsString(message));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendAll(MessageDTO messageDTO) {
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
