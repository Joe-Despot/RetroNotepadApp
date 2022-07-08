package hr.fer.zemris.ooup.lab3.zad2.file;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import hr.fer.zemris.ooup.lab3.zad2.texteditors.TextEditorModel;

public class OpenFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private TextEditorModel textEditorModeL;
	private JTextField textField;

	public OpenFrame(TextEditorModel tem) {
		this.textEditorModeL = tem;
		initilizeOpenFileScreen();
	}

	public void initilizeOpenFileScreen() {

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
		label.setText("Enter folder path:");

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

		private JFrame frame;
		private OpenClass open;
		private String folderPath;
		private PopUpLabelOpen popUpOpen;

		public MyKeyListener(JFrame frame, JPanel panel, GridBagConstraints c) {
			this.frame = frame;
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
				open = new OpenClass();
				folderPath = textField.getText();
				ArrayList<JButton> buttons = open.openFolder(folderPath);
				popUpOpen = new PopUpLabelOpen();
				popUpOpen.addComponents(buttons);
				popUpOpen.showPopUpOpen();
				popUpOpen.setButtonListener(this);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton o = (JButton) e.getSource();
			String name = o.getText();
			Path path = Paths.get(folderPath + "\\" + name);

			try {
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
				textEditorModeL.setLines(lines);
				textEditorModeL.notifyText();
				frame.dispose();
				popUpOpen.dispose();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}

}
