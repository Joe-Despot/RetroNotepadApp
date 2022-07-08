package hr.fer.zemris.ooup.lab3.zad2.editaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.ooup.lab3.zad2.observers.StackObserver;

public class UndoManager {

	public Stack<EditAction> getUndoStack() {
		return undoStack;
	}

	public Stack<EditAction> getRedoStack() {
		return redoStack;
	}

	private static UndoManager instance;

	private Stack<EditAction> undoStack;
	private Stack<EditAction> redoStack;

	private List<StackObserver> undoStackSubscribers;
	private List<StackObserver> redoStackSubscribers;

	public UndoManager() {
		this.undoStack = new Stack<EditAction>();
		this.redoStack = new Stack<EditAction>();
		this.redoStackSubscribers = new ArrayList<StackObserver>();
		this.undoStackSubscribers = new ArrayList<StackObserver>();
	}

	public static UndoManager getInstance() {
		if (instance == null) {
			instance = new UndoManager();
		}
		return instance;
	}

	public void undo() {
		System.out.println("undo: " + getRedoStack());
		if (undoStack.isEmpty()) {
			return;
		}
		EditAction undo = undoStack.pop();
		redoStack.push(undo);
		undo.execute_undo();
		notifyRedo();
		notifyUndo();
	}

	public void redo() {
		System.out.println("redo: " + getRedoStack());
		if (redoStack.isEmpty()) {
			return;
		}
		EditAction redo = redoStack.pop();
		redo.execute_do();
		undoStack.push(redo);
		notifyRedo();
	}

	public void push(EditAction c) {
		redoStack.clear();
		undoStack.push(c);
		notifyRedo();
		notifyUndo();
	}

	public void subscribeToRedoStack(StackObserver stackAction) {
		System.out.println("SUBSKRIBED");
		this.redoStackSubscribers.add(stackAction);
	}

	public void subscribeToUndoStack(StackObserver stackAction) {
		this.undoStackSubscribers.add(stackAction);
	}

	public void unsubscribeFromRedoStack(StackObserver stackAction) {
		this.redoStackSubscribers.remove(stackAction);
	}

	public void unsubscribeFromUndoStack(StackObserver stackAction) {
		this.undoStackSubscribers.remove(stackAction);
	}

	public void notifyRedo() {
		for (StackObserver stackObserver : redoStackSubscribers)
			stackObserver.updateStack();
	}

	public void notifyUndo() {
		for (StackObserver stackObserver : undoStackSubscribers)
			stackObserver.updateStack();
	}

}
