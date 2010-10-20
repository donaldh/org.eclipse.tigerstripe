/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.patterns;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.model.annotation.AnnotationHelper;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IProjectPattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ProjectPatternTest extends TestCase {

	private ITigerstripeModelProject builtinProject;
	private final static String PROJECTNAME = "TestProject";
	private final static String PROJECTVERSION = "1.0";

	private ITigerstripeModelProject contributedProject;
	private final static String CONTRIBUTEDPROJECTNAME = "ContributedProject";
	private final static String CONTRIBUTEDPROJECTVERSION = "2.0";

	private final static String SOME_TEST_ANNOTS = "org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots";

	// No set up required - no stereotypes allowed on projects

	@Override
	protected void tearDown() throws Exception {
		if (builtinProject != null && builtinProject.exists())
			builtinProject.delete(true, null);
	}

	public void testBuiltinProjectPattern() throws Exception {

		String patternname = "org.eclipse.tigerstripe.workbench.base.Project";

		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNotNull("No patterns with name '" + patternname + "' returned",
				pattern);
		IProjectPattern projectPattern = (IProjectPattern) pattern;

		builtinProject = projectPattern.createProject(PROJECTNAME, null, null);
		assertNotNull(builtinProject);

		projectPattern.annotateProject(builtinProject);

		// Check it out!
		assertNotNull(builtinProject);
		assertTrue("Name doesn't match ", builtinProject.getName().equals(
				PROJECTNAME));

	}

	public void testContributedProjectPattern() throws Exception {

		String patternname = "com.test.Project";

		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNotNull("No patterns with name '" + patternname + "' returned",
				pattern);
		IProjectPattern projectPattern = (IProjectPattern) pattern;

		contributedProject = projectPattern.createProject(
				CONTRIBUTEDPROJECTNAME, null, null);
		assertNotNull(contributedProject);

		projectPattern.annotateProject(contributedProject);

		// Check it out!
		assertNotNull(contributedProject);
		assertTrue("Name doesn't match ",
				contributedProject.getName().equals(CONTRIBUTEDPROJECTNAME));

		// This should have an Annotation
		AnnotationHelper helper = AnnotationHelper.getInstance();
		List<Annotation> annos = helper.getAnnotations(contributedProject);
		assertEquals("Wrong number of Annotations on project", 1, annos.size());

		List<Annotation> test1Annos = helper.getAnnotations(contributedProject,
				TestAnnot1.class);

		assertEquals("Wrong number of TestAnnot1 on project", 1,
				test1Annos.size());

		for (Annotation ann : test1Annos) {
			if (ann.getContent() instanceof TestAnnot1) {
				TestAnnot1 ta1 = (TestAnnot1) ann.getContent();
				assertEquals(
						"TestAnnot1 value on project did not match expected value",
						ta1.getTwine(), "hemp");
			} else {
				fail("Object is not a TestAnnot1 " + ann.getClass().getName());
			}
		}
	}

}
