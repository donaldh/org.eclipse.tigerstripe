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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.undo;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.UndoableEdit;

public class ModelUndoableEdit extends UndoableEdit implements
		IModelChangeDelta {

	private URI affectedModelComponentURI;
	private int type = UNKNOWN;
	private String feature;
	private Object oldValue;
	private Object newValue;
	private ITigerstripeModelProject project;

	public ModelUndoableEdit(URI affectedModelComponentURI, int type,
			String feature, Object oldValue, Object newValue,
			ITigerstripeModelProject project) {
		this.project = project;
		this.affectedModelComponentURI = affectedModelComponentURI;
		this.type = type;
		this.feature = feature;
		this.oldValue = oldValue;
		this.newValue = newValue;

	}

	public URI getAffectedModelComponentURI() {
		return affectedModelComponentURI;
	}

	public ITigerstripeModelProject[] getAffectedProjects()
			throws TigerstripeException {
		return project.getReferencedProjects();
	}

	public String getFeature() {
		return feature;
	}

	public Object getNewValue() {
		return newValue;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public ITigerstripeModelProject getProject() {
		return project;
	}

	public int getType() {
		return type;
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
		case IModelChangeDelta.UNKNOWN:
			result = "UNKNOWN: {";
		}

		result = result + "componentURI = '" + affectedModelComponentURI
				+ "', " + "feature = '" + feature + "', " + "oldValue = '"
				+ oldValue + "', " + "newValue = '" + newValue + "', "
				+ "project = '" + project + "'}";
		return result;
	}

}
