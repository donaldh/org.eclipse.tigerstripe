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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Path;
import org.eclipse.draw2d.graph.Rank;
import org.eclipse.draw2d.graph.ShortestPathRouter;

/**
 * Routes the edges. Assumes edges have their end points preassigned. Also,
 * edges may have starting/ending routed points preassigned as well. Edges are
 * routed obliquely or orthogonally around the nodes.
 * 
 * @author aboyko
 * @since 2.1
 */
@SuppressWarnings({ "deprecation" })
class EdgesRouter {

	private DirectedGraph g;
	private static int OBSTACLE_WIDTH = 10000;

	public EdgesRouter(DirectedGraph g) {
		this.g = g;
	}

	void routeEdges() {
		for (int i = 0; i < g.nodes.size(); i++) {
			routeEdgesFromNode(g.nodes.getNode(i));
		}
	}

	private void routeEdgesFromNode(Node n) {
		/*
		 * Gather default routed edges going right and left
		 */
		List<Edge> leftEdges = new ArrayList<Edge>();
		List<Edge> rightEdges = new ArrayList<Edge>();

		int rankHeight = getRankHeightFromNode(n, g);
		for (int i = 0; i < n.outgoing.size(); i++) {
			Edge e = n.outgoing.getEdge(i);
			if (shouldAccountForObstaclesInSourceRank(e, rankHeight)) {
				Node guideNode = e.vNodes == null ? e.target : e.vNodes
						.getNode(0);
				int diff = guideNode.x + guideNode.getOffsetIncoming()
						- e.source.x - e.source.getOffsetOutgoing();
				if (n.getLeft() != null && diff < 0) {
					leftEdges.add(e);
				} else if (n.getRight() != null && diff > 0) {
					rightEdges.add(e);
				} else {
					routeEdge(e, null);
				}
			} else {
				routeEdge(e, null);
			}
		}

		/*
		 * Sort left and right edges
		 */
		Collections.sort(leftEdges, new EdgeComparator(false));
		Collections.sort(rightEdges, new EdgeComparator(true));

		routeLeftEdges(leftEdges, n);
		routeRightEdges(rightEdges, n);
	}

	private boolean shouldAccountForObstaclesInSourceRank(Edge e, int rankHeight) {
		if (rankHeight == e.source.height) {
			return false;
		}
		if (e instanceof ConstrainedEdge) {
			ConstrainedEdge ce = (ConstrainedEdge) e;
			if (ce.startingRoutedPoints.size() > 0) {
				return false;
			}
		}
		return true;
	}

	private void routeEdge(Edge e, Rectangle extraObstacle) {
		if (e instanceof ConstrainedEdge
				&& ((ConstrainedEdge) e).getStyle().equals(
						ConstrainedEdge.ORTHOGONAL_ROUTING_STYLE)) {
			routeOrthogonalEdge((ConstrainedEdge) e);
		} else {
			routeDefaultEdge(e, extraObstacle);
		}
	}

	private void routeOrthogonalEdge(ConstrainedEdge edge) {
		PointList points = new PointList();
		if (edge.startingRoutedPoints.size() == 0) {
			points.addPoint(edge.start);
		} else {
			points.addAll(edge.startingRoutedPoints);
		}
		Node previousNode = edge.source;
		if (edge.vNodes != null) {
			for (int i = 0; i < edge.vNodes.size(); i++) {
				Node vNode = edge.vNodes.getNode(i);
				int nextPtX = vNode.x + vNode.width / 2;
				int prevPtX = points.getLastPoint().x;
				if (prevPtX != nextPtX) {
					int rankBottomY = previousNode.y
							+ getRankHeightFromNode(previousNode, g);
					int midY = rankBottomY + (vNode.y - rankBottomY) / 2;
					points.addPoint(prevPtX, midY);
					points.addPoint(nextPtX, midY);
				}
				previousNode = vNode;
			}
		}
		Point prevPt = points.getLastPoint();
		Point lastPt = edge.endingRoutedPoints.size() == 0 ? edge.end
				: edge.endingRoutedPoints.getFirstPoint();
		if (prevPt.x != lastPt.x) {
			int rankBottomY = previousNode.y
					+ getRankHeightFromNode(previousNode, g);
			int midY = rankBottomY + (lastPt.y - rankBottomY) / 2;
			points.addPoint(prevPt.x, midY);
			points.addPoint(lastPt.x, midY);
		}
		if (edge.endingRoutedPoints.size() > 0) {
			points.addAll(edge.endingRoutedPoints);
		} else {
			points.addPoint(edge.end);
		}
		edge.getPoints().removeAllPoints();
		edge.getPoints().addAll(points);
	}

