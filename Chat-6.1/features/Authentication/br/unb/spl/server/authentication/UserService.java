package br.unb.spl.server.authentication;

public interface UserService {
	public String registerUser(User user);

	public String authenticateUser(User user);
}
