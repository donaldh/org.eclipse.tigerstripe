/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.samples.eclipseSummit08.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.samples.eclipseSummit08.Activator;
import org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.Note;
import org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO;
import org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.documentation.Documentation;

public class LabelProvider extends org.eclipse.jface.viewers.LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
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
		if (element instanceof Documentation)
			return Activator.createImage("icons/documentation.png");
		if (element instanceof TODO)
			return Activator.createImage("icons/todo.png");
		if (element instanceof Note) {
			return Activator.createImage("icons/designNote.png");
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Documentation) {
			Documentation doc = (Documentation) element;
			if (doc.getAuthor() != null && doc.getAuthor().length() != 0) {
				return "Documentation (" + doc.getAuthor() + ")";
			}
			return "Documentation";
		} else if (element instanceof Note) {
			Note note = (Note) element;
			String text = note.getText();

			if (text == null)
				return "Note";

			if (text.length() < 6) {
				return "Note: " + text;
			} else {
				return "Note: " + text.substring(0, 5) + "...";
			}
		} else if (element instanceof TODO) {
			TODO todo = (TODO) element;
			String text = todo.getSummary();

			if (text == null)
				return "TODO";

			if (text.length() < 6) {
				return "TODO: " + text;
			} else {
				return "TODO: " + text.substring(0, 5) + "...";
			}
		}
		return super.getText(element);
	}

}
