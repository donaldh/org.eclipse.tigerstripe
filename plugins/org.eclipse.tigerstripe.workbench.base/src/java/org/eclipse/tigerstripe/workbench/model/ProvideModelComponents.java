/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for a model component method which return a model component or
 * collection or collection of model components. The annotation is used
 * internally to extend the returned model component objects with
 * <code>IContextProjectAware</code> interface to provide current execution
 * context in case of referenced modules or projects.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvideModelComponents {

}
