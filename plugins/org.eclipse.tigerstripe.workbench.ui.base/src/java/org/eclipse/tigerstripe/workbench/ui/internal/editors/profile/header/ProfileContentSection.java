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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.header;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.primitive.PrimitiveTypesPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.stereotypes.StereotypesPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class ProfileContentSection extends TigerstripeSectionPart {

	public ProfileContentSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Profile Content");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.maxWidth = MAX_RIGHT_COLUMN_WIDTH;
		getSection().setLayoutData(td);

		createProjectComponents(getBody(), getToolkit());
		createDescription(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createProjectComponents(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		FormText rtext = toolkit.createFormText(parent, true);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		rtext.setLayoutData(td);
		StringBuffer buf = new StringBuffer();

		buf.append("<form>");
		buf
				.append("<li><b>General Information</b>: containing high level information about the profile.</li>");
		// buf.append("<li><a href=\"repositories\">Artifact Repositories</a>:
		// containing all artifacts used to model the OSS/J Interface.</li>");
		buf
				.append("<li><a href=\"stereotypes\">Stereotypes</a>: allowing to define stereotypes for application to a model with additional information for generation purpose.</li>");
		buf
				.append("<li><a href=\"primitiveTypes\">Primitive Types</a>: allowing to define specific primitive types to cover specific modeling needs.</li>");
		// buf.append("<li><a href=\"properties\">Properties Definitions</a>:
		// properties available at runtime.</li>");
		buf.append("</form>");
		rtext.setText(buf.toString(), true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if ("stereotypes".equals(e.getHref())) {
					getPage().getEditor()
							.setActivePage(StereotypesPage.PAGE_ID);
				} else if ("primitiveTypes".equals(e.getHref())) {
					getPage().getEditor().setActivePage(
							PrimitiveTypesPage.PAGE_ID);
					// } else if ( "dependencies".equals( (String) e.getHref()
					// )) {
					// getPage().getEditor().setActivePage(
					// DescriptorDependenciesPage.PAGE_ID );
				}
			}
		});
	}

	private void createDescription(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		FormText rtext = toolkit.createFormText(parent, true);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		rtext.setLayoutData(td);
		String data = "<form><p>A profile is saved as <b>*."
				+ IWorkbenchProfile.FILE_EXTENSION + "</b> file.</p></form>";
		rtext.setText(data, true, false);
	}

}
