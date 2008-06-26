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
package org.eclipse.tigerstripe.annotation.internal.core;

import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.IAnnotationTarget;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationTarget implements IAnnotationTarget {
	
	private Object object;
	private String description;
	private AnnotationType type;
	
	public AnnotationTarget(Object object, String description, AnnotationType type) {
		this.object = object;
		this.description = description;
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationTarget#getAdaptedObject()
	 */
	public Object getAdaptedObject() {
		return object;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationTarget#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.core.IAnnotationTarget#getType()
	 */
	public AnnotationType getType() {
		return type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + description + ", " + type + ")";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AnnotationTarget) {
			AnnotationTarget target = (AnnotationTarget)obj;
			return equals(getType(), target.getType()) &&
				equals(getDescription(), target.getDescription()) &&
				equals(getAdaptedObject(), target.getAdaptedObject());
		}
		return super.equals(obj);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (hashCode(getType()) >> 16) ^ (hashCode(getDescription()) >> 8)
			^ hashCode(getAdaptedObject());
	}
	
	protected static boolean equals(Object object1, Object object2) {
		if (object1 == null)
			return object2 == null;
		if (object2 == null)
			return false;
		return object1.equals(object2);
	}
	
	protected static int hashCode(Object object) {
		if (object == null)
			return 0;
		return object.hashCode();
	}

}
