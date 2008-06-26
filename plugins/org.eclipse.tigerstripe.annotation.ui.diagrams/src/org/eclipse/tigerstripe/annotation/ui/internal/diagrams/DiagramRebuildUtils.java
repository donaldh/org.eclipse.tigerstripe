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
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest.ConnectionViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.ui.diagrams.DiagramAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.diagrams.IAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaAnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations;
import org.eclipse.tigerstripe.annotation.ui.diagrams.parts.AnnotationEditPart;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.parts.AnnotationConnectionEditPart;

/**
 * @author Yuri Strot
 * 
 */
public class DiagramRebuildUtils {

	public static void rebuld(DiagramEditor editor) {
		DiagramEditPart root = editor.getDiagramEditPart();

		Map<EObject, EditPart> elements = new HashMap<EObject, EditPart>();
		collectElements(root, elements);

		for (EObject object : elements.keySet()) {
			EditPart part = elements.get(object);
			if (part instanceof AnnotationEditPart)
				continue;
			updateAnnotations(editor, part);
		}
		editor.doSave(new NullProgressMonitor());
	}

	protected static void collectElements(EditPart part,
			Map<EObject, EditPart> elements) {
		List<?> list = part.getChildren();
		for (Object object : list) {
			if (object instanceof EditPart) {
				EditPart child = (EditPart) object;
				collectElement(child, elements);
				collectElements(child, elements);
			}
		}
	}

	protected static void collectElement(EditPart part,
			Map<EObject, EditPart> elements) {
		if (part.getModel() instanceof View) {
			View view = (View) part.getModel();
			EObject element = view.getElement();

			if (element != null && elements.get(element) == null)
				elements.put(element, part);
		}
	}

	public static AnnotationStatus[] getPartAnnotations(EditPart part) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		AnnotationUtils.getAllAnnotations(part, annotations);

		Map<Annotation, AnnotationNode> parts = new HashMap<Annotation, AnnotationNode>();

		GraphicalEditPart gep = (GraphicalEditPart) part;
		View view = (View) gep.getModel();
		addAllAnnotations(parts, view.getSourceEdges(), false);
		addAllAnnotations(parts, view.getTargetEdges(), true);

