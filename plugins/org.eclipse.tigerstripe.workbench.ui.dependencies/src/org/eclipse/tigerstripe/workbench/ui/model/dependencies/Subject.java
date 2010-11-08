/**
 * <copyright>
 * </copyright>
 *
 * $Id: Subject.java,v 1.1 2010/11/05 20:20:45 ystrot Exp $
 */
package org.eclipse.tigerstripe.workbench.ui.model.dependencies;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Subject</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isOpened <em>Opened</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isUseCustomStyle <em>Use Custom Style</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isMaster <em>Master</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getSubject()
 * @model
 * @generated
 */
public interface Subject extends Shape {
	/**
	 * Returns the value of the '<em><b>Opened</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Opened</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Opened</em>' attribute.
	 * @see #setOpened(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getSubject_Opened()
	 * @model
	 * @generated
	 */
	boolean isOpened();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isOpened <em>Opened</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Opened</em>' attribute.
	 * @see #isOpened()
	 * @generated
	 */
	void setOpened(boolean value);

	/**
	 * Returns the value of the '<em><b>Use Custom Style</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use Custom Style</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use Custom Style</em>' attribute.
	 * @see #setUseCustomStyle(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getSubject_UseCustomStyle()
	 * @model
	 * @generated
	 */
	boolean isUseCustomStyle();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isUseCustomStyle <em>Use Custom Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Custom Style</em>' attribute.
	 * @see #isUseCustomStyle()
	 * @generated
	 */
	void setUseCustomStyle(boolean value);

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' reference.
	 * @see #setKind(Kind)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getSubject_Kind()
	 * @model
	 * @generated
	 */
	Kind getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#getKind <em>Kind</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' reference.
	 * @see #getKind()
	 * @generated
	 */
	void setKind(Kind value);

	/**
	 * Returns the value of the '<em><b>Master</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Master</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Master</em>' attribute.
	 * @see #setMaster(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getSubject_Master()
	 * @model
	 * @generated
	 */
	boolean isMaster();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isMaster <em>Master</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Master</em>' attribute.
	 * @see #isMaster()
	 * @generated
	 */
	void setMaster(boolean value);

} // Subject