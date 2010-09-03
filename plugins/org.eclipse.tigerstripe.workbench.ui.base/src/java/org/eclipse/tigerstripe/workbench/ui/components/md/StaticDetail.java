package org.eclipse.tigerstripe.workbench.ui.components.md;

import org.eclipse.swt.widgets.Control;

public class StaticDetail implements IDetails {

	private final Control control;

	public StaticDetail(Control control) {
		this.control = control;
	}

	public void hide() {
		control.setVisible(false);
	}

	public void show() {
		control.setVisible(false);
	}

	public void init() {
		// Control allready created
	}

	public void switchTarget(Object target) {
	}
}