		AnnotationStatus[] statuses = new AnnotationStatus[annotations.size()];
		int i = 0;
		for (Annotation annotation : annotations) {
			AnnotationNode node = parts.get(annotation);
			if (node != null)
				statuses[i] = new AnnotationStatus(node);
			else
				statuses[i] = new AnnotationStatus(annotation);
			i++;
		}
		return statuses;
	}

	public static void updateMetaInfo(final DiagramEditor editor,
			final EditPart part, final AnnotationStatus[] statuses, final boolean show) {
		final MetaAnnotationNode node = getMetaInfo(editor);
		modify(editor.getEditingDomain(), new Runnable() {
		
			public void run() {
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
				for (int i = 0; i < statuses.length; i++) {
					String id = getTypeId(statuses[i]);
					if (id != null) {
						if (show) {
							if (!partMeta.getTypes().contains(id))
								partMeta.getTypes().add(id);
						}
						else {
							partMeta.getTypes().remove(id);
						}
					}
				}
			}
		
		});
	}
	
	protected static void modify(TransactionalEditingDomain domain,
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
		if (part.getParent() != null) {
			List<View> views = new ArrayList<View>();
			for (AnnotationStatus status : annotations) {
				switch (status.getStatus()) {
				case AnnotationStatus.STATUS_NON_EXIST:
					EditPart annotationPart = createAnnotation(
							part.getParent(), status.getAnnotation());
					if (annotationPart != null)
						createConnection(editor, annotationPart, part);
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

	protected static void setViewVisible(
			final TransactionalEditingDomain domain, final View view,
			final boolean visible) {

		EObjectAdapter adapter = new EObjectAdapter(view);
		SetPropertyCommand command = new SetPropertyCommand(domain, adapter,
				PackageUtil.getID(NotationPackage.eINSTANCE.getView_Visible()),
				"visible", Boolean.valueOf(visible));
		CompositeTransactionalCommand cc = new CompositeTransactionalCommand(
				domain, "Some label");
		cc.add(command);
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
				if (source)
					st.add(edge.getSource());
				else
					st.add(edge.getTarget());
			}
		}
		for (View view : st)
			addAnnotation(annotations, view);
	}

	protected static void addAnnotation(
			Map<Annotation, AnnotationNode> annotations, View view) {
		if (view instanceof AnnotationNode) {
			AnnotationNode node = (AnnotationNode) view;
			Annotation annotation = node.getAnnotation();
			if (annotation != null)
				annotations.put(annotation, node);
			else {
				DeleteCommand command = new DeleteCommand(view);
				if (command.canExecute()) {
					try {
						command.execute(new NullProgressMonitor(), null);
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected static AnnotationEditPart getAnnotationEditPart(EditPart part) {
		if (part instanceof AnnotationEditPart)
			return (AnnotationEditPart) part;
		if (part instanceof AnnotationConnectionEditPart) {
			AnnotationConnectionEditPart connection = (AnnotationConnectionEditPart) part;
			if (connection.getTarget() instanceof AnnotationEditPart)
				return (AnnotationEditPart) connection.getTarget();
			if (connection.getSource() instanceof AnnotationEditPart)
				return (AnnotationEditPart) connection.getSource();
		}
		return null;
	}

	protected static Annotation getEqualsAnnotation(
			List<Annotation> annotations, AnnotationEditPart part) {
		Annotation ann = part.getAnnotation();
		if (ann == null)
			return null;
		for (Annotation annotation : annotations) {
			if (annotation.getId() == ann.getId())
				return annotation;
		}
		return null;
	}

	protected static void updateAnnotations(DiagramEditor editor, EditPart part) {
		EditPart container = part.getParent();
		View view = (View)part.getModel();
		if (container != null) {
			AnnotationStatus[] annotations = getPartAnnotations(part);
			MetaAnnotationNode node = getMetaInfo(editor);
			for (Object object : node.getChildren()) {
				if (object instanceof MetaViewAnnotations) {
					MetaViewAnnotations mva = (MetaViewAnnotations)object;
					if (mva.getView() == view) {
						EList<String> types = mva.getTypes();
						List<AnnotationStatus> statuses = new ArrayList<AnnotationStatus>();
						for (AnnotationStatus annotationStatus : annotations) {
							String id = getTypeId(annotationStatus);
							if (id != null) {
								if (types.contains(id))
									statuses.add(annotationStatus);
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
			if (type != null)
				return type.getId();
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

	protected static EditPart createAnnotation(EditPart part, Annotation annotation) {
		Node node = createNode(DiagramAnnotationType.ANNOTATION_TYPE, new EObjectAdapter(annotation), 
				((IGraphicalEditPart) part).getEditingDomain(), (View) part.getModel());
		if (node != null) {
			Object object = part.getViewer().getEditPartRegistry()
					.get(node);
			if (object instanceof EditPart) {
				return (EditPart) object;
			}
		}
		return null;
	}

	protected static EditPart createConnection(DiagramEditor editor,
			EditPart source, EditPart target) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source;
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target;

		CreateConnectionRequest connectionRequest = createTargetRequest();

		connectionRequest.setTargetEditPart(sourceEditPart);
		connectionRequest.setType(RequestConstants.REQ_CONNECTION_START);
		connectionRequest.setLocation(new Point(0, 0));

		// only if the connection is supported will we get a non null
		// command from the sourceEditPart
		if (sourceEditPart.getCommand(connectionRequest) != null) {

			connectionRequest.setSourceEditPart(sourceEditPart);
			connectionRequest.setTargetEditPart(targetEditPart);
			connectionRequest.setType(RequestConstants.REQ_CONNECTION_END);
			connectionRequest.setLocation(new Point(0, 0));

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
