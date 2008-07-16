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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewLocationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.parts.AnnotationEditPart;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.DelegateAction;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.AnnotationStatus;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.DiagramRebuildUtils;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Yuri Strot
 *
 */
public class ChangeAnnotationVisibility extends DelegateAction {
	
	private DiagramEditor editor; 
	private EditPart ref;
	private AnnotationStatus status;
	
	private Annotation annotation;
	private boolean show;
	
	public ChangeAnnotationVisibility(boolean show) {
		this.show = show;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.internal.actions.DelegateAction#adaptSelection(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	protected void adaptSelection(ISelection selection) {
		Object object = getSelected(selection);
		if (object instanceof Annotation)
			annotation = (Annotation)object;
		else
			annotation = null;
		setEnabled(annotation != null && isEditorEnabled());
	}
	
	protected boolean isEditorEnabled() {
		IWorkbenchPart part = WorkbenchUtil.getEditor();
		if (part instanceof DiagramEditor) {
			editor = (DiagramEditor)part;
			ref = getPart(editor);
			if (ref != null) {
				AnnotationNode node = getNode(editor);
				status = node == null ? new AnnotationStatus(annotation) :
					new AnnotationStatus(node);
				if ((status.getStatus() != AnnotationStatus.STATUS_VISIBLE || !show) &&
						(status.getStatus() == AnnotationStatus.STATUS_VISIBLE || show)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		AnnotationStatus[] statuses = 
			new AnnotationStatus[] { status };
		if (show)
			DiagramRebuildUtils.showAnnotations(editor, 
					ref, statuses);
		else
			DiagramRebuildUtils.hideAnnotations(editor, 
					ref, statuses);
		DiagramRebuildUtils.addToExclusion(editor, ref, status, show);
	}
	
	protected EditPart getPart(DiagramEditor editor) {
		List<?> parts = editor.getDiagramGraphicalViewer().getSelectedEditParts();
		return foundPart(parts);
	}
	
	protected EditPart foundPart(List<?> parts) { 
		for (Object object : parts) {
			if (object instanceof EditPart) {
				EditPart child = (EditPart)object;
				List<Annotation> annotations = new ArrayList<Annotation>();
				AnnotationUtils.getAllAnnotations(object, annotations);
				for (Annotation annotation : annotations) {
					if (annotation.equals(this.annotation))
						return child;
				}
				if (child instanceof AnnotationEditPart) {
					AnnotationEditPart part = (AnnotationEditPart)child;
					List<View> ends = DiagramRebuildUtils.getAnnotationEnd((View)part.getModel());
					if (ends.size() > 0) {
						View view = ends.get(0);
						if (view instanceof ViewLocationNode) {
							EObject targetView = ((ViewLocationNode)view).getView();
							Object result = part.getViewer().getEditPartRegistry().get(targetView);
							if (result instanceof EditPart)
								return (EditPart)result;
						}
					}
				}
			}
		}
		return null;
	}
	
	protected AnnotationNode getNode(DiagramEditor editor) {
		Diagram diagram = editor.getDiagram();
		for (Object object : diagram.getChildren()) {
			if (object instanceof AnnotationNode) {
				AnnotationNode node = (AnnotationNode)object;
				Annotation ann = node.getAnnotation();
				if (ann != null && ann.equals(annotation)) {
					return node;
				}
			}
		}
		return null;
	}

}
