/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.ObjectAdapter;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.layout.LayoutNode;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest.ConnectionViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutService;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.LayoutType;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.ui.diagrams.DiagramAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.diagrams.IAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaAnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewLocationNode;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.locations.LocationMapper;

/**
 * @author Yuri Strot
 * 
 */
public class DiagramRebuildUtils {
	
	public static void collectParts(Map<?, ?> registry, View container, List<EditPart> parts) {
		List<?> children = container.getVisibleChildren();
		List<Edge> edges = new ArrayList<Edge>();
		for (Object child : children) {
			View view = (View)child;
			if (ignore(view)) {
				continue;
			}
			lookForEdges(view, edges);
			collectPart(registry, view, parts);
			collectParts(registry, view, parts);
		}
		for (Edge edge : edges) {
			collectPart(registry, edge, parts);
			collectParts(registry, edge, parts);
		}
	}
	
	protected static void collectPart(Map<?, ?> registry, View view, List<EditPart> parts) {
		Object value = registry.get(view);
		if (value instanceof EditPart) {
			EditPart part = (EditPart)value;
			if (!parts.contains(part)) {
				parts.add(part);
			}
		}
	}
	
	public static Map<View, ViewLocationNode> getLocations(Diagram diagram) {
		List<?> children = diagram.getChildren();
		Map<View, ViewLocationNode> locations = new HashMap<View, ViewLocationNode>();
		for (Object object : children) {
			if (object instanceof ViewLocationNode) {
				ViewLocationNode location = (ViewLocationNode)object;
				View view = getView(location);
				if (view != null) {
					if (!locations.containsKey(view)) {
						locations.put(view, location);
					}
				}
			}
		}
		return locations;
	}
	
	public static View getView(ViewLocationNode location) {
		return (View)location.getView();
	}
	
	protected static boolean ignore(View view) {
		if (view instanceof AnnotationNode || 
				view instanceof MetaAnnotationNode ||
				view instanceof MetaViewAnnotations ||
				view instanceof ViewLocationNode) {
			return true;
		}
		return false;
	}
	
	protected static void lookForEdges(View view, List<Edge> edges) {
		lookForEdges(view.getSourceEdges(), edges);
		lookForEdges(view.getTargetEdges(), edges);
	}
	
	protected static void lookForEdges(List<?> list, List<Edge> edges) {
		for (Object object : list) {
			Edge edge = (Edge)object;
			if (!edges.contains(edge)) {
				edges.add(edge);
				lookForEdges(edge, edges);
			}
		}
	}

	public static void rebuld(DiagramEditor editor) {
		Diagram diagram = editor.getDiagram();
		List<EditPart> parts = new ArrayList<EditPart>();
		collectParts(editor.getDiagramGraphicalViewer().getEditPartRegistry(), diagram, parts);
		
		Map<View, ViewLocationNode> locations = getLocations(diagram);
		for (View view : locations.keySet()) {
			Map<?, ?> registry = editor.getDiagramGraphicalViewer().getEditPartRegistry();
			Object from = registry.get(view);
			Object to = registry.get(locations.get(view));
			if (from instanceof EditPart && to instanceof EditPart) {
				LocationMapper.mapLocation((EditPart)from, (EditPart)to);
			}
		}
		for (EditPart part : parts) {
			updateAnnotations(editor, locations, part);
		}

		if (editor.isDirty()) {
			editor.doSave(new NullProgressMonitor());
		}
	}
	
	public static AnnotationStatus[] getPartAnnotations(EditPart part) {
		View view = (View)part.getModel();
		return getPartAnnotations(part, getLocations(view.getDiagram()));
	}

