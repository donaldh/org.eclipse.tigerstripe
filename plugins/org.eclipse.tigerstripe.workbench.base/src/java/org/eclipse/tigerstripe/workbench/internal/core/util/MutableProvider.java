/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.util;

public class MutableProvider<T> implements Provider<T> {

	private T value;

	public MutableProvider() {
	}

	public MutableProvider(T value) {
		set(value);
	}

	public void set(T value) {
		this.value = value;
	}

	public T get() {
		return value;
	}
}
