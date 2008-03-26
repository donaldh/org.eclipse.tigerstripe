package org.eclipse.tigerstripe.workbench.ui.base.test.wizards.artifacts;

import junit.framework.Assert;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.NewArtifactWizard;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.NewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.entity.NewEntityWizard;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.entity.NewEntityWizardPage;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.wizards.project.NewProjectWizardHelper;
import org.eclipse.ui.PlatformUI;

/**
 * This helper provides a set of convenience methods to create/delete/edit
 * artifacts
 * 
 * @author Eric Dillon
 * 
 */
public class NewArtifactHelper {

	/**
	 * Creates an artifact for the given specs. It uses the corresponding wizard
	 * for creation behind the scenes.
	 * 
	 * @param packageName
	 * @param name
	 * @param artifactType
	 * @return
	 * @throws TigerstripeException
	 */
	public static final IAbstractArtifact createArtifactWithWizard(String packageName,
			String name, Class artifactType, IProject project) throws Exception {
		NewArtifactWizardPage page = null;
		if (artifactType == IManagedEntityArtifact.class) {
			page = new NewEntityWizardPage();
		} else {
			throw new IllegalArgumentException("Un-supported artifactType: "
					+ artifactType);
		}
		IStructuredSelection ssel = new StructuredSelection(
				new Object[] { project });
		page.init(ssel);

		String fqn = name;
		if (packageName != null && packageName.length() != 0) {
			fqn = packageName + "." + name;
		}
		page.setArtifactName(name, true);

		IJavaProject jProject = JavaCore.create(project);
		IPackageFragmentRoot root = jProject.getAllPackageFragmentRoots()[0];
		page.setArtifactPackageFragment(root.createPackageFragment(packageName,
				true, new NullProgressMonitor()));

		NewArtifactWizard wizard = null;
		if (artifactType == IManagedEntityArtifact.class) {
			wizard = new NewEntityWizard();
		}
		wizard.init(PlatformUI.getWorkbench(), ssel);
		IRunnableWithProgress runnable = wizard.getRunnable(page);
		runnable.run(new NullProgressMonitor());

		// We need to wait until the openArtifact operation completes or else
		// the artifact manager is not notified of the newly created artifact
		// THIS IS UGLY!
		refreshArtifactMgr(fqn, project);

		IAbstractArtifact artifact = assertValidArtifact(fqn, project.getName());
		return artifact;
	}

	/**
	 * Asserts that the given fqn corresponds to a valid artifact in the given
	 * project of the workspace
	 * 
	 * @param fqn
	 * @param projectName
	 */
	public static IAbstractArtifact assertValidArtifact(String fqn,
			String projectName) {
		try {
			IAbstractTigerstripeProject aTSProject = NewProjectWizardHelper.getProject(projectName);
			if (aTSProject instanceof ITigerstripeModelProject) {
				ITigerstripeModelProject tsProject = (ITigerstripeModelProject) aTSProject;
				IArtifactManagerSession session = tsProject
						.getArtifactManagerSession();
				IAbstractArtifact artifact = session
						.getArtifactByFullyQualifiedName(fqn);
				if (artifact == null)
					Assert.fail("'" + fqn
							+ "' is not a valid artifact in project '"
							+ projectName + "'.");
				else
					return artifact;
			} else {
				Assert.fail("'" + projectName
						+ "' is not a valid model project.");
			}
		} catch (TigerstripeException e) {
			Assert.fail("Can't determine whether artifact '" + fqn
					+ " in '" + projectName + "' exists: " + e.getMessage());
		}
		return null;
	}

	public static IAbstractArtifact assertValidTSArtifact(String fqn,
			String projectName, Class artifactClass) {
		IAbstractArtifact artifact = assertValidArtifact(fqn, projectName);
		if (!artifactClass.isInstance(artifact)) {
			Assert.fail(fqn + " in '" + projectName + "' is not a "
					+ artifactClass.getCanonicalName() + "It is a "
					+ artifact.getClass().getCanonicalName());
		}
		return artifact;
	}

	/**
	 * 	This method shouldn't be necessary but since the wizards don't use the proper
	 * save mechanism for creating artifacts, the artifact mgr is currently notified manually, 
	 * here for testing, or by the "opening of the editor" in the real scenario.
	 * 
	 * @param fqn
	 * @param project
	 * @throws Exception
	 */
	public static void refreshArtifactMgr(String fqn, IProject project)
			throws Exception {
		ITigerstripeModelProject art = (ITigerstripeModelProject) EclipsePlugin.getITigerstripeProjectFor(project);
		art.getArtifactManagerSession().refresh(true, new NullProgressMonitor());

	}
}
