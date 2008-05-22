/**
 * <copyright>
 * </copyright>
 *
 * $Id: MetamodelPackageImpl.java,v 1.3 2008/05/22 18:26:28 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.tigerstripe.metamodel.EAggregationEnum;
import org.eclipse.tigerstripe.metamodel.EChangeableEnum;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IArgument;
import org.eclipse.tigerstripe.metamodel.IAssociationArtifact;
import org.eclipse.tigerstripe.metamodel.IAssociationClassArtifact;
import org.eclipse.tigerstripe.metamodel.IAssociationEnd;
import org.eclipse.tigerstripe.metamodel.IDatatypeArtifact;
import org.eclipse.tigerstripe.metamodel.IDependencyArtifact;
import org.eclipse.tigerstripe.metamodel.IEnumArtifact;
import org.eclipse.tigerstripe.metamodel.IEventArtifact;
import org.eclipse.tigerstripe.metamodel.IExceptionArtifact;
import org.eclipse.tigerstripe.metamodel.IField;
import org.eclipse.tigerstripe.metamodel.ILiteral;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.IMethod;
import org.eclipse.tigerstripe.metamodel.IModel;
import org.eclipse.tigerstripe.metamodel.IModelComponent;
import org.eclipse.tigerstripe.metamodel.IMultiplicity;
import org.eclipse.tigerstripe.metamodel.IPackage;
import org.eclipse.tigerstripe.metamodel.IPrimitiveType;
import org.eclipse.tigerstripe.metamodel.IQualifiedNamedComponent;
import org.eclipse.tigerstripe.metamodel.IQueryArtifact;
import org.eclipse.tigerstripe.metamodel.ISessionArtifact;
import org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue;
import org.eclipse.tigerstripe.metamodel.IStereotypeCapable;
import org.eclipse.tigerstripe.metamodel.IStereotypeInstance;
import org.eclipse.tigerstripe.metamodel.IType;
import org.eclipse.tigerstripe.metamodel.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelFactory;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;
import org.eclipse.tigerstripe.metamodel.VisibilityEnum;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MetamodelPackageImpl extends EPackageImpl implements MetamodelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iAbstractArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iPrimitiveTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iManagedEntityArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iDatatypeArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iExceptionArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iSessionArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iQueryArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iUpdateProcedureArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iEventArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iAssociationArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iAssociationClassArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iDependencyArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iEnumArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iFieldEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iMethodEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iLiteralEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iModelComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iQualifiedNamedComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iAssociationEndEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iArgumentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iPackageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iStereotypeCapableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iStereotypeInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iStereotypeAttributeValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iMultiplicityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum visibilityEnumEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eAggregationEnumEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eChangeableEnumEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MetamodelPackageImpl() {
		super(eNS_URI, MetamodelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MetamodelPackage init() {
		if (isInited) return (MetamodelPackage)EPackage.Registry.INSTANCE.getEPackage(MetamodelPackage.eNS_URI);

		// Obtain or create and register package
		MetamodelPackageImpl theMetamodelPackage = (MetamodelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof MetamodelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new MetamodelPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theMetamodelPackage.createPackageContents();

		// Initialize created meta-data
		theMetamodelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMetamodelPackage.freeze();

		return theMetamodelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIAbstractArtifact() {
		return iAbstractArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAbstractArtifact_Fields() {
		return (EReference)iAbstractArtifactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAbstractArtifact_Methods() {
		return (EReference)iAbstractArtifactEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAbstractArtifact_Literals() {
		return (EReference)iAbstractArtifactEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIAbstractArtifact_Abstract() {
		return (EAttribute)iAbstractArtifactEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAbstractArtifact_ExtendedArtifact() {
		return (EReference)iAbstractArtifactEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAbstractArtifact_ImplementedArtifacts() {
		return (EReference)iAbstractArtifactEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAbstractArtifact_ExtendingArtifacts() {
		return (EReference)iAbstractArtifactEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAbstractArtifact_ImplementingArtifacts() {
		return (EReference)iAbstractArtifactEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPrimitiveType() {
		return iPrimitiveTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIManagedEntityArtifact() {
		return iManagedEntityArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIManagedEntityArtifact_PrimaryKey() {
		return (EAttribute)iManagedEntityArtifactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIDatatypeArtifact() {
		return iDatatypeArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIExceptionArtifact() {
		return iExceptionArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getISessionArtifact() {
		return iSessionArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getISessionArtifact_ManagedEntities() {
		return (EReference)iSessionArtifactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getISessionArtifact_EmittedNotifications() {
		return (EReference)iSessionArtifactEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getISessionArtifact_SupportedNamedQueries() {
		return (EReference)iSessionArtifactEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getISessionArtifact_ExposedUpdateProcedures() {
		return (EReference)iSessionArtifactEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIQueryArtifact() {
		return iQueryArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIQueryArtifact_ReturnedType() {
		return (EReference)iQueryArtifactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIUpdateProcedureArtifact() {
		return iUpdateProcedureArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIEventArtifact() {
		return iEventArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIAssociationArtifact() {
		return iAssociationArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAssociationArtifact_AEnd() {
		return (EReference)iAssociationArtifactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAssociationArtifact_ZEnd() {
		return (EReference)iAssociationArtifactEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIAssociationClassArtifact() {
		return iAssociationClassArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIDependencyArtifact() {
		return iDependencyArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIDependencyArtifact_AEndType() {
		return (EReference)iDependencyArtifactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIDependencyArtifact_ZEndType() {
		return (EReference)iDependencyArtifactEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIEnumArtifact() {
		return iEnumArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIEnumArtifact_BaseType() {
		return (EAttribute)iEnumArtifactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIField() {
		return iFieldEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIField_ReadOnly() {
		return (EAttribute)iFieldEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIField_Ordered() {
		return (EAttribute)iFieldEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIField_Unique() {
		return (EAttribute)iFieldEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIField_Type() {
		return (EReference)iFieldEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIField_DefaultValue() {
		return (EAttribute)iFieldEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIMethod() {
		return iMethodEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIMethod_Arguments() {
		return (EReference)iMethodEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIMethod_ReturnType() {
		return (EReference)iMethodEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIMethod_Abstract() {
		return (EAttribute)iMethodEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIMethod_Ordered() {
		return (EAttribute)iMethodEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIMethod_Unique() {
		return (EAttribute)iMethodEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIMethod_Exceptions() {
		return (EReference)iMethodEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIMethod_Void() {
		return (EAttribute)iMethodEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIMethod_DefaultReturnValue() {
		return (EAttribute)iMethodEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIMethod_MethodReturnName() {
		return (EAttribute)iMethodEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIMethod_ReturnStereotypeInstances() {
		return (EReference)iMethodEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getILiteral() {
		return iLiteralEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getILiteral_Value() {
		return (EAttribute)iLiteralEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getILiteral_Type() {
		return (EReference)iLiteralEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIModelComponent() {
		return iModelComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIModelComponent_Name() {
		return (EAttribute)iModelComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIModelComponent_Comment() {
		return (EAttribute)iModelComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIModelComponent_Visibility() {
		return (EAttribute)iModelComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIQualifiedNamedComponent() {
		return iQualifiedNamedComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIQualifiedNamedComponent_Package() {
		return (EAttribute)iQualifiedNamedComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIType() {
		return iTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIType_FullyQualifiedName() {
		return (EAttribute)iTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIType_Multiplicity() {
		return (EReference)iTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIAssociationEnd() {
		return iAssociationEndEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIAssociationEnd_Aggregation() {
		return (EAttribute)iAssociationEndEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIAssociationEnd_Changeable() {
		return (EAttribute)iAssociationEndEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIAssociationEnd_Navigable() {
		return (EAttribute)iAssociationEndEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIAssociationEnd_Ordered() {
		return (EAttribute)iAssociationEndEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIAssociationEnd_Unique() {
		return (EAttribute)iAssociationEndEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAssociationEnd_Multiplicity() {
		return (EReference)iAssociationEndEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIAssociationEnd_Type() {
		return (EReference)iAssociationEndEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIArgument() {
		return iArgumentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIArgument_Type() {
		return (EReference)iArgumentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIArgument_DefaultValue() {
		return (EAttribute)iArgumentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIArgument_Ordered() {
		return (EAttribute)iArgumentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIArgument_Unique() {
		return (EAttribute)iArgumentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIModel() {
		return iModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIModel_Packages() {
		return (EReference)iModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIPackage() {
		return iPackageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIPackage_Artifacts() {
		return (EReference)iPackageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIStereotypeCapable() {
		return iStereotypeCapableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIStereotypeCapable_StereotypeInstances() {
		return (EReference)iStereotypeCapableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIStereotypeInstance() {
		return iStereotypeInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIStereotypeInstance_Name() {
		return (EAttribute)iStereotypeInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIStereotypeInstance_AttributeValues() {
		return (EReference)iStereotypeInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIStereotypeAttributeValue() {
		return iStereotypeAttributeValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIStereotypeAttributeValue_Name() {
		return (EAttribute)iStereotypeAttributeValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIStereotypeAttributeValue_Value() {
		return (EAttribute)iStereotypeAttributeValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIMultiplicity() {
		return iMultiplicityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIMultiplicity_LowerBound() {
		return (EAttribute)iMultiplicityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIMultiplicity_UpperBound() {
		return (EAttribute)iMultiplicityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getVisibilityEnum() {
		return visibilityEnumEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getEAggregationEnum() {
		return eAggregationEnumEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getEChangeableEnum() {
		return eChangeableEnumEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetamodelFactory getMetamodelFactory() {
		return (MetamodelFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		iAbstractArtifactEClass = createEClass(IABSTRACT_ARTIFACT);
		createEReference(iAbstractArtifactEClass, IABSTRACT_ARTIFACT__FIELDS);
		createEReference(iAbstractArtifactEClass, IABSTRACT_ARTIFACT__METHODS);
		createEReference(iAbstractArtifactEClass, IABSTRACT_ARTIFACT__LITERALS);
		createEAttribute(iAbstractArtifactEClass, IABSTRACT_ARTIFACT__ABSTRACT);
		createEReference(iAbstractArtifactEClass, IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT);
		createEReference(iAbstractArtifactEClass, IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS);
		createEReference(iAbstractArtifactEClass, IABSTRACT_ARTIFACT__EXTENDING_ARTIFACTS);
		createEReference(iAbstractArtifactEClass, IABSTRACT_ARTIFACT__IMPLEMENTING_ARTIFACTS);

		iPrimitiveTypeEClass = createEClass(IPRIMITIVE_TYPE);

		iManagedEntityArtifactEClass = createEClass(IMANAGED_ENTITY_ARTIFACT);
		createEAttribute(iManagedEntityArtifactEClass, IMANAGED_ENTITY_ARTIFACT__PRIMARY_KEY);

		iDatatypeArtifactEClass = createEClass(IDATATYPE_ARTIFACT);

		iExceptionArtifactEClass = createEClass(IEXCEPTION_ARTIFACT);

		iSessionArtifactEClass = createEClass(ISESSION_ARTIFACT);
		createEReference(iSessionArtifactEClass, ISESSION_ARTIFACT__MANAGED_ENTITIES);
		createEReference(iSessionArtifactEClass, ISESSION_ARTIFACT__EMITTED_NOTIFICATIONS);
		createEReference(iSessionArtifactEClass, ISESSION_ARTIFACT__SUPPORTED_NAMED_QUERIES);
		createEReference(iSessionArtifactEClass, ISESSION_ARTIFACT__EXPOSED_UPDATE_PROCEDURES);

		iQueryArtifactEClass = createEClass(IQUERY_ARTIFACT);
		createEReference(iQueryArtifactEClass, IQUERY_ARTIFACT__RETURNED_TYPE);

		iUpdateProcedureArtifactEClass = createEClass(IUPDATE_PROCEDURE_ARTIFACT);

		iEventArtifactEClass = createEClass(IEVENT_ARTIFACT);

		iAssociationArtifactEClass = createEClass(IASSOCIATION_ARTIFACT);
		createEReference(iAssociationArtifactEClass, IASSOCIATION_ARTIFACT__AEND);
		createEReference(iAssociationArtifactEClass, IASSOCIATION_ARTIFACT__ZEND);

		iAssociationClassArtifactEClass = createEClass(IASSOCIATION_CLASS_ARTIFACT);

		iDependencyArtifactEClass = createEClass(IDEPENDENCY_ARTIFACT);
		createEReference(iDependencyArtifactEClass, IDEPENDENCY_ARTIFACT__AEND_TYPE);
		createEReference(iDependencyArtifactEClass, IDEPENDENCY_ARTIFACT__ZEND_TYPE);

		iEnumArtifactEClass = createEClass(IENUM_ARTIFACT);
		createEAttribute(iEnumArtifactEClass, IENUM_ARTIFACT__BASE_TYPE);

		iFieldEClass = createEClass(IFIELD);
		createEAttribute(iFieldEClass, IFIELD__READ_ONLY);
		createEAttribute(iFieldEClass, IFIELD__ORDERED);
		createEAttribute(iFieldEClass, IFIELD__UNIQUE);
		createEReference(iFieldEClass, IFIELD__TYPE);
		createEAttribute(iFieldEClass, IFIELD__DEFAULT_VALUE);

		iMethodEClass = createEClass(IMETHOD);
		createEReference(iMethodEClass, IMETHOD__ARGUMENTS);
		createEReference(iMethodEClass, IMETHOD__RETURN_TYPE);
		createEAttribute(iMethodEClass, IMETHOD__ABSTRACT);
		createEAttribute(iMethodEClass, IMETHOD__ORDERED);
		createEAttribute(iMethodEClass, IMETHOD__UNIQUE);
		createEReference(iMethodEClass, IMETHOD__EXCEPTIONS);
		createEAttribute(iMethodEClass, IMETHOD__VOID);
		createEAttribute(iMethodEClass, IMETHOD__DEFAULT_RETURN_VALUE);
		createEAttribute(iMethodEClass, IMETHOD__METHOD_RETURN_NAME);
		createEReference(iMethodEClass, IMETHOD__RETURN_STEREOTYPE_INSTANCES);

		iLiteralEClass = createEClass(ILITERAL);
		createEAttribute(iLiteralEClass, ILITERAL__VALUE);
		createEReference(iLiteralEClass, ILITERAL__TYPE);

		iModelComponentEClass = createEClass(IMODEL_COMPONENT);
		createEAttribute(iModelComponentEClass, IMODEL_COMPONENT__NAME);
		createEAttribute(iModelComponentEClass, IMODEL_COMPONENT__COMMENT);
		createEAttribute(iModelComponentEClass, IMODEL_COMPONENT__VISIBILITY);

		iQualifiedNamedComponentEClass = createEClass(IQUALIFIED_NAMED_COMPONENT);
		createEAttribute(iQualifiedNamedComponentEClass, IQUALIFIED_NAMED_COMPONENT__PACKAGE);

		iTypeEClass = createEClass(ITYPE);
		createEAttribute(iTypeEClass, ITYPE__FULLY_QUALIFIED_NAME);
		createEReference(iTypeEClass, ITYPE__MULTIPLICITY);

		iAssociationEndEClass = createEClass(IASSOCIATION_END);
		createEAttribute(iAssociationEndEClass, IASSOCIATION_END__AGGREGATION);
		createEAttribute(iAssociationEndEClass, IASSOCIATION_END__CHANGEABLE);
		createEAttribute(iAssociationEndEClass, IASSOCIATION_END__NAVIGABLE);
		createEAttribute(iAssociationEndEClass, IASSOCIATION_END__ORDERED);
		createEAttribute(iAssociationEndEClass, IASSOCIATION_END__UNIQUE);
		createEReference(iAssociationEndEClass, IASSOCIATION_END__MULTIPLICITY);
		createEReference(iAssociationEndEClass, IASSOCIATION_END__TYPE);

		iArgumentEClass = createEClass(IARGUMENT);
		createEReference(iArgumentEClass, IARGUMENT__TYPE);
		createEAttribute(iArgumentEClass, IARGUMENT__DEFAULT_VALUE);
		createEAttribute(iArgumentEClass, IARGUMENT__ORDERED);
		createEAttribute(iArgumentEClass, IARGUMENT__UNIQUE);

		iModelEClass = createEClass(IMODEL);
		createEReference(iModelEClass, IMODEL__PACKAGES);

		iPackageEClass = createEClass(IPACKAGE);
		createEReference(iPackageEClass, IPACKAGE__ARTIFACTS);

		iStereotypeCapableEClass = createEClass(ISTEREOTYPE_CAPABLE);
		createEReference(iStereotypeCapableEClass, ISTEREOTYPE_CAPABLE__STEREOTYPE_INSTANCES);

		iStereotypeInstanceEClass = createEClass(ISTEREOTYPE_INSTANCE);
		createEAttribute(iStereotypeInstanceEClass, ISTEREOTYPE_INSTANCE__NAME);
		createEReference(iStereotypeInstanceEClass, ISTEREOTYPE_INSTANCE__ATTRIBUTE_VALUES);

		iStereotypeAttributeValueEClass = createEClass(ISTEREOTYPE_ATTRIBUTE_VALUE);
		createEAttribute(iStereotypeAttributeValueEClass, ISTEREOTYPE_ATTRIBUTE_VALUE__NAME);
		createEAttribute(iStereotypeAttributeValueEClass, ISTEREOTYPE_ATTRIBUTE_VALUE__VALUE);

		iMultiplicityEClass = createEClass(IMULTIPLICITY);
		createEAttribute(iMultiplicityEClass, IMULTIPLICITY__LOWER_BOUND);
		createEAttribute(iMultiplicityEClass, IMULTIPLICITY__UPPER_BOUND);

		// Create enums
		visibilityEnumEEnum = createEEnum(VISIBILITY_ENUM);
		eAggregationEnumEEnum = createEEnum(EAGGREGATION_ENUM);
		eChangeableEnumEEnum = createEEnum(ECHANGEABLE_ENUM);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		iAbstractArtifactEClass.getESuperTypes().add(this.getIQualifiedNamedComponent());
		iPrimitiveTypeEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iManagedEntityArtifactEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iDatatypeArtifactEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iExceptionArtifactEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iSessionArtifactEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iQueryArtifactEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iUpdateProcedureArtifactEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iEventArtifactEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iAssociationArtifactEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iAssociationClassArtifactEClass.getESuperTypes().add(this.getIAssociationArtifact());
		iAssociationClassArtifactEClass.getESuperTypes().add(this.getIManagedEntityArtifact());
		iDependencyArtifactEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iEnumArtifactEClass.getESuperTypes().add(this.getIAbstractArtifact());
		iFieldEClass.getESuperTypes().add(this.getIModelComponent());
		iMethodEClass.getESuperTypes().add(this.getIModelComponent());
		iLiteralEClass.getESuperTypes().add(this.getIModelComponent());
		iModelComponentEClass.getESuperTypes().add(this.getIStereotypeCapable());
		iQualifiedNamedComponentEClass.getESuperTypes().add(this.getIModelComponent());
		iAssociationEndEClass.getESuperTypes().add(this.getIModelComponent());
		iArgumentEClass.getESuperTypes().add(this.getIModelComponent());
		iModelEClass.getESuperTypes().add(this.getIModelComponent());
		iPackageEClass.getESuperTypes().add(this.getIModelComponent());

		// Initialize classes and features; add operations and parameters
		initEClass(iAbstractArtifactEClass, IAbstractArtifact.class, "IAbstractArtifact", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIAbstractArtifact_Fields(), this.getIField(), null, "fields", null, 0, -1, IAbstractArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIAbstractArtifact_Methods(), this.getIMethod(), null, "methods", null, 0, -1, IAbstractArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIAbstractArtifact_Literals(), this.getILiteral(), null, "literals", null, 0, -1, IAbstractArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIAbstractArtifact_Abstract(), ecorePackage.getEBoolean(), "abstract", null, 0, 1, IAbstractArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIAbstractArtifact_ExtendedArtifact(), this.getIAbstractArtifact(), this.getIAbstractArtifact_ExtendingArtifacts(), "extendedArtifact", null, 0, 1, IAbstractArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIAbstractArtifact_ImplementedArtifacts(), this.getIAbstractArtifact(), this.getIAbstractArtifact_ImplementingArtifacts(), "implementedArtifacts", null, 0, -1, IAbstractArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIAbstractArtifact_ExtendingArtifacts(), this.getIAbstractArtifact(), this.getIAbstractArtifact_ExtendedArtifact(), "extendingArtifacts", null, 0, -1, IAbstractArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIAbstractArtifact_ImplementingArtifacts(), this.getIAbstractArtifact(), this.getIAbstractArtifact_ImplementedArtifacts(), "implementingArtifacts", null, 0, -1, IAbstractArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(iAbstractArtifactEClass, this.getIAbstractArtifact(), "getAncestors", 0, -1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iAbstractArtifactEClass, this.getIField(), "getInheritedFields", 0, -1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iAbstractArtifactEClass, this.getIMethod(), "getInheritedMethods", 0, -1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iAbstractArtifactEClass, this.getIAbstractArtifact(), "getReferencedArtifacts", 0, -1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iAbstractArtifactEClass, ecorePackage.getEBoolean(), "hasExtends", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iPrimitiveTypeEClass, IPrimitiveType.class, "IPrimitiveType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(iManagedEntityArtifactEClass, IManagedEntityArtifact.class, "IManagedEntityArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIManagedEntityArtifact_PrimaryKey(), ecorePackage.getEString(), "primaryKey", null, 0, 1, IManagedEntityArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iDatatypeArtifactEClass, IDatatypeArtifact.class, "IDatatypeArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(iExceptionArtifactEClass, IExceptionArtifact.class, "IExceptionArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(iSessionArtifactEClass, ISessionArtifact.class, "ISessionArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getISessionArtifact_ManagedEntities(), this.getIManagedEntityArtifact(), null, "managedEntities", null, 0, -1, ISessionArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getISessionArtifact_EmittedNotifications(), this.getIEventArtifact(), null, "emittedNotifications", null, 0, -1, ISessionArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getISessionArtifact_SupportedNamedQueries(), this.getIQueryArtifact(), null, "supportedNamedQueries", null, 0, -1, ISessionArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getISessionArtifact_ExposedUpdateProcedures(), this.getIUpdateProcedureArtifact(), null, "exposedUpdateProcedures", null, 0, -1, ISessionArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iQueryArtifactEClass, IQueryArtifact.class, "IQueryArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIQueryArtifact_ReturnedType(), this.getIType(), null, "returnedType", null, 0, -1, IQueryArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iUpdateProcedureArtifactEClass, IUpdateProcedureArtifact.class, "IUpdateProcedureArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(iEventArtifactEClass, IEventArtifact.class, "IEventArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(iAssociationArtifactEClass, IAssociationArtifact.class, "IAssociationArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIAssociationArtifact_AEnd(), this.getIAssociationEnd(), null, "aEnd", null, 1, 1, IAssociationArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIAssociationArtifact_ZEnd(), this.getIAssociationEnd(), null, "zEnd", null, 1, 1, IAssociationArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(iAssociationArtifactEClass, this.getIAssociationEnd(), "getAssociationEnds", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(iAssociationClassArtifactEClass, IAssociationClassArtifact.class, "IAssociationClassArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(iDependencyArtifactEClass, IDependencyArtifact.class, "IDependencyArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIDependencyArtifact_AEndType(), this.getIType(), null, "aEndType", null, 1, 1, IDependencyArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIDependencyArtifact_ZEndType(), this.getIType(), null, "zEndType", null, 1, 1, IDependencyArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iEnumArtifactEClass, IEnumArtifact.class, "IEnumArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIEnumArtifact_BaseType(), ecorePackage.getEString(), "baseType", "int", 0, 1, IEnumArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(iEnumArtifactEClass, ecorePackage.getEString(), "getMaxLiteral", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iEnumArtifactEClass, ecorePackage.getEString(), "getMinLiteral", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iFieldEClass, IField.class, "IField", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIField_ReadOnly(), ecorePackage.getEBoolean(), "readOnly", null, 0, 1, IField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIField_Ordered(), ecorePackage.getEBoolean(), "ordered", null, 0, 1, IField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIField_Unique(), ecorePackage.getEBoolean(), "unique", null, 0, 1, IField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIField_Type(), this.getIType(), null, "type", null, 0, 1, IField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIField_DefaultValue(), ecorePackage.getEString(), "defaultValue", null, 0, 1, IField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iMethodEClass, IMethod.class, "IMethod", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIMethod_Arguments(), this.getIArgument(), null, "arguments", null, 0, -1, IMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIMethod_ReturnType(), this.getIType(), null, "returnType", null, 0, 1, IMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIMethod_Abstract(), ecorePackage.getEBoolean(), "abstract", null, 0, 1, IMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIMethod_Ordered(), ecorePackage.getEBoolean(), "ordered", null, 0, 1, IMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIMethod_Unique(), ecorePackage.getEBoolean(), "unique", null, 0, 1, IMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIMethod_Exceptions(), this.getIType(), null, "exceptions", null, 0, -1, IMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIMethod_Void(), ecorePackage.getEBoolean(), "void", null, 0, 1, IMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIMethod_DefaultReturnValue(), ecorePackage.getEString(), "defaultReturnValue", null, 0, 1, IMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIMethod_MethodReturnName(), ecorePackage.getEString(), "methodReturnName", null, 0, 1, IMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIMethod_ReturnStereotypeInstances(), this.getIStereotypeInstance(), null, "returnStereotypeInstances", null, 0, -1, IMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(iMethodEClass, ecorePackage.getEString(), "getMethodId", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iLiteralEClass, ILiteral.class, "ILiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getILiteral_Value(), ecorePackage.getEString(), "value", null, 0, 1, ILiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getILiteral_Type(), this.getIType(), null, "type", null, 0, 1, ILiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iModelComponentEClass, IModelComponent.class, "IModelComponent", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIModelComponent_Name(), ecorePackage.getEString(), "name", null, 0, 1, IModelComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIModelComponent_Comment(), ecorePackage.getEString(), "comment", null, 0, 1, IModelComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIModelComponent_Visibility(), this.getVisibilityEnum(), "visibility", null, 0, 1, IModelComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iQualifiedNamedComponentEClass, IQualifiedNamedComponent.class, "IQualifiedNamedComponent", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIQualifiedNamedComponent_Package(), ecorePackage.getEString(), "package", null, 0, 1, IQualifiedNamedComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(iQualifiedNamedComponentEClass, ecorePackage.getEString(), "getFullyQualifiedName", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iTypeEClass, IType.class, "IType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIType_FullyQualifiedName(), ecorePackage.getEString(), "fullyQualifiedName", null, 0, 1, IType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIType_Multiplicity(), this.getIMultiplicity(), null, "multiplicity", null, 0, 1, IType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(iTypeEClass, ecorePackage.getEString(), "getName", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iTypeEClass, ecorePackage.getEString(), "getPackage", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iTypeEClass, ecorePackage.getEBoolean(), "isArtifact", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iTypeEClass, ecorePackage.getEBoolean(), "isDatatype", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iTypeEClass, ecorePackage.getEBoolean(), "isEntityType", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iTypeEClass, ecorePackage.getEBoolean(), "isEnum", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(iTypeEClass, ecorePackage.getEBoolean(), "isPrimitive", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iAssociationEndEClass, IAssociationEnd.class, "IAssociationEnd", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIAssociationEnd_Aggregation(), this.getEAggregationEnum(), "aggregation", null, 1, 1, IAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIAssociationEnd_Changeable(), this.getEChangeableEnum(), "changeable", null, 0, 1, IAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIAssociationEnd_Navigable(), ecorePackage.getEBoolean(), "navigable", null, 1, 1, IAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIAssociationEnd_Ordered(), ecorePackage.getEBoolean(), "ordered", null, 1, 1, IAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIAssociationEnd_Unique(), ecorePackage.getEBoolean(), "unique", null, 1, 1, IAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIAssociationEnd_Multiplicity(), this.getIMultiplicity(), null, "multiplicity", null, 0, 1, IAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIAssociationEnd_Type(), this.getIAbstractArtifact(), null, "type", null, 0, 1, IAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iArgumentEClass, IArgument.class, "IArgument", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIArgument_Type(), this.getIType(), null, "type", null, 1, 1, IArgument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIArgument_DefaultValue(), ecorePackage.getEString(), "defaultValue", null, 0, 1, IArgument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIArgument_Ordered(), ecorePackage.getEBoolean(), "ordered", null, 0, 1, IArgument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIArgument_Unique(), ecorePackage.getEBoolean(), "unique", null, 0, 1, IArgument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iModelEClass, IModel.class, "IModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIModel_Packages(), this.getIPackage(), null, "packages", null, 0, -1, IModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iPackageEClass, IPackage.class, "IPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIPackage_Artifacts(), this.getIQualifiedNamedComponent(), null, "artifacts", null, 0, -1, IPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iStereotypeCapableEClass, IStereotypeCapable.class, "IStereotypeCapable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIStereotypeCapable_StereotypeInstances(), this.getIStereotypeInstance(), null, "stereotypeInstances", null, 0, -1, IStereotypeCapable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(iStereotypeCapableEClass, this.getIStereotypeInstance(), "getStereotypeInstanceByName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(iStereotypeCapableEClass, ecorePackage.getEBoolean(), "hasStereotypeInstance", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iStereotypeInstanceEClass, IStereotypeInstance.class, "IStereotypeInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIStereotypeInstance_Name(), ecorePackage.getEString(), "name", null, 0, 1, IStereotypeInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIStereotypeInstance_AttributeValues(), this.getIStereotypeAttributeValue(), null, "attributeValues", null, 0, -1, IStereotypeInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iStereotypeAttributeValueEClass, IStereotypeAttributeValue.class, "IStereotypeAttributeValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIStereotypeAttributeValue_Name(), ecorePackage.getEString(), "name", null, 0, 1, IStereotypeAttributeValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIStereotypeAttributeValue_Value(), ecorePackage.getEString(), "value", null, 0, 1, IStereotypeAttributeValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iMultiplicityEClass, IMultiplicity.class, "IMultiplicity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIMultiplicity_LowerBound(), ecorePackage.getEInt(), "lowerBound", null, 0, 1, IMultiplicity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIMultiplicity_UpperBound(), ecorePackage.getEInt(), "upperBound", null, 0, 1, IMultiplicity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(visibilityEnumEEnum, VisibilityEnum.class, "VisibilityEnum");
		addEEnumLiteral(visibilityEnumEEnum, VisibilityEnum.PUBLIC);
		addEEnumLiteral(visibilityEnumEEnum, VisibilityEnum.PACKAGE);
		addEEnumLiteral(visibilityEnumEEnum, VisibilityEnum.PRIVATE);
		addEEnumLiteral(visibilityEnumEEnum, VisibilityEnum.PROTECTED);

		initEEnum(eAggregationEnumEEnum, EAggregationEnum.class, "EAggregationEnum");
		addEEnumLiteral(eAggregationEnumEEnum, EAggregationEnum.NONE);
		addEEnumLiteral(eAggregationEnumEEnum, EAggregationEnum.SHARED);
		addEEnumLiteral(eAggregationEnumEEnum, EAggregationEnum.COMPOSITE);

		initEEnum(eChangeableEnumEEnum, EChangeableEnum.class, "EChangeableEnum");
		addEEnumLiteral(eChangeableEnumEEnum, EChangeableEnum.NONE);
		addEEnumLiteral(eChangeableEnumEEnum, EChangeableEnum.FROZEN);
		addEEnumLiteral(eChangeableEnumEEnum, EChangeableEnum.ADD_ONLY);

		// Create resource
		createResource(eNS_URI);
	}

} //MetamodelPackageImpl
