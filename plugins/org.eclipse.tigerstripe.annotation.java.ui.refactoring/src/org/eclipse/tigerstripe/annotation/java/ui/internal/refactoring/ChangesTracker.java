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
import org.eclipse.ltk.core.refactoring.resource.MoveResourcesDescriptor;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceDescriptor;
import org.eclipse.tigerstripe.annotation.java.JavaURIConverter;
import org.eclipse.tigerstripe.annotation.java.ui.internal.refactoring.RefactoringUtil.RenameJavaResult;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.ILazyObject;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.IRefactoringChangesListener;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.JavaElementTree;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.JavaRefactoringSupport;
import org.eclipse.tigerstripe.annotation.java.ui.refactoring.ResourceRefactoringSupport;
import org.eclipse.tigerstripe.annotation.resource.ResourceURIConverter;

/**
 * @author Yuri Strot
 *
 */
public class ChangesTracker {
	
	private ILazyObject lazyObject; 
	
	private ListenerList listeners = new ListenerList();
	
	private boolean blockDeletion = false;
	
	private ChangesTracker() {
		startChangesTracking();
	}
	
	private static ChangesTracker tracker;
	
	public static void initialize() {
		if (tracker == null)
			tracker = new ChangesTracker();
	}
	
	protected void fireDeleted(final ILazyObject path) {
		//do not block workspace
		new Thread() {
			public void run() {
				for (Object object : listeners.getListeners()) {
					IRefactoringChangesListener listener = (IRefactoringChangesListener)object;
					listener.deleted(path);
				}
			}
		}.start();
	}
	
	protected void fireChanged(final ILazyObject oldPath, final ILazyObject newPath, final int kind) {
		//do not block workspace
		new Thread() {
			public void run() {
				for (Object object : listeners.getListeners()) {
					IRefactoringChangesListener listener = (IRefactoringChangesListener)object;
					listener.changed(oldPath, newPath, kind);
				}
			}
		}.start();
	}
	
	protected void fireMoved(final ILazyObject[] objects, final ILazyObject destination, final int kind) {
		//do not block workspace
		new Thread() {
			public void run() {
				for (Object object : listeners.getListeners()) {
					IRefactoringChangesListener listener = (IRefactoringChangesListener)object;
					listener.moved(objects, destination, kind);
				}
			}
		}.start();
	}
	
	protected void initializeListeners() {
		listeners.add(new JavaRefactoringSupport());
		listeners.add(new ResourceRefactoringSupport());
	}
	
	protected void startChangesTracking() {
		initializeListeners();
		RefactoringCore.getHistoryService().addExecutionListener(new IRefactoringExecutionListener() {
			
			public void executionNotification(RefactoringExecutionEvent event) {
				RefactoringDescriptor des = event.getDescriptor().requestDescriptor(new NullProgressMonitor());
				if (des instanceof RenameJavaElementDescriptor) {
					processRename((RenameJavaElementDescriptor)des, event.getEventType());
				}
				if (des instanceof MoveDescriptor) {
					processMove(des, event.getEventType());
				}
				if (des instanceof RenameResourceDescriptor) {
					processRename((RenameResourceDescriptor)des, event.getEventType());
				}
				if (des instanceof MoveResourcesDescriptor) {
					processMove((MoveResourcesDescriptor)des, event.getEventType());
				}
			}
		
		});
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			
			public void resourceChanged(IResourceChangeEvent event) {
				try {
					if (event != null && event.getDelta() != null) {
						event.getDelta().accept(new IResourceDeltaVisitor() {
							
							public boolean visit(IResourceDelta delta) throws CoreException {
								switch (delta.getKind()) {
									case IResourceDelta.ADDED:
										return false;
									case IResourceDelta.CHANGED:
										return true;
									case IResourceDelta.REMOVED:
										deleted(delta.getResource());
										return false;
									default:
										return true;
								}
							}
						});
					}
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
			RenameJavaResult result = RefactoringUtil.getElement(des);
			IJavaElement jElement = (IJavaElement)result.getElement().getObject();
			lazyObject = new JavaLazyObject(jElement, result.getName());
			if (result == null || result.isTypeParameter())
				return;
			blockDeletion = true;
			fireChanged(result.getElement(), lazyObject, IRefactoringChangesListener.ABOUT_TO_CHANGE);
		}
		else if ((eventType == RefactoringExecutionEvent.PERFORMED)){
			blockDeletion = false;
			RenameJavaResult result = RefactoringUtil.getElement(des);
			fireChanged(result.getElement(), lazyObject, IRefactoringChangesListener.CHANGED);
		}
	}
	
	public void processRename(RenameResourceDescriptor rrd, int eventType) {
		IPath path = RefactoringUtil.getResourcePath(rrd);
		if (path == null)
			return;
		IPath newPath = RefactoringUtil.getNewPath(path, rrd);
		if (newPath == null)
			return;
		if (eventType == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			blockDeletion = true;
			fireChanged(new ResourceLazyObject(path), new ResourceLazyObject(newPath),
					IRefactoringChangesListener.ABOUT_TO_CHANGE);
		}
		else if (eventType == RefactoringExecutionEvent.PERFORMED) {
			blockDeletion = false;
			fireChanged(new ResourceLazyObject(path), new ResourceLazyObject(newPath),
					IRefactoringChangesListener.CHANGED);
		}
	}
	
	public void processMove(MoveResourcesDescriptor des, int eventType) {
		ILazyObject[] objects = RefactoringUtil.getResourcesPath(des);
		ILazyObject destination = RefactoringUtil.getDestination(des);
		if (eventType == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			blockDeletion = true;
			fireMoved(objects, destination, IRefactoringChangesListener.ABOUT_TO_CHANGE);
		}
		else if (eventType == RefactoringExecutionEvent.PERFORMED){
			blockDeletion = false;
			fireMoved(objects, destination, IRefactoringChangesListener.CHANGED);
		}
	}
	
	public void processMove(RefactoringDescriptor des, int eventType) {
		ILazyObject[] objects = RefactoringUtil.getResources(des);
		ILazyObject destination = RefactoringUtil.getDestination(des);
		if (eventType == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			blockDeletion = true;
			fireMoved(objects, destination, IRefactoringChangesListener.ABOUT_TO_CHANGE);
		}
		else if (eventType == RefactoringExecutionEvent.PERFORMED){
			blockDeletion = false;
			fireMoved(objects, destination, IRefactoringChangesListener.CHANGED);
		}
	}
	
	private static class JavaLazyObject implements ILazyObject {
		
		private IJavaElement element;
		private String name;
		
		public JavaLazyObject(IJavaElement element, String name) {
			this.element = element;
			this.name = name;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.tigerstripe.annotation.java.ui.refactoring.ILazyObject#getObject()
		 */
		public Object getObject() {
			URI newUri = JavaURIConverter.toURI(element, name);
			if (newUri != null)
				return JavaURIConverter.toJava(newUri);
			return null;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			JavaLazyObject jlo = (JavaLazyObject)obj;
			return jlo.element.equals(element) && jlo.name.equals(name);
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return element.hashCode() >> 16 ^ name.hashCode();
		}
		
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
