/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.viewsupport.ColoringLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.IElementWrapper;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeM0GeneratorNature;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripePluginProjectNature;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeProjectNature;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.TigerstripeUILabels;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;

@SuppressWarnings("restriction")
public class TigerstripeExplorerLabelProvider extends
		JavaNavigatorLabelProvider {

	private final AbstractArtifactLabelProvider artifactLabelProvider = new AbstractArtifactLabelProvider();
	private final IJavaModel javaModel;
	
	public TigerstripeExplorerLabelProvider() {
		javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
	}
	
	@Override
	public StyledString getStyledText(Object element) {
		StyledString string = null;
		if (element instanceof IElementWrapper) {
			Object el = ((IElementWrapper) element).getElement();
			if (!(el instanceof IModelComponent) && !(el instanceof IPackageFragment)) {
				IAdapterManager manager = Platform.getAdapterManager();
				Object adapted = manager.getAdapter(element,
						IModelComponent.class);
				if (adapted != null) {
					el = adapted;
				}
			}
			return getStyledText(el);
		} else if (element instanceof IProject) {
			IAbstractTigerstripeProject tsProj = (IAbstractTigerstripeProject) toJavaProject(
					(IProject) element).getAdapter(
					IAbstractTigerstripeProject.class);
			string = TigerstripeUILabels.getStyledString(tsProj,
					TigerstripeUILabels.COLORIZE);
		} else if (element instanceof IJavaElement) {
			IJavaElement jElem = (IJavaElement) element;
			if (jElem.getElementType() == IJavaElement.COMPILATION_UNIT
					|| jElem.getElementType() == IJavaElement.CLASS_FILE) {
				IAbstractArtifact artifact = (IAbstractArtifact) jElem
						.getAdapter(IAbstractArtifact.class);
				if (artifact != null)
					string = TigerstripeUILabels.getStyledString(artifact,
							TigerstripeUILabels.COLORIZE);
				else {
					// Jar file entries (Modules content) need to be addressed
					// differently
					if (jElem instanceof IClassFile) {
						IClassFile cf = (IClassFile) jElem;
						string = new StyledString(cf.getElementName()
								.replaceFirst("\\.class", ""));
					}
				}
			}
		} else if (element instanceof IModelComponent) {
			string = TigerstripeUILabels.getStyledString(element,
					TigerstripeUILabels.COLORIZE);
		} else if (element instanceof IRelationshipEnd) {
			string = TigerstripeUILabels.getStyledString(element,
					TigerstripeUILabels.COLORIZE);
		} else if (element instanceof AbstractLogicalExplorerNode) {
			string = TigerstripeUILabels.getStyledString(element,
					TigerstripeUILabels.COLORIZE);
		} else if (element instanceof RelationshipAnchor) {
			string = TigerstripeUILabels.getStyledString(element,
					TigerstripeUILabels.COLORIZE);
		}

		if (string == null || string.length() == 0)
			return super.getStyledText(element);

		String decorated = decorateText(string.getString(), element);
		if (decorated != null) {
			return ColoringLabelProvider.styleDecoratedString(decorated,
					StyledString.DECORATIONS_STYLER, string);
		}
		return string;
	}

	private IJavaProject toJavaProject(IProject project) {
		return javaModel.getJavaProject(project.getName());
	}

	@Override
	public String getText(Object element) {
		return getStyledText(element).getString();
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IElementWrapper) {
			return getImage(((IElementWrapper) element).getElement());
		} else if (element instanceof IProject) {
			IProject iProject = (IProject) element;
			try {
				if (TigerstripeProjectNature.hasNature(iProject)
						&& iProject.isOpen())
					return decorateImage(Images.get(Images.TSPROJECT_FOLDER),
							element);
				else if (TigerstripePluginProjectNature.hasNature(iProject)
						&& iProject.isOpen())
					return decorateImage(
							Images.get(Images.PLUGINPROJECT_FOLDER), element);
				else if (TigerstripeM0GeneratorNature.hasNature(iProject)
						&& iProject.isOpen())
					return decorateImage(
							Images.get(Images.PLUGINPROJECT_FOLDER), element);
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
							|| jElem.getElementType() == IJavaElement.CLASS_FILE
							|| jElem.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
						IAbstractArtifact artifact = TSExplorerUtils
								.getArtifactFor(element);
						if (artifact != null)
							return decorateImage(
									artifactLabelProvider.getImage(artifact),
									element);
						else
							return super.getImage(element);
					}
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		} else if (element instanceof IField)
			return decorateImage(artifactLabelProvider.getImage(element), element);
		else if (element instanceof IMethod)
			return decorateImage(artifactLabelProvider.getImage(element), element);
		else if (element instanceof ILiteral)
			return decorateImage(artifactLabelProvider.getImage(element), element);
		else if (element instanceof IAssociationEnd)
			return decorateImage(Images.get(Images.ASSOCIATION_ICON), element);
		else if (element instanceof IRelationshipEnd)
			return decorateImage(Images.get(Images.DEPENDENCY_ICON), element);
		else if (element instanceof RelationshipAnchor)
			return decorateImage(Images.get(Images.ASSOCIATION_ICON), element);
		else if (element instanceof AbstractLogicalExplorerNode) {
			AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) element;
			if (node.getImage() != null)
				return decorateImage(node.getImage(), node.getKeyResource());
			else
				return super.getImage(node.getKeyResource());
		} else if (element instanceof IAbstractArtifact) {
			return decorateImage(artifactLabelProvider.getImage(element),
					element);
		}
		return super.getImage(element);
	}

	private Image decorateImage(Image image, Object element) {
		return getDelegeteLabelProvider().decorateImage(image, element);
	}

	private String decorateText(String string, Object element) {
		return getDelegeteLabelProvider().decorateText(string, element);
	}
}
