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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IField;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.NamedElement;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

public class SortedClassInstanceVariableCompartmentEditPart extends
		ListCompartmentEditPart {

	ClassInstance instance;
	IAbstractArtifact artifact;

	public SortedClassInstanceVariableCompartmentEditPart(EObject model) {
		super(model);
		if (model instanceof NodeImpl) {
			// should be the Class Instance node...
			NodeImpl container = (NodeImpl) model.eContainer();
			Object containingElement = container.getElement();
			if (!(containingElement instanceof ClassInstance))
				throw new ClassCastException(
						"only NodeImpl EObjects contained within a ClassInstance be passed into this constructor");
			// if got to here, know we are dealing with a class instance as the
			// containing EObject
			instance = (ClassInstance) containingElement;
		} else
			throw new ClassCastException(
					"only NodeImpl EObjects can be passed into this constructor");
	}

	@Override
	protected boolean hasModelChildrenChanged(Notification evt) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected List getSortedChildren() {
		String fullyQualifiedName = instance.getFullyQualifiedName();
		InstanceMapEditPart mapEditPart = (InstanceMapEditPart) this
				.getParent().getParent();
		InstanceMap map = (InstanceMap) ((View) mapEditPart.getModel())
				.getElement();
		DiagramGraphicalViewer viewer = (DiagramGraphicalViewer) mapEditPart
				.getViewer();
		DiagramEditDomain domain = (DiagramEditDomain) viewer.getEditDomain();

		// NOTE: to avoid problems during ClosedDiagram refreshing on
		// removedArtifacts
		// and renamedArtifacts (where the original artifact couldn't be found
		// anymore)
		// if the artifact cannot be found, we don't both with sorting fields.
		IAbstractTigerstripeProject aProject = map
				.getCorrespondingITigerstripeProject();
		if (!(aProject instanceof ITigerstripeProject))
			throw new RuntimeException("non-Tigerstripe Project found");
		ITigerstripeProject project = (ITigerstripeProject) aProject;
		try {
			IArtifactManagerSession session = project
					.getArtifactManagerSession();
			artifact = session
					.getArtifactByFullyQualifiedName(fullyQualifiedName);
		} catch (TigerstripeException e) {
			return super.getSortedChildren();
		}
		if (artifact == null)
			return super.getSortedChildren();
		List children = super.getSortedChildren();
		if (children.size() <= 0)
			return children;
		IField[] fields = InstanceDiagramUtils.getInstanceVariables(artifact);
		List<String> fieldNames = new ArrayList<String>();
		for (IField field : fields) {
			fieldNames.add(field.getName());
		}
		List sortedChildren = sortNodesByName(children, fieldNames);
		return sortedChildren;
	}

	private List sortNodesByName(List elements, List<String> fieldNames) {
		if (elements.size() == 0)
			return Collections.EMPTY_LIST;
		List<SortedNodeListElement> sortList = new ArrayList<SortedNodeListElement>();
		for (Object element : elements)
			sortList.add(new SortedNodeListElement((NodeImpl) element,
					fieldNames));
		Collections.sort(sortList);
		List returnList = new ArrayList();
		for (SortedNodeListElement element : sortList)
			returnList.add(element.getNode());
		return returnList;
	}

	private class SortedNodeListElement implements Comparable {
		// a couple of instance variables used in comparison of nodes by name
		private NodeImpl node;
		private String name;
		private int nameIndex;
		private List<String> fieldNames;

		// construct a new (sortable) element to use in a list
		public SortedNodeListElement(NodeImpl node, List<String> fieldNames) {
			this.node = node;
			this.fieldNames = fieldNames;
			EObject eObj = node.getElement();
			if (eObj instanceof NamedElement) {
				this.name = ((NamedElement) eObj).getName();
			} else
				throw new ClassCastException(node
						+ " does not contain a NamedElement");
			nameIndex = fieldNames.indexOf(name);
		}

		// implements comparable interface
		public int compareTo(Object arg0) {
			if (arg0 instanceof SortedNodeListElement) {
				NodeImpl compareNode = ((SortedNodeListElement) arg0).getNode();
				EObject eObj = compareNode.getElement();
				if (eObj instanceof NamedElement) {
					String inputName = ((NamedElement) eObj).getName();
					int inputIndex = fieldNames.indexOf(inputName);
					return (nameIndex - inputIndex);
				} else
					throw new UnsupportedOperationException(
							"only comparisons between NamedElements are supported");
			}
			throw new UnsupportedOperationException(
					"only comparisons between SortedNodeListElements are supported");
		}

		// used to get underlying node from element
		public NodeImpl getNode() {
			return node;
		}
	}

}
