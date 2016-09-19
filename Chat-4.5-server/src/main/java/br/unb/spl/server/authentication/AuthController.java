package br.unb.spl.server.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class AuthController {
	@RequestMapping(value = "/html/authentication", method = RequestMethod.GET, produces = "text/html")
	public @ResponseBody String getDivAuthentication() {

		return AuthenticationFactory.getDiv();
	}
}