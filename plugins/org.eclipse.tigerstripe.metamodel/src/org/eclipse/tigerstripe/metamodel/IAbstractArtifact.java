/**
 * <copyright>
 * </copyright>
 *
 * $Id: IAbstractArtifact.java,v 1.4 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IAbstract Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getFields <em>Fields</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getMethods <em>Methods</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getLiterals <em>Literals</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#isAbstract <em>Abstract</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendedArtifact <em>Extended Artifact</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getImplementedArtifacts <em>Implemented Artifacts</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendingArtifacts <em>Extending Artifacts</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getImplementingArtifacts <em>Implementing Artifacts</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact()
 * @model abstract="true"
 * @generated
 */
public interface IAbstractArtifact extends IQualifiedNamedComponent {
	/**
	 * Returns the value of the '<em><b>Fields</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IField}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fields</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fields</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_Fields()
	 * @model containment="true"
	 * @generated
	 */
	EList<IField> getFields();

	/**
	 * Returns the value of the '<em><b>Methods</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IMethod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Methods</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Methods</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_Methods()
	 * @model containment="true"
	 * @generated
	 */
	EList<IMethod> getMethods();

	/**
	 * Returns the value of the '<em><b>Literals</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.ILiteral}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Literals</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Literals</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_Literals()
	 * @model containment="true"
	 * @generated
	 */
	EList<ILiteral> getLiterals();

	/**
	 * Returns the value of the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Abstract</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Abstract</em>' attribute.
	 * @see #setAbstract(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_Abstract()
	 * @model
	 * @generated
	 */
	boolean isAbstract();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#isAbstract <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Abstract</em>' attribute.
	 * @see #isAbstract()
	 * @generated
	 */
	void setAbstract(boolean value);

	/**
	 * Returns the value of the '<em><b>Extended Artifact</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendingArtifacts <em>Extending Artifacts</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extended Artifact</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extended Artifact</em>' reference.
	 * @see #setExtendedArtifact(IAbstractArtifact)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_ExtendedArtifact()
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendingArtifacts
	 * @model opposite="extendingArtifacts"
	 * @generated
	 */
	IAbstractArtifact getExtendedArtifact();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendedArtifact <em>Extended Artifact</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extended Artifact</em>' reference.
	 * @see #getExtendedArtifact()
	 * @generated
	 */
	void setExtendedArtifact(IAbstractArtifact value);

	/**
	 * Returns the value of the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getImplementingArtifacts <em>Implementing Artifacts</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implemented Artifacts</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implemented Artifacts</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_ImplementedArtifacts()
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getImplementingArtifacts
	 * @model opposite="implementingArtifacts"
	 * @generated
	 */
	EList<IAbstractArtifact> getImplementedArtifacts();

	/**
	 * Returns the value of the '<em><b>Extending Artifacts</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendedArtifact <em>Extended Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extending Artifacts</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extending Artifacts</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_ExtendingArtifacts()
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendedArtifact
	 * @model opposite="extendedArtifact"
	 * @generated
	 */
	EList<IAbstractArtifact> getExtendingArtifacts();

	/**
	 * Returns the value of the '<em><b>Implementing Artifacts</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getImplementedArtifacts <em>Implemented Artifacts</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implementing Artifacts</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implementing Artifacts</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_ImplementingArtifacts()
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getImplementedArtifacts
	 * @model opposite="implementedArtifacts"
	 * @generated
	 */
	EList<IAbstractArtifact> getImplementingArtifacts();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<IAbstractArtifact> getAncestors();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<IField> getInheritedFields();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<IMethod> getInheritedMethods();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<IAbstractArtifact> getReferencedArtifacts();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean hasExtends();

} // IAbstractArtifact
