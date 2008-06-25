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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.MenuCreator;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.AnnotationTree;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.AnnotationTree.AnnotationTypeNode;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.AnnotationTree.EditPartNode;
import org.eclipse.ui.IActionDelegate;

/**
 * @author Yuri Strot
 *
 */
public class ChangeAnnotationTypesVisibility extends Action implements IActionDelegate {
	
	private List<Object> list;
	private MenuCreator menu;
	
	private AnnotationTree tree;
	private boolean show;
	
	public ChangeAnnotationTypesVisibility(boolean show) {
		list = new ArrayList<Object>();
		menu = new MenuCreator(list);
		this.show = show;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		List<EditPart> parts = new ArrayList<EditPart>();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection)selection;
			Iterator<?> it = sel.iterator();
			while (it.hasNext()) {
				Object elem = it.next();
				if (elem instanceof DiagramEditPart) {
					List<?> children = ((DiagramEditPart)elem).getChildren();
					for (Object object : children) {
						if (object instanceof EditPart) {
							if (!parts.contains((EditPart)object))
								parts.add((EditPart)object);
						}
					}
					parts.add((EditPart)elem);
				}
				else if (elem instanceof EditPart) {
					if (!parts.contains((EditPart)elem))
						parts.add((EditPart)elem);
				}
			}
		}
		updateParts(parts.toArray(new EditPart[parts.size()]));
		if (! (show ? tree.canBeShown() : tree.canBeHidden()) )
			action.setEnabled(false);
		else {
			action.setEnabled(true);
			updateList();
			action.setMenuCreator(menu);
		}
	}
	
	protected void updateParts(EditPart[] parts) {
		tree = AnnotationTree.buildTree(parts);
	}
	
	protected void updateList() {
		list.clear();
		Map<EditPart, EditPartNode> nodes = new HashMap<EditPart, EditPartNode>();
		for (AnnotationType type : tree.getTypes()) {
			AnnotationTypeNode node = tree.getNode(type);
			if ((show ? node.canBeShown() : node.canBeHidden())) {
				for (EditPartNode epNode : node.getNodes()) {
					EditPart part = epNode.getPart();
					EditPartNode newEPNode = nodes.get(part);
					if (newEPNode == null) {
						newEPNode = new EditPartNode(epNode);
						nodes.put(epNode.getPart(), newEPNode);
					}
					else {
						newEPNode.addAnnotations(epNode.getAnnotations());
					}
				}
				ChangeAnnotationsVisibility action = new ChangeAnnotationsVisibility(
						node.getNodes(), show);
				action.setText(type.getName());
				list.add(action);
			}
		}
		if (list.size() > 1) {
			EditPartNode[] epNodes = nodes.values().toArray(new EditPartNode[nodes.size()]);
			ChangeAnnotationsVisibility action = new ChangeAnnotationsVisibility(
					epNodes, show);
			action.setText("All Types");
			list.add(0, new Separator());
			list.add(0, action);
		}
	}

}
