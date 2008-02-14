/**
 * <copyright>
 * </copyright>
 *
 * $Id: IQueryArtifact.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQuery Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IQueryArtifact#getReturnedType <em>Returned Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIQueryArtifact()
 * @model
 * @generated
 */
public interface IQueryArtifact extends IAbstractArtifact {
	/**
	 * Returns the value of the '<em><b>Returned Type</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Returned Type</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Returned Type</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIQueryArtifact_ReturnedType()
	 * @model
	 * @generated
	 */
	EList<IType> getReturnedType();

} // IQueryArtifact
