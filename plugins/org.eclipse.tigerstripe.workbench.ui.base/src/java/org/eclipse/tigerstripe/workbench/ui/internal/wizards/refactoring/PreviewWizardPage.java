/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactFQRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;

public class PreviewWizardPage extends WizardPage {

	private class ModelChangeDeltaLabelProvider extends AbstractArtifactLabelProvider {

		@Override
		public Image getImage(Object element) {

			if (element instanceof ModelChangeDelta) {
				ModelChangeDelta delta = (ModelChangeDelta) element;
				return super.getImage(delta.getComponent());
			} else {
				return null;
			}

		}

		@Override
		public String getText(Object element) {

			if (element instanceof ModelChangeDelta) {

				ModelChangeDelta delta = (ModelChangeDelta) element;
				if (delta.getComponent() instanceof AbstractArtifact) {

					StringBuffer sb = new StringBuffer();
					if (delta.getFeature().equals(IArtifactFQRenameRequest.FQN_FEATURE)) {
						sb.append("Move/Rename ");
						sb.append(delta.getOldValue());
						sb.append(" to ");
						sb.append(delta.getNewValue());
						return sb.toString();
					} else if (delta.getFeature().equals(IArtifactSetFeatureRequest.EXTENDS_FEATURE)) {
						AbstractArtifact artifact = (AbstractArtifact) delta.getComponent();
						sb.append(artifact.getFullyQualifiedName() + " ");
						sb.append("generalization ");
						sb.append(delta.getOldValue());
						sb.append(" updated to ");
						sb.append(delta.getNewValue());
					} else if ((delta.getFeature().equals(IArtifactSetFeatureRequest.ZEND) || (delta.getFeature()
							.equals(IArtifactSetFeatureRequest.AEND)))) {
						AbstractArtifact artifact = (AbstractArtifact) delta.getComponent();
						sb.append(artifact.getFullyQualifiedName() + " ");
						sb.append(delta.getFeature() + " ");
						sb.append("moved/renamed to ");
						sb.append(delta.getNewValue());
					} else {
						sb.append(delta.toString());
					}

					return sb.toString();

				} else {
					return "NOT AN ARTIFACT!";
				}

			}
			return null;
		}

		private String getAssociationLabel(ModelChangeDelta delta) {

			AbstractArtifact artifact = (AbstractArtifact) delta.getComponent();
			StringBuffer sb = new StringBuffer();
			sb.append(artifact.getFullyQualifiedName() + " ");
			sb.append(delta.getFeature() + " ");
			sb.append("moved/renamed to ");
			sb.append(delta.getNewValue());

			return sb.toString();
		}
	}

	public static final String PAGE_NAME = "PreviewPage";

	private TableViewer tableViewer;

	public PreviewWizardPage() {
		super(PAGE_NAME);
	}

	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		new Label(composite, SWT.LEFT).setText(" Changes to be performed");

		Table table = new Table(composite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ModelChangeDeltaLabelProvider());
		tableViewer.setSorter(new ViewerSorter());

		AbstractModelRefactorWizard wizard = (AbstractModelRefactorWizard) getWizard();
		try {
			tableViewer.setInput(wizard.getCommand().getDeltas());
		} catch (TigerstripeException e) {

			EclipsePlugin.log(e);
		}

		setControl(composite);
	}

}
