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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.util.URI;
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
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class StereotypeNoteProvider implements INoteProvider, IArtifactChangeListener {

	private IStereotypeCapable component;
	private IAbstractArtifactInternal artifact;
	private URI uri;
	private final ListenerList listeners = new ListenerList();

	public StereotypeNoteProvider() {
	}

	public void addListener(INoteListener listener) {
		listeners.add(listener);
	}

	public void removeListener(INoteListener listener) {
		listeners.remove(listener);
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
			component = StereotypeCapableModelHelper.getCapable(element);
		}
		
		if (component == null && part instanceof ArtifactEditorBase) {
			ArtifactEditorBase artifactEditor = (ArtifactEditorBase) part;
			component = artifactEditor.getIArtifact();
		}
		
		if (component != null) {
			artifact = StereotypeCapableModelHelper.getArtifact(component);
			uri = StereotypeCapableModelHelper.getUri(component);
			addListeners();
		}
	}

	private void addListeners() {
		if (artifact != null) {
			artifact.getArtifactManager().addArtifactManagerListener(this);
		}
	}

	private void removeListeners() {
		if (artifact != null) {
			artifact.getArtifactManager().removeArtifactManagerListener(this);
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
	
	public void artifactRemoved(IAbstractArtifact artifact) {
	}

	public void artifactAdded(IAbstractArtifact artifact) {
	}

	public void artifactChanged(IAbstractArtifact changedArtifact,
			IAbstractArtifact oldArtifact) {
		if (uri != null && artifact != null) {
			IResource resource = (IResource) artifact
					.getAdapter(IResource.class);
			IResource changedResource = (IResource) changedArtifact
					.getAdapter(IResource.class);
			if (resource != null && resource.equals(changedResource)) {
				component = StereotypeCapableModelHelper.getCapable(uri);
				fireUpdate();
			}
		}
	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
	}

	public void managerReloaded() {
	}

}
