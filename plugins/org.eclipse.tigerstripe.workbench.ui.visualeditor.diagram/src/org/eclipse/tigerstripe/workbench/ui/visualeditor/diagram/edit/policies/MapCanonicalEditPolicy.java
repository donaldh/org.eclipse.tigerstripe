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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AbstractArtifactExtendsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AbstractArtifactImplementsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassAssociatedClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute2EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute4EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute5EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute6EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute7EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AttributeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.LiteralEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Method2EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Method3EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Method4EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MethodEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamePackageInterface;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactReturnedTypeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ReferenceEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactEmittedNotificationsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactExposedProceduresEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactManagedEntitiesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactNamedQueriesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeVisualIDRegistry;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.TigerstripeElementTypes;

/**
 * @generated
 */
public class MapCanonicalEditPolicy extends CanonicalConnectionEditPolicy {

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
		for (Iterator values = ((Map) modelObject).getArtifacts().iterator(); values
				.hasNext();) {
			nextValue = (EObject) values.next();
			nodeVID = TigerstripeVisualIDRegistry.getNodeVisualID(viewObject,
					nextValue);
			switch (nodeVID) {
			case NamedQueryArtifactEditPart.VISUAL_ID: {
				result.add(nextValue);
				break;
			}
			case ExceptionArtifactEditPart.VISUAL_ID: {
				result.add(nextValue);
				break;
			}
			case ManagedEntityArtifactEditPart.VISUAL_ID: {
				result.add(nextValue);
				break;
			}
			case NotificationArtifactEditPart.VISUAL_ID: {
				result.add(nextValue);
				break;
			}
			case DatatypeArtifactEditPart.VISUAL_ID: {
				result.add(nextValue);
				break;
			}
			case EnumerationEditPart.VISUAL_ID: {
				result.add(nextValue);
				break;
			}
			case UpdateProcedureArtifactEditPart.VISUAL_ID: {
				result.add(nextValue);
				break;
			}
			case SessionFacadeArtifactEditPart.VISUAL_ID: {
				result.add(nextValue);
				break;
			}
			case AssociationClassClassEditPart.VISUAL_ID: {
				result.add(nextValue);
				break;
			}
			}
		}
		return result;
	}

	private String[] shouldRefreshLinks = {
			((IHintedType) TigerstripeElementTypes.Association_3001)
					.getSemanticHint(),
			((IHintedType) TigerstripeElementTypes.AssociationClass_3010)
					.getSemanticHint(),
			((IHintedType) TigerstripeElementTypes.Dependency_3008)
					.getSemanticHint(),
			((IHintedType) TigerstripeElementTypes.AbstractArtifactExtends_3007)
					.getSemanticHint(),
			((IHintedType) TigerstripeElementTypes.AbstractArtifactImplements_3012)
					.getSemanticHint(),
			((IHintedType) TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004)
					.getSemanticHint(),
			((IHintedType) TigerstripeElementTypes.SessionFacadeArtifactEmittedNotifications_3002)
					.getSemanticHint(),
			((IHintedType) TigerstripeElementTypes.SessionFacadeArtifactExposedProcedures_3006)
					.getSemanticHint(),
			((IHintedType) TigerstripeElementTypes.SessionFacadeArtifactManagedEntities_3003)
					.getSemanticHint(),
			((IHintedType) TigerstripeElementTypes.SessionFacadeArtifactNamedQueries_3005)
					.getSemanticHint(),
			((IHintedType) TigerstripeElementTypes.Reference_3009)
					.getSemanticHint(), };

	/**
	 * 
	 */
	@Override
	protected boolean shouldDeleteView(View view) {
		// TigerstripeRuntime.logInfoMessage("Should delete: " + view.getType()
		// + " view.isSetElement()=" + view.isSetElement()
		// + "view.getElement()=" + view.getElement()
		// + " view.getElement().eIsProxy()="
		// + view.getElement().eIsProxy());
		// ED we need to force refresh for views that don't have an element
		for (String semanticHint : shouldRefreshLinks) {
			if (semanticHint.equals(view.getType()))
				return true;
		}
		
		if ( view instanceof Node ) {
			if ( view.getElement() instanceof Map) 
				return true;
			if ( view.getElement() instanceof AssociationClassClass)
				return true;
		}

		return super.shouldDeleteView(view);
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
	public void refreshSemantic() {
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
	private java.util.Map myEObject2ViewMap = new HashMap();

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
				int diagramLinkVisualID = TigerstripeVisualIDRegistry
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
		int diagramElementVisualID = TigerstripeVisualIDRegistry
				.getVisualID(view);
		switch (diagramElementVisualID) {
		case NamedQueryArtifactEditPart.VISUAL_ID:
		case ExceptionArtifactEditPart.VISUAL_ID:
		case ManagedEntityArtifactEditPart.VISUAL_ID:
		case NotificationArtifactEditPart.VISUAL_ID:
		case DatatypeArtifactEditPart.VISUAL_ID:
		case EnumerationEditPart.VISUAL_ID:
		case UpdateProcedureArtifactEditPart.VISUAL_ID:
		case SessionFacadeArtifactEditPart.VISUAL_ID:
		case AssociationClassClassEditPart.VISUAL_ID:
		case AttributeEditPart.VISUAL_ID:
		case Attribute2EditPart.VISUAL_ID:
		case Attribute3EditPart.VISUAL_ID:
		case MethodEditPart.VISUAL_ID:
		case Attribute4EditPart.VISUAL_ID:
		case Attribute5EditPart.VISUAL_ID:
		case Method2EditPart.VISUAL_ID:
		case LiteralEditPart.VISUAL_ID:
		case Attribute6EditPart.VISUAL_ID:
		case Method3EditPart.VISUAL_ID:
		case Attribute7EditPart.VISUAL_ID:
		case Method4EditPart.VISUAL_ID:
		case MapEditPart.VISUAL_ID: {
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
		if (VisualeditorPackage.eINSTANCE.getMap().isSuperTypeOf(
				containerMetaclass)) {
			for (Iterator values = ((Map) container).getAssociations()
					.iterator(); values.hasNext();) {
				EObject nextValue = ((EObject) values.next());
				int linkVID = TigerstripeVisualIDRegistry
						.getLinkWithClassVisualID(nextValue);
				if (AssociationEditPart.VISUAL_ID == linkVID) {
					Object structuralFeatureResult = ((Association) nextValue)
							.getZEnd();
					if (structuralFeatureResult instanceof EObject) {
						EObject dst = (EObject) structuralFeatureResult;
						structuralFeatureResult = ((Association) nextValue)
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
		if (VisualeditorPackage.eINSTANCE.getMap().isSuperTypeOf(
				containerMetaclass)) {
			for (Iterator values = ((Map) container).getDependencies()
					.iterator(); values.hasNext();) {
				EObject nextValue = ((EObject) values.next());
				int linkVID = TigerstripeVisualIDRegistry
						.getLinkWithClassVisualID(nextValue);
				if (DependencyEditPart.VISUAL_ID == linkVID) {
					Object structuralFeatureResult = ((Dependency) nextValue)
							.getZEnd();
					if (structuralFeatureResult instanceof EObject) {
						EObject dst = (EObject) structuralFeatureResult;
						structuralFeatureResult = ((Dependency) nextValue)
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
		if (VisualeditorPackage.eINSTANCE.getAbstractArtifact().isSuperTypeOf(
				containerMetaclass)) {
			for (Iterator values = ((AbstractArtifact) container)
					.getReferences().iterator(); values.hasNext();) {
				EObject nextValue = ((EObject) values.next());
				int linkVID = TigerstripeVisualIDRegistry
						.getLinkWithClassVisualID(nextValue);
				if (ReferenceEditPart.VISUAL_ID == linkVID) {
					Object structuralFeatureResult = ((Reference) nextValue)
							.getZEnd();
					if (structuralFeatureResult instanceof EObject) {
						EObject dst = (EObject) structuralFeatureResult;
						EObject src = container;
						myLinkDescriptors.add(new LinkDescriptor(src, dst,
								nextValue, linkVID));
					}
				}
			}
		}
		if (VisualeditorPackage.eINSTANCE.getMap().isSuperTypeOf(
				containerMetaclass)) {
			for (Iterator values = ((Map) container).getAssociations()
					.iterator(); values.hasNext();) {
				EObject nextValue = ((EObject) values.next());
				int linkVID = TigerstripeVisualIDRegistry
						.getLinkWithClassVisualID(nextValue);
				if (AssociationClassEditPart.VISUAL_ID == linkVID) {
					Object structuralFeatureResult = ((Association) nextValue)
							.getZEnd();
					if (structuralFeatureResult instanceof EObject) {
						EObject dst = (EObject) structuralFeatureResult;
						structuralFeatureResult = ((Association) nextValue)
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

		if (VisualeditorPackage.eINSTANCE.getSessionFacadeArtifact()
				.isSuperTypeOf(containerMetaclass)) {
			for (Iterator destinations = ((SessionFacadeArtifact) container)
					.getEmittedNotifications().iterator(); destinations
					.hasNext();) {
				EObject nextDestination = (EObject) destinations.next();
				myLinkDescriptors
						.add(new LinkDescriptor(
								container,
								nextDestination,
								TigerstripeElementTypes.SessionFacadeArtifactEmittedNotifications_3002,
								SessionFacadeArtifactEmittedNotificationsEditPart.VISUAL_ID));

			}
		}
		if (VisualeditorPackage.eINSTANCE.getSessionFacadeArtifact()
				.isSuperTypeOf(containerMetaclass)) {
			for (Iterator destinations = ((SessionFacadeArtifact) container)
					.getManagedEntities().iterator(); destinations.hasNext();) {
				EObject nextDestination = (EObject) destinations.next();
				myLinkDescriptors
						.add(new LinkDescriptor(
								container,
								nextDestination,
								TigerstripeElementTypes.SessionFacadeArtifactManagedEntities_3003,
								SessionFacadeArtifactManagedEntitiesEditPart.VISUAL_ID));

			}
		}
		if (VisualeditorPackage.eINSTANCE.getNamedQueryArtifact()
				.isSuperTypeOf(containerMetaclass)) {
			EObject nextDestination = ((NamedQueryArtifact) container)
					.getReturnedType();
			myLinkDescriptors
					.add(new LinkDescriptor(
							container,
							nextDestination,
							TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004,
							NamedQueryArtifactReturnedTypeEditPart.VISUAL_ID));

		}
		if (VisualeditorPackage.eINSTANCE.getSessionFacadeArtifact()
				.isSuperTypeOf(containerMetaclass)) {
			for (Iterator destinations = ((SessionFacadeArtifact) container)
					.getNamedQueries().iterator(); destinations.hasNext();) {
				EObject nextDestination = (EObject) destinations.next();
				myLinkDescriptors
						.add(new LinkDescriptor(
								container,
								nextDestination,
								TigerstripeElementTypes.SessionFacadeArtifactNamedQueries_3005,
								SessionFacadeArtifactNamedQueriesEditPart.VISUAL_ID));

			}
		}
		if (VisualeditorPackage.eINSTANCE.getSessionFacadeArtifact()
				.isSuperTypeOf(containerMetaclass)) {
			for (Iterator destinations = ((SessionFacadeArtifact) container)
					.getExposedProcedures().iterator(); destinations.hasNext();) {
				EObject nextDestination = (EObject) destinations.next();
				myLinkDescriptors
						.add(new LinkDescriptor(
								container,
								nextDestination,
								TigerstripeElementTypes.SessionFacadeArtifactExposedProcedures_3006,
								SessionFacadeArtifactExposedProceduresEditPart.VISUAL_ID));

			}
		}
		if (VisualeditorPackage.eINSTANCE.getAbstractArtifact().isSuperTypeOf(
				containerMetaclass)) {
			EObject nextDestination = ((AbstractArtifact) container)
					.getExtends();
			myLinkDescriptors.add(new LinkDescriptor(container,
					nextDestination,
					TigerstripeElementTypes.AbstractArtifactExtends_3007,
					AbstractArtifactExtendsEditPart.VISUAL_ID));

		}

		if (VisualeditorPackage.eINSTANCE.getAssociationClass().isSuperTypeOf(
				containerMetaclass)) {
			EObject nextDestination = ((AssociationClass) container)
					.getAssociatedClass();
			myLinkDescriptors
					.add(new LinkDescriptor(
							container,
							nextDestination,
							TigerstripeElementTypes.AssociationClassAssociatedClass_3011,
							AssociationClassAssociatedClassEditPart.VISUAL_ID));

		}
		if (VisualeditorPackage.eINSTANCE.getAbstractArtifact().isSuperTypeOf(
				containerMetaclass)) {
			for (Iterator destinations = ((AbstractArtifact) container)
					.getImplements().iterator(); destinations.hasNext();) {
				EObject nextDestination = (EObject) destinations.next();
				myLinkDescriptors
						.add(new LinkDescriptor(
								container,
								nextDestination,
								TigerstripeElementTypes.AbstractArtifactImplements_3012,
								AbstractArtifactImplementsEditPart.VISUAL_ID));

			}
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * Catch and handle delete of AssociationClass parts or changes in the
	 * basePackage of the diagram that this map represents
	 * 
	 * @seeorg.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#
	 * handleNotificationEvent(org.eclipse.emf.common.notify.Notification)
	 */
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
			Map map = (Map) event.getNotifier();
			List<AbstractArtifact> artifacts = map.getArtifacts();
			IGraphicalEditPart host = (IGraphicalEditPart) getHost();
			for (AbstractArtifact artifact : artifacts) {
				// then, for each artifact, get the edit part that goes along
				// with it
				IGraphicalEditPart editPart = (IGraphicalEditPart) host
						.findEditPart(getHost(), artifact);
				if (editPart != null) {
					// if the edit part is not null, get a list of that edit
					// part's children
					List<AbstractEditPart> childEditParts = editPart
							.getChildren();
					for (AbstractEditPart childEditPart : childEditParts) {
						// and then refresh each of the children
						if (childEditPart instanceof NamePackageInterface)
							childEditPart.refresh();
					}
				}
			}
			List<QualifiedNamedElement> linkElements = new ArrayList<QualifiedNamedElement>();
			linkElements.addAll(map.getAssociations());
			linkElements.addAll(map.getDependencies());
			EditPart hostEditPart = getHost();
			for (QualifiedNamedElement element : linkElements) {
				if (element instanceof AssociationClass)
					continue;
				// then, for each artifact, get the edit part that goes along
				// with it
				IGraphicalEditPart editPart = (IGraphicalEditPart) findConnectionEditPart(element);
				if (editPart != null) {
					// if the edit part is not null, get a list of that edit
					// part's children
					List<AbstractEditPart> childEditParts = editPart
							.getChildren();
					for (AbstractEditPart childEditPart : childEditParts) {
						// and then refresh each of the children
						if (childEditPart instanceof NamePackageInterface)
							childEditPart.refresh();
					}
				}
			}
		} else if (event.getEventType() == Notification.REMOVE
				&& feature instanceof EReferenceImpl
				&& oldValue instanceof AssociationClass) {
			// if we get here, then we are trying to remove AssociationClass,
			// so remove the AssociationClassClass with the same name
			AssociationClass assocClass = (AssociationClass) oldValue;
			Map map = (Map) event.getNotifier();
			AssociationClassClass assocClassClass = findAssociatedAssociationClassClass(
					assocClass, map);
			if (assocClassClass != null)
				map.getArtifacts().remove(assocClassClass);
		} else if (event.getEventType() == Notification.REMOVE
				&& feature instanceof EReferenceImpl
				&& oldValue instanceof AssociationClassClass) {
			// if we get here, then we are trying to remove an
			// AssociationClassClass,
			// so remove the AssociationClass with the same name
			AssociationClassClass assocClassClass = (AssociationClassClass) oldValue;
			Map map = (Map) event.getNotifier();
			AssociationClass assocClass = findAssociatedAssociationClass(
					assocClassClass, map);
			if (assocClass != null)
				map.getAssociations().remove(assocClass);
		} else if (event.getEventType() == Notification.REMOVE_MANY
				&& feature instanceof EReferenceImpl
				&& oldValue instanceof EList) {
			// if in here, are removing multiple objects, for each one check to
			// see if
			// it is an association class or association class class and handle
			// accordingly
			EList valList = (EList) oldValue;
			Map map = (Map) event.getNotifier();
			for (Object val : valList) {
				if (val instanceof AssociationClass) {
					AssociationClass assocClass = (AssociationClass) val;
					AssociationClassClass assocClassClass = findAssociatedAssociationClassClass(
							assocClass, map);
					if (assocClassClass != null)
						map.getArtifacts().remove(assocClassClass);
				} else if (val instanceof AssociationClassClass) {
					AssociationClassClass assocClassClass = (AssociationClassClass) oldValue;
					AssociationClass assocClass = findAssociatedAssociationClass(
							assocClassClass, map);
					if (assocClass != null)
						map.getAssociations().remove(assocClass);
				} else if (val instanceof Dependency) {
					Dependency dependency = (Dependency) oldValue;
					if (dependency != null)
						map.getDependencies().remove(dependency);
				}
			}
		}
		super.handleNotificationEvent(event);
	}

	public EditPart findConnectionEditPart(EObject theElement) {
		if (theElement == null)
			return null;
		List<EditPart> connectionEditParts = ((MapEditPart) this.getHost())
				.getConnections();
		for (EditPart connectionEditPart : connectionEditParts) {
			View view = (View) ((IAdaptable) connectionEditPart)
					.getAdapter(View.class);
			if (view != null) {
				EObject el = ViewUtil.resolveSemanticElement(view);
				if ((el != null) && el.equals(theElement))
					return connectionEditPart;
			}
		}
		return null;
	}

	public AssociationClassClass findAssociatedAssociationClassClass(
			AssociationClass associationClass, Map map) {
		if (associationClass == null || associationClass.getName() == null)
			return null;
		List<AbstractArtifact> artifacts = map.getArtifacts();
		for (AbstractArtifact artifact : artifacts) {
			if (artifact instanceof AssociationClassClass
					&& artifact.getName().equals(associationClass.getName())
					&& artifact.getPackage().equals(
							associationClass.getPackage()))
				return (AssociationClassClass) artifact;
		}

		return null;
	}

	public AssociationClass findAssociatedAssociationClass(
			AssociationClassClass associationClassClass, Map map) {
		if (associationClassClass == null
				|| associationClassClass.getName() == null)
			return null;
		List<Association> associations = map.getAssociations();
		for (Association association : associations) {
			if (association instanceof AssociationClass
					&& association.getName().equals(
							associationClassClass.getName())
					&& association.getPackage().equals(
							associationClassClass.getPackage()))
				return (AssociationClass) association;
		}

		return null;
	}
}
