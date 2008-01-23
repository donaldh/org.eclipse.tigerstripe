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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts;

import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.ReadOnlyEditorInput;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.AbstractArtifactLabelProvider;

/**
 * THis is passed to an editor when trying to open an artifact from a module
 * 
 * @author Eric Dillon
 * 
 */
public class ReadOnlyArtifactEditorInput extends ReadOnlyEditorInput {

	private AbstractArtifactLabelProvider labelProvider = new AbstractArtifactLabelProvider();

	private IAbstractArtifact artifact;

	public ReadOnlyArtifactEditorInput(JarEntryFile jarEntryFile,
			IAbstractArtifact artifact) {
		super(jarEntryFile);
		this.artifact = artifact;
	}

	public String getToolTipText() {
		return artifact.getFullyQualifiedName() + " (read-only)";
	}

	public IAbstractArtifact getArtifact() {
		return this.artifact;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// return labelProvider.getImage(artifact);
		return null;
	}

	@Override
	public String getName() {
		return artifact.getName();
	}

}
