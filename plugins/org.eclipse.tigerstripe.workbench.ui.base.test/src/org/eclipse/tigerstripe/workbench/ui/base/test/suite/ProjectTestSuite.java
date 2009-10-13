package org.eclipse.tigerstripe.workbench.ui.base.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ClearExpectedAuditErrors;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.CloseProject;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewArtifacts;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.NewProject;
import org.eclipse.tigerstripe.workbench.ui.internal.perspective.TigerstripePerspectiveFactory;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

public class ProjectTestSuite extends TestCase
{
	/**
	 
	 * f) create a project.
	 * g) add artifacts & components - use stereotypes & primitive type
	 * 
	 
	 * 
	 */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(); 
        suite.addTestSuite(CleanWorkspace.class);
        
        // Bring up the Tigerstripe perspective
		openPerspective(TigerstripePerspectiveFactory.ID);
        
        // creates a new Project - do this so we are in the TES perspective
        suite.addTestSuite(NewProject.class);
        
        
        //  add artifacts to our project
        suite.addTestSuite(NewArtifacts.class);
        
        suite.addTestSuite(ClearExpectedAuditErrors.class);
        
        // close Project
        suite.addTestSuite(CloseProject.class);
        
        return suite;
    }
    
	private static void openPerspective(String perspId) {
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			workbench.showPerspective(perspId, workbench
					.getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			EclipsePlugin.log(e);
		}
	}

}
