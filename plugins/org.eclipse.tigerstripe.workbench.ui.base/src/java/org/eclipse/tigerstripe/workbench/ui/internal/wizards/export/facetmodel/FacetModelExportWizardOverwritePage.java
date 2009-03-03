package org.eclipse.tigerstripe.workbench.ui.internal.wizards.export.facetmodel;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class FacetModelExportWizardOverwritePage extends WizardPage {

	protected FacetModelExportWizardOverwritePage(String pageName) {
		
		super(pageName);
		setTitle("Test Composite: Overwrite View");
		setDescription("Test description for composite overwrite view.");
	}

	public void createControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
		
		new Label(composite, SWT.LEFT).setText("THIS IS THE PAGE THAT WILL DISPLAY THE ARTIFACTS TO OVERWRITE");
		
		setControl(composite);
	}

}
