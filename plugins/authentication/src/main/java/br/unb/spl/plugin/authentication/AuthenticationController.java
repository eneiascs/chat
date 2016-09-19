package br.unb.spl.plugin.authentication;

import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class AuthenticationController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/authenticate", method = RequestMethod.POST, headers = {
			"Content-type=application/json" })
	@ResponseBody
	public String authenticateUser(@RequestBody User user) {

		return JSON.toString(userService.authenticateUser(user));
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST, headers = {
			"Content-type=application/json" })
	@ResponseBody
	public String registerUser(@RequestBody User user) {

		return JSON.toString(userService.registerUser(user));
	}

}