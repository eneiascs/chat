package br.unb.spl.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

	private int count = 0;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		
		count++;

	
			resp.getWriter().println(String.format("<h1>Chat %s</h1>", count));

		// response.sendRedirect("index.jsp" );

		// LlamaChat llamaChat=new LlamaChat();
		// llamaChat.username="eneiascs1";
		// llamaChat.PORT=42412;
		//
		// URL url = new URL("http://localhost:8080/");
		// llamaChat.site = url.getHost();
		// llamaChat.locationURL = "http://" + llamaChat + ":" + url.getPort() +
		// "/" + url.getPath();
		//
		// llamaChat.connect();
		// //response.getWriter().println("<OBJECT
		// classid='clsid:8AD9C840-044E-11D1-B3E9-00805F499D93' width='615'
		// height='360'
		// codebase='http://java.sun.com/products/plugin/autodl/jinstall-1_4-windows-i586.cab#Version=1,8,0,mn'>
		// <PARAM name='code' value='client/LlamaChat.class'> <PARAM
		// name='archive' value='LlamaChat.jar'> <PARAM name='type'
		// value='application/x-java-applet;version=1.8'> <PARAM
		// name='scriptable' value='true'> <PARAM name='username'
		// value='[replace with username]'> <PARAM name='port' value='[replace
		// with port]'> <COMMENT> <EMBED
		// type='application/x-java-applet;version=1.8' width='615' height='360'
		// code='client/LlamaChat.class' archive='LlamaChat.jar'
		// pluginspage='http://java.sun.com/j2se/1.8.1/download.html'
		// username='[replace with username]' port='[replace with port]'
		// <NOEMBED> No Java 1.8 plugin </NOEMBED></EMBED> </COMMENT>
		// </OBJECT>");
	

	}
		
}
