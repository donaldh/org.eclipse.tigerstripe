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

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions.TSOpenAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class ArtifactSectionPart extends TigerstripeSectionPart {

	private IOssjArtifactFormLabelProvider labelProvider;

	private IOssjArtifactFormContentProvider contentProvider;

	public ArtifactSectionPart(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider, int style) {

		// Annoyance 14 - Removed Description (js)
		super(page, parent, toolkit,
				style != ExpandableComposite.NO_TITLE ? style
						| ExpandableComposite.TITLE_BAR
						: ExpandableComposite.NO_TITLE);

		this.labelProvider = labelProvider;
		this.contentProvider = contentProvider;
	}

	protected boolean isReadonly() {
		return getIArtifact().isReadonly();
	}

	protected IAbstractArtifact getIArtifact() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		return editor.getIArtifact();
	}

	protected IOssjArtifactFormContentProvider getContentProvider() {
		return contentProvider;
	}

	protected void setContentProvider(
			IOssjArtifactFormContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	protected IOssjArtifactFormLabelProvider getLabelProvider() {
		return labelProvider;
	}

	protected void setLabelProvider(IOssjArtifactFormLabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	protected void navigateToKeyPressed(KeyEvent e) {
		if (e.getSource() instanceof Text) {
			Text text = (Text) e.getSource();
			String fqn = text.getText();
			if (fqn != null && fqn.length() != 0) {
				if (getIArtifact() != null
						&& getIArtifact().getTigerstripeProject() != null) {
					try {
						IArtifactManagerSession session = getIArtifact()
								.getTigerstripeProject().getArtifactManagerSession();
						IAbstractArtifact target = session
								.getArtifactByFullyQualifiedName(fqn);
						if (target != null) {
							IWorkbenchPage page = PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getActivePage();
							TSOpenAction.openEditor(target, page);
						}
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
					}
				}
			}
		}
	}

}
