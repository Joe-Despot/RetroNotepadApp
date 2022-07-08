package hr.fer.zemris.ooup.lab3.zad2.menus;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.BorderFactory;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.border.Border;

import hr.fer.zemris.ooup.lab3.zad2.file.OpenFrame;
import hr.fer.zemris.ooup.lab3.zad2.file.SaveFrame;
import hr.fer.zemris.ooup.lab3.zad2.interfacePlugin.Plugin;
import hr.fer.zemris.ooup.lab3.zad2.locations.Location;
import hr.fer.zemris.ooup.lab3.zad2.locations.LocationRange;
import hr.fer.zemris.ooup.lab3.zad2.texteditors.TextEditor;

public class TextEditorMenu implements ActionListener, ItemListener {

	private TextEditor textEditor;
	private JMenuBar menuBar;
	private JMenu menuEdit;
	private JMenu menuFile;
	private JMenu menuMove;
	private JMenuItem menuItemUndo;
	private JMenuItem menuItemRedo;
	private JMenuItem menuItemCut;
	private JMenuItem menuItemCopy;
	private JMenuItem menuItemPaste;
	private JMenuItem menuItemPAT;
	private JMenuItem menuItemDelete;
	private JMenuItem menuItemClear;
	private JMenuItem menuItemOpen;
	private JMenuItem menuItemSave;
	private JMenuItem menuItemExit;
	private JMenuItem menuItemCursorToStart;
	private JMenuItem menuItemCursorToEnd;
	private List<Class> allClasses;
	private List<Plugin> plugins;
	private Border blackline = BorderFactory.createLineBorder(Color.green);
	@SuppressWarnings("unused")
	private SaveFrame saveFrame;
	@SuppressWarnings("unused")
	private OpenFrame openFrame;

	public TextEditorMenu(TextEditor textEditor)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.textEditor = textEditor;
		menuBar = new JMenuBar();

		UIManager.put("MenuItem.background", Color.black);
		UIManager.put("MenuItem.foreground", Color.green);
		UIManager.put("MenuItem.opaque", true);

		UIManager.put("Menu.background", Color.black);
		UIManager.put("Menu.foreground", Color.green);
		UIManager.put("Menu.opaque", true);

		UIManager.put("TabbedPane.selected", Color.black);
		UIManager.put("MenuItem.selectionBackground", Color.black);
		UIManager.put("MenuItem.selectionForeground", Color.white);
		UIManager.put("Menu.selectionBackground", Color.black);
		UIManager.put("Menu.selectionForeground", Color.white);
		UIManager.put("MenuBar.selectionBackground", Color.black);
		UIManager.put("MenuBar.selectionForeground", Color.white);

		menuBar.setBorder(blackline);
		menuBar.setOpaque(true);
		menuBar.setBackground(Color.black);
		menuBar.setMargin(new Insets(3, 3, 3, 3));
		// Edit Menu
		menuEdit = new JMenu("Edit");
		menuEdit.setOpaque(true);
		menuEdit.setFont(new Font("Courier new", 0, 17));
		menuEdit.setBorder(blackline);
		menuBar.add(menuEdit);
		// Edit Menu items
		menuItemUndo = new JMenuItem("Undo");
		menuItemUndo.setEnabled(false);
		menuItemRedo = new JMenuItem("Redo");
		menuItemRedo.setEnabled(false);
		menuItemCut = new JMenuItem("Cut");
		menuItemCut.setEnabled(false);
		menuItemCopy = new JMenuItem("Copy");
		menuItemCopy.setEnabled(false);
		menuItemPaste = new JMenuItem("Paste");
		menuItemPaste.setEnabled(false);
		menuItemPAT = new JMenuItem("Paste and Take");
		menuItemPAT.setEnabled(false);
		menuItemDelete = new JMenuItem("Delete Selection");
		menuItemDelete.setEnabled(false);
		menuItemClear = new JMenuItem("Clear document");
		menuItemClear.setEnabled(false);

