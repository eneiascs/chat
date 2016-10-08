package br.unb.spl.server.connection;/**
 * TODO description
 */


import br.unb.spl.server.authentication.User;
import br.unb.spl.server.main.HtmlLoader;


public class AuthenticationLoader{
	public static String getDivAuthentication() {
		
		return HtmlLoader.getHtml("no-authentication.html");
	}
	
	public static String authenticateUser(User user) {
		
		return null;
	}
	public static String registerUser(User user) {
		
		return null;
	}
}

