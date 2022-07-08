package hr.fer.zemris.ooup.lab3.zad2.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hr.fer.zemris.ooup.lab3.zad2.texteditors.EditorPanel;

public class TimerListener implements ActionListener {
	private EditorPanel textEditorPanel;

	public TimerListener(EditorPanel textEditorPaenl) {
		this.textEditorPanel = textEditorPaenl;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			textEditorPanel.blinkingCursor();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