		menuItemUndo.addActionListener(this);
		menuItemRedo.addActionListener(this);
		menuItemCut.addActionListener(this);
		menuItemCopy.addActionListener(this);
		menuItemPaste.addActionListener(this);
		menuItemPAT.addActionListener(this);
		menuItemDelete.addActionListener(this);
		menuItemClear.addActionListener(this);

		menuEdit.add(menuItemUndo);
		menuEdit.add(menuItemRedo);
		menuEdit.add(menuItemCut);
		menuEdit.add(menuItemCopy);
		menuEdit.add(menuItemPaste);
		menuEdit.add(menuItemPAT);
		menuEdit.add(menuItemDelete);
		menuEdit.add(menuItemClear);
		menuBar.add(menuEdit);
		// File Menu
		menuFile = new JMenu("File");
		menuFile.setOpaque(true);
		menuFile.setFont(new Font("Courier new", 0, 17));
		menuFile.setBorder(blackline);
		menuBar.add(menuFile);
		// File Menu Items
		menuItemOpen = new JMenuItem("Open");
		menuItemSave = new JMenuItem("Save");
		menuItemExit = new JMenuItem("Exit");

		menuItemOpen.addActionListener(this);
		menuItemSave.addActionListener(this);
		menuItemExit.addActionListener(this);

		menuFile.add(menuItemOpen);
		menuFile.add(menuItemSave);
		menuFile.add(menuItemExit);

		// Move Menu
		menuMove = new JMenu("Move");
		menuMove.setOpaque(true);
		menuMove.setFont(new Font("Courier new", 0, 17));
		menuMove.setBorder(blackline);
		menuBar.add(menuMove);
		// Move Menu Items
		menuItemCursorToStart = new JMenuItem("Cursor to document start");
		menuItemCursorToEnd = new JMenuItem("Cursor to document end");

		menuItemCursorToStart.addActionListener(this);
		menuItemCursorToEnd.addActionListener(this);

		menuMove.add(menuItemCursorToStart);
		menuMove.add(menuItemCursorToEnd);

