/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.patterns;

import java.util.Collection;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.InTransaction;
import org.eclipse.tigerstripe.annotation.core.InTransaction.Operation;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.annotation.AnnotationHelper;
import org.eclipse.tigerstripe.workbench.patterns.IProjectPattern;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ProjectPattern extends Pattern implements IProjectPattern {

	// Need methods to create the project
	public ITigerstripeModelProject createProject(String projectName,
			IPath path, String defaultArtifactPackage)
			throws TigerstripeException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		if (defaultArtifactPackage != null) {
			projectDetails.getProperties().setProperty(
					IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP,
					defaultArtifactPackage);
		}
		projectDetails.setModelId(projectName);
		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.createProject(projectName, projectDetails, path,
						ITigerstripeModelProject.class, null, null);
		return project;

	}

	public void annotateProject(ITigerstripeModelProject project)
			throws TigerstripeException {
		Collection<EObject> annotationContents = xmlParserUtils
				.getAnnotations(element);

		for (EObject content : annotationContents) {
			addAnnotation(project, content);
		}
	}

	protected void addAnnotation(final ITigerstripeModelProject project,
			final EObject content) throws TigerstripeException {

		InTransaction.write(new Operation() {

			public void run() throws Throwable {
				try {
					String annotationClass = content.getClass().getInterfaces()[0]
							.getName();
					Annotation anno = helper.addAnnotation(project,
							Util.packageOf(annotationClass),
							Util.nameOf(annotationClass));
					anno.setContent(content);
					AnnotationHelper.getInstance().saveAnnotation(anno);
				} catch (Exception e) {
					e.printStackTrace();
					throw new TigerstripeException(
							"Exception adding annotation to project", e);
				}
			}
		});

	}

}
