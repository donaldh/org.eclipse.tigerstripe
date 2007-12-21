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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.header;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.properties.PluginDescriptorPropertiesPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.rules.PluginDescriptorRulesPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.runtime.RuntimePage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ProjectContentSection extends PluginDescriptorSectionPart {

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

		TableWrapData td = new TableWrapData(TableWrapData.FILL);
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
				.append("<li><a href=\"rules\">Template Trigger Rules</a>: each of the rules that will be executed when this template is run.</li>");
		buf
				.append("<li><a href=\"properties\">Properties Definitions</a>: properties available when running the template, either globally defined or relatively to an artifact.</li>");
		buf
				.append("<li><a href=\"runtime\">Runtime Properties</a>: defining what is available at runtime on the plugin's classpath.</li>");
		buf.append("</form>");
		rtext.setText(buf.toString(), true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if ("properties".equals(e.getHref())) {
					getPage().getEditor().setActivePage(
							PluginDescriptorPropertiesPage.PAGE_ID);
				} else if ("rules".equals(e.getHref())) {
					getPage().getEditor().setActivePage(
							PluginDescriptorRulesPage.PAGE_ID);
				} else if ("runtime".equals(e.getHref())) {
					getPage().getEditor().setActivePage(RuntimePage.PAGE_ID);
					// } else if ( "publish".equals( (String) e.getHref() )) {
					// getPage().getEditor().setActivePage(
					// PublishConfigurationPage.PAGE_ID );
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
		String data = "<form><p>The plugin descriptor is saved as <b>ts-plugin.xml</b> in the top level directory of the project.</p></form>";
		rtext.setText(data, true, false);
	}

}
