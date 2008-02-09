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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.gmf.TigerstripeShapeNodeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorPlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @generated
 */
public class TigerstripeModelingAssistantProvider extends
		ModelingAssistantProvider {

	private boolean isReadOnlyEditPart(IGraphicalEditPart editPart) {
		Object modelObj = editPart.getModel();
		if (modelObj instanceof NodeImpl) {
			NodeImpl nodeImpl = (NodeImpl) modelObj;
			Object elementObj = nodeImpl.getElement();
			if (elementObj instanceof QualifiedNamedElement) {
				QualifiedNamedElement artifact = (QualifiedNamedElement) elementObj;
				return artifact.isIsReadonly();
			}
		}
		return false;
	}

	// If the extendsRelationship is hidden on the diagram, we need to prevent
	// the
	// "add Extends" option to appear on the assistant.
	private boolean isHidingExtends(IGraphicalEditPart editPart) {
		Object modelObj = editPart.getModel();
		if (modelObj instanceof NodeImpl) {
			NodeImpl nodeImpl = (NodeImpl) modelObj;
			Object elementObj = nodeImpl.getElement();
			if (elementObj instanceof QualifiedNamedElement) {
				QualifiedNamedElement artifact = (QualifiedNamedElement) elementObj;
				NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
						artifact);
				return Boolean
						.parseBoolean(helper
								.getProperty(NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS));
			}
		}
		return false;
	}

	/**
	 * @generated NOT
	 */
	@Override
	public List getTypesForPopupBar(IAdaptable host) {
		IGraphicalEditPart editPart = (IGraphicalEditPart) host
				.getAdapter(IGraphicalEditPart.class);
		// first check to see if it's an editPart that has an associated
		// read-only "QualifiedNamedElement" object in it's model, if so then
		// we don't want to add any objects to the popup-bar
		if (isReadOnlyEditPart(editPart))
			return Collections.EMPTY_LIST;
		if (editPart instanceof NamedQueryArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.Attribute_2001);
			return types;
		}
		if (editPart instanceof ExceptionArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.Attribute_2002);
			return types;
		}
		if (editPart instanceof ManagedEntityArtifactEditPart) {
			List types = new ArrayList();
			if (!isReadOnlyEditPart(editPart)) {
				types.add(TigerstripeElementTypes.Attribute_2003);
				types.add(TigerstripeElementTypes.Method_2004);
			}
			return types;
		}
		if (editPart instanceof NotificationArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.Attribute_2005);
			return types;
		}
		if (editPart instanceof DatatypeArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.Attribute_2006);
			types.add(TigerstripeElementTypes.Method_2007);
			return types;
		}
		if (editPart instanceof EnumerationEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.Literal_2008);
			return types;
		}
		if (editPart instanceof UpdateProcedureArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.Attribute_2009);
			return types;
		}
		if (editPart instanceof SessionFacadeArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.Method_2010);
			return types;
		}
		if (editPart instanceof AssociationClassClassEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.Attribute_2011);
			types.add(TigerstripeElementTypes.Method_2012);
			return types;
		}
		if (editPart instanceof MapEditPart) {
			List types = new ArrayList();
			/*
			 * based on the profile being used, add the appropriate artifacts to
			 * the palette (only those artifacts that should be in the model for
			 * a given profile)
			 */
			IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession()
					.getActiveProfile();
			CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
					.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
			if (prop.getDetailsForType(IManagedEntityArtifact.class.getName())
					.isEnabled()) {
				types.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
			}
			if (prop.getDetailsForType(IDatatypeArtifact.class.getName())
					.isEnabled()) {
				types.add(TigerstripeElementTypes.DatatypeArtifact_1005);
			}
			if (prop.getDetailsForType(IEnumArtifact.class.getName())
					.isEnabled()) {
				types.add(TigerstripeElementTypes.Enumeration_1006);
			}
			if (prop.getDetailsForType(IQueryArtifact.class.getName())
					.isEnabled()) {
				types.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
			}
			if (prop
					.getDetailsForType(IUpdateProcedureArtifact.class.getName())
					.isEnabled()) {
				types.add(TigerstripeElementTypes.UpdateProcedureArtifact_1007);
			}
			if (prop.getDetailsForType(IEventArtifact.class.getName())
					.isEnabled()) {
				types.add(TigerstripeElementTypes.NotificationArtifact_1004);
			}
			if (prop.getDetailsForType(ISessionArtifact.class.getName())
					.isEnabled()) {
				types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			}
			if (prop.getDetailsForType(IExceptionArtifact.class.getName())
					.isEnabled()) {
				types.add(TigerstripeElementTypes.ExceptionArtifact_1002);
			}
			return types;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * Checks in the current active profile whether References are allowed or
	 * not
	 * 
	 * @return
	 */
	private boolean shouldDisplayReference() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		boolean displayReference = prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEATTRIBUTES_ASREFERENCE);
		return displayReference;
	}

	private boolean shouldDisplayManages() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		boolean displayManages = prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEMANAGEDENTITIES_ONSESSION);
		return displayManages;
	}

	private boolean shouldDisplayExposes() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		boolean displayExposes = prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEEXPOSEDPROCEDURES_ONSESSION);
		return displayExposes;
	}

	private boolean shouldDisplayEmits() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		boolean displayEmits = prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEEMITTEDNOTIFICATIONS_ONSESSION);
		return displayEmits;
	}

	private boolean shouldDisplayUses() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		boolean displayUses = prop
				.getPropertyValue(IOssjLegacySettigsProperty.USENAMEDQUERIES_ONSESSION);
		return displayUses;
	}

	private boolean shouldDisplayImplements() {
		GlobalSettingsProperty prop = (GlobalSettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.GLOBAL_SETTINGS);
		boolean displayImplements = prop
				.getPropertyValue(IGlobalSettingsProperty.IMPLEMENTSRELATIONSHIP);
		return displayImplements;
	}

	/**
	 * @generated NOT
	 */
	@Override
	public List getRelTypesOnSource(IAdaptable source) {

		boolean displayReference = shouldDisplayReference();
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source
				.getAdapter(IGraphicalEditPart.class);
		// first check to see if it's an editPart that has an associated
		// read-only "QualifiedNamedElement" object in it's model, if so then
		// we don't want to allow for any changes so return an empty list
		if (isReadOnlyEditPart(sourceEditPart))
			return Collections.EMPTY_LIST;
		if (sourceEditPart instanceof ManagedEntityArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			if (shouldDisplayImplements())
				types
						.add(TigerstripeElementTypes.AbstractArtifactImplements_3012);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (sourceEditPart instanceof DatatypeArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (sourceEditPart instanceof EnumerationEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			types.add(TigerstripeElementTypes.Dependency_3008);
			return types;
		}
		if (sourceEditPart instanceof NotificationArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (sourceEditPart instanceof UpdateProcedureArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (sourceEditPart instanceof NamedQueryArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			types
					.add(TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (sourceEditPart instanceof SessionFacadeArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			if (shouldDisplayManages())
				types
						.add(TigerstripeElementTypes.SessionFacadeArtifactManagedEntities_3003);
			if (shouldDisplayUses())
				types
						.add(TigerstripeElementTypes.SessionFacadeArtifactNamedQueries_3005);
			if (shouldDisplayExposes())
				types
						.add(TigerstripeElementTypes.SessionFacadeArtifactExposedProcedures_3006);
			if (shouldDisplayEmits())
				types
						.add(TigerstripeElementTypes.SessionFacadeArtifactEmittedNotifications_3002);
			types.add(TigerstripeElementTypes.Association_3001);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (sourceEditPart instanceof ExceptionArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			types.add(TigerstripeElementTypes.Association_3001);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (sourceEditPart instanceof AssociationClassClassEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			if (shouldDisplayImplements())
				types
						.add(TigerstripeElementTypes.AbstractArtifactImplements_3012);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Association_3001);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated NOT
	 */
	@Override
	public List getRelTypesOnTarget(IAdaptable target) {
		boolean displayReference = shouldDisplayReference();
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target
				.getAdapter(IGraphicalEditPart.class);
		// first check to see if it's an editPart that has an associated
		// read-only "QualifiedNamedElement" object in it's model, if so then
		// we don't want to allow for any changes so return an empty list
		if (isReadOnlyEditPart(targetEditPart))
			return Collections.EMPTY_LIST;
		if (targetEditPart instanceof ManagedEntityArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			if (shouldDisplayManages())
				types
						.add(TigerstripeElementTypes.SessionFacadeArtifactManagedEntities_3003);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (targetEditPart instanceof DatatypeArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			types
					.add(TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (targetEditPart instanceof EnumerationEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			// No reference whatsoever on Enumerations
			// if (displayReference)
			// types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			return types;
		}
		if (targetEditPart instanceof NamedQueryArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			if (shouldDisplayUses())
				types
						.add(TigerstripeElementTypes.SessionFacadeArtifactNamedQueries_3005);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (targetEditPart instanceof UpdateProcedureArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			if (shouldDisplayExposes())
				types
						.add(TigerstripeElementTypes.SessionFacadeArtifactExposedProcedures_3006);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (targetEditPart instanceof NotificationArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			if (shouldDisplayEmits())
				types
						.add(TigerstripeElementTypes.SessionFacadeArtifactEmittedNotifications_3002);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (targetEditPart instanceof SessionFacadeArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			if (shouldDisplayImplements())
				types
						.add(TigerstripeElementTypes.AbstractArtifactImplements_3012);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (targetEditPart instanceof ExceptionArtifactEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			types.add(TigerstripeElementTypes.Association_3001);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		if (targetEditPart instanceof AssociationClassClassEditPart) {
			List types = new ArrayList();
			types.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			if (displayReference)
				types.add(TigerstripeElementTypes.Reference_3009);
			types.add(TigerstripeElementTypes.Association_3001);
			types.add(TigerstripeElementTypes.Dependency_3008);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
			return types;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated NOT
	 */
	@Override
	public List getRelTypesOnSourceAndTarget(IAdaptable source,
			IAdaptable target) {
		boolean displayReference = shouldDisplayReference();
		// otherwise, use the source and target edit parts to determine what
		// types of
		// links are allowed
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source
				.getAdapter(IGraphicalEditPart.class);
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target
				.getAdapter(IGraphicalEditPart.class);
		// first check to see if it's an editPart that has an associated
		// read-only "QualifiedNamedElement" object in it's model, if so then
		// we don't want to allow for any changes so return an empty list
		if (isReadOnlyEditPart(sourceEditPart))
			return Collections.EMPTY_LIST;
		if (sourceEditPart instanceof ManagedEntityArtifactEditPart) {
			List types = new ArrayList();
			if (targetEditPart instanceof ManagedEntityArtifactEditPart
					&& targetEditPart != sourceEditPart
					&& !isOutboundReference(sourceEditPart, targetEditPart)) {
				if (!isHidingExtends(sourceEditPart))
					types
							.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			} else if (targetEditPart instanceof SessionFacadeArtifactEditPart) {
				if (shouldDisplayImplements()
						&& !isOutboundReference(sourceEditPart, targetEditPart)) // Bug
					// 924
					types
							.add(TigerstripeElementTypes.AbstractArtifactImplements_3012);
			}
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				addAssociationForTarget(targetEditPart, types);
			if (displayReference
					&& !(targetEditPart instanceof EnumerationEditPart)
					&& !isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Reference_3009);
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Dependency_3008);
			return types;
		}
		if (sourceEditPart instanceof DatatypeArtifactEditPart) {
			List types = new ArrayList();
			if ((targetEditPart instanceof DatatypeArtifactEditPart || targetEditPart instanceof EnumerationEditPart)
					&& targetEditPart != sourceEditPart
					&& !isOutboundReference(sourceEditPart, targetEditPart)) {
				if (!isHidingExtends(sourceEditPart))
					types
							.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			}
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				addAssociationForTarget(targetEditPart, types);
			if (displayReference
					&& !(targetEditPart instanceof EnumerationEditPart)
					&& !isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Reference_3009);
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Dependency_3008);
			return types;
		}
		if (sourceEditPart instanceof EnumerationEditPart) {
			List types = new ArrayList();
			if (targetEditPart instanceof EnumerationEditPart
					&& targetEditPart != sourceEditPart
					&& !isOutboundReference(sourceEditPart, targetEditPart)) {
				if (!isHidingExtends(sourceEditPart))
					types
							.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			}
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Dependency_3008);
			return types;
		}
		if (sourceEditPart instanceof NamedQueryArtifactEditPart) {
			List types = new ArrayList();
			if ((targetEditPart instanceof ManagedEntityArtifactEditPart || targetEditPart instanceof DatatypeArtifactEditPart)
					&& targetEditPart != sourceEditPart
					&& !isOutboundReference(sourceEditPart, targetEditPart)) {
				// returns relationship to either a Managed Entity or a Datatype
				types
						.add(TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004);
			} else if (targetEditPart instanceof NamedQueryArtifactEditPart
					&& targetEditPart != sourceEditPart
					&& !isOutboundReference(sourceEditPart, targetEditPart)) {
				if (!isHidingExtends(sourceEditPart))
					types
							.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			}
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				addAssociationForTarget(targetEditPart, types);
			if (displayReference
					&& !(targetEditPart instanceof EnumerationEditPart)
					&& !isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Reference_3009);
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Dependency_3008);
			return types;
		}
		if (sourceEditPart instanceof UpdateProcedureArtifactEditPart) {
			List types = new ArrayList();
			if (targetEditPart instanceof UpdateProcedureArtifactEditPart
					&& targetEditPart != sourceEditPart
					&& !isOutboundReference(sourceEditPart, targetEditPart)) {
				if (!isHidingExtends(sourceEditPart))
					types
							.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			}
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				addAssociationForTarget(targetEditPart, types);
			if (displayReference
					&& !(targetEditPart instanceof EnumerationEditPart)
					&& !isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Reference_3009);
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Dependency_3008);
			return types;
		}
		if (sourceEditPart instanceof NotificationArtifactEditPart) {
			List types = new ArrayList();
			if (targetEditPart instanceof NotificationArtifactEditPart
					&& targetEditPart != sourceEditPart
					&& !isOutboundReference(sourceEditPart, targetEditPart)) {
				if (!isHidingExtends(sourceEditPart))
					types
							.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			}
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				addAssociationForTarget(targetEditPart, types);
			if (displayReference
					&& !(targetEditPart instanceof EnumerationEditPart)
					&& !isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Reference_3009);
			if (!isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Dependency_3008);
			return types;
		}
		if (sourceEditPart instanceof SessionFacadeArtifactEditPart) {
			List types = new ArrayList();
			if (targetEditPart instanceof UpdateProcedureArtifactEditPart
					&& targetEditPart != sourceEditPart) {
				// exposes relationship to an Update Procedure
				if (shouldDisplayExposes()
						&& !isOutboundReference(sourceEditPart, targetEditPart))
					types
							.add(TigerstripeElementTypes.SessionFacadeArtifactExposedProcedures_3006);
			} else if (targetEditPart instanceof NotificationArtifactEditPart) {
				// emits relationship to a Notification Artifact
				if (shouldDisplayEmits()
						&& !isOutboundReference(sourceEditPart, targetEditPart))
					types
							.add(TigerstripeElementTypes.SessionFacadeArtifactEmittedNotifications_3002);
			} else if (targetEditPart instanceof ManagedEntityArtifactEditPart) {
				// manages relationship to a Managed Entity
				if (shouldDisplayManages()
						&& !isOutboundReference(sourceEditPart, targetEditPart))
					types
							.add(TigerstripeElementTypes.SessionFacadeArtifactManagedEntities_3003);
			} else if (targetEditPart instanceof NamedQueryArtifactEditPart) {
				// supports relationship to a Named Query
				if (shouldDisplayUses()
						&& !isOutboundReference(sourceEditPart, targetEditPart))
					types
							.add(TigerstripeElementTypes.SessionFacadeArtifactNamedQueries_3005);
			} else if (targetEditPart instanceof SessionFacadeArtifactEditPart
					&& targetEditPart != sourceEditPart
					&& !isOutboundReference(sourceEditPart, targetEditPart)) {
				// extends relationship to another Session Facade
				if (!isHidingExtends(sourceEditPart))
					types
							.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			}
			if (!isOutboundReference(sourceEditPart, targetEditPart)) {
				addAssociationForTarget(targetEditPart, types);
				types.add(TigerstripeElementTypes.Dependency_3008);
			}
			return types;
		}
		if (sourceEditPart instanceof ExceptionArtifactEditPart) {
			List types = new ArrayList();
			if (targetEditPart instanceof ExceptionArtifactEditPart
					&& targetEditPart != sourceEditPart
					&& !isOutboundReference(sourceEditPart, targetEditPart)) {
				if (!isHidingExtends(sourceEditPart))
					types
							.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			}
			if (displayReference
					&& !(targetEditPart instanceof EnumerationEditPart)
					&& !isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Reference_3009);

			if (!isOutboundReference(sourceEditPart, targetEditPart)) {
				addAssociationForTarget(targetEditPart, types);
				types.add(TigerstripeElementTypes.Dependency_3008);
			}
			return types;
		}
		if (sourceEditPart instanceof AssociationClassClassEditPart) {
			List types = new ArrayList();
			if ((targetEditPart instanceof AssociationClassClassEditPart || targetEditPart instanceof ManagedEntityArtifactEditPart) // Bug
					// 789
					&& targetEditPart != sourceEditPart
					&& !isOutboundReference(sourceEditPart, targetEditPart)) {
				if (!isHidingExtends(sourceEditPart))
					types
							.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
			}
			if (displayReference
					&& !(targetEditPart instanceof EnumerationEditPart)
					&& !isOutboundReference(sourceEditPart, targetEditPart))
				types.add(TigerstripeElementTypes.Reference_3009);

			if (!isOutboundReference(sourceEditPart, targetEditPart)) {
				types.add(TigerstripeElementTypes.Association_3001);
				types.add(TigerstripeElementTypes.Dependency_3008);
				types.add(TigerstripeElementTypes.AssociationClass_3010);
			}
			return types;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated NOT
	 */
	@Override
	public List getTypesForSource(IAdaptable target,
			IElementType relationshipType) {
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target
				.getAdapter(IGraphicalEditPart.class);
		if (targetEditPart instanceof ManagedEntityArtifactEditPart) {
			// target is a Managed Entity
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.SessionFacadeArtifactManagedEntities_3003) {
				// "manages" association to this Managed Entity from a Session
				// Facade
				types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			} else if (relationshipType == TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004) {
				// returns association from Named Query to Manged Entity
				types.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
			} else if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from another Managed Entity or Datatype
				// to this Managed Entity
				types.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
				types.add(TigerstripeElementTypes.DatatypeArtifact_1005);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceSourcetypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (targetEditPart instanceof DatatypeArtifactEditPart) {
			// target is a Datatype
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004) {
				// returns association to this Datatype from a Named Query
				types.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
			} else if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from another Datatype to this Datatype
				types.add(TigerstripeElementTypes.DatatypeArtifact_1005);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship to this Datatype from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceSourcetypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (targetEditPart instanceof EnumerationEditPart) {
			// target is an Enumeration
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from another Enumeration to this
				// Enumeration
				types.add(TigerstripeElementTypes.Enumeration_1006);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship to this Enumeration from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceSourcetypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (targetEditPart instanceof NotificationArtifactEditPart) {
			// target is a Notification Artifact
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.SessionFacadeArtifactEmittedNotifications_3002) {
				// emits relationship to this Notification Artifact from a
				// Session Facade
				types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			} else if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from another Notification to this
				// Notification
				types.add(TigerstripeElementTypes.NotificationArtifact_1004);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship to this Notification from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceSourcetypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (targetEditPart instanceof UpdateProcedureArtifactEditPart) {
			// target is an Update Procedure
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.SessionFacadeArtifactExposedProcedures_3006) {
				// exposes relationship to this Update Procedure from a Session
				// Facade
				types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			} else if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from another Update Procedure to this
				// one
				types.add(TigerstripeElementTypes.UpdateProcedureArtifact_1007);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship to this Update Procedure from
				// another Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceSourcetypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (targetEditPart instanceof NamedQueryArtifactEditPart) {
			// target is a Named Query
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.SessionFacadeArtifactNamedQueries_3005) {
				// supports relationship to this Named Query from a Session
				// Facade
				types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			} else if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from another Named Query to this one
				types.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship to this Named Query from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceSourcetypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (targetEditPart instanceof SessionFacadeArtifactEditPart) {
			// target is a Session Facade
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from another Session Facade to this one
				types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			} else if (relationshipType == TigerstripeElementTypes.AbstractArtifactImplements_3012) {
				types.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
				types.add(TigerstripeElementTypes.AssociationClass_3010);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship to this Session Facade from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceSourcetypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (targetEditPart instanceof ExceptionArtifactEditPart) {
			// target is an Exception
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from another Exception to this one
				types.add(TigerstripeElementTypes.ExceptionArtifact_1002);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship to this Exception from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceSourcetypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		// start again here...
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated NOT
	 */
	@Override
	public List getTypesForTarget(IAdaptable source,
			IElementType relationshipType) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source
				.getAdapter(IGraphicalEditPart.class);
		if (sourceEditPart instanceof ManagedEntityArtifactEditPart) {
			// target is a Managed Entity
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from this Managed Entity to another
				// Managed Entity
				types.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
			} else if (relationshipType == TigerstripeElementTypes.AbstractArtifactImplements_3012) {
				types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship from this Managed Entity to another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (sourceEditPart instanceof DatatypeArtifactEditPart) {
			// target is a Datatype
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from this Datatype to another Datatype
				// or Enumeration
				types.add(TigerstripeElementTypes.DatatypeArtifact_1005);
				types.add(TigerstripeElementTypes.Enumeration_1006);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship from this Datatype to another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (sourceEditPart instanceof EnumerationEditPart) {
			// target is an Enumeration
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from this Enumeration to another
				// Enumeration
				types.add(TigerstripeElementTypes.Enumeration_1006);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (sourceEditPart instanceof NotificationArtifactEditPart) {
			// target is a Notification Artifact
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from this Notification to another
				// Notification
				types.add(TigerstripeElementTypes.NotificationArtifact_1004);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship from this Notification to another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (sourceEditPart instanceof UpdateProcedureArtifactEditPart) {
			// target is an Update Procedure
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from this Update Procedure to another
				// Update Procedure
				types.add(TigerstripeElementTypes.UpdateProcedureArtifact_1007);
			} else if (relationshipType == TigerstripeElementTypes.Association_3001) {
				// association relationship from this Update Procedure to
				// another Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				// association relationship to this Managed Entity from another
				// Abstract Artifact
				addAssociationDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceDatatypes(types);
			} else if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (sourceEditPart instanceof NamedQueryArtifactEditPart) {
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004) {
				types.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
				types.add(TigerstripeElementTypes.DatatypeArtifact_1005);
			}
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// Named Query can only extend another Named Query
				types.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
			}
			if (relationshipType == TigerstripeElementTypes.Association_3001) {
				addAssociationDatatypes(types);
			}
			if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				addAssociationDatatypes(types);
			}
			if (relationshipType == TigerstripeElementTypes.Reference_3009) {
				addReferenceDatatypes(types);
			}
			if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (sourceEditPart instanceof SessionFacadeArtifactEditPart) {
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.SessionFacadeArtifactEmittedNotifications_3002) {
				types.add(TigerstripeElementTypes.NotificationArtifact_1004);
			}
			if (relationshipType == TigerstripeElementTypes.SessionFacadeArtifactManagedEntities_3003) {
				types.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
			}
			if (relationshipType == TigerstripeElementTypes.SessionFacadeArtifactNamedQueries_3005) {
				types.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
			}
			if (relationshipType == TigerstripeElementTypes.SessionFacadeArtifactExposedProcedures_3006) {
				types.add(TigerstripeElementTypes.UpdateProcedureArtifact_1007);
			}
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// Session Facade can only extend another Session Facade
				types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			}
			if (relationshipType == TigerstripeElementTypes.Association_3001) {
				addAssociationDatatypes(types);
			}
			if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				addAssociationDatatypes(types);
			}
			if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		if (sourceEditPart instanceof ExceptionArtifactEditPart) {
			List types = new ArrayList();
			if (relationshipType == TigerstripeElementTypes.Association_3001) {
				addAssociationDatatypes(types);
			}
			if (relationshipType == TigerstripeElementTypes.AssociationClass_3010) {
				addAssociationDatatypes(types);
			}
			if (relationshipType == TigerstripeElementTypes.AbstractArtifactExtends_3007) {
				// extends relationship from another Exception to this one
				types.add(TigerstripeElementTypes.ExceptionArtifact_1002);
			}
			if (relationshipType == TigerstripeElementTypes.Dependency_3008) {
				addDependencyDatatypes(types);
			}
			return types;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	@Override
	public EObject selectExistingElementForSource(IAdaptable target,
			IElementType relationshipType) {
		return selectExistingElement(target, getTypesForSource(target,
				relationshipType));
	}

	/**
	 * @generated
	 */
	@Override
	public EObject selectExistingElementForTarget(IAdaptable source,
			IElementType relationshipType) {
		return selectExistingElement(source, getTypesForTarget(source,
				relationshipType));
	}

	/**
	 * @generated
	 */
	protected EObject selectExistingElement(IAdaptable host, Collection types) {
		if (types.isEmpty())
			return null;
		IGraphicalEditPart editPart = (IGraphicalEditPart) host
				.getAdapter(IGraphicalEditPart.class);
		if (editPart == null)
			return null;
		Diagram diagram = (Diagram) editPart.getRoot().getContents().getModel();
		Collection elements = new HashSet();
		for (Iterator it = diagram.getElement().eAllContents(); it.hasNext();) {
			EObject element = (EObject) it.next();
			if (isApplicableElement(element, types)) {
				elements.add(element);
			}
		}
		if (elements.isEmpty())
			return null;
		return selectElement((EObject[]) elements.toArray(new EObject[elements
				.size()]));
	}

	/**
	 * @generated
	 */
	protected boolean isApplicableElement(EObject element, Collection types) {
		IElementType type = ElementTypeRegistry.getInstance().getElementType(
				element);
		return types.contains(type);
	}

	/**
	 * @generated
	 */
	protected EObject selectElement(EObject[] elements) {
		Shell shell = Display.getCurrent().getActiveShell();
		ILabelProvider labelProvider = new AdapterFactoryLabelProvider(
				TigerstripeDiagramEditorPlugin.getInstance()
						.getItemProvidersAdapterFactory());
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				shell, labelProvider);
		dialog.setMessage("Available domain model elements:");
		dialog.setTitle("Select domain model element");
		dialog.setMultipleSelection(false);
		dialog.setElements(elements);
		EObject selected = null;
		if (dialog.open() == Window.OK) {
			selected = (EObject) dialog.getFirstResult();
		}
		return selected;
	}

	/*
	 * internal method that is used to add datatypes that can be included in an
	 * association relationship to the list of data types being returned from
	 * other methods in this class
	 */
	private void addAssociationDatatypes(List types) {
		if (types != null) {
			types.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
			types.add(TigerstripeElementTypes.DatatypeArtifact_1005);
			types.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
			types.add(TigerstripeElementTypes.UpdateProcedureArtifact_1007);
			types.add(TigerstripeElementTypes.NotificationArtifact_1004);
			types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			types.add(TigerstripeElementTypes.ExceptionArtifact_1002);
			types.add(TigerstripeElementTypes.AssociationClassClass_1009);
		}
	}

	/*
	 * internal method that is used to add datatypes that can be included in an
	 * association relationship to the list of data types being returned from
	 * other methods in this class
	 */
	private void addDependencyDatatypes(List types) {
		if (types != null) {
			types.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
			types.add(TigerstripeElementTypes.DatatypeArtifact_1005);
			types.add(TigerstripeElementTypes.Enumeration_1006);
			types.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
			types.add(TigerstripeElementTypes.UpdateProcedureArtifact_1007);
			types.add(TigerstripeElementTypes.NotificationArtifact_1004);
			types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			types.add(TigerstripeElementTypes.ExceptionArtifact_1002);
			types.add(TigerstripeElementTypes.AssociationClassClass_1009);
		}
	}

	/*
	 * internal method that is used to add datatypes that can be included as a
	 * target in a reference relationship to the list of data types being
	 * returned from other methods in this class
	 */
	private void addReferenceDatatypes(List types) {
		if (types != null) {
			types.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
			types.add(TigerstripeElementTypes.DatatypeArtifact_1005);
			// types.add(TigerstripeElementTypes.Enumeration_1006);
			types.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
			types.add(TigerstripeElementTypes.UpdateProcedureArtifact_1007);
			types.add(TigerstripeElementTypes.NotificationArtifact_1004);
			types.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
			types.add(TigerstripeElementTypes.ExceptionArtifact_1002);
			types.add(TigerstripeElementTypes.AssociationClassClass_1009);
		}
	}

	/*
	 * internal method that is used to add datatypes that can be included as a
	 * source in a reference relationship to the list of data types being
	 * returned from other methods in this class
	 */
	private void addReferenceSourcetypes(List types) {
		if (types != null) {
			types.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
			types.add(TigerstripeElementTypes.DatatypeArtifact_1005);
			types.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
			types.add(TigerstripeElementTypes.UpdateProcedureArtifact_1007);
			types.add(TigerstripeElementTypes.NotificationArtifact_1004);
			types.add(TigerstripeElementTypes.AssociationClassClass_1009);
		}
	}

	/*
	 * an internal method that can be used to add an association link to the
	 * list of types if the targetEditPart is of the appropriate type
	 */
	private void addAssociationForTarget(IGraphicalEditPart targetEditPart,
			List types) {
		if (targetEditPart instanceof ManagedEntityArtifactEditPart
				|| targetEditPart instanceof DatatypeArtifactEditPart
				|| targetEditPart instanceof NamedQueryArtifactEditPart
				|| targetEditPart instanceof UpdateProcedureArtifactEditPart
				|| targetEditPart instanceof NotificationArtifactEditPart
				|| targetEditPart instanceof SessionFacadeArtifactEditPart
				|| targetEditPart instanceof ExceptionArtifactEditPart
				|| targetEditPart instanceof AssociationClassClassEditPart) {
			// association relationship from this artifact to other
			// artifacts that can be included in an association relationship
			types.add(TigerstripeElementTypes.Association_3001);
			types.add(TigerstripeElementTypes.AssociationClass_3010);
		}

	}

	/**
	 * This method checks whether creating a relationship between sourcePart and
	 * target would create an outbound relationship, i.e. creating a
	 * relationship from a referenced project into the local project.
	 * 
	 * Bug 924 & Bug 925
	 * 
	 * @param sourcePart
	 * @param targetPart
	 * @return
	 */
	protected boolean isOutboundReference(IGraphicalEditPart sourcePart,
			IGraphicalEditPart targetPart) {

		if (!(sourcePart instanceof TigerstripeShapeNodeEditPart)
				|| !(targetPart instanceof TigerstripeShapeNodeEditPart))
			return false;

		// we need to establish in which project source and target live.
		// If the source is in a referenced project but target is not in that
		// project, it would create an outbound reference.
		Diagram diagram = (Diagram) ((MapEditPart) sourcePart.getParent())
				.getModel();
		Map map = (Map) diagram.getElement();

		Node srcNode = (Node) sourcePart.getModel();
		Node tgtNode = (Node) targetPart.getModel();
		String srcEArtifactFQN = ((AbstractArtifact) srcNode.getElement())
				.getFullyQualifiedName();
		String tgtEArtifactFQN = ((AbstractArtifact) tgtNode.getElement())
				.getFullyQualifiedName();

		ITigerstripeModelProject tsProject = map
				.getCorrespondingITigerstripeProject();
		try {
			IArtifactManagerSession session = tsProject
					.getArtifactManagerSession();
			IAbstractArtifact src = session
					.getArtifactByFullyQualifiedName(srcEArtifactFQN);
			ITigerstripeModelProject srcProject = src.getTigerstripeProject();

			IAbstractArtifact tgt = session
					.getArtifactByFullyQualifiedName(tgtEArtifactFQN);
			ITigerstripeModelProject tgProject = tgt.getTigerstripeProject();

			boolean srcIsLocal = tsProject.equals(srcProject);
			boolean tgtIsLocal = tsProject.equals(tgProject);

			if (srcIsLocal)
				return false;
			else {
				if (tgtIsLocal)
					return true;
				else {
					if (tgProject != null)
						return !tgProject.equals(srcProject);
					else
						return tgProject != srcProject;
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return false;
	}
}
