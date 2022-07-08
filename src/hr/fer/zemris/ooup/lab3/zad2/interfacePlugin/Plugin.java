package hr.fer.zemris.ooup.lab3.zad2.interfacePlugin;

import hr.fer.zemris.ooup.lab3.zad2.clipboard.ClipboardStack;
import hr.fer.zemris.ooup.lab3.zad2.editaction.UndoManager;
import hr.fer.zemris.ooup.lab3.zad2.texteditors.TextEditorModel;

public interface Plugin {
	String getName(); // ime plugina (za izbornicku stavku)

	String getDescription(); // kratki opis

	void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack);
}