	private Point getPathStartPoint(Edge e) {
		if (e instanceof ConstrainedEdge) {
			ConstrainedEdge ce = (ConstrainedEdge) e;
			if (ce.startingRoutedPoints.size() != 0) {
				return ce.startingRoutedPoints.getLastPoint();
			}
		}
		if (e.start == null) {
			e.start = new Point(e.source.x + e.source.getOffsetOutgoing(),
					e.source.y + e.source.height);
		}
		return e.start;
	}

	private Point getPathEndPoint(Edge e) {
		if (e instanceof ConstrainedEdge) {
			ConstrainedEdge ce = (ConstrainedEdge) e;
			if (ce.endingRoutedPoints.size() != 0) {
				return ce.endingRoutedPoints.getFirstPoint();
			}
		}
		if (e.end == null) {
			e.end = new Point(e.target.x + e.target.getOffsetIncoming(),
					e.target.y);
		}
		return e.end;
	}

	private void routeDefaultEdge(Edge edge, Rectangle extraObstacle) {
		ShortestPathRouter router = new ShortestPathRouter();
		Path path = new Path(getPathStartPoint(edge), getPathEndPoint(edge));
		router.addPath(path);
		if (extraObstacle != null) {
			router.addObstacle(extraObstacle);
		}
		if (edge.vNodes != null) {
			for (int i = 0; i < edge.vNodes.size(); i++) {
				Node node = edge.vNodes.getNode(i);
				int rankHeight = getRankHeightFromNode(node, g);
				if (node.getLeft() != null) {
					int right = node.getLeft().x + node.getLeft().width
							+ g.getPadding(node.getLeft()).right
							+ edge.getPadding();
					int width = Math.max(right, OBSTACLE_WIDTH);
					int height = Math.max(rankHeight, 2);
					router.addObstacle(new Rectangle(right - width, node
							.getLeft().y, width, height));
				}
				if (node.getRight() != null) {
					int left = node.getRight().x
							- g.getPadding(node.getRight()).left
							- edge.getPadding();
					int width = Math.max(g.getLayoutSize().width - left,
							OBSTACLE_WIDTH);
					int height = Math.max(rankHeight, 2);
					router.addObstacle(new Rectangle(left, node.getRight().y,
							width, height));
				}
			}
		}
		router.setSpacing(0);
		router.solve();
		edge.getPoints().removeAllPoints();

		ConstrainedEdge ce = edge instanceof ConstrainedEdge ? (ConstrainedEdge) edge
				: null;
		if (ce != null) {
			for (int i = 0; i < ce.startingRoutedPoints.size() - 1; i++) {
				edge.getPoints().addPoint(ce.startingRoutedPoints.getPoint(i));
			}
		}
		edge.getPoints().addAll(path.getPoints());
		if (ce != null) {
			for (int i = 1; i < ce.endingRoutedPoints.size(); i++) {
				edge.getPoints().addPoint(ce.endingRoutedPoints.getPoint(i));
			}
		}
	}

	@SuppressWarnings("unused")
	private void addSourceObstacle(Edge e, float ratio, boolean leftEdge,
			ShortestPathRouter router) {
		int rankHeight = getRankHeightFromNode(e.source, g);
		if (leftEdge && e.source.getLeft() != null) {
			int leftX = e.source.getLeft().x + e.source.getLeft().width
					+ g.getPadding(e.source.getLeft()).right;
			int rightX = e.source.x + e.source.getOffsetOutgoing();
			int uniformObstacle = (int) (leftX * (1 - ratio) + ratio * rightX);
			int obstacleRightX = Math.min(e.start.x - 1, uniformObstacle);
			int width = Math.max(obstacleRightX, OBSTACLE_WIDTH);
			int height = Math.max(rankHeight, 2);
			router.addObstacle(new Rectangle(obstacleRightX - width, e.source
					.getLeft().y, width, height));
		} else if (e.source.getRight() != null) {
			int leftX = e.source.x + e.source.getOffsetOutgoing();
			int rightX = e.source.getRight().x
					- g.getPadding(e.source.getRight()).left;
			int uniformObstacle = (int) (rightX * (1 - ratio) + ratio * leftX);
			int obstacleLeftX = Math.max(e.start.x + 1, uniformObstacle);
			int width = Math.max(g.getLayoutSize().width - obstacleLeftX,
					OBSTACLE_WIDTH);
			int height = Math.max(rankHeight, 2);
			router.addObstacle(new Rectangle(obstacleLeftX,
					e.source.getRight().y, width, height));
		}
	}

