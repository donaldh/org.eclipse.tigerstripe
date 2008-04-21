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
package org.eclipse.tigerstripe.annotation.java.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.OpenAction;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.tigerstripe.annotation.ui.core.ISelectionConverter;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * @author Yuri Strot
 *
 */
public class JavaSelectionConverter implements ISelectionConverter {

	public ISelection convert(IWorkbenchPart part, ISelection selection) {
		if (part instanceof AbstractTextEditor) {
			AbstractTextEditor editor = (AbstractTextEditor)part;
			ISourceViewer sourceViewer = getViewer(editor);
			if (sourceViewer != null) {
				StyledText styledText = sourceViewer.getTextWidget();
				if (styledText != null) {
					int caret = 0;
					if (sourceViewer instanceof ITextViewerExtension5) {
						ITextViewerExtension5 extension = (ITextViewerExtension5)sourceViewer;
						caret = extension.widgetOffset2ModelOffset(styledText.getCaretOffset());
					} else {
						int offset = sourceViewer.getVisibleRegion().getOffset();
						caret = offset + styledText.getCaretOffset();
					}

					IJavaElement element = getElementAt(editor, caret);
					
					if (element != null) {
						return new StructuredSelection(element);
					}
				}
			}
		}
	    return null;
    }
	
	
	public ISourceViewer getViewer(AbstractTextEditor editor) {
		try {
			Method method = AbstractTextEditor.class.getDeclaredMethod("getSourceViewer");
			method.setAccessible(true);
			return (ISourceViewer)method.invoke(editor);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public IJavaElement getElementAt(AbstractTextEditor editor, int caret) {
		IEditorInput input = editor.getEditorInput();
		IClassFile classFile = (IClassFile)input.getAdapter(IClassFile.class);
		if (classFile != null) {
			try {
				return classFile.getElementAt(caret);
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		else {
			IJavaElement javaElement = getEditorJavaElement(input);
			if (javaElement instanceof ICompilationUnit) {
				try {
					return ((ICompilationUnit)javaElement).getElementAt(caret);
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	public static IJavaElement getEditorJavaElement(IEditorInput input) {
		// Performance: check working copy manager first: this is faster
		IJavaElement je = JavaUI.getWorkingCopyManager().getWorkingCopy(input);
		if (je == null) {
			try {
				JavaUI.getWorkingCopyManager().connect(input);
				je = JavaUI.getWorkingCopyManager().getWorkingCopy(input);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		if (je != null)
			return je;
		
		return (IJavaElement)input.getAdapter(IJavaElement.class);
	}

	public void open(ISelection selection) {
		IJavaElement element = getElement(selection);
		if (element != null) {
			switch (element.getElementType()) {
        		case IJavaElement.JAVA_MODEL:
        		case IJavaElement.JAVA_PROJECT:
        		case IJavaElement.PACKAGE_FRAGMENT:
        		case IJavaElement.PACKAGE_FRAGMENT_ROOT:
        			IResource resource = (IResource)Platform.getAdapterManager(
        			     ).getAdapter(element, IResource.class);
        			if (resource != null) {
        				openResource(resource);
            			break;
        			}
        			//if this resource do not open like resource,
        			//try to open it like IJavaElement 
            	default:
        			openSelection(new StructuredSelection(element));
            }
		}
    }
	
	public void openResource(IResource resource) {
		IWorkbenchPage activePage = WorkbenchUtil.getPage();
		if (activePage != null) {
			IViewPart view;
            try {
	            view = activePage.showView(JavaUI.ID_PACKAGES);
				if (view instanceof IPackagesViewPart) {
					((IPackagesViewPart) view).selectAndReveal(resource);
				}
            }
            catch (PartInitException e) {
	            e.printStackTrace();
            }
		}
	}
	
	protected IJavaElement getElement(ISelection selection) {
		if (selection instanceof StructuredSelection) {
			Iterator<?> it = ((StructuredSelection)selection).iterator();
			while (it.hasNext()) {
	            Object elem = (Object) it.next();
	            if (elem instanceof IJavaElement)
	            	return (IJavaElement)elem;
            }
		}
		return null;
	}
	
	protected void openSelection(ISelection selection) {
		IWorkbenchSite site = WorkbenchUtil.getSite();
		if (site != null) {
			IStructuredSelection sel = (IStructuredSelection)selection;
			new OpenAction(site).run(sel);
		}
	}

}
