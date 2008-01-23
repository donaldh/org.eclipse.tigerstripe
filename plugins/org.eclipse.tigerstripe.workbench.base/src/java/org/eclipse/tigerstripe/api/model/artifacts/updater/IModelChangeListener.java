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
package org.eclipse.tigerstripe.api.model.artifacts.updater;

/**
 * A listener that will get a copy of each IModelChangeRequest after it's been
 * executed on the model.
 * 
 * This is intended to be implemented by classes that need to be notified when
 * the model changes.
 * 
 * @author Eric Dillon
 * 
 */
public interface IModelChangeListener {

	public void notifyModelChanged(IModelChangeRequest executedRequest);
}
