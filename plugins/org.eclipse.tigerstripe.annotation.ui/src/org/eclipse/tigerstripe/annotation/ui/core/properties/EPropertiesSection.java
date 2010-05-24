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
package org.eclipse.tigerstripe.annotation.ui.core.properties;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;

/**
 * Base class for EMF properties section
 * 
 * @author Yuri Strot
 */
public class EPropertiesSection extends AbstractPropertySection {

	/**
	 * Update section
	 * 
	 * @param content
	 * @param readOnly
	 */
	protected void updateSection(EObject content, boolean readOnly) {
	}

	@Override
	public boolean shouldUseExtraSpace() {
		return true;
	}

	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		updateSection(selection);
	}

	protected void updateSection(final ISelection selection) {
		INote note = getNote(selection);
		if (note != null) {
			updateSection(note.getContent(), note.isReadOnly());
		}
	}

	private INote getNote(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			if (element instanceof INote) {
				return (INote) element;
			}
		}
		return null;
	}

}
