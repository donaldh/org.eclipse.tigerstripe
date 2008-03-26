package org.eclipse.tigerstripe.workbench.ui.base.test.wizards.generator;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.base.test.editors.helper.EditorHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.wizards.project.NewProjectWizardHelper;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

public class TestNewGeneratorProjectWizardOperations extends TestCase{
	
	private static final String PROJECT_NAME = "DummyPluginProject";
	private IProject projectHandle;
	private TestNewPluginProjectWizardPage pageOne;

	public final void setUp() throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		IPath defaultDirectoryPath = Platform.getLocation();
		String projectDirectory = defaultDirectoryPath.toOSString();

		projectHandle = root.getProject(PROJECT_NAME);
		
		pageOne = new TestNewPluginProjectWizardPage(null, null);
		
		pageOne.setProjectNewProjectDetails(PROJECT_NAME, projectDirectory+File.separator+PROJECT_NAME);
		pageOne.setProjectHandle(projectHandle);
	}


	public final void tearDown() throws Exception {
		NewProjectWizardHelper.removeProject(PROJECT_NAME);

	}
	
	public final void testNewPluginProjectWizard()
	throws TigerstripeException, InvocationTargetException,
	InterruptedException {
		NewPluginProjectWizardHelper.newPluginProjectWizardCreate(pageOne);
		NewPluginProjectWizardHelper.assertValidPluginProject(pageOne.getProjectHandle().getName());
		IFile descriptor = (IFile) pageOne.getProjectHandle().findMember("ts-plugin.xml");

		try {
			
			 IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();


			 EditorPart editor = (EditorPart) page.openEditor(new FileEditorInput(descriptor), 
					 "org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptorEditor");

			
			
			
			IEditorPart part = EditorHelper.getEditorPartForPluginDescriptor(projectHandle);
			Assert.assertNotNull("Editor part is null", part);
		}catch (Exception e){
			Assert.fail("Failed to open editor "+e.getMessage());
		}
	}

}
