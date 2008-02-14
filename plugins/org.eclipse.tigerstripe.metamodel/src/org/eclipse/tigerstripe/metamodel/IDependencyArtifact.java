/**
 * <copyright>
 * </copyright>
 *
 * $Id: IDependencyArtifact.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IDependency Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IDependencyArtifact#getAEndType <em>AEnd Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IDependencyArtifact#getZEndType <em>ZEnd Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIDependencyArtifact()
 * @model
 * @generated
 */
public interface IDependencyArtifact extends IAbstractArtifact {
	/**
	 * Returns the value of the '<em><b>AEnd Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AEnd Type</em>' reference.
	 * @see #setAEndType(IType)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIDependencyArtifact_AEndType()
	 * @model required="true"
	 * @generated
	 */
	IType getAEndType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IDependencyArtifact#getAEndType <em>AEnd Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd Type</em>' reference.
	 * @see #getAEndType()
	 * @generated
	 */
	void setAEndType(IType value);

	/**
	 * Returns the value of the '<em><b>ZEnd Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ZEnd Type</em>' reference.
	 * @see #setZEndType(IType)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIDependencyArtifact_ZEndType()
	 * @model required="true"
	 * @generated
	 */
	IType getZEndType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IDependencyArtifact#getZEndType <em>ZEnd Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd Type</em>' reference.
	 * @see #getZEndType()
	 * @generated
	 */
	void setZEndType(IType value);

} // IDependencyArtifact
