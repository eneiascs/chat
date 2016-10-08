import br.unb.spl.server.color.ColorLoader;
import br.unb.spl.server.main.HtmlLoader;
import br.unb.spl.server.message.Message;

public aspect Color {


public String Message.textColor;
private String Message.getTextColor(){
	return textColor;
};
public String Message.backgroundColor;
public String Message.getBackgroundColor(){
	return backgroundColor;
};
	
String around(): execution(String ColorLoader.getDivColor()){
		
		return HtmlLoader.getHtml("color.html");
		
	}



}