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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorCreationWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

/**
 * @generated
 */
public class InstanceCreationWizard extends EditorCreationWizard {

	/**
	 * @generated
	 */
	@Override
	public void addPages() {
		super.addPages();
		if (page == null) {
			page = new InstanceCreationWizardPage(getWorkbench(),
					getSelection());
		}
		addPage(page);
	}

	/**
	 * @generated
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle("New Instance Diagram"); //$NON-NLS-1$
		setDefaultPageImageDescriptor(InstanceDiagramEditorPlugin
				.getBundledImageDescriptor("icons/wizban/NewInstancediagramWizard.gif")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}
}
