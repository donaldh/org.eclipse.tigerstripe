/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;

public enum ArtifactAuditorFactory {

	INSTANCE;

	private final IConfigurationElement[] contributedElements;

	private ArtifactAuditorFactory() {
		contributedElements = Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor(
						"org.eclipse.tigerstripe.workbench.base.customArtifactAuditor");

	}

	public IArtifactAuditor newArtifactAuditor(IProject project,
			IAbstractArtifact artifact) throws TigerstripeException {

		List<IArtifactAuditor> auditors = new ArrayList<IArtifactAuditor>(
				contributedElements.length + 2);

		auditors.add(new CommonArtifactAuditor());
		for (IConfigurationElement e : contributedElements) {
			try {
				auditors.add(wrapSafeable((IArtifactAuditor) e
						.createExecutableExtension("auditorClass")));
			} catch (CoreException e1) {
				TigerstripeProjectAuditor.reportError(
						"Invalid custom audit definitions.", project, 222);
			}
		}
		auditors.add(getAuditor(artifact));

		IArtifactAuditor result = new CompositeArtifactAuditor(auditors);
		result.setDetails(project, artifact);
		return result;
	}

	private IArtifactAuditor wrapSafeable(final IArtifactAuditor delegat) {

		return new IArtifactAuditor() {

			public void setDetails(IProject project, IAbstractArtifact artifact) {
				delegat.setDetails(project, artifact);
			}

			public void run(final IProgressMonitor monitor) {
				SafeRunner.run(new ISafeRunnable() {
					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						delegat.run(monitor);
					}
				});
			}

		};
	}

	private IArtifactAuditor getAuditor(IAbstractArtifact artifact)
			throws TigerstripeException {
		if (artifact instanceof IManagedEntityArtifact) {
			return new ManagedEntityArtifactAuditor();
		} else if (artifact instanceof IDatatypeArtifact) {
			return new DatatypeArtifactAuditor();
		} else if (artifact instanceof IEnumArtifact) {
			return new EnumerationArtifactAuditor();
		} else if (artifact instanceof IEventArtifact) {
			return new EventArtifactAuditor();
		} else if (artifact instanceof IExceptionArtifact) {
			return new ExceptionArtifactAuditor();
		} else if (artifact instanceof IQueryArtifact) {
			return new QueryArtifactAuditor();
		} else if (artifact instanceof ISessionArtifact) {
			return new SessionFacadeArtifactAuditor();
		} else if (artifact instanceof IUpdateProcedureArtifact) {
			return new UpdateProcedureArtifactAuditor();
		} else if (artifact instanceof IAssociationClassArtifact) {
			return new AssociationClassArtifactAuditor();
		} else if (artifact instanceof IAssociationArtifact) {
			return new AssociationArtifactAuditor();
		} else if (artifact instanceof IDependencyArtifact) {
			return new DependencyArtifactAuditor();
		} else if (artifact instanceof IPackageArtifact) {
			return new PackageArtifactAuditor();
		} else if (artifact instanceof IPrimitiveTypeArtifact) {
			return IArtifactAuditor.EMPTY;
		} else {
			throw new TigerstripeException(
					"Internal Error, can't find artifact auditor for " + artifact);
		}
	}
}
