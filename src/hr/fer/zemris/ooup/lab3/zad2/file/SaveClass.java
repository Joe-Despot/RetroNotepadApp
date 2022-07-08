package hr.fer.zemris.ooup.lab3.zad2.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SaveClass {

	private File myTxtFile;
	private String fileName;

	public SaveClass(String fileName) throws IOException {
		this.fileName = fileName + ".txt";
	}

	public Boolean createSaveFile() {
		this.myTxtFile = new File(this.fileName);
		if (myTxtFile.exists()) {
			return false;
		}
		return true;
	}

	public void writeToFile(List<String> listOfLines) {
		try {
			FileWriter myWriter = new FileWriter(fileName);
			for (int i = 0; i < listOfLines.size(); i++) {
				myWriter.write(listOfLines.get(i) + "\n");
			}
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clearFile() {
		try {
			FileWriter myWriter = new FileWriter(fileName, false);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
