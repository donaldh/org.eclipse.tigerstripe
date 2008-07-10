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
	private Annotation[] annotations;

	public ModelAnnotationChangeDelta(int type, Annotation[] annotations) {
		this.type = type;
		this.annotations = annotations;
	}

	public URI getAffectedModelComponentURI() {
		return annotations[0].getUri();
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return annotations[0].toString();
	}

}
