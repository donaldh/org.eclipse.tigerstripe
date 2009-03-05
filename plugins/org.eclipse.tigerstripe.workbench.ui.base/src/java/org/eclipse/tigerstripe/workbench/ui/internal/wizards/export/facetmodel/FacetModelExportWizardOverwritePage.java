package org.eclipse.tigerstripe.workbench.ui.internal.wizards.export.facetmodel;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

public class FacetModelExportWizardOverwritePage extends WizardPage {

	private static final String WIZARD_PAGE_NAME = "EXPORT_WIZARD_OVERWRITE";	
	
	private TableViewer tableViewer;
	
	protected FacetModelExportWizardOverwritePage() {
		
		super(WIZARD_PAGE_NAME);
		setTitle("Test Composite: Overwrite View");
		setDescription("Test description for composite overwrite view.");
	}

	public void createControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		new Label(composite, SWT.LEFT).setText("Changes to be performed");
		
		Table table = new Table(composite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		TableViewer tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		setControl(composite);
	}
	
	public void init() {
		
		System.out.println("In the intialize method");
	}

}
