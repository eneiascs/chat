package br.unb.spl.server.main;

import java.util.Properties;

import org.apache.log4j.Logger;

import br.unb.spl.server.config.ConfigLoader;
import br.unb.spl.server.plugin.IPlugin;

public class PluginLoader {
	protected static final Logger debugLog = Logger.getLogger("debugLogger");

	public void loadPlugins() {
		String basePackage = "br.unb.spl.plugin";
		try {
			Properties props = ConfigLoader.getConfig();

			for (Object feature : props.keySet()) {
				debugLog.info(String.format("Registering plugin %s", feature.toString()));
				Class<?> pluginClass = Class.forName(String.format("%s.%s.%sPlugin", basePackage,
						feature.toString().toLowerCase(), feature.toString()));
				IPlugin plugin = (IPlugin) pluginClass.newInstance();
				plugin.register();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
