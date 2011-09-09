/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     Anton Salnik (xored software, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.core.project.pluggable.rules;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.AbstractTigerstripeTestCase;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ModelProject;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.Rule;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class TestRule extends AbstractTigerstripeTestCase {

	private static final String PROJECT_ID = "TestProject1";

	private static final String REFERENCED_PROJECT_ID = "TestProject2";

	private final String[] expectedContextKeys = new String[] {
			IRule.MODULE_ARTIFACTS, IRule.MODULE_ENTITIES,
			IRule.MODULE_DATATYPES, IRule.MODULE_EVENTS,
			IRule.MODULE_ENUMERATIONS, IRule.MODULE_EXCEPTIONS,
			IRule.MODULE_QUERIES, IRule.MODULE_SESSIONS,
			IRule.MODULE_UPDATEPROCEDURES, IRule.MODULE_ASSOCIATIONS,
			IRule.MODULE_DEPENDENCIES, IRule.MODULE_ASSOCIATIONCLASSES,
			IRule.MODULE_PACKAGES };

	private ITigerstripeModelProject project;
	private ITigerstripeModelProject refProject;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		project = (ITigerstripeModelProject) createEmptyModelProject(PROJECT_ID,
				PROJECT_ID);
		refProject = (ITigerstripeModelProject) createEmptyModelProject(
				REFERENCED_PROJECT_ID, REFERENCED_PROJECT_ID);
	}

	@Override
	protected void tearDown() throws Exception {
		deleteModelProject(project);
		deleteModelProject(refProject);
		super.tearDown();
	}

	public void testGetModuleContextEmpty() throws Exception {
		Rule rule = new TestRuleImpl();
		Map<String, Object> context = rule.getModuleContext(new ModelProject(
				project));
		emptyContextExpected(context);

		context = rule
				.getModuleContext(new ModelProject(refProject, project, 1));
		emptyContextExpected(context);
	}

	private void emptyContextExpected(Map<String, Object> context) {
		checkContext(context, true, new Class[] {}, new Class[] {});
	}

	public void testGetModuleContextBaseProject() throws Exception {
		int totalElements = createEachArtifactType(project);
		Rule rule = new TestRuleImpl();
		Map<String, Object> context = rule.getModuleContext(new ModelProject(
				project));
		// add primitive type artifact
		totalElements = totalElements + 1;
		checkContext(context, false, new Class[] { IAbstractArtifact.class },
				new Class[] { IContextProjectAware.class });
	}

	public void testGetModuleContextReferencedProject() throws Exception {
		int totalElements = createEachArtifactType(refProject);
		Rule rule = new TestRuleImpl();
		Map<String, Object> context = rule.getModuleContext(new ModelProject(
				refProject, project, 1));
		// add primitive type artifact
		totalElements = totalElements + 1;
		checkContext(context, false,
				new Class[] { IContextProjectAware.class }, new Class[] {});
	}

	private void checkContext(Map<String, Object> context,
			boolean isEmptyCollections, Class<?>[] expected,
			Class<?>[] nonExpected) {
		assertNotNull(context);
		assertEquals(expectedContextKeys.length, context.size());
		for (String key : expectedContextKeys) {
			Object value = context.get(key);
			assertTrue(value instanceof Collection<?>);
			if (isEmptyCollections) {
				assertTrue(((Collection<?>) value).size() == 0);
			} else {
				assertTrue(((Collection<?>) value).size() > 0);
			}

			for (Iterator<?> it = ((Collection<?>) value).iterator(); it
					.hasNext();) {
				Object object = (Object) it.next();
				for (Class<?> exp : expected) {
					assertTrue(exp.isAssignableFrom(object.getClass()));
				}

				for (Class<?> exp : nonExpected) {
					assertTrue(!exp.isAssignableFrom(object.getClass()));
				}
			}
		}
	}

	private class TestRuleImpl extends Rule {

		public String getType() {
			return null;
		}

		@Override
		protected String getReportTemplatePath() {
			return null;
		}

		@Override
		public String getLabel() {
			return null;
		}

		@Override
		public Node getBodyAsNode(Document document) {
			return null;
		}

		@Override
		public void buildBodyFromNode(Node node) {
		}

		@Override
		public void trigger(PluggablePluginConfig pluginConfig,
				IPluginRuleExecutor exec) throws TigerstripeException {
		}
	}
}
