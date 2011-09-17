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
package org.eclipse.tigerstripe.workbench.base.test.annotation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.internal.core.AnnotationManager;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.AbstractTigerstripeTestCase;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class AbstractTigerstripeAnnotationsTestCase extends
		AbstractTigerstripeTestCase {

	protected void cleanUpAnnotations(ITigerstripeModelProject aProject)
			throws TigerstripeException {
		List<Object> annotationTargets = new LinkedList<Object>();
		annotationTargets.add(aProject);
		Collection<IAbstractArtifact> artifacts = getAllArtifacts(aProject);
		annotationTargets.addAll(artifacts);
		for (IAbstractArtifact aa : artifacts) {
			annotationTargets.addAll(removeNulls(aa
					.getContainedModelComponents()));
		}
		for (Object object : annotationTargets) {
			AnnotationPlugin.getManager().removeAnnotations(object);
		}
	}

	<T> Collection<T> removeNulls(Collection<T> in) {
		for (Iterator<T> i = in.iterator(); i.hasNext();)
			if (i.next() == null)
				i.remove();
		return in;
	}
}
