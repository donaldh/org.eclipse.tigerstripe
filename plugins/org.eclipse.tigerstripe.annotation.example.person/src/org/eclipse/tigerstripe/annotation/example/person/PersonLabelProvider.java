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
package org.eclipse.tigerstripe.annotation.example.person;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Yuri Strot
 *
 */
public class PersonLabelProvider extends LabelProvider {
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		Person person = (Person)element;
		StringBuffer buffer = new StringBuffer("Person");
		Name name = person.getName();
		if (name != null) {
			buffer.append(" (");
			if (name.getFirstName() != null && name.getFirstName().length() > 0)
				buffer.append(name.getFirstName());
			if (name.getLastName() != null && name.getLastName().length() > 0) {
				if (buffer.length() > 8)
					buffer.append(" ");
				buffer.append(name.getLastName());
			}
			buffer.append(")");
		}
		return buffer.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		ImageDescriptor des = PersonExamplePlugin.createImage("icons/person.png");
		return des == null ? null : des.createImage();
	}

}
