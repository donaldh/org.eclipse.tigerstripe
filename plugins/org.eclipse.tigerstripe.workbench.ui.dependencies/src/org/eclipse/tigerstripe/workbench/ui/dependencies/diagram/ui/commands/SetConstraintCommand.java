/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.commands;

import static org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils.fromDimension;
import static org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils.fromPoint;
import static org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils.toDimension;
import static org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils.toPoint;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;

public class SetConstraintCommand extends Command {

	/** Stores the new size and location. */
	private final Rectangle newBounds;
	/** Stores the old size and location. */
	private Rectangle oldBounds;
	/** A request to move/resize an edit part. */
	private final ChangeBoundsRequest request;

	/** Shape to manipulate. */
	private final Shape shape;

	/**
	 * Create a command that can resize and/or move a shape.
	 * 
	 * @param shape
	 *            the shape to manipulate
	 * @param req
	 *            the move and resize request
	 * @param newBounds
	 *            the new size and location
	 * @throws IllegalArgumentException
	 *             if any of the parameters is null
	 */
	public SetConstraintCommand(Shape shape, ChangeBoundsRequest req,
			Rectangle newBounds) {
		if (shape == null || req == null || newBounds == null) {
			throw new IllegalArgumentException();
		}
		this.shape = shape;
		this.request = req;
		this.newBounds = newBounds.getCopy();
		setLabel("move / resize");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		Object type = request.getType();
		// make sure the Request is of a type we support:
		return (RequestConstants.REQ_MOVE.equals(type)
				|| RequestConstants.REQ_MOVE_CHILDREN.equals(type)
				|| RequestConstants.REQ_RESIZE.equals(type) || RequestConstants.REQ_RESIZE_CHILDREN
				.equals(type));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		oldBounds = new Rectangle(toPoint(shape.getLocation()),
				toDimension(shape.getSize()));
		redo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		shape.setSize(fromDimension(newBounds.getSize()));
		shape.setLocation(fromPoint(newBounds.getLocation()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		shape.setSize(fromDimension(oldBounds.getSize()));
		shape.setLocation(fromPoint(oldBounds.getLocation()));
	}
}
