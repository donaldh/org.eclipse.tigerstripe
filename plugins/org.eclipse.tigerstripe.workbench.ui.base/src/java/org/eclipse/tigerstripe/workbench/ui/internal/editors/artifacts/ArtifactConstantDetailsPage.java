/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactComponent;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ArtifactConstantDetailsPage implements IDetailsPage {

	private StereotypeSectionManager stereotypeMgr;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class ConstantDetailsPageListener implements ModifyListener,
			KeyListener, SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.F3)
				navigateToKeyPressed(e);
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	private IManagedForm form;

	private ArtifactConstantsSection master;

	private ILiteral literal;

	private Button addAnno;

	private Button editAnno;

	private Button removeAnno;

	private Table annTable;

	private boolean silentUpdate = false;

	private boolean isReadOnly = false;

	public ArtifactConstantDetailsPage(boolean isReadOnly) {
		super();
		this.isReadOnly = isReadOnly;
	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		createFieldInfo(parent);
		createAnnotations(parent, form.getToolkit());
		form.getToolkit().paintBordersFor(parent);
	}

	private void createAnnotations(Composite parent, FormToolkit toolkit) {
		
		Label label = toolkit.createLabel(parent, "Annotations");
		label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		
		Composite innerComposite = toolkit.createComposite(parent);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		innerComposite.setLayoutData(td);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		innerComposite.setLayout(layout);
		
		annTable = toolkit.createTable(innerComposite, SWT.BORDER);
		annTable.setEnabled(!isReadOnly);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 3;
		td.heightHint = 70;
		annTable.setLayoutData(td);

		addAnno = toolkit.createButton(innerComposite, "Add", SWT.PUSH);
		addAnno.setEnabled(!isReadOnly);
		td = new TableWrapData(TableWrapData.FILL);
		addAnno.setLayoutData(td);

		editAnno = toolkit.createButton(innerComposite, "Edit", SWT.PUSH);
		editAnno.setEnabled(!isReadOnly);
		td = new TableWrapData(TableWrapData.FILL);
		editAnno.setLayoutData(td);

		removeAnno = toolkit.createButton(innerComposite, "Remove", SWT.PUSH);
		removeAnno.setEnabled(!isReadOnly);
	}

	// ============================================================
	private void setLiteral(ILiteral literal) {
		this.literal = literal;
	}

	private ILiteral getLiteral() {
		return literal;
	}

	// ============================================================
	private Text nameText;

	// private Text typeText;

	private Text valueText;

	private CCombo baseTypeCombo;

	private final static String[] supportedPrimitiveTypes = { "int", "String" };

	// private Button typeBrowseButton;

	private Text commentText;

	private void createFieldInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		ConstantDetailsPageListener adapter = new ConstantDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		label.setEnabled(!isReadOnly);
		nameText = toolkit.createText(sectionClient, "");
		nameText.setEnabled(!isReadOnly);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addModifyListener(adapter);

		label = toolkit.createLabel(sectionClient, "");

		label = toolkit.createLabel(sectionClient, "Description: ");
		label.setEnabled(!isReadOnly);
		commentText = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		commentText.setEnabled(!isReadOnly);
		commentText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		commentText.addModifyListener(adapter);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 70;
		gd.widthHint = 300;
		commentText.setLayoutData(gd);
		label = toolkit.createLabel(sectionClient, "");

		label = toolkit.createLabel(sectionClient, "Visibility: ");
		label.setEnabled(!isReadOnly);
		Composite visiComposite = toolkit.createComposite(sectionClient);
		visiComposite.setEnabled(!isReadOnly);
		gLayout = new GridLayout();
		gLayout.numColumns = 3;
		visiComposite.setLayout(gLayout);
		visiComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		publicButton = toolkit.createButton(visiComposite, "Public", SWT.RADIO);
		publicButton.setEnabled(!isReadOnly);
		publicButton.addSelectionListener(adapter);
		protectedButton = toolkit.createButton(visiComposite, "Protected",
				SWT.RADIO);
		protectedButton.setEnabled(!isReadOnly);
		protectedButton.addSelectionListener(adapter);
		privateButton = toolkit.createButton(visiComposite, "Private",
				SWT.RADIO);
		privateButton.setEnabled(!isReadOnly);
		privateButton.addSelectionListener(adapter);
		label = toolkit.createLabel(sectionClient, "");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		label = toolkit.createLabel(sectionClient, "Value: ");
		label.setEnabled(!isReadOnly);
		valueText = toolkit.createText(sectionClient, "");
		valueText.setEnabled(!isReadOnly);
		valueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		valueText.addModifyListener(adapter);

		label = toolkit.createLabel(sectionClient, "");

		label = toolkit.createLabel(sectionClient, "Type: ", SWT.WRAP);
		label.setEnabled(!isReadOnly);
		baseTypeCombo = new CCombo(sectionClient, SWT.SINGLE | SWT.READ_ONLY
				| SWT.BORDER);
		baseTypeCombo.setEnabled(!isReadOnly);
		toolkit.adapt(this.baseTypeCombo, true, true);

		for (int i = 0; i < supportedPrimitiveTypes.length; i++) {
			baseTypeCombo.add(supportedPrimitiveTypes[i]);
		}
		baseTypeCombo.addSelectionListener(adapter);
		label = toolkit.createLabel(sectionClient, "", SWT.WRAP);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
	}

	// ===================================================================

	private Button publicButton;

	private Button protectedButton;

	private Button privateButton;

	public void initialize(IManagedForm form) {
		this.form = form;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	protected void pageModified() {
		master.markPageModified();
	}

	public boolean isDirty() {
		return master.isDirty();
	}

	public void commit(boolean onSave) {
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void setFocus() {
		nameText.setFocus();
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {

		if (part instanceof ArtifactConstantsSection) {
			master = (ArtifactConstantsSection) part;

			Table labelsTable = master.getViewer().getTable();

			ILiteral selected = (ILiteral) labelsTable.getSelection()[0].getData();
			setLiteral(selected);
			ArtifactEditorBase editor = (ArtifactEditorBase) master.getPage()
					.getEditor();

			if (stereotypeMgr == null) {
				stereotypeMgr = new StereotypeSectionManager(addAnno, editAnno,
						removeAnno, annTable, (ArtifactComponent) getLiteral(),
						master.getSection().getShell(), editor);
				stereotypeMgr.delegate();
			} else {
				stereotypeMgr
						.setArtifactComponent((ArtifactComponent) getLiteral());
			}

			updateForm();
		}
	}

	private void updateForm() {

		setSilentUpdate(true);
		ILiteral literal = getLiteral();
		nameText.setText(getLiteral().getName());

		String typeFqn = Misc.removeJavaLangString(getLiteral().getType()
				.getFullyQualifiedName());

		for (int i = 0; i < supportedPrimitiveTypes.length; i++) {
			if (typeFqn.equals(supportedPrimitiveTypes[i])) {
				baseTypeCombo.select(i);
				baseTypeCombo.setEnabled(!isReadOnly
						&& !(master.getIArtifact() instanceof EnumArtifact));
			}
		}

		valueText.setText(getLiteral().getValue());
		setVisibility(getLiteral().getVisibility());
		commentText.setText(getLiteral().getComment() != null ? getLiteral()
				.getComment() : "");

		if (stereotypeMgr != null) {
			stereotypeMgr.refresh();
		}

		setSilentUpdate(false);
	}

	private void setVisibility(EVisibility visibility) {
		if (visibility == null){
			visibility = EVisibility.PUBLIC;
		}
		publicButton
				.setSelection(visibility.equals(EVisibility.PUBLIC));
		protectedButton
				.setSelection(visibility.equals(EVisibility.PROTECTED));
		privateButton
				.setSelection(visibility.equals(EVisibility.PRIVATE));
	}

	private EVisibility getVisibility() {
		if (publicButton.getSelection())
			return EVisibility.PUBLIC;
		else if (protectedButton.getSelection())
			return EVisibility.PROTECTED;
		else
			return EVisibility.PRIVATE;
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	private void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	/**
	 * If silent Update is set, the form should not consider the updates to
	 * fields.
	 * 
	 * @return
	 */
	private boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	public void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == publicButton || e.getSource() == privateButton
				|| e.getSource() == protectedButton) {
			getLiteral().setVisibility(getVisibility());
		} else if (e.getSource() == baseTypeCombo) {
			IType type = getLiteral().makeType();
			type.setFullyQualifiedName(baseTypeCombo.getItem(baseTypeCombo
					.getSelectionIndex()));
			type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);;
			getLiteral().setType(type);
		}
		pageModified();
	}

	public void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			if (e.getSource() == nameText) {
				getLiteral().setName(nameText.getText().trim());
				if (master != null) {
					TableViewer viewer = master.getViewer();
					// viewer.refresh(getLabel());
					viewer.refresh();
				}
			} else if (e.getSource() == valueText) {
				getLiteral().setValue(valueText.getText().trim());
				if (master != null) {
					TableViewer viewer = master.getViewer();
					viewer.refresh();

				}
			} else if (e.getSource() == commentText) {
				getLiteral().setComment(commentText.getText().trim());
			}

			pageModified();
		}
	}

	private void navigateToKeyPressed(KeyEvent e) {
		master.navigateToKeyPressed(e);
	}

	public Table getAnnTable() {
		return annTable;
	}

	public IManagedForm getForm() {
		return form;
	}

}
