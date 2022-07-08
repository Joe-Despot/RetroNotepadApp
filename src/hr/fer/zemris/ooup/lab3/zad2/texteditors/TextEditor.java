package hr.fer.zemris.ooup.lab3.zad2.texteditors;

import hr.fer.zemris.ooup.lab3.zad2.clipboard.ClipboardStack;
import hr.fer.zemris.ooup.lab3.zad2.editaction.UndoManager;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.MenuKeyEvent;
import hr.fer.zemris.ooup.lab3.zad2.locations.Location;
import hr.fer.zemris.ooup.lab3.zad2.menus.TextEditorMenu;
import hr.fer.zemris.ooup.lab3.zad2.menus.TextEditorStatus;
import hr.fer.zemris.ooup.lab3.zad2.observers.ClipboardObserver;
import hr.fer.zemris.ooup.lab3.zad2.observers.CursorObserver;
import hr.fer.zemris.ooup.lab3.zad2.observers.StackObserver;
import hr.fer.zemris.ooup.lab3.zad2.observers.TextObserver;

public class TextEditor extends JFrame implements CursorObserver, TextObserver, ClipboardObserver, StackObserver {

	private static final long serialVersionUID = 1L;
	private TextEditorModel textEditorModel;
	private EditorPanel editorPanel;
	private Color c_gray_background = Color.decode("#A6A3A3");
	private ClipboardStack clipboardStack;
	private TextEditorMenu textEditorMenu;
	private TextEditorStatus textEditorStatus;
	private boolean shiftFlag = false;;
	private boolean ctrlFlag;
	private UndoManager undoManager;

