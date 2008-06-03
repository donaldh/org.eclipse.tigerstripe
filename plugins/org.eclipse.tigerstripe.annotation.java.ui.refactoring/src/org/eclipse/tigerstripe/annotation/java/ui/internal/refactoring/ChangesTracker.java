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
package org.eclipse.tigerstripe.annotation.java.ui.internal.refactoring;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.refactoring.descriptors.MoveDescriptor;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.history.IRefactoringExecutionListener;
import org.eclipse.ltk.core.refactoring.history.RefactoringExecutionEvent;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IRefactoringSupport;
import org.eclipse.tigerstripe.annotation.java.JavaURIConverter;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.IElementChanges;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.JavaChanges;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.JavaElementTree;
import org.eclipse.tigerstripe.annotation.resource.ResourceURIConverter;

/**
 * @author Yuri Strot
 *
 */
public class ChangesTracker {
	
	private RefactoringUtil.RenameJavaResult renameResult;
	private IElementChanges[] changes;
	
	private ListenerList listeners = new ListenerList();
	
	private boolean blockDeletion = false;
	
	private ChangesTracker() {
		startChangesTracking();
	}
	
	private static ChangesTracker tracker;
	
	public static ChangesTracker getInstance() {
		if (tracker == null)
			tracker = new ChangesTracker();
		return tracker;
	}
	
