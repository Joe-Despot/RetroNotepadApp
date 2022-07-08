package hr.fer.zemris.ooup.lab3.zad2.texteditors;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

import hr.fer.zemris.ooup.lab3.zad2.listeners.TimerListener;
import hr.fer.zemris.ooup.lab3.zad2.locations.Location;

public class EditorPanel extends JPanel {
	private Boolean flag = true;
	private static final int BLINKING_RATE = 800;
	private static final long serialVersionUID = 1L;
	private Cursor cursor = new Cursor();
	private Timer timer;
	private List<String> listOfLines;
	private List<Location> rangeLocations;
	private Boolean blueBackground = false;
	private TextEditorModel textEditorModel;
	Color c_red = new Color(1f, 0f, 0f, .3f);

	public EditorPanel(TextEditorModel textEditorModel) {
		this.textEditorModel = textEditorModel;
		timer = new Timer(BLINKING_RATE, new TimerListener(this));
		timer.start();
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.requestFocusInWindow();
		this.setBackground(Color.BLACK);
	}

	public void updateText() {
		repaint();
	}

	public void updateCursor(Location location) {
		moveCursor(location.getX(), location.getY());
	}

	private void moveCursor(int x, int y) {
		int CURR_X = cursor.getX();
		int CURR_Y = cursor.getY();
		int CURR_W = cursor.getWidth();
		int CURR_H = cursor.getHeight();
		int OFFSET = 1;

		cursor.notTransparentCursor(this.getGraphics(), CURR_X, CURR_Y, CURR_W + OFFSET, CURR_H + OFFSET);

		cursor.setX(x * 10);
		cursor.setY(y * 13);
		repaint(cursor.getX(), cursor.getY(), cursor.getWidth() + OFFSET, cursor.getHeight() + OFFSET);

	}

	public void lockCursor() {
		cursor.setHeight(0);
	}

	public void unLockCursor() {
		cursor.setHeight(15);
	}

	public void blinkingCursor() throws InterruptedException {
		if (cursor == null || this.getGraphics() == null) {
			return;
		}
		if (flag) {
			flag = false;
			cursor.notTransparentCursor(this.getGraphics(), cursor.getX(), cursor.getY(), cursor.getWidth() + 1,
					cursor.getHeight() + 1);

		} else {
			flag = true;
			cursor.paintCursor(this.getGraphics());
		}

	}

	public void paintComponent(Graphics g) {
		listOfLines = textEditorModel.getLines();
		System.out.println(listOfLines);
		int rows = 0;
		super.paintComponent(g);
		g.setFont(new Font("Courier new", 0, 17));
		for (String s : listOfLines) {
			rows++;
			g.setColor(Color.GREEN);
			g.drawString(s, 0, rows * 13);
		}
		if (blueBackground) {
			g.setColor(c_red);
			int start_y = rangeLocations.get(0).getY();
			if (start_y == rangeLocations.get(1).getY()) {
				for (int start_x = rangeLocations.get(0).getX(); start_x < rangeLocations.get(1).getX(); start_x++) {
					g.fillRect(start_x * 10, start_y * 13, 10, 13);
				}
				blueBackground = false;
			} else {
				try {
					for (int start_x = rangeLocations.get(0).getX(); start_x < listOfLines.get(start_y)
							.length(); start_x++) {
						g.fillRect(start_x * 10, start_y * 13, 10, 13);
					}
				} catch (IndexOutOfBoundsException e) {
					blueBackground = false;
				}
				start_y++;
				while (start_y <= rangeLocations.get(1).getY()) {
					if (start_y != rangeLocations.get(1).getY()) {
						for (int start_x = 0; start_x < listOfLines.get(start_y).length(); start_x++) {
							g.fillRect(start_x * 10, start_y * 13, 10, 13);
						}
					} else {
						for (int start_x = 0; start_x < rangeLocations.get(1).getX(); start_x++) {
							g.fillRect(start_x * 10, start_y * 13, 10, 13);
						}
					}
					start_y++;
				}
				blueBackground = false;
			}
			blueBackground = false;
		}
		cursor.paintCursor(g);
		blueBackground = false;
	}

	public void setShiftBackground(List<Location> rangeLocations) {
		blueBackground = true;
		this.rangeLocations = new ArrayList<Location>();
		if (rangeLocations != null) {
			this.rangeLocations.add(rangeLocations.get(0));
			this.rangeLocations.add(rangeLocations.get(rangeLocations.size() - 1));
		} else {
			repaint();
			this.rangeLocations.clear();
		}
	}

}
