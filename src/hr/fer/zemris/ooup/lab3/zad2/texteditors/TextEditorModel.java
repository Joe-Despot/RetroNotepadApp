package hr.fer.zemris.ooup.lab3.zad2.texteditors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import hr.fer.zemris.ooup.lab3.zad2.editaction.DeleteAfterManager;
import hr.fer.zemris.ooup.lab3.zad2.editaction.DeleteBeforeManager;
import hr.fer.zemris.ooup.lab3.zad2.editaction.DeleteRangeManager;
import hr.fer.zemris.ooup.lab3.zad2.editaction.EditAction;
import hr.fer.zemris.ooup.lab3.zad2.editaction.InsertCharManager;
import hr.fer.zemris.ooup.lab3.zad2.editaction.InsertStringManager;
import hr.fer.zemris.ooup.lab3.zad2.editaction.UndoManager;
import hr.fer.zemris.ooup.lab3.zad2.iterators.IterateThroughLines;
import hr.fer.zemris.ooup.lab3.zad2.locations.Location;
import hr.fer.zemris.ooup.lab3.zad2.locations.LocationRange;
import hr.fer.zemris.ooup.lab3.zad2.observers.ClipboardObserver;
import hr.fer.zemris.ooup.lab3.zad2.observers.CursorObserver;
import hr.fer.zemris.ooup.lab3.zad2.observers.TextObserver;

public class TextEditorModel {
	private List<String> lines;
	private LocationRange selectionRange;
	private Location cursorLocation;
	private Iterator<String> iteratorLines;
	private List<CursorObserver> cursorSubscribers;
	private List<TextObserver> textSubscribers;
	private List<ClipboardObserver> clipboardStackSubsribers;
	int x, y;
	private char c;
	private String typedKey;
	private String text = "";
	private List<Location> rangeLocations = null;
	private Location start = null;
	private EditorPanel editorPanel;
	private Boolean isShiftPressed = false;
	private EditAction editAction;
	private UndoManager undoManager;

	public TextEditorModel(String text) {
		this.undoManager = UndoManager.getInstance();
		this.selectionRange = new LocationRange();
		this.lines = new ArrayList<String>(Arrays.asList(text.split("\n")));
		cursorSubscribers = new ArrayList<CursorObserver>();
		textSubscribers = new ArrayList<TextObserver>();
		this.clipboardStackSubsribers = new ArrayList<ClipboardObserver>();
		cursorLocation = new Location(0, 0);
		this.notifyCursors();
		this.notifyText();
	}

	// FLAGS
	public void setShiftFlag(Boolean b) {
		isShiftPressed = b;
	}

	public Boolean getShiftFlag() {
		return isShiftPressed;
	}

	// DELETE OPERATIONS
	public void deleteBefore() {
		editAction = new DeleteBeforeManager(this);
		undoManager.push(editAction);
		if (getSelectionRange() != null) {
			deleteRange(getSelectionRange());
			this.notifyCursors();
			this.notifyText();
			return;
		}
		if (cursorLocation.getX() == 0) {
			if (cursorLocation.getY() == 0)
				return;
			else if (lines.get(cursorLocation.getY()) == null) {
				cursorLocation.setY(cursorLocation.getY() - 1);
			} else if (lines.get(cursorLocation.getY()).equals("")) {
				lines.remove(cursorLocation.getY());
				cursorLocation.setX(lines.get(cursorLocation.getY() - 1).length());
				cursorLocation.setY(cursorLocation.getY() - 1);
			} else {
				cursorLocation.setX(lines.get(cursorLocation.getY() - 1).length());
				lines.set(cursorLocation.getY() - 1,
						lines.get(cursorLocation.getY() - 1) + lines.get(cursorLocation.getY()));
				lines.remove(cursorLocation.getY());
				cursorLocation.setY(cursorLocation.getY() - 1);
			}
			setSelectionRange(new LocationRange());
			this.notifyCursors();
			this.notifyText();
			return;
		}
		String secondText;
		text = lines.get(cursorLocation.getY());
		secondText = text.substring(cursorLocation.getX(), text.length());
		text = text.substring(0, cursorLocation.getX() - 1);

		if (secondText == null)
			secondText = "";
		text = text + secondText;

		cursorLocation.setX(cursorLocation.getX() - 1);
		lines.remove(cursorLocation.getY());
		lines.add(cursorLocation.getY(), text);
		this.notifyCursors();
		this.notifyText();

	}

