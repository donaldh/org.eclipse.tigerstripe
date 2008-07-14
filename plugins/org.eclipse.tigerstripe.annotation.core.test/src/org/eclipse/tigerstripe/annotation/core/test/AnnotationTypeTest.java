/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.core.test;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelPackage;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationTypeTest extends TestCase {
	
	private static final String PACKAGE_NAME = ModelPackage.eNS_PREFIX;
	
	private static final String AUTHOR_CLASS = ModelPackage.eINSTANCE.getAuthor().getName();
	private static final String MIME_TYPE_CLASS = ModelPackage.eINSTANCE.getMimeType().getName();
	private static final String HIBERNATE_CLASS = ModelPackage.eINSTANCE.getHibernate().getName();
	private static final String PROJECT_DESCRIPTION_CLASS = ModelPackage.eINSTANCE.getProjectDescription().getName();
	
	public void testAllTypes() {
		AnnotationType[] types = AnnotationPlugin.getManager().getTypes();
		assertTrue(types.length >= 4);
	}
	
	public void testType() {
		AnnotationType type = AnnotationPlugin.getManager().getType(PACKAGE_NAME, AUTHOR_CLASS);
		assertNotNull(type);
		type = AnnotationPlugin.getManager().getType(PACKAGE_NAME, MIME_TYPE_CLASS);
		assertNotNull(type);
		type = AnnotationPlugin.getManager().getType(PACKAGE_NAME, HIBERNATE_CLASS);
		assertNotNull(type);
		type = AnnotationPlugin.getManager().getType(PACKAGE_NAME, PROJECT_DESCRIPTION_CLASS);
		assertNotNull(type);
		
	}

}
