package br.unb.spl.server.controller;

import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unb.spl.server.entity.User;
import br.unb.spl.server.service.UserService;

@RequestMapping("/users")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class UserController {
	@Autowired
	private UserService userService;

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public String authenticateUser(@RequestBody User user) {
		System.out.printf("User %s %s%n", user.getUsername(), user.getPassword());
		return JSON.toString(userService.authenticateUser(user));
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public String registerUser(@RequestBody User user) {

		return JSON.toString(userService.registerUser(user));
	}
}