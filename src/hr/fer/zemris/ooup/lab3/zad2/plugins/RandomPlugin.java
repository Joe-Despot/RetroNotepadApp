package hr.fer.zemris.ooup.lab3.zad2.plugins;

import hr.fer.zemris.ooup.lab3.zad2.clipboard.ClipboardStack;
import hr.fer.zemris.ooup.lab3.zad2.editaction.UndoManager;
import hr.fer.zemris.ooup.lab3.zad2.interfacePlugin.Plugin;
import hr.fer.zemris.ooup.lab3.zad2.texteditors.TextEditorModel;
import java.nio.charset.Charset;
import java.util.Random;

public class RandomPlugin implements Plugin {

	@Override
	public String getName() {
		return "RandomPlugin";
	}

	@Override
	public String getDescription() {
		return "test1 test2 test3 random random";
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		for (int i = 0; i < model.getLines().size(); i++) {
			byte[] array = new byte[7];
			new Random().nextBytes(array);
			String generatedString = new String(array, Charset.forName("UTF-8"));
			model.getLines().set(i, generatedString);

		}
		model.notifyText();
	}

}
