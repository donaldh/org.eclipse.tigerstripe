/**
 * <copyright>
 * </copyright>
 *
 * $Id: IManagedEntityDetails.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IManaged Entity Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails#getManagedEntity <em>Managed Entity</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails#getCrudFlavorDetails <em>Crud Flavor Details</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails#getCustomMethodFlavorDetails <em>Custom Method Flavor Details</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIManagedEntityDetails()
 * @model
 * @generated
 */
public interface IManagedEntityDetails extends EObject {
	/**
	 * Returns the value of the '<em><b>Managed Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Managed Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Managed Entity</em>' reference.
	 * @see #setManagedEntity(IManagedEntityArtifact)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIManagedEntityDetails_ManagedEntity()
	 * @model
	 * @generated
	 */
	IManagedEntityArtifact getManagedEntity();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails#getManagedEntity <em>Managed Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Managed Entity</em>' reference.
	 * @see #getManagedEntity()
	 * @generated
	 */
	void setManagedEntity(IManagedEntityArtifact value);

	/**
	 * Returns the value of the '<em><b>Crud Flavor Details</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Crud Flavor Details</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Crud Flavor Details</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIManagedEntityDetails_CrudFlavorDetails()
	 * @model
	 * @generated
	 */
	EList<IEntityMethodFlavorDetails> getCrudFlavorDetails();

	/**
	 * Returns the value of the '<em><b>Custom Method Flavor Details</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Custom Method Flavor Details</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Custom Method Flavor Details</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIManagedEntityDetails_CustomMethodFlavorDetails()
	 * @model
	 * @generated
	 */
	EList<IEntityMethodFlavorDetails> getCustomMethodFlavorDetails();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	IEntityMethodFlavorDetails getCRUDFlavorDetails(EMethodType methodType, OssjEntityMethodFlavor flavor);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	IEntityMethodFlavorDetails getCustomMethodFlavorDetails(OssjEntityMethodFlavor flavor, String methodID);

} // IManagedEntityDetails
