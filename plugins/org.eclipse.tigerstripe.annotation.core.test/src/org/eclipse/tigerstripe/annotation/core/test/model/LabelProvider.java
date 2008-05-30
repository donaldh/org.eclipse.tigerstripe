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
package org.eclipse.tigerstripe.annotation.core.test.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.core.test.Activator;

/**
 * @author Yuri Strot
 *
 */
public class LabelProvider extends org.eclipse.jface.viewers.LabelProvider {
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		ImageDescriptor des = getDescriptor(element);
		if (des != null)
			return des.createImage();
		return super.getImage(element);
	}
	
	protected ImageDescriptor getDescriptor(Object element) {
		if (element instanceof MimeType)
			return Activator.createImage("icons/mimetype.gif");
		if (element instanceof Author)
			return Activator.createImage("icons/author.gif");
		if (element instanceof Hibernate) {
			return Activator.createImage("icons/hibernate.gif");
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof MimeType) {
			MimeType type = (MimeType)element;
			String s = "MIME Type";
			if (type.getMimeType() != null)
				s += " (" + type.getMimeType() + ")";
			return s;
		}
		if (element instanceof Hibernate) {
			Hibernate hibernate = (Hibernate)element;
			return "Hibernate (persistence=" + hibernate.isPersistance() + ")";
		}
		if (element instanceof Author) {
			Author author = (Author)element;
			String details = "";
			if (author.getFirstName() != null) {
				details += author.getFirstName() + " ";
			}
			if (author.getLastName() != null) {
				details += author.getLastName() + " ";
			}
			details = details.trim();
			if (details.length() == 0)
				return "Author";
			else
				return "Author (" + details + ")";
		}
		return super.getText(element);
	}

}
