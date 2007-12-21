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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.graphics.Image;

public class ClassDiagramLogicalNode extends AbstractGMFDiagramNode {

	public static String MODEL_EXT = "vwm";

	public static String DIAG_EXT = "wvd";

	@Override
	protected String getModelExtension() {
		return MODEL_EXT;
	}

	public final static ClassDiagramLogicalNode MODEL = new ClassDiagramLogicalNode(
			null);

	public ClassDiagramLogicalNode(IFile diagramFile) {
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
		return new ClassDiagramLogicalNode((IFile) resource);
	}

	@Override
	public String getEditor() {
		return "org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorID";
	}

}
