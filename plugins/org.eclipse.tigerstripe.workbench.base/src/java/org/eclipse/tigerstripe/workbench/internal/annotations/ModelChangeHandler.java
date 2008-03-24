/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.annotations;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationSchemeRegistry;
import org.eclipse.tigerstripe.annotations.AnnotationStore;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class ModelChangeHandler implements IArtifactChangeListener {

	public void artifactAdded(IAbstractArtifact artifact) {
		// nothing to do
	}

	public void artifactChanged(IAbstractArtifact artifact) {
		// I guess we should figure out if there is any URI contained in this
		// artifact that should be cleaned up.
		// Unfortunately, nothing to compare to, so can't do.
		// This will need to wait until EMF port.
	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		// Remove the corresponding URI entry from store
		ModelComponentAnnotable annotable = new ModelComponentAnnotable(
				artifact);
		AnnotationSchemeRegistry registry = AnnotationSchemeRegistry.eINSTANCE;

		try {
			for (IAnnotationScheme scheme : registry
					.getDefinedSchemes(annotable.getURI())) {
				AnnotationStore store = annotable.getStore(scheme);
				store.uriRemoved(annotable.getURI());
			}
		} catch (AnnotationCoreException e) {
			BasePlugin.log(e);
		}
	}

	public void artifactRenamed(final IAbstractArtifact artifact,
			final String fromFQN) {

		Job renameJob = new Job("Annotation update job(rename)") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// Rename the corresponding URI entry from store
				ModelComponentAnnotable annotable = new ModelComponentAnnotable(
						artifact);
				AnnotationSchemeRegistry registry = AnnotationSchemeRegistry.eINSTANCE;
				try {
					String oldURI = annotable.getURIFor(fromFQN).toString();

					for (IAnnotationScheme scheme : registry
							.getDefinedSchemes(annotable.getURI())) {
						AnnotationStore store = annotable.getStore(scheme);
						store.uriChanged(oldURI, annotable.getURI());
					}
				} catch (AnnotationCoreException e) {
					BasePlugin.log(e);
					return Status.CANCEL_STATUS;
				} catch (TigerstripeException e) {
					BasePlugin.log(e);
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}
		};

		renameJob.schedule();
	}

	public void managerReloaded() {
		// TODO Auto-generated method stub

	}

}
