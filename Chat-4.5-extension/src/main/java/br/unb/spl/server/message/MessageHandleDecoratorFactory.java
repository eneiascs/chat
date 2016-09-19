package br.unb.spl.server.message;

import java.util.ArrayList;
import java.util.List;

public class MessageHandleDecoratorFactory {
	private static List<Class<?>> decorators = new ArrayList<Class<?>>();

	public static IMessage getInstance() {
		try {
			IMessage message = new Message();

			for (Class<?> class1 : decorators) {
				message = (IMessage) class1.getDeclaredConstructor(IMessage.class).newInstance(message);
			}
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addDecorator(Class<?> class1) {
		decorators.add(class1);
	}

	public static void addDecoratorFirstElement(Class<?> class1) {
		List<Class<?>> dec = decorators;
		decorators = new ArrayList<Class<?>>();
		decorators.add(class1);
		decorators.addAll(dec);
	}
}
