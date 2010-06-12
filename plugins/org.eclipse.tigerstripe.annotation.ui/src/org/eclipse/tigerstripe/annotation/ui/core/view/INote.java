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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;

public interface INote {

	public static INote[] EMPTY = new INote[0];

	public void addChangeListener(INoteChangeListener listener);

	public void removeChangeListener(INoteChangeListener listener);

	public INoteChangeListener[] getListeners();

	public void save() throws CoreException;

	public void revert() throws CoreException;

	public void remove();

	public EObject getContent();

	public String getLabel();

	public Image getImage();

	public String getDescription();

	public boolean isReadOnly();

}