	public void deleteAfter() {
		editAction = new DeleteAfterManager(this);
		undoManager.push(editAction);
		if (getSelectionRange() != null) {
			deleteRange(getSelectionRange());
			this.notifyCursors();
			this.notifyText();
			return;
		}
		if (lines.get(cursorLocation.getY()) == "") {
			this.notifyCursors();
			this.notifyText();
			return;
		}

		String secondText;
		text = lines.get(cursorLocation.getY());
		try {
			secondText = text.substring(cursorLocation.getX() + 1, text.length());
		} catch (StringIndexOutOfBoundsException e) {
			return;
		}
		text = text.substring(0, cursorLocation.getX());

		if (secondText == null)
			return;
		text = text + secondText;

		cursorLocation.setX(cursorLocation.getX());
		lines.remove(cursorLocation.getY());
		lines.add(cursorLocation.getY(), text);
		this.notifyCursors();
		this.notifyText();

	}

	public void deleteRange(LocationRange r) {
		if (r == null)
			return;
		editAction = new DeleteRangeManager(this);
		undoManager.push(editAction);
		editAction = new DeleteRangeManager(this);
		undoManager.push(editAction);
		String temp;
		Location start = r.getLocation_start();
		Location end = r.getLocation_end();
		String remainder_1st;
		String remainder_2nd;
		temp = lines.get(start.getY());
		try {
			remainder_1st = temp.substring(0, start.getX());
		} catch (IndexOutOfBoundsException e) {
			remainder_1st = "";
		}

		temp = lines.get(end.getY());
		try {
			remainder_2nd = temp.substring(end.getX(), lines.get(end.getY()).length());
		} catch (IndexOutOfBoundsException e) {
			remainder_2nd = "";
		}
		for (y = start.getY() + 1; y < end.getY(); y++) {
			try {
				lines.remove(y);
			} catch (IndexOutOfBoundsException e) {

			}
		}

		if (end.getY() - start.getY() == 1)
			lines.remove(end.getY());
		else if (end.getY() - start.getY() > 1) {
			lines.remove(start.getY() + 1);
		}

		lines.set(start.getY(), remainder_1st + remainder_2nd);

		cursorLocation.setLocation(start.getX(), start.getY());
		this.notifyCursors();
		this.notifyText();
	}

	// SELECTION OPERATIONS
	public LocationRange getSelectionRange() {
		return this.selectionRange;
	}

	public void setSelectionOrientation(String s, EditorPanel editorPanel) {
		isShiftPressed = true;
		this.editorPanel = editorPanel;
		Location start;
		Location end;
		if (s == "r") {
			start = new Location(cursorLocation.getX(), cursorLocation.getY());
			if (cursorLocation.getX() != lines.get(cursorLocation.getY()).length()) {
				end = new Location(cursorLocation.getX() + 1, cursorLocation.getY());
			} else
				end = new Location(cursorLocation.getX(), cursorLocation.getY());
		} else if (s == "l") {
			start = new Location(cursorLocation.getX(), cursorLocation.getY());
			if (cursorLocation.getX() != 0) {
				end = new Location(cursorLocation.getX() - 1, cursorLocation.getY());
			} else
				end = new Location(cursorLocation.getX(), cursorLocation.getY());
		} else if (s == "u") {
			start = new Location(cursorLocation.getX(), cursorLocation.getY());
			if (cursorLocation.getY() != 0) {
				end = new Location(cursorLocation.getX(), cursorLocation.getY() - 1);
			} else
				end = new Location(cursorLocation.getX(), cursorLocation.getY());
		} else {
			if (cursorLocation.getY() == lines.size() - 1)
				end = new Location(cursorLocation.getX(), cursorLocation.getY());
			else
				end = new Location(cursorLocation.getX(), cursorLocation.getY() + 1);
			start = new Location(cursorLocation.getX(), cursorLocation.getY());
		}
		setSelectionRange(new LocationRange(start, end));
	}

