package org.eclipse.tigerstripe.workbench.ui.base.test.wizards.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.project.NewProjectWizardPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.GeneralPreferencePage;

/**
 * This is a mocked-up page to accomodate testing. It remains very close to
 * the actual one in terms of behavior while it removes the GUI interaction.
 * 
 * @author Eric Dillon
 * 
 */
public class TestNewProjectWizardPage extends NewProjectWizardPage {

	private IProject projectHandle;

	private String defaultArtifactPackage = "";

	public TestNewProjectWizardPage(ISelection selection,
			ImageDescriptor image) {
		super(selection, image);
		initialize();

		Preferences store = EclipsePlugin.getDefault()
				.getPluginPreferences();

		if ("".equals(store
				.getString(GeneralPreferencePage.P_DEFAULTPACKAGE))
				|| store.getString(GeneralPreferencePage.P_DEFAULTPACKAGE) == null) {
			defaultArtifactPackage = "com.local";
		} else {
			defaultArtifactPackage = store
					.getString(GeneralPreferencePage.P_DEFAULTPACKAGE);
		}
	}

	@Override
	protected String getDefaultArtifactPackageText() {
		return defaultArtifactPackage;
	}

	@Override
	public IProject getProjectHandle() {
		return this.projectHandle;
	}

	public void setProjectHandle(IProject projectHandle) {
		this.projectHandle = projectHandle;
	}

	@Override
	protected String getProjectName() {
		return getProjectHandle().getName();
	}
}