package org.eclipse.tigerstripe.workbench.ui.base.test.wizards.generator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.ui.base.test.wizards.project.TestNewProjectWizard;
import org.eclipse.tigerstripe.workbench.ui.base.test.wizards.project.TestNewProjectWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class NewPluginProjectWizardHelper {

	/**

	 * 
	 * @param pageOne
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	public static void newPluginProjectWizardCreate(
			final TestNewPluginProjectWizardPage pageOne)
			throws InterruptedException, InvocationTargetException {
		TestNewPluginProjectWizard wizard = new TestNewPluginProjectWizard();
		wizard.addPage(pageOne);
		
		wizard.createNewProjectWizard(pageOne);
	}
	
	public static void assertValidPluginProject(String projectName){
		
	}
	
}
