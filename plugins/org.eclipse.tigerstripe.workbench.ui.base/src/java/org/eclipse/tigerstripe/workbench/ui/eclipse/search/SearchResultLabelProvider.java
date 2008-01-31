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
package org.eclipse.tigerstripe.workbench.ui.eclipse.search;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILiteral;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.resources.Images;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class SearchResultLabelProvider extends LabelProvider {

	private AbstractArtifactLabelProvider artifactLabelProvider;
	private WorkbenchLabelProvider workbenchLabelProvider;
	private boolean includeParentNameInLabel = false;

	public SearchResultLabelProvider() {
		this(false);
	}

	public SearchResultLabelProvider(boolean includeParentNameInLabel) {
		artifactLabelProvider = new AbstractArtifactLabelProvider();
		workbenchLabelProvider = new WorkbenchLabelProvider();
		this.includeParentNameInLabel = includeParentNameInLabel;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IAbstractArtifact)
			return artifactLabelProvider.getImage(element);
		else if (element instanceof IField)
			return Images.getInstance().get(Images.FIELDPRIVATE_ICON);
		else if (element instanceof IMethod)
			return Images.getInstance().get(Images.METHODPUB_ICON);
		else if (element instanceof ILiteral)
			return Images.getInstance().get(Images.LITERAL_ICON);
		else if (element instanceof IAssociationEnd)
			return Images.getInstance().get(Images.ASSOCIATIONARROW_ICON);
		else {

			if (element instanceof ModuleArtifactManager)
				// This case happens when the search brings up a result from a
				// module
				return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_JAR);

			if (element instanceof ArtifactManager) {
				ArtifactManager mgr = (ArtifactManager) element;
				if (mgr.getTSProject() instanceof PhantomTigerstripeProject)
					return Images.getInstance().get(Images.PROFILE_ICON);
			}

			if (element instanceof IProject) {
				if (EclipsePlugin.getITigerstripeProjectFor((IProject) element) instanceof ITigerstripeProject)
					return Images.getInstance().get(Images.TSPROJECT_FOLDER);
			}
			if (element instanceof IAdaptable) {
				IAdaptable ad = (IAdaptable) element;
				IJavaElement jElm = (IJavaElement) ad
						.getAdapter(IJavaElement.class);
				if (jElm != null)
					return workbenchLabelProvider.getImage(jElm);
			}
			return workbenchLabelProvider.getImage(element);
		}
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IModelComponent) {
			if (includeParentNameInLabel) {
				String label = ((IModelComponent) element).getName();
				if (element instanceof IField) {
					label = label
							+ " - "
							+ ((IField) element).getContainingArtifact()
									.getFullyQualifiedName();
				} else if (element instanceof IMethod) {
					label = label
							+ " - "
							+ ((IMethod) element).getContainingArtifact()
									.getFullyQualifiedName();
				} else if (element instanceof ILiteral) {
					label = label
							+ " - "
							+ ((ILiteral) element).getContainingArtifact()
									.getFullyQualifiedName();
				} else if (element instanceof IAssociationEnd) {
					label = label
							+ " - "
							+ ((IAssociationEnd) element)
									.getContainingArtifact()
									.getFullyQualifiedName();
				}
				return label;
			} else
				return ((IModelComponent) element).getName();
		} else if (element instanceof IJavaElement) {
			IJavaElement res = (IJavaElement) element;
			return res.getElementName();
		} else if (element instanceof IResource) {
			IResource res = (IResource) element;
			return res.getName();
		} else if (element instanceof ModuleArtifactManager) {
			// This case happens when the search brings up a result from a
			// module
			ModuleArtifactManager mMgr = (ModuleArtifactManager) element;
			if (mMgr.getEmbeddedProject() != null)
				return mMgr.getEmbeddedProject().getProjectDetails().getName()
						+ "-"
						+ mMgr.getEmbeddedProject().getProjectDetails()
								.getVersion();
			else
				return "unknown";
		} else if (element instanceof ArtifactManager) {
			// This may happen if the result is coming from the PhantomProject
			// (i.e. from a definition
			// in the active profile)
			ArtifactManager mgr = (ArtifactManager) element;
			if (mgr.getTSProject() instanceof PhantomTigerstripeProject)
				return "Active Profile ("
						+ TigerstripeCore.getWorkbenchProfileSession().getActiveProfile()
								.getName() + ")";
		}

		return workbenchLabelProvider.getText(element);
	}

}
