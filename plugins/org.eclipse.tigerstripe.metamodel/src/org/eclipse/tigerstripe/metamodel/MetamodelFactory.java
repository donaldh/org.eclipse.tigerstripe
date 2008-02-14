/**
 * <copyright>
 * </copyright>
 *
 * $Id: MetamodelFactory.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage
 * @generated
 */
public interface MetamodelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MetamodelFactory eINSTANCE = org.eclipse.tigerstripe.metamodel.impl.MetamodelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>IPrimitive Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IPrimitive Type</em>'.
	 * @generated
	 */
	IPrimitiveType createIPrimitiveType();

	/**
	 * Returns a new object of class '<em>IManaged Entity Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IManaged Entity Artifact</em>'.
	 * @generated
	 */
	IManagedEntityArtifact createIManagedEntityArtifact();

	/**
	 * Returns a new object of class '<em>IDatatype Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IDatatype Artifact</em>'.
	 * @generated
	 */
	IDatatypeArtifact createIDatatypeArtifact();

	/**
	 * Returns a new object of class '<em>IException Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IException Artifact</em>'.
	 * @generated
	 */
	IExceptionArtifact createIExceptionArtifact();

	/**
	 * Returns a new object of class '<em>ISession Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ISession Artifact</em>'.
	 * @generated
	 */
	ISessionArtifact createISessionArtifact();

	/**
	 * Returns a new object of class '<em>IQuery Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IQuery Artifact</em>'.
	 * @generated
	 */
	IQueryArtifact createIQueryArtifact();

	/**
	 * Returns a new object of class '<em>IUpdate Procedure Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IUpdate Procedure Artifact</em>'.
	 * @generated
	 */
	IUpdateProcedureArtifact createIUpdateProcedureArtifact();

	/**
	 * Returns a new object of class '<em>IEvent Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IEvent Artifact</em>'.
	 * @generated
	 */
	IEventArtifact createIEventArtifact();

	/**
	 * Returns a new object of class '<em>IAssociation Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IAssociation Artifact</em>'.
	 * @generated
	 */
	IAssociationArtifact createIAssociationArtifact();

	/**
	 * Returns a new object of class '<em>IAssociation Class Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IAssociation Class Artifact</em>'.
	 * @generated
	 */
	IAssociationClassArtifact createIAssociationClassArtifact();

	/**
	 * Returns a new object of class '<em>IDependency Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IDependency Artifact</em>'.
	 * @generated
	 */
	IDependencyArtifact createIDependencyArtifact();

	/**
	 * Returns a new object of class '<em>IEnum Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IEnum Artifact</em>'.
	 * @generated
	 */
	IEnumArtifact createIEnumArtifact();

	/**
	 * Returns a new object of class '<em>IField</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IField</em>'.
	 * @generated
	 */
	IField createIField();

	/**
	 * Returns a new object of class '<em>IMethod</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IMethod</em>'.
	 * @generated
	 */
	IMethod createIMethod();

	/**
	 * Returns a new object of class '<em>ILiteral</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ILiteral</em>'.
	 * @generated
	 */
	ILiteral createILiteral();

	/**
	 * Returns a new object of class '<em>IType</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IType</em>'.
	 * @generated
	 */
	IType createIType();

	/**
	 * Returns a new object of class '<em>IAssociation End</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IAssociation End</em>'.
	 * @generated
	 */
	IAssociationEnd createIAssociationEnd();

	/**
	 * Returns a new object of class '<em>IArgument</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IArgument</em>'.
	 * @generated
	 */
	IArgument createIArgument();

	/**
	 * Returns a new object of class '<em>IModel</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IModel</em>'.
	 * @generated
	 */
	IModel createIModel();

	/**
	 * Returns a new object of class '<em>IPackage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IPackage</em>'.
	 * @generated
	 */
	IPackage createIPackage();

	/**
	 * Returns a new object of class '<em>IStereotype Capable</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IStereotype Capable</em>'.
	 * @generated
	 */
	IStereotypeCapable createIStereotypeCapable();

	/**
	 * Returns a new object of class '<em>IStereotype Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IStereotype Instance</em>'.
	 * @generated
	 */
	IStereotypeInstance createIStereotypeInstance();

	/**
	 * Returns a new object of class '<em>IStereotype Attribute Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IStereotype Attribute Value</em>'.
	 * @generated
	 */
	IStereotypeAttributeValue createIStereotypeAttributeValue();

	/**
	 * Returns a new object of class '<em>IEntity Method Flavor Details</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IEntity Method Flavor Details</em>'.
	 * @generated
	 */
	IEntityMethodFlavorDetails createIEntityMethodFlavorDetails();

	/**
	 * Returns a new object of class '<em>IMultiplicity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IMultiplicity</em>'.
	 * @generated
	 */
	IMultiplicity createIMultiplicity();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MetamodelPackage getMetamodelPackage();

} //MetamodelFactory
