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
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.editor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyDiagramHandler;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.layout.LayoutUtils;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts.SubjectEditPart;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Registry;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.utils.CheckUtils;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;

public class DependencyDiagramHandler implements IDependencyDiagramHandler {

	private final DependencyDiagramEditor editor;

	public DependencyDiagramHandler(DependencyDiagramEditor editor) {
		this.editor = CheckUtils.notNull(editor, "editor");
	}

	public void addDependency(IDependencySubject from, IDependencySubject to) {
		getRegistry().addDependency(from, to);

		SubjectEditPart fromEditPart = findEditPart(from);
		if (fromEditPart != null) {
			fromEditPart.getParent().refresh();
			fromEditPart.refresh();
		}
		SubjectEditPart toEditPart = findEditPart(to);
		if (toEditPart != null) {
			toEditPart.refresh();
		}
	}

	public void removeDependency(IDependencySubject from, IDependencySubject to) {
		getRegistry().removeDependency(from, to);

		SubjectEditPart fromEditPart = findEditPart(from);
		if (fromEditPart != null) {
			fromEditPart.getParent().refresh();
			fromEditPart.refresh();
		}
		SubjectEditPart toEditPart = findEditPart(to);
		if (toEditPart != null) {
			toEditPart.refresh();
		}
	}

	private SubjectEditPart findEditPart(IDependencySubject externalSubject) {
		Set<?> entrySet = editor.getGraphicalViewer().getEditPartRegistry()
				.entrySet();
		for (Object object : entrySet) {
			Entry<?, ?> entry = (Entry<?, ?>) object;
			Object element = entry.getKey();
			if (element instanceof Subject
					&& ((Subject) element).getExternalId().equals(
							externalSubject.getId())) {
				return (SubjectEditPart) entry.getValue();
			}
		}
		return null;
	}

	public void layout(Collection<IDependencySubject> externalSubjects) {
		Set<?> entrySet = editor.getGraphicalViewer().getEditPartRegistry()
				.entrySet();
		Set<GraphicalEditPart> parts = new HashSet<GraphicalEditPart>();
		for (Object object : entrySet) {
			Entry<?, ?> entry = (Entry<?, ?>) object;
			if (externalSubjects.contains(entry.getKey())) {
				parts.add((GraphicalEditPart) entry.getValue());
			}
		}
		LayoutUtils.layout(parts, false);
	}

	public void update() {
		getRegistry()
				.update(editor.getExternalContext().getRootExternalModel());
		editor.updateViewer();
	}

	public Registry getRegistry() {
		return editor.getRegistry();
	}
}
