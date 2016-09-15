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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.unb.spl.server.controller.ConfigLoader;
import br.unb.spl.server.dto.Sessions;
import br.unb.spl.server.message.EncryptedMessage;
import br.unb.spl.server.message.HandleMessageSubject;
import br.unb.spl.server.message.HistoryHandleMessageObserver;
import br.unb.spl.server.message.HistoryMessage;
import br.unb.spl.server.message.IMessage;
import br.unb.spl.server.message.Message;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class ChatWebSocket {
	private static final Logger debugLog = Logger.getLogger("debugLogger");

	// @Autowired
	// private UserService userService;

	private final CountDownLatch closeLatch;

	private Session session;
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	private HandleMessageSubject handleMessageSubject;

	public ChatWebSocket() {
		this.closeLatch = new CountDownLatch(1);
		this.handleMessageSubject = new HandleMessageSubject();

		// Feature History
		if (ConfigLoader.getConfig().getProperty("ServerMessagesHistory") != null) {
			handleMessageSubject.register(new HistoryHandleMessageObserver());
		}
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

		IMessage message = new Message();
		IMessage messageAll = new Message();

		// Feature Encryption
		if (ConfigLoader.getConfig().getProperty("ServerMessageEncryption") != null) {
			message = new EncryptedMessage(message);
			messageAll = new EncryptedMessage(messageAll);

		}

		message.createMessage(String.format("Connected as %s", username));
		messageAll.createMessage(String.format("%s has joined the chat", username));

		send(message);
		sendAll(messageAll);
	}

	@OnWebSocketMessage
	public void handleMessage(String text) {
		handleMessageSubject.notifyBeforeHandleMessage(text);
		IMessage message = new Message();

		if (ConfigLoader.getConfig().getProperty("ServerMessageDecryption") != null) {
			message = new EncryptedMessage(message);
		}
		if (ConfigLoader.getConfig().getProperty("ServerMessagesHistory") != null) {
			message = new HistoryMessage(message);
		}

		message.handleMessage(text);

		message.createMessage(message.getText());

		sendAll(message);

		handleMessageSubject.notifyAfterHandleMessage(message);

	}

	// sends message to browser
	private void send(IMessage message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (session.isOpen()) {

				session.getRemote().sendString(mapper.writeValueAsString(message));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendAll(IMessage messageDTO) {
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
