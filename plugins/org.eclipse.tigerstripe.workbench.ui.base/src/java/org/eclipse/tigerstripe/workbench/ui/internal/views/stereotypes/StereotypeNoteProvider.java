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
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.tigerstripe.annotation.ui.core.view.INoteListener;
import org.eclipse.tigerstripe.annotation.ui.core.view.INoteProvider;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeListener;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class StereotypeNoteProvider implements INoteProvider,
		IStereotypeListener {

	public StereotypeNoteProvider() {
	}

	public void addListener(INoteListener listener) {
		listeners.add(listener);
		if (listeners.size() > 0) {
			addListeners();
		}
	}

	public void removeListener(INoteListener listener) {
		listeners.remove(listener);
		if (listeners.size() == 0) {
			removeListeners();
		}
	}

	public void stereotypeAdded(IStereotypeInstance instance) {
		fireUpdate();
	}

	public void stereotypeRemove(IStereotypeInstance instance) {
		fireUpdate();
	}

	public void fillMenu(IMenuManager manager, String groupName, INote note) {
		if (component != null) {
			Shell shell = PlatformUI.getWorkbench().getDisplay()
					.getActiveShell();
			IAction action = new AddStereotypeAction(component, shell,
					getImageDescriptor());
			if (StereotypeNote.isReadOnly(component)) {
				action.setEnabled(false);
			}
			ActionContributionItem item = new ActionContributionItem(action);
			manager.appendToGroup(groupName, item);
		}
	}

	public INote[] getNotes() {
		if (component != null) {
			Collection<IStereotypeInstance> instances = component
					.getStereotypeInstances();
			List<StereotypeNote> notes = new ArrayList<StereotypeNote>(
					instances.size());
			for (IStereotypeInstance instance : instances) {
				notes.add(new StereotypeNote(component, instance));
			}
			return notes.toArray(new INote[notes.size()]);
		}
		return INote.EMPTY;
	}

	public boolean isNotable() {
		return component != null;
	}

	public void setSelection(IWorkbenchPart part, ISelection selection) {
		removeListeners();
		component = null;
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			component = getCapable(element);
		}
		
		if (component == null && part instanceof ArtifactEditorBase) {
			ArtifactEditorBase artifactEditor = (ArtifactEditorBase) part;
			component = artifactEditor.getIArtifact();
		}
		
		if (component != null) {
			if (listeners.size() > 0) {
				addListeners();
			}
		}
	}

	private void addListeners() {
		if (component != null) {
			component.addStereotypeListener(this);
		}
	}

	private void removeListeners() {
		if (component != null) {
			component.removeStereotypeListener(this);
		}
	}

	private void fireUpdate() {
		Object[] objects = listeners.getListeners();
		if (objects.length > 0) {
			INote[] notes = getNotes();
			for (Object object : objects) {
				INoteListener listener = (INoteListener) object;
				listener.notesChanged(notes);
			}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.ui.core.view.INoteProvider#getImage()
	 */
	public ImageDescriptor getImageDescriptor() {
		return Images.getDescriptor(Images.STEREOTYPE_ICON);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.ui.core.view.INoteProvider#getLabel()
	 */
	public String getLabel() {
		return "Stereotypes";
	}

	private IStereotypeCapable component;
	private final ListenerList listeners = new ListenerList();

}
