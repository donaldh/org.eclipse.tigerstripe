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
package org.eclipse.tigerstripe.annotation.java.ui.refactoring;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
import org.eclipse.tigerstripe.annotation.core.RefactoringSupport;
import org.eclipse.tigerstripe.annotation.java.JavaURIConverter;
import org.eclipse.tigerstripe.annotation.resource.ResourceURIConverter;

/**
 * @author Yuri Strot
 *
 */
public class JavaRefactoringSupport extends RefactoringSupport {
	
	private RefactoringUtil.RenameJavaResult renameResult;
	private IElementChanges[] changes;
	private IResource resource;
	
	public JavaRefactoringSupport() {
		addListeners();
	}
	
	protected void addListeners() {
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
	}
	
	protected void processRename(RenameJavaElementDescriptor des, int eventType) {
		if (eventType == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			renameResult = RefactoringUtil.getElement(des);
			changes = new IElementChanges[] { new JavaChanges(
					renameResult.getElement()) };
		}
		else if ((eventType == RefactoringExecutionEvent.PERFORMED) && renameResult != null){
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
	
	public void processResourceRename(RefactoringDescriptor rrd, int eventType) {
		if (eventType == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			resource = RefactoringUtil.getResource(rrd);
		}
		else if (eventType == RefactoringExecutionEvent.PERFORMED && resource != null){
			IPath newPath = RefactoringUtil.getNewPath(resource, rrd);
			if (newPath != null) {
				Map<URI, URI> changes = new HashMap<URI, URI>();
				IResource newResource = ResourcesPlugin.getWorkspace().getRoot().findMember(newPath);
				collectChanges(newResource, resource.getFullPath(), changes);
				refactoringPerformed(changes);
			}
			resource = null;
		}
	}
	
	public void processMove(RefactoringDescriptor des, int eventType) {
		if (eventType == RefactoringExecutionEvent.ABOUT_TO_PERFORM) {
			changes = RefactoringUtil.getResources(des);
		}
		else if (eventType == RefactoringExecutionEvent.PERFORMED && changes != null){
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
