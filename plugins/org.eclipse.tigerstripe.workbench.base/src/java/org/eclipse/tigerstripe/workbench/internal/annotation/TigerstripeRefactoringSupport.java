/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.annotation;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IRefactoringSupport;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

public class TigerstripeRefactoringSupport implements
		ITigerstripeChangeListener {

	public final static TigerstripeRefactoringSupport INSTANCE = new TigerstripeRefactoringSupport();

	private IRefactoringSupport refactor = AnnotationPlugin.getManager()
			.getRefactoringSupport();

	private TigerstripeRefactoringSupport() {
	}

	public void start() {
		TigerstripeCore.addTigerstripeChangeListener(this,
				ITigerstripeChangeListener.ALL);
	}

	public void stop() {
		TigerstripeCore.addTigerstripeChangeListener(this,
				ITigerstripeChangeListener.ALL);
	}

	public void modelChanged(IModelChangeDelta[] deltas) {
		for (IModelChangeDelta delta : deltas) {
			if (delta.getType() == IModelChangeDelta.REMOVE) {
				refactor.deleted((URI)delta.getOldValue(), true);
			} else if (delta.getType() == IModelChangeDelta.SET
					&& "name".equals(delta.getFeature())) {
				URI oldUri = (URI) delta.getOldValue();
				URI newUri = (URI) delta.getNewValue();
				refactor.changed(oldUri, newUri, true);
			}
		}
	}

	public void projectAdded(IAbstractTigerstripeProject project) {
		// do nothing
	}

	public void projectDeleted(String projectName) {
		// do nothing
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		// do nothing
	}
}
