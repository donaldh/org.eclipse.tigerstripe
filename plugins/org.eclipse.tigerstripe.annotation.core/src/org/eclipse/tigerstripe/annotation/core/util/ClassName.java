/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.core.util;

/**
 * @author Yuri Strot
 *
 */
public class ClassName {
	
	public ClassName(String className) {
		int index = className.lastIndexOf(".");
		if (index >= 0) {
			packij = className.substring(0, index);
			clazz = className.substring(index + 1);
		}
		else {
			packij = "";
			clazz = className;
		}
	}
	
	public ClassName(String packij, String clazz) {
		this.packij = packij;
		this.clazz = clazz;
	}
	
	public String getFullClassName() {
		return packij + "." + clazz;
	}
	
	/**
	 * @return the packij
	 */
	public String getPackage() {
		return packij;
	}
	
	/**
	 * @return the clazz
	 */
	public String getClazz() {
		return clazz;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFullClassName();
	}
	
	private String packij;
	private String clazz;
	

}
