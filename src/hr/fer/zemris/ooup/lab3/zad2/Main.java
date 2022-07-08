package hr.fer.zemris.ooup.lab3.zad2;

import java.awt.AWTException;
import java.lang.reflect.InvocationTargetException;
import hr.fer.zemris.ooup.lab3.zad2.texteditors.TextEditor;
import hr.fer.zemris.ooup.lab3.zad2.texteditors.TextEditorModel;

public class Main {

	public static void main(String[] args) throws AWTException, InterruptedException, IndexOutOfBoundsException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		TextEditorModel testEditorModel = new TextEditorModel("");
		@SuppressWarnings("unused")
		TextEditor textEditor = new TextEditor(testEditorModel);
	}

}
