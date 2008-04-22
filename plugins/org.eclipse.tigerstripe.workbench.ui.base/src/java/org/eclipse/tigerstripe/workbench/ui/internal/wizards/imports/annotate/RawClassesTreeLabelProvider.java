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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.Annotable;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate.RawClassesTreeContentProvider.AssociationEndNode;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate.RawClassesTreeContentProvider.AssociationNode;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate.RawClassesTreeContentProvider.AttributeNode;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate.RawClassesTreeContentProvider.ClassNode;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate.RawClassesTreeContentProvider.ConstantNode;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate.RawClassesTreeContentProvider.DependencyEndNode;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate.RawClassesTreeContentProvider.DependencyNode;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate.RawClassesTreeContentProvider.Node;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate.RawClassesTreeContentProvider.OperationNode;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.internal.WorkbenchImages;

public class RawClassesTreeLabelProvider extends LabelProvider {

	private boolean showDelta = false;

	public void setDelta(boolean showDelta) {
		this.showDelta = showDelta;
	}

	@Override
	public String getText(Object element) {
		String text = ((Node) element).getName();

		if (showDelta) {
			switch (((Node) element).getDelta()) {
			case Annotable.DELTA_ADDED:
				text = "[ADDED] " + text;
				break;
			case Annotable.DELTA_CHANGED:
				text = "[CHANGED] " + text;
				break;
			case Annotable.DELTA_REMOVED:
				text = "[REMOVED] " + text;
				break;
			case Annotable.DELTA_UNKNOWN:
				text = "[UNKNOWN] " + text;
				break;
			}
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {
		Node node = (Node) element;

		if (node instanceof ClassNode || node instanceof AssociationNode
				|| node instanceof DependencyNode) {

			AnnotableElement elm = null;
			if (node instanceof ClassNode) {
				ClassNode cNode = (ClassNode) node;
				elm = cNode.getAnnotableElement();
			} else if (node instanceof AssociationNode) {
				AssociationNode aNode = (AssociationNode) node;
				elm = aNode.getAnnotableElement();
			} else if (node instanceof DependencyNode) {
				DependencyNode aNode = (DependencyNode) node;
				elm = aNode.getAnnotableElement();
			}

			if (AnnotableElement.AS_ENTITY.equals(elm.getAnnotationType()))
				return Images.get(Images.ENTITY_ICON);
			else if (AnnotableElement.AS_DATATYPE.equals(elm
					.getAnnotationType()))
				return Images.get(Images.DATATYPE_ICON);
			else if (AnnotableElement.AS_ENUMERATION.equals(elm
					.getAnnotationType()))
				return Images.get(Images.ENUM_ICON);
			else if (AnnotableElement.AS_ASSOCIATION.equals(elm
					.getAnnotationType()))
				return Images.get(Images.ASSOCIATION_ICON);
			else if (AnnotableElement.AS_ASSOCIATIONCLASS.equals(elm
					.getAnnotationType()))
				return Images.get(Images.ASSOCIATIONCLASS_ICON);
			else if (AnnotableElement.AS_DEPENDENCY.equals(elm
					.getAnnotationType()))
				return Images.get(Images.DEPENDENCY_ICON);
			else if (AnnotableElement.AS_SESSIONFACADE.equals(elm
					.getAnnotationType()))
				return Images.get(Images.SESSION_ICON);
			else if (AnnotableElement.AS_NAMEDQUERY.equals(elm
					.getAnnotationType()))
				return Images.get(Images.QUERY_ICON);
			else if (AnnotableElement.AS_NOTIFICATION.equals(elm
					.getAnnotationType()))
				return Images.get(Images.NOTIFICATION_ICON);
			else if (AnnotableElement.AS_EXCEPTION.equals(elm
					.getAnnotationType()))
				return Images.get(Images.EXCEPTION_ICON);
			else if (AnnotableElement.AS_UPDATEPROC.equals(elm
					.getAnnotationType()))
				return Images.get(Images.UPDATEPROC_ICON);
			else
				return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_HELP);
		} else if (node instanceof AttributeNode) {
			AttributeNode attrNode = (AttributeNode) node;
			if (attrNode.getAnnotableElementAttribute().shouldIgnore())
				return WorkbenchImages
						.getImage(ISharedImages.IMG_TOOL_DELETE_DISABLED);
			else
				return JavaPluginImages.get(JavaPluginImages.IMG_FIELD_DEFAULT);
		} else if (node instanceof OperationNode) {
			OperationNode opNode = (OperationNode) node;
			if (opNode.getAnnotableElementOperation().shouldIgnore())
				return WorkbenchImages
						.getImage(ISharedImages.IMG_TOOL_DELETE_DISABLED);
			else
				return JavaPluginImages.get(JavaPluginImages.IMG_MISC_DEFAULT);
		} else if (node instanceof ConstantNode) {
			ConstantNode cstNode = (ConstantNode) node;
			if (cstNode.getAnnotableElementConstant().shouldIgnore())
				return WorkbenchImages
						.getImage(ISharedImages.IMG_TOOL_DELETE_DISABLED);
			else
				return JavaPluginImages.get(JavaPluginImages.IMG_MISC_DEFAULT);
		} else if (node instanceof AssociationEndNode) {
			AssociationEndNode endNode = (AssociationEndNode) node;
			if (endNode.getAnnotableAssociationEnd().shouldIgnore())
				return WorkbenchImages
						.getImage(ISharedImages.IMG_TOOL_DELETE_DISABLED);
			else
				return Images.get(Images.ASSOCIATION_ICON);
		} else if (node instanceof DependencyEndNode)
			return Images.get(Images.DEPENDENCY_ICON);

		return null;
	}

}
