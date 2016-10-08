package br.unb.spl.server.authentication;


import org.mortbay.util.ajax.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unb.spl.server.connection.AuthenticationLoader;

@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class AuthenticationController {
	
	@RequestMapping(value = "/user/authenticate", method = RequestMethod.POST, headers = {
			"Content-type=application/json" })
	@ResponseBody
	public String authenticateUser(@RequestBody User user) {

		return JSON.toString(AuthenticationLoader.authenticateUser(user));
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST, headers = {
			"Content-type=application/json" })
	@ResponseBody
	public String registerUser(@RequestBody User user) {

		return JSON.toString(AuthenticationLoader.registerUser(user));
	}

}