	public void addListener(IRefactoringChangesListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IRefactoringChangesListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireDeleted(ILazyObject path) {
		for (Object object : listeners.getListeners()) {
			IRefactoringChangesListener listener = (IRefactoringChangesListener)object;
			listener.deleted(path);
		}
	}
	
	protected void fireChanged(ILazyObject oldPath, ILazyObject newPath, int kind) {
		for (Object object : listeners.getListeners()) {
			IRefactoringChangesListener listener = (IRefactoringChangesListener)object;
			listener.changed(oldPath, newPath, kind);
		}
	}
	
	protected void startChangesTracking() {
		RefactoringCore.getHistoryService().addExecutionListener(new IRefactoringExecutionListener() {
			
			public void executionNotification(RefactoringExecutionEvent event) {
				RefactoringDescriptor des = event.getDescriptor().requestDescriptor(new NullProgressMonitor());
				if (des instanceof RenameJavaElementDescriptor) {
					processRename((RenameJavaElementDescriptor)des, event.getEventType());
				}
				if ("org.eclipse.jdt.ui.rename.resource".equals(des.getID())) {
					processResourceRename(des, event.getEventType());
				}
				if (des instanceof MoveDescriptor || "org.eclipse.jdt.ui.move".equals(des.getID())) {
					processMove(des, event.getEventType());
				}
			}
		
		});
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			
			public void resourceChanged(IResourceChangeEvent event) {
				try {
					event.getDelta().accept(new IResourceDeltaVisitor() {
						
						public boolean visit(IResourceDelta delta) throws CoreException {
							switch (delta.getKind()) {
								case IResourceDelta.ADDED:
									return false;
								case IResourceDelta.CHANGED:
									return true;
								case IResourceDelta.REMOVED:
									deleted(delta.getResource());
									return true;
								default:
									return true;
							}
						}
					});
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		
		});
	}
	
	protected void deleted(IResource resource) {
		if (!blockDeletion)
			fireDeleted(new LazyObject(resource));
	}
	
	
	protected void processRename(RenameJavaElementDescriptor des, int eventType) {
		if (eventType == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			blockDeletion = true;
			renameResult = RefactoringUtil.getElement(des);
			changes = new IElementChanges[] { new JavaChanges(
					renameResult.getElement()) };
		}
		else if ((eventType == RefactoringExecutionEvent.PERFORMED)){
			blockDeletion = false;
			if (renameResult.isTypeParameter()) {
				//TODO need to implement
			}
			else if (changes != null && changes.length == 1) {
				refactoringPerformed(changes[0].getChanges(getNewElement()));
			}
			renameResult = null;
			changes = null;
		}
	}
	
	protected void refactoringPerformed(Map<URI, URI> uris) {
		IRefactoringSupport rs = AnnotationPlugin.getManager().getRefactoringSupport();
		for (URI newUri : uris.keySet()) {
			rs.changed(newUri, uris.get(newUri));
		}
	}
	
	public void processResourceRename(RefactoringDescriptor rrd, int eventType) {
		IPath path = RefactoringUtil.getResourcePath(rrd);
		if (path == null)
			return;
		IPath newPath = RefactoringUtil.getNewPath(path, rrd);
		if (newPath == null)
			return;
		int kind = -1;
		switch (eventType) {
			case RefactoringExecutionEvent.ABOUT_TO_PERFORM:
				kind = IRefactoringChangesListener.ABOUT_TO_CHANGE;
				blockDeletion = true;
				break;
			case RefactoringExecutionEvent.PERFORMED:
				kind = IRefactoringChangesListener.CHANGED;
				blockDeletion = false;
				break;
			default:
				break;
		}
		if (kind != -1)
			fireChanged(new ResourceLazyObject(path), new ResourceLazyObject(newPath), kind);
	}
	
	public void processMove(RefactoringDescriptor des, int eventType) {
		if (eventType == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			blockDeletion = true;
			changes = RefactoringUtil.getResources(des);
		}
		else if (eventType == RefactoringExecutionEvent.PERFORMED){
			blockDeletion = false;
			IResource destination = RefactoringUtil.getDestination(des);
			if (destination != null) {
				Map<URI, URI> allChanges = new HashMap<URI, URI>();
				for (int i = 0; i < changes.length; i++) {
					IPath oldPath = changes[i].getPath();
					IPath newPath = destination.getFullPath().append(oldPath.lastSegment());
					IResource newResource = ResourcesPlugin.getWorkspace().getRoot().findMember(newPath);
					allChanges.putAll(changes[i].getChanges(newResource));
                }
				refactoringPerformed(allChanges);
			}
			changes = null;
		}
	}
	
	protected IJavaElement getNewElement() {
		URI newUri = JavaURIConverter.toURI(renameResult.getElement(), renameResult.getName());
		if (newUri != null)
			return JavaURIConverter.toJava(newUri);
		return null;
	}
	
	protected static void collectChanges(IJavaElement element, JavaElementTree tree, Map<URI, URI> changes) {
		URI newUri = JavaURIConverter.toURI(element);
		changes.put(tree.getElement(), newUri);
		if (element instanceof IParent) {
			IJavaElement[] children;
            try {
	            children = ((IParent)element).getChildren();
	            JavaElementTree[] elements = tree.getChildren().toArray(new JavaElementTree[tree.getChildren().size()]);
	            if (elements.length != children.length) {
	            	throw new IllegalArgumentException("Old and New structure should be the same!");
	            }
				for (int i = 0; i < children.length; i++) {
					IJavaElement child = children[i];
					URI childUri = JavaURIConverter.toURI(child);
					if (childUri == null)
						continue;
	            	collectChanges(child, elements[i], changes);
	            }
            }
            catch (JavaModelException e) {
	            e.printStackTrace();
            }
		}
	}
	
	protected static void collectChanges(IResource resource, IPath oldPath, Map<URI, URI> changes) {
		changes.put(ResourceURIConverter.toURI(oldPath), ResourceURIConverter.toURI(resource));
		if (resource instanceof IContainer) {
			IContainer container = (IContainer)resource;
			try {
				//TODO how we going on to work with phantom and team private resources?
	            IResource[] members = container.members(0);
	            for (int i = 0; i < members.length; i++) {
	            	IResource child = members[i];
	            	IPath childOldPath = oldPath.append(child.getFullPath().lastSegment());
	            	collectChanges(child, childOldPath, changes);
                }
            }
            catch (CoreException e) {
	            e.printStackTrace();
            }
		}
	}

}