	public TextEditor(TextEditorModel textEditorModel)
			throws IndexOutOfBoundsException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.undoManager = textEditorModel.getUndoManager();
		this.undoManager.subscribeToRedoStack(this);
		this.undoManager.subscribeToUndoStack(this);
		this.textEditorModel = textEditorModel;
		this.clipboardStack = new ClipboardStack();
		this.clipboardStack.subscribeToClipboardObservers(this);
		this.textEditorModel.subscribeToCursorObserver(this);
		this.textEditorModel.subscribeToTextObserver(this);
		initialize();
	}

	private void initialize() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(true);
		contentPanel.setBackground(Color.black);
		Border padding = BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black);
		contentPanel.setBorder(padding);
		this.setContentPane(contentPanel);

		contentPanel.setLayout(new BorderLayout());
		this.editorPanel = new EditorPanel(textEditorModel);
		editorPanel.setBackground(Color.black);
		this.editorPanel.addKeyListener(new MyKeyListener());

		textEditorMenu = new TextEditorMenu(this);
		this.setJMenuBar(textEditorMenu.getMenuBar());

		textEditorStatus = new TextEditorStatus();
		contentPanel.add(textEditorStatus.getStatus(), BorderLayout.PAGE_END);

		JScrollPane scrollPane = new JScrollPane(editorPanel);
		scrollPane.getViewport().setPreferredSize(new Dimension(400, 400));
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		this.setTitle("OOUP_TextEditor");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setBackground(c_gray_background);

		textEditorModel.notifyCursors();
		textEditorModel.notifyText();
	}

	class MyKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			shiftFlag = e.isShiftDown();
			ctrlFlag = e.isControlDown();
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				textEditorModel.setShiftFlag(false);
				if (shiftFlag) {
					textEditorModel.setShiftFlag(true);
					textEditorModel.setSelectionOrientation("r", editorPanel);
					shiftFlag = false;
				} else {
					textEditorModel.setSelectionRange(null);
				}
				textEditorModel.moveCursorRight();
				break;
			case KeyEvent.VK_LEFT:
				textEditorModel.setShiftFlag(false);
				if (shiftFlag) {
					textEditorModel.setShiftFlag(true);
					textEditorModel.setSelectionOrientation("l", editorPanel);
				} else {
					textEditorModel.setSelectionRange(null);
				}
				textEditorModel.moveCursorLeft();
				break;
			case KeyEvent.VK_UP:
				textEditorModel.setShiftFlag(false);
				if (shiftFlag) {
					textEditorModel.setShiftFlag(true);
					textEditorModel.setSelectionOrientation("u", editorPanel);
				} else {
					textEditorModel.setSelectionRange(null);
				}
				textEditorModel.moveCursorUp();
				break;
			case KeyEvent.VK_DOWN:
				textEditorModel.setShiftFlag(false);
				if (shiftFlag) {
					textEditorModel.setShiftFlag(true);
					textEditorModel.setSelectionOrientation("d", editorPanel);
				} else {
					textEditorModel.setSelectionRange(null);
				}
				textEditorModel.moveCursorDown();
				break;
			case KeyEvent.VK_BACK_SPACE:
				textEditorModel.deleteBefore();
				break;
			case KeyEvent.VK_ENTER:
				textEditorModel.insertNewLine();
				textEditorModel.setSelectionRange(null);
				break;
			case KeyEvent.VK_DELETE:
				textEditorModel.deleteAfter();
				textEditorModel.setSelectionRange(null);
				break;
			case MenuKeyEvent.VK_SHIFT:
				break;
			case MenuKeyEvent.VK_CONTROL:
				break;
			case MenuKeyEvent.VK_C:
				if (ctrlFlag) {
					System.out.println(textEditorModel.getSelectionRange());
					clipboardStack
							.addToStack(textEditorModel.locationRangeToString(textEditorModel.getSelectionRange()));
					textEditorModel.setSelectionRange(null);
				} else {
					textEditorModel.insert(e.getKeyChar());
					textEditorModel.moveCursorRight();
				}
				break;
			case MenuKeyEvent.VK_X:
				if (ctrlFlag) {
					clipboardStack
							.addToStack(textEditorModel.locationRangeToString(textEditorModel.getSelectionRange()));
					textEditorModel.deleteRange(textEditorModel.getSelectionRange());
					textEditorModel.setSelectionRange(null);
				} else {
					textEditorModel.insert(e.getKeyChar());
					textEditorModel.moveCursorRight();
				}
				break;
			case MenuKeyEvent.VK_V:
				if (ctrlFlag) {
					List<String> text = clipboardStack.readFromTop();
					if (text == null)
						text = new ArrayList<String>();
					textEditorModel.insert(text);
					textEditorModel.setSelectionRange(null);
				} else {
					textEditorModel.insert(e.getKeyChar());
					textEditorModel.moveCursorRight();
				}
				break;
			case MenuKeyEvent.VK_Z:
				if (ctrlFlag) {
					textEditorModel.undoCommand();
					textEditorModel.setSelectionRange(null);
				} else {
					textEditorModel.insert(e.getKeyChar());
					textEditorModel.moveCursorRight();
				}
				break;
			case MenuKeyEvent.VK_Y:
				if (ctrlFlag) {
					textEditorModel.redoCommand();
					textEditorModel.setSelectionRange(null);
				} else {
					textEditorModel.insert(e.getKeyChar());
					textEditorModel.moveCursorRight();
				}
				break;

			default:
				textEditorModel.insert(e.getKeyChar());
				textEditorModel.moveCursorRight();
				textEditorModel.setSelectionRange(null);
				updateMenu();
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

	}

	public EditorPanel getEditorPanel() {
		return this.editorPanel;
	}

	public Boolean getCTRLFlag() {
		return this.ctrlFlag;
	}

	public TextEditorModel getTextEditorModel() {
		return this.textEditorModel;
	}

	public ClipboardStack getClipboardStack() {
		return this.clipboardStack;
	}

	@Override
	public void updateCursorLocation(Location location) {
		textEditorStatus.updateCursorLocation(location);
		editorPanel.updateCursor(location);

	}

	@Override
	public void updateText() {
		editorPanel.updateText();
		updateMenu();
	}

	@Override
	public void updateClipboard() {
		textEditorModel.notifyClipbpoard();
		updateMenu();
	}

	@Override
	public void updateStack() {
		updateMenu();
	}

	private void updateMenu() {
		if (textEditorModel.getSelectionRange() == null
				|| (textEditorModel.getSelectionRange().getLocation_start().getX() == 0
						&& textEditorModel.getSelectionRange().getLocation_start().getY() == 0
						&& textEditorModel.getSelectionRange().getLocation_end().getX() == 0
						&& textEditorModel.getSelectionRange().getLocation_end().getY() == 0))
			textEditorMenu.getMenuItemCopy().setEnabled(false);
		else
			textEditorMenu.getMenuItemCopy().setEnabled(true);
		if (textEditorModel.getSelectionRange() == null
				|| (textEditorModel.getSelectionRange().getLocation_start().getX() == 0
						&& textEditorModel.getSelectionRange().getLocation_start().getY() == 0
						&& textEditorModel.getSelectionRange().getLocation_end().getX() == 0
						&& textEditorModel.getSelectionRange().getLocation_end().getY() == 0))
			textEditorMenu.getMenuItemCut().setEnabled(false);
		else
			textEditorMenu.getMenuItemCut().setEnabled(true);
		if (undoManager.getUndoStack().isEmpty())
			textEditorMenu.getMenuItemUndo().setEnabled(false);
		else
			textEditorMenu.getMenuItemUndo().setEnabled(true);
		if (undoManager.getRedoStack().isEmpty())
			textEditorMenu.getMenuItemRedo().setEnabled(false);
		else
			textEditorMenu.getMenuItemRedo().setEnabled(true);
		if (clipboardStack.getTexts().isEmpty())
			textEditorMenu.getMenuItemPaste().setEnabled(false);
		else
			textEditorMenu.getMenuItemPaste().setEnabled(true);
		if (clipboardStack.getTexts().isEmpty() || textEditorModel.getSelectionRange() == null)
			textEditorMenu.getMenuItemPasteAndTake().setEnabled(false);
		else
			textEditorMenu.getMenuItemPasteAndTake().setEnabled(true);
		if (textEditorModel.getSelectionRange() == null
				|| (textEditorModel.getSelectionRange().getLocation_start().getX() == 0
						&& textEditorModel.getSelectionRange().getLocation_end().getX() == 0
						&& textEditorModel.getSelectionRange().getLocation_start().getY() == 0
						&& textEditorModel.getSelectionRange().getLocation_end().getY() == 0))
			textEditorMenu.getMenuItemDelete().setEnabled(false);
		else
			textEditorMenu.getMenuItemDelete().setEnabled(true);
		if (textEditorModel.getLines() == null || textEditorModel.getLines().isEmpty())
			textEditorMenu.getMenuItemClear().setEnabled(false);
		else
			textEditorMenu.getMenuItemClear().setEnabled(true);
	}
}