	public static AnnotationStatus[] getPartAnnotations(EditPart part, Map<View, ViewLocationNode> locations) {
		Set<Annotation> annotations = new LinkedHashSet<Annotation>();
		AnnotationUtils.collectAllAnnotations(part, annotations);

		Map<Annotation, AnnotationNode> parts = new HashMap<Annotation, AnnotationNode>();

		GraphicalEditPart gep = (GraphicalEditPart) part;
		View view = (View) gep.getModel();
		ViewLocationNode location = locations.get(view);
		if (location != null) {
			addAllAnnotations(parts, location.getSourceEdges(), false);
			addAllAnnotations(parts, location.getTargetEdges(), true);
		}

		AnnotationStatus[] statuses = new AnnotationStatus[annotations.size()];
		int i = 0;
		for (Annotation annotation : annotations) {
			AnnotationNode node = parts.get(annotation);
			if (node != null) {
				statuses[i] = new AnnotationStatus(node);
			} else {
				statuses[i] = new AnnotationStatus(annotation);
			}
			i++;
		}
		return statuses;
	}
	
	protected static MetaViewAnnotations getMetaViewAnnotations(
			final DiagramEditor editor, final EditPart part) {
		final MetaAnnotationNode node = getMetaInfo(editor);
		List<?> list = node.getChildren();
		final View view = (View)part.getModel();
		MetaViewAnnotations partMeta = null;
		for (Object object : list) {
			if (object instanceof MetaViewAnnotations) {
				MetaViewAnnotations mva = (MetaViewAnnotations)object;
				if (view == mva.getView()) {
					partMeta = mva;
					break;
				}
			}
		}
		if (partMeta == null) {
			partMeta = (MetaViewAnnotations)createNode(
					DiagramAnnotationType.META_VIEW_ANNOTATION_TYPE, null,
					editor.getEditingDomain(), node);
			partMeta.setView(view);
		}
		return partMeta;
	}

	public static void addToExclusion(final DiagramEditor editor,
			final EditPart part, final AnnotationStatus status, final boolean show) {
		modify(editor.getEditingDomain(), new Runnable() {
		
			public void run() {
				MetaViewAnnotations partMeta = getMetaViewAnnotations(editor, part);
				String annId = status.getAnnotation().getId();
				String id = getTypeId(status);
				if (id != null && annId != null) {
					boolean inTypes = partMeta.getTypes().contains(id);
					boolean addToExclusion = (inTypes && !show) || (!inTypes && show);
					List<String> exclusion = partMeta.getExclusionAnnotations();
					if (addToExclusion && !exclusion.contains(annId)) {
						exclusion.add(annId);
					}
					if (!addToExclusion) {
						exclusion.remove(annId);
					}
				}
			}
		
		});
	}

	public static void updateMetaInfo(final DiagramEditor editor,
			final EditPart part, final AnnotationStatus[] statuses, final boolean show) {
		modify(editor.getEditingDomain(), new Runnable() {
		
			public void run() {
				MetaViewAnnotations partMeta = getMetaViewAnnotations(editor, part);
				for (int i = 0; i < statuses.length; i++) {
					String id = getTypeId(statuses[i]);
					if (id != null) {
						if (show) {
							if (!partMeta.getTypes().contains(id)) {
								partMeta.getTypes().add(id);
							}
						}
						else {
							partMeta.getTypes().remove(id);
						}
					}
					Annotation annotation = statuses[i].getAnnotation();
					if (annotation != null) {
						partMeta.getExclusionAnnotations().remove(
								annotation.getId());
					}
				}
			}
		
		});
	}
	
