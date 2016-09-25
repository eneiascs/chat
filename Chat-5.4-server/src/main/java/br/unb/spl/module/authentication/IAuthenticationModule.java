package br.unb.spl.module.authentication;

public interface IAuthenticationModule {
	public String registerUser(User user);

	public String authenticateUser(User user);

	public String getHtmlAuthentication();
}
