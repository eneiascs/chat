package br.unb.spl.server.authentication;

public interface IAuthenticationModule {
	public String registerUser(User user);

	public String authenticateUser(User user);

	public String getHtmlAuthentication();
}
