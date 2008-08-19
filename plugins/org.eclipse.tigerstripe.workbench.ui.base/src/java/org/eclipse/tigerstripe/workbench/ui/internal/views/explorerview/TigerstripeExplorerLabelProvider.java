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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerContentProvider;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerLabelProvider;
import org.eclipse.jdt.internal.ui.viewsupport.ColoringLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
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
		PackageExplorerLabelProvider {

	private AbstractArtifactLabelProvider artifactLabelProvider = new AbstractArtifactLabelProvider();

	public TigerstripeExplorerLabelProvider(PackageExplorerContentProvider cp) {
		super(cp);
	}

	@Override
	public StyledString getStyledText(Object element) {
		StyledString string = null;
		if (element instanceof IJavaProject) {
			IJavaProject jProject = (IJavaProject) element;
			IAbstractTigerstripeProject tsProj = (IAbstractTigerstripeProject) jProject
					.getAdapter(IAbstractTigerstripeProject.class);
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
		}

		if (string == null || string.length() == 0)
			return super.getStyledText(element);

		String decorated = decorateText(string.getString(), element);
		if (decorated != null) {
			return ColoringLabelProvider.decorateStyledString(string,
					decorated, StyledString.DECORATIONS_STYLER);
		}
		return string;
	}

	@Override
	public String getText(Object element) {
		return getStyledText(element).getString();
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IJavaProject) {
			IJavaProject jProject = (IJavaProject) element;
			IProject iProject = jProject.getProject();

			try {
				if (TigerstripeProjectNature.hasNature(iProject)
						&& iProject.isOpen())
					return decorateImage(Images.get(Images.TSPROJECT_FOLDER),
							element);
				else if (TigerstripePluginProjectNature.hasNature(iProject)
						&& iProject.isOpen())
					return decorateImage(Images
							.get(Images.PLUGINPROJECT_FOLDER), element);
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
							|| jElem.getElementType() == IJavaElement.CLASS_FILE || jElem.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
						IAbstractArtifact artifact = TSExplorerUtils
								.getArtifactFor(element);
						if (artifact != null)
							return decorateImage(artifactLabelProvider
									.getImage(artifact), element);
						else
							return super.getImage(element);
					}
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		} else if (element instanceof IField)
			return decorateImage(Images.get(Images.FIELD_ICON), element);
		else if (element instanceof IMethod)
			return decorateImage(Images.get(Images.METHOD_ICON), element);
		else if (element instanceof ILiteral)
			return decorateImage(Images.get(Images.LITERAL_ICON), element);
		else if (element instanceof IAssociationEnd)
			return decorateImage(Images.get(Images.ASSOCIATION_ICON), element);
		else if (element instanceof IRelationshipEnd)
			return decorateImage(Images.get(Images.DEPENDENCY_ICON), element);
		else if (element instanceof AbstractLogicalExplorerNode) {
			AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) element;
			if (node.getImage() != null)
				return decorateImage(node.getImage(), node.getKeyResource());
			else
				return super.getImage(node.getKeyResource());
		}
		return super.getImage(element);
	}

}
