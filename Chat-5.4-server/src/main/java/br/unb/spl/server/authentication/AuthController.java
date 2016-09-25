package br.unb.spl.server.authentication;

import org.mortbay.util.ajax.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unb.spl.module.authentication.AuthenticationModule;
import br.unb.spl.module.authentication.IAuthenticationModule;
import br.unb.spl.module.authentication.User;

@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class AuthController {
	@RequestMapping(value = "/user/authenticate", method = RequestMethod.POST, headers = {
			"Content-type=application/json" })
	@ResponseBody
	public String authenticateUser(@RequestBody User user) {
		IAuthenticationModule authenticationModule = AuthenticationModule.getInstance();
		return JSON.toString(authenticationModule.authenticateUser(user));
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST, headers = {
			"Content-type=application/json" })
	@ResponseBody
	public String registerUser(@RequestBody User user) {
		IAuthenticationModule authenticationModule = AuthenticationModule.getInstance();

		return JSON.toString(authenticationModule.registerUser(user));
	}

	@RequestMapping(value = "/html/authentication", method = RequestMethod.GET, produces = "text/html")
	public @ResponseBody String getDivAuthentication() {
		IAuthenticationModule authenticationModule = AuthenticationModule.getInstance();

		return authenticationModule.getHtmlAuthentication();
	}
}