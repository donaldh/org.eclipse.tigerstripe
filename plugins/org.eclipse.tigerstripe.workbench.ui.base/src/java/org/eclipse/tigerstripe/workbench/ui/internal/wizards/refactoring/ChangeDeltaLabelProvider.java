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

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactAddFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactComponent;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method.Argument;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method.Exception;
import org.eclipse.tigerstripe.workbench.internal.refactor.diagrams.DiagramChangeDelta;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;

public class ChangeDeltaLabelProvider extends AbstractArtifactLabelProvider {

	private static final String DELTA_TO = " to ";
	private static final String DELTA_FROM = " will be moved from ";

	private Image classDiagImg;
	private Image instanceDiagImg;
	private Image methodImg;
	private Image exceptionImg;

	@Override
	public Image getImage(Object element) {

		if (element instanceof ModelChangeDelta) {

			ModelChangeDelta delta = (ModelChangeDelta) element;
			Object component = delta.getComponent();

			if (component instanceof Argument) {

				if (methodImg == null) {
					ImageDescriptor desc = Images
							.getDescriptor(Images.METHOD_ICON);
					methodImg = desc.createImage();
				}
				return methodImg;
			}
			
			if (component instanceof Exception) {

				if (exceptionImg == null) {
					ImageDescriptor desc = Images
							.getDescriptor(Images.EXCEPTION_ICON);
					exceptionImg = desc.createImage();
				}
				return exceptionImg;
			}

			return super.getImage(delta.getComponent());

		} else if (element instanceof DiagramChangeDelta) {

			DiagramChangeDelta delta = (DiagramChangeDelta) element;
			if ((delta.getAffDiagramHandle().getDiagramResource()
					.getFileExtension()).equals("wvd")) {

				ImageDescriptor desc = Images
						.getDescriptor(Images.VISUALEDITOR_ICON);
				classDiagImg = desc.createImage();
				return classDiagImg;

			} else if ((delta.getAffDiagramHandle().getDiagramResource()
					.getFileExtension()).equals("wod")) {

				ImageDescriptor desc = Images
						.getDescriptor(Images.INSTANCEEDITOR_ICON);
				instanceDiagImg = desc.createImage();
				return instanceDiagImg;
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {

		StringBuffer lblText = new StringBuffer();

		if (element instanceof ModelChangeDelta) {

			ModelChangeDelta delta = (ModelChangeDelta) element;
			Object component = delta.getComponent();

			if (component instanceof ArtifactComponent) {

				ArtifactComponent artComponent = (ArtifactComponent) component;
				lblText.append(artComponent.getLabel() + " ");
				lblText.append(artComponent.getName());

				if (delta.getFeature().equals(
						IArtifactSetFeatureRequest.EXTENDS_FEATURE)) {

					lblText.append(" will now extend ");
					lblText.append(delta.getNewValue());
					return lblText.toString();
				}

				if (delta.getFeature().equals(
						IArtifactAddFeatureRequest.IMPLEMENTS_FEATURE)) {

					switch (delta.getType()) {
					case ModelChangeDelta.ADD:
						lblText.append(" will implement ");
						lblText.append(delta.getNewValue());
						break;
					case ModelChangeDelta.REMOVE:
						lblText.append(" will no longer implement ");
						lblText.append(delta.getOldValue());
						break;
					default:
						break;
					}

					return lblText.toString();
				}

				lblText.append(DELTA_FROM);
				lblText.append(delta.getOldValue());
				lblText.append(DELTA_TO);
				lblText.append(delta.getNewValue());

			} else if (component instanceof Argument) {

				// Argument is a special case
				Argument arg = (Argument) component;
				lblText.append("Method argument ");
				lblText.append(arg.getName());
				lblText.append(DELTA_FROM);
				lblText.append(delta.getOldValue());
				lblText.append(DELTA_TO);
				lblText.append(delta.getNewValue());
			} else if (component instanceof Exception) {

				// Exception is a special case
				Exception exp = (Exception) component;
				lblText.append("Method ");
				lblText.append(exp.getContainingMethod().getName());
				lblText.append(" will throw ");
				lblText.append(delta.getNewValue());
			}

			else {
				lblText.append(component.getClass() + " ");
				lblText.append(delta.toString());
			}

			return lblText.toString();
		}

		if (element instanceof DiagramChangeDelta) {

			DiagramChangeDelta delta = (DiagramChangeDelta) element;

			lblText.append("Diagram ");
			lblText.append(delta.getAffDiagramHandle().getModelResource()
					.getName());
			lblText.append(DELTA_FROM);
			lblText.append(delta.getAffDiagramHandle().getModelResource()
					.getFullPath());
			lblText.append(DELTA_TO);
			lblText.append(delta.getDestinationPath());
			lblText.append(File.separator);
			lblText.append(delta.getAffDiagramHandle().getModelResource()
					.getName());

			return lblText.toString();
		}

		return null;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (classDiagImg != null) {
			classDiagImg.dispose();
			classDiagImg = null;
		}
		if (instanceDiagImg != null) {
			instanceDiagImg.dispose();
			instanceDiagImg = null;
		}
		if (methodImg != null) {
			methodImg.dispose();
			methodImg = null;
		}
		if (exceptionImg != null) {
			exceptionImg.dispose();
			exceptionImg = null;
		}
	}

}