	public void setSelectionRange(LocationRange range) {
		if (range == null) {
			this.selectionRange = null;
			return;
		}
		if (range.getLocation_end().getX() == 0 && range.getLocation_end().getY() == 0
				&& range.getLocation_start().getX() == 0 && range.getLocation_start().getY() == 0) {
			if (rangeLocations != null && !rangeLocations.isEmpty()) {
				editorPanel.setShiftBackground(rangeLocations);
				this.notifyCursors();
				this.notifyText();
				return;
			}
			this.selectionRange = null;
			return;
		}
		if (rangeLocations == null) {
			rangeLocations = new ArrayList<Location>();
			start = range.getLocation_start();
		}
		Location end = range.getLocation_end();
		rangeLocations.add(start);
		rangeLocations.add(end);
		if (rangeLocations != null)
			Collections.sort(rangeLocations);
		System.out.println(rangeLocations);
		if (range != null)
			editorPanel.setShiftBackground(rangeLocations);
		this.selectionRange = new LocationRange(rangeLocations.get(0), rangeLocations.get(rangeLocations.size() - 1));
		this.notifyCursors();
		this.notifyText();
	}

	public List<String> locationRangeToString(LocationRange lr) {
		if (lr == null)
			return null;
		System.out.println(lr);
		String temp;
		Location start = lr.getLocation_start();
		Location end = lr.getLocation_end();
		String remainder_1st;
		String remainder_2nd;
		List<String> result = new ArrayList<String>();
		temp = lines.get(start.getY());
		if (start.getY() == end.getY()) {
			try {
				remainder_1st = temp.substring(start.getX(), end.getX());
			} catch (IndexOutOfBoundsException e) {
				remainder_1st = "";
			}
		} else {
			try {
				remainder_1st = temp.substring(start.getX(), lines.get(start.getY()).length());
			} catch (IndexOutOfBoundsException e) {
				remainder_1st = "";
			}
		}
		result.add(remainder_1st);
		if (end.getY() - start.getY() != 0) {
			for (y = start.getY() + 1; y < end.getY(); y++) {
				try {
					result.add(lines.get(y));
				} catch (IndexOutOfBoundsException e) {
				}
			}
		}
		temp = lines.get(end.getY());
		if (end.getY() != start.getY()) {
			try {
				remainder_2nd = temp.substring(0, end.getX());
			} catch (IndexOutOfBoundsException e) {
				remainder_2nd = "";
			}
		} else {
			remainder_2nd = "";
		}
		if (remainder_2nd != "")
			result.add(remainder_2nd);
		this.notifyCursors();
		this.notifyText();
		return result;
	}

	// CURSOR MOVMENT
	public void moveCursorLeft() {
		x = cursorLocation.getX();
		y = cursorLocation.getY();

		if (x > 0)
			cursorLocation.setLocation(x - 1, y);
		else if (x == 0 && y > 0)
			cursorLocation.setLocation(lines.get(y - 1).length(), y - 1);
		else
			cursorLocation.setLocation(0, 0);

		if (!isShiftPressed) {
			if (rangeLocations != null)
				rangeLocations = null;
		}
		notifyCursors();
		notifyText();
	}

	public void moveCursorRight() {
		x = cursorLocation.getX();
		y = cursorLocation.getY();

		try {
			if (cursorLocation.getX() < lines.get(cursorLocation.getY()).length()) {
				cursorLocation.setLocation(x + 1, y);
			} else {
				cursorLocation.setLocation(lines.get(y + 1).length(), y + 1);
			}
		} catch (IndexOutOfBoundsException e) {
		}
		if (!isShiftPressed) {
			if (rangeLocations != null)
				rangeLocations = null;
		}
		notifyCursors();
		notifyText();
	}

	public void moveCursorUp() {
		x = cursorLocation.getX();
		y = cursorLocation.getY();

		try {
			if ((x <= lines.get(y - 1).length()) && (lines.get(y - 1).length() != 0)) {
				cursorLocation.setLocation(x, y - 1);
			} else if (y == 0) {
				return;
			} else {
				cursorLocation.setLocation(lines.get(y - 1).length(), y - 1);
			}
		} catch (IndexOutOfBoundsException e) {

		}

		if (!isShiftPressed) {
			if (rangeLocations != null)
				rangeLocations = null;
		}
		notifyCursors();
		notifyText();
	}

	public void moveCursorDown() {
		x = cursorLocation.getX();
		y = cursorLocation.getY();
		try {
			if ((x <= lines.get(y + 1).length()) && (lines.get(y + 1).length() != 0)) {
				cursorLocation.setLocation(x, y + 1);
			} else {
				cursorLocation.setLocation(lines.get(y + 1).length(), y + 1);
			}
		} catch (IndexOutOfBoundsException e) {
			cursorLocation.setLocation(x, y);
		}
		if (!isShiftPressed) {
			if (rangeLocations != null)
				rangeLocations = null;
		}
		notifyCursors();
		notifyText();
	}

