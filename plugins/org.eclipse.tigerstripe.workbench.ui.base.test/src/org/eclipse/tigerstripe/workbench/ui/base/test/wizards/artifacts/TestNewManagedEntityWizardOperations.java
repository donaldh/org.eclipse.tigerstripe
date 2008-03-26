package org.eclipse.tigerstripe.workbench.ui.base.test.wizards.artifacts;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.entity.NewEntityWizard;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.entity.NewEntityWizardPage;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.base.test.wizards.project.NewProjectWizardHelper;
import org.eclipse.ui.PlatformUI;

/**
 * Tests various combinations of use of the NewEntityWizardPage
 * 
 * @author Eric Dillon
 * 
 */
public class TestNewManagedEntityWizardOperations extends TestCase{

	private static String ENTITY1 = "Entity1";

	private static String PROJECT = "EntityTestProject";


	public  final void setUp() throws Exception {
		NewProjectWizardHelper.createModelProject(PROJECT);
	}


	public  final void tearDown() throws Exception {
		NewProjectWizardHelper.removeProject(PROJECT);
	}

	public static void setProjectName(String name){
		PROJECT = name;
	}
	
	public static void setEntityName(String name){
		ENTITY1 = name;
	}
	
	private IProject getProject() throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(PROJECT);
		return project;
	}


	public final void testNewEntityWizardPageDefault() throws Exception {
		NewEntityWizardPage page = new NewEntityWizardPage();
		IStructuredSelection ssel = new StructuredSelection(
				new Object[] { getProject() });
		page.init(ssel);

		page.setArtifactName(ENTITY1, true);
		NewEntityWizard wizard = new NewEntityWizard();
		wizard.init(PlatformUI.getWorkbench(), ssel);
		IRunnableWithProgress runnable = wizard.getRunnable(page);
		runnable.run(new NullProgressMonitor());

		// We need to wait until the openArtifact operation completes or else
		// the artifact manager is not notified of the newly created artifact
		// THIS IS UGLY!
		NewArtifactHelper.refreshArtifactMgr("com.mycompany."
				+ ENTITY1, getProject());

		NewArtifactHelper.assertValidArtifact("com.mycompany." + ENTITY1, PROJECT);
	}

	/**
	 * Checks that the wizard won't let you create an Entity with the FQN that
	 * already exists in the project.
	 * 
	 * @throws Exception
	 */

	public final void testPreventDuplicateEntity() throws Exception {
		NewArtifactHelper helper = new NewArtifactHelper();
		IAbstractArtifact art1 = helper.createArtifactWithWizard(
				"com.mycompany", "EntityDup1", IManagedEntityArtifact.class,
				getProject());

		NewEntityWizardPage page = new NewEntityWizardPage();
		IStructuredSelection ssel = new StructuredSelection(
				new Object[] { getProject() });
		page.init(ssel);

		page.setArtifactName("EntityDup1", true);
		IJavaProject jProject = JavaCore.create(getProject());
		IPackageFragmentRoot root = jProject.getAllPackageFragmentRoots()[0];
		page.setArtifactPackageFragment(root
				.getPackageFragment("com.mycompany"));

		boolean f = page.isPageComplete();
		Assert.assertFalse(
				"Wizard doesn't prevent creating duplicate artifact", f);
	}

}
