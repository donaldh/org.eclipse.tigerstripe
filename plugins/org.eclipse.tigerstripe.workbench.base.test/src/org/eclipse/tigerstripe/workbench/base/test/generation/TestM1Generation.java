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
package org.eclipse.tigerstripe.workbench.base.test.generation;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.utils.M1ProjectHelper;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestM1Generation extends TestCase {

	private ITigerstripeModelProject project;
	private ITigerstripeM1GeneratorProject generator;

	private static final String MODEL = "TestM1Generation";
	private static final String GENERATOR = "TestM1GenerationGenerator";

	@Override
	protected void setUp() throws Exception {
		project = ModelProjectHelper.createModelProject(MODEL);
		generator = M1ProjectHelper.createM1Project(GENERATOR, true);
	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);

		if (generator != null && generator.exists())
			generator.delete(true, null);
	}

	public void testDeploy() throws TigerstripeException {

	}
}
