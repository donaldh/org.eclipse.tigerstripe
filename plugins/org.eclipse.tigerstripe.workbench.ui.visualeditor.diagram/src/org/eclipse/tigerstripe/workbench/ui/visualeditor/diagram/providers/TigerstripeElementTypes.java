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

import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorPlugin;



/**
 * @generated
 */
public class TigerstripeElementTypes {
	
	/**
	 * @generated
	 */
	private TigerstripeElementTypes() {
	}

	/**
	 * @generated
	 */
	private static Map elements;

	/**
	 * @generated
	 */
	private static ImageRegistry imageRegistry;

	/**
	 * @generated
	 */
	private static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
		}
		return imageRegistry;
	}

	/**
	 * @generated
	 */
	private static String getImageRegistryKey(ENamedElement element) {
		return element.getName();
	}

	/**
	 * @generated
	 */
	private static ImageDescriptor getProvidedImageDescriptor(
			ENamedElement element) {
		if (element instanceof EStructuralFeature) {
			element = ((EStructuralFeature) element).getEContainingClass();
		}
		if (element instanceof EClass) {
			EClass eClass = (EClass) element;
			if (!eClass.isAbstract())
				return TigerstripeDiagramEditorPlugin.getInstance()
						.getItemImageDescriptor(
								eClass.getEPackage().getEFactoryInstance()
										.create(eClass));
		}
		// TODO : support structural features
		return null;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(ENamedElement element) {
		String key = getImageRegistryKey(element);
		ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
		if (imageDescriptor == null) {
			imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
		}
		return imageDescriptor;
	}

	/**
	 * @generated
	 */
	public static Image getImage(ENamedElement element) {
		
		String key = getImageRegistryKey(element);
		Image image = getImageRegistry().get(key);
		if (image == null) {
			ImageDescriptor imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
			image = getImageRegistry().get(key);
		}
		return image;
	}

	
	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null)
			return null;
		return getImageDescriptor(element);
	}

	/**
	 * @generated NOT
	 */
	public static Image getImage(CustomElementType custom){
		String key = custom.getDisplayName();
		Image image = getImageRegistry().get(key);

		if (image == null) {
			ImageDescriptor imageDescriptor = custom.getPattern().getImageDescriptor();
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}

			getImageRegistry().put(key, imageDescriptor.createImage());
			image = getImageRegistry().get(key);
		}
		return image;
	}

	/**
	 * @generated NOT
	 */
	public static Image getImage(IAdaptable hint) {
		if (hint instanceof CustomElementType){
			return getImage((CustomElementType) hint);
		}
		ENamedElement element = getElement(hint);
		if (element == null)
			return null;
		return getImage(element);
	}

	/**
	 * Returns 'type' of the ecore object associated with the hint.
	 * 
	 * @generated
	 */
	public static ENamedElement getElement(IAdaptable hint) {
		Object type = hint.getAdapter(IElementType.class);
		if (elements == null) {
			elements = new IdentityHashMap();
			elements.put(Map_79, VisualeditorPackage.eINSTANCE.getMap());
			elements.put(Attribute_2001, VisualeditorPackage.eINSTANCE
					.getAttribute());
			elements.put(Attribute_2002, VisualeditorPackage.eINSTANCE
					.getAttribute());
			elements.put(Attribute_2003, VisualeditorPackage.eINSTANCE
					.getAttribute());
			elements
					.put(Method_2004, VisualeditorPackage.eINSTANCE.getMethod());
			elements.put(Attribute_2005, VisualeditorPackage.eINSTANCE
					.getAttribute());
			elements.put(Attribute_2006, VisualeditorPackage.eINSTANCE
					.getAttribute());
			elements
					.put(Method_2007, VisualeditorPackage.eINSTANCE.getMethod());
			elements.put(Literal_2008, VisualeditorPackage.eINSTANCE
					.getLiteral());
			elements.put(Attribute_2009, VisualeditorPackage.eINSTANCE
					.getAttribute());
			elements
					.put(Method_2010, VisualeditorPackage.eINSTANCE.getMethod());
			elements.put(Attribute_2011, VisualeditorPackage.eINSTANCE
					.getAttribute());
			elements
					.put(Method_2012, VisualeditorPackage.eINSTANCE.getMethod());
			elements.put(NamedQueryArtifact_1001, VisualeditorPackage.eINSTANCE
					.getNamedQueryArtifact());
			elements.put(ExceptionArtifact_1002, VisualeditorPackage.eINSTANCE
					.getExceptionArtifact());
			elements.put(ManagedEntityArtifact_1003,
					VisualeditorPackage.eINSTANCE.getManagedEntityArtifact());
			elements.put(NotificationArtifact_1004,
					VisualeditorPackage.eINSTANCE.getNotificationArtifact());
			elements.put(DatatypeArtifact_1005, VisualeditorPackage.eINSTANCE
					.getDatatypeArtifact());
			elements.put(Enumeration_1006, VisualeditorPackage.eINSTANCE
					.getEnumeration());
			elements.put(UpdateProcedureArtifact_1007,
					VisualeditorPackage.eINSTANCE.getUpdateProcedureArtifact());
			elements.put(SessionFacadeArtifact_1008,
					VisualeditorPackage.eINSTANCE.getSessionFacadeArtifact());
			elements.put(AssociationClassClass_1009,
					VisualeditorPackage.eINSTANCE.getAssociationClassClass());
			elements.put(Association_3001, VisualeditorPackage.eINSTANCE
					.getAssociation());
			elements.put(SessionFacadeArtifactEmittedNotifications_3002,
					VisualeditorPackage.eINSTANCE
							.getSessionFacadeArtifact_EmittedNotifications());
			elements.put(SessionFacadeArtifactManagedEntities_3003,
					VisualeditorPackage.eINSTANCE
							.getSessionFacadeArtifact_ManagedEntities());
			elements.put(NamedQueryArtifactReturnedType_3004,
					VisualeditorPackage.eINSTANCE
							.getNamedQueryArtifact_ReturnedType());
			elements.put(SessionFacadeArtifactNamedQueries_3005,
					VisualeditorPackage.eINSTANCE
							.getSessionFacadeArtifact_NamedQueries());
			elements.put(SessionFacadeArtifactExposedProcedures_3006,
					VisualeditorPackage.eINSTANCE
							.getSessionFacadeArtifact_ExposedProcedures());
			elements
					.put(AbstractArtifactExtends_3007,
							VisualeditorPackage.eINSTANCE
									.getAbstractArtifact_Extends());
			elements.put(Dependency_3008, VisualeditorPackage.eINSTANCE
					.getDependency());
			elements.put(Reference_3009, VisualeditorPackage.eINSTANCE
					.getReference());
			elements.put(AssociationClass_3010, VisualeditorPackage.eINSTANCE
					.getAssociationClass());
			elements.put(AssociationClassAssociatedClass_3011,
					VisualeditorPackage.eINSTANCE
							.getAssociationClass_AssociatedClass());
			elements.put(AbstractArtifactImplements_3012,
					VisualeditorPackage.eINSTANCE
							.getAbstractArtifact_Implements());
		}
		return (ENamedElement) elements.get(type);
	}

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

	/**
	 * @generated
	 */
	private static Set KNOWN_ELEMENT_TYPES;

	/**
	 * @generated
	 */
	public static boolean isKnownElementType(IElementType elementType) {
		//System.out.println("Is Known? "+elementType.getDisplayName());
		if (KNOWN_ELEMENT_TYPES == null) {
			KNOWN_ELEMENT_TYPES = new HashSet();
			KNOWN_ELEMENT_TYPES.add(Map_79);
			KNOWN_ELEMENT_TYPES.add(Attribute_2001);
			KNOWN_ELEMENT_TYPES.add(Attribute_2002);
			KNOWN_ELEMENT_TYPES.add(Attribute_2003);
			KNOWN_ELEMENT_TYPES.add(Method_2004);
			KNOWN_ELEMENT_TYPES.add(Attribute_2005);
			KNOWN_ELEMENT_TYPES.add(Attribute_2006);
			KNOWN_ELEMENT_TYPES.add(Method_2007);
			KNOWN_ELEMENT_TYPES.add(Literal_2008);
			KNOWN_ELEMENT_TYPES.add(Attribute_2009);
			KNOWN_ELEMENT_TYPES.add(Method_2010);
			KNOWN_ELEMENT_TYPES.add(Attribute_2011);
			KNOWN_ELEMENT_TYPES.add(Method_2012);
			KNOWN_ELEMENT_TYPES.add(NamedQueryArtifact_1001);
			KNOWN_ELEMENT_TYPES.add(ExceptionArtifact_1002);
			KNOWN_ELEMENT_TYPES.add(ManagedEntityArtifact_1003);
			KNOWN_ELEMENT_TYPES.add(NotificationArtifact_1004);
			KNOWN_ELEMENT_TYPES.add(DatatypeArtifact_1005);
			KNOWN_ELEMENT_TYPES.add(Enumeration_1006);
			KNOWN_ELEMENT_TYPES.add(UpdateProcedureArtifact_1007);
			KNOWN_ELEMENT_TYPES.add(SessionFacadeArtifact_1008);
			KNOWN_ELEMENT_TYPES.add(AssociationClassClass_1009);
			KNOWN_ELEMENT_TYPES.add(Association_3001);
			KNOWN_ELEMENT_TYPES
					.add(SessionFacadeArtifactEmittedNotifications_3002);
			KNOWN_ELEMENT_TYPES.add(SessionFacadeArtifactManagedEntities_3003);
			KNOWN_ELEMENT_TYPES.add(NamedQueryArtifactReturnedType_3004);
			KNOWN_ELEMENT_TYPES.add(SessionFacadeArtifactNamedQueries_3005);
			KNOWN_ELEMENT_TYPES
					.add(SessionFacadeArtifactExposedProcedures_3006);
			KNOWN_ELEMENT_TYPES.add(AbstractArtifactExtends_3007);
			KNOWN_ELEMENT_TYPES.add(Dependency_3008);
			KNOWN_ELEMENT_TYPES.add(Reference_3009);
			KNOWN_ELEMENT_TYPES.add(AssociationClass_3010);
			KNOWN_ELEMENT_TYPES.add(AssociationClassAssociatedClass_3011);
			KNOWN_ELEMENT_TYPES.add(AbstractArtifactImplements_3012);
		}
		return KNOWN_ELEMENT_TYPES.contains(elementType);
	}
	
	public static IHintedType getCustomType(IHintedType baseType, IArtifactPattern pattern){
		
		IHintedType noo = new CustomElementType((IHintedType) baseType,pattern);
		if (KNOWN_ELEMENT_TYPES == null){
			// Do this to make sure the next line doesn't prodiuce an NullPE
			isKnownElementType(baseType);
		}
		KNOWN_ELEMENT_TYPES.add(noo);
		if (elements == null){
			// Do this to make sure the next line doesn't prodiuce an NullPE
			getElement((IElementType) baseType);
		}
		elements.put(noo, VisualeditorPackage.eINSTANCE
				.getReference());
		return noo;
	}
}
