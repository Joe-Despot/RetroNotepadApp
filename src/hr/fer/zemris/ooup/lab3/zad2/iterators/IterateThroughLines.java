package hr.fer.zemris.ooup.lab3.zad2.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IterateThroughLines implements Iterator<String> {

	List<String> lines;
	private int current_index;
	private int last_index;

	public IterateThroughLines(List<String> lines) {
		this.lines = new ArrayList<String>(lines);
		this.current_index = 0;
		this.last_index = lines.size();
	}

	@Override
	public boolean hasNext() {
		return current_index < last_index && lines.get(current_index) != null;

	}

	@Override
	public String next() {
		if(hasNext())
			return lines.get(current_index++);
		else
			throw new IndexOutOfBoundsException();
	}

}
