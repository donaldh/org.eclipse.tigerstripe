/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * @author Alena Repina
 */
public class ArtifactStereotypesSection extends ArtifactSectionPart {

	public static int MASTER_TABLE_COMPONENT_WIDTH = 250;

	private StereotypeSectionManager stereotypeMgr;

	private Table annTable;
	private Text description;
	private Composite detailsComposite;

	public ArtifactStereotypesSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider, SWT.NONE);
		setTitle("Stereotypes");
		createContent();
	}

	@Override
	protected void createContent() {
		IManagedForm managedForm = getPage().getManagedForm();
		FormToolkit toolkit = getToolkit();

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		Composite body = getToolkit().createComposite(getSection());
		body.setLayout(TigerstripeLayoutFactory.createClearGridLayout(1, false));
		SashForm sashForm = new SashForm(body, SWT.HORIZONTAL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(body.getMenu());
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);

		sashForm.setWeights(new int[] { 1, 2 });

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	private void createMasterPart(final IManagedForm managedForm,
			Composite parent) {
		FormToolkit toolkit = getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);

		Composite sectionClient = toolkit.createComposite(section);
		GridLayout layout = new GridLayout(2, false);
		sectionClient.setLayout(layout);

		annTable = toolkit.createTable(sectionClient, SWT.BORDER | SWT.FLAT);
		annTable.setEnabled(!this.isReadonly());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = MASTER_TABLE_COMPONENT_WIDTH;
		annTable.setLayoutData(gd);

		Composite buttonClient = toolkit.createComposite(sectionClient);
		buttonClient.setLayoutData(new GridData(GridData.FILL));
		buttonClient.setLayout(TigerstripeLayoutFactory
				.createButtonsGridLayout());

		Button addAnno = toolkit.createButton(buttonClient, "Add", SWT.PUSH);
		addAnno.setData("name", "Add_Stereo_Artifact");
		addAnno.setEnabled(!this.isReadonly());
		addAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));

		Button editAnno = toolkit.createButton(buttonClient, "Edit", SWT.PUSH);
		editAnno.setData("name", "Edit_Stereo_Artifact");
		editAnno.setEnabled(!this.isReadonly());
		editAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));

		Button removeAnno = toolkit.createButton(buttonClient, "Remove",
				SWT.PUSH);
		removeAnno.setData("name", "Remove_Stereo_Artifact");
		removeAnno.setEnabled(!this.isReadonly());
		removeAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));

		stereotypeMgr = new StereotypeSectionManager(addAnno, editAnno,
				removeAnno, annTable, getIArtifact(), sectionClient.getShell(),
				new PageModifyCallback(getPage()) {
					@Override
					public void modify() {
						super.modify();
						refreshDetailsPart();
					}
				});
		stereotypeMgr.delegate();

		toolkit.paintBordersFor(sectionClient);
		section.setClient(sectionClient);
	}

	private void createDetailsPart(final IManagedForm mform, Composite parent) {
		FormToolkit toolkit = getToolkit();
		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayout(new GridLayout());
		detailsComposite = toolkit.createComposite(section);
		detailsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		detailsComposite.setLayout(new GridLayout());

		Label label = new Label(detailsComposite, SWT.NONE);
		label.setText("Description:");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		description = new Text(detailsComposite, SWT.MULTI
				| SWT.READ_ONLY | SWT.V_SCROLL | SWT.WRAP);
		description.setBackground(parent.getBackground());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 350;
		gd.heightHint = 50;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		description.setLayoutData(gd);
		
		setVisibleRecursive(detailsComposite, false);

		annTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshDetailsPart();
			}
		});

		toolkit.paintBordersFor(detailsComposite);
		section.setClient(detailsComposite);
	}

	private void refreshDetailsPart() {
		String result = "";
		TableItem[] items = annTable.getSelection();
		if (items.length > 0) {
			Object selected = items[0].getData();
			if (selected != null && selected instanceof IStereotypeInstance) {
				IStereotype stereotype = ((IStereotypeInstance) selected)
						.getCharacterizingStereotype();
				if (stereotype != null && stereotype.getDescription() != null) {
					result = stereotype.getDescription();
				}
			}
			setDetailsPartVisibility(true);
		} else {
			setDetailsPartVisibility(false);
		}
		description.setText(result);
	}

	private void setDetailsPartVisibility(boolean visible) {
		if (detailsComposite.isVisible() != visible) {
			setVisibleRecursive(detailsComposite, visible);
		}
	}

	private void setVisibleRecursive(Control control, boolean visible) {
		control.setVisible(visible);

		if (control instanceof Composite) {
			for (Control subControl : ((Composite) control).getChildren()) {
				setVisibleRecursive(subControl, visible);
			}
		}
	}

	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		if (stereotypeMgr != null) {
			stereotypeMgr.setArtifactComponent(getIArtifact());
			stereotypeMgr.refresh();
		}
	}

	public Table getAnnTable() {
		return annTable;
	}

}
