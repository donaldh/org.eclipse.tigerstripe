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
package org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.layout.LayoutUtils;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts.DiagramEditPart;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts.ShapeEditPart;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts.SubjectEditPart;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesFactory;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Dimension;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Point;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;

public class Utils {

	public static Subject createFromExternalModel(IDependencySubject model) {
		ModelsFactory factory = ModelsFactory.INSTANCE;
		Subject result = factory.createSubject();
		result.setExternalId(model.getId());
		return result;
	}

	public static IDependencySubject getExternalModel(Subject subject,
			EditPartViewer viewer) {
		Registry registry = getRegistry(viewer);
		LayerDescriptor layerInfo = registry.getLayerInfo(subject
				.getParentLayer().getId());
		if (layerInfo == null) {
			return null;
		}
		SubjectDescriptor sd = layerInfo.getLayerData().get(
				subject.getExternalId());
		if (sd == null) {
			return null;
		}
		return sd.getExternalSubject();
	}

	public static Registry getRegistry(EditPartViewer viewer) {
		return getService(viewer, Registry.class);
	}

	public static <T> T getService(EditPartViewer viewer, Class<T> serviceClass) {
		Object service = viewer.getProperty(serviceClass.getName());
		if (service == null) {
			throw new IllegalStateException(serviceClass
					+ " does not initialized");
		}
		return serviceClass.cast(service);
	}

	public static DiagramEditPart getDiagramEditPart(EditPart part) {
		Iterator<?> it = part.getRoot().getChildren().iterator();
		if (!it.hasNext()) {
			throwIllegalStateOfRootEditPart();
		}
		Object first = it.next();
		if (!(first instanceof DiagramEditPart)) {
			throwIllegalStateOfRootEditPart();
		}
		return (DiagramEditPart) first;
	}

	public static Diagram getDiagram(EditPart part) {
		return (Diagram) getDiagramEditPart(part).getModel();
	}

	public static void linkWithCheck(Shape from, Shape to) {
		if (!isLinked(from, to)) {
			link(from, to);
		}
	}

	public static void link(Shape from, Shape to) {
		Connection connection = DependenciesFactory.eINSTANCE
				.createConnection();
		connection.setTarget(from);
		connection.setSource(to);
		from.getTargetConnections().add(connection);
		to.getSourceConnections().add(connection);
	}

	public static void unLink(Subject from, Subject to) {
		Connection connection = findConnection(from, to);
		if (connection != null) {
			from.getTargetConnections().remove(connection);
			to.getSourceConnections().remove(connection);
		}
	}

	public static Connection findConnection(Shape from, Shape to) {
		for (Connection con : from.getTargetConnections()) {
			if (con.getSource().equals(to)) {
				return con;
			}
		}
		return null;
	}

	public static boolean isLinked(Shape from, Shape to) {
		return findConnection(from, to) != null;
	}

	public static IDependencySubject findExternalModel(Subject subject,
			EditPartViewer viewer) {
		IDependencySubject model = getExternalModel(subject, viewer);
		Assert.isNotNull(model);
		return model;
	}

	public static Image chooseIcon(IDependencySubject subject) {
		Image image;
		if ((image = subject.getIcon()) != null) {
			return image;
		}
		return subject.getType().getImage();
	}

	public static org.eclipse.draw2d.geometry.Point toPoint(Point point) {
		if (point == null) {
			return new org.eclipse.draw2d.geometry.Point();
		} else {
			return new org.eclipse.draw2d.geometry.Point(point.getX(),
					point.getY());
		}
	}

	public static org.eclipse.draw2d.geometry.Dimension toDimension(
			Dimension dimension) {
		if (dimension == null) {
			return new org.eclipse.draw2d.geometry.Dimension();
		} else {
			return new org.eclipse.draw2d.geometry.Dimension(
					dimension.getWidth(), dimension.getHeight());
		}
	}

	public static Dimension fromDimension(
			org.eclipse.draw2d.geometry.Dimension size) {
		Dimension dimension = DependenciesFactory.eINSTANCE.createDimension();
		if (size != null) {
			dimension.setWidth(size.width);
			dimension.setHeight(size.height);
		}
		return dimension;
	}

	public static Point fromPoint(org.eclipse.draw2d.geometry.Point location) {
		Point point = DependenciesFactory.eINSTANCE.createPoint();
		if (location != null) {
			point.setX(location.x);
			point.setY(location.y);
		}
		return point;
	}

