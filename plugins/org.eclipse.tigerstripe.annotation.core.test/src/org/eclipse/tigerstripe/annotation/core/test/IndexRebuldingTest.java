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

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;

/**
 * @author Yuri Strot
 *
 */
public class IndexRebuldingTest extends AbstractResourceTestCase {
	
	protected IProject project1;
	protected IProject project2;
	
	@Override
	protected void setUp() throws Exception {
		project1 = createProject("project1");
		project2 = createProject("project2");
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		deleteProject(project1);
		deleteProject(project2);
	}
	
	public void test1() {
		IAnnotationManager manager = AnnotationPlugin.getManager();
		try {
			manager.addAnnotation(project1, createMimeType("text/html"));
			removeProjectFile(project1);
			Annotation[] annotations = manager.getAnnotations(project1, false);
			assertEquals(annotations.length, 0);
		}
		finally {
			manager.removeAnnotations(project1);
		}
	}
	
	private void removeProjectFile(IProject project) {
		IPath path = project.getLocation();
		if (path != null) {
			path = path.append(".annotations");
			File file = path.toFile();
			if (file != null) {
				file.delete();
			}
		}
	}
	
	private MimeType createMimeType(String text) {
		MimeType type = ModelFactory.eINSTANCE.createMimeType();
		type.setMimeType(text);
		return type;
	}

}
