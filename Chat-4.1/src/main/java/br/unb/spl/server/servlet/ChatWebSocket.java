package br.unb.spl.server.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


@WebSocket(maxTextMessageSize = 64 * 1024)
public class ChatWebSocket {
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
		System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
//		for (Session s : Sessions.getInstance().getSessions()) {
//			if (!s.isOpen()) {
//				
//				sendAll(String.format("%s has left the chat",s.getRemoteAddress()));
//				//Sessions.getInstance().getSessions().remove(s);
//			}
//
//		}
		
		
		this.session = null;
		this.closeLatch.countDown(); // trigger latch
		System.out.printf("Active sessions: %d ", Sessions.getInstance().getSessions().size());
		
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		System.out.printf("Got connect: %s%n", session);
		this.session = session;
		Sessions.getInstance().getSessions().add(session);
		System.out.printf("Active sessions: %d ", Sessions.getInstance().getSessions().size());
		send(String.format("Connected as %s",session.getRemoteAddress()));
		sendAll(String.format("%s has joinned the chat",session.getRemoteAddress()));
	}

	@OnWebSocketMessage
	public void handleMessage(String message) {

		System.out.printf("Got msg: %s  %n", message);
		
		sendAll(String.format("%s: %s", session.getRemoteAddress(),message));
	}

	// sends message to browser
	private void send(String message) {
		try {
			if (session.isOpen()) {

				session.getRemote().sendString(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendAll(String message) {
		
		try {
			for (Session s : Sessions.getInstance().getSessions()) {
				if (s.isOpen()) {
					s.getRemote().sendString(message);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
