/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelReferenceFactoryImpl.java,v 1.2 2011/12/01 15:24:06 verastov Exp $
 */
package org.eclipse.tigerstripe.workbench.annotation.modelReference.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.tigerstripe.workbench.annotation.modelReference.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelReferenceFactoryImpl extends EFactoryImpl implements ModelReferenceFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ModelReferenceFactory init() {
		try {
			ModelReferenceFactory theModelReferenceFactory = (ModelReferenceFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/tigerstripe/workbench/annotation/modelreference.ecore"); 
			if (theModelReferenceFactory != null) {
				return theModelReferenceFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ModelReferenceFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReferenceFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ModelReferencePackage.MODEL_REFERENCE: return createModelReference();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReference createModelReference() {
		ModelReferenceImpl modelReference = new ModelReferenceImpl();
		return modelReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelReferencePackage getModelReferencePackage() {
		return (ModelReferencePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ModelReferencePackage getPackage() {
		return ModelReferencePackage.eINSTANCE;
	}

} //ModelReferenceFactoryImpl
