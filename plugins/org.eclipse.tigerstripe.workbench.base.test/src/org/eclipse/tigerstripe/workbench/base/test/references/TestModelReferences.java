package org.eclipse.tigerstripe.workbench.base.test.references;

import junit.framework.Assert;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.base.test.AbstractTigerstripeTestCase;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestModelReferences extends AbstractTigerstripeTestCase {

	private final static String ID1 = "org.eclipse.tigerstripe.sample.aProject";
	private final static String ID2 = "org.eclipse.tigerstripe.sample.bProject";

	ITigerstripeModelProject aProject = null;
	ITigerstripeModelProject bProject = null;

	public void setUp() throws Exception {
		runInWorkspace(new SafeRunnable() {
			
			public void run() throws Exception {
				aProject = ModelProjectHelper.createEmptyModelProject("aProject", ID1);
				assertTrue(aProject instanceof ITigerstripeModelProject);
				bProject = ModelProjectHelper.createEmptyModelProject("bProject", null);
				assertTrue(bProject instanceof ITigerstripeModelProject);
			}
		});
	}

	public void tearDown() throws Exception {
		workspace.run(new IWorkspaceRunnable() {
			
			public void run(IProgressMonitor monitor) throws CoreException {
				final IResource proja = workspace.getRoot().getProject("aProject");
				if (proja.exists()) {
					proja.delete(true, null);
				}
				
				final IResource projb = workspace.getRoot().findMember("bProject");
				if (projb.exists()) {
					projb.delete(true, null);
				}
			}
		}, null);
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
		runInWorkspace(new SafeRunnable() {
			public void run() throws Exception {
				final ITigerstripeModelProject wc = (ITigerstripeModelProject) aProject
						.makeWorkingCopy(new NullProgressMonitor());
				wc.setModelId(ID2);
				wc.commit(new NullProgressMonitor());
			}
		});
		
		final ModelReference ref2 = new ModelReference(ID2);
		Assert.assertTrue(ref2.isWorkspaceReference());
		Assert.assertFalse(ref2.isProjectContextReference());
		Assert.assertTrue(ref2.isResolved());
		Assert.assertEquals(aProject, ref2.getResolvedModel());

		final ModelReference ref1 = new ModelReference(ID1);
		Assert.assertTrue(ref1.isWorkspaceReference());
		Assert.assertFalse(ref1.isProjectContextReference());
		Assert.assertFalse(ref1.isResolved());
		
		runInWorkspace(new SafeRunnable() {
			public void run() throws Exception {
				final ITigerstripeModelProject wc = (ITigerstripeModelProject) aProject
						.makeWorkingCopy(new NullProgressMonitor());
				wc.setModelId(ID1);
				wc.commit(new NullProgressMonitor());
			}
		});
		
		final ModelReference anotherRef1 = new ModelReference(ID1);
		Assert.assertTrue(anotherRef1.isWorkspaceReference());
		Assert.assertFalse(anotherRef1.isProjectContextReference());
		Assert.assertTrue(anotherRef1.isResolved());
		Assert.assertEquals(aProject, anotherRef1.getResolvedModel());

	}
}
