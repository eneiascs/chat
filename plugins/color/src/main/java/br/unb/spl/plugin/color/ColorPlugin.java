package br.unb.spl.plugin.color;

import br.unb.spl.server.color.ColorFactory;
import br.unb.spl.server.config.HtmlLoader;
import br.unb.spl.server.plugin.IPlugin;

public class ColorPlugin implements IPlugin {

	public void register() {
		ColorFactory.setDiv(HtmlLoader.getHtml("color.html"));
	}

}
