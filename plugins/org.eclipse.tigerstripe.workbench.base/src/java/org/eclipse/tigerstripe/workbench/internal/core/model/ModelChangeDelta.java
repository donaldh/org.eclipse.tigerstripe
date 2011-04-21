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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request.ArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactFQRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.refactor.ModelChangeDeltaProcessor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * 
 * @author erdillon
 */
public class ModelChangeDelta implements IModelChangeDelta {

	public final static ModelChangeDelta UNKNOWNDELTA = new ModelChangeDelta(
			UNKNOWN);

	private ITigerstripeModelProject project;
	private Object oldValue;
	private Object newValue;
	private String feature;
	private Object component;
	private URI componentURI;
	private int type;
	private Object source;

	public ModelChangeDelta() {
		this(UNKNOWN);
	}

	public ModelChangeDelta(int type) {
		setType(type);
	}

	public ITigerstripeModelProject[] getAffectedProjects()
			throws TigerstripeException {
		List<ITigerstripeModelProject> projects = new ArrayList<ITigerstripeModelProject>();
		
		ITigerstripeModelProject project = getProject();
		
		if (project!=null) {
			projects.add(project);
			for (ITigerstripeModelProject other : project.getReferencedProjects()) {
				projects.add(other);
			}
		}

		return projects.toArray(new ITigerstripeModelProject[projects.size()]);
	}

	public ITigerstripeModelProject getProject() {
		return this.project;
	}

	public void setProject(ITigerstripeModelProject project) {
		this.project = project;
	}

	public URI getAffectedModelComponentURI() {
		return componentURI;
	}

	public void setAffectedModelComponentURI(URI uri) {
		this.componentURI = uri;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public Object getSource() {
		return this.source;
	}

	@Override
	public String toString() {
		String result = ": {";

		switch (type) {
		case IModelChangeDelta.SET:
			result = "SET: {";
			break;
		case IModelChangeDelta.ADD:
			result = "ADD: {";
			break;
		case IModelChangeDelta.REMOVE:
			result = "REMOVE: {";
			break;
		case IModelChangeDelta.COPY:
			result = "COPY: {";
			break;
		case IModelChangeDelta.UNKNOWN:
			result = "UNKNOWN: {";
		}

		result = result + "componentURI = '" + componentURI + "', "
				+ "feature = '" + feature + "', " + "oldValue = '" + oldValue
				+ "', " + "newValue = '" + newValue + "', " + "project = '"
				+ project + "', " + "source = '" + source + "'}";
		return result;
	}

	public IResource getAffectedResource() {
		IModelComponent component = TigerstripeURIAdapterFactory
				.uriToComponent(componentURI);
		if (component instanceof IAbstractArtifact) {
			IAbstractArtifact art = (IAbstractArtifact) component;
			return (IResource) art.getAdapter(IResource.class);
		} else if (component.getContainingModelComponent() instanceof IAbstractArtifact) {
			IAbstractArtifact art = (IAbstractArtifact) component
					.getContainingModelComponent();
			return (IResource) art.getAdapter(IResource.class);
		}
		return null;
	}

	public void apply(Collection<Object> toCleanUp,
			Collection<IAbstractArtifact> toSave) throws TigerstripeException {
		ModelChangeDeltaProcessor.processModelChangeDelta(this, toCleanUp, toSave);
	}

	/**
	 * Clients should not use this.
	 * 
	 * The component is generally not set unless it is part of a refactor
	 * operation. Clients should use {@link #getAffectedModelComponentURI()}
	 * instead.
	 * 
	 * @return the component targeted by this model change. This will be null if
	 *         not part of a refactor operation.
	 */
	public Object getComponent() {
		return component;
	}

	/**
	 * Clients should not use this.
	 * 
	 * The component is only set by the refactor framework as part of a refactor
	 * operation.
	 * 
	 * @param component
	 *            - the component being affected by the refactor operation
	 */
	public void setComponent(Object component) {
		this.component = component;
	}

	/**
	 * Clients should not use this.
	 * 
	 * This method is used by the refactor framework only to figure out whether
	 * the model change delta is affecting the "inside" of an artifact (i.e. no
	 * rename of the artifact).
	 * 
	 * @return true if the change delta isn't targeted at renaming an artifact
	 *         and this change is part of a refactor.
	 */
	public boolean isComponentRefactor() {
		if (component instanceof IAbstractArtifact) {
			return !IArtifactFQRenameRequest.FQN_FEATURE.equals(getFeature());
		}
		return true;
	}

	public boolean isRelationEndRefactor() {
		if (component instanceof IRelationship) {
			return ArtifactSetFeatureRequest.AEND.equals(getFeature())
					|| ArtifactSetFeatureRequest.ZEND.equals(getFeature());
		}
		return false;
	}
}
