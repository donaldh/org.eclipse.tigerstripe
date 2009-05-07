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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.EdgeImpl;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.AssociationInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.NamePackageInterface;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.VariableEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceVisualIDRegistry;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramReferenceMapper;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

/**
 * @generated
 */
public class InstanceMapCanonicalEditPolicy extends
		CanonicalConnectionEditPolicy {

	/**
	 * @generated
	 */
	@Override
	protected List getSemanticChildrenList() {
		List result = new LinkedList();
		EObject modelObject = ((View) getHost().getModel()).getElement();
		View viewObject = (View) getHost().getModel();
		EObject nextValue;
		int nodeVID;
		for (Iterator values = ((InstanceMap) modelObject).getClassInstances()
				.iterator(); values.hasNext();) {
			nextValue = (EObject) values.next();
			nodeVID = InstanceVisualIDRegistry.getNodeVisualID(viewObject,
					nextValue);
			if (ClassInstanceEditPart.VISUAL_ID == nodeVID) {
				result.add(nextValue);
			}
		}
		return result;
	}

	// private String[] shouldRefreshLinks = {
	// ((IHintedType) InstanceElementTypes.AssociationInstance_3001)
	// .getSemanticHint() };

	/**
	 * @generated NOT
	 */
	@Override
	protected boolean shouldDeleteView(View view) {
		// ED we need to force refresh for views that don't have an element
		// for (String semanticHint : shouldRefreshLinks) {
		// if (semanticHint.equals(view.getType())) {
		// return true;
		// }
		// }
		// System.out.println("Shoulddelete: " + view);

		return view.isSetElement() && view.getElement() != null;
		// && view.getElement().eIsProxy();
	}

	/**
	 * @generated
	 */
	@Override
	protected String getDefaultFactoryHint() {
		return null;
	}

	/**
	 * @generated
	 */
	@Override
	protected List getSemanticConnectionsList() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	@Override
	protected EObject getSourceElement(EObject relationship) {
		return null;
	}

	/**
	 * @generated
	 */
	@Override
	protected EObject getTargetElement(EObject relationship) {
		return null;
	}

	/**
	 * @generated
	 */
	@Override
	protected boolean shouldIncludeConnection(Edge connector,
			Collection children) {
		return false;
	}

	/**
	 * @generated
	 */
	@Override
	protected void refreshSemantic() {
		List createdViews = new LinkedList();
		createdViews.addAll(refreshSemanticChildren());
		List createdConnectionViews = new LinkedList();
		createdConnectionViews.addAll(refreshSemanticConnections());
		createdConnectionViews.addAll(refreshConnections());

		if (createdViews.size() > 1) {
			// perform a layout of the container
			DeferredLayoutCommand layoutCmd = new DeferredLayoutCommand(host()
					.getEditingDomain(), createdViews, host());
			executeCommand(new ICommandProxy(layoutCmd));
		}

		createdViews.addAll(createdConnectionViews);
		makeViewsImmutable(createdViews);
	}

	/**
	 * @generated
	 */
	private Collection myLinkDescriptors = new LinkedList();

	/**
	 * @generated
	 */
	private Map myEObject2ViewMap = new HashMap();

	/**
	 * @generated
	 */
	private Collection refreshConnections() {
		try {
			collectAllLinks(getDiagram());
			Collection existingLinks = new LinkedList(getDiagram().getEdges());
			for (Iterator diagramLinks = existingLinks.iterator(); diagramLinks
					.hasNext();) {
				Edge nextDiagramLink = (Edge) diagramLinks.next();
				EObject diagramLinkObject = nextDiagramLink.getElement();
				EObject diagramLinkSrc = nextDiagramLink.getSource()
						.getElement();
				EObject diagramLinkDst = nextDiagramLink.getTarget()
						.getElement();
				int diagramLinkVisualID = InstanceVisualIDRegistry
						.getVisualID(nextDiagramLink);
				for (Iterator modelLinkDescriptors = myLinkDescriptors
						.iterator(); modelLinkDescriptors.hasNext();) {
					LinkDescriptor nextLinkDescriptor = (LinkDescriptor) modelLinkDescriptors
							.next();
					if (diagramLinkObject == nextLinkDescriptor
							.getLinkElement()
							&& diagramLinkSrc == nextLinkDescriptor.getSource()
							&& diagramLinkDst == nextLinkDescriptor
									.getDestination()
							&& diagramLinkVisualID == nextLinkDescriptor
									.getVisualID()) {
						diagramLinks.remove();
						modelLinkDescriptors.remove();
					}
				}
			}
			deleteViews(existingLinks.iterator());
			return createConnections(myLinkDescriptors);
		} finally {
			myLinkDescriptors.clear();
			myEObject2ViewMap.clear();
		}
	}

	/**
	 * @generated
	 */
	private void collectAllLinks(View view) {
		EObject modelElement = view.getElement();
		int diagramElementVisualID = InstanceVisualIDRegistry.getVisualID(view);
		switch (diagramElementVisualID) {
		case ClassInstanceEditPart.VISUAL_ID:
		case VariableEditPart.VISUAL_ID:
		case InstanceMapEditPart.VISUAL_ID: {
			myEObject2ViewMap.put(modelElement, view);
			storeLinks(modelElement, getDiagram());
		}
		default: {
		}
			for (Iterator children = view.getChildren().iterator(); children
					.hasNext();) {
				View childView = (View) children.next();
				collectAllLinks(childView);
			}
		}
	}

	/**
	 * @generated
	 */
	private Collection createConnections(Collection linkDescriptors) {
		if (linkDescriptors.isEmpty())
			return Collections.EMPTY_LIST;
		List adapters = new LinkedList();
		for (Iterator linkDescriptorsIterator = linkDescriptors.iterator(); linkDescriptorsIterator
				.hasNext();) {
			final LinkDescriptor nextLinkDescriptor = (LinkDescriptor) linkDescriptorsIterator
					.next();
			EditPart sourceEditPart = getEditPartFor(nextLinkDescriptor
					.getSource());
			EditPart targetEditPart = getEditPartFor(nextLinkDescriptor
					.getDestination());
			if (sourceEditPart == null || targetEditPart == null) {
				continue;
			}
			CreateConnectionViewRequest.ConnectionViewDescriptor descriptor = new CreateConnectionViewRequest.ConnectionViewDescriptor(
					nextLinkDescriptor.getSemanticAdapter(), null,
					ViewUtil.APPEND, false, ((IGraphicalEditPart) getHost())
							.getDiagramPreferencesHint());
			CreateConnectionViewRequest ccr = new CreateConnectionViewRequest(
					descriptor);
			ccr.setType(org.eclipse.gef.RequestConstants.REQ_CONNECTION_START);
			ccr.setSourceEditPart(sourceEditPart);
			sourceEditPart.getCommand(ccr);
			ccr.setTargetEditPart(targetEditPart);
			ccr.setType(org.eclipse.gef.RequestConstants.REQ_CONNECTION_END);
			Command cmd = targetEditPart.getCommand(ccr);
			if (cmd != null && cmd.canExecute()) {
				executeCommand(cmd);
				IAdaptable viewAdapter = (IAdaptable) ccr.getNewObject();
				if (viewAdapter != null) {
					adapters.add(viewAdapter);
				}
			}
		}
		return adapters;
	}

	/**
	 * @generated
	 */
	private EditPart getEditPartFor(EObject modelElement) {
		View view = (View) myEObject2ViewMap.get(modelElement);
		if (view != null)
			return (EditPart) getHost().getViewer().getEditPartRegistry().get(
					view);
		return null;
	}

	/**
	 * @generated
	 */
	private void storeLinks(EObject container, Diagram diagram) {
		EClass containerMetaclass = container.eClass();
		storeFeatureModelFacetLinks(container, containerMetaclass, diagram);
		storeTypeModelFacetLinks(container, containerMetaclass);
	}

	/**
	 * @generated
	 */
	private void storeTypeModelFacetLinks(EObject container,
			EClass containerMetaclass) {
		if (InstancediagramPackage.eINSTANCE.getInstanceMap().isSuperTypeOf(
				containerMetaclass)) {
			for (Iterator values = ((InstanceMap) container)
					.getAssociationInstances().iterator(); values.hasNext();) {
				EObject nextValue = ((EObject) values.next());
				int linkVID = InstanceVisualIDRegistry
						.getLinkWithClassVisualID(nextValue);
				if (AssociationInstanceEditPart.VISUAL_ID == linkVID) {
					Object structuralFeatureResult = ((AssociationInstance) nextValue)
							.getZEnd();
					if (structuralFeatureResult instanceof EObject) {
						EObject dst = (EObject) structuralFeatureResult;
						structuralFeatureResult = ((AssociationInstance) nextValue)
								.getAEnd();
						if (structuralFeatureResult instanceof EObject) {
							EObject src = (EObject) structuralFeatureResult;
							myLinkDescriptors.add(new LinkDescriptor(src, dst,
									nextValue, linkVID));
						}
					}
				}
			}
		}
	}

	/**
	 * @generated
	 */
	private void storeFeatureModelFacetLinks(EObject container,
			EClass containerMetaclass, Diagram diagram) {

	}

	/**
	 * @generated
	 */
	private Diagram getDiagram() {
		return ((View) getHost().getModel()).getDiagram();
	}

	/**
	 * @generated
	 */
	private class LinkDescriptor {

		/**
		 * @generated
		 */
		private EObject mySource;

		/**
		 * @generated
		 */
		private EObject myDestination;

		/**
		 * @generated
		 */
		private EObject myLinkElement;

		/**
		 * @generated
		 */
		private int myVisualID;

		/**
		 * @generated
		 */
		private IAdaptable mySemanticAdapter;

		/**
		 * @generated
		 */
		protected LinkDescriptor(EObject source, EObject destination,
				EObject linkElement, int linkVID) {
			this(source, destination, linkVID);
			myLinkElement = linkElement;
			mySemanticAdapter = new EObjectAdapter(linkElement);
		}

		/**
		 * @generated
		 */
		protected LinkDescriptor(EObject source, EObject destination,
				IElementType elementType, int linkVID) {
			this(source, destination, linkVID);
			myLinkElement = null;
			final IElementType elementTypeCopy = elementType;
			mySemanticAdapter = new IAdaptable() {
				public Object getAdapter(Class adapter) {
					if (IElementType.class.equals(adapter))
						return elementTypeCopy;
					return null;
				}
			};
		}

		/**
		 * @generated
		 */
		private LinkDescriptor(EObject source, EObject destination, int linkVID) {
			mySource = source;
			myDestination = destination;
			myVisualID = linkVID;
		}

		/**
		 * @generated
		 */
		protected EObject getSource() {
			return mySource;
		}

		/**
		 * @generated
		 */
		protected EObject getDestination() {
			return myDestination;
		}

		/**
		 * @generated
		 */
		protected EObject getLinkElement() {
			return myLinkElement;
		}

		/**
		 * @generated
		 */
		protected int getVisualID() {
			return myVisualID;
		}

		/**
		 * @generated
		 */
		protected IAdaptable getSemanticAdapter() {
			return mySemanticAdapter;
		}
	}

	@Override
	protected void handleNotificationEvent(Notification event) {
		Object feature = event.getFeature();
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		if (feature instanceof EAttributeImpl
				&& ((EAttributeImpl) feature).getName().equals("basePackage")
				&& oldValue != null && !oldValue.equals(newValue)) {
			/*
			 * if here, then the basePackage property is being changed, need to
			 * update the other artifacts in the diagram to reflect the change
			 * in the root package for the diagram (their displayed package will
			 * probably need to change as well). To do this, first we need to
			 * get a list of the artifacts contained in this map
			 */
			InstanceMap map = (InstanceMap) event.getNotifier();
			List<ClassInstance> classInstances = map.getClassInstances();
			List<AssociationInstance> assocInstances = map
					.getAssociationInstances();
			IGraphicalEditPart host = (IGraphicalEditPart) getHost();
			for (ClassInstance instance : classInstances) {
				// then, for each artifact, get the edit part that goes along
				// with it
				IGraphicalEditPart editPart = (IGraphicalEditPart) host
						.findEditPart(getHost(), instance);
				if (editPart != null) {
					// if the edit part is not null, get a list of that edit
					// part's children
					List<EditPart> childEditParts = editPart.getChildren();
					for (EditPart childEditPart : childEditParts) {
						// and then refresh each of the children
						if (childEditPart instanceof NamePackageInterface)
							childEditPart.refresh();
					}
				}
			}
			InstanceMapEditPart mapEditPart = (InstanceMapEditPart) host;
			List connectionList = mapEditPart.getConnections();
			Map<String, EditPart> connectionMap = new HashMap<String, EditPart>();
			for (Object obj : connectionList) {
				if (obj instanceof AssociationInstanceEditPart) {
					AssociationInstanceEditPart assocEditPart = (AssociationInstanceEditPart) obj;
					EObject eObject = ((EdgeImpl) assocEditPart.getModel())
							.getElement();
					AssociationInstance assoc = (AssociationInstance) eObject;
					String key = assoc.toString();
					connectionMap.put(key, assocEditPart);
				}
			}
			for (AssociationInstance instance : assocInstances) {
				// then, for each artifact, get the edit part that goes along
				// with it
				String key = instance.toString();
				IGraphicalEditPart editPart = (IGraphicalEditPart) connectionMap
						.get(key);
				if (editPart != null) {
					// if the edit part is not null, get a list of that edit
					// part's children
					List<EditPart> childEditParts = editPart.getChildren();
					for (EditPart childEditPart : childEditParts) {
						// and then refresh each of the children
						if (childEditPart instanceof NamePackageInterface)
							childEditPart.refresh();
					}
				}
			}
		} else if (event.getEventType() == Notification.REMOVE
				&& feature instanceof EReferenceImpl
				&& oldValue instanceof AssociationInstance) {
			AssociationInstance assocInstance = (AssociationInstance) oldValue;
			String assocInstanceName = assocInstance.getArtifactName();
			boolean isAssocClassAEnd = false;
			boolean isAssocClassZEnd = false;
			String otherAssocInstanceName = "";
			String classInstanceName = "";
			if (null != assocInstanceName && !"".equals(assocInstanceName)) {
				isAssocClassAEnd = assocInstanceName.endsWith("::aEnd");
				isAssocClassZEnd = assocInstanceName.endsWith("::zEnd");
			}
			if (!isAssocClassAEnd && !isAssocClassZEnd) {
				super.handleNotificationEvent(event);
				return;
			} else if (isAssocClassAEnd) {
				int pos = assocInstanceName.lastIndexOf("::aEnd");
				classInstanceName = assocInstanceName.substring(0, pos);
				otherAssocInstanceName = classInstanceName + "::zEnd";
			} else {
				int pos = assocInstanceName.lastIndexOf("::zEnd");
				classInstanceName = assocInstanceName.substring(0, pos);
				otherAssocInstanceName = classInstanceName + "::aEnd";
			}
			// if we get here, then we are trying to remove an Association from
			// an Association
			// Class instance, so need to remove the other Association from the
			// model
			InstanceMap map = (InstanceMap) event.getNotifier();
			Object[] assocInstances = map.getAssociationInstances().toArray();
			List<AssociationInstance> aiToRemove = new ArrayList<AssociationInstance>();
			for (Object obj : assocInstances) {
				AssociationInstance newInstance = (AssociationInstance) obj;
				if (newInstance != assocInstance) {
					if (otherAssocInstanceName.equals(newInstance
							.getArtifactName())) {
						// have found the other association instance that is
						// linked to this
						// association instance by artifact name (they are both
						// part of the
						// same association class artifact)
						aiToRemove.add(newInstance);
						break;
					}
				}
			}
			if (!aiToRemove.isEmpty())
				map.getAssociationInstances().removeAll(aiToRemove);

			EList classInstances = map.getClassInstances();
			List<ClassInstance> ciToRemove = new ArrayList<ClassInstance>();
			for (Object obj : classInstances) {
				ClassInstance classInstance = (ClassInstance) obj;
				if (classInstance.isAssociationClassInstance()
						&& classInstance.getArtifactName().equals(
								classInstanceName)) {
					// have found an association class instance that is linked
					// to this association
					// instance by name (they are both part of the same
					// artifact)
					ciToRemove.add(classInstance);
					break;
				}
			}
			if (!ciToRemove.isEmpty())
				map.getClassInstances().removeAll(ciToRemove);
		} else if (event.getEventType() == Notification.REMOVE
				&& feature instanceof EReferenceImpl
				&& oldValue instanceof ClassInstance) {
			// removing a class instance, so loop through associations in the
			// map and
			// remove any that have a null aEnd or zEnd (that don't point to
			// anything
			// on one end or the other)
			ClassInstance classInstance = (ClassInstance) oldValue;
			List<AssociationInstance> associations = new ArrayList<AssociationInstance>();
			InstanceMap map = (InstanceMap) event.getNotifier();
			associations.addAll(map.getAssociationInstances());
			List<AssociationInstance> assocsToRemove = new ArrayList<AssociationInstance>();
			for (AssociationInstance association : associations) {
				if (association.getAEnd() == null
						|| association.getZEnd() == null)
					assocsToRemove.add(association);
			}
			if (assocsToRemove.size() > 0)
				map.getAssociationInstances().removeAll(assocsToRemove);
			// then remove the old class instance name from each of the
			// variables
			// in the map that reference it
			String instanceName = classInstance.getArtifactName();
			List<Variable> dependentVariables = InstanceDiagramReferenceMapper.eINSTANCE
					.getDependentVariables(map, instanceName);
			for (Variable variable : dependentVariables) {
				String oldStrVal = variable.getValue();
				String newStrVal = InstanceDiagramUtils
						.removeInstanceReference(oldStrVal, instanceName);
				if (newStrVal.length() > 0)
					variable.setValue(newStrVal);
				else {
					// no values left, so remove the variable from the class
					// instance that contains it
					ClassInstance containingClassInstance = (ClassInstance) variable
							.eContainer();
					containingClassInstance.getVariables().remove(variable);
				}
			}
			// after have updated the variables that used to refer to this
			// instance,
			// need to remove any references from variables in this class to
			// other instances
			// in the diagram from the reference mapper
			List<Variable> variableList = classInstance.getVariables();
			for (Variable variable : variableList) {
				InstanceDiagramReferenceMapper.eINSTANCE
						.removeAllVariableReferences(map, variable);
			}
			// finally, update the reference mapper to remove this instance from
			// the map of
			// instance references that it maintains
			List<Variable> mapEntry = InstanceDiagramReferenceMapper.eINSTANCE
					.removeInstance(map, instanceName);
		}
		super.handleNotificationEvent(event);
	}
}
