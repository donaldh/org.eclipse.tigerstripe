/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Jim Strawn
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.internal.core.model.export;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class OverwriteArtifactFinderTest extends TestCase {

	private ITigerstripeModelProject source;

	private ITigerstripeModelProject destination;
	
	protected void setUp() throws Exception {
		
		source = ModelProjectHelper.createModelProject("SourceProject", true);
		destination = ModelProjectHelper.createModelProject("DestinationProject", false);
	}

	protected void tearDown() throws Exception {
		
		if (source != null && source.exists()) {
			source.delete(true, new NullProgressMonitor());
		}

		if (destination != null && destination.exists()) {
			destination.delete(true, new NullProgressMonitor());
		}
	}
	
	public void testGetArtifactsList() throws Exception {
		
		List<IAbstractArtifact> artifacts = OverwriteArtifactFinder.getArtifactsList(source, destination);
		assertNotNull("Artifacts list is null", artifacts);
		assertEquals(4, artifacts.size()); // two for pkg, two artifacts
		
		assertTrue(artifactExistsByFqn(artifacts, ModelProjectHelper.M1));
		assertTrue(artifactExistsByFqn(artifacts, ModelProjectHelper.M2));
		assertTrue(artifactExistsByFqn(artifacts, "com"));
		assertTrue(artifactExistsByFqn(artifacts, "com.mycompany"));
		assertFalse(artifactExistsByFqn(artifacts, ModelProjectHelper.M3));
		assertFalse(artifactExistsByFqn(artifacts, ModelProjectHelper.AC1));
		assertFalse(artifactExistsByFqn(artifacts, ModelProjectHelper.AS1));
		
	}
	
	public void testGetArtifactsListWithEmptyDestination() throws Exception {
		
		// overwrite destination project
		if (destination != null && destination.exists()) {
			destination.delete(true, new NullProgressMonitor());
		}
		destination = ModelProjectHelper.createEmptyModelProject("DestinationProject");
		List<IAbstractArtifact> artifacts = OverwriteArtifactFinder.getArtifactsList(source, destination);
		
		assertNotNull("Artifacts list is null", artifacts);
		assertEquals(0, artifacts.size());
		
	}

	private boolean artifactExistsByFqn(List <IAbstractArtifact> artifacts, String fqn) {
		
		for (IAbstractArtifact artifact: artifacts) {
				
			if(artifact.getFullyQualifiedName().equals(fqn)) {
				return true;
			}
		}
		return false;
	}
	
	
}
