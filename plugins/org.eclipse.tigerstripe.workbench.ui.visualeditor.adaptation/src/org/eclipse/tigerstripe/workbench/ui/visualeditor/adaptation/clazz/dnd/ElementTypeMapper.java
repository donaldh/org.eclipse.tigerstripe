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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
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
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;

/**
 * This class enables the mapping of a iArtifact Type into a the corresponding
 * element type to be used to create a new part on the diagram.
 * 
 * WARNING: this duplicates the labels found in
 * org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.TigerstripeElementTypes
 * to avoid a circular dependency :-( This means that class needs to be updated
 * if the diagram class are being regenerated.
 * 
 * @author Eric Dillon
 * 
 */
public class ElementTypeMapper {

	/**
	 * @generated
	 */
	public static final IElementType Map_79 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Map_79"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Attribute_2001 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Attribute_2001"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Attribute_2002 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Attribute_2002"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Attribute_2003 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Attribute_2003"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Method_2004 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Method_2004"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Attribute_2005 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Attribute_2005"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Attribute_2006 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Attribute_2006"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Method_2007 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Method_2007"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Literal_2008 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Literal_2008"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Attribute_2009 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Attribute_2009"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Method_2010 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Method_2010"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Attribute_2011 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Attribute_2011"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Method_2012 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Method_2012"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType NamedQueryArtifact_1001 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.NamedQueryArtifact_1001"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ExceptionArtifact_1002 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.ExceptionArtifact_1002"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ManagedEntityArtifact_1003 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.ManagedEntityArtifact_1003"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType NotificationArtifact_1004 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.NotificationArtifact_1004"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType DatatypeArtifact_1005 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.DatatypeArtifact_1005"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Enumeration_1006 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Enumeration_1006"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType UpdateProcedureArtifact_1007 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.UpdateProcedureArtifact_1007"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType SessionFacadeArtifact_1008 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.SessionFacadeArtifact_1008"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType AssociationClassClass_1009 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.AssociationClassClass_1009"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Association_3001 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Association_3001"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType SessionFacadeArtifactEmittedNotifications_3002 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.SessionFacadeArtifactEmittedNotifications_3002"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType SessionFacadeArtifactManagedEntities_3003 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.SessionFacadeArtifactManagedEntities_3003"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType NamedQueryArtifactReturnedType_3004 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.NamedQueryArtifactReturnedType_3004"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType SessionFacadeArtifactNamedQueries_3005 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.SessionFacadeArtifactNamedQueries_3005"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType SessionFacadeArtifactExposedProcedures_3006 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.SessionFacadeArtifactExposedProcedures_3006"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType AbstractArtifactExtends_3007 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.AbstractArtifactExtends_3007"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Dependency_3008 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Dependency_3008"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Reference_3009 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.Reference_3009"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType AssociationClass_3010 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.AssociationClass_3010"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType AssociationClassAssociatedClass_3011 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.AssociationClassAssociatedClass_3011"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType AbstractArtifactImplements_3012 = getElementType("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.AbstractArtifactImplements_3012"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	private static IElementType getElementType(String id) {
		return ElementTypeRegistry.getInstance().getType(id);
	}

	public static IElementType mapToElementType(Object obj) {

		if (obj instanceof IManagedEntityArtifact)
			return ManagedEntityArtifact_1003;
		else if (obj instanceof IDatatypeArtifact)
			return DatatypeArtifact_1005;
		else if (obj instanceof IEnumArtifact)
			return Enumeration_1006;
		else if (obj instanceof IQueryArtifact)
			return NamedQueryArtifact_1001;
		else if (obj instanceof IUpdateProcedureArtifact)
			return UpdateProcedureArtifact_1007;
		else if (obj instanceof IExceptionArtifact)
			return ExceptionArtifact_1002;
		else if (obj instanceof ISessionArtifact)
			return SessionFacadeArtifact_1008;
		else if (obj instanceof IEventArtifact)
			return NotificationArtifact_1004;
		else if (obj instanceof IAssociationClassArtifact)
			return AssociationClass_3010;
		else if (obj instanceof IAssociationArtifact)
			return Association_3001;
		else if (obj instanceof IDependencyArtifact)
			return Dependency_3008;

		return null;
	}

	public static IElementType mapClassStringToElementType(String artifactClassName) {

		if (artifactClassName.equals(IManagedEntityArtifact.class.getName()))
			return ManagedEntityArtifact_1003;
		else if (artifactClassName.equals(IDatatypeArtifact.class.getName()))
			return DatatypeArtifact_1005;
		else if (artifactClassName.equals(IEnumArtifact.class.getName()))
			return Enumeration_1006;
		else if (artifactClassName.equals(IQueryArtifact.class.getName()))
			return NamedQueryArtifact_1001;
		else if (artifactClassName.equals(IUpdateProcedureArtifact.class.getName()))
			return UpdateProcedureArtifact_1007;
		else if (artifactClassName.equals(IExceptionArtifact.class.getName()))
			return ExceptionArtifact_1002;
		else if (artifactClassName.equals(ISessionArtifact.class.getName()))
			return SessionFacadeArtifact_1008;
		else if (artifactClassName.equals(IEventArtifact.class.getName()))
			return NotificationArtifact_1004;
		else if (artifactClassName.equals(IAssociationClassArtifact.class.getName()))
			return AssociationClass_3010;
		else if (artifactClassName.equals(IAssociationArtifact.class.getName()))
			return Association_3001;
		else if (artifactClassName.equals(IDependencyArtifact.class.getName()))
			return Dependency_3008;

		return null;
	}
	
	public static Class mapToIArtifactType(EObject eObject)
			throws TigerstripeException {
		if (eObject instanceof ManagedEntityArtifact)
			return IManagedEntityArtifact.class;
		else if (eObject instanceof DatatypeArtifact)
			return IDatatypeArtifact.class;
		else if (eObject instanceof Enumeration)
			return IEnumArtifact.class;
		else if (eObject instanceof AssociationClass)
			return IAssociationClassArtifact.class;
		else if (eObject instanceof Association)
			return IAssociationArtifact.class;
		else if (eObject instanceof Dependency)
			return IDependencyArtifact.class;
		else if (eObject instanceof NotificationArtifact)
			return IEventArtifact.class;
		else if (eObject instanceof NamedQueryArtifact)
			return IQueryArtifact.class;
		else if (eObject instanceof UpdateProcedureArtifact)
			return IUpdateProcedureArtifact.class;
		else if (eObject instanceof SessionFacadeArtifact)
			return ISessionArtifact.class;
		else if (eObject instanceof ExceptionArtifact)
			return IExceptionArtifact.class;
		else if (eObject instanceof AssociationClassClass)
			return IAssociationClassArtifact.class;

		throw new TigerstripeException("Can't map " + eObject
				+ " to IXXXXX type class");
	}

}