	public static void modify(TransactionalEditingDomain domain,
			final Runnable runnable) {
		AbstractTransactionalCommand command = new AbstractTransactionalCommand(
				domain, "General modification command", null) {

			@Override
			protected CommandResult doExecuteWithResult(
					IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				runnable.run();
				return null;
			}

		};
		try {
			command.execute(new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void showAnnotations(DiagramEditor editor, EditPart part,
			AnnotationStatus[] annotations) {
		List<View> views = new ArrayList<View>();
		for (AnnotationStatus status : annotations) {
			switch (status.getStatus()) {
			case AnnotationStatus.STATUS_NON_EXIST:
				EditPart annotationPart = createAnnotation(
						editor.getDiagramEditPart(), status.getAnnotation());
				EditPart viewLocation = createViewLocation(
						editor.getDiagramEditPart(), part);
				if (annotationPart != null && viewLocation != null) {
					createConnection(editor, annotationPart, viewLocation);
				}
				break;
			case AnnotationStatus.STATUS_VISIBLE:
				break;
			case AnnotationStatus.STATUS_HIDDEN:
				views.add(status.getNode());
				break;
			}
		}
		setViewsVisible(editor.getEditingDomain(), views
				.toArray(new View[views.size()]), true);
	}
	
	protected static void layout(List<GraphicalEditPart> nodes, DiagramEditPart diagramEP) {
        Shell shell = new Shell();
        try {
            List<Object> hints = new ArrayList<Object>(2);
            hints.add(LayoutType.DEFAULT);
            hints.add(diagramEP);
            IAdaptable layoutHint = new ObjectAdapter(hints);
            final Runnable layoutRun = LayoutService.getInstance()
                .layoutLayoutNodes(getLayoutNodes(nodes), true,
                    layoutHint);
            layoutRun.run();
        } finally {
            shell.dispose();
        }
	}
	
	protected static List<LayoutNode> getLayoutNodes(List<GraphicalEditPart> nodes) {
        List<LayoutNode> layoutNodes = new ArrayList<LayoutNode>(nodes.size());
        Iterator<GraphicalEditPart> it = nodes.iterator();
        while (it.hasNext()) {
            GraphicalEditPart part = it.next();
            Object model = part.getModel();
            if (model instanceof Node) {
                Dimension size = part.getFigure().getBounds().getSize();
                layoutNodes.add(new LayoutNode((Node)model, size.width, size.height));
            }
        }
        return layoutNodes;
	}

	public static void hideAnnotations(DiagramEditor editor, EditPart part,
			AnnotationStatus[] annotations) {
		if (part.getParent() != null) {
			List<View> views = new ArrayList<View>();
			for (AnnotationStatus status : annotations) {
				switch (status.getStatus()) {
				case AnnotationStatus.STATUS_NON_EXIST:
					break;
				case AnnotationStatus.STATUS_VISIBLE:
					views.add(status.getNode());
					break;
				case AnnotationStatus.STATUS_HIDDEN:
					break;
				}
			}
			setViewsVisible(editor.getEditingDomain(), views
					.toArray(new View[views.size()]), false);
		}
	}

	protected static void setViewsVisible(
			final TransactionalEditingDomain domain, final View[] views,
			final boolean visible) {
		List<SetPropertyCommand> commands = new ArrayList<SetPropertyCommand>(
				views.length);
		for (int i = 0; i < views.length; i++) {
			EObjectAdapter adapter = new EObjectAdapter(views[i]);
			SetPropertyCommand command = new SetPropertyCommand(domain,
					adapter, PackageUtil.getID(NotationPackage.eINSTANCE
							.getView_Visible()), "visible", Boolean
							.valueOf(visible));
			commands.add(command);
		}
		CompositeTransactionalCommand command = new CompositeTransactionalCommand(
				domain, "Set views visible", commands);
		if (command.canExecute()) {
			try {
				command.execute(new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

	}

	protected static void addAllAnnotations(
			Map<Annotation, AnnotationNode> annotations, List<?> edges,
			boolean source) {
		List<View> st = new ArrayList<View>();
		for (Object object : edges) {
			if (object instanceof Edge) {
				Edge edge = (Edge) object;
				if (source) {
					st.add(edge.getSource());
				} else {
					st.add(edge.getTarget());
				}
			}
		}
		for (View view : st) {
			addAnnotation(annotations, view);
		}
	}

	protected static void addAnnotation(
			Map<Annotation, AnnotationNode> annotations, View view) {
		if (view instanceof AnnotationNode) {
			AnnotationNode node = (AnnotationNode) view;
			Annotation annotation = node.getAnnotation();
			if (annotation != null) {
				annotations.put(annotation, node);
			} else {
				deleteAnnotation(view);
			}
		}
	}
	
	public static void deleteAnnotation(View view) {
		List<View> views = getAnnotationEnd(view);
		deleteView(view);
		for (View view2 : views) {
			deleteView(view2);
		}
	}
	
	protected static void deleteView(View view) {
		DeleteCommand command = new DeleteCommand(view);
		if (command.canExecute()) {
			try {
				command.execute(new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List<View> getAnnotationEnd(View view) {
		if (view instanceof AnnotationNode) {
			return getSpecifiedEnd(view, ViewLocationNode.class);
		}
		else if (view instanceof ViewLocationNode) {
			return getSpecifiedEnd(view, AnnotationNode.class);
		}
		return null;
	}
	
	protected static List<View> getSpecifiedEnd(View view, Class<?> clazz) {
		List<View> views = new ArrayList<View>();
		for (Object object : view.getSourceEdges()) {
			Edge edge = (Edge)object;
			if (clazz.isInstance(edge.getTarget())) {
				views.add(edge.getTarget());
			}
		}
		for (Object object : view.getTargetEdges()) {
			Edge edge = (Edge)object;
			if (clazz.isInstance(edge.getSource())) {
				views.add(edge.getSource());
			}
		}
		return views;
	}

	protected static void updateAnnotations(DiagramEditor editor, Map<View, ViewLocationNode> locations, EditPart part) {
		EditPart container = part.getParent();
		View view = (View)part.getModel();
		if (container != null) {
			AnnotationStatus[] annotations = getPartAnnotations(part, locations);
			MetaAnnotationNode node = getMetaInfo(editor);
			for (Object object : node.getChildren()) {
				if (object instanceof MetaViewAnnotations) {
					MetaViewAnnotations mva = (MetaViewAnnotations)object;
					if (mva.getView() == view) {
						EList<String> types = mva.getTypes();
						List<AnnotationStatus> statuses = new ArrayList<AnnotationStatus>();
						for (AnnotationStatus annotationStatus : annotations) {
							String annId = annotationStatus.getAnnotation().getId();
							List<String> exlusionAnnotations = mva.getExclusionAnnotations();
							String id = getTypeId(annotationStatus);
							if (id != null) {
								boolean inTypes = types.contains(id);
								boolean inExlusion = exlusionAnnotations.contains(annId);
								if ((inTypes && !inExlusion) || (!inTypes && inExlusion)) {
									statuses.add(annotationStatus);
								}
							}
						}
						if (statuses.size() > 0) {
							showAnnotations(editor, part, statuses.toArray(
									new AnnotationStatus[statuses.size()]));
						}
					}
				}
			}
		}
	}
	
	protected static String getTypeId(AnnotationStatus status) {
		Annotation annotation = status.getAnnotation();
		if (annotation != null) {
			AnnotationType type = AnnotationPlugin.getManager().getType(annotation);
			if (type != null) {
				return type.getId();
			}
		}
		return null;
	}

	protected static MetaAnnotationNode getMetaInfo(final DiagramEditor editor) {
		Diagram diagram = editor.getDiagram();
		List<?> children = diagram.getChildren();
		for (Object object : children) {
			if (object instanceof MetaAnnotationNode) {
				return (MetaAnnotationNode) object;
			}
		}
		return (MetaAnnotationNode)createNode(DiagramAnnotationType.META_ANNOTATION_TYPE,
				null, editor.getEditingDomain(), editor.getDiagram());
	}

	protected static Node createNode(String hint, IAdaptable adaptable,
			TransactionalEditingDomain domain, View parent) {
		ViewDescriptor viewDescriptor = new ViewDescriptor(adaptable, Node.class,
				hint, PreferencesHint.USE_DEFAULTS);

		CreateCommand command = new CreateCommand(domain,
				viewDescriptor, parent);
		if (command.canExecute()) {
			try {
				command.execute(new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			Node node = (Node) viewDescriptor.getAdapter(Node.class);
			while(node.getChildren().size() > 0) {
				View view = (View)node.getChildren().get(0);
				DeleteCommand delete = new DeleteCommand(view);
				try {
					delete.execute(new NullProgressMonitor(), null);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			return node;
		}
		return null;
	}
	
	protected static TransactionalEditingDomain getDomain(EditPart part) {
		if (part instanceof IGraphicalEditPart) {
			return ((IGraphicalEditPart) part).getEditingDomain();
		}
		if (part instanceof RootEditPart) {
			EditPart content = ((RootEditPart)part).getContents();
			if (content instanceof IGraphicalEditPart) {
				return ((IGraphicalEditPart) content).getEditingDomain();
			}
		}
		return null;
	}

	protected static EditPart createAnnotation(EditPart part, Annotation annotation) {
		TransactionalEditingDomain domain = getDomain(part);
		if (domain != null) {
			Node node = createNode(DiagramAnnotationType.ANNOTATION_TYPE, new EObjectAdapter(annotation), 
					domain, (View) part.getModel());
			if (node != null) {
				Object object = part.getViewer().getEditPartRegistry()
						.get(node);
				if (object instanceof EditPart) {
					return (EditPart) object;
				}
			}
		}
		return null;
	}

	protected static EditPart createViewLocation(EditPart containerPart, EditPart viewPart) {
		EditPart part = LocationMapper.getLocationPart(viewPart);
		if (part != null) {
			return part;
		}
		TransactionalEditingDomain domain = getDomain(containerPart);
		if (domain != null) {
			View view = (View)viewPart.getModel();
			Node node = createNode(DiagramAnnotationType.VIEW_LOCATION_NODE_TYPE, new EObjectAdapter(view), 
					domain, view.getDiagram());
			if (node != null) {
				Map<?, ?> registry = containerPart.getViewer().getEditPartRegistry();
				Object object = registry.get(node);
				if (object instanceof EditPart) {
					LocationMapper.mapLocation(viewPart, (EditPart) object);
					return (EditPart) object;
				}
			}
		}
		return null;
	}
	
	protected static Point getLocation(IGraphicalEditPart part) {
		return part.getFigure().getBounds().getCenter();
	}

	protected static EditPart createConnection(DiagramEditor editor,
			EditPart source, EditPart target) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source;
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target;

		CreateConnectionRequest connectionRequest = createTargetRequest();

		connectionRequest.setTargetEditPart(sourceEditPart);
		connectionRequest.setType(RequestConstants.REQ_CONNECTION_START);
		connectionRequest.setLocation(getLocation(sourceEditPart));

		// only if the connection is supported will we get a non null
		// command from the sourceEditPart
		if (sourceEditPart.getCommand(connectionRequest) != null) {

			connectionRequest.setSourceEditPart(sourceEditPart);
			connectionRequest.setTargetEditPart(targetEditPart);
			connectionRequest.setType(RequestConstants.REQ_CONNECTION_END);
			connectionRequest.setLocation(getLocation(targetEditPart));

			Command command = targetEditPart.getCommand(connectionRequest);

			if (command != null && command.canExecute()) {
				command.execute();
			}
		}
		return null;
	}

	protected static CreateConnectionViewRequest createTargetRequest() {
		IAnnotationType type = DiagramAnnotationType.CONNECTION;
		String hint = type.getSemanticHint();

		ConnectionViewDescriptor viewDescriptor = new ConnectionViewDescriptor(
				type, hint, PreferencesHint.USE_DEFAULTS);
		return new CreateConnectionViewRequest(viewDescriptor);
	}

}
