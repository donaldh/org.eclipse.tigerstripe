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
import java.util.Set;

import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;

public class GraphTraverser {

	private final boolean seeTarget;
	private final boolean seeSource;

	public GraphTraverser(Direction direction) {
		seeTarget = Direction.TARGET.equals(direction)
				|| Direction.BOTH.equals(direction);
		seeSource = Direction.SOURCE.equals(direction)
				|| Direction.BOTH.equals(direction);
	}

	public void traverse(Shape root, Visitor visitor) {
		traverse(root, visitor, new HashSet<Shape>(), new HashSet<Connection>());
	}

	private void traverse(Shape root, Visitor visitor, Set<Shape> seenShapes,
			Set<Connection> seenConnections) {

		if (!seenShapes.add(root)) {
			return;
		}

		dispatchVisit(root, visitor);

		if (seeTarget) {
			for (Connection con : root.getTargetConnections()) {
				if (seenConnections.add(con)) {
					visitor.visit(con);
				}
				traverse(con.getSource(), visitor, seenShapes, seenConnections);
			}
		}
		if (seeSource)
			for (Connection con : root.getSourceConnections()) {
				if (seenConnections.add(con)) {
					visitor.visit(con);
				}
				traverse(con.getTarget(), visitor, seenShapes, seenConnections);
			}
	}

	private void dispatchVisit(Shape shape, Visitor visitor) {
		visitor.visit(shape);
		if (shape instanceof Subject) {
			visitor.visit((Subject) shape);
		} else if (shape instanceof Note) {
			visitor.visit((Note) shape);
		} else {
			throw new UnsupportedOperationException(String.format(
					"Shape type '%s' not supported", shape.eClass().getName()));
		}
	}

	public static enum Direction {
		TARGET, SOURCE, BOTH;
	}

	public static interface Visitor {

		void visit(Shape shape);

		void visit(Subject subject);

		void visit(Note note);

		void visit(Connection connection);
	}

}
