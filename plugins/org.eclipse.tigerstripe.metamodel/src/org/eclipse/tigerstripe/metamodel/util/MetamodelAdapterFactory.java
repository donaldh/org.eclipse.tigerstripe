/**
 * <copyright>
 * </copyright>
 *
 * $Id: MetamodelAdapterFactory.java,v 1.2 2008/05/22 18:26:36 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.tigerstripe.metamodel.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage
 * @generated
 */
public class MetamodelAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MetamodelPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetamodelAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = MetamodelPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch the delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MetamodelSwitch<Adapter> modelSwitch =
		new MetamodelSwitch<Adapter>() {
			@Override
			public Adapter caseIAbstractArtifact(IAbstractArtifact object) {
				return createIAbstractArtifactAdapter();
			}
			@Override
			public Adapter caseIPrimitiveType(IPrimitiveType object) {
				return createIPrimitiveTypeAdapter();
			}
			@Override
			public Adapter caseIManagedEntityArtifact(IManagedEntityArtifact object) {
				return createIManagedEntityArtifactAdapter();
			}
			@Override
			public Adapter caseIDatatypeArtifact(IDatatypeArtifact object) {
				return createIDatatypeArtifactAdapter();
			}
			@Override
			public Adapter caseIExceptionArtifact(IExceptionArtifact object) {
				return createIExceptionArtifactAdapter();
			}
			@Override
			public Adapter caseISessionArtifact(ISessionArtifact object) {
				return createISessionArtifactAdapter();
			}
			@Override
			public Adapter caseIQueryArtifact(IQueryArtifact object) {
				return createIQueryArtifactAdapter();
			}
			@Override
			public Adapter caseIUpdateProcedureArtifact(IUpdateProcedureArtifact object) {
				return createIUpdateProcedureArtifactAdapter();
			}
			@Override
			public Adapter caseIEventArtifact(IEventArtifact object) {
				return createIEventArtifactAdapter();
			}
			@Override
			public Adapter caseIAssociationArtifact(IAssociationArtifact object) {
				return createIAssociationArtifactAdapter();
			}
			@Override
			public Adapter caseIAssociationClassArtifact(IAssociationClassArtifact object) {
				return createIAssociationClassArtifactAdapter();
			}
			@Override
			public Adapter caseIDependencyArtifact(IDependencyArtifact object) {
				return createIDependencyArtifactAdapter();
			}
			@Override
			public Adapter caseIEnumArtifact(IEnumArtifact object) {
				return createIEnumArtifactAdapter();
			}
			@Override
			public Adapter caseIField(IField object) {
				return createIFieldAdapter();
			}
			@Override
			public Adapter caseIMethod(IMethod object) {
				return createIMethodAdapter();
			}
			@Override
			public Adapter caseILiteral(ILiteral object) {
				return createILiteralAdapter();
			}
			@Override
			public Adapter caseIModelComponent(IModelComponent object) {
				return createIModelComponentAdapter();
			}
			@Override
			public Adapter caseIQualifiedNamedComponent(IQualifiedNamedComponent object) {
				return createIQualifiedNamedComponentAdapter();
			}
			@Override
			public Adapter caseIType(IType object) {
				return createITypeAdapter();
			}
			@Override
			public Adapter caseIAssociationEnd(IAssociationEnd object) {
				return createIAssociationEndAdapter();
			}
			@Override
			public Adapter caseIArgument(IArgument object) {
				return createIArgumentAdapter();
			}
			@Override
			public Adapter caseIModel(IModel object) {
				return createIModelAdapter();
			}
			@Override
			public Adapter caseIPackage(IPackage object) {
				return createIPackageAdapter();
			}
			@Override
			public Adapter caseIStereotypeCapable(IStereotypeCapable object) {
				return createIStereotypeCapableAdapter();
			}
			@Override
			public Adapter caseIStereotypeInstance(IStereotypeInstance object) {
				return createIStereotypeInstanceAdapter();
			}
			@Override
			public Adapter caseIStereotypeAttributeValue(IStereotypeAttributeValue object) {
				return createIStereotypeAttributeValueAdapter();
			}
			@Override
			public Adapter caseIMultiplicity(IMultiplicity object) {
				return createIMultiplicityAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact <em>IAbstract Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact
	 * @generated
	 */
	public Adapter createIAbstractArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IPrimitiveType <em>IPrimitive Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IPrimitiveType
	 * @generated
	 */
	public Adapter createIPrimitiveTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact <em>IManaged Entity Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact
	 * @generated
	 */
	public Adapter createIManagedEntityArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IDatatypeArtifact <em>IDatatype Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IDatatypeArtifact
	 * @generated
	 */
	public Adapter createIDatatypeArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IExceptionArtifact <em>IException Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IExceptionArtifact
	 * @generated
	 */
	public Adapter createIExceptionArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.ISessionArtifact <em>ISession Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.ISessionArtifact
	 * @generated
	 */
	public Adapter createISessionArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IQueryArtifact <em>IQuery Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IQueryArtifact
	 * @generated
	 */
	public Adapter createIQueryArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IUpdateProcedureArtifact <em>IUpdate Procedure Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IUpdateProcedureArtifact
	 * @generated
	 */
	public Adapter createIUpdateProcedureArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IEventArtifact <em>IEvent Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IEventArtifact
	 * @generated
	 */
	public Adapter createIEventArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IAssociationArtifact <em>IAssociation Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationArtifact
	 * @generated
	 */
	public Adapter createIAssociationArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IAssociationClassArtifact <em>IAssociation Class Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationClassArtifact
	 * @generated
	 */
	public Adapter createIAssociationClassArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IDependencyArtifact <em>IDependency Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IDependencyArtifact
	 * @generated
	 */
	public Adapter createIDependencyArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IEnumArtifact <em>IEnum Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IEnumArtifact
	 * @generated
	 */
	public Adapter createIEnumArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IField <em>IField</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IField
	 * @generated
	 */
	public Adapter createIFieldAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IMethod <em>IMethod</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod
	 * @generated
	 */
	public Adapter createIMethodAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.ILiteral <em>ILiteral</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.ILiteral
	 * @generated
	 */
	public Adapter createILiteralAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IModelComponent <em>IModel Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IModelComponent
	 * @generated
	 */
	public Adapter createIModelComponentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IQualifiedNamedComponent <em>IQualified Named Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IQualifiedNamedComponent
	 * @generated
	 */
	public Adapter createIQualifiedNamedComponentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IType <em>IType</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IType
	 * @generated
	 */
	public Adapter createITypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd <em>IAssociation End</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationEnd
	 * @generated
	 */
	public Adapter createIAssociationEndAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IArgument <em>IArgument</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IArgument
	 * @generated
	 */
	public Adapter createIArgumentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IModel <em>IModel</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IModel
	 * @generated
	 */
	public Adapter createIModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IPackage <em>IPackage</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IPackage
	 * @generated
	 */
	public Adapter createIPackageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IStereotypeCapable <em>IStereotype Capable</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeCapable
	 * @generated
	 */
	public Adapter createIStereotypeCapableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IStereotypeInstance <em>IStereotype Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeInstance
	 * @generated
	 */
	public Adapter createIStereotypeInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue <em>IStereotype Attribute Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue
	 * @generated
	 */
	public Adapter createIStereotypeAttributeValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.IMultiplicity <em>IMultiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.IMultiplicity
	 * @generated
	 */
	public Adapter createIMultiplicityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //MetamodelAdapterFactory
