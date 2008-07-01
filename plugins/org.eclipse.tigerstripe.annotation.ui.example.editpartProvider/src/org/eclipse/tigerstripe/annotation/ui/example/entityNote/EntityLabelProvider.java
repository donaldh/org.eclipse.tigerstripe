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
package org.eclipse.tigerstripe.annotation.ui.example.entityNote;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

/**
 * @author Yuri Strot
 *
 */
public class EntityLabelProvider extends LabelProvider {
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof EntityNote) {
			EntityNote note = (EntityNote)element;
			return "Entity Note: " + note.getDescription();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return Images.get(Images.ENTITY_ICON);
	}

}
