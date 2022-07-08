package hr.fer.zemris.ooup.lab3.zad2.plugins;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;

import hr.fer.zemris.ooup.lab3.zad2.clipboard.ClipboardStack;
import hr.fer.zemris.ooup.lab3.zad2.editaction.UndoManager;
import hr.fer.zemris.ooup.lab3.zad2.interfacePlugin.Plugin;
import hr.fer.zemris.ooup.lab3.zad2.texteditors.TextEditorModel;

public class Statistika implements Plugin {

	@Override
	public String getName() {
		return "Statistika";
	}

	@Override
	public String getDescription() {
		return "Plugin koji broji koliko ima redaka, rijeci i slova u dokumentu i to prikazuje korisniku u dijalogu";
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel();
		label.setFont(new Font("Courier", 0, 17));
		frame.add(label);

		label.setOpaque(true);
		label.setBackground(Color.black);
		label.setForeground(Color.green);

		String s = "Broj redaka: " + model.getLines().size();
		int wordCount = 0;
		for (int i = 0; i < model.getLines().size(); i++) {
			wordCount += countWordsUsingSplit(model.getLines().get(i));
		}
		s += ", Broj rijeci: " + wordCount;
		int letterCount = 0;
		for (int i = 0; i < model.getLines().size(); i++) {
			letterCount += model.getLines().get(i).replace(" ", "").length();
		}
		s += ", Broj slova: " + letterCount;
		label.setText(s);
		frame.setVisible(true);
		frame.pack();
	}

	public static int countWordsUsingSplit(String input) {
		if (input == null || input.isEmpty()) {
			return 0;
		}
		String[] words = input.split("\\s+");
		return words.length;
	}

}
