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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ComponentUtils;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ArtifactContentSection extends ArtifactSectionPart {

	private final TigerstripeFormPage page;

	public ArtifactContentSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider, SWT.NONE);
		setTitle("Details");
		this.page = page;
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

	private static final Map<String, Class<?>> HREFS_CLASSES = new HashMap<String, Class<?>>(8);

	static {
		HREFS_CLASSES.put("attributes", ArtifactAttributesSection.class);
		HREFS_CLASSES.put("methods", ArtifactMethodsSection.class);
		HREFS_CLASSES.put("constants", ArtifactConstantsSection.class);
		HREFS_CLASSES.put("aEnd", EndSection.class);
		HREFS_CLASSES.put("zEnd", EndSection.class);
	}

	private void createArtifactComponents(Composite parent, FormToolkit toolkit) {
		FormText rtext = toolkit.createFormText(parent, true);
		TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.colspan = 2;
		rtext.setLayoutData(twd);
		String text = getContentProvider().getText(
				IArtifactFormContentProvider.ARTIFACT_CONTENT_COMPONENTS);
		rtext.setText(text, true, false);

		// Fix for bug # 79 - Detail pane headings Hyperlinked
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				Class<?> sectionClass = HREFS_CLASSES.get(e.getHref());
				if (sectionClass != null) {
					for (IFormPart formPart : getManagedForm().getParts()) {
						if (sectionClass.isInstance(formPart)) {
							expandSetFocusAndScroll(((SectionPart) formPart)
									.getSection());
						}
						if (formPart instanceof EndSection) {
							((EndSection) formPart).selectEndByEnd(e.getHref()
									.toString());
						}
					}
				}
			}
		});

	}

	private void expandSetFocusAndScroll(Section section) {
		section.setExpanded(true);
		section.forceFocus();
		// determine where the section is and scroll so that
		// it is visible
		ComponentUtils.scrollToComponent(page.getManagedForm().getForm(), section);
	}

	private void createDescription(Composite parent, FormToolkit toolkit) {
		FormText rtext = toolkit.createFormText(parent, true);
		TableWrapData twd = new TableWrapData(TableWrapData.FILL);
		twd.colspan = 2;
		rtext.setLayoutData(twd);
		String data = getContentProvider().getText(
				IArtifactFormContentProvider.ARTIFACT_CONTENT_DESCRIPTION);
		rtext.setText(data, true, false);
	}
}