	private void routeLeftEdges(List<Edge> edges, Node n) {
		if (edges.isEmpty()) {
			return;
		}
		int rankHeight = getRankHeightFromNode(n, g);
		int obstacleX = n.getLeft().x + n.getLeft().width
				+ g.getPadding(n.getLeft()).right;
		int median = n.x + n.getOffsetOutgoing();
		for (int i = 0; i < edges.size(); i++) {
			Edge e = edges.get(i);
			if (e instanceof ConstrainedEdge
					&& ConstrainedEdge.ORTHOGONAL_ROUTING_STYLE
							.equals(((ConstrainedEdge) e).getStyle())) {
				routeEdge(e, null);
				obstacleX = e.start.x;
			} else {
				int offset = Math.min(e.getPadding(),
						Math.abs(obstacleX - median) / (edges.size() + 1 - i));
				obstacleX = Math.min(obstacleX + offset, e.start.x - 1);
				int width = Math.max(obstacleX, OBSTACLE_WIDTH);
				int height = Math.max(rankHeight, 2);
				routeEdge(e,
						new Rectangle(obstacleX - width, e.source.getLeft().y,
								width, height));
			}
		}
	}

	private void routeRightEdges(List<Edge> edges, Node n) {
		if (edges.isEmpty()) {
			return;
		}
		int rankHeight = getRankHeightFromNode(n, g);
		int obstacleX = n.getRight().x - g.getPadding(n.getRight()).left;
		int median = n.x + n.getOffsetOutgoing();
		for (int i = 0; i < edges.size(); i++) {
			Edge e = edges.get(i);
			if (e instanceof ConstrainedEdge
					&& ConstrainedEdge.ORTHOGONAL_ROUTING_STYLE
							.equals(((ConstrainedEdge) e).getStyle())) {
				routeEdge(e, null);
				obstacleX = e.start.x;
			} else {
				int offset = Math.min(e.getPadding(),
						Math.abs(obstacleX - median) / (edges.size() + 1 - i));
				obstacleX = Math.max(obstacleX - offset, e.start.x + 1);
				int width = Math.max(g.getLayoutSize().width - obstacleX,
						OBSTACLE_WIDTH);
				int height = Math.max(rankHeight, 2);
				routeEdge(e, new Rectangle(obstacleX, e.source.getRight().y,
						width, height));
			}
		}
	}

	private class EdgeComparator implements Comparator<Edge> {

		private boolean reverse;

		public EdgeComparator(boolean reverse) {
			this.reverse = reverse;
		}

		public int compare(Edge e1, Edge e2) {
			Node guideNode1 = e1.vNodes == null ? e1.target : e1.vNodes
					.getNode(0);
			Node guideNode2 = e2.vNodes == null ? e2.target : e2.vNodes
					.getNode(0);
			int diff = guideNode1.x + guideNode1.getOffsetIncoming()
					- guideNode2.x - guideNode2.getOffsetIncoming();
			return reverse ? -diff : diff;
		}
	}

	static int getRankHeightFromNode(Node n, DirectedGraph g) {
		int rankHeight = -1;
		Rank rank = getNodeRank(n, g);
		if (rank != null) {
			for (int i = 0; i < rank.size(); i++) {
				rankHeight = Math.max(rankHeight, rank.getNode(i).height);
			}
		}
		return rankHeight;
	}

	static Rank getNodeRank(Node n, DirectedGraph g) {
		for (int i = 0; i < g.ranks.size(); i++) {
			Rank rank = g.ranks.getRank(i);
			if (!rank.isEmpty()) {
				if (n.y == rank.getNode(0).y) {
					return rank;
				}
			}
		}
		return null;
	}

}
