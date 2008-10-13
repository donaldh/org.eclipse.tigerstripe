/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public abstract class BaseAnnotableModel extends BaseAnnotable implements
		AnnotableModel {

	private ITigerstripeModelProject targetProject;

	private IModelImportConfiguration config;

	public boolean isMapped() {
		// makes the compiler happy
		return true;
	}

	public BaseAnnotableModel(ITigerstripeModelProject targetProject,
			IModelImportConfiguration config) throws TigerstripeException {
		super(targetProject.getName());
		this.targetProject = targetProject;
		this.config = config;
	}

	public ITigerstripeModelProject getTargetProject() {
		return this.targetProject;
	}

	public IModelImportConfiguration getConfig() {
		return this.config;
	}

	private Collection<AnnotableElement> annotableElements = new ArrayList<AnnotableElement>();

	private Collection<AnnotableElementPackage> annotablePackageElements = new ArrayList<AnnotableElementPackage>();

	private Collection<AnnotableDatatype> annotableDatatypes = new ArrayList<AnnotableDatatype>();

	private Collection<AnnotableElement> annotableEnumerations = new ArrayList<AnnotableElement>();

	public void setAnnotableElements(
			Collection<AnnotableElement> annotableElements) {
		this.annotableElements = annotableElements;
	}

	public void setAnnotablePackageElements(
			Collection<AnnotableElementPackage> annotablePackageElements) {
		this.annotablePackageElements = annotablePackageElements;
	}

	public Collection<AnnotableElement> getAnnotableElements() {
		return annotableElements;
	}

	public Collection<AnnotableElementPackage> getAnnotablePackageElements() {
		return annotablePackageElements;
	}

	public Collection<AnnotableDatatype> getAnnotableDatatypes() {
		return annotableDatatypes;
	}

	public void setAnnotableDatatypes(
			Collection<AnnotableDatatype> annotableDatatypes) {
		this.annotableDatatypes = annotableDatatypes;
	}

	public void setAnnotableEnumerations(
			Collection<AnnotableElement> annotableEnumerations) {
		this.annotableEnumerations = annotableEnumerations;
	}

	public Collection<AnnotableElement> getAnnotableEnumerations() {
		return this.annotableEnumerations;
	}

	/**
	 * 
	 * 
	 */
	public void applyTargetProjectArtifactTypes(
			ITigerstripeModelProject referenceProject) {
		Collection<AnnotableElement> elements = getAnnotableElements();

		try {
			IArtifactManagerSession session = referenceProject
					.getArtifactManagerSession();

			for (AnnotableElement elm : elements) {
				String fqn = elm.getFullyQualifiedName();
				IAbstractArtifact artifact = session
						.getArtifactByFullyQualifiedName(fqn);

				if (artifact != null) {

					if (artifact instanceof IManagedEntityArtifact) {
						elm.setAnnotationType(AnnotableElement.AS_ENTITY);
					} else if (artifact instanceof IDatatypeArtifact) {
						elm.setAnnotationType(AnnotableElement.AS_DATATYPE);
					} else if (artifact instanceof IEnumArtifact) {
						elm.setAnnotationType(AnnotableElement.AS_ENUMERATION);
					}
				}
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e); // FIXME
		}
	}

}
