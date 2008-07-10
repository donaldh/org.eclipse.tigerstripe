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
package org.eclipse.tigerstripe.workbench.internal.annotation;

import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

public class AnnotationUtils {

	public static boolean isModelAnnotation(Annotation annotation) {
		URI uri = annotation.getUri();
		IModelComponent component = TigerstripeURIAdapterFactory
				.uriToComponent(uri);

		return component != null;
	}

	public static Annotation[] extractModelAnnotations(Annotation[] annotations) {
		ArrayList<Annotation> result = new ArrayList<Annotation>();
		for (Annotation annotation : annotations) {
			if (isModelAnnotation(annotation))
				result.add(annotation);
		}
		return result.toArray(new Annotation[result.size()]);
	}
}
