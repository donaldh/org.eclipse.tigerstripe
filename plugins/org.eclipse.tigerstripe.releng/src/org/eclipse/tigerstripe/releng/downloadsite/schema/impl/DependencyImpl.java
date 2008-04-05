/**
 * <copyright>
 * </copyright>
 *
 * $Id: DependencyImpl.java,v 1.2 2008/04/08 22:22:15 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.tigerstripe.releng.downloadsite.schema.Dependency;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage;

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
		return DownloadSitePackage.Literals.DEPENDENCY;
	}

} //DependencyImpl
