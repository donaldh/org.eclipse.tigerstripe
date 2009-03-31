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
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactFQRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IMethodSetRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactComponent;
import org.eclipse.tigerstripe.workbench.internal.core.model.Field;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method.Argument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
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
				if (delta.getComponent() instanceof ArtifactComponent) {

					if (delta.getComponent() instanceof AbstractArtifact) {

						if (delta.getComponent() instanceof IQueryArtifact && delta.getFeature().equals(IArtifactSetFeatureRequest.RETURNED_TYPE)) {

							AbstractArtifact artifact = (AbstractArtifact) delta.getComponent();
							StringBuffer sb = new StringBuffer();
							sb.append(artifact.getName());
							sb.append(" return type moved/renamed from ");
							sb.append(delta.getOldValue());
							sb.append(" to ");
							sb.append(delta.getNewValue());
							return sb.toString();
						}

						StringBuffer sb = new StringBuffer();
						if (delta.getFeature().equals(IArtifactFQRenameRequest.FQN_FEATURE)) {
							sb.append("Move/Rename ");
							sb.append(delta.getOldValue());
							sb.append(" to ");
							sb.append(delta.getNewValue());
						} else if (delta.getFeature().equals(IArtifactSetFeatureRequest.EXTENDS_FEATURE)) {
							AbstractArtifact artifact = (AbstractArtifact) delta.getComponent();
							sb.append(artifact.getFullyQualifiedName() + " ");
							sb.append("generalization ");
							sb.append(delta.getOldValue());
							sb.append(" moved/renamed to ");
							sb.append(delta.getNewValue());
						} else if ((delta.getFeature().equals(IArtifactSetFeatureRequest.ZEND) || (delta.getFeature()
								.equals(IArtifactSetFeatureRequest.AEND)))) {
							AbstractArtifact artifact = (AbstractArtifact) delta.getComponent();
							sb.append(artifact.getFullyQualifiedName() + " ");
							sb.append(delta.getFeature() + " ");
							sb.append("moved/renamed to ");
							sb.append(delta.getNewValue());
						} else if ((delta.getFeature().equals(IArtifactAddFeatureRequest.IMPLEMENTS_FEATURE))) {
							AbstractArtifact artifact = (AbstractArtifact) delta.getComponent();
							sb.append(artifact.getFullyQualifiedName() + " ");
							sb.append("implemented interface ");
							sb.append(delta.getOldValue());
							sb.append(" moved/renamed to ");
							sb.append(delta.getNewValue());
						} else {
							sb.append(delta.toString());
						}

						return sb.toString();

					} else {

						ArtifactComponent component = (ArtifactComponent) delta.getComponent();

						if (component instanceof Field) {

							StringBuffer sb = new StringBuffer();

							IModelComponent mdlComponent = component.getContainingModelComponent();
							IAbstractArtifact artifact = (IAbstractArtifact) mdlComponent;

							sb.append("Attribute ");
							sb.append(artifact.getFullyQualifiedName() + "." + component.getName());
							sb.append(" type moved/renamed from ");
							sb.append(delta.getOldValue());
							sb.append(" to ");
							sb.append(delta.getNewValue());
							return sb.toString();

						}

						if (delta.getFeature().equals(IMethodSetRequest.TYPE_FEATURE)) {

							StringBuffer sb = new StringBuffer();

							IModelComponent mdlComponent = component.getContainingModelComponent();
							IAbstractArtifact artifact = (IAbstractArtifact) mdlComponent;

							sb.append("Method ");
							sb.append(artifact.getFullyQualifiedName() + "." + component.getName());
							sb.append(" return type moved/renamed from ");
							sb.append(delta.getOldValue());
							sb.append(" to ");
							sb.append(delta.getNewValue());
							return sb.toString();
						}
					}
				}

				// Method Argument is a special case
				if (delta.getComponent() instanceof Argument) {

					Argument arg = (Argument) delta.getComponent();
					StringBuffer sb = new StringBuffer();
					sb.append("Method ");
					sb.append(arg.getContainingArtifact().getFullyQualifiedName());
					sb.append(" argument type moved/renamed from ");
					sb.append(delta.getOldValue());
					sb.append(" to ");
					sb.append(delta.getNewValue());
					return sb.toString();
				}

			}
			return null;
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
