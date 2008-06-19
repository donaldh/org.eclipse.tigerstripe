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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest.ConnectionViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.diagrams.DiagramAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.diagrams.IAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.parts.AnnotationConnectionEditPart;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.parts.AnnotationEditPart;
import org.eclipse.tigerstripe.annotation.ui.util.AdaptableUtil;

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
	
	protected static void collectElements(EditPart part, Map<EObject, EditPart> elements) {
		List<?> list = part.getChildren();
		for (Object object : list) {
			if (object instanceof EditPart) {
				EditPart child = (EditPart)object;
				collectElement(child, elements);
				collectElements(child, elements);
			}
		}
	}
	
	protected static void collectElement(EditPart part, Map<EObject, EditPart> elements) {
		if (part.getModel() instanceof View) {
			View view = (View)part.getModel();
			EObject element = view.getElement();
			
			if (element != null && elements.get(element) == null)
				elements.put(element, part);
		}
	}
	
	protected static Annotation[] getAnnotations(EditPart part) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		
		Annotation[] array = AdaptableUtil.getAllAnnotations(part);
		annotations.addAll(Arrays.asList(array));
		
		if (part.getModel() instanceof View) {
			View view = (View)part.getModel();
			if (!DiagramAnnotationType.ANNOTATION_TYPE.equals(view.getType())) {
				EObject element = view.getElement();
				if (element != null) {
					array = AdaptableUtil.getAllAnnotations(element);
					for (int i = 0; i < array.length; i++) {
						if (!annotations.contains(array[i]))
							annotations.add(array[i]);
					}
				}
			}
		}
		
		if (part instanceof GraphicalEditPart) {
			GraphicalEditPart gep = (GraphicalEditPart)part;
			removeAllExist(annotations, gep.getSourceConnections());
			removeAllExist(annotations, gep.getTargetConnections());
		}
		
		return annotations.toArray(new Annotation[annotations.size()]);
	}
	
	protected static void removeAllExist(List<Annotation> annotations, List<?> parts) {
		for (Object object : new ArrayList<Object>(parts)) {
			if (object instanceof EditPart)
				removeExist(annotations, (EditPart)object);
		}
	}
	
	protected static AnnotationEditPart getAnnotationEditPart(EditPart part) {
		if (part instanceof AnnotationEditPart)
			return (AnnotationEditPart)part;
		if (part instanceof AnnotationConnectionEditPart) {
			AnnotationConnectionEditPart connection = (AnnotationConnectionEditPart)part;
			if (connection.getTarget() instanceof AnnotationEditPart)
				return (AnnotationEditPart)connection.getTarget();
			if (connection.getSource() instanceof AnnotationEditPart)
				return (AnnotationEditPart)connection.getSource();
		}
		return null;
	}
	
	protected static void removeExist(List<Annotation> annotations, EditPart part) {
		AnnotationEditPart annotationPart = getAnnotationEditPart(part);
		Annotation annotation = getEqualsAnnotation(annotations, annotationPart);
		if (annotation != null)
			annotations.remove(annotation);
		else {
			View view = (View)annotationPart.getModel();
			DeleteCommand command = new DeleteCommand(view);
			if (command.canExecute())
				try {
					command.execute(new NullProgressMonitor(), null);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
		}
	}
	
	protected static Annotation getEqualsAnnotation(List<Annotation> annotations, AnnotationEditPart part) {
		Annotation ann = part.getAnnotation();
		if (ann == null) return null;
		for (Annotation annotation : annotations) {
			if (annotation.getId() == ann.getId())
				return annotation;
		}
		return null;
	}
	
	protected static void updateAnnotations(DiagramEditor editor, EditPart part) {
		EditPart container = part.getParent();
		if (container != null) {
			Annotation[] annotations = getAnnotations(part);
			for (Annotation annotation : annotations) {
				EditPart annotationPart = createAnnotation(container, annotation);
				if (annotationPart != null) {
					//setText(annotationPart, DisplayAnnotationUtil.getText(annotation));
					createConnection(editor, annotationPart, part);
				}
			}
		}
	}
	
	protected static EditPart createAnnotation(EditPart part, Annotation annotation) {
		IHintedType type = DiagramAnnotationType.ANN;
		String hint = type.getSemanticHint();
		ViewDescriptor viewDescriptor = new ViewDescriptor(new EObjectAdapter(annotation),
			Node.class, hint, PreferencesHint.USE_DEFAULTS);
		CreateCommand command = new CreateCommand(((IGraphicalEditPart)part).getEditingDomain(),
				viewDescriptor, (View)part.getModel());
		if (command.canExecute()) {
			try {
				command.execute(new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			View view = (View)viewDescriptor.getAdapter(View.class);
			if (view != null) {
				Object object = part.getViewer().getEditPartRegistry().get(view);
				if (object instanceof EditPart) {
					return (EditPart)object;
				}
			}
		}
		return null;
	}
	
	protected static void setText(EditPart part, String newText) {
		TransactionalEditingDomain domain = ((IGraphicalEditPart)part).getEditingDomain();
		EObjectAdapter adapter = new EObjectAdapter((View)part.getModel());
		SetPropertyCommand command = new SetPropertyCommand(domain, adapter,
                Properties.ID_DESCRIPTION, ViewType.TEXT, newText);
		if (command.canExecute()) {
			try {
				command.execute(new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected static EditPart createConnection(DiagramEditor editor, EditPart source, EditPart target) {
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