		initilizePluginMenu();
	}

	public JMenuBar getMenuBar() {
		return this.menuBar;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		textEditor.getEditorPanel().lockCursor();
		JMenuItem source = (JMenuItem) (e.getSource());
		String s = source.getText();
		if (s == "Undo") {
			textEditor.getTextEditorModel().undoCommand();
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Redo") {
			textEditor.getTextEditorModel().redoCommand();
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Cut") {
			textEditor.getClipboardStack().addToStack(textEditor.getTextEditorModel()
					.locationRangeToString(textEditor.getTextEditorModel().getSelectionRange()));
			textEditor.getTextEditorModel().deleteRange(textEditor.getTextEditorModel().getSelectionRange());
			textEditor.getTextEditorModel().setSelectionRange(new LocationRange());
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Copy") {
			textEditor.getClipboardStack().addToStack(textEditor.getTextEditorModel()
					.locationRangeToString(textEditor.getTextEditorModel().getSelectionRange()));
			textEditor.getTextEditorModel().setSelectionRange(new LocationRange());
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Paste") {
			List<String> text = textEditor.getClipboardStack().readFromTop();
			textEditor.getTextEditorModel().insert(text);
			textEditor.getTextEditorModel().setSelectionRange(new LocationRange());
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Paste and take") {
			List<String> text = textEditor.getClipboardStack().readFromTop();
			textEditor.getClipboardStack().addToStack(textEditor.getTextEditorModel()
					.locationRangeToString(textEditor.getTextEditorModel().getSelectionRange()));
			textEditor.getTextEditorModel().insert(text);
			textEditor.getTextEditorModel().setSelectionRange(new LocationRange());
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Delete Selection") {
			textEditor.getTextEditorModel().deleteRange(textEditor.getTextEditorModel().getSelectionRange());
			textEditor.getTextEditorModel().setSelectionRange(new LocationRange());
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Clear document") {
			textEditor.getTextEditorModel().deleteRange(new LocationRange(new Location(0, 0),
					new Location(
							textEditor.getTextEditorModel().getLines()
									.get(textEditor.getTextEditorModel().getLines().size() - 1).length(),
							textEditor.getTextEditorModel().getLines().size() - 1)));
			textEditor.getTextEditorModel().setSelectionRange(new LocationRange());
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Save") {
			saveFrame = new SaveFrame(textEditor.getTextEditorModel().getLines());
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Cursor to document start") {
			textEditor.getTextEditorModel().setCursorLocation(new Location(0, 0));
			textEditor.updateCursorLocation(new Location(0, 0));
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Cursor to document end") {
			textEditor.getTextEditorModel()
					.setCursorLocation(new Location(
							textEditor.getTextEditorModel().getLines()
									.get(textEditor.getTextEditorModel().getLines().size() - 1).length(),
							textEditor.getTextEditorModel().getLines().size() - 1));
			textEditor.updateCursorLocation(new Location(
					textEditor.getTextEditorModel().getLines()
							.get(textEditor.getTextEditorModel().getLines().size() - 1).length(),
					textEditor.getTextEditorModel().getLines().size() - 1));
			textEditor.getEditorPanel().unLockCursor();
		} else if (s == "Exit") {
			textEditor.dispose();
		} else if (s == "Open") {
			openFrame = new OpenFrame(textEditor.getTextEditorModel());
		} else {
			for (int i = 0; i < allClasses.size(); i++) {
				if (s.equals(formatToClassName(allClasses.get(i).toString()))) {
					plugins.get(i).execute(textEditor.getTextEditorModel(),
							textEditor.getTextEditorModel().getUndoManager(), textEditor.getClipboardStack());
				}
			}
			textEditor.getEditorPanel().unLockCursor();
		}

	}

	public void initilizePluginMenu() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		plugins = new ArrayList<Plugin>();
		JMenu plugin = new JMenu("Plugins");
		plugin.setOpaque(true);
		plugin.setFont(new Font("Courier new", 0, 17));
		plugin.setBorder(blackline);
		menuBar.add(plugin);

		allClasses = findAllClassesUsingClassLoader("hr.fer.zemris.ooup.lab3.zad2.plugins");

		for (int i = 0; i < allClasses.size(); i++) {
			Class<?> clazz = Class.forName(formatToPackage(allClasses.get(i).toString()));
			Constructor<?> constructor = clazz.getConstructor();
			Plugin instance = (Plugin) constructor.newInstance();
			plugins.add(instance);
			JMenuItem menuItemStatistics = new JMenuItem(instance.getName());
			menuItemStatistics.addActionListener(this);
			plugin.add(menuItemStatistics);
		}
	}

	private String formatToPackage(String s) {
		return s.substring(6, s.length());
	}

	private String formatToClassName(String s) {
		return s.substring(43, s.length());
	}

	@SuppressWarnings("rawtypes")
	public List<Class> findAllClassesUsingClassLoader(String packageName) {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines().filter(line -> line.endsWith(".class")).map(line -> getClass(line, packageName))
				.collect(Collectors.toList());
	}

	@SuppressWarnings("rawtypes")
	private Class getClass(String className, String packageName) {
		try {
			return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException e) {
			// handle the exception
		}
		return null;
	}

	public JMenuItem getMenuItemUndo() {
		return menuItemUndo;
	}

	public JMenuItem getMenuItemRedo() {
		return menuItemRedo;
	}

	public JMenuItem getMenuItemPaste() {
		return menuItemPaste;
	}

	public JMenuItem getMenuItemCopy() {
		return menuItemCopy;
	}

	public JMenuItem getMenuItemCut() {
		return menuItemCut;
	}

	public JMenuItem getMenuItemPasteAndTake() {
		return menuItemPAT;
	}

	public JMenuItem getMenuItemDelete() {
		return menuItemDelete;
	}

	public JMenuItem getMenuItemClear() {
		return menuItemClear;
	}

}
