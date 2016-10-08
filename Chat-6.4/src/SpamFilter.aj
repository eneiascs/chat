import java.util.ArrayList;
import java.util.List;

import br.unb.spl.server.message.Message;

public aspect SpamFilter {
	void around(): execution(void onCreate()){

		Message message =(Message) thisJoinPoint.getThis();
		List<String> spamList = new ArrayList<String>();
		spamList.add("spam");
		spamList.add("list");
		spamList.add("forbidden");
		for (String string : spamList) {
			if (message.getText().toLowerCase().contains(string.toLowerCase())) {
				message.setText("**Word blocked**");
			}
		}
		
		proceed();
		
		return;
		
		
		}

}