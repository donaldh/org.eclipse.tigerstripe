package org.eclipse.tigerstripe.workbench.internal.core.model;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ProxyUtils {

	public static IAbstractArtifact reload(IAbstractArtifact artifact) {

		if (artifact == null) {
			return null;
		}

		try {
			ITigerstripeModelProject project = artifact.getProject();

			if (project == null) {
				return artifact;
			}

			IArtifactManagerSession artifactManagerSession = project
					.getArtifactManagerSession();

			if (artifactManagerSession == null) {
				return artifact;
			}

			ArtifactManager artifactManager = artifactManagerSession
					.getArtifactManager();

			if (artifactManager == null) {
				return artifact;
			}

			IAbstractArtifactInternal actual = artifactManager
					.getArtifactByFullyQualifiedName(
							artifact.getFullyQualifiedName(), false,
							new NullProgressMonitor());

			if (ContextProjectAwareProxy.isContextualProxy(artifact)) {
				ContextProjectAwareProxy.changeTraget(artifact, actual);
				return artifact;
			} else {
				return actual;
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return artifact;
		}
	}
}
