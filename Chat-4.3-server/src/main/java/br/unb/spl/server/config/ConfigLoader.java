package br.unb.spl.server.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ConfigLoader {
	private static Properties properties;

	public static Properties getConfig() {
		try {
			if (properties == null) {
				Resource resource = new ClassPathResource("/default.config");
				properties = PropertiesLoaderUtils.loadProperties(resource);

			}
			return properties;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
