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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.Action;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.AnnotationStatus;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.DiagramRebuildUtils;
import org.eclipse.tigerstripe.annotation.ui.util.AdaptableUtil;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Yuri Strot
 *
 */
public class ChangeAnnotationVisibility extends Action {
	
	private DiagramEditor editor; 
	private EditPart ref;
	private AnnotationStatus[] statuses;
	
	private Annotation annotation;
	private boolean show;
	
	public ChangeAnnotationVisibility(Annotation annotation, boolean show) {
		this.annotation = annotation;
		this.show = show;
		init();
	}
	
	protected void init() {
		IWorkbenchPart part = WorkbenchUtil.getEditor();
		if (part instanceof DiagramEditor) {
			editor = (DiagramEditor)part;
			ref = getPart(editor);
			if (ref != null) {
				AnnotationNode node = getNode(editor);
				AnnotationStatus status = node == null ? new AnnotationStatus(annotation) :
					new AnnotationStatus(node);
				if ((status.getStatus() != AnnotationStatus.STATUS_VISIBLE || !show) &&
						(status.getStatus() == AnnotationStatus.STATUS_VISIBLE || show)) {
					statuses = new AnnotationStatus[] { status };
					setEnabled(true);
					return;
				}
			}
		}
		setEnabled(false);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		if (show)
			DiagramRebuildUtils.showAnnotations(editor, 
					ref, statuses);
		else
			DiagramRebuildUtils.hideAnnotations(editor, 
					ref, statuses);
	}
	
	protected EditPart getPart(DiagramEditor editor) {
		DiagramEditPart part = editor.getDiagramEditPart();
		List<?> list = part.getChildren();
		for (Object object : list) {
			if (object instanceof EditPart) {
				EditPart child = (EditPart)object;
				Annotation[] annotations = AdaptableUtil.getAllAnnotations(child);
				for (int i = 0; i < annotations.length; i++) {
					if (annotations[i].equals(annotation))
						return child;
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
