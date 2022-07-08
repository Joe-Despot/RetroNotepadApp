package hr.fer.zemris.ooup.lab3.zad2.menus;

import java.awt.Font;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import hr.fer.zemris.ooup.lab3.zad2.locations.Location;
import hr.fer.zemris.ooup.lab3.zad2.observers.CursorObserver;

public class TextEditorStatus implements CursorObserver {

	private JLabel label;

	public TextEditorStatus() {
		Border blackline = BorderFactory.createLineBorder(Color.green);
		this.label = new JLabel("LINE: 0, COL: 0");
		label.setBackground(Color.black);
		label.setForeground(Color.green);
		label.setBorder(blackline);
		label.setOpaque(true);
		label.setBackground(Color.black);
	}

	public JLabel getStatus() {
		return this.label;
	}

	@Override
	public void updateCursorLocation(Location loc) {
		label.setFont(new Font("Courier new", 0, 17));
		label.setText("LINE: " + loc.getY() + ", COL: " + loc.getX());
	}
}
