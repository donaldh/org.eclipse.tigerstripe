package org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.stereotypes;

public interface Validatable {

	boolean hasErrors();

	String getErrorMessage();

	void validate();
	
}
