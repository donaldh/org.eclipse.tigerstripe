/**
 * <copyright>
 * </copyright>
 *
 * $Id: SomeTestAnnotsFactoryImpl.java,v 1.1 2008/07/18 01:07:18 jworrell Exp $
 */
package org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SomeTestAnnotsFactoryImpl extends EFactoryImpl implements SomeTestAnnotsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SomeTestAnnotsFactory init() {
		try {
			SomeTestAnnotsFactory theSomeTestAnnotsFactory = (SomeTestAnnotsFactory)EPackage.Registry.INSTANCE.getEFactory("http://org/eclipse/tigerstripe/annotation/testAnnots.ecore"); 
			if (theSomeTestAnnotsFactory != null) {
				return theSomeTestAnnotsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SomeTestAnnotsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SomeTestAnnotsFactoryImpl() {
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
			case SomeTestAnnotsPackage.TEST_ANNOT1: return createTestAnnot1();
			case SomeTestAnnotsPackage.TEST_ANNOT2: return createTestAnnot2();
			case SomeTestAnnotsPackage.TEST_ANNOT3: return createTestAnnot3();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestAnnot1 createTestAnnot1() {
		TestAnnot1Impl testAnnot1 = new TestAnnot1Impl();
		return testAnnot1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestAnnot2 createTestAnnot2() {
		TestAnnot2Impl testAnnot2 = new TestAnnot2Impl();
		return testAnnot2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestAnnot3 createTestAnnot3() {
		TestAnnot3Impl testAnnot3 = new TestAnnot3Impl();
		return testAnnot3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SomeTestAnnotsPackage getSomeTestAnnotsPackage() {
		return (SomeTestAnnotsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SomeTestAnnotsPackage getPackage() {
		return SomeTestAnnotsPackage.eINSTANCE;
	}

} //SomeTestAnnotsFactoryImpl
