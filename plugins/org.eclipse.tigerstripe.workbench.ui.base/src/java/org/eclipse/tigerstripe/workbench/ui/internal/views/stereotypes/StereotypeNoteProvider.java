/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.stereotypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.tigerstripe.annotation.ui.core.view.INoteListener;
import org.eclipse.tigerstripe.annotation.ui.core.view.INoteProvider;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.ui.IWorkbenchPart;

public class StereotypeNoteProvider implements INoteProvider {

	public StereotypeNoteProvider() {
	}

	public void addListener(INoteListener listener) {
	}

	public void removeListener(INoteListener listener) {
	}

	public void fillMenu(IMenuManager manager, String groupName, INote note) {
	}

	public INote[] getNotes() {
		if (capable != null) {
			Collection<IStereotypeInstance> instances = capable
					.getStereotypeInstances();
			List<StereotypeNote> notes = new ArrayList<StereotypeNote>(
					instances.size());
			for (IStereotypeInstance instance : instances) {
				notes.add(new StereotypeNote(capable, instance));
			}
			return notes.toArray(new INote[notes.size()]);
		}
		return new INote[0];
	}

	public boolean isNotable() {
		return capable != null;
	}

	public void setSelection(IWorkbenchPart part, ISelection selection) {
		capable = null;
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			capable = getCapable(element);
		}
	}

	private IStereotypeCapable getCapable(Object element) {
		if (element == null)
			return null;
		if (element instanceof IStereotypeCapable) {
			return (IStereotypeCapable) element;
		}
		IStereotypeCapable capable = getCapable(IStereotypeCapable.class,
				element);
		if (capable == null) {
			capable = getCapable(IModelComponent.class, element);
		}
		return capable;
	}

	private static <T extends IStereotypeCapable> IStereotypeCapable getCapable(
			Class<T> clazz, Object element) {
		Object result = null;
		if (element instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) element;
			IStereotypeCapable capable = (IStereotypeCapable) adaptable
					.getAdapter(clazz);
			result = capable;
		}
		if (result == null) {
			result = Platform.getAdapterManager().getAdapter(element, clazz);
		}
		return result == null ? null : clazz.cast(result);
	}

	private IStereotypeCapable capable;

}
