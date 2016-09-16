package br.unb.spl.server.user;

public interface UserService {
	public String registerUser(User user);

	public String authenticateUser(User user);
}
