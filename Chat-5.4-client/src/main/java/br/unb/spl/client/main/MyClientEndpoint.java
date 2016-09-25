package br.unb.spl.client.main;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.unb.spl.server.message.IMessage;
import br.unb.spl.server.message.Message;

@ClientEndpoint
public class MyClientEndpoint {
	private Session session;
    @OnOpen
    public void onOpen(Session session) {
    	this.session=session;
    	System.out.println("Connected to endpoint: " + session.getBasicRemote());
       
    }

    @OnMessage
    public void processMessage(String text) {
        
    	ObjectMapper mapper = new ObjectMapper();
		Message msg;
		try {
			msg = (Message) mapper.readValue(text.getBytes(), Message.class);
				
			System.out.println("Received message in client: " + msg.getText());
			StartHttpClient.messageLatch.countDown();
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

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }
    private void send(IMessage message) {
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