package br.unb.spl.module.authentication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
	private Map<String, User> users = new HashMap<String, User>();

	public String registerUser(User user) {
		if (users.containsKey(user.getUsername())) {
			return "This username is already registered";

		} else {
			users.put(user.getUsername(), user);
			return "OK";
		}
	}

	public String authenticateUser(User user) {
		User us = users.get(user.getUsername());
		if (us != null && us.getPassword().equals(user.getPassword())) {
			return "OK";
		} else {
			return "Username or password incorrect";
		}
	}

}
