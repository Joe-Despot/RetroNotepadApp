package hr.fer.zemris.ooup.lab3.zad2.file;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PopUpLabelOpen extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel label;
	private ArrayList<JButton> buttons;
	private JPanel listFilesPanel;

	public PopUpLabelOpen() {
	}

	public void setButtonListener(ActionListener listener) {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).addActionListener(listener);
		}
	}

	public void showPopUpOpen() {
		label = new JLabel("Choose file: ");
		label.setFont(new Font("Courier new", 0, 17));
		label.setBackground(Color.black);
		label.setForeground(Color.green);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setFont(new Font("Courier new", 0, 17));
			buttons.get(i).setBackground(Color.black);
			buttons.get(i).setForeground(Color.green);
			buttons.get(i).setBorder(BorderFactory.createLineBorder(Color.green, 1));
		}
		panel.setBackground(Color.black);
		panel.setForeground(Color.green);
		panel.add(label, BorderLayout.PAGE_START);
		panel.add(listFilesPanel);
		this.add(panel);
		this.setVisible(true);
		this.pack();
	}

	public void addComponents(ArrayList<JButton> buttons) {
		this.buttons = buttons;
		listFilesPanel = new JPanel();
		listFilesPanel.setLayout(new GridLayout(0, 1));
		for (int i = 0; i < buttons.size(); i++) {
			listFilesPanel.add(buttons.get(i));
		}
	}

}
