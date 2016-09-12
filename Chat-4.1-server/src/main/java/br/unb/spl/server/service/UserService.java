package br.unb.spl.server.service;

import br.unb.spl.server.entity.User;

public interface UserService {
	public String registerUser(User user);

	public String authenticateUser(User user);
}
