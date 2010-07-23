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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * @author Alena Repina
 */
public class ArtifactStereotypesSection extends ArtifactSectionPart {

	private StereotypeSectionManager stereotypeMgr;

	private Table annTable;

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
		Section section = getSection();
		FormToolkit toolkit = getToolkit();

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		section.setLayoutData(td);

		Composite body = getBody();
		GridData gd = new GridData(GridData.FILL_BOTH);
		body.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		body.setLayout(layout);
		section.setClient(body);

		annTable = toolkit.createTable(body, SWT.BORDER | SWT.FLAT);
		gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 3;
		annTable.setEnabled(!this.isReadonly());
		annTable.setLayoutData(gd);

		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		Button addAnno = toolkit.createButton(body, "Add", SWT.PUSH);
		addAnno.setData("name", "Add_Stereo_Artifact");
		addAnno.setEnabled(!this.isReadonly());
		addAnno.setLayoutData(gd);

		Button editAnno = toolkit.createButton(body, "Edit", SWT.PUSH);
		editAnno.setData("name", "Edit_Stereo_Artifact");
		editAnno.setEnabled(!this.isReadonly());
		editAnno.setLayoutData(gd);

		Button removeAnno = toolkit.createButton(body, "Remove", SWT.PUSH);
		removeAnno.setData("name", "Remove_Stereo_Artifact");
		removeAnno.setEnabled(!this.isReadonly());
		removeAnno.setLayoutData(gd);

		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		stereotypeMgr = new StereotypeSectionManager(addAnno, editAnno,
				removeAnno, annTable, (ArtifactComponent) getIArtifact(),
				getBody().getShell(), editor);
		stereotypeMgr.delegate();

		// updateForm();
		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
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
			stereotypeMgr.refresh();
		}
	}

	public Table getAnnTable() {
		return annTable;
	}

}
