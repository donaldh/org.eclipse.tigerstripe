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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class VisualeditorPackageImpl extends EPackageImpl implements
		VisualeditorPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass namedElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass qualifiedNamedElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass abstractArtifactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass managedEntityArtifactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass datatypeArtifactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass notificationArtifactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass namedQueryArtifactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass enumerationEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass updateProcedureArtifactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass exceptionArtifactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass sessionFacadeArtifactEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass associationEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass associationClassEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass typedElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass attributeEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass methodEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass literalEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass parameterEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass mapEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass referenceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass dependencyEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass associationClassClassEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass diagramPropertyEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum aggregationEnumEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum changeableEnumEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum assocMultiplicityEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum typeMultiplicityEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum visibilityEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory
	 * method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private VisualeditorPackageImpl() {
		super(eNS_URI, VisualeditorFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model,
	 * and for any others upon which it depends. Simple dependencies are
	 * satisfied by calling this method on all dependent packages before doing
	 * anything else. This method drives initialization for interdependent
	 * packages directly, in parallel with this package, itself.
	 * <p>
	 * Of this package and its interdependencies, all packages which have not
	 * yet been registered by their URI values are first created and registered.
	 * The packages are then initialized in two steps: meta-model objects for
	 * all of the packages are created before any are initialized, since one
	 * package's meta-model objects may refer to those of another.
	 * <p>
	 * Invocation of this method will not affect any packages that have already
	 * been initialized. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static VisualeditorPackage init() {
		if (isInited)
			return (VisualeditorPackage) EPackage.Registry.INSTANCE
					.getEPackage(VisualeditorPackage.eNS_URI);

		// Obtain or create and register package
		VisualeditorPackageImpl theVisualeditorPackage = (VisualeditorPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI) instanceof VisualeditorPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI)
				: new VisualeditorPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theVisualeditorPackage.createPackageContents();

		// Initialize created meta-data
		theVisualeditorPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theVisualeditorPackage.freeze();

		return theVisualeditorPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getNamedElement() {
		return namedElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getNamedElement_Name() {
		return (EAttribute) namedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getNamedElement_Stereotypes() {
		return (EAttribute) namedElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getNamedElement_Properties() {
		return (EReference) namedElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getQualifiedNamedElement() {
		return qualifiedNamedElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getQualifiedNamedElement_Package() {
		return (EAttribute) qualifiedNamedElementEClass
				.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getQualifiedNamedElement_IsReadonly() {
		return (EAttribute) qualifiedNamedElementEClass
				.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getQualifiedNamedElement_IsAbstract() {
		return (EAttribute) qualifiedNamedElementEClass
				.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAbstractArtifact() {
		return abstractArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAbstractArtifact_Extends() {
		return (EReference) abstractArtifactEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAbstractArtifact_Attributes() {
		return (EReference) abstractArtifactEClass.getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAbstractArtifact_Literals() {
		return (EReference) abstractArtifactEClass.getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAbstractArtifact_Methods() {
		return (EReference) abstractArtifactEClass.getEStructuralFeatures()
				.get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAbstractArtifact_References() {
		return (EReference) abstractArtifactEClass.getEStructuralFeatures()
				.get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAbstractArtifact_Implements() {
		return (EReference) abstractArtifactEClass.getEStructuralFeatures()
				.get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getManagedEntityArtifact() {
		return managedEntityArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDatatypeArtifact() {
		return datatypeArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getNotificationArtifact() {
		return notificationArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getNamedQueryArtifact() {
		return namedQueryArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getNamedQueryArtifact_ReturnedType() {
		return (EReference) namedQueryArtifactEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getEnumeration() {
		return enumerationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getEnumeration_BaseType() {
		return (EAttribute) enumerationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getUpdateProcedureArtifact() {
		return updateProcedureArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getExceptionArtifact() {
		return exceptionArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getSessionFacadeArtifact() {
		return sessionFacadeArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSessionFacadeArtifact_ManagedEntities() {
		return (EReference) sessionFacadeArtifactEClass
				.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSessionFacadeArtifact_EmittedNotifications() {
		return (EReference) sessionFacadeArtifactEClass
				.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSessionFacadeArtifact_NamedQueries() {
		return (EReference) sessionFacadeArtifactEClass
				.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getSessionFacadeArtifact_ExposedProcedures() {
		return (EReference) sessionFacadeArtifactEClass
				.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAssociation() {
		return associationEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAssociation_AEnd() {
		return (EReference) associationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_AEndName() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_AEndMultiplicity() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_AEndIsNavigable() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_AEndIsOrdered() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_AEndIsUnique() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_AEndIsChangeable() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_AEndAggregation() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAssociation_ZEnd() {
		return (EReference) associationEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_ZEndName() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_ZEndMultiplicity() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_ZEndIsNavigable() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_ZEndIsOrdered() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_ZEndIsUnique() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_ZEndIsChangeable() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_ZEndAggregation() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_AEndVisibility() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getAssociation_ZEndVisibility() {
		return (EAttribute) associationEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAssociationClass() {
		return associationClassEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getAssociationClass_AssociatedClass() {
		return (EReference) associationClassEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getTypedElement() {
		return typedElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTypedElement_Type() {
		return (EAttribute) typedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTypedElement_Multiplicity() {
		return (EAttribute) typedElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTypedElement_Visibility() {
		return (EAttribute) typedElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTypedElement_IsOrdered() {
		return (EAttribute) typedElementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTypedElement_IsUnique() {
		return (EAttribute) typedElementEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTypedElement_TypeMultiplicity() {
		return (EAttribute) typedElementEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getTypedElement_DefaultValue() {
		return (EAttribute) typedElementEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAttribute() {
		return attributeEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getMethod() {
		return methodEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMethod_Parameters() {
		return (EReference) methodEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getMethod_IsAbstract() {
		return (EAttribute) methodEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getLiteral() {
		return literalEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getLiteral_Value() {
		return (EAttribute) literalEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getParameter() {
		return parameterEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getMap() {
		return mapEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMap_Artifacts() {
		return (EReference) mapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMap_Associations() {
		return (EReference) mapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMap_Dependencies() {
		return (EReference) mapEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getMap_BasePackage() {
		return (EAttribute) mapEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getMap_Properties() {
		return (EReference) mapEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getReference() {
		return referenceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getReference_Multiplicity() {
		return (EAttribute) referenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getReference_ZEnd() {
		return (EReference) referenceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getReference_TypeMultiplicity() {
		return (EAttribute) referenceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getReference_IsOrdered() {
		return (EAttribute) referenceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getReference_IsUnique() {
		return (EAttribute) referenceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDependency() {
		return dependencyEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDependency_AEnd() {
		return (EReference) dependencyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDependency_AEndMultiplicity() {
		return (EAttribute) dependencyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDependency_AEndIsNavigable() {
		return (EAttribute) dependencyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getDependency_ZEnd() {
		return (EReference) dependencyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDependency_ZEndMultiplicity() {
		return (EAttribute) dependencyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDependency_ZEndIsNavigable() {
		return (EAttribute) dependencyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getAssociationClassClass() {
		return associationClassClassEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getDiagramProperty() {
		return diagramPropertyEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDiagramProperty_Name() {
		return (EAttribute) diagramPropertyEClass.getEStructuralFeatures().get(
				0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getDiagramProperty_Value() {
		return (EAttribute) diagramPropertyEClass.getEStructuralFeatures().get(
				1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getAggregationEnum() {
		return aggregationEnumEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getChangeableEnum() {
		return changeableEnumEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getAssocMultiplicity() {
		return assocMultiplicityEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getTypeMultiplicity() {
		return typeMultiplicityEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getVisibility() {
		return visibilityEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VisualeditorFactory getVisualeditorFactory() {
		return (VisualeditorFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to
	 * have no affect on any invocation but its first. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		namedElementEClass = createEClass(NAMED_ELEMENT);
		createEAttribute(namedElementEClass, NAMED_ELEMENT__NAME);
		createEAttribute(namedElementEClass, NAMED_ELEMENT__STEREOTYPES);
		createEReference(namedElementEClass, NAMED_ELEMENT__PROPERTIES);

		qualifiedNamedElementEClass = createEClass(QUALIFIED_NAMED_ELEMENT);
		createEAttribute(qualifiedNamedElementEClass,
				QUALIFIED_NAMED_ELEMENT__PACKAGE);
		createEAttribute(qualifiedNamedElementEClass,
				QUALIFIED_NAMED_ELEMENT__IS_READONLY);
		createEAttribute(qualifiedNamedElementEClass,
				QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT);

		abstractArtifactEClass = createEClass(ABSTRACT_ARTIFACT);
		createEReference(abstractArtifactEClass, ABSTRACT_ARTIFACT__EXTENDS);
		createEReference(abstractArtifactEClass, ABSTRACT_ARTIFACT__ATTRIBUTES);
		createEReference(abstractArtifactEClass, ABSTRACT_ARTIFACT__LITERALS);
		createEReference(abstractArtifactEClass, ABSTRACT_ARTIFACT__METHODS);
		createEReference(abstractArtifactEClass, ABSTRACT_ARTIFACT__REFERENCES);
		createEReference(abstractArtifactEClass, ABSTRACT_ARTIFACT__IMPLEMENTS);

		managedEntityArtifactEClass = createEClass(MANAGED_ENTITY_ARTIFACT);

		datatypeArtifactEClass = createEClass(DATATYPE_ARTIFACT);

		notificationArtifactEClass = createEClass(NOTIFICATION_ARTIFACT);

		namedQueryArtifactEClass = createEClass(NAMED_QUERY_ARTIFACT);
		createEReference(namedQueryArtifactEClass,
				NAMED_QUERY_ARTIFACT__RETURNED_TYPE);

		enumerationEClass = createEClass(ENUMERATION);
		createEAttribute(enumerationEClass, ENUMERATION__BASE_TYPE);

		updateProcedureArtifactEClass = createEClass(UPDATE_PROCEDURE_ARTIFACT);

		exceptionArtifactEClass = createEClass(EXCEPTION_ARTIFACT);

		sessionFacadeArtifactEClass = createEClass(SESSION_FACADE_ARTIFACT);
		createEReference(sessionFacadeArtifactEClass,
				SESSION_FACADE_ARTIFACT__MANAGED_ENTITIES);
		createEReference(sessionFacadeArtifactEClass,
				SESSION_FACADE_ARTIFACT__EMITTED_NOTIFICATIONS);
		createEReference(sessionFacadeArtifactEClass,
				SESSION_FACADE_ARTIFACT__NAMED_QUERIES);
		createEReference(sessionFacadeArtifactEClass,
				SESSION_FACADE_ARTIFACT__EXPOSED_PROCEDURES);

		associationEClass = createEClass(ASSOCIATION);
		createEReference(associationEClass, ASSOCIATION__AEND);
		createEAttribute(associationEClass, ASSOCIATION__AEND_NAME);
		createEAttribute(associationEClass, ASSOCIATION__AEND_MULTIPLICITY);
		createEAttribute(associationEClass, ASSOCIATION__AEND_IS_NAVIGABLE);
		createEAttribute(associationEClass, ASSOCIATION__AEND_IS_ORDERED);
		createEAttribute(associationEClass, ASSOCIATION__AEND_IS_UNIQUE);
		createEAttribute(associationEClass, ASSOCIATION__AEND_IS_CHANGEABLE);
		createEAttribute(associationEClass, ASSOCIATION__AEND_AGGREGATION);
		createEReference(associationEClass, ASSOCIATION__ZEND);
		createEAttribute(associationEClass, ASSOCIATION__ZEND_NAME);
		createEAttribute(associationEClass, ASSOCIATION__ZEND_MULTIPLICITY);
		createEAttribute(associationEClass, ASSOCIATION__ZEND_IS_NAVIGABLE);
		createEAttribute(associationEClass, ASSOCIATION__ZEND_IS_ORDERED);
		createEAttribute(associationEClass, ASSOCIATION__ZEND_IS_UNIQUE);
		createEAttribute(associationEClass, ASSOCIATION__ZEND_IS_CHANGEABLE);
		createEAttribute(associationEClass, ASSOCIATION__ZEND_AGGREGATION);
		createEAttribute(associationEClass, ASSOCIATION__AEND_VISIBILITY);
		createEAttribute(associationEClass, ASSOCIATION__ZEND_VISIBILITY);

		associationClassEClass = createEClass(ASSOCIATION_CLASS);
		createEReference(associationClassEClass,
				ASSOCIATION_CLASS__ASSOCIATED_CLASS);

		typedElementEClass = createEClass(TYPED_ELEMENT);
		createEAttribute(typedElementEClass, TYPED_ELEMENT__TYPE);
		createEAttribute(typedElementEClass, TYPED_ELEMENT__MULTIPLICITY);
		createEAttribute(typedElementEClass, TYPED_ELEMENT__VISIBILITY);
		createEAttribute(typedElementEClass, TYPED_ELEMENT__IS_ORDERED);
		createEAttribute(typedElementEClass, TYPED_ELEMENT__IS_UNIQUE);
		createEAttribute(typedElementEClass, TYPED_ELEMENT__TYPE_MULTIPLICITY);
		createEAttribute(typedElementEClass, TYPED_ELEMENT__DEFAULT_VALUE);

		attributeEClass = createEClass(ATTRIBUTE);

		methodEClass = createEClass(METHOD);
		createEReference(methodEClass, METHOD__PARAMETERS);
		createEAttribute(methodEClass, METHOD__IS_ABSTRACT);

		literalEClass = createEClass(LITERAL);
		createEAttribute(literalEClass, LITERAL__VALUE);

		parameterEClass = createEClass(PARAMETER);

		mapEClass = createEClass(MAP);
		createEReference(mapEClass, MAP__ARTIFACTS);
		createEReference(mapEClass, MAP__ASSOCIATIONS);
		createEReference(mapEClass, MAP__DEPENDENCIES);
		createEAttribute(mapEClass, MAP__BASE_PACKAGE);
		createEReference(mapEClass, MAP__PROPERTIES);

		referenceEClass = createEClass(REFERENCE);
		createEAttribute(referenceEClass, REFERENCE__MULTIPLICITY);
		createEReference(referenceEClass, REFERENCE__ZEND);
		createEAttribute(referenceEClass, REFERENCE__TYPE_MULTIPLICITY);
		createEAttribute(referenceEClass, REFERENCE__IS_ORDERED);
		createEAttribute(referenceEClass, REFERENCE__IS_UNIQUE);

		dependencyEClass = createEClass(DEPENDENCY);
		createEReference(dependencyEClass, DEPENDENCY__AEND);
		createEAttribute(dependencyEClass, DEPENDENCY__AEND_MULTIPLICITY);
		createEAttribute(dependencyEClass, DEPENDENCY__AEND_IS_NAVIGABLE);
		createEReference(dependencyEClass, DEPENDENCY__ZEND);
		createEAttribute(dependencyEClass, DEPENDENCY__ZEND_MULTIPLICITY);
		createEAttribute(dependencyEClass, DEPENDENCY__ZEND_IS_NAVIGABLE);

		associationClassClassEClass = createEClass(ASSOCIATION_CLASS_CLASS);

		diagramPropertyEClass = createEClass(DIAGRAM_PROPERTY);
		createEAttribute(diagramPropertyEClass, DIAGRAM_PROPERTY__NAME);
		createEAttribute(diagramPropertyEClass, DIAGRAM_PROPERTY__VALUE);

		// Create enums
		aggregationEnumEEnum = createEEnum(AGGREGATION_ENUM);
		changeableEnumEEnum = createEEnum(CHANGEABLE_ENUM);
		assocMultiplicityEEnum = createEEnum(ASSOC_MULTIPLICITY);
		typeMultiplicityEEnum = createEEnum(TYPE_MULTIPLICITY);
		visibilityEEnum = createEEnum(VISIBILITY);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Add supertypes to classes
		qualifiedNamedElementEClass.getESuperTypes()
				.add(this.getNamedElement());
		abstractArtifactEClass.getESuperTypes().add(
				this.getQualifiedNamedElement());
		managedEntityArtifactEClass.getESuperTypes().add(
				this.getAbstractArtifact());
		datatypeArtifactEClass.getESuperTypes().add(this.getAbstractArtifact());
		notificationArtifactEClass.getESuperTypes().add(
				this.getAbstractArtifact());
		namedQueryArtifactEClass.getESuperTypes().add(
				this.getAbstractArtifact());
		enumerationEClass.getESuperTypes().add(this.getAbstractArtifact());
		updateProcedureArtifactEClass.getESuperTypes().add(
				this.getAbstractArtifact());
		exceptionArtifactEClass.getESuperTypes()
				.add(this.getAbstractArtifact());
		sessionFacadeArtifactEClass.getESuperTypes().add(
				this.getAbstractArtifact());
		associationEClass.getESuperTypes().add(this.getQualifiedNamedElement());
		associationClassEClass.getESuperTypes().add(this.getAssociation());
		typedElementEClass.getESuperTypes().add(this.getNamedElement());
		attributeEClass.getESuperTypes().add(this.getTypedElement());
		methodEClass.getESuperTypes().add(this.getTypedElement());
		literalEClass.getESuperTypes().add(this.getTypedElement());
		parameterEClass.getESuperTypes().add(this.getTypedElement());
		referenceEClass.getESuperTypes().add(this.getNamedElement());
		dependencyEClass.getESuperTypes().add(this.getQualifiedNamedElement());
		associationClassClassEClass.getESuperTypes().add(
				this.getAbstractArtifact());

		// Initialize classes and features; add operations and parameters
		initEClass(namedElementEClass, NamedElement.class, "NamedElement",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedElement_Name(), ecorePackage.getEString(),
				"name", null, 0, 1, NamedElement.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getNamedElement_Stereotypes(),
				ecorePackage.getEString(), "stereotypes", null, 0, -1,
				NamedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNamedElement_Properties(), this.getDiagramProperty(),
				null, "properties", null, 0, -1, NamedElement.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		initEClass(qualifiedNamedElementEClass, QualifiedNamedElement.class,
				"QualifiedNamedElement", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getQualifiedNamedElement_Package(), ecorePackage
				.getEString(), "package", null, 0, 1,
				QualifiedNamedElement.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getQualifiedNamedElement_IsReadonly(), ecorePackage
				.getEBoolean(), "isReadonly", null, 0, 1,
				QualifiedNamedElement.class, !IS_TRANSIENT, !IS_VOLATILE,
				!IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getQualifiedNamedElement_IsAbstract(), ecorePackage
				.getEBoolean(), "isAbstract", null, 0, 1,
				QualifiedNamedElement.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		addEOperation(qualifiedNamedElementEClass, ecorePackage.getEString(),
				"getFullyQualifiedName", 0, 1);

		initEClass(abstractArtifactEClass, AbstractArtifact.class,
				"AbstractArtifact", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAbstractArtifact_Extends(), this
				.getAbstractArtifact(), null, "extends", null, 0, 1,
				AbstractArtifact.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractArtifact_Attributes(), this.getAttribute(),
				null, "attributes", null, 0, -1, AbstractArtifact.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getAbstractArtifact_Literals(), this.getLiteral(), null,
				"literals", null, 0, -1, AbstractArtifact.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractArtifact_Methods(), this.getMethod(), null,
				"methods", null, 0, -1, AbstractArtifact.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractArtifact_References(), this.getReference(),
				null, "references", null, 0, -1, AbstractArtifact.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getAbstractArtifact_Implements(), this
				.getAbstractArtifact(), null, "implements", null, 0, -1,
				AbstractArtifact.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(managedEntityArtifactEClass, ManagedEntityArtifact.class,
				"ManagedEntityArtifact", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(datatypeArtifactEClass, DatatypeArtifact.class,
				"DatatypeArtifact", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(notificationArtifactEClass, NotificationArtifact.class,
				"NotificationArtifact", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(namedQueryArtifactEClass, NamedQueryArtifact.class,
				"NamedQueryArtifact", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNamedQueryArtifact_ReturnedType(), this
				.getAbstractArtifact(), null, "returnedType", null, 0, 1,
				NamedQueryArtifact.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(enumerationEClass, Enumeration.class, "Enumeration",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEnumeration_BaseType(), ecorePackage.getEString(),
				"baseType", null, 0, 1, Enumeration.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(updateProcedureArtifactEClass,
				UpdateProcedureArtifact.class, "UpdateProcedureArtifact",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(exceptionArtifactEClass, ExceptionArtifact.class,
				"ExceptionArtifact", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(sessionFacadeArtifactEClass, SessionFacadeArtifact.class,
				"SessionFacadeArtifact", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSessionFacadeArtifact_ManagedEntities(), this
				.getManagedEntityArtifact(), null, "managedEntities", null, 0,
				-1, SessionFacadeArtifact.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSessionFacadeArtifact_EmittedNotifications(), this
				.getNotificationArtifact(), null, "emittedNotifications", null,
				0, -1, SessionFacadeArtifact.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSessionFacadeArtifact_NamedQueries(), this
				.getNamedQueryArtifact(), null, "namedQueries", null, 0, -1,
				SessionFacadeArtifact.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSessionFacadeArtifact_ExposedProcedures(), this
				.getUpdateProcedureArtifact(), null, "exposedProcedures", null,
				0, -1, SessionFacadeArtifact.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(associationEClass, Association.class, "Association",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAssociation_AEnd(), this.getAbstractArtifact(), null,
				"aEnd", null, 0, 1, Association.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_AEndName(), ecorePackage.getEString(),
				"aEndName", null, 0, 1, Association.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_AEndMultiplicity(), this
				.getAssocMultiplicity(), "aEndMultiplicity", null, 0, 1,
				Association.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_AEndIsNavigable(), ecorePackage
				.getEBoolean(), "aEndIsNavigable", null, 0, 1,
				Association.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_AEndIsOrdered(), ecorePackage
				.getEBoolean(), "aEndIsOrdered", null, 0, 1, Association.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_AEndIsUnique(), ecorePackage
				.getEBoolean(), "aEndIsUnique", null, 0, 1, Association.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_AEndIsChangeable(), this
				.getChangeableEnum(), "aEndIsChangeable", null, 0, 1,
				Association.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_AEndAggregation(), this
				.getAggregationEnum(), "aEndAggregation", null, 0, 1,
				Association.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssociation_ZEnd(), this.getAbstractArtifact(), null,
				"zEnd", null, 0, 1, Association.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_ZEndName(), ecorePackage.getEString(),
				"zEndName", null, 0, 1, Association.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_ZEndMultiplicity(), this
				.getAssocMultiplicity(), "zEndMultiplicity", null, 0, 1,
				Association.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_ZEndIsNavigable(), ecorePackage
				.getEBoolean(), "zEndIsNavigable", null, 0, 1,
				Association.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_ZEndIsOrdered(), ecorePackage
				.getEBoolean(), "zEndIsOrdered", null, 0, 1, Association.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_ZEndIsUnique(), ecorePackage
				.getEBoolean(), "zEndIsUnique", null, 0, 1, Association.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_ZEndIsChangeable(), this
				.getChangeableEnum(), "zEndIsChangeable", null, 0, 1,
				Association.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_ZEndAggregation(), this
				.getAggregationEnum(), "zEndAggregation", null, 0, 1,
				Association.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_AEndVisibility(), this.getVisibility(),
				"aEndVisibility", null, 0, 1, Association.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociation_ZEndVisibility(), this.getVisibility(),
				"zEndVisibility", null, 0, 1, Association.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(associationClassEClass, AssociationClass.class,
				"AssociationClass", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAssociationClass_AssociatedClass(), this
				.getAssociationClassClass(), null, "associatedClass", null, 0,
				1, AssociationClass.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(typedElementEClass, TypedElement.class, "TypedElement",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTypedElement_Type(), ecorePackage.getEString(),
				"type", null, 0, 1, TypedElement.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getTypedElement_Multiplicity(), this
				.getTypeMultiplicity(), "multiplicity", null, 0, 1,
				TypedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTypedElement_Visibility(), this.getVisibility(),
				"visibility", null, 0, 1, TypedElement.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getTypedElement_IsOrdered(), ecorePackage.getEBoolean(),
				"isOrdered", null, 0, 1, TypedElement.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getTypedElement_IsUnique(), ecorePackage.getEBoolean(),
				"isUnique", null, 0, 1, TypedElement.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getTypedElement_TypeMultiplicity(), this
				.getAssocMultiplicity(), "typeMultiplicity", null, 0, 1,
				TypedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTypedElement_DefaultValue(), ecorePackage
				.getEString(), "defaultValue", null, 0, 1, TypedElement.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(attributeEClass, Attribute.class, "Attribute", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(methodEClass, Method.class, "Method", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMethod_Parameters(), this.getParameter(), null,
				"parameters", null, 0, -1, Method.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMethod_IsAbstract(), ecorePackage.getEBoolean(),
				"isAbstract", null, 0, 1, Method.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(literalEClass, Literal.class, "Literal", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLiteral_Value(), ecorePackage.getEString(), "value",
				null, 0, 1, Literal.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		initEClass(parameterEClass, Parameter.class, "Parameter", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(mapEClass, Map.class, "Map", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMap_Artifacts(), this.getAbstractArtifact(), null,
				"artifacts", null, 0, -1, Map.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMap_Associations(), this.getAssociation(), null,
				"associations", null, 0, -1, Map.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMap_Dependencies(), this.getDependency(), null,
				"dependencies", null, 0, -1, Map.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMap_BasePackage(), ecorePackage.getEString(),
				"basePackage", null, 0, 1, Map.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getMap_Properties(), this.getDiagramProperty(), null,
				"properties", null, 0, -1, Map.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(referenceEClass, Reference.class, "Reference", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getReference_Multiplicity(), this.getTypeMultiplicity(),
				"multiplicity", null, 0, 1, Reference.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getReference_ZEnd(), this.getAbstractArtifact(), null,
				"zEnd", null, 0, 1, Reference.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_TypeMultiplicity(), this
				.getAssocMultiplicity(), "typeMultiplicity", null, 0, 1,
				Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_IsOrdered(), ecorePackage.getEBoolean(),
				"isOrdered", null, 0, 1, Reference.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_IsUnique(), ecorePackage.getEBoolean(),
				"isUnique", null, 0, 1, Reference.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(dependencyEClass, Dependency.class, "Dependency",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDependency_AEnd(), this.getAbstractArtifact(), null,
				"aEnd", null, 0, 1, Dependency.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDependency_AEndMultiplicity(), this
				.getAssocMultiplicity(), "aEndMultiplicity", null, 0, 1,
				Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDependency_AEndIsNavigable(), ecorePackage
				.getEBoolean(), "aEndIsNavigable", null, 0, 1,
				Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDependency_ZEnd(), this.getAbstractArtifact(), null,
				"zEnd", null, 0, 1, Dependency.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDependency_ZEndMultiplicity(), this
				.getAssocMultiplicity(), "zEndMultiplicity", null, 0, 1,
				Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDependency_ZEndIsNavigable(), ecorePackage
				.getEBoolean(), "zEndIsNavigable", null, 0, 1,
				Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(associationClassClassEClass, AssociationClassClass.class,
				"AssociationClassClass", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		initEClass(diagramPropertyEClass, DiagramProperty.class,
				"DiagramProperty", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDiagramProperty_Name(), ecorePackage.getEString(),
				"name", null, 0, 1, DiagramProperty.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiagramProperty_Value(), ecorePackage.getEString(),
				"value", null, 0, 1, DiagramProperty.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(aggregationEnumEEnum, AggregationEnum.class,
				"AggregationEnum");
		addEEnumLiteral(aggregationEnumEEnum, AggregationEnum.NONE_LITERAL);
		addEEnumLiteral(aggregationEnumEEnum, AggregationEnum.SHARED_LITERAL);
		addEEnumLiteral(aggregationEnumEEnum, AggregationEnum.COMPOSITE_LITERAL);

		initEEnum(changeableEnumEEnum, ChangeableEnum.class, "ChangeableEnum");
		addEEnumLiteral(changeableEnumEEnum, ChangeableEnum.NONE_LITERAL);
		addEEnumLiteral(changeableEnumEEnum, ChangeableEnum.FROZEN_LITERAL);
		addEEnumLiteral(changeableEnumEEnum, ChangeableEnum.ADD_ONLY_LITERAL);

		initEEnum(assocMultiplicityEEnum, AssocMultiplicity.class,
				"AssocMultiplicity");
		addEEnumLiteral(assocMultiplicityEEnum, AssocMultiplicity.ONE_LITERAL);
		addEEnumLiteral(assocMultiplicityEEnum, AssocMultiplicity.ZERO_LITERAL);
		addEEnumLiteral(assocMultiplicityEEnum,
				AssocMultiplicity.ZERO_ONE_LITERAL);
		addEEnumLiteral(assocMultiplicityEEnum,
				AssocMultiplicity.ZERO_STAR_LITERAL);
		addEEnumLiteral(assocMultiplicityEEnum,
				AssocMultiplicity.ONE_STAR_LITERAL);
		addEEnumLiteral(assocMultiplicityEEnum, AssocMultiplicity.STAR_LITERAL);

		initEEnum(typeMultiplicityEEnum, TypeMultiplicity.class,
				"TypeMultiplicity");
		addEEnumLiteral(typeMultiplicityEEnum, TypeMultiplicity.NONE_LITERAL);
		addEEnumLiteral(typeMultiplicityEEnum, TypeMultiplicity.ARRAY_LITERAL);
		addEEnumLiteral(typeMultiplicityEEnum,
				TypeMultiplicity.ARRAYOFARRAY_LITERAL);

		initEEnum(visibilityEEnum, Visibility.class, "Visibility");
		addEEnumLiteral(visibilityEEnum, Visibility.PUBLIC_LITERAL);
		addEEnumLiteral(visibilityEEnum, Visibility.PROTECTED_LITERAL);
		addEEnumLiteral(visibilityEEnum, Visibility.PRIVATE_LITERAL);
		addEEnumLiteral(visibilityEEnum, Visibility.PACKAGE_LITERAL);

		// Create resource
		createResource(eNS_URI);
	}

} // VisualeditorPackageImpl
