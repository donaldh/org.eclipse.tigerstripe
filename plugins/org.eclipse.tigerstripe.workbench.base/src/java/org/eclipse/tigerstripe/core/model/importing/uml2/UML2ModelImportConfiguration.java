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
package org.eclipse.tigerstripe.core.model.importing.uml2;

import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.importing.BaseModelImportConfiguration;
import org.eclipse.tigerstripe.core.model.importing.IModelImportConfiguration;
import org.eclipse.uml2.uml.Model;

public class UML2ModelImportConfiguration extends BaseModelImportConfiguration {

	private Model model;
	private String modelLibrary;

	public UML2ModelImportConfiguration(ITigerstripeProject referenceProject) {
		setReferenceProject(referenceProject);
	}

	public IModelImportConfiguration make(ITigerstripeProject referenceProject) {
		return new UML2ModelImportConfiguration(referenceProject);
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Model getModel() {
		return this.model;
	}

	public void setModelLibrary(String modelLibrary) {
		this.modelLibrary = modelLibrary;
	}

	public String getModelLibrary() {
		return this.modelLibrary;
	}

}
