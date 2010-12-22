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
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;

public class ConnectionEditPart extends AbstractConnectionEditPart {

	@Override
	protected void createEditPolicies() {
	}

	@Override
	protected IFigure createFigure() {
		PolylineConnection connectionFigure = (PolylineConnection) super
				.createFigure();
		Connection model = getConnection();
		if (model.getSource() instanceof Subject
				&& model.getTarget() instanceof Subject) {
			PolygonDecoration decoration = new PolygonDecoration();
			decoration.setForegroundColor(ColorConstants.black);
			decoration.setBackgroundColor(ColorConstants.white);
			Subject source = ((Subject) model.getSource());
			IDependencySubject extSource = Utils.findExternalModel(source,
					getViewer());

			if (extSource != null && extSource.isReverseDirection()) {
				connectionFigure.setTargetDecoration(decoration);
			} else {
				connectionFigure.setSourceDecoration(decoration);
			}
			if (!((Subject) model.getTarget()).isMaster()) {
				connectionFigure.setLineDash(new float[] { 5 });
			}
		}
		if (model.getSource() instanceof Note
				|| model.getTarget() instanceof Note) {
			connectionFigure.setForegroundColor(ColorConstants.lightGray);
		}
		return connectionFigure;
	}

	public Connection getConnection() {
		return (Connection) getModel();
	}
}
