package org.eclipse.tigerstripe.workbench.model;

import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ArtifactUtils {

	public static IArtifactManagerSession getSession(IAbstractArtifact artifact) {
		try {
			ITigerstripeModelProject project = artifact.getProject();
			if (project != null) {
				return project.getArtifactManagerSession();
			}
		} catch (Exception e) {
			BasePlugin.log(e);
		}
		return null;
	}

	public static ArtifactManager getManager(IAbstractArtifact artifact) {
		IArtifactManagerSession session = getSession(artifact);
		if (session != null) {
			return session.getArtifactManager();
		}
		return null;
	}
}
