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
import org.eclipse.tigerstripe.workbench.internal.refactor.ModelChangeDeltaProcessor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
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

		projects.add(getProject());
		for (ITigerstripeModelProject other : getProject()
				.getReferencedProjects()) {
			projects.add(other);
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

	public void apply(Collection<Object> toCleanUp) throws TigerstripeException {
		ModelChangeDeltaProcessor.processModelChangeDelta(this, toCleanUp);
	}

}
