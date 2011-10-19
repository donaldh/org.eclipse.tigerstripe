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
package org.eclipse.tigerstripe.workbench.internal.core;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;

public class ModelAnnotationChangeDelta implements IModelAnnotationChangeDelta {

	private int type = UNKNOWN;
	private Annotation annotation;

	public ModelAnnotationChangeDelta(int type, Annotation annotation) {
		this.type = type;
		this.annotation = annotation;
	}

	public URI getAffectedModelComponentURI() {
		return annotation.getUri();
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return annotation.toString();
	}

}
