/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.project;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.workbench.base.test.utils.M1ProjectHelper;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalRule;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public class TestM1ProjectContents extends TestCase {

	private ITigerstripeM1GeneratorProject generator;

	private static final String GENERATOR = "TestM1ProjectContents";

	@Override
	protected void setUp() throws Exception {
		generator = M1ProjectHelper.createM1Project(GENERATOR, false);
	}

	@Override
	protected void tearDown() throws Exception {
		if (generator != null && generator.exists())
			generator.delete(true, null);
	}

	public void testGlobalRule() throws Exception {
		ITigerstripeM1GeneratorProject gProject = (ITigerstripeM1GeneratorProject) generator
				.makeWorkingCopy(null);

		// Create a rule
		ITemplateBasedRule rule = (ITemplateBasedRule) gProject
				.makeRule(IGlobalTemplateRule.class);
		assertNotNull(rule);

		rule.setEnabled(true);
		rule.setName("Rule1");
		rule.setDescription("a description");
		rule.setOutputFile("output.txt");
		rule.setTemplate("templates/listAll.vm");

		gProject.addGlobalRule((IGlobalRule) rule);
		gProject.commit(null);

		IRule[] rules = generator.getGlobalRules();
		assertTrue(rules.length == 1);

		assertTrue(rules[0] instanceof ITemplateBasedRule);
		ITemplateBasedRule theRule = (ITemplateBasedRule) rules[0];
		assertTrue("Rule1".equals(theRule.getName()));
		assertTrue("a description".equals(theRule.getDescription()));
		assertTrue("output.txt".equals(theRule.getOutputFile()));
		assertTrue("templates/listAll.vm".equals(theRule.getTemplate()));

		// Edit it
		IRule[] wRules = gProject.getGlobalRules();
		wRules[0].setDescription("Another Description");
		gProject.commit(null);

		rules = generator.getGlobalRules();
		theRule = (ITemplateBasedRule) rules[0];
		assertTrue("Another Description".equals(theRule.getDescription()));

		// Remove it
		gProject.removeGlobalRule((IGlobalRule) rule);
		gProject.commit(null);

		rules = generator.getGlobalRules();
		assertTrue(rules.length == 0);
	}
}
