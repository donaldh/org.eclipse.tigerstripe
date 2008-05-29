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
package org.eclipse.tigerstripe.workbench.base.test.adapt;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestAdapters extends TestCase {

	private ITigerstripeModelProject project;
	private final static String PROJECTNAME = "TestAdapters";

	@Override
	protected void setUp() throws Exception {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName(PROJECTNAME);
		project = (ITigerstripeModelProject) TigerstripeCore.createProject(
				projectDetails, null, ITigerstripeModelProject.class, null,
				null);
	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);
	}

	public void testProjectAdapters() throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource res = root.findMember(PROJECTNAME);

		ITigerstripeModelProject mp = (ITigerstripeModelProject) res
				.getAdapter(ITigerstripeModelProject.class);
		assertNotNull(mp);

		IJavaProject jProject = JavaCore.create((IProject) res);
		mp = (ITigerstripeModelProject) jProject
				.getAdapter(ITigerstripeModelProject.class);
		assertNotNull(mp);
	}

	public void testManagedEntityArtifactAdapters() throws Exception {
		IManagedEntityArtifact me = (IManagedEntityArtifact) project
				.getArtifactManagerSession().makeArtifact(
						IManagedEntityArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.ME");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		IManagedEntityArtifact me2 = (IManagedEntityArtifact) res
				.getAdapter(IManagedEntityArtifact.class);
		assertTrue(art == me2);
		
		IModelComponent comp = (IModelComponent) res.getAdapter(IModelComponent.class);
		assertTrue(art == comp);
	}

	public void testDatatypeArtifactAdapters() throws Exception {
		IDatatypeArtifact me = (IDatatypeArtifact) project
				.getArtifactManagerSession().makeArtifact(
						IDatatypeArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.DA");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		IDatatypeArtifact me2 = (IDatatypeArtifact) res
				.getAdapter(IDatatypeArtifact.class);
		assertTrue(art == me2);

		IManagedEntityArtifact me3 = (IManagedEntityArtifact) res
				.getAdapter(IManagedEntityArtifact.class);
		assertTrue(me3 == null);

	}

	public void testEnumArtifactAdapters() throws Exception {
		IEnumArtifact me = (IEnumArtifact) project.getArtifactManagerSession()
				.makeArtifact(IEnumArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.En");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		IEnumArtifact me2 = (IEnumArtifact) res.getAdapter(IEnumArtifact.class);
		assertTrue(art == me2);
	}

	public void testExceptionArtifactAdapters() throws Exception {
		IExceptionArtifact me = (IExceptionArtifact) project
				.getArtifactManagerSession().makeArtifact(
						IExceptionArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.Ex");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		IExceptionArtifact me2 = (IExceptionArtifact) res
				.getAdapter(IExceptionArtifact.class);
		assertTrue(art == me2);
	}

	public void testSessionArtifactAdapters() throws Exception {
		ISessionArtifact me = (ISessionArtifact) project
				.getArtifactManagerSession().makeArtifact(
						ISessionArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.Se");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		ISessionArtifact me2 = (ISessionArtifact) res
				.getAdapter(ISessionArtifact.class);
		assertTrue(art == me2);
	}

	public void testQueryArtifactAdapters() throws Exception {
		IQueryArtifact me = (IQueryArtifact) project
				.getArtifactManagerSession().makeArtifact(
						IQueryArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.Qu");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		IQueryArtifact me2 = (IQueryArtifact) res
				.getAdapter(IQueryArtifact.class);
		assertTrue(art == me2);
	}

	public void testUpdateProcedureArtifactAdapters() throws Exception {
		IUpdateProcedureArtifact me = (IUpdateProcedureArtifact) project
				.getArtifactManagerSession().makeArtifact(
						IUpdateProcedureArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.Up");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		IUpdateProcedureArtifact me2 = (IUpdateProcedureArtifact) res
				.getAdapter(IUpdateProcedureArtifact.class);
		assertTrue(art == me2);
	}

	public void testEventArtifactAdapters() throws Exception {
		IEventArtifact me = (IEventArtifact) project
				.getArtifactManagerSession().makeArtifact(
						IEventArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.Ev");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		IEventArtifact me2 = (IEventArtifact) res
				.getAdapter(IEventArtifact.class);
		assertTrue(art == me2);
	}

	public void testAssociationArtifactAdapters() throws Exception {
		IAssociationArtifact me = (IAssociationArtifact) project
				.getArtifactManagerSession().makeArtifact(
						IAssociationArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.Ass");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		IAssociationArtifact me2 = (IAssociationArtifact) res
				.getAdapter(IAssociationArtifact.class);
		assertTrue(art == me2);
	}

	public void testAssociationClassArtifactAdapters() throws Exception {
		IAssociationClassArtifact me = (IAssociationClassArtifact) project
				.getArtifactManagerSession().makeArtifact(
						IAssociationClassArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.AssC");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		IAssociationClassArtifact me2 = (IAssociationClassArtifact) res
				.getAdapter(IAssociationClassArtifact.class);
		assertTrue(art == me2);
	}

	public void testDependencyArtifactAdapters() throws Exception {
		IDependencyArtifact me = (IDependencyArtifact) project
				.getArtifactManagerSession().makeArtifact(
						IDependencyArtifact.class.getName());
		me.setFullyQualifiedName("com.mycompany.AssC");
		me.doSave(null);

		((IProject) project.getAdapter(IProject.class)).refreshLocal(
				IResource.DEPTH_INFINITE, null);

		IResource res = (IResource) me.getAdapter(IResource.class);
		assertNotNull(res);

		IAbstractArtifact art = (IAbstractArtifact) res
				.getAdapter(IAbstractArtifact.class);
		assertNotNull(art);
		IDependencyArtifact me2 = (IDependencyArtifact) res
				.getAdapter(IDependencyArtifact.class);
		assertTrue(art == me2);
	}
}
