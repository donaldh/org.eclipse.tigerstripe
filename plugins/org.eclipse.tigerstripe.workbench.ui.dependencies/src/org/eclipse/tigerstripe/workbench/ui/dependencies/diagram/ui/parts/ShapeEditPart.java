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

import static org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.ColorUtils.toColor;
import static org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils.toDimension;
import static org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils.toPoint;

import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.properties.ShapePropertySource;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle;
import org.eclipse.ui.views.properties.IPropertySource;

public abstract class ShapeEditPart extends AbstractGraphicalEditPart implements
		NodeEditPart {

	private AdapterImpl visualAdapter;
	private ShapePropertySource propertySource;

	protected abstract IFigure doCreateFigure();

	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			visualAdapter = new EContentAdapter() {
				@Override
				public void notifyChanged(Notification msg) {
					if (msg.getEventType() == Notification.SET) {
						Object feature = msg.getFeature();
						if (DependenciesPackage.Literals.SHAPE__LOCATION
								.equals(feature)
								|| DependenciesPackage.Literals.SHAPE__SIZE
										.equals(feature)) {
							refreshVisuals();
						} else if (DependenciesPackage.Literals.SHAPE_STYLE__BACKGROUND_COLOR
								.equals(feature)
								|| DependenciesPackage.Literals.SHAPE_STYLE__FOREGROUND_COLOR
										.equals(feature)
								|| DependenciesPackage.Literals.SHAPE__STYLE
										.equals(feature)) {
							updateStyle();
							refreshVisuals();
						}
					}
				}
			};
			getShape().eAdapters().add(visualAdapter);
		}
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			if (visualAdapter != null) {
				getShape().eAdapters().remove(visualAdapter);
			}
		}
	}

	@Override
	protected IFigure createFigure() {
		IFigure figure = doCreateFigure();
		updateStyle(figure, getStyle());
		return figure;
	}

	@Override
	public void setModel(Object model) {
		super.setModel(model);
		propertySource = new ShapePropertySource(getShape());
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
		if (IPropertySource.class.isAssignableFrom(key)) {
			return propertySource;
		}
		return super.getAdapter(key);
	}

	protected ShapeStyle getStyle() {
		return getShape().getStyle();
	}

	protected void updateStyle() {
		updateStyle(getFigure(), getStyle());
	}

	private void updateStyle(IFigure figure, ShapeStyle style) {
		figure.setForegroundColor(toColor(style.getForegroundColor()));
		figure.setBackgroundColor(toColor(style.getBackgroundColor()));
	}

	public Shape getShape() {
		return (Shape) getModel();
	}

	protected ConnectionAnchor getConnectionAnchor() {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	protected List<Connection> getModelSourceConnections() {
		return getShape().getSourceConnections();
	}

	@Override
	protected List<Connection> getModelTargetConnections() {
		return getShape().getTargetConnections();
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	@Override
	public void refreshVisuals() {
		// notify parent container of changed position & location
		// if this line is removed, the XYLayoutManager used by the parent
		// container
		// (the Figure of the ShapesDiagramEditPart), will not know the bounds
		// of this figure
		// and will not draw it correctly.
		Rectangle bounds = new Rectangle(toPoint(getShape().getLocation()),
				toDimension(getShape().getSize()));
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), bounds);
	}

	@Override
	protected void createEditPolicies() {
	}

}
