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

/**
 * Base class for Model import results (i.e. imported into an AnnotableModel)
 * 
 * 
 * @author Eric Dillon
 * 
 */
public class ModelImportResult {

	private AnnotableModel model = null;
	private ModelImportException importException;

	public boolean isSuccessful() {
		return importException == null;
	}

	public AnnotableModel getModel() {
		return model;
	}

	public void setModel(AnnotableModel model) {
		this.model = model;
	}

	public ModelImportException getImportException() {
		return this.importException;
	}

	public void setImportException(ModelImportException importException) {
		this.importException = importException;
	}
}
