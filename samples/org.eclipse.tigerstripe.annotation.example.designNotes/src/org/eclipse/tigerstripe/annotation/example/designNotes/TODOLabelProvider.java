package org.eclipse.tigerstripe.annotation.example.designNotes;

import org.eclipse.jface.viewers.LabelProvider;

public class TODOLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {

		if (element instanceof TODO) {
			TODO note = (TODO) element;
			String text = note.getSummary();
			
			if ( text == null )
				return "TODO";
			
			if (text.length() < 6) {
				return "TODO: " + text;
			} else {
				return "TODO: " + text.substring(0, 5) + "...";
			}
		}
		return super.getText(element);
	}

}
