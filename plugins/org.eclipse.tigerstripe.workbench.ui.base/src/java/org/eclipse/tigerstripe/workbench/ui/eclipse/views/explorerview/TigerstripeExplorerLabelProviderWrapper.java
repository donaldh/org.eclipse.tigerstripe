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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerLabelProvider;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerProblemsDecorator;
import org.eclipse.jdt.internal.ui.viewsupport.JavaUILabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILabel;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.natures.TigerstripePluginProjectNature;
import org.eclipse.tigerstripe.workbench.ui.eclipse.natures.TigerstripeProjectNature;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.ColorUtils;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.resources.Images;

public class TigerstripeExplorerLabelProviderWrapper extends
		JavaUILabelProvider {

	private PackageExplorerLabelProvider pkgExplorerLabelProvider;
	private AbstractArtifactLabelProvider artifactLabelProvider = new AbstractArtifactLabelProvider();

	private PackageExplorerProblemsDecorator fProblemDecorator;

	public TigerstripeExplorerLabelProviderWrapper(long textFlags,
			int imageFlags,
			PackageExplorerLabelProvider pkgExplorerLabelProvider) {
		super(textFlags, imageFlags);
		this.pkgExplorerLabelProvider = pkgExplorerLabelProvider;
		fProblemDecorator = new PackageExplorerProblemsDecorator();
		addLabelDecorator(fProblemDecorator);

	}

	@Override
	public String getText(Object element) {
		if (element instanceof IJavaProject) {
			IJavaProject jProject = (IJavaProject) element;
			IProject iProject = jProject.getProject();
			try {
				if (TigerstripeProjectNature.hasNature(iProject)) {
					IAbstractTigerstripeProject aProject = EclipsePlugin
							.getITigerstripeProjectFor(iProject);
					if (aProject instanceof ITigerstripeProject) {
						ITigerstripeProject tsProject = (ITigerstripeProject) aProject;
						try {
							if (tsProject.getActiveFacet() != null
									&& tsProject.getActiveFacet().canResolve()) {
								String activeName = jProject.getElementName()
										+ " {"
										+ tsProject.getActiveFacet().resolve()
												.getName() + "}";
								return decorateText(activeName, element);
							}
						} catch (TigerstripeException e) {
							// Upon import the tigerstripe.xml may not be
							// there yet, so all we want is to display
							// the project name. There won't be any active
							// facet at that stage anyway
							return pkgExplorerLabelProvider.getText(element);
						}
					}
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
			return pkgExplorerLabelProvider.getText(element);
		} else if (element instanceof IJavaElement) {
			IJavaElement jElem = (IJavaElement) element;

			if (jElem.getElementType() == IJavaElement.COMPILATION_UNIT
					|| jElem.getElementType() == IJavaElement.CLASS_FILE) {
				IAbstractArtifact artifact = TSExplorerUtils
						.getArtifactFor(element);
				if (artifact != null)
					return decorateText(artifact.getName(), element);
				else
					return pkgExplorerLabelProvider.getText(element);
			}
		} else if (element instanceof IField) {
			IField field = (IField) element;
			return decorateText(field.getLabelString(), element);
		} else if (element instanceof IMethod) {
			IMethod method = (IMethod) element;
			return decorateText(method.getLabelString(), element);
		} else if (element instanceof ILabel) {
			ILabel label = (ILabel) element;
			return decorateText(label.getLabelString(), element);
		} else if (element instanceof IRelationshipEnd) {
			IRelationshipEnd end = (IRelationshipEnd) element;
			// String endName =
			// end.getNameForType(end.getType().getFullyQualifiedName());
			String endName = end.getName();
			return (endName.equals("")) ? decorateText(end.getType().getName(),
					element) : decorateText(endName + "::"
					+ end.getType().getName(), element);
		} else if (element instanceof AbstractLogicalExplorerNode) {
			AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) element;
			return decorateText(node.getText(), element);
		}
		return pkgExplorerLabelProvider.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IJavaProject) {
			IJavaProject jProject = (IJavaProject) element;
			IProject iProject = jProject.getProject();

			try {
				if (TigerstripeProjectNature.hasNature(iProject)
						&& iProject.isOpen())
					return decorateImage(TigerstripePluginImages
							.get(TigerstripePluginImages.TSPROJECT_FOLDER),
							element);
				else if (TigerstripePluginProjectNature.hasNature(iProject)
						&& iProject.isOpen())
					return decorateImage(TigerstripePluginImages
							.get(TigerstripePluginImages.PLUGINPROJECT_FOLDER),
							element);
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		} else if (element instanceof IJavaElement) {
			IJavaElement jElem = (IJavaElement) element;

			// Tigerstripe projects need special handling to display icons
			// properly
			IProject iProject = jElem.getJavaProject().getProject();
			try {
				if (TigerstripeProjectNature.hasNature(iProject)) {
					if (jElem.getElementType() == IJavaElement.COMPILATION_UNIT
							|| jElem.getElementType() == IJavaElement.CLASS_FILE) {
						IAbstractArtifact artifact = TSExplorerUtils
								.getArtifactFor(element);
						if (artifact != null)
							return decorateImage(artifactLabelProvider
									.getImage(artifact), element);
						else
							return pkgExplorerLabelProvider.getImage(element);
					}
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		} else if (element instanceof IField)
			return decorateImage(Images.getInstance().get(
					Images.FIELDPRIVATE_ICON), element);
		else if (element instanceof IMethod)
			return decorateImage(Images.getInstance()
					.get(Images.METHODPUB_ICON), element);
		else if (element instanceof ILabel)
			return decorateImage(Images.getInstance().get(Images.LITERAL_ICON),
					element);
		else if (element instanceof IAssociationEnd)
			return decorateImage(Images.getInstance().get(
					Images.ASSOCIATIONARROW_ICON), element);
		else if (element instanceof IRelationshipEnd)
			return decorateImage(Images.getInstance().get(
					Images.DEPENDENCYARROW_ICON), element);
		else if (element instanceof AbstractLogicalExplorerNode) {
			AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) element;
			if (node.getImage() != null)
				return decorateImage(node.getImage(), node.getKeyResource());
			else
				return pkgExplorerLabelProvider.getImage(node.getKeyResource());
		}
		return pkgExplorerLabelProvider.getImage(element);
	}

	@Override
	public Color getForeground(Object element) {
		if (element instanceof IJavaProject) {
			IJavaProject jProject = (IJavaProject) element;
			IProject iProject = jProject.getProject();
			try {
				if (TigerstripeProjectNature.hasNature(iProject)) {
					IAbstractTigerstripeProject aProject = EclipsePlugin
							.getITigerstripeProjectFor(iProject);
					if (aProject instanceof ITigerstripeProject) {
						ITigerstripeProject tsProject = (ITigerstripeProject) aProject;
						try {
							if (tsProject.getActiveFacet() != null)
								return ColorUtils.TS_ORANGE;
						} catch (TigerstripeException e) {
							// upon import the project cannot be resolved
							// yet, and the facet cannot be determined.
							return ColorUtils.BLACK;
						}
					}
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
			return ColorUtils.BLACK;
		} else if (element instanceof IJavaElement) {
			IJavaElement jElem = (IJavaElement) element;

			// Tigerstripe projects need special handling to display icons
			// properly
			IProject iProject = jElem.getJavaProject().getProject();
			try {
				if (TigerstripeProjectNature.hasNature(iProject)) {
					if (jElem.getElementType() == IJavaElement.COMPILATION_UNIT
							|| jElem.getElementType() == IJavaElement.CLASS_FILE) {
						IAbstractArtifact artifact = TSExplorerUtils
								.getArtifactFor(element);
						if (artifact != null)
							return artifactLabelProvider
									.getForeground(artifact);
						else
							return pkgExplorerLabelProvider
									.getForeground(element);
					}
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		} else if (element instanceof IField)
			return artifactLabelProvider.getForeground(element);
		else if (element instanceof IMethod)
			return artifactLabelProvider.getForeground(element);
		else if (element instanceof ILabel)
			return artifactLabelProvider.getForeground(element);
		else if (element instanceof IAssociationEnd)
			return artifactLabelProvider.getForeground(element);
		else if (element instanceof IRelationshipEnd)
			return artifactLabelProvider.getForeground(element);
		else if (element instanceof IResource
				&& IContractSegment.FILE_EXTENSION.equals(((IResource) element)
						.getFileExtension())) {
			IResource res = (IResource) element;
			IProject project = res.getProject();
			IAbstractTigerstripeProject aProject = EclipsePlugin
					.getITigerstripeProjectFor(project);
			if (aProject instanceof ITigerstripeProject) {
				ITigerstripeProject tsProject = (ITigerstripeProject) aProject;
				try {
					if (tsProject.getActiveFacet() != null) {
						IFacetReference ref = tsProject.getActiveFacet();
						if (res.getLocationURI().equals(ref.getURI()))
							return ColorUtils.TS_ORANGE;
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
		return pkgExplorerLabelProvider.getForeground(element);
	}

	public void setIsFlatLayout(boolean state) {
		pkgExplorerLabelProvider.setIsFlatLayout(state);
	}
}
