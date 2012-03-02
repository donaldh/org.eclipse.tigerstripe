package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.wizards;

import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.PUSH;
import static org.eclipse.swt.layout.GridData.FILL_BOTH;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;

public class FilterArtifactsPage extends WizardPage {

	private CheckboxTableViewer viewer;
	private final ArtifactsProvider aprovider;

	protected FilterArtifactsPage(ArtifactsProvider aprovider) {
		super("Selected Artifacts Filter");
		setTitle("Selected Artifacts Filter");
		setDescription("Here you can filter the artifacts before they are added to the diagram");
		this.aprovider = aprovider;
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		parent = new Composite(parent, NONE);
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(parent);
		
		viewer = CheckboxTableViewer.newCheckList(parent, BORDER);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new AbstractArtifactLabelProvider());
		viewer.getControl().setLayoutData(new GridData(FILL_BOTH));
		createButtonsForButtonBar(parent);
		setControl(parent);
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		parent = new Composite(parent, NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).applyTo(parent);
		GridDataFactory.fillDefaults().applyTo(parent);
		Button selectAllButton = new Button(parent, PUSH);
		selectAllButton.setText("Select All");
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.setCheckedElements((Object[])viewer.getInput());
			}
		});
		setButtonLayoutData(selectAllButton);
		
		Button deSelectAllButton = new Button(parent, PUSH);
		deSelectAllButton.setText("Deselect All");
		deSelectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.setCheckedElements(new Object[0]);
			}
		});
		setButtonLayoutData(deSelectAllButton);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (viewer != null && visible) {
			IAbstractArtifact[] artifacts = aprovider.provide();
			viewer.setInput(artifacts);
			viewer.setCheckedElements(artifacts);
		}
	}

	public CheckboxTableViewer getViewer() {
		return viewer;
	}
}
