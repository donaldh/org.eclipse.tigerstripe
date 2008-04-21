/**
 * <copyright>
 * 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *     
 * </copyright>
 *
 * $Id: ResourcesFactory.java,v 1.1 2008/04/21 23:30:28 edillon Exp $
 */
package org.eclipse.tigersrtipe.espace.resources;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigersrtipe.espace.resources.ResourcesPackage
 * @generated
 */
public interface ResourcesFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ResourcesFactory eINSTANCE = org.eclipse.tigersrtipe.espace.resources.impl.ResourcesFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Resource List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Resource List</em>'.
     * @generated
     */
    ResourceList createResourceList();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    ResourcesPackage getResourcesPackage();

} //ResourcesFactory
