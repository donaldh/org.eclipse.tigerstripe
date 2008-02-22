/**
 * <copyright>
 * </copyright>
 *
 * $Id: IAbstractArtifact.java,v 1.2 2008/02/22 20:01:22 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.tigerstripe.metamodel.extensions.IStandardSpecifics;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>IAbstract Artifact</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getFields <em>Fields</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getMethods <em>Methods</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getLiterals <em>Literals</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#isAbstract <em>Abstract</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendedArtifact <em>Extended Artifact</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getImplementedArtifacts <em>Implemented Artifacts</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getStandardSpecifics <em>Standard Specifics</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact()
 * @model abstract="true"
 * @generated
 */
public interface IAbstractArtifact extends IQualifiedNamedComponent {
	/**
	 * Returns the value of the '<em><b>Fields</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.metamodel.IField}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Fields</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Fields</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_Fields()
	 * @model containment="true"
	 * @generated
	 */
	EList<IField> getFields();

	/**
	 * Returns the value of the '<em><b>Methods</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.metamodel.IMethod}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Methods</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Methods</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_Methods()
	 * @model containment="true"
	 * @generated
	 */
	EList<IMethod> getMethods();

	/**
	 * Returns the value of the '<em><b>Literals</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.metamodel.ILiteral}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Literals</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Literals</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_Literals()
	 * @model containment="true"
	 * @generated
	 */
	EList<ILiteral> getLiterals();

	/**
	 * Returns the value of the '<em><b>Abstract</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Abstract</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Abstract</em>' attribute.
	 * @see #setAbstract(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_Abstract()
	 * @model
	 * @generated
	 */
	boolean isAbstract();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#isAbstract <em>Abstract</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Abstract</em>' attribute.
	 * @see #isAbstract()
	 * @generated
	 */
	void setAbstract(boolean value);

	/**
	 * Returns the value of the '<em><b>Extended Artifact</b></em>'
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extended Artifact</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Extended Artifact</em>' reference.
	 * @see #setExtendedArtifact(IAbstractArtifact)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_ExtendedArtifact()
	 * @model
	 * @generated
	 */
	IAbstractArtifact getExtendedArtifact();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendedArtifact <em>Extended Artifact</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Extended Artifact</em>'
	 *            reference.
	 * @see #getExtendedArtifact()
	 * @generated
	 */
	void setExtendedArtifact(IAbstractArtifact value);

	/**
	 * Returns the value of the '<em><b>Implemented Artifacts</b></em>'
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implemented Artifacts</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Implemented Artifacts</em>' reference
	 *         list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_ImplementedArtifacts()
	 * @model
	 * @generated
	 */
	EList<IAbstractArtifact> getImplementedArtifacts();

	/**
	 * Returns the value of the '<em><b>Standard Specifics</b></em>'
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Standard Specifics</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Standard Specifics</em>' reference.
	 * @see #setStandardSpecifics(IStandardSpecifics)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAbstractArtifact_StandardSpecifics()
	 * @model
	 * @generated
	 */
	IStandardSpecifics getStandardSpecifics();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getStandardSpecifics <em>Standard Specifics</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Standard Specifics</em>'
	 *            reference.
	 * @see #getStandardSpecifics()
	 * @generated
	 */
	void setStandardSpecifics(IStandardSpecifics value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation"
	 * @generated
	 */
	EList<IAbstractArtifact> getAncestors();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation"
	 * @generated
	 */
	EList<IAbstractArtifact> getExtendingArtifacts();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation"
	 * @generated
	 */
	EList<IAbstractArtifact> getImplementingArtifact();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation"
	 * @generated
	 */
	EList<IField> getInheritedFields();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation"
	 * @generated
	 */
	EList<IMethod> getInheritedMethods();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation"
	 * @generated
	 */
	EList<IAbstractArtifact> getReferencedArtifacts();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model
	 * @generated
	 */
	boolean hasExtends();

} // IAbstractArtifact
