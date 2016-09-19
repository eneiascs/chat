package br.unb.spl.plugin.authentication;

import br.unb.spl.server.authentication.AuthenticationFactory;
import br.unb.spl.server.config.HtmlLoader;
import br.unb.spl.server.plugin.IPlugin;

public class AuthenticationPlugin implements IPlugin {

	public void register() {
		AuthenticationFactory.setDivAuthentication(HtmlLoader.getHtml("authentication.html"));
	}

}
