package org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.scope;

import org.eclipse.jface.viewers.ICellEditorValidator;

public enum EmptyValidator implements ICellEditorValidator {

	INSTANCE;
	
	public String isValid(Object value) {
		if (value.toString().isEmpty()) {
			return "Value can't be empty";
		} else {
			return null;
		}
	}

}
