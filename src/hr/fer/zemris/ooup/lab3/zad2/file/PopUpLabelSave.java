package hr.fer.zemris.ooup.lab3.zad2.file;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PopUpLabelSave extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel label;
	private JButton buttonRewrite;
	private JButton buttonDiscard;

	public PopUpLabelSave() {
		this.panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		this.label = new JLabel(
				"<html>Filename already exists. <br> Would you like to rewrite it or cancel saving file?<html>");
		panel.setBackground(Color.black);
		panel.setForeground(Color.green);
		label.setFont(new Font("Courier new", 0, 17));
		label.setForeground(Color.green);
		panel.add(label, c);
		buttonRewrite = new JButton("Rewrite");
		buttonDiscard = new JButton("Cancel");
		buttonRewrite.setBorder(BorderFactory.createLineBorder(Color.green, 1));
		buttonDiscard.setBorder(BorderFactory.createLineBorder(Color.green, 1));
		buttonRewrite.setBackground(Color.black);
		buttonRewrite.setForeground(Color.green);
		buttonDiscard.setBackground(Color.black);
		buttonDiscard.setForeground(Color.green);
		c.insets = new Insets(0, 250, 0, 0);
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		this.panel.add(buttonRewrite, c);
		c.insets = new Insets(0, 0, 0, 250);
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		this.panel.add(buttonDiscard, c);
		this.add(panel);
		this.setVisible(true);
		this.pack();
	}

	public void setButtonListener(ActionListener listener) {
		buttonRewrite.addActionListener(listener);
		buttonDiscard.addActionListener(listener);
	}
}
