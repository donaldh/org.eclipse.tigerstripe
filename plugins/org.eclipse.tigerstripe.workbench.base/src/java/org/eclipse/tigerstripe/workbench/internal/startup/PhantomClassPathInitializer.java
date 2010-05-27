package org.eclipse.tigerstripe.workbench.internal.startup;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProjectMgr;

public class PhantomClassPathInitializer extends ClasspathVariableInitializer {

	public PhantomClassPathInitializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(String variable) {
		String pathStr = PhantomTigerstripeProjectMgr.getInstance()
			.getPhantomURI().getPath()
			+ ITigerstripeConstants.PHANTOMLIB_DEF;
		Path phantomPath = new Path(pathStr);
		try {
			JavaCore.setClasspathVariable(ITigerstripeConstants.PHANTOMLIB,
					phantomPath, null);
			if (!phantomPath.toFile().exists()) {
				IStatus status = new Status(
						IStatus.ERROR,
						BasePlugin.getPluginId(),
						222,
						ITigerstripeConstants.PHANTOMLIB + " couldn't be resolved.",
						null);
				BasePlugin.log(status);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			IStatus status = new Status(
					IStatus.ERROR,
					BasePlugin.getPluginId(),
					222,
					"Error setting up "+ITigerstripeConstants.PHANTOMLIB + " classPathVariable.",
					e);
			BasePlugin.log(status);
		}
	}

}
