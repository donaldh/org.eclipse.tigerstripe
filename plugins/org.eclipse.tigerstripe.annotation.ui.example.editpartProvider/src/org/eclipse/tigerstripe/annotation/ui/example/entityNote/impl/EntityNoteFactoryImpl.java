/**
 * <copyright>
 * </copyright>
 *
 * $Id: EntityNoteFactoryImpl.java,v 1.1 2008/07/01 08:49:23 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.example.entityNote.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.tigerstripe.annotation.ui.example.entityNote.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EntityNoteFactoryImpl extends EFactoryImpl implements EntityNoteFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EntityNoteFactory init() {
		try {
			EntityNoteFactory theEntityNoteFactory = (EntityNoteFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/tigerstripe/annotation/ui/example/entityNote.ecore"); 
			if (theEntityNoteFactory != null) {
				return theEntityNoteFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EntityNoteFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityNoteFactoryImpl() {
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
			case EntityNotePackage.ENTITY_NOTE: return createEntityNote();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityNote createEntityNote() {
		EntityNoteImpl entityNote = new EntityNoteImpl();
		return entityNote;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityNotePackage getEntityNotePackage() {
		return (EntityNotePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EntityNotePackage getPackage() {
		return EntityNotePackage.eINSTANCE;
	}

} //EntityNoteFactoryImpl
