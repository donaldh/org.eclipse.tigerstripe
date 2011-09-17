/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringDelegate;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.api.project.IPhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ChangeIdRefactoringListener implements IRefactoringChangesListener {

	private final Map<ILazyObject, URIsToChange> changes = new HashMap<ILazyObject, URIsToChange>();

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.core.refactoring.
	 * IRefactoringChangesListener #changed(org.eclipse.tigerstripe.annotation
	 * .core.refactoring.IRefactoringDelegate,
	 * org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject,
	 * org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject, int)
	 */
	public void changed(IRefactoringDelegate delegate, ILazyObject oldObject,
			ILazyObject newObject, int kind) {
		if (kind == ABOUT_TO_CHANGE) {
			ITigerstripeModelProject project = getProject(oldObject);
			if (project != null) {
				URI oldUri = getURI(project);

				ITigerstripeModelProject[] referencingProjects = getReferencingProjects(project);
				List<URI> referencingUris = new ArrayList<URI>();
				for (ITigerstripeModelProject referencingProject : referencingProjects) {
					URI referencingURI = getURI(referencingProject);
					if (referencingURI != null) {
						referencingUris.add(referencingURI);
					}
				}

				changes.put(
						oldObject,
						new URIsToChange(oldUri, referencingUris
								.toArray(new URI[referencingUris.size()])));
			}
		} else if (kind == CHANGED) {
			URIsToChange toChange = changes.get(oldObject);
			if (toChange != null) {
				ITigerstripeModelProject project = getProject(oldObject);
				if (project != null && toChange.getOldUri() != null) {
					URI newUri = getURI(project);
					if (newUri != null) {
						delegate.changed(toChange.getOldUri(), newUri, true);

						for (URI refUri : toChange.getReferencingUris()) {
							delegate.changed(
									createContextURI(refUri,
											toChange.getOldUri()),
									createContextURI(refUri, newUri), true);
						}
					}
				}
				changes.remove(oldObject);
			}
		}
	}

	private URI createContextURI(URI context, URI reference) {
		return URI.createHierarchicalURI(
				TigerstripeURIAdapterFactory.SCHEME_TS, null, null,
				new String[] { context.segment(0), reference.segment(0) },
				null, null);
	}

	private URI getURI(ITigerstripeModelProject project) {
		return getURI(null, project);
	}

	private URI getURI(ITigerstripeModelProject context,
			ITigerstripeModelProject project) {
		try {
			String[] segments = context != null ? new String[] {
					context.getModelId(), project.getModelId() }
					: new String[] { project.getModelId() };
			return URI.createHierarchicalURI(
					TigerstripeURIAdapterFactory.SCHEME_TS, null, null,
					segments, null, null);
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return null;
		}
	}

	/**
	 * @param oldObject
	 * @return
	 */
	private ITigerstripeModelProject getProject(ILazyObject object) {
		Object obj = object.getObject();
		if (obj == null)
			return null;
		ITigerstripeModelProject project = null;
		if (obj instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) obj;
			project = (ITigerstripeModelProject) adaptable
					.getAdapter(ITigerstripeModelProject.class);
		}
		if (project == null) {
			project = (ITigerstripeModelProject) Platform.getAdapterManager()
					.getAdapter(obj, ITigerstripeModelProject.class);
		}
		return project;
	}

	private ITigerstripeModelProject[] getReferencingProjects(
			ITigerstripeModelProject modelProject) {
		List<ITigerstripeModelProject> result = new ArrayList<ITigerstripeModelProject>();
		try {
			ModelReference[] referencingModels = modelProject
					.getReferencingModels(1);
			for (ModelReference referencingModel : referencingModels) {
				ITigerstripeModelProject project = referencingModel
						.getResolvedModel();
				if (project != null
						&& !(project instanceof ITigerstripeModuleProject)
						&& !(project instanceof IPhantomTigerstripeProject)) {
					result.add(project);
				}
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return result.toArray(new ITigerstripeModelProject[result.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.core.refactoring.
	 * IRefactoringChangesListener #copied(org.eclipse.tigerstripe.annotation.
	 * core.refactoring.IRefactoringDelegate,
	 * org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject[],
	 * org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject,
	 * java.util.Map, int)
	 */
	public void copied(IRefactoringDelegate delegate, ILazyObject[] objects,
			ILazyObject destination, Map<ILazyObject, String> newNames, int kind) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.core.refactoring.
	 * IRefactoringChangesListener #deleted(org.eclipse.tigerstripe.annotation
	 * .core.refactoring.IRefactoringDelegate,
	 * org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject)
	 */
	public void deleted(IRefactoringDelegate delegate, ILazyObject object) {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.core.refactoring.
	 * IRefactoringChangesListener
	 * #moved(org.eclipse.tigerstripe.annotation.core
	 * .refactoring.IRefactoringDelegate,
	 * org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject[],
	 * org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject, int)
	 */
	public void moved(IRefactoringDelegate delegate, ILazyObject[] objects,
			ILazyObject destination, int kind) {
		// Do nothing
	}

	private class URIsToChange {
		private final URI oldUri;
		private final URI[] referencingUris;

		public URIsToChange(URI oldUri, URI[] referencingUris) {
			this.oldUri = oldUri;
			this.referencingUris = referencingUris;
		}

		public URI getOldUri() {
			return oldUri;
		}

		public URI[] getReferencingUris() {
			return referencingUris;
		}
	}
}