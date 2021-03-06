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
package org.eclipse.tigerstripe.annotation.ui.core.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.IRefactoringListener;
import org.eclipse.tigerstripe.annotation.core.refactoring.RefactoringChange;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.Images;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.OpenAnnotationWizardAction;
import org.eclipse.tigerstripe.annotation.ui.internal.core.AnnotationUIManager;
import org.eclipse.tigerstripe.annotation.ui.internal.util.AnnotationSelectionUtils;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class AnnotationNoteProvider implements INoteProvider,
		IRefactoringListener, IAnnotationListener, IResourceChangeListener {

	private final ListenerList listeners = new ListenerList();

	public void addListener(INoteListener listener) {
		assert listener != null;
		if (listeners.size() == 0) {
			addListeners();
		}
		listeners.add(listener);
	}

	public void removeListener(INoteListener listener) {
		assert listener != null;
		listeners.remove(listener);
		if (listeners.size() == 0) {
			removeListeners();
		}
	}

	public INote[] getNotes() {
		return notes;
	}

	public boolean isNotable() {
		return annotable != null;
	}

	// ////////////////////
	//
	// Listeners
	//
	// ////////////////////
	public void setSelection(IWorkbenchPart part, ISelection sel) {
		selection = AnnotationUIManager.getInstance().convertSelection(part,
				sel);
		updateSelection();
	}

	public void annotationAdded(Annotation annotation) {
		fireUpdateAsync();
	}

	public void annotationChanged(Annotation annotations) {
		fireUpdateAsync();
	}

	public void annotationRemoved(Annotation annotation) {
		for (INote note : notes) {
			AnnotationNote annNote = (AnnotationNote) note;
			if (annNote.getAnnotation().equals(annotation)) {
				annNote.setValid(false);
				break;
			}
		}
		fireUpdateAsync();
	}

	public void refactoringPerformed(RefactoringChange change) {
		fireUpdate();
	}

	public void fillMenu(IMenuManager manager, String groupName, INote note) {
		if (annotable != null) {
			IAction action = new OpenAnnotationWizardAction(annotable,
					"Add Annotation", getImageDescriptor());
			ActionContributionItem item = new ActionContributionItem(action);
			manager.appendToGroup(groupName, item);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org
	 * .eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		final boolean[] updateSelection = new boolean[1];
		if (delta != null && resources.size() > 0) {
			try {
				delta.accept(new IResourceDeltaVisitor() {
					public boolean visit(IResourceDelta delta)
							throws CoreException {
						if ((delta.getFlags() & IResourceDelta.OPEN) > 0) {
							IResource res = delta.getResource();
							if (res != null && resources.contains(res)) {
								updateSelection[0] = true;
							}
						}
						return !updateSelection[0];
					}
				});
			} catch (CoreException e) {
				AnnotationUIPlugin.log(e);
			}
		}
		if (updateSelection[0]) {
			fireUpdate();
		}
	}

	private void fireUpdateAsync() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			
			public void run() {
				fireUpdate();
			}
		});
	}
	
	private void fireUpdate() {
		updateSelection();
		for (INoteListener listener : getListeners()) {
			listener.notesChanged(notes);
		}
	}

	private void updateSelection() {
		updateResources();
		Object annotable = AnnotationSelectionUtils
				.getAnnotableElement(selection);
		if (annotable == null) {
			setNotes(null, null);
		} else {
			Set<Annotation> annotations = new LinkedHashSet<Annotation>();
			Annotation annotation = AnnotationSelectionUtils
					.getAnnotation(annotable);
			if (annotation != null) {
				annotable = AnnotationPlugin.getManager().getAnnotatedObject(
						annotation);
				annotations.add(annotation);
				setNotes(annotable, annotations);
			} else {
				if (AnnotationUtils.collectAllAnnotations(annotable, annotations)) {
					setNotes(annotable, annotations);
				} else {
					setNotes(null, null);
				}
			}
		}
	}

	private void updateResources() {
		resources = new ArrayList<IResource>();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) selection;
			Iterator<?> it = sel.iterator();
			while (it.hasNext()) {
				Object object = it.next();
				IResource res = (IResource) Platform.getAdapterManager()
						.getAdapter(object, IResource.class);
				if (res != null) {
					resources.add(res);
				}
			}
		}
	}

	private void setNotes(Object annotable, Collection<Annotation> annotations) {
		this.annotable = annotable;
		int size;
		if (annotations == null || (size = annotations.size()) == 0) {
			notes = INote.EMPTY;
		} else {
			Annotation[] asArray = annotations.toArray(new Annotation[size]);
			notes = new INote[size];
			for (int i = 0; i < size; i++) {
				notes[i] = new AnnotationNote(asArray[i]);
			}
		}
	}

	private INoteListener[] getListeners() {
		Object[] objects = listeners.getListeners();
		INoteListener[] result = new INoteListener[objects.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = (INoteListener) objects[i];
		}
		return result;
	}

	private void addListeners() {
		AnnotationPlugin.getManager().addRefactoringListener(this);
		AnnotationPlugin.getManager().addAnnotationListener(this);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	private void removeListeners() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		AnnotationPlugin.getManager().removeAnnotationListener(this);
		AnnotationPlugin.getManager().removeRefactoringListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.ui.core.view.INoteProvider#getImage()
	 */
	public ImageDescriptor getImageDescriptor() {
		return Images.getDescriptor(Images.ANNOTATION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.ui.core.view.INoteProvider#getLabel()
	 */
	public String getLabel() {
		return "Annotations"; 
	}

	private ISelection selection;
	private List<IResource> resources = new ArrayList<IResource>();
	private Object annotable;
	private INote[] notes = INote.EMPTY;
}
