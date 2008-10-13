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
package org.eclipse.tigerstripe.workbench.base.test.annotation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.ResourcesPlugin;
//import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.annotation.internal.core.AnnotationManager;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;

/**
 * @author Yuri Strot
 * 
 */
public abstract class AbstractTigerstripeTestCase extends TestCase {

	protected IAbstractTigerstripeProject createModelProject(String projectName)
			throws TigerstripeException {
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		IAbstractTigerstripeProject aProject = TigerstripeCore.createProject(
				projectName, details, null, ITigerstripeModelProject.class,
				null, new NullProgressMonitor());
		return aProject;
	}

	protected void deleteModelProject(IAbstractTigerstripeProject project)
			throws TigerstripeException {
		project.delete(true, new NullProgressMonitor());
	}

	protected boolean createEachArtifactType(
			IAbstractTigerstripeProject aProject) throws TigerstripeException {
		// IAbstractTigerstripeProject aProject =
		// createModelProject("testCreateRemoveEachArtifactType");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artifactHelper = new ArtifactTestHelper(project);

		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();
		Collection<String> supportedArtifacts = mgrSession
				.getSupportedArtifacts();
		for (String supportedArtifact : supportedArtifacts) {
			if (!supportedArtifact.endsWith("IPackageArtifact")) {

				// System.out.println("SupportedArtifact: "+supportedArtifact);

				String name = "InstanceOf" + supportedArtifact;
				String pack = "com.test";
				artifactHelper.createArtifact(supportedArtifact, name, pack);
			}
		}
		return true;
	}

	protected boolean removeEachArtifactType(
			IAbstractTigerstripeProject aProject) throws TigerstripeException {
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artifactHelper = new ArtifactTestHelper(project);
		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();

		Collection<String> supportedArtifacts = mgrSession
				.getSupportedArtifacts();
		for (String supportedArtifact : supportedArtifacts) {
			if (!supportedArtifact.endsWith("IPackageArtifact")) {
				String name = "InstanceOf" + supportedArtifact;
				String pack = "com.test";
				String fqn = pack + "." + name;
				IAbstractArtifact artifact = mgrSession
						.getArtifactByFullyQualifiedName(fqn);
				artifactHelper.remove(artifact);
			}
		}
		return true;
	}

	protected Collection<IAbstractArtifact> getArtifacts(
			IAbstractTigerstripeProject aProject) throws TigerstripeException {
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artifactHelper = new ArtifactTestHelper(project);
		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();

		Collection<String> supportedArtifacts = mgrSession
				.getSupportedArtifacts();
		Collection<IAbstractArtifact> artifacts = new LinkedList<IAbstractArtifact>();
		for (String supportedArtifact : supportedArtifacts) {
			if (!supportedArtifact.endsWith("IPackageArtifact")) {
				String name = "InstanceOf" + supportedArtifact;
				String pack = "com.test";
				String fqn = pack + "." + name;
				IAbstractArtifact artifact = mgrSession
						.getArtifactByFullyQualifiedName(fqn);
				artifacts.add(artifact);
			}
		}
		return artifacts;
	}

	protected Collection<IAbstractArtifact> getAllArtifacts(
			IAbstractTigerstripeProject aProject) throws TigerstripeException {
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();

		Collection<String> supportedArtifacts = mgrSession
				.getSupportedArtifacts();
		Collection<IAbstractArtifact> artifacts = new LinkedList<IAbstractArtifact>();
		for (String supportedArtifact : supportedArtifacts) {
			IQueryArtifactsByType query = (IQueryArtifactsByType) mgrSession
					.makeQuery(IQueryArtifactsByType.class.getName());
			query.setIncludeDependencies(true);
			query.setArtifactType(supportedArtifact);

			Collection<IAbstractArtifact> entities = mgrSession
					.queryArtifact(query);
			// System.out.println(supportedArtifact+": "+entities);
			if (entities != null && !entities.isEmpty())
				artifacts.addAll(entities);
		}
		return artifacts;
	}

	protected void cleanUpAnnotations(ITigerstripeModelProject aProject)
			throws TigerstripeException {
		List<Object> annotationTargets = new LinkedList<Object>();
		annotationTargets.add(aProject);
		Collection<IAbstractArtifact> artifacts = getAllArtifacts(aProject);
		annotationTargets.addAll(artifacts);
		for (IAbstractArtifact aa : artifacts) {
			annotationTargets.addAll(removeNulls(aa
					.getContainedModelComponents()));
		}
		for (Object object : annotationTargets) {
			AnnotationManager.getInstance().removeAnnotations(object);
		}
	}

	<T> Collection<T> removeNulls(Collection<T> in) {
		for (Iterator<T> i = in.iterator(); i.hasNext();)
			if (i.next() == null)
				i.remove();
		return in;
	}
}
