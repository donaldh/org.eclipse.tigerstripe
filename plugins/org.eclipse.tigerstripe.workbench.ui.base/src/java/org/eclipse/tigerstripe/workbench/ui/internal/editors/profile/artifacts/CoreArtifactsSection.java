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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.artifacts;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.ProfileEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class CoreArtifactsSection extends TigerstripeSectionPart {

	// Set the table column property names
	private final String CHECKED_COLUMN = "checked";

	private final String ARTIFACTYPE_COLUMN = "artifact type";

	// Set column names
	private String[] columnNames = new String[] { CHECKED_COLUMN,
			ARTIFACTYPE_COLUMN, };

	private TableViewer artifactTableViewer;

	public CoreArtifactsSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
		setTitle("&Core Artifact Settings");
		createContent();
	}

	public List<String> getColumnNames() {
		return Arrays.asList(columnNames);
	}

	@Override
	public void refresh() {
		artifactTableViewer.refresh(true);
	}

	@Override
	protected void createContent() {
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		// createComment(getBody(), getToolkit());
		createArtifactTable(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createArtifactTable(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		Table table = toolkit.createTable(parent, SWT.SINGLE | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION);
		table.setEnabled(ProfileEditor.isEditable());
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		table.setLayoutData(td);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column = new TableColumn(table, SWT.CENTER, 0);
		column.setText(" ");
		column.setWidth(20);

		// 2nd column with task Description
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Artifact Type");
		column.setWidth(300);

		artifactTableViewer = new TableViewer(table);
		artifactTableViewer.setUseHashlookup(true);

		artifactTableViewer.setColumnProperties(columnNames);

		// Create the cell editor
		CellEditor[] editors = new CellEditor[columnNames.length];

		// Column 1 : Completed (Checkbox)
		editors[0] = new CheckboxCellEditor(table);

		// Column 2 : none
		editors[1] = null;

		// Assign the cell editors to the viewer
		artifactTableViewer.setCellEditors(editors);
		artifactTableViewer
				.setLabelProvider(new ArtifactDetailsLabelProvider());
		artifactTableViewer
				.setContentProvider(new CoreArtifactDetailsContentProvider());

		try {
			ProfileEditor editor = (ProfileEditor) getPage().getEditor();
			IWorkbenchProfile handle = editor.getProfile();

			artifactTableViewer
					.setCellModifier(new ArtifactDetailsCellModifier(this));
			artifactTableViewer.setInput(handle);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void markPageModified() {
		ProfileEditor editor = (ProfileEditor) getPage().getEditor();
		editor.pageModified();
	}

	private class CoreArtifactDetailsContentProvider implements
			IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			IWorkbenchProfile profile = (IWorkbenchProfile) inputElement;
			CoreArtifactSettingsProperty property = (CoreArtifactSettingsProperty) profile
					.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
			return property.getDetails();
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
		}

	}
}
