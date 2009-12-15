package org.eclipse.tigerstripe.workbench.base.test.references;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestModelReferences extends TestCase {

	private final static String ID1 = "org.eclipse.tigerstripe.sample.aProject";
	private final static String ID2 = "org.eclipse.tigerstripe.sample.bProject";

	ITigerstripeModelProject aProject = null;
	ITigerstripeModelProject bProject = null;

	public void setUp() throws Exception {
		aProject = ModelProjectHelper.createEmptyModelProject("aProject", ID1);
		assertTrue(aProject instanceof ITigerstripeModelProject);
		bProject = ModelProjectHelper.createEmptyModelProject("bProject", null);
		assertTrue(bProject instanceof ITigerstripeModelProject);
	}

	public void tearDown() throws Exception {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IResource proja = workspace.getRoot().findMember("aProject");
		if (proja != null)
			proja.delete(true, new NullProgressMonitor());
		proja = workspace.getRoot().findMember("bProject");
		if (proja != null)
			proja.delete(true, new NullProgressMonitor());
	}

	public void testWorkspaceResolutionById() throws Exception {

		ModelReference ref = new ModelReference(ID1);
		Assert.assertTrue(ref.isWorkspaceReference());
		Assert.assertFalse(ref.isProjectContextReference());
		Assert.assertTrue(ref.isResolved());
		Assert.assertEquals(aProject, ref.getResolvedModel());

		ref = new ModelReference(ID2);
		Assert.assertTrue(ref.isWorkspaceReference());
		Assert.assertFalse(ref.isProjectContextReference());
		Assert.assertFalse(ref.isResolved());

		ref = new ModelReference("bProject");
		Assert.assertTrue(ref.isWorkspaceReference());
		Assert.assertFalse(ref.isProjectContextReference());
		Assert.assertTrue(ref.isResolved());
		Assert.assertEquals(bProject, ref.getResolvedModel());
	}

	public void testChangeModelId() throws Exception {

		ITigerstripeModelProject wc = (ITigerstripeModelProject) aProject
				.makeWorkingCopy(new NullProgressMonitor());
		wc.setModelId(ID2);
		wc.commit(new NullProgressMonitor());

		ModelReference ref = new ModelReference(ID2);
		Assert.assertTrue(ref.isWorkspaceReference());
		Assert.assertFalse(ref.isProjectContextReference());
		Assert.assertTrue(ref.isResolved());
		Assert.assertEquals(aProject, ref.getResolvedModel());

		ref = new ModelReference(ID1);
		Assert.assertTrue(ref.isWorkspaceReference());
		Assert.assertFalse(ref.isProjectContextReference());
		Assert.assertFalse(ref.isResolved());

		wc = (ITigerstripeModelProject) aProject
				.makeWorkingCopy(new NullProgressMonitor());
		wc.setModelId(ID1);
		wc.commit(new NullProgressMonitor());

		ref = new ModelReference(ID1);
		Assert.assertTrue(ref.isWorkspaceReference());
		Assert.assertFalse(ref.isProjectContextReference());
		Assert.assertTrue(ref.isResolved());
		Assert.assertEquals(aProject, ref.getResolvedModel());

	}
}
