/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.AssociationNode;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.ClassNode;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.DependencyNode;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.Node;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.PackageNode;


public class UmlClassesTreeLabelProvider extends LabelProvider {
	
	private Image errorImage = JavaPluginImages.get(JavaPluginImages.IMG_OBJS_ERROR);
	
	private List typeList;
	
	public void setSupportedArtifacts(CoreArtifactSettingsProperty property){
		typeList = Arrays.asList(property.getEnabledArtifactTypes());
	}

	public String getText(Object element) {
		String text = ((Node) element).getName();
		return text;
	}

	public Image getImage(Object element) {
		Node node = (Node) element;

		if (node instanceof ClassNode) {
			if (((ClassNode) node).getMappingType().equals(IManagedEntityArtifact.class.getName())){
				if (typeList.contains(IManagedEntityArtifact.class.getName())){
					return Images.get(Images.ENTITY_ICON);
			} else {
				return errorImage;
			}
			} 
			
			if (((ClassNode) node).getMappingType().equals(IDatatypeArtifact.class.getName())){
				if (typeList.contains(IDatatypeArtifact.class.getName())){
				return Images.get(Images.DATATYPE_ICON);
			} else {
				return errorImage;
			}
			}
			if (((ClassNode) node).getMappingType().equals(IEnumArtifact.class.getName())){
				if (typeList.contains(IEnumArtifact.class.getName())){
					return Images.get(Images.ENUM_ICON);
				} else {
					return errorImage;
				}

			}
			if (((ClassNode) node).getMappingType().equals(ISessionArtifact.class.getName())){
				if (typeList.contains(ISessionArtifact.class.getName())){
				return Images.get(Images.SESSION_ICON);
			} else {
				return errorImage;
			}
			}
			if (((ClassNode) node).getMappingType().equals(IUpdateProcedureArtifact.class.getName())){
				if (typeList.contains(IUpdateProcedureArtifact.class.getName())){
				return Images.get(Images.UPDATEPROC_ICON);
			} else {
				return errorImage;
			}
			}
			
			if (((ClassNode) node).getMappingType().equals(IQueryArtifact.class.getName())){
				if (typeList.contains(IQueryArtifact.class.getName())){
				return Images.get(Images.QUERY_ICON);
			} else {
				return errorImage;
			}
			}
			if (((ClassNode) node).getMappingType().equals(IExceptionArtifact.class.getName())){
				if (typeList.contains(IExceptionArtifact.class.getName())){
				return Images.get(Images.EXCEPTION_ICON);
			} else {
				return errorImage;
			}
			}
			if (((ClassNode) node).getMappingType().equals(IEventArtifact.class.getName())){
				if (typeList.contains(IEventArtifact.class.getName())){
				return Images.get(Images.NOTIFICATION_ICON);
			} else {
				return errorImage;
			}
			}
		} else if (node instanceof AssociationNode){
			if (((AssociationNode) node).getMappingType().equals(IAssociationClassArtifact.class.getName())){
				if (typeList.contains(IAssociationClassArtifact.class.getName())){
				return Images.get(Images.ASSOCIATIONCLASS_ICON);
			} else {
				return errorImage;
			}
			} 
			if (((AssociationNode) node).getMappingType().equals(IAssociationArtifact.class.getName())){
				if (typeList.contains(IAssociationArtifact.class.getName())){
				return Images.get(Images.ASSOCIATION_ICON);
			} else {
				return errorImage;
			}
			} 
			
		} else if (node instanceof DependencyNode){
			if (((DependencyNode) node).getMappingType().equals(IDependencyArtifact.class.getName())){
				if (typeList.contains(IDependencyArtifact.class.getName())){
				return Images.get(Images.DEPENDENCY_ICON);
			} else {
				return errorImage;
			}
			}
		} else if (node instanceof PackageNode){
				return Images.get(Images.PACKAGE_ICON);

		}
		return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_HELP);
	}
}
