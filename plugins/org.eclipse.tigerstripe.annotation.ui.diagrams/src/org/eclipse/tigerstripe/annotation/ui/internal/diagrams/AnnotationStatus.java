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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams;

import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationStatus {
	
	public static final int STATUS_NON_EXIST = 0;
	
	public static final int STATUS_HIDDEN = 1;
	
	public static final int STATUS_VISIBLE = 2;
	
	private AnnotationNode node;
	private Annotation annotation;
	private int status;

	/**
	 * @param annotation
	 * @param status
	 */
	public AnnotationStatus(Annotation annotation) {
		this.annotation = annotation;
		this.status = STATUS_NON_EXIST;
		node = null;
	}

	/**
	 * @param annotation
	 * @param status
	 */
	public AnnotationStatus(AnnotationNode node) {
		this.annotation = node.getAnnotation();
		this.status = node.isVisible() ? STATUS_VISIBLE : STATUS_HIDDEN;
		this.node = node;
	}
	
	/**
	 * @return the annotation
	 */
	public Annotation getAnnotation() {
		return annotation;
	}
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * @return the part
	 */
	public AnnotationNode getNode() {
		return node;
	}

}
