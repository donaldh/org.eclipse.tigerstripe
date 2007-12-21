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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.header;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.dependencies.DescriptorDependenciesPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins.PluginConfigurationPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.repositories.DescriptorRepositoriesPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ProjectContentSection extends TigerstripeDescriptorSectionPart {

	public ProjectContentSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Descriptor Content");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		getSection().setLayout(layout);

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
				.append("<li><b>General Informations</b>: containing high level information about the project.</li>");
		// buf.append("<li><a href=\"repositories\">Artifact Repositories</a>:
		// containing all artifacts used to model the OSS/J Interface.</li>");
		buf
				.append("<li><a href=\"plugins\">Generation Details</a>: defining what Tigerstripe will generate for this Service Contract Project.</li>");
		if (!this.isReadonly()) {
			buf
					.append("<li><a href=\"dependencies\">Dependencies</a>: defining references to Tigerstripe models or libraries.</li>");
		}
		buf.append("</form>");
		rtext.setText(buf.toString(), true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if ("repositories".equals(e.getHref())) {
					getPage().getEditor().setActivePage(
							DescriptorRepositoriesPage.PAGE_ID);
				} else if ("plugins".equals(e.getHref())) {
					getPage().getEditor().setActivePage(
							PluginConfigurationPage.PAGE_ID);
				} else if ("dependencies".equals(e.getHref())) {
					getPage().getEditor().setActivePage(
							DescriptorDependenciesPage.PAGE_ID);
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
		String data = "<form><p>The project descriptor is saved as <b>"
				+ ITigerstripeConstants.PROJECT_DESCRIPTOR
				+ "</b> in the top level directory of the project.</p></form>";
		rtext.setText(data, true, false);
	}

}
