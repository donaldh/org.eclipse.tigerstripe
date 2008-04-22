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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.graphics.Image;

public class InstanceDiagramLogicalNode extends AbstractGMFDiagramNode {

	public static String MODEL_EXT = "owm";

	public static String DIAG_EXT = "wod";

	@Override
	protected String getModelExtension() {
		return MODEL_EXT;
	}

	@Override
	protected String getDiagramExtension() {
		return DIAG_EXT;
	}

	public final static InstanceDiagramLogicalNode MODEL = new InstanceDiagramLogicalNode(
			null);

	public InstanceDiagramLogicalNode(IFile diagramFile) {
		super(diagramFile);
	}

	@Override
	public Image getImage() {
		return null; // the regular class diagram image will be used
	}

	@Override
	public IResource getKeyResource() {
		return getDiagramFile();
	}

	@Override
	public String getText() {
		if (getDiagramFile() != null && getDiagramFile().exists())
			return getDiagramFile().getName().substring(0,
					getDiagramFile().getName().lastIndexOf("."));
		else if (getModelFile() != null && getModelFile().exists())
			return getModelFile().getName();
		return "unknown";
	}

	@Override
	public boolean isKeyResource(IResource resource) {
		return resource != null && DIAG_EXT.equals(resource.getFileExtension());
	}

	@Override
	public boolean shouldFilterResource(IResource resource) {
		return resource != null
				&& MODEL_EXT.equals(resource.getFileExtension());
	}

	@Override
	public AbstractLogicalExplorerNode makeInstance(IResource resource) {
		return new InstanceDiagramLogicalNode((IFile) resource);
	}

	@Override
	public String getEditor() {
		return "org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceDiagramEditorID";
	}
}
