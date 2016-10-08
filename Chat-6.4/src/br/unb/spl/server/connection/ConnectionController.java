package br.unb.spl.server.connection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.unb.spl.server.main.HtmlLoader;

@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class ConnectionController {
	@RequestMapping(value = "/html/authentication", method = RequestMethod.GET, produces = "text/html")
	public @ResponseBody String getDivAuthentication() {

		return  AuthenticationLoader.getDivAuthentication();
	}
}