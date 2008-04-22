/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.m0Descriptor.header;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorOverviewPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.header.GeneralInfoSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.header.ProjectContentSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.header.WelcomeSection;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class M0OverviewPage extends GeneratorOverviewPage {
	public static final String PAGE_ID = "m0Descriptor.overview"; //$NON-NLS-1$

	public M0OverviewPage(FormEditor editor) {
		super(editor, PAGE_ID);
	}

	public M0OverviewPage() {
		super(PAGE_ID);
	}

	@Override
	protected String getFormTitle() {
		return "Tigerstripe M0-Level Generator";
	}

	@Override
	protected TigerstripeSectionPart getWelcomeSection(
			TigerstripeFormPage page, Composite body, FormToolkit toolkit) {
		return new WelcomeSection(page, body, toolkit) {
			protected void createWelcomeMsg(Composite parent,
					FormToolkit toolkit) {
				TableWrapData td = null;

				String data = "<form><p><b>Tigerstripe M0-Level Generator Editor.</b></p></form>";
				FormText rtext = toolkit.createFormText(parent, true);
				td = new TableWrapData(TableWrapData.FILL_GRAB);
				td.colspan = 2;
				rtext.setLayoutData(td);
				rtext.setText(data, true, false);
				rtext.addHyperlinkListener(new HyperlinkAdapter() {
				});
			}

		};
	}

	@Override
	protected TigerstripeSectionPart getGeneralInfoSection(
			TigerstripeFormPage page, Composite body, FormToolkit toolkit) {
		return new GeneralInfoSection(page, body, toolkit) {
			@Override
			protected String[] getSupportedNature() {
				return new String[] { EPluggablePluginNature.M0.name() };
			}
		};
	}

	@Override
	protected TigerstripeSectionPart getContentSection(
			TigerstripeFormPage page, Composite body, FormToolkit toolkit) {
		return new ProjectContentSection(page, body, toolkit);
	}

}
