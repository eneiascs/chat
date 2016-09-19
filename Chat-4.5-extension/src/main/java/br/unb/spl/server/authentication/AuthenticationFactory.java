package br.unb.spl.server.authentication;

import br.unb.spl.server.config.HtmlLoader;

public class AuthenticationFactory {
	private static String div = HtmlLoader.getHtml("no-authentication.html");

	public static String getDiv() {
		return div;
	}

	public static void setDivAuthentication(String divAuthentication) {
		AuthenticationFactory.div = divAuthentication;
	}

}
