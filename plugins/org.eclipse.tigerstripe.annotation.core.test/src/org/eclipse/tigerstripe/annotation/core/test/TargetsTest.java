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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;
import org.eclipse.tigerstripe.annotation.core.test.provider.IResourcePath;
import org.eclipse.tigerstripe.annotation.core.test.provider.ResourcePath;

/**
 * @author Yuri Strot
 *
 */
public class TargetsTest extends AbstractResourceTestCase {
	
	protected IProject project;
	protected IResourcePath path;
	
	@Override
	protected void setUp() throws Exception {
		project = createProject("project");
		path = new ResourcePath(project.getFullPath());
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		deleteProject(project);
	}
	
	public void testTargets() {
		IAnnotationManager manager = AnnotationPlugin.getManager();
		try {
			Annotation annotation1 = manager.addAnnotation(project,
					ModelFactory.eINSTANCE.createAuthor());
			assertNotNull(annotation1);
			
			Annotation annotation2 = manager.addAnnotation(path,
					ModelFactory.eINSTANCE.createAuthor());
			assertNotNull(annotation2);
			
			List<String> targets = getTargets(project);
			assertTrue(targets.contains(IResourcePath.class.getName()));
		}
		finally {
			manager.removeAnnotations(project);
			manager.removeAnnotations(path);
		}
	}
	
	public static List<String> getTargets(Object object) {
		List<String> resultTypes = new ArrayList<String>();
		String[] targets = AnnotationPlugin.getManager().getAnnotatedObjectTypes();
		for (int i = 0; i < targets.length; i++) {
			if (getAdapted(object, targets[i]) != null) {
				resultTypes.add(targets[i]);
			}
		}
		return resultTypes;
	}
	
	public static Object getAdapted(Object object, String className) {
		Object adapted = Platform.getAdapterManager().loadAdapter(object, className);
		if (adapted != null)
			return adapted;
		try {
			Class<?> clazz = Class.forName(className, true, object.getClass().getClassLoader());
			return Platform.getAdapterManager().getAdapter(object, clazz);
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

}
