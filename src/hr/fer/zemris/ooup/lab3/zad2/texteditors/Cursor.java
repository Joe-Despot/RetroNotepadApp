package hr.fer.zemris.ooup.lab3.zad2.texteditors;

import java.awt.Color;
import java.awt.Graphics;

public class Cursor {
	private int xPos = 0;
	private int yPos = 0;
	private int width = 0;
	private int height = 15;

	public void setHeight(int height) {
		this.height = height;
	}

	public void setX(int xPos) {
		this.xPos = xPos;
	}

	public int getX() {
		return xPos;
	}

	public void setY(int yPos) {
		this.yPos = yPos;
	}

	public int getY() {
		return yPos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void paintCursor(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, width, height);
		g.setColor(Color.GREEN);
		g.drawRect(xPos, yPos, width, height);
	}

	public void notTransparentCursor(Graphics g, int x, int y, int w, int h) {
		g.setColor(Color.BLACK);
		g.clearRect(x, y, w, h);
	}
}
