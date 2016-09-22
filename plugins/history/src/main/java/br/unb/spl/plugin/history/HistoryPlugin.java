package br.unb.spl.plugin.history;

import br.unb.spl.server.config.HtmlLoader;
import br.unb.spl.server.history.HistoryFactory;
import br.unb.spl.server.message.HandleMessageSubjectFactory;
import br.unb.spl.server.message.MessageHandleDecoratorFactory;
import br.unb.spl.server.plugin.IPlugin;

public class HistoryPlugin implements IPlugin {

	public void register() {
		HistoryFactory.setDiv(HtmlLoader.getHtml("history.html"));
		HandleMessageSubjectFactory.register(new HistoryHandleMessageObserver());
		MessageHandleDecoratorFactory.addDecorator(HistoryMessage.class);

	}

}
