package br.unb.spl.plugin.authentication;

public interface UserService {
	public String registerUser(User user);

	public String authenticateUser(User user);
}
