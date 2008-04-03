/**
 * <copyright>
 * </copyright>
 *
 * $Id: DependencyImpl.java,v 1.1 2008/04/03 21:45:32 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.ant.builds.schema.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaPackage;
import org.eclipse.tigerstripe.releng.ant.builds.schema.Dependency;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DependencyImpl extends BundleImpl implements Dependency {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DependencyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BuildSchemaPackage.Literals.DEPENDENCY;
	}

} //DependencyImpl
