package br.unb.spl.server.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/config")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class ConfigController {

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Properties getConfig() throws IOException {

		return ConfigLoader.getConfig();
	}

}