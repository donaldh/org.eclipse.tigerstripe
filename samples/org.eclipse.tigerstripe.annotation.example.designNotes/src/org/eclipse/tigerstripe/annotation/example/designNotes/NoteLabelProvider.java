package org.eclipse.tigerstripe.annotation.example.designNotes;

import org.eclipse.jface.viewers.LabelProvider;

public class NoteLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {

		if (element instanceof Note) {
			Note note = (Note) element;
			String text = note.getText();
			
			if ( text == null )
				return "Note";
			
			if (text.length() < 6) {
				return "Note: " + text;
			} else {
				return "Note: " + text.substring(0, 5) + "...";
			}
		}
		return super.getText(element);
	}

}
