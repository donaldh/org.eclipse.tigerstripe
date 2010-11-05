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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.ReconnectRequest;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class BaseLayout {

	// Minimum sep between icon and bottommost horizontal arc
	protected int minX = -1;
	protected int minY = -1;
	protected int layoutDefaultMargin = 0;

	protected static final int NODE_PADDING = 30;
	protected static final int MIN_EDGE_PADDING = 15;
	protected static final int MAX_EDGE_PADDING = NODE_PADDING * 3;
	protected static final int MIN_EDGE_END_POINTS_PADDING = 5;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gmf.runtime.diagram.ui.services.layout.
	 * AbstractLayoutEditPartProvider
	 * #layoutEditParts(org.eclipse.gef.GraphicalEditPart,
	 * org.eclipse.core.runtime.IAdaptable)
	 */
	public Command layoutEditParts(GraphicalEditPart containerEditPart) {
		// setup graph
		DirectedGraph g = createGraph();
		build_graph(g, containerEditPart.getChildren());
		createGraphLayout().visit(g);
		// update the diagram based on the graph
		Command cmd = update_diagram(containerEditPart, g, false);

		return cmd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gmf.runtime.diagram.ui.services.layout.
	 * AbstractLayoutEditPartProvider#layoutEditParts(java.util.List,
	 * org.eclipse.core.runtime.IAdaptable)
	 */
	public Command layoutEditParts(List<GraphicalEditPart> selectedObjects) {

		if (selectedObjects.size() == 0) {
			return null;
		}

		// get the container edit part for the children
		GraphicalEditPart editPart = selectedObjects.get(0);
		GraphicalEditPart containerEditPart = (GraphicalEditPart) editPart
				.getParent();

		DirectedGraph g = createGraph();
		build_graph(g, selectedObjects);
		createGraphLayout().visit(g);
		// update the diagram based on the graph
		Command cmd = update_diagram(containerEditPart, g, true);

		return cmd;
	}

	/**
	 * layoutTopDown Utility function that is commonly subclasses by domain
	 * specific layouts to determine whether a specific connection type is layed
	 * out in a top down manner.
	 * 
	 * @param poly
	 *            <code>ConnectionEditPart</code> to determine whether it is to
	 *            be layed out in a top-down fashion.
	 * @return true if connection is to be layed out top-down, false otherwise.
	 */
	protected boolean layoutTopDown(ConnectionEditPart poly) {
		return false;
	}

	/**
	 * build_nodes Method to build up the nodes in the temporary Graph structure
	 * which the algorithm is executed on.
	 * 
	 * @param selectedObjects
	 *            List of selected objects to be layed out.
	 * @param editPartToNodeDict
	 *            Map of editparts from the GEF to the temporary Nodes used for
	 *            layout.
	 * @return NodeList list of Nodes that is passed to the graph structure.
	 */
	protected NodeList build_nodes(List<GraphicalEditPart> selection,
			Map<GraphicalEditPart, Node> editPartToNodeDict, Subgraph root) {
		ListIterator<GraphicalEditPart> li = selection.listIterator();

		NodeList nodes = new NodeList();

		while (li.hasNext()) {
			GraphicalEditPart node = li.next();

			Point position = getLocation(node);

			// determine topleft most point, layout of items will be placed
			// starting at topleft point
			if (minX == -1) {
				minX = position.x;
				minY = position.y;
			} else {
				minX = Math.min(minX, position.x);
				minY = Math.min(minY, position.y);
			}

			ConstantSizeNode n = new ConstantSizeNode(node);
			n.setPadding(new Insets(NODE_PADDING));
			n.setMinIncomingPadding(MIN_EDGE_END_POINTS_PADDING);
			n.setMinOutgoingPadding(MIN_EDGE_END_POINTS_PADDING);
			Dimension size = getSize(node);

			setNodeMetrics(n, new Rectangle(position.x, position.y, size.width,
					size.height));

			editPartToNodeDict.put(node, n);
			nodes.add(n);
		}

		return nodes;
	}

	protected abstract Point getLocation(GraphicalEditPart node);

	protected abstract Dimension getSize(GraphicalEditPart node);

	/**
	 * setNodeMetrics Sets the extend and position into the node object. Defined
	 * as abstract to allow subclasses to implement to perform a transformation
	 * on the values stored in the node. i.e. support for Left-Right layout as
	 * opposed to Top-Down.
	 * 
	 * @param n
	 *            Node that will receive the metrics values to be set.
	 * @param r
	 *            Rectangle that represents the location and extend of the Node.
	 */
	final protected void setNodeMetrics(Node n, Rectangle r) {
		Rectangle rectGraph = translateToGraph(r);
		n.x = rectGraph.x;
		n.y = rectGraph.y;
		n.width = rectGraph.width;
		n.height = rectGraph.height;
	}

	/**
	 * getNodeMetrics Retrieves the node extend and position from the node
	 * object. Defined as abstract to allow subclasses to implement to perform a
	 * transformation on the values stored in the node. i.e. support for
	 * Left-Right layout as opposed to Top-Down.
	 * 
	 * @param n
	 *            Node that has the metrics values to be retrieved.
	 * @return Rectangle that represents the location and extend of the Node.
	 */
	protected Rectangle getNodeMetrics(Node n) {
		Rectangle rect = new Rectangle(n.x, n.y, n.width, n.height);
		PrecisionRectangle preciseRect = new PrecisionRectangle(rect);
		return translateFromGraph(preciseRect);
	}

	/**
	 * Retrieves the extent and position from the given logical rectangle in GEF
	 * graph coordinates. Defined as abstract to allow subclasses to implement
	 * to perform a transformation on the values stored in the node. i.e.
	 * support for Left-Right layout as opposed to Top-Down.
	 * 
	 * @param rect
	 *            <code>Rectangle</code> that has the values to be translated in
	 *            logical (relative) coordinates.
	 * 
	 * @return <code>Rectangle</code> in graph coordinates.
	 */
	abstract protected Rectangle translateToGraph(Rectangle r);

	/**
	 * Retrieves the logical extent and position from the given rectangle.
	 * Defined as abstract to allow subclasses to implement to perform a
	 * transformation on the values stored in the node. i.e. support for
	 * Left-Right layout as opposed to Top-Down.
	 * 
	 * @param rect
	 *            <code>Rectangle</code> that has the values to be translated in
	 *            graph (pixel) coordinates.
	 * @return <code>Rectangle</code> in logical coordinates.
	 */
	abstract protected Rectangle translateFromGraph(Rectangle rect);

	/**
	 * build_edges Method to build up the edges in the temporary Graph structure
	 * which the algorithm is executed on.
	 * 
	 * selectedObjects List of selected objects to be layed out.
	 * 
	 * @param editPartToNodeDict
	 *            Map of editparts from the GEF to the temporary Nodes used for
	 *            layout.
	 * @return EdgeList list of Edges that is passed to the graph structure.
	 */
	protected EdgeList build_edges(List<GraphicalEditPart> selectedObjects,
			Map<GraphicalEditPart, Node> editPartToNodeDict) {

		EdgeList edges = new EdgeList();

		// Do "topdown" relationships first. Since they traditionally
		// go upward on a diagram, they are reversed when placed in the graph
		// for
		// layout. Also, layout traverses the arcs from each node in the order
		// of their insertion when finding a spanning tree for its constructed
		// hierarchy. Therefore, arcs added early are less likely to be
		// reversed.
		// In fact, since there are no cycles in these arcs, adding
		// them to the graph first should guarantee that they are never
		// reversed,
		// i.e., the inheritance hierarchy is preserved graphically.
		ArrayList<GraphicalEditPart> objects = new ArrayList<GraphicalEditPart>();
		objects.addAll(selectedObjects);
		ListIterator<GraphicalEditPart> li = objects.listIterator();
		ArrayList<ConnectionEditPart> notTopDownEdges = new ArrayList<ConnectionEditPart>();
		while (li.hasNext()) {
			GraphicalEditPart gep = li.next();
			if (gep instanceof ConnectionEditPart) {
				ConnectionEditPart poly = (ConnectionEditPart) gep;

				if (layoutTopDown(poly)) {
					EditPart from = poly.getSource();
					EditPart to = poly.getTarget();
					Node fromNode = editPartToNodeDict.get(from);
					Node toNode = editPartToNodeDict.get(to);

					if (fromNode != null && toNode != null
							&& !checkSelfEdge(from, to, editPartToNodeDict)) {
						addEdge(edges, poly, toNode, fromNode);
					}
				} else {
					notTopDownEdges.add(poly);
				}
			}
		}

		// third pass for all other connections
		ListIterator<ConnectionEditPart> cli = notTopDownEdges.listIterator();
		while (cli.hasNext()) {
			ConnectionEditPart poly = cli.next();
			EditPart from = poly.getSource();
			EditPart to = poly.getTarget();
			Node fromNode = editPartToNodeDict.get(from);
			Node toNode = editPartToNodeDict.get(to);

			if (fromNode != null && toNode != null
					&& !checkSelfEdge(from, to, editPartToNodeDict)) {
				addEdge(edges, poly, fromNode, toNode);
			}
		}
		return edges;
	}

	private boolean checkSelfEdge(EditPart from, EditPart to,
			Map<GraphicalEditPart, Node> dictionary) {
		Node graphSource = dictionary.get(from);
		Node graphTarget = dictionary.get(to);
		return graphSource != null && graphTarget != null
				&& graphSource.equals(graphTarget);
	}

	/**
	 * @param edges
	 * @param gep
	 * @param fromNode
	 * @param toNode
	 */
	private void addEdge(EdgeList edges, ConnectionEditPart connectionEP,
			Node fromNode, Node toNode) {
		ConstrainedEdge edge = new ConstrainedEdge(connectionEP, fromNode,
				toNode);
		initializeEdge(connectionEP, edge);

		edges.add(edge);
	}

	/**
	 * initializeEdge Method used as a hook to initialize the Edge layout
	 * object. LayoutProvider subclasses may wish to initialize the edge
	 * different to customize the layout for their diagram domain.
	 * 
	 * @param connectionEP
	 *            EditPart used as a seed to initialize the edge properties
	 * @param edge
	 *            Edge to initialize with default values for the layout
	 */
	protected void initializeEdge(ConnectionEditPart connectionEP, Edge edge) {
		List affectingChildren = getAffectingChildren(connectionEP);

		// set the padding based on the extent of the children.
		edge.setPadding(Math.max(edge.getPadding(),
				calculateEdgePadding(connectionEP, affectingChildren)));
		edge.setDelta(Math.max(edge.getDelta(), affectingChildren.size() / 2));
		if (edge instanceof ConstrainedEdge
				&& ((Connection) connectionEP.getFigure())
						.getConnectionRouter() instanceof OrthogonalRouter) {
			((ConstrainedEdge) edge)
					.setStyle(ConstrainedEdge.ORTHOGONAL_ROUTING_STYLE);
		}
	}

	/**
	 * Calculates the edge padding needed to initialize edge with. Uses the
	 * number of children as a factor in determine the dynamic padding value.
	 */
	private int calculateEdgePadding(ConnectionEditPart connectionEP,
			List<GraphicalEditPart> affectingChildren) {
		ListIterator<GraphicalEditPart> li = affectingChildren.listIterator();

		int padding = 0;

		// union the children widths
		while (li.hasNext()) {
			GraphicalEditPart gep = li.next();

			padding = Math.max(padding, Math.max(
					gep.getFigure().getBounds().width, gep.getFigure()
							.getBounds().height));
		}

		Rectangle.SINGLETON.x = 0;
		Rectangle.SINGLETON.y = 0;
		Rectangle.SINGLETON.width = padding;
		Rectangle.SINGLETON.height = padding;
		return Math.min(Math.max(
				Math.round(translateToGraph(Rectangle.SINGLETON).width * 1.5f),
				MIN_EDGE_PADDING), MAX_EDGE_PADDING);
	}

	/**
	 * Retrieve the associated children from the given connection editpart that
	 * will affect the layout.
	 * 
	 * @param conn
	 *            the <code>ConnectionEditPart</code> to retrieve the children
	 *            from
	 * @return a <code>List</code> that contains <code>GraphicalEditPart</code>
	 *         objects
	 */
	private List<GraphicalEditPart> getAffectingChildren(ConnectionEditPart conn) {
		List children = conn.getChildren();
		ListIterator lli = children.listIterator();
		List<GraphicalEditPart> affectingChildrenList = new ArrayList<GraphicalEditPart>();
		while (lli.hasNext()) {
			Object obj = lli.next();
			if (obj instanceof GraphicalEditPart) {
				GraphicalEditPart lep = (GraphicalEditPart) obj;
				Rectangle lepBox = lep.getFigure().getBounds().getCopy();

				if (!lep.getFigure().isVisible() || lepBox.width == 0
						|| lepBox.height == 0)
					continue;

				affectingChildrenList.add(lep);
			}
		}
		return affectingChildrenList;
	}

	/**
	 * getRelevantConnections Given the editpart to Nodes Map this will
	 * calculate the connections in the diagram that are important to the
	 * layout.
	 * 
	 * @param editPartToNodeDict
	 *            Hashtable of editparts from the GEF to the temporary Nodes
	 *            used for layout.
	 * @return List of ConnectionEditPart that are to be part of the layout
	 *         routine.
	 */
	protected List getRelevantConnections(
			Hashtable<GraphicalEditPart, Node> editPartToNodeDict) {
		Enumeration<GraphicalEditPart> enumeration = editPartToNodeDict.keys();
		ArrayList connectionsToMove = new ArrayList();
		while (enumeration.hasMoreElements()) {
			GraphicalEditPart part = enumeration.nextElement();
			Set<ConnectionEditPart> sourceConnections = new HashSet<ConnectionEditPart>(
					part.getSourceConnections());

			for (Iterator<ConnectionEditPart> iter = sourceConnections
					.iterator(); iter.hasNext();) {
				ConnectionEditPart connection = iter.next();
				EditPart target = connection.getTarget();
				Object o = editPartToNodeDict.get(target);
				if (o != null) {
					connectionsToMove.add(connection);
				}
			}
		}

		return connectionsToMove;
	}

	@SuppressWarnings("unused")
	private EditPart getFirstAnscestorinNodesMap(EditPart editPart,
			Map editPartToNodeDict) {
		EditPart ancestor = editPart;
		while (ancestor != null) {
			if (editPartToNodeDict.get(ancestor) != null)
				return ancestor;
			ancestor = ancestor.getParent();
		}
		return null;
	}

	/**
	 * Method build_graph. This method will build the proxy graph that the
	 * layout is based on.
	 * 
	 * @param g
	 *            DirectedGraph structure that will be populated with Nodes and
	 *            Edges in this method.
	 * @param selectedObjects
	 *            List of editparts that the Nodes and Edges will be calculated
	 *            from.
	 */
	protected void build_graph(DirectedGraph g,
			List<GraphicalEditPart> selectedObjects) {
		Hashtable<GraphicalEditPart, Node> editPartToNodeDict = new Hashtable<GraphicalEditPart, Node>(
				500);
		this.minX = -1;
		this.minY = -1;
		NodeList nodes = build_nodes(selectedObjects, editPartToNodeDict, null);

		// append edges that should be added to the graph
		ArrayList<GraphicalEditPart> objects = new ArrayList<GraphicalEditPart>();
		objects.addAll(selectedObjects);
		objects.addAll(getRelevantConnections(editPartToNodeDict));
		EdgeList edges = build_edges(objects, editPartToNodeDict);
		g.nodes = nodes;
		g.edges = edges;
		postProcessGraph(g, editPartToNodeDict);
		// printGraph(g);
	}

	protected void postProcessGraph(DirectedGraph g,
			Hashtable editPartToNodeDict) {
		// default do nothing
	}

	/**
	 * reverse Utility function to reverse the order of points in a list.
	 * 
	 * @param c
	 *            PointList that is passed to the routine.
	 * @param rc
	 *            PointList that is reversed.
	 */
	private void reverse(PointList c, PointList rc) {
		rc.removeAllPoints();

		for (int i = c.size() - 1; i >= 0; i--) {
			rc.addPoint(c.getPoint(i));
		}
	}

	/**
	 * Computes the command that will route the given connection editpart with
	 * the given points.
	 */
	protected Command routeThrough(Edge edge, ConnectionEditPart connectEP,
			Node source, Node target, PointList points, Point diff) {

		if (connectEP == null)
			return null;

		PointList routePoints = points;
		if (source.data == connectEP.getTarget()) {
			routePoints = new PointList(points.size());
			reverse(points, routePoints);
			Node tmpNode = source;
			source = target;
			target = tmpNode;
		}

		double totalEdgeDiffX = diff.preciseX();
		double totalEdgeDiffY = diff.preciseY();

		PrecisionPointList allPoints = new PrecisionPointList(
				routePoints.size());
		for (int i = 0; i < routePoints.size(); i++) {
			allPoints.addPrecisionPoint(routePoints.getPoint(i).preciseX()
					+ totalEdgeDiffX, routePoints.getPoint(i).preciseY()
					+ totalEdgeDiffY);
		}

		CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$

		if (cc.isEmpty())
			return null;
		return cc;
	}

	/**
	 * Creates source and target anchor commands and appends them to the
	 * compound command passed in. Returns a line segment ends of which are the
	 * new source and target anchor reference points for further use in the
	 * command setting the bend points.
	 * 
	 * @param cc
	 *            command to add anchors commands to
	 * @param sourceAnchorLocation
	 *            the source anchor location coordinates
	 * @param targetAnchorLocation
	 *            the target anchor location coordinates
	 * @param source
	 *            source node
	 * @param target
	 *            target node
	 * @param cep
	 *            connection editpart
	 * @param diffX
	 *            x axis offset
	 * @param diffY
	 *            y axis offset
	 * @return <code>LineSeg</code> origin is the new source anchor reference
	 *         point and origin is the new target anchor reference point
	 */
	protected LineSeg addAnchorsCommands(CompoundCommand cc,
			Point sourceAnchorLocation, Point targetAnchorLocation,
			Node source, Node target, ConnectionEditPart cep, Point diff) {
		Rectangle sourceExt = getNodeMetrics(source);
		Rectangle targetExt = getNodeMetrics(target);
		sourceExt.translate(diff);
		targetExt.translate(diff);

		/*
		 * If source or target anchor command won't be created or will be
		 * non-executable, source or target reference point is assumed to be the
		 * geometric centre of a shape.
		 */
		Point resultantSourceAnchorReference = sourceExt.getCenter();
		Point resultantTargetAnchorReference = targetExt.getCenter();

		PrecisionPoint sourceRatio = new PrecisionPoint(
				(sourceAnchorLocation.preciseX() - sourceExt.preciseX())
						/ sourceExt.preciseWidth(),
				(sourceAnchorLocation.preciseY() - sourceExt.preciseY())
						/ sourceExt.preciseHeight());
		PrecisionPoint targetRatio = new PrecisionPoint(
				(targetAnchorLocation.preciseX() - targetExt.preciseX())
						/ targetExt.preciseWidth(),
				(targetAnchorLocation.preciseY() - targetExt.preciseY())
						/ targetExt.preciseHeight());

		/*
		 * Need to fake reconnection of the ends of the connection. Currently
		 * existing figure coordinates (old coordinates) needs to be used for
		 * this, since the reconnection location is passed in absolute
		 * coordinates.
		 */
		if (cep.getSource().equals(source.data)) {
			ReconnectRequest reconnectRequest = new ReconnectRequest(
					org.eclipse.gef.RequestConstants.REQ_RECONNECT_SOURCE);
			reconnectRequest.setConnectionEditPart(cep);
			reconnectRequest.setTargetEditPart(cep.getSource());
			IFigure sourceFig = ((GraphicalEditPart) cep.getSource())
					.getFigure();
			Point sourceAnchorReference = new PrecisionPoint(sourceFig
					.getBounds().preciseX()
					+ sourceRatio.preciseX()
					* sourceFig.getBounds().preciseWidth(), sourceFig
					.getBounds().preciseY()
					+ sourceRatio.preciseY()
					* sourceFig.getBounds().preciseHeight());
			sourceFig.translateToAbsolute(sourceAnchorReference);
			reconnectRequest.setLocation(sourceAnchorReference);
			Command sourceAnchorCommand = cep.getSource().getCommand(
					reconnectRequest);
			if (sourceAnchorCommand != null && sourceAnchorCommand.canExecute()) {
				cc.add(sourceAnchorCommand);
			}
		} else {
			resultantSourceAnchorReference = getNewAnchorReferencePoint(source,
					sourceExt, ((Connection) cep.getFigure()).getSourceAnchor()
							.getReferencePoint());
		}

		if (cep.getTarget().equals(target.data)) {
			ReconnectRequest reconnectRequest = new ReconnectRequest(
					org.eclipse.gef.RequestConstants.REQ_RECONNECT_TARGET);
			reconnectRequest.setConnectionEditPart(cep);
			reconnectRequest.setTargetEditPart(cep.getTarget());
			IFigure targetFig = ((GraphicalEditPart) cep.getTarget())
					.getFigure();
			Point targetAnchorReference = new PrecisionPoint(targetFig
					.getBounds().preciseX()
					+ targetRatio.preciseX()
					* targetFig.getBounds().preciseWidth(), targetFig
					.getBounds().preciseY()
					+ targetRatio.preciseY()
					* targetFig.getBounds().preciseHeight());
			targetFig.translateToAbsolute(targetAnchorReference);
			reconnectRequest.setLocation(targetAnchorReference);
			Command targetAnchorCommand = cep.getTarget().getCommand(
					reconnectRequest);
			if (targetAnchorCommand != null && targetAnchorCommand.canExecute()) {
				cc.add(targetAnchorCommand);
			}
		} else {
			resultantTargetAnchorReference = getNewAnchorReferencePoint(target,
					targetExt, ((Connection) cep.getFigure()).getTargetAnchor()
							.getReferencePoint());
		}
		return new LineSeg(resultantSourceAnchorReference,
				resultantTargetAnchorReference);
	}

	private Point getNewAnchorReferencePoint(Node node,
			Rectangle nodeBoundsOnDiagram, Point oldAbsReference) {
		GraphicalEditPart gep = (GraphicalEditPart) node.data;
		PrecisionPoint parentLocation = new PrecisionPoint(gep.getFigure()
				.getBounds().getLocation());
		gep.getFigure().translateToAbsolute(parentLocation);
		PrecisionDimension diff = new PrecisionDimension(
				oldAbsReference.preciseX() - parentLocation.preciseX(),
				oldAbsReference.preciseY() - parentLocation.preciseY());
		return nodeBoundsOnDiagram.getLocation().translate(diff);
	}

	/**
	 * Method update_diagram. Once the layout has been calculated with the GEF
	 * graph structure, the new layout values need to be propogated into the
	 * diagram. This is accomplished by creating a compound command that
	 * contains sub commands to change shapes positions and connection
	 * bendpoints positions. The command is subsequently executed by the calling
	 * action and then through the command infrastructure is undoable and
	 * redoable.
	 * 
	 * @param diagramEP
	 *            IGraphicalEditPart container that is target for the commands.
	 * @param g
	 *            DirectedGraph structure that contains the results of the
	 *            layout operation.
	 * @param isLayoutForSelected
	 *            boolean indicating that the layout is to be performed on
	 *            selected objects only. At this stage this is relevant only to
	 *            calculate the offset in the diagram where the layout will
	 *            occur.
	 * @return Command usually a command command that will set the locations of
	 *         nodes and bendpoints for connections.
	 */
	protected Command update_diagram(GraphicalEditPart diagramEP,
			DirectedGraph g, boolean isLayoutForSelected) {

		CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$

		Point diff = getLayoutPositionDelta(g, isLayoutForSelected);
		Command cmd = createNodeChangeBoundCommands(g, diff);
		if (cmd != null)
			cc.add(cmd);

		cmd = createEdgesChangeBoundsCommands(g, diff);
		if (cmd != null)
			cc.add(cmd);

		return cc;
	}

	/*
	 * Find all of the arcs and set their intermediate points. This loop does
	 * not set the icon positions yet, because that causes recalculation of the
	 * arc connection points. The intermediate points of both outgoing and
	 * incomping arcs must be set before recalculating connection points.
	 */
	protected Command createEdgesChangeBoundsCommands(DirectedGraph g,
			Point diff) {

		CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
		PointList points = new PrecisionPointList(10);

		ListIterator<?> vi = g.edges.listIterator();
		while (vi.hasNext()) {
			Edge edge = (Edge) vi.next();

			if (edge.data == null || edge.getPoints() == null)
				continue;

			points.removeAllPoints();

			ConnectionEditPart cep = null;
			Node source = null, target = null;

			collectPoints(points, edge);
			cep = (ConnectionEditPart) edge.data;
			source = edge.source;
			target = edge.target;

			if (cep != null) {
				// Reset the points list
				Command cmd = routeThrough(edge, cep, source, target, points,
						diff);
				if (cmd != null)
					cc.add(cmd);
			}
		}

		if (cc.isEmpty())
			return null;
		return cc;
	}

	private void collectPoints(PointList points, Edge edge) {
		PointList pointList = edge.getPoints();
		for (int i = 0; i < pointList.size(); i++) {
			Rectangle pt = translateFromGraph(new Rectangle(
					pointList.getPoint(i), new Dimension()));
			points.addPoint(pt.getLocation());
		}
	}

	protected Command createNodeChangeBoundCommands(DirectedGraph g, Point diff) {
		ListIterator vi = g.nodes.listIterator();
		CompoundCommand cc = new CompoundCommand(""); //$NON-NLS-1$
		createSubCommands(diff, vi, cc);
		if (cc.isEmpty())
			return null;
		return cc;
	}

	protected void createSubCommands(Point diff, ListIterator vi,
			CompoundCommand cc) {
		// Now set the position of the icons. This causes the
		// arc connection points to be recalculated
		while (vi.hasNext()) {
			Node node = (Node) vi.next();
			if (node.data instanceof GraphicalEditPart) {
				GraphicalEditPart gep = (GraphicalEditPart) node.data;

				ChangeBoundsRequest request = new ChangeBoundsRequest(
						RequestConstants.REQ_MOVE);
				Rectangle nodeExt = getNodeMetrics(node);
				Point ptLocation = new PrecisionPoint(nodeExt.preciseX()
						+ diff.preciseX(), nodeExt.preciseY() + diff.preciseY());

				PrecisionPoint ptOldLocation = new PrecisionPoint(gep
						.getFigure().getBounds().getLocation());
				gep.getFigure().translateToAbsolute(ptOldLocation);

				gep.getFigure().translateToAbsolute(ptLocation);
				PrecisionPoint delta = new PrecisionPoint(ptLocation.preciseX()
						- ptOldLocation.preciseX(), ptLocation.preciseY()
						- ptOldLocation.preciseY());

				request.setEditParts(gep);
				request.setMoveDelta(delta);
				request.setLocation(ptLocation);

				Command cmd = gep.getCommand(request);
				if (cmd != null && cmd.canExecute()) {
					cc.add(cmd);
				}
			}
			if (node instanceof ConstantSizeNode) {
				ConstantSizeNode cn = (ConstantSizeNode) node;
				for (Iterator<BorderNode> itr = cn.borderNodes.iterator(); itr
						.hasNext();) {
					createBorderItemChangeBoundsCommand(itr.next(), cn, cc);
				}
			}
		}
	}

	private void createBorderItemChangeBoundsCommand(BorderNode bn,
			ConstantSizeNode parentNode, CompoundCommand cc) {
		ChangeBoundsRequest request = new ChangeBoundsRequest(
				RequestConstants.REQ_MOVE);
		Rectangle parentRect = getNodeMetrics(parentNode);
		Rectangle borderItemRect = getNodeMetrics(bn);
		Dimension offset = borderItemRect.getLocation().getDifference(
				parentRect.getLocation());

		IFigure parentFigure = ((GraphicalEditPart) parentNode.data)
				.getFigure();
		IFigure borderItemFigure = ((GraphicalEditPart) bn.data).getFigure();

		PrecisionPoint oldParentLocation = new PrecisionPoint(parentFigure
				.getBounds().getLocation());
		PrecisionPoint oldBorderItemLocation = new PrecisionPoint(
				borderItemFigure.getBounds().getLocation());
		PrecisionPoint newBorderItemLocation = new PrecisionPoint(
				oldParentLocation.preciseX() + offset.preciseWidth(),
				oldParentLocation.preciseY() + offset.preciseHeight());
		parentFigure.translateToAbsolute(oldParentLocation);
		parentFigure.translateToAbsolute(newBorderItemLocation);
		borderItemFigure.translateToAbsolute(oldBorderItemLocation);

		PrecisionPoint delta = new PrecisionPoint(
				newBorderItemLocation.preciseX()
						- oldBorderItemLocation.preciseX(),
				newBorderItemLocation.preciseY()
						- oldBorderItemLocation.preciseY());
		GraphicalEditPart gep = (GraphicalEditPart) bn.data;
		request.setEditParts(gep);
		request.setMoveDelta(delta);
		request.setLocation(newBorderItemLocation);

		Command cmd = gep.getCommand(request);
		if (cmd != null && cmd.canExecute()) {
			cc.add(cmd);
		}
	}

	private Point getLayoutPositionDelta(DirectedGraph g,
			boolean isLayoutForSelected) {
		// If laying out selected objects, use diff variables to
		// position objects at topleft corner of enclosing rectangle.
		if (isLayoutForSelected) {
			ListIterator vi;
			vi = g.nodes.listIterator();
			Point ptLayoutMin = new Point(-1, -1);
			while (vi.hasNext()) {
				Node node = (Node) vi.next();
				// ignore ghost node
				if (node.data != null) {
					Rectangle nodeExt = getNodeMetrics(node);
					if (ptLayoutMin.x == -1) {
						ptLayoutMin.x = nodeExt.x;
						ptLayoutMin.y = nodeExt.y;
					} else {
						ptLayoutMin.x = Math.min(ptLayoutMin.x, nodeExt.x);
						ptLayoutMin.y = Math.min(ptLayoutMin.y, nodeExt.y);
					}
				}
			}

			return new Point(this.minX - ptLayoutMin.x, this.minY
					- ptLayoutMin.y);
		}

		return new Point(layoutDefaultMargin, layoutDefaultMargin);
	}

	/**
	 * Creates the graph that will be used by the layouy provider Clients can
	 * override this method create different kind of graphs This method is
	 * called by
	 * {@link DefaultProvider#layoutEditParts(GraphicalEditPart, IAdaptable) }
	 * and {@link DefaultProvider#layoutEditParts(List, IAdaptable)}
	 * 
	 * @return the Graph that will be used by the layout algorithm
	 */
	protected DirectedGraph createGraph() {
		return new DirectedGraph();
	}

	/**
	 * Creates the graph layout algorithm that will be used to layout the
	 * diagram This method is called by
	 * {@link DefaultProvider#layoutEditParts(GraphicalEditPart, IAdaptable) }
	 * and {@link DefaultProvider#layoutEditParts(List, IAdaptable)}
	 * 
	 * @return the graph layout
	 */
	protected DirectedGraphLayout createGraphLayout() {
		return new GMFDirectedGraphLayout();
	}

}
