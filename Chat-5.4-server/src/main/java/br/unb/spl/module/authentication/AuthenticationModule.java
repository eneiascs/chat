package br.unb.spl.module.authentication;

public class AuthenticationModule implements IAuthenticationModule {
	private static AuthenticationModule auhtenticationModule = new AuthenticationModule();
	private UserService userService = new UserServiceImpl();

	public AuthenticationModule() {
		super();
	}

	public static AuthenticationModule getInstance() {
		return auhtenticationModule;
	}

	public String registerUser(User user) {

		return userService.registerUser(user);
	}

	public String authenticateUser(User user) {

		return userService.authenticateUser(user);
	}

	public String getHtmlAuthentication() {
		return HtmlLoader.getHtml("authentication.html");
	}
}
