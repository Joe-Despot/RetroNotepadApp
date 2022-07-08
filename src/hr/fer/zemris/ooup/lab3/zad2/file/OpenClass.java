package hr.fer.zemris.ooup.lab3.zad2.file;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;

public class OpenClass {

	public OpenClass() {
	}

	public ArrayList<JButton> openFolder(String folderPath) {
		ArrayList<JButton> buttons = new ArrayList<JButton>();
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				buttons.add(new JButton(file.getName()));
			}
		}
		return buttons;
	}
}
