package br.unb.spl.plugin.messageencryption;

public class EncryptorFactory {
	private static Class<?> instanceClass;

	public static Class<?> getInstanceClass() {
		return instanceClass;
	}

	public static void setInstanceClass(Class<?> ic) {
		instanceClass = ic;
	}

	public static IEncryptorStrategy getInstance() {
		try {
			if (instanceClass != null) {
				return (IEncryptorStrategy) instanceClass.newInstance();
			} else {
				return null;
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}