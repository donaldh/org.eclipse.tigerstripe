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
package org.eclipse.tigerstripe.workbench;

import org.eclipse.core.resources.IResource;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

/**
 * ITigerstripeChangeListeners are notified whenever a change in the workspace
 * that is related to Tigerstripe occurs.
 * 
 * @see TigerstripeCore#addModelChangeListener(ITigerstripeChangeListener)
 * @see TigerstripeCore#removeTigerstripeChangeListener(ITigerstripeChangeListener)
 * @author erdillon
 */
public interface ITigerstripeChangeListener {

	public final static int PROJECT = 0x1;
	public final static int MODEL = 0x2;
	public final static int ANNOTATION = 0x4;

	public final static int ALL = PROJECT | MODEL | ANNOTATION;

	/**
	 * Notification that a Tigerstripe project was added to the workspace
	 * 
	 * Note: this listener needs to be registered with {@link #PROJECT} level to
	 * get notified
	 * 
	 * @param project
	 */
	public void projectAdded(IAbstractTigerstripeProject project);

	/**
	 * Notification that a Tigerstripe project was remove to the workspace. At
	 * the time of the notification, the project may already have been deleted
	 * from the workspace. So only its name can be provided.
	 * 
	 * Note: this listener needs to be registered with {@link #PROJECT} level to
	 * get notified
	 * 
	 * @param project
	 */
	public void projectDeleted(String projectName);
	
	/**
	 * Notification that a Tigerstripe descriptor was changed in the workspace. 
	 * 
	 * Note: this listener needs to be registered with {@link #PROJECT} level to
	 * get notified
	 * 
	 * @param project
	 */
	public void descriptorChanged(IResource changedDescriptor);

	/**
	 * Notification that model changes occurred.
	 * 
	 * Note: this listener needs to be registered with {@link #MODEL} level to
	 * get notified
	 * 
	 * @param delta
	 */
	public void modelChanged(IModelChangeDelta[] delta);

	public void annotationChanged(IModelAnnotationChangeDelta[] delta);
}
