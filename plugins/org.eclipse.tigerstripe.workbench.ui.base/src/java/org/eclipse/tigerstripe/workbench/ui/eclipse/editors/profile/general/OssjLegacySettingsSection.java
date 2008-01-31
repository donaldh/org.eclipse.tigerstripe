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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.general;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.ProfileEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjLegacySettingsSection extends TigerstripeSectionPart {

	private HashMap<String, Button> buttonMap = new HashMap<String, Button>();
	private OssjLegacySettingsProperty property;

	public OssjLegacySettingsSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
		setTitle("&OSS/J Legacy Settings");
		getSection().marginWidth = 10;
		getSection().marginHeight = 5;
		getSection().clientVerticalSpacing = 4;

		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 1;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData();
		getSection().setLayoutData(td);

		createSelectionControls(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createSelectionControls(Composite parent, FormToolkit toolkit) {
		buttonMap.clear();

		TableWrapData td = null;

		ProfileEditor editor = (ProfileEditor) getPage().getEditor();
		try {
			IWorkbenchProfile profile = editor.getProfile();
			property = (OssjLegacySettingsProperty) profile
					.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

			String[][] propertyDefs = property.getProperties();
			for (String[] oneProp : propertyDefs) {
				final String propKey = oneProp[0];
				final String propLabel = oneProp[1];
				// final String propDefault = oneProp[2];// not used here
				final String sectionTitle = oneProp[3];

				if (!"".equals(sectionTitle)) {
					Label l = toolkit.createLabel(parent, "", SWT.NONE);
					td = new TableWrapData(TableWrapData.FILL_GRAB);
					td.colspan = 2;
					td.heightHint = 4;
					l.setLayoutData(td);

					l = toolkit.createLabel(parent, sectionTitle, SWT.BOLD);
					l.setFont(new Font(null, "arial", 8, SWT.BOLD));
					td = new TableWrapData(TableWrapData.FILL_GRAB);
					td.colspan = 2;
					l.setLayoutData(td);
				}

				final Button button = toolkit.createButton(parent, propLabel,
						SWT.CHECK);
				button.setEnabled(ProfileEditor.isEditable());
				button.setSelection(property.getPropertyValue(propKey));
				if (ProfileEditor.isEditable()) {
					button.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {
						}

						public void widgetSelected(SelectionEvent e) {
							boolean selection = button.getSelection();
							property.setPropertyValue(propKey, selection);
							markPageModified();
						}
					});
				}
				buttonMap.put(propKey, button);
				td = new TableWrapData();
				td.indent = 5;
				td.colspan = 2;
				button.setLayoutData(td);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	@Override
	public void refresh() {
		updateSection();
	}

	protected void updateSection() {
		ProfileEditor editor = (ProfileEditor) getPage().getEditor();
		try {
			IWorkbenchProfile profile = editor.getProfile();
			property = (OssjLegacySettingsProperty) profile
					.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

			String[][] propertyDefs = property.getProperties();
			for (String[] oneProp : propertyDefs) {
				final String propKey = oneProp[0];
				Button button = buttonMap.get(propKey);
				button.setSelection(property.getPropertyValue(propKey));
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void markPageModified() {
		ProfileEditor editor = (ProfileEditor) getPage().getEditor();
		editor.pageModified();
	}

}