	public static void switchToLayer(Layer layer, EditPartViewer viewer) {
		if (layer.getShapes().isEmpty()) {
			return;
		}
		RootEditPart rootEditPart = viewer.getRootEditPart();
		Diagram diagram = Utils.getDiagram(rootEditPart);
		Layer currentLayer = diagram.getCurrentLayer();
		if (layer.equals(currentLayer)) {
			return;
		}
		diagram.getLayersHistory().add(currentLayer);
		diagram.setCurrentLayer(layer);
		ensureLayout(viewer, !layer.isWasLayouting());
		layer.setWasLayouting(true);
		// if (!layer.isWasLayouting()) {
		// LayoutUtils.layout(rootEditPart.getContents(), true);
		// layer.setWasLayouting(true);
		// }
	}

	public static void popLayer(EditPartViewer viewer) {
		Diagram diagram = Utils.getDiagram(viewer.getRootEditPart());
		EList<Layer> history = diagram.getLayersHistory();
		if (history.isEmpty()) {
			return;
		}
		Layer layer = history.remove(history.size() - 1);
		diagram.setCurrentLayer(layer);
	}

	public static void loadDependencies(SubjectEditPart part) {
		Registry registry = getRegistry(part.getViewer());
		Subject subject = part.getSubject();
		if (subject.isLoaded()) {
			return;
		}
		Set<Shape> attended = new HashSet<Shape>(subject.getParentLayer()
				.getShapes());
		Set<Subject> affected = registry.loadDependencies(subject);
		part.getParent().refresh();
		part.refresh();
		Map<?, ?> editPartRegistry = part.getViewer().getEditPartRegistry();

		Set<GraphicalEditPart> toLayout = new HashSet<GraphicalEditPart>();

		for (Subject s : affected) {
			if (s.isWasLayouting()) {
				continue;
			}
			GraphicalEditPart aPart = (GraphicalEditPart) editPartRegistry
					.get(s);
			if (aPart == null) {
				continue;
			}
			if (!s.equals(subject) && !attended.contains(s)) {
				toLayout.add(aPart);
				if (aPart instanceof ShapeEditPart) {
					((ShapeEditPart) aPart).getShape().setWasLayouting(true);
				}
			}
			aPart.refresh();
		}

		LayoutUtils.layout(toLayout, true);
	}

	public static void collapseDependencies(SubjectEditPart part) {
		Registry registry = getRegistry(part.getViewer());
		Subject subject = part.getSubject();
		if (!subject.isLoaded()) {
			return;
		}
		registry.collapseDependencies(subject);
		EditPart parent = part.getParent();
		parent.refresh();

		Iterator<?> it = parent.getChildren().iterator();
		while (it.hasNext()) {
			((EditPart) it.next()).refresh();
		}
	}

	private static void throwIllegalStateOfRootEditPart() {
		throw new IllegalStateException(
				"Root edit part must contain one element of class "
						+ DiagramEditPart.class.getName());
	}

	public static boolean ensureLayout(EditPartViewer viewer) {
		return ensureLayout(viewer, false);
	}

	public static boolean ensureLayout(EditPartViewer viewer,
			boolean useAnimation) {
		return ensureLayout(getDiagramEditPart(viewer.getRootEditPart()),
				useAnimation);
	}

	public static boolean ensureLayout(DiagramEditPart diagramEditPart) {
		return ensureLayout(diagramEditPart, false);
	}

	public static boolean ensureLayout(DiagramEditPart diagramEditPart,
			boolean useAnimation) {
		@SuppressWarnings("unchecked")
		List<EditPart> children = diagramEditPart.getChildren();

		Set<ShapeEditPart> toLayout = new HashSet<ShapeEditPart>();

		for (EditPart editPart : children) {
			if (editPart instanceof ShapeEditPart) {
				if (!((ShapeEditPart) editPart).getShape().isWasLayouting()) {
					toLayout.add((ShapeEditPart) editPart);
				}
			}
		}
		LayoutUtils.layout(toLayout, useAnimation);
		for (ShapeEditPart shapeEditPart : toLayout) {
			shapeEditPart.getShape().setWasLayouting(true);
		}
		return !toLayout.isEmpty();
	}

}
