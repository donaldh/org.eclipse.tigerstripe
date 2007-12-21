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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectionHandle;
import org.eclipse.swt.graphics.Image;

public class TigerstripeInstanceConnectionHandle extends ConnectionHandle {

	static Image imageEast = InstanceDiagramEditorPlugin
			.getBundledImageDescriptor("icons/obj16/InstanceLinkEast.gif")
			.createImage();
	static Image imageWest = InstanceDiagramEditorPlugin
			.getBundledImageDescriptor("icons/obj16/InstanceLinkWest.gif")
			.createImage();
	static Image imageNorth = InstanceDiagramEditorPlugin
			.getBundledImageDescriptor("icons/obj16/InstanceLinkNorth.gif")
			.createImage();
	static Image imageSouth = InstanceDiagramEditorPlugin
			.getBundledImageDescriptor("icons/obj16/InstanceLinkSouth.gif")
			.createImage();

	public TigerstripeInstanceConnectionHandle(
			IGraphicalEditPart ownerEditPart,
			HandleDirection relationshipDirection, String tooltip) {
		super(ownerEditPart, relationshipDirection, tooltip);
	}

	@Override
	protected Image getImage(int side) {
		if (side == PositionConstants.EAST)
			return imageEast;
		else if (side == PositionConstants.WEST)
			return imageWest;
		else if (side == PositionConstants.NORTH)
			return imageNorth;
		else
			return imageSouth;
	}

}
