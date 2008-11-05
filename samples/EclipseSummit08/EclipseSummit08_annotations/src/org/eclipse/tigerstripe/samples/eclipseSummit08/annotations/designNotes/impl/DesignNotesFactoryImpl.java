/**
 * <copyright>
 * </copyright>
 *
 * $Id: DesignNotesFactoryImpl.java,v 1.1 2008/11/05 19:53:22 edillon Exp $
 */
package org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DesignNotesFactoryImpl extends EFactoryImpl implements DesignNotesFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DesignNotesFactory init() {
		try {
			DesignNotesFactory theDesignNotesFactory = (DesignNotesFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/tigerstripe/samples/eclipseSummit08/annotations/designNotes.ecore"); 
			if (theDesignNotesFactory != null) {
				return theDesignNotesFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DesignNotesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DesignNotesFactoryImpl() {
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
			case DesignNotesPackage.NOTE: return createNote();
			case DesignNotesPackage.TODO: return createTODO();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Note createNote() {
		NoteImpl note = new NoteImpl();
		return note;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TODO createTODO() {
		TODOImpl todo = new TODOImpl();
		return todo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DesignNotesPackage getDesignNotesPackage() {
		return (DesignNotesPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DesignNotesPackage getPackage() {
		return DesignNotesPackage.eINSTANCE;
	}

} //DesignNotesFactoryImpl
