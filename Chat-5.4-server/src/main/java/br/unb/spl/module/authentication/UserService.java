package br.unb.spl.module.authentication;

public interface UserService {
	public String registerUser(User user);

	public String authenticateUser(User user);
}
