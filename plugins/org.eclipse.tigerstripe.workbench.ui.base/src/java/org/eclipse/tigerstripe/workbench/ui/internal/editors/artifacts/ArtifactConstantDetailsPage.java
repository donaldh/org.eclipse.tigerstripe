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

import org.eclipse.emf.common.util.URI;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener.IURIBaseProviderPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ArtifactConstantDetailsPage implements IDetailsPage,
		IURIBaseProviderPage {

	private TextEditListener nameEditListener;

	private StereotypeSectionManager stereotypeMgr;

	public URI getBaseURI() {
		return (URI) getLiteral().getAdapter(URI.class);
	}

	public ITigerstripeModelProject getProject() {
		try {
			if (getLiteral() != null)
				return getLiteral().getProject();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

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

	private final ArtifactConstantsSection master;

	private ILiteral literal;

	private Button addAnno;

	private Button editAnno;

	private Button removeAnno;

	private Table annTable;

	private boolean silentUpdate = false;

	private boolean isReadOnly = false;

	public ArtifactConstantDetailsPage(ArtifactConstantsSection master,
			boolean isReadOnly) {
		super();
		this.master = master;
		this.isReadOnly = isReadOnly;
	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		parent.setLayoutData(td);
		int height = createFieldInfo(parent);
		/*
		 * FIXME Just workaround to avoid appearing scrolls on details part.
		 */
		master.setMinimumHeight(height);

		form.getToolkit().paintBordersFor(parent);
	}

	private void createStereotypes(Composite parent, FormToolkit toolkit) {
		toolkit.createLabel(parent, "Stereotypes:");

		Composite innerComposite = toolkit.createComposite(parent);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		innerComposite.setLayoutData(gd);
		TableWrapLayout layout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		innerComposite.setLayout(layout);

		annTable = toolkit.createTable(innerComposite, SWT.BORDER);
		annTable.setEnabled(!isReadOnly);

		Composite buttonsClient = toolkit.createComposite(innerComposite);
		buttonsClient.setLayoutData(new TableWrapData(TableWrapData.FILL));
		layout = new TableWrapLayout();
		layout.topMargin = layout.bottomMargin = 0;
		buttonsClient.setLayout(layout);

		addAnno = toolkit.createButton(buttonsClient, "Add", SWT.PUSH);
		// Support for testing
		addAnno.setData("name", "Add_Stereotype_Attribute");
		addAnno.setEnabled(!isReadOnly);
		addAnno.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		editAnno = toolkit.createButton(buttonsClient, "Edit", SWT.PUSH);
		editAnno.setData("name", "Edit_Stereotype_Attribute");
		editAnno.setEnabled(!isReadOnly);
		editAnno.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		removeAnno = toolkit.createButton(buttonsClient, "Remove", SWT.PUSH);
		// Support for testing
		removeAnno.setData("name", "Remove_Stereotype_Attribute");
		removeAnno.setEnabled(!isReadOnly);
		removeAnno.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Point p = buttonsClient.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = p.y;
		annTable.setLayoutData(td);
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

	private int createFieldInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		ConstantDetailsPageListener adapter = new ConstantDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		layout.bottomMargin = layout.topMargin = 0;
		sectionClient.setLayout(layout);
		sectionClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		label.setEnabled(!isReadOnly);
		nameText = toolkit.createText(sectionClient, "");
		nameText.setEnabled(!isReadOnly);
		nameText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		nameText.addModifyListener(adapter);
		TigerstripeFormEditor editor = (TigerstripeFormEditor) ((TigerstripeFormPage) getForm()
				.getContainer()).getEditor();
		if (!isReadOnly) {
			nameEditListener = new TextEditListener(editor, "name",
					IModelChangeDelta.SET, this);
			nameText.addModifyListener(nameEditListener);
		}

		label = toolkit.createLabel(sectionClient, "Description: ");
		label.setEnabled(!isReadOnly);
		commentText = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		commentText.setEnabled(!isReadOnly);
		commentText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		commentText.addModifyListener(adapter);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		gd.heightHint = 70;
		gd.maxHeight = 70;
		commentText.setLayoutData(gd);

		label = toolkit.createLabel(sectionClient, "Visibility: ");
		label.setEnabled(!isReadOnly);

		Composite visiComposite = toolkit.createComposite(sectionClient);
		layout = new TableWrapLayout();
		layout.leftMargin = 0;
		layout.rightMargin = 0;
		layout.numColumns = 4;
		visiComposite.setEnabled(!isReadOnly);
		visiComposite.setLayout(layout);
		visiComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
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

		label = toolkit.createLabel(sectionClient, "Value: ");
		label.setEnabled(!isReadOnly);
		valueText = toolkit.createText(sectionClient, "");
		valueText.setEnabled(!isReadOnly);
		valueText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		valueText.addModifyListener(adapter);

		label = toolkit.createLabel(sectionClient, "Type: ");
		label.setEnabled(!isReadOnly);
		baseTypeCombo = new CCombo(sectionClient, SWT.SINGLE | SWT.READ_ONLY
				| SWT.BORDER);
		baseTypeCombo.setEnabled(!isReadOnly);
		toolkit.adapt(this.baseTypeCombo, true, true);

		for (int i = 0; i < supportedPrimitiveTypes.length; i++) {
			baseTypeCombo.add(supportedPrimitiveTypes[i]);
		}
		baseTypeCombo.addSelectionListener(adapter);
		baseTypeCombo.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		createStereotypes(sectionClient, form.getToolkit());

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		return sectionClient.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
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
			if (nameEditListener != null)
				nameEditListener.reset();

			Table labelsTable = master.getViewer().getTable();

			ILiteral selected = (ILiteral) labelsTable.getSelection()[0]
					.getData();
			setLiteral(selected);
			ArtifactEditorBase editor = (ArtifactEditorBase) master.getPage()
					.getEditor();

			if (stereotypeMgr == null) {
				stereotypeMgr = new StereotypeSectionManager(addAnno, editAnno,
						removeAnno, annTable, getLiteral(), master.getSection()
								.getShell(), editor);
				stereotypeMgr.delegate();
			} else {
				stereotypeMgr.setArtifactComponent(getLiteral());
			}

			updateForm();
		}
	}

	private void updateForm() {

		setSilentUpdate(true);
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
		if (visibility == null) {
			visibility = EVisibility.PUBLIC;
		}
		publicButton.setSelection(visibility.equals(EVisibility.PUBLIC));
		protectedButton.setSelection(visibility.equals(EVisibility.PROTECTED));
		privateButton.setSelection(visibility.equals(EVisibility.PRIVATE));
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
			type.setTypeMultiplicity(EMultiplicity.ZERO_ONE);
			;
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
