/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationStore;
import org.eclipse.tigerstripe.annotations.IAnnotable;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILiteral;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * This is a main integration point into the Annotation Framework so all model
 * components can be adapted as an IAnnotable
 * 
 * @author erdillon
 * 
 */
public class ModelComponentAnnotable implements IAnnotable {

	private IModelComponent component;

	public ModelComponentAnnotable(IModelComponent component) {
		this.component = component;
	}

	@Override
	public AnnotationStore getStore(IAnnotationScheme scheme)
			throws AnnotationCoreException {
		try {
			ITigerstripeProject project = getProject();
			IProject iProject = (IProject) project.getAdapter(IProject.class);
			if (iProject != null) {
				return AnnotationStore.getDefaultFactory().getAnnotationStore(
						iProject, scheme);
			}
			throw new AnnotationCoreException(
					"Can't find AnnotationStore for: " + component);
		} catch (TigerstripeException e) {
			throw new AnnotationCoreException(e);
		}
	}

	@Override
	public String getURI() throws AnnotationCoreException {

		if (component instanceof IField) {
			StringBuffer buf = getArtifactURI(((IField) component)
					.getContainingArtifact());
			buf.append("#");
			buf.append(component.getName());
			return buf.toString();
		} else if (component instanceof ILiteral) {
			StringBuffer buf = getArtifactURI(((ILiteral) component)
					.getContainingArtifact());
			buf.append("#");
			buf.append(component.getName());
			return buf.toString();
		} else if (component instanceof IArgument) {
			// FIXME: currently IArgument is not an IModelComponent

		} else if (component instanceof IMethod) {
			IMethod m = (IMethod) component;
			IAbstractArtifact art = m.getContainingArtifact();
			StringBuffer URI = getArtifactURI(art);

			URI.append(m.getName());
			URI.append("(");
			boolean first = true;
			for (IArgument arg : m.getArguments()) {
				if (!first) {
					URI.append(",");
				}
				URI.append(arg.getName());
			}
			URI.append(")");
			return URI.toString();
		} else if (component instanceof IAssociationEnd) {
			IAssociationEnd end = (IAssociationEnd) component;
			IAssociationArtifact assoc = end.getContainingAssociation();
			StringBuffer URI = getArtifactURI(assoc);
			if (assoc.getAEnd() == end) {
				URI.append(";aEnd");
			} else {
				URI.append(";zEnd");
			}
			return URI.toString();
		} else if (component instanceof IAbstractArtifact) {
			return getArtifactURI((IAbstractArtifact) component).toString();
		}
		throw new AnnotationCoreException("Can't determine URI for:"
				+ component);
	}

	protected StringBuffer getArtifactURI(IAbstractArtifact artifact) {
		StringBuffer URI = new StringBuffer("tigerstripe://");

		if (artifact.getTigerstripeProject() instanceof ITigerstripeProject) {
			URI.append(artifact.getTigerstripeProject().getProjectLabel());
			URI.append("/");
		} else if (artifact.getTigerstripeProject() instanceof ITigerstripeModuleProject) {
//			ITigerstripeModuleProject mProject = (ITigerstripeModuleProject) artifact
//					.getTigerstripeProject();

		}
		URI.append(artifact.getFullyQualifiedName());

		return URI;
	}

	private ITigerstripeProject getProject() throws TigerstripeException {
		if (component instanceof IAbstractArtifact) {
			IAbstractArtifact art = (IAbstractArtifact) component;
			return art.getTigerstripeProject();
		} else if (component instanceof IField) {
			return ((IField) component).getContainingArtifact()
					.getTigerstripeProject();
		} else if (component instanceof IMethod) {
			return ((IMethod) component).getContainingArtifact()
					.getTigerstripeProject();
		} else if (component instanceof ILiteral) {
			return ((ILiteral) component).getContainingArtifact()
					.getTigerstripeProject();
		} else if (component instanceof IAssociationEnd) {
			return ((IAssociationEnd) component).getContainingArtifact()
					.getTigerstripeProject();
		}

		throw new TigerstripeException("Can't get project for: " + component);
	}
}
