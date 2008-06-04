package org.eclipse.tigerstripe.workbench.ui.base.test.utils;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;
import org.osgi.framework.Bundle;

public class FileUtils {

	public static void copyResourceToWorkspace(String resource, String dir, String file) throws IOException, CoreException{
		// Where can we get it from ?			
		Bundle bundle = org.eclipse.core.runtime.Platform.getBundle("org.eclipse.tigerstripe.workbench.ui.base.test");
		URL resourceURL = bundle.getEntry("resources/"+resource);	
		
		// Where to put it is easy!
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource templatesDir = root.findMember(dir);
		IFile newFile = root.getFile(new Path(templatesDir.getFullPath()+"/"+file));
		
		newFile.create(resourceURL.openStream(), 0, new NullProgressMonitor());
		
	}
	
	public static void appendResourceToWorkspace(String resource, String dir, String file) throws IOException, CoreException{
		// Where can we get it from ?			
		Bundle bundle = org.eclipse.core.runtime.Platform.getBundle("org.eclipse.tigerstripe.workbench.ui.base.test");
		URL resourceURL = bundle.getEntry("resources/"+resource);	
		
		// Where to put it is easy!
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource templatesDir = root.findMember(dir);
		IFile newFile = root.getFile(new Path(templatesDir.getFullPath()+"/"+file));
		
		newFile.appendContents(resourceURL.openStream(), 0, new NullProgressMonitor());
		
	}
	
}
