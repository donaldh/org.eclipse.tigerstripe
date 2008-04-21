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
package org.eclipse.tigerstripe.annotation.ui.internal.view;

import java.util.Comparator;

import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;

/**
 * @author Yuri Strot
 *
 */
public abstract class AnnotationComparator implements Comparator<Object> {
	
	public static final AnnotationComparator CONTRIBUTOR = new AnnotationComparator() {

		public int compare(Annotation o1, Annotation o2) {
		    return o1.toString().compareTo(o2.toString());
		}

	};
	
	public static final AnnotationComparator URI = new AnnotationComparator() {

		public int compare(Annotation o1, Annotation o2) {
			return o1.getUri().toString().compareTo(o2.getUri().toString());
		}

	};
	
	public static final AnnotationComparator TYPE = new AnnotationComparator() {

		public int compare(Annotation o1, Annotation o2) {
			AnnotationType type1 = AnnotationPlugin.getManager().getType(o1);
			AnnotationType type2 = AnnotationPlugin.getManager().getType(o2);
			if (type1 == null && type2 == null)
				return 0;
			if (type1 == null)
				return -1;
			if (type2 == null)
				return 1;
			return type1.getName().compareTo(type2.getName());
		}

	};
	
	public int compare(Object o1, Object o2) {
		return compare((Annotation)o1, (Annotation)o2);
	}
	
	protected abstract int compare(Annotation a1, Annotation a2);
	
}