	// CLIPBOARD LOGIC
	public void subscribeToClipboardObserver(ClipboardObserver clipboardStack) {
		clipboardStackSubsribers.add(clipboardStack);
	}

	public void unSubscribeFromClipboardObserver(ClipboardObserver clipboardStack) {
		clipboardStackSubsribers.remove(clipboardStack);
	}

	public void notifyClipbpoard() {
		for (ClipboardObserver co : clipboardStackSubsribers)
			co.updateClipboard();
	}

	// CURSOR OBSERVER LOGIC
	public void subscribeToCursorObserver(CursorObserver cursorObserver) {
		cursorSubscribers.add(cursorObserver);
	}

	public void unSubscribeFromCursoerObserver(CursorObserver cursorObserver) {
		cursorSubscribers.remove(cursorObserver);
	}

	public void notifyCursors() {
		for (CursorObserver co : cursorSubscribers)
			co.updateCursorLocation(cursorLocation);
	}

	// TEXT OBSERVER LOGIC
	public void subscribeToTextObserver(TextObserver textObserver) {
		textSubscribers.add(textObserver);
	}

	public void unSubscribeFromTextObserver(TextObserver textObserver) {
		textSubscribers.remove(textObserver);
	}

	public void notifyText() {
		for (TextObserver to : textSubscribers)
			to.updateText();
	}

	// LINE OPERATIONS
	public List<String> getLines() {
		return this.lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public Iterator<String> linesRange(int index1, int index2) {
		iteratorLines = new IterateThroughLines(lines);
		return iteratorLines;
	}

	public Iterator<String> allLines() {
		iteratorLines = new IterateThroughLines(lines);
		return iteratorLines;
	}

	// GET & SET CURSOR
	public Location getCursorLoc() {
		return this.cursorLocation;
	}

	public void setCursorLocation(Location loc) {
		this.cursorLocation = loc;
	}

	// INSERT CHAR AND STRING
	public void insert(char c) {
		this.c = c;
		editAction = new InsertCharManager(this);
		undoManager.push(editAction);
		typedKey = Character.toString(c);
		try {
			text = lines.get(cursorLocation.getY());
		} catch (IndexOutOfBoundsException e) {
			lines.add(cursorLocation.getY(), "");
		}
		text = text.substring(0, cursorLocation.getX()) + typedKey
				+ text.substring(cursorLocation.getX(), text.length());
		lines.set(cursorLocation.getY(), text);
		this.notifyCursors();
		this.notifyText();
	}

	public void insert(List<String> textInsert) {
		editAction = new InsertStringManager(this);
		undoManager.push(editAction);
		int y = cursorLocation.getY();
		int x = cursorLocation.getX();
		try {
			text = lines.get(y);
		} catch (IndexOutOfBoundsException e) {
			lines.add(y, "");
		}
		String firstString = text.substring(0, x);
		String secondString = text.substring(x, lines.get(y).length());
		for (String s : textInsert) {
			if (textInsert.size() == 1) {
				lines.set(y, firstString + s + secondString);
				break;
			}
			if (s == textInsert.get(0)) {
				lines.set(y, firstString + s);
				y++;
				continue;
			} else if (s == textInsert.get(textInsert.size() - 1)) {
				lines.add(y, s + secondString);
				y++;
				continue;
			} else {
				lines.add(y, s);
				y++;
			}
		}
		this.notifyCursors();
		this.notifyText();
	}

	public void insertNewLine() {
		String secondText;
		text = lines.get(cursorLocation.getY());
		secondText = text.substring(cursorLocation.getX(), text.length());
		text = text.substring(0, cursorLocation.getX());
		cursorLocation.setY(cursorLocation.getY() + 1);
		cursorLocation.setX(0);
		if (secondText == null) {
			secondText = "";
		}
		if (cursorLocation.getY() - 1 < 0) {
			lines.set(0, text);
		} else {
			lines.set(cursorLocation.getY() - 1, text);
		}
		lines.add(cursorLocation.getY(), secondText);
		this.notifyCursors();
		this.notifyText();
	}

	public void undoCommand() {
		undoManager.undo();
	}

	public void redoCommand() {
		undoManager.redo();

	}

	public UndoManager getUndoManager() {
		return this.undoManager;
	}

	public char getChar() {
		return c;
	}

}
