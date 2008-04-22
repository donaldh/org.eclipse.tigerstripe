/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.ui.viewsupport.ProblemTreeViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.LogicalExplorerNodeFactory;

public class TSProblemTreeViewer extends ProblemTreeViewer {

	public TSProblemTreeViewer(Composite parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	public TSProblemTreeViewer(Tree tree) {
		super(tree);
		// TODO Auto-generated constructor stub
	}

	public TSProblemTreeViewer(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Need to filter the changes on a files that are potentially changes
	 * 
	 */
	@Override
	protected void handleLabelProviderChanged(LabelProviderChangedEvent event) {
		Object[] rawElements = event.getElements();
		List<Object> filteredChildren = new ArrayList<Object>();
		for (Object object : rawElements) {
			Object node = LogicalExplorerNodeFactory.getInstance().getNode(
					object);
			if (node != null) {
				filteredChildren.add(node);
			}
		}

		LabelProviderChangedEvent filteredEvent = new LabelProviderChangedEvent(
				(IBaseLabelProvider) event.getSource(), filteredChildren
						.toArray(new Object[filteredChildren.size()]));
		super.handleLabelProviderChanged(filteredEvent);
	}

}
