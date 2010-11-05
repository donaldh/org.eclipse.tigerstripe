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

import static org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.ColorUtils.toColorAsInt;

import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.ConnectionStyle;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesFactory;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Dimension;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Point;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;

public class ModelsFactory extends EFactoryDelegate implements
		DependenciesFactory {

	public static final ModelsFactory INSTANCE = new ModelsFactory();

	@Override
	public DependenciesFactory getDelegate() {
		return eINSTANCE;
	}

	public Shape createShape() {
		Shape shape = getDelegate().createShape();
		initShape(shape, 50, 50);
		return shape;
	}

	public Point createPoint() {
		return getDelegate().createPoint();
	}

	public Dimension createDimension(int w, int h) {
		Dimension dim = createDimension();
		dim.setWidth(w);
		dim.setHeight(h);
		return dim;
	}

	public Dimension createDimension() {
		return getDelegate().createDimension();
	}

	public Subject createSubject() {
		Subject subject = getDelegate().createSubject();
		initShape(subject, 200, 80);
		ShapeStyle style = subject.getStyle();
		style.setBackgroundColor(toColorAsInt(0, 255, 255, 222));
		style.setForegroundColor(toColorAsInt(0, 147, 147, 42));
		return subject;
	}

	public Note createNote() {
		Note note = getDelegate().createNote();
		initShape(note, 100, 20);
		ShapeStyle style = note.getStyle();
		style.setBackgroundColor(toColorAsInt(0, 255, 236, 236));
		style.setForegroundColor(toColorAsInt(0, 255, 172, 172));
		return note;
	}

	public ShapeStyle createShapeStyle() {
		return getDelegate().createShapeStyle();
	}

	public Connection createConnection() {
		Connection connection = getDelegate().createConnection();
		ConnectionStyle connectionStyle = createConnectionStyle();
		connection.setStyle(connectionStyle);
		return connection;
	}

	public ConnectionStyle createConnectionStyle() {
		return getDelegate().createConnectionStyle();
	}

	public Diagram createDiagram() {
		return getDelegate().createDiagram();
	}

	public DependenciesPackage getDependenciesPackage() {
		return getDelegate().getDependenciesPackage();
	}

	private void initShape(Shape shape, int w, int h) {
		shape.setSize(createDimension(w, h));
		shape.setLocation(createPoint());
		shape.setStyle(createShapeStyle());
	}

	public Kind createKind() {
		Kind kind = getDelegate().createKind();
		kind.setStyle(createShapeStyle());
		return kind;
	}

	public ShapeStyle createDefaultSubjectStyle() {
		ShapeStyle style = createShapeStyle();
		style.setBackgroundColor(toColorAsInt(0, 255, 255, 222));
		style.setForegroundColor(toColorAsInt(0, 147, 147, 42));
		return style;
	}

	public Layer createLayer() {
		return getDelegate().createLayer();
	}

}
