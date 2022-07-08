package hr.fer.zemris.ooup.lab3.zad2.editaction;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.ooup.lab3.zad2.locations.Location;
import hr.fer.zemris.ooup.lab3.zad2.texteditors.TextEditorModel;

public class DeleteRangeManager implements EditAction {

	private TextEditorModel textEditorModel;
	private List<String> previousLines;
	private Location previousCursorLocation;

	public DeleteRangeManager(TextEditorModel tem) {
		this.textEditorModel = tem;
		this.previousLines = new ArrayList<String>(tem.getLines());
		this.previousCursorLocation = new Location(tem.getCursorLoc().getX(), tem.getCursorLoc().getY());

	}

	@Override
	public void execute_do() {

	}

	@Override
	public void execute_undo() {
		textEditorModel.setLines(previousLines);
		textEditorModel.setCursorLocation(previousCursorLocation);
		textEditorModel.notifyText();
		textEditorModel.notifyCursors();
	}

	@Override
	public String toString() {
		return "DeleteRangeManager";
	}

}