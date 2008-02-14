/**
 * <copyright>
 * </copyright>
 *
 * $Id: OssjAdapterFactory.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.tigerstripe.metamodel.extensions.IStandardSpecifics;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage
 * @generated
 */
public class OssjAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static OssjPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OssjAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = OssjPackage.eINSTANCE;
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
	protected OssjSwitch<Adapter> modelSwitch =
		new OssjSwitch<Adapter>() {
			@Override
			public Adapter caseIOssjArtifactSpecifics(IOssjArtifactSpecifics object) {
				return createIOssjArtifactSpecificsAdapter();
			}
			@Override
			public Adapter caseIOssjDatatypeSpecifics(IOssjDatatypeSpecifics object) {
				return createIOssjDatatypeSpecificsAdapter();
			}
			@Override
			public Adapter caseIOssjEntitySpecifics(IOssjEntitySpecifics object) {
				return createIOssjEntitySpecificsAdapter();
			}
			@Override
			public Adapter caseIOssjEnumSpecifics(IOssjEnumSpecifics object) {
				return createIOssjEnumSpecificsAdapter();
			}
			@Override
			public Adapter caseIOssjEventSpecifics(IOssjEventSpecifics object) {
				return createIOssjEventSpecificsAdapter();
			}
			@Override
			public Adapter caseIEventDescriptorEntry(IEventDescriptorEntry object) {
				return createIEventDescriptorEntryAdapter();
			}
			@Override
			public Adapter caseIOssjQuerySpecifics(IOssjQuerySpecifics object) {
				return createIOssjQuerySpecificsAdapter();
			}
			@Override
			public Adapter caseIOssjUpdateProcedureSpecifics(IOssjUpdateProcedureSpecifics object) {
				return createIOssjUpdateProcedureSpecificsAdapter();
			}
			@Override
			public Adapter caseIManagedEntityDetails(IManagedEntityDetails object) {
				return createIManagedEntityDetailsAdapter();
			}
			@Override
			public Adapter caseIStandardSpecifics(IStandardSpecifics object) {
				return createIStandardSpecificsAdapter();
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjArtifactSpecifics <em>IOssj Artifact Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjArtifactSpecifics
	 * @generated
	 */
	public Adapter createIOssjArtifactSpecificsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjDatatypeSpecifics <em>IOssj Datatype Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjDatatypeSpecifics
	 * @generated
	 */
	public Adapter createIOssjDatatypeSpecificsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics <em>IOssj Entity Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics
	 * @generated
	 */
	public Adapter createIOssjEntitySpecificsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics <em>IOssj Enum Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics
	 * @generated
	 */
	public Adapter createIOssjEnumSpecificsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics <em>IOssj Event Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics
	 * @generated
	 */
	public Adapter createIOssjEventSpecificsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry <em>IEvent Descriptor Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry
	 * @generated
	 */
	public Adapter createIEventDescriptorEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics <em>IOssj Query Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics
	 * @generated
	 */
	public Adapter createIOssjQuerySpecificsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjUpdateProcedureSpecifics <em>IOssj Update Procedure Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjUpdateProcedureSpecifics
	 * @generated
	 */
	public Adapter createIOssjUpdateProcedureSpecificsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails <em>IManaged Entity Details</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails
	 * @generated
	 */
	public Adapter createIManagedEntityDetailsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.metamodel.extensions.IStandardSpecifics <em>IStandard Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.IStandardSpecifics
	 * @generated
	 */
	public Adapter createIStandardSpecificsAdapter() {
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

} //OssjAdapterFactory
