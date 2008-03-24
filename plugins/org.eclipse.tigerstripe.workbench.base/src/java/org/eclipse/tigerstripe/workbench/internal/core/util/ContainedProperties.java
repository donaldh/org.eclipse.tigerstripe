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
package org.eclipse.tigerstripe.workbench.internal.core.util;

import java.util.Map;
import java.util.Properties;

import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.internal.IContainerObject;

public class ContainedProperties extends Properties implements IContainedObject {

	// ====================================================================
	// IContainedObject
	private boolean isLocalDirty = false;
	private IContainerObject container = null;

	public void setContainer(IContainerObject container) {
		isLocalDirty = false;
		this.container = container;
	}

	public void clearDirty() {
		isLocalDirty = false;
	}

	public boolean isDirty() {
		return isLocalDirty;
	}

	/**
	 * Marks this object as dirty and notify the container if any
	 * 
	 */
	protected void markDirty() {
		isLocalDirty = true;
		if (container != null) {
			container.notifyDirty(this);
		}
	}

	public IContainerObject getContainer() {
		return this.container;
	}
	
	public ContainedProperties() {
		super();
	}

	public ContainedProperties(Properties defaults) {
		super();
		for( Object key : defaults.keySet()) {
			put(key, defaults.get(key));
		}
	}

	@Override
	public synchronized Object setProperty(String key, String value) {
		markDirty();
		return super.setProperty(key, value);
	}

	@Override
	public synchronized Object put(Object key, Object value) {
		markDirty();
		return super.put(key, value);
	}

	@Override
	public synchronized void putAll(Map<? extends Object, ? extends Object> t) {
		markDirty();
		super.putAll(t);
	}

	@Override
	public synchronized Object remove(Object key) {
		markDirty();
		return super.remove(key);
	}

}
