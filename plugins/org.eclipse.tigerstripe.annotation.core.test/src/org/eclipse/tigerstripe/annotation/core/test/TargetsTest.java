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

import org.eclipse.core.resources.IProject;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationTarget;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelPackage;
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
		TargetAnnotationType[] types = AnnotationPlugin.getManager().getAnnotationTargets(project);

		boolean adaptedToResourcePath = false;
		boolean projectDescription = false;
		for (int i = 0; i < types.length; i++) {
			if (types[i].getType().getClazz().equals(ModelPackage.eINSTANCE.getProjectDescription())) {
				projectDescription = true;
			}
			IAnnotationTarget[] targets = types[i].getTargets();
			for (IAnnotationTarget annotationTarget : targets) {
				if (annotationTarget.getAdaptedObject() instanceof IResourcePath)
					adaptedToResourcePath = true;
			}
		}
		assertTrue(adaptedToResourcePath);
		assertTrue(projectDescription);
	}

}
