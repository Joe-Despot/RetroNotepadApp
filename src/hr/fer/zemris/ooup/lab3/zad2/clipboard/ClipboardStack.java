package hr.fer.zemris.ooup.lab3.zad2.clipboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import hr.fer.zemris.ooup.lab3.zad2.observers.*;

public class ClipboardStack {

	private Stack<List<String>> texts;
	List<ClipboardObserver> clipboardSubscribers;

	public ClipboardStack() {
		this.texts = new Stack<List<String>>();
		clipboardSubscribers = new ArrayList<ClipboardObserver>();
	}

	public void addToStack(List<String> s) {
		if (s == null) {
			return;
		}
		texts.add(0, s);
		notifyClipboard();
		System.out.println("clipboard: " + this.texts);
	}

	public Stack<List<String>> getTexts() {
		return texts;
	}

	public void removeFromStack(List<String> s) {
		texts.remove(s);
		notifyClipboard();
	}

	public List<String> readFromTop() {
		notifyClipboard();
		if (texts.get(0) == null)
			return null;
		return texts.get(0);
	}

	public Boolean isEmpty() {
		return texts.isEmpty();
	}

	public void clearStack() {
		texts.clear();
		notifyClipboard();
	}

	public void subscribeToClipboardObservers(ClipboardObserver co) {
		clipboardSubscribers.add(co);
	}

	public void unSubscribeFromClipboardObservers(ClipboardObserver co) {
		clipboardSubscribers.remove(co);
	}

	public void notifyClipboard() {
		for (ClipboardObserver co : clipboardSubscribers)
			co.updateClipboard();
	}

}
