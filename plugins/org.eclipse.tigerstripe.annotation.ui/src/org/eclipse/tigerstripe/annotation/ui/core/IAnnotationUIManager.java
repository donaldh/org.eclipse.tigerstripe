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
package org.eclipse.tigerstripe.annotation.ui.core;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.IRefactoringSupport;
import org.eclipse.ui.ISelectionListener;

/**
 * An annotation UI manager provide operations for annotation creation, removing and changing
 * from the UI. Also it allow to change and listen annotation selection.
 * 
 * @see IRefactoringSupport
 * @see ISelectionConverter
 * @author Yuri Strot
 */
public interface IAnnotationUIManager {
	
	/**
	 * Add annotation selection listener. For getting annotable object from the
	 * provided selection you need to do following:
	 * <pre>
	 *	if (selection instanceof IStructuredSelection) {
	 *		IStructuredSelection sel = (IStructuredSelection)selection;
	 *		Iterator<?> it = sel.iterator();
	 *		while (it.hasNext()) {
	 *			Object element = it.next();
	 *			IAnnotable adaptable = (IAnnotable)Platform.getAdapterManager().getAdapter(element, IAnnotable.class);
	 *			if (adaptable != null)
	 *				return element;
     *		}
     *	}
	 * </pre>
	 * 
	 * @param listener
	 */
	public void addSelectionListener(ISelectionListener listener);
	
	/**
	 * Remove annotation selection listener
	 * 
	 * @param listener
	 */
	public void removeSelectionListener(ISelectionListener listener);
	
	/**
	 * Return current selection
	 * 
	 * @return current selection
	 */
	public ISelection getSelection();
	
	/**
	 * Return label provider for the annotation type
	 * 
	 * @param annotationType annotation type
	 * @return label provider for the annotation type
	 */
	public ILabelProvider getLabelProvider(AnnotationType annotationType);
	
	/**
	 * Open annotable object in the workbench
	 * 
	 * @param object
	 */
	public void open(Object object);
	
	/**
	 * Add selection filter
	 * 
	 * @param filter
	 */
	public void addSelectionFilter(ISelectionFilter filter);
	
	/**
	 * Remove selection filter
	 * 
	 * @param filter
	 */
	public void removeSelectionFilter(ISelectionFilter filter);

}
