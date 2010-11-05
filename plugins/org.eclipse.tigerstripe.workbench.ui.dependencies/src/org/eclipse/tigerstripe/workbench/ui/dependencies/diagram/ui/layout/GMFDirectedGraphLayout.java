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
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.layout;

import java.util.Iterator;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;

public class GMFDirectedGraphLayout extends DirectedGraphLayout {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.graph.DirectedGraphLayout#visit(org.eclipse.draw2d
	 * .graph.DirectedGraph)
	 */
	@Override
	public void visit(DirectedGraph graph) {
		storeNodesSizes(graph);
		super.visit(graph);
		recallNodesSizes(graph);
		if (graph.getDirection() != PositionConstants.SOUTH) {
			transpose(graph);
		}
		postProcessGraph(graph);
		if (graph.getDirection() != PositionConstants.SOUTH) {
			transpose(graph);
		}
	}

	/**
	 * Performs layout work after Draw2D DGL completes
	 * 
	 * @param graph
	 *            the directed graph
	 */
	public void postProcessGraph(DirectedGraph graph) {
		invertEdges(graph);
		new EdgeEndPointsAssignment(graph).assignEdgesEndPoints();
		// new PreRouteEdges(graph).preRouteEdges();
		new EdgesRouter(graph).routeEdges();
		invertEdges(graph);

		// new CleanupBorderNodeEdges(graph).cleanup();
	}

	static void storeNodesSizes(DirectedGraph g) {
		for (int i = 0; i < g.nodes.size(); i++) {
			Node n = g.nodes.getNode(i);
			if (n instanceof ConstantSizeNode) {
				ConstantSizeNode cn = (ConstantSizeNode) n;
				cn.constantWidth = cn.width;
				cn.constantHeight = cn.height;
			}
		}
	}

	static void recallNodesSizes(DirectedGraph g) {
		for (int i = 0; i < g.nodes.size(); i++) {
			Node n = g.nodes.getNode(i);
			if (n instanceof ConstantSizeNode) {
				ConstantSizeNode cn = (ConstantSizeNode) n;
				cn.width = cn.constantWidth;
				cn.height = cn.constantHeight;
			}
		}
	}

	static void transpose(DirectedGraph g) {
		for (int i = 0; i < g.nodes.size(); i++) {
			transpose(g.nodes.getNode(i));
		}
		for (int i = 0; i < g.edges.size(); i++) {
			transpose(g.edges.getEdge(i));
		}
		g.getLayoutSize().transpose();
		g.getDefaultPadding().transpose();
		if (g instanceof CompoundDirectedGraph) {
			CompoundDirectedGraph cg = (CompoundDirectedGraph) g;
			for (int i = 0; i < cg.subgraphs.size(); i++) {
				transpose(cg.subgraphs.getNode(i));
			}
		}
	}

	private static void transpose(Node n) {
		int temp = n.x;
		n.x = n.y;
		n.y = temp;
		temp = n.width;
		n.width = n.height;
		n.height = temp;
		if (n.getPadding() != null) {
			n.getPadding().transpose();
		}
		if (n instanceof ConstantSizeNode) {
			for (Iterator<BorderNode> itr = ((ConstantSizeNode) n).borderNodes
					.iterator(); itr.hasNext();) {
				transpose(itr.next());
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void transpose(Edge e) {
		e.start.transpose();
		e.end.transpose();
		e.getPoints().transpose();
		if (e.vNodes != null) {
			for (int i = 0; i < e.vNodes.size(); i++) {
				transpose(e.vNodes.getNode(i));
			}
		}
	}

	static void invertEdges(DirectedGraph g) {
		for (int i = 0; i < g.edges.size(); i++) {
			Edge e = g.edges.getEdge(i);
			if (e.isFeedback()) {
				e.invert();
			}
		}
	}
}
