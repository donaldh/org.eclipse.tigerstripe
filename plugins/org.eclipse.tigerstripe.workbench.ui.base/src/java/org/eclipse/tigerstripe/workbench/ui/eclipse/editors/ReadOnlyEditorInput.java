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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors;

import org.eclipse.core.resources.IStorage;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * This is used when the user is trying to open the content of a module. We pass
 * this Editor Input to the corresponding editor so it knows it should be in
 * read-only mode.
 * 
 * @author Eric Dillon
 */
public abstract class ReadOnlyEditorInput implements IEditorInput {

	private JarEntryFile jarEntryFile;

	public ReadOnlyEditorInput(JarEntryFile jarEntryFile) {
		this.jarEntryFile = jarEntryFile;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return jarEntryFile.getName();
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IStorage.class)
			return jarEntryFile;
		return null;
	}

	protected JarEntryFile getJarEntryFile() {
		return this.jarEntryFile;
	}

}
