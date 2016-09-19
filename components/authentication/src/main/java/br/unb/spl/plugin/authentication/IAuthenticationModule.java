package br.unb.spl.plugin.authentication;

public interface IAuthenticationModule {
	public String registerUser(User user);

	public String authenticateUser(User user);

	public String getHtmlAuthentication();
}
