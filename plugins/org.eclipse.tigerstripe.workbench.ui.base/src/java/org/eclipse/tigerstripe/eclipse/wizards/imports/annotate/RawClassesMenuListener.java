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
package org.eclipse.tigerstripe.eclipse.wizards.imports.annotate;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.eclipse.wizards.imports.annotate.RawClassesTreeContentProvider.AssociationNode;
import org.eclipse.tigerstripe.eclipse.wizards.imports.annotate.RawClassesTreeContentProvider.AttributeNode;
import org.eclipse.tigerstripe.eclipse.wizards.imports.annotate.RawClassesTreeContentProvider.ClassNode;
import org.eclipse.tigerstripe.eclipse.wizards.imports.annotate.RawClassesTreeContentProvider.ConstantNode;
import org.eclipse.tigerstripe.eclipse.wizards.imports.annotate.RawClassesTreeContentProvider.DependencyNode;
import org.eclipse.tigerstripe.eclipse.wizards.imports.annotate.RawClassesTreeContentProvider.OperationNode;

public class RawClassesMenuListener implements IMenuListener {

	private TreeViewer viewer;

	private class DontAnnotateAction extends Action {
		@Override
		public String getText() {
			return "Ignore";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_UNKNOWN);
				} else if (selectedObj instanceof AssociationNode) {
					AssociationNode node = (AssociationNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_UNKNOWN);
				} else if (selectedObj instanceof DependencyNode) {
					DependencyNode node = (DependencyNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_UNKNOWN);
				} else if (selectedObj instanceof AttributeNode) {
					AttributeNode node = (AttributeNode) selectedObj;
					node.getAnnotableElementAttribute().setShouldIgnore(true);
				} else if (selectedObj instanceof OperationNode) {
					OperationNode node = (OperationNode) selectedObj;
					node.getAnnotableElementOperation().setShouldIgnore(true);
				} else if (selectedObj instanceof ConstantNode) {
					ConstantNode node = (ConstantNode) selectedObj;
					node.getAnnotableElementConstant().setShouldIgnore(true);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AnnotateAction extends Action {

		@Override
		public String getText() {
			return "Import";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof AttributeNode) {
					AttributeNode node = (AttributeNode) selectedObj;
					node.getAnnotableElementAttribute().setShouldIgnore(false);
				} else if (selectedObj instanceof OperationNode) {
					OperationNode node = (OperationNode) selectedObj;
					node.getAnnotableElementOperation().setShouldIgnore(false);
				} else if (selectedObj instanceof ConstantNode) {
					ConstantNode node = (ConstantNode) selectedObj;
					node.getAnnotableElementConstant().setShouldIgnore(false);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsEntityAction extends Action {

		@Override
		public int getStyle() {
			// TODO Auto-generated method stub
			return super.getStyle();
		}

		@Override
		public String getText() {
			return "as Managed Entity";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_ENTITY);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsEnumerationAction extends Action {
		@Override
		public String getText() {
			return "as Enumeration";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_ENUMERATION);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsDatatypeAction extends Action {
		@Override
		public String getText() {
			return "as Datatype";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_DATATYPE);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsSessionAction extends Action {
		@Override
		public String getText() {
			return "as Session Facade";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_SESSIONFACADE);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsQueryAction extends Action {
		@Override
		public String getText() {
			return "as Named Query";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_NAMEDQUERY);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsNotificationAction extends Action {
		@Override
		public String getText() {
			return "as Notification";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_NOTIFICATION);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsUpdateProcAction extends Action {
		@Override
		public String getText() {
			return "as Update Procedure";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_UPDATEPROC);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsExceptionAction extends Action {
		@Override
		public String getText() {
			return "as Exception";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_EXCEPTION);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsDependencyAction extends Action {
		@Override
		public String getText() {
			return "as Dependency";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof DependencyNode) {
					DependencyNode node = (DependencyNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_DEPENDENCY);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsAssociationAction extends Action {
		@Override
		public String getText() {
			return "as Association";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof AssociationNode) {
					AssociationNode node = (AssociationNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_ASSOCIATION);
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsAssociationClassAction extends Action {
		@Override
		public String getText() {
			return "as AssociationClass";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof AssociationNode) {
					AssociationNode node = (AssociationNode) selectedObj;
					node.getAnnotableElement().setAnnotationType(
							AnnotableElement.AS_ASSOCIATIONCLASS);
				}
			}
			viewer.refresh(true);
		}
	}

	public RawClassesMenuListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	public void menuAboutToShow(IMenuManager manager) {

		IStructuredSelection ssel = (IStructuredSelection) viewer
				.getSelection();
		boolean classNodeSelected = false;
		boolean attributeNodeSelected = false;
		boolean constantNodeSelected = false;
		boolean operationNodeSelected = false;
		boolean associationSelected = false;
		boolean dependencySelected = false;

		for (Object obj : ssel.toArray()) {
			classNodeSelected = obj instanceof ClassNode;
			associationSelected = obj instanceof AssociationNode;
			dependencySelected = obj instanceof DependencyNode;
			attributeNodeSelected = obj instanceof AttributeNode;
			constantNodeSelected = obj instanceof ConstantNode;
			operationNodeSelected = obj instanceof OperationNode;
		}

		if (classNodeSelected) {
			manager.add(new DontAnnotateAction());
			manager.add(new AsEntityAction());
			manager.add(new AsDatatypeAction());
			manager.add(new AsEnumerationAction());
			manager.add(new AsExceptionAction());
			manager.add(new AsQueryAction());
			manager.add(new AsNotificationAction());
			manager.add(new AsUpdateProcAction());
			manager.add(new AsSessionAction());
		} else if (dependencySelected) {
			manager.add(new DontAnnotateAction());
			manager.add(new AsDependencyAction());
		} else if (associationSelected) {
			manager.add(new DontAnnotateAction());
			manager.add(new AsAssociationAction());
			manager.add(new AsAssociationClassAction());
		} else if (attributeNodeSelected || constantNodeSelected
				|| operationNodeSelected) {
			manager.add(new DontAnnotateAction());
			manager.add(new AnnotateAction());
		}

	}

}
