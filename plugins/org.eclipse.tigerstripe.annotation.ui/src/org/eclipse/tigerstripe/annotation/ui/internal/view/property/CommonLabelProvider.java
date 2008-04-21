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
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.ui.PlatformUI;


/**
 * Label provider that delegates to the Icon Service
 * and the Parser Service for the images and text it provides.
 *
 */
public class CommonLabelProvider
	extends DecoratingLabelProvider {
	
	/**
	 * A label provider which uses the icon and parser service to
	 * get the labels.
	 */
	private static class MyDelegatingLabelProvider extends LabelProvider {

		public Image getImage(Object element) {
			return null;
		}

		public String getText(Object element) {
			if (element instanceof IStructuredSelection) {
				Annotation annotation = (Annotation)((IStructuredSelection)element).getFirstElement();
				return annotation.getUri().toString();
			}
			return "";
		}
	};
	
	/**
	 * Constructors a new label provider instance.
	 */
	public CommonLabelProvider() {
		super(
			new MyDelegatingLabelProvider(),
			PlatformUI
				.getWorkbench()
				.getDecoratorManager()
				.getLabelDecorator());
	}
	
	/**
	 * Sets the parser options.
	 * @param options parser options
	 */
	public void setParserOptions(int options) {
	}
	
	/**
	 * Sets the icon options.
	 * @param options icon options
	 */
	public void setIconOptions(int options) {
	}
	
}
