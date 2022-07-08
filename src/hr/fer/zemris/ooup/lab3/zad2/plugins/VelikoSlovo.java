package hr.fer.zemris.ooup.lab3.zad2.plugins;

import hr.fer.zemris.ooup.lab3.zad2.clipboard.ClipboardStack;
import hr.fer.zemris.ooup.lab3.zad2.editaction.UndoManager;
import hr.fer.zemris.ooup.lab3.zad2.interfacePlugin.Plugin;
import hr.fer.zemris.ooup.lab3.zad2.texteditors.TextEditorModel;

public class VelikoSlovo implements Plugin {

	@Override
	public String getName() {
		return "VelikoSlovo";
	}

	@Override
	public String getDescription() {
		return "prolazi kroz dokument i svako prvo slovo rijeci mijenja u veliko \"ovo je tekst\" ==> \"Ovo Je Tekst\")" ;
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		String temp;
		for (int i = 0; i < model.getLines().size(); i++) {
			temp = capitilize(model.getLines().get(i));
			model.getLines().set(i, temp);
		}
		model.notifyText();
	}

	public String capitilize(String s) {
		char[] charArray = s.toCharArray();
		boolean foundSpace = true;

		for (int i = 0; i < charArray.length; i++) {
			if (Character.isLetter(charArray[i])) {
				if (foundSpace) {
					charArray[i] = Character.toUpperCase(charArray[i]);
					foundSpace = false;
				}
			} else {
				foundSpace = true;
			}
		}
		return String.valueOf(charArray);
	}

}