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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.segment.header;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.segment.scope.ScopePage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.segment.useCases.UseCasesPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class SegmentContentSection extends TigerstripeSectionPart {

	public SegmentContentSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Contract Facet");
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
				.append("<li><b>General Information</b>: containing high level information about the facet.</li>");
		buf
				.append("<li><a href=\"scope\">A Facet Scope</a>: defining the scope of this facet within the context of the project.</li>");
		buf
				.append("<li><a href=\"useCases\">A set of UseCases</a>: presenting how this facet is to be used.</li>");
		buf.append("</form>");
		rtext.setText(buf.toString(), true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if ("scope".equals(e.getHref())) {
					getPage().getEditor().setActivePage(ScopePage.PAGE_ID);
				} else if ("useCases".equals(e.getHref())) {
					getPage().getEditor().setActivePage(UseCasesPage.PAGE_ID);
					// } else if ( "dependencies".equals( (String) e.getHref()
					// )) {
					// getPage().getEditor().setActivePage(
					// DescriptorDependenciesPage.PAGE_ID );
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
		String data = "<form><p>A Contract Facet is saved as <b>*."
				+ IContractSegment.FILE_EXTENSION + "</b> file.</p></form>";
		rtext.setText(data, true, false);
	}

}
