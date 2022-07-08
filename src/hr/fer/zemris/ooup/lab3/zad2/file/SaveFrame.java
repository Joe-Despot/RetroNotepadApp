package hr.fer.zemris.ooup.lab3.zad2.file;

import java.awt.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SaveFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private String fileName;
	private Boolean flag = false;
	private List<String> listOfLines;
	private SaveClass save;

	public SaveFrame(List<String> listOfLines) {
		this.listOfLines = listOfLines;
		initilizeSaveInfoScreen();
	}

	public String getFileName() {
		return this.fileName;
	}

	private void initilizeSaveInfoScreen() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel();
		textField = new JTextField(20);

		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(label, c);
		panel.setOpaque(true);
		panel.setBackground(Color.black);
		panel.setForeground(Color.green);

		label.setFont(new Font("Courier new", 0, 17));
		label.setBackground(Color.black);
		label.setForeground(Color.green);
		label.setText("Enter file name:");

		textField.setFont(new Font("Courier", 0, 17));
		textField.setBackground(Color.black);
		textField.setForeground(Color.green);
		textField.addKeyListener(new MyKeyListener(this, panel, c));
		textField.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		textField.setFocusable(true);
		textField.setRequestFocusEnabled(true);

		panel.add(textField);
		panel.setFocusable(true);
		this.add(panel);

		this.setVisible(true);
		this.pack();
		textField.grabFocus();

	}

	class MyKeyListener implements KeyListener, ActionListener {

		private PopUpLabelSave popUp;

		public MyKeyListener(JFrame frame, JPanel panel, GridBagConstraints c) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String input = textField.getText();
				if (input == "") {
					return;
				} else {
					save = null;
					try {
						save = new SaveClass(input);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if (save.createSaveFile()) {
						save.writeToFile(listOfLines);
					} else {
						popUp = new PopUpLabelSave();
						popUp.setButtonListener(this);
					}
				}
			}

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton o = (JButton) e.getSource();
			String name = o.getText();
			if (name == "Rewrite") {
				save.clearFile();
				save.writeToFile(listOfLines);
				popUp.dispose();
				dispose();
			} else {
				dispose();
				popUp.dispose();
			}
		}

	}

	public boolean getFlag() {
		return flag;
	}

}
