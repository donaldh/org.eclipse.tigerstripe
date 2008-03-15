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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.IArtifactFormContentProvider;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.association.AssociationSpecificsSection;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjArtifactContentSection extends ArtifactSectionPart {

	public OssjArtifactContentSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider, SWT.NONE);
		setTitle("Details");
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

		createArtifactComponents(getBody(), getToolkit());
		createDescription(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createArtifactComponents(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		FormText rtext = toolkit.createFormText(parent, true);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		rtext.setLayoutData(td);
		rtext.setText(getContentProvider().getText(
				IArtifactFormContentProvider.ARTIFACT_CONTENT_COMPONENTS),
				true, false);

		// Fix for bug # 79 - Detail pane headings Hyperlinked
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if ("attributes".equals(e.getHref())) {
					IFormPart[] ifp = getManagedForm().getParts();
					for (IFormPart i : ifp) {
						if (i instanceof OssjArtifactAttributesSection) {
							OssjArtifactAttributesSection oaas = (OssjArtifactAttributesSection) i;
							oaas.getSection().setExpanded(true);
							oaas.getSection().forceFocus();
						}
					}
				} else if ("methods".equals(e.getHref())) {
					IFormPart[] ifp = getManagedForm().getParts();
					for (IFormPart i : ifp) {
						if (i instanceof OssjArtifactMethodsSection) {
							OssjArtifactMethodsSection oamd = (OssjArtifactMethodsSection) i;
							oamd.getSection().setExpanded(true);
							oamd.getSection().forceFocus();
						}
					}
				} else if ("constants".equals(e.getHref())) {
					IFormPart[] ifp = getManagedForm().getParts();
					for (IFormPart i : ifp) {
						if (i instanceof OssjArtifactConstantsSection) {
							OssjArtifactConstantsSection oacd = (OssjArtifactConstantsSection) i;
							oacd.getSection().setExpanded(true);
							oacd.getSection().forceFocus();
						}
					}
				} else if ("aEnd".equals(e.getHref())
						|| "zEnd".equals(e.getHref())) {
					IFormPart[] ifp = getManagedForm().getParts();
					for (IFormPart i : ifp) {
						if (i instanceof AssociationSpecificsSection) {
							AssociationSpecificsSection oacd = (AssociationSpecificsSection) i;
							oacd.getSection().setExpanded(true);
							oacd.getSection().forceFocus();
						}
					}
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
		String data = getContentProvider().getText(
				IArtifactFormContentProvider.ARTIFACT_CONTENT_DESCRIPTION);
		rtext.setText(data, true, false);
	}

}
