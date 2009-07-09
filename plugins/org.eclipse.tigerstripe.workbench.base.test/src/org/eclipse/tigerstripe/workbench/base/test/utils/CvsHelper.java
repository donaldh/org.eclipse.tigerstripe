package org.eclipse.tigerstripe.workbench.base.test.utils;

import junit.framework.Assert;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.team.internal.ccvs.core.CVSException;
import org.eclipse.team.internal.ccvs.core.ICVSFolder;
import org.eclipse.team.internal.ccvs.core.ICVSRepositoryLocation;
import org.eclipse.team.internal.ccvs.core.client.Checkout;
import org.eclipse.team.internal.ccvs.core.client.Command;
import org.eclipse.team.internal.ccvs.core.client.Session;
import org.eclipse.team.internal.ccvs.core.resources.CVSWorkspaceRoot;
import org.eclipse.team.internal.ccvs.core.util.KnownRepositories;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A Helper class to get projects out of CVS for testing purposes
 * 
 * @author erdillon
 * 
 */
@SuppressWarnings("restriction")
public class CvsHelper {

	private String cvsRoot = null;
	private ICVSRepositoryLocation location = null;
	private ICVSFolder root = null;

	public CvsHelper(String cvsRoot) throws CVSException {
		this.cvsRoot = cvsRoot;
		this.location = KnownRepositories.getInstance().getRepository(
				getCvsRoot());
		this.root = CVSWorkspaceRoot.getCVSFolderFor(ResourcesPlugin
				.getWorkspace().getRoot());
	}

	public String getCvsRoot() {
		return this.cvsRoot;
	}

	/**
	 * Checks out of CVS a specific module.
	 * 
	 * If the project being checkout is a TigerstripeModelProject, a clean audit
	 * is automatically triggered.
	 * 
	 * @param remoteModule
	 *            - the name on the CVS repository of the module to checkout
	 * @param localDirname
	 *            - the local project dirname (at the root of the workspace
	 * 
	 */
	public void checkout(String remoteModule, String localDirname)
			throws Exception {
		Session session = new Session(location, root);
		session.open(null);
		Command.CHECKOUT.execute(session, Command.NO_GLOBAL_OPTIONS,
				new Command.LocalOption[] {
						Checkout.makeDirectoryNameOption(localDirname),
						Command.PRUNE_EMPTY_DIRECTORIES },
				new String[] { remoteModule }, null, null);
		session.close();

		// Then need to make sure the project is build
		IProject testProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(localDirname);
		Assert.assertTrue(testProject != null && testProject.exists());

		ITigerstripeModelProject project = (ITigerstripeModelProject) testProject
				.getAdapter(ITigerstripeModelProject.class);
		if (project != null) {
			cleanAuditNow(project, null);
		}
	}

	@SuppressWarnings("deprecation")
	private void cleanAuditNow(ITigerstripeModelProject tsProject,
			IProgressMonitor monitor) throws Exception {
		if (monitor == null)
			monitor = new NullProgressMonitor();

		try {
			monitor.beginTask("Refreshing project:"
					+ ((ITigerstripeModelProject) tsProject).getName(), 5);

			((ITigerstripeModelProject) tsProject).getArtifactManagerSession()
					.setBroadcastMask(IArtifactChangeListener.NOTIFY_NONE);
			((ITigerstripeModelProject) tsProject).getArtifactManagerSession()
					.refreshAll(true, monitor);
			monitor.done();
		} finally {
			((ITigerstripeModelProject) tsProject).getArtifactManagerSession()
					.setBroadcastMask(IArtifactChangeListener.NOTIFY_ALL);
		}

	}

}
