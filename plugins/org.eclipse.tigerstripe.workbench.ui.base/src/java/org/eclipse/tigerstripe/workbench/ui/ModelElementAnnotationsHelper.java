/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

public class ModelElementAnnotationsHelper {

	private ModelElementAnnotationsHelper() {
	}

	public static String getAnnotationsAsString(Object modelElement) {
		return getAnnotationsAsString(false, false, modelElement);
	}

	public static String getAnnotationsAsString(boolean ignoreStereotypes,
			boolean ignoreAnnotations, Object modelElement) {
		StringBuilder builder = new StringBuilder("");

		if (!ignoreStereotypes && modelElement instanceof IStereotypeCapable) {
			Collection<IStereotypeInstance> stereotypes = ((IStereotypeCapable) modelElement)
					.getStereotypeInstances();
			if (stereotypes.size() > 0) {
				IWorkbenchProfile profile = TigerstripeCore
						.getWorkbenchProfileSession().getActiveProfile();
				for (IStereotypeInstance instance : stereotypes) {
					// Check that the stereotype is enabled in the profile
					IStereotype stereo = profile.getStereotypeByName(instance
							.getName());
					if (stereo != null) {
						appendElement(instance.getName(), builder);
					}
				}
			}
		}

		if (!ignoreAnnotations) {
			List<Annotation> annotations = new ArrayList<Annotation>();
			AnnotationUtils.getAllAnnotations(modelElement, annotations);
			for (Annotation annotation : annotations) {
				appendElement(DisplayAnnotationUtil.getText(annotation),
						builder);
			}
		}

		appendTail(builder);

		return builder.toString();
	}

	private static void appendElement(String element, StringBuilder builder) {
		if (builder.length() == 0) {
			builder.append("<<");
		} else {
			builder.append(",");
		}
		builder.append(element);
	}

	private static void appendTail(StringBuilder builder) {
		if (builder.length() > 0) {
			builder.append(">>");
		}
	}
}
