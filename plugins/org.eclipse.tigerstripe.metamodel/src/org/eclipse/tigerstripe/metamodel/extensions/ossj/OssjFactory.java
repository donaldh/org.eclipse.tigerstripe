/**
 * <copyright>
 * </copyright>
 *
 * $Id: OssjFactory.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage
 * @generated
 */
public interface OssjFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	OssjFactory eINSTANCE = org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>IOssj Artifact Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IOssj Artifact Specifics</em>'.
	 * @generated
	 */
	IOssjArtifactSpecifics createIOssjArtifactSpecifics();

	/**
	 * Returns a new object of class '<em>IOssj Datatype Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IOssj Datatype Specifics</em>'.
	 * @generated
	 */
	IOssjDatatypeSpecifics createIOssjDatatypeSpecifics();

	/**
	 * Returns a new object of class '<em>IOssj Entity Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IOssj Entity Specifics</em>'.
	 * @generated
	 */
	IOssjEntitySpecifics createIOssjEntitySpecifics();

	/**
	 * Returns a new object of class '<em>IOssj Enum Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IOssj Enum Specifics</em>'.
	 * @generated
	 */
	IOssjEnumSpecifics createIOssjEnumSpecifics();

	/**
	 * Returns a new object of class '<em>IOssj Event Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IOssj Event Specifics</em>'.
	 * @generated
	 */
	IOssjEventSpecifics createIOssjEventSpecifics();

	/**
	 * Returns a new object of class '<em>IEvent Descriptor Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IEvent Descriptor Entry</em>'.
	 * @generated
	 */
	IEventDescriptorEntry createIEventDescriptorEntry();

	/**
	 * Returns a new object of class '<em>IOssj Query Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IOssj Query Specifics</em>'.
	 * @generated
	 */
	IOssjQuerySpecifics createIOssjQuerySpecifics();

	/**
	 * Returns a new object of class '<em>IOssj Update Procedure Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IOssj Update Procedure Specifics</em>'.
	 * @generated
	 */
	IOssjUpdateProcedureSpecifics createIOssjUpdateProcedureSpecifics();

	/**
	 * Returns a new object of class '<em>IManaged Entity Details</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IManaged Entity Details</em>'.
	 * @generated
	 */
	IManagedEntityDetails createIManagedEntityDetails();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	OssjPackage getOssjPackage();

} //OssjFactory
