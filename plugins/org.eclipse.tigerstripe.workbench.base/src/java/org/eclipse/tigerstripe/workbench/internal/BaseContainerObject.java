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
package org.eclipse.tigerstripe.workbench.internal;

import java.util.HashSet;
import java.util.Set;

public class BaseContainerObject implements IContainerObject {

	private Set<IContainedObject> dirtyContained = new HashSet<IContainedObject>();

	@Override
	public void clearDirtyOnContained() {
		for (IContainedObject obj : dirtyContained) {
			obj.clearDirty();
			if (obj instanceof IContainerObject) {
				((IContainerObject) obj).clearDirtyOnContained();
			}
		}
		dirtyContained.clear();
	}

	@Override
	public boolean isContainedDirty() {
		return !dirtyContained.isEmpty();
	}

	@Override
	public void notifyDirty(IContainedObject contained) {
		dirtyContained.add(contained);
		
		// If this is itself contained, make sure to propagate one level up too.
		if ( this instanceof IContainedObject ) {
			IContainerObject parentContainer = ((IContainedObject) this).getContainer();
			if ( parentContainer != null )
				parentContainer.notifyDirty((IContainedObject) this);
		}
	}

}
