package br.unb.spl.server.color;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/html")
@Controller
@SuppressWarnings("UnusedDeclaration")
public class ColorController {

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/color", method = RequestMethod.GET, produces = "text/html")

	public @ResponseBody String getDivColor() {

		return ColorFactory.getDiv();
	}

}