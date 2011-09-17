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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringDelegate;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

public class ChangeIdRefactoringListener implements IRefactoringChangesListener {

	private Map<ILazyObject, URI> changes = new HashMap<ILazyObject, URI>();

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
				changes.put(oldObject, oldUri);
			}
		} else if (kind == CHANGED) {
			ITigerstripeModelProject project = getProject(oldObject);
			URI oldUri = changes.get(oldObject);
			if (project != null && oldUri != null) {
				URI newUri = getURI(project);
				if (newUri != null) {
					delegate.changed(oldUri, newUri, true);
				}
			}
		}
	}

	private URI getURI(ITigerstripeModelProject project) {
		try {
			return URI.createHierarchicalURI(
					TigerstripeURIAdapterFactory.SCHEME_TS, null, null,
					new String[] { project.getModelId() }, null, null);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
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
}