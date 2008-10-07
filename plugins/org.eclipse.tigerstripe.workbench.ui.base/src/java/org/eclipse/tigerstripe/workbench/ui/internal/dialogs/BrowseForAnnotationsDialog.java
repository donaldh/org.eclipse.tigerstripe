/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @author Eric Dillon
 * 
 * 
 */
public class BrowseForAnnotationsDialog {

	private class AnnotationTypeLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof AnnotationType) {
				ILabelProvider prov = AnnotationUIPlugin.getManager()
						.getLabelProvider((AnnotationType) element);
				if (prov != null)
					return prov.getText(element);
				else
					return ((AnnotationType) element).getName();
			}

			return null;
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof AnnotationType) {
				ILabelProvider prov = AnnotationUIPlugin.getManager()
						.getLabelProvider((AnnotationType) element);
				if (prov != null)
					return prov.getImage(element);
			}

			return null;
		}

	}

	private Collection<AnnotationType> existingAnnotationTypes;

	private String title = "Annotation Type Selection";

	private String message = "Please select a Annotation Type";

	/**
	 * 
	 * @param initialElement
	 * @param model
	 */
	public BrowseForAnnotationsDialog(
			Collection<AnnotationType> existingInstances) {
		this.existingAnnotationTypes = existingInstances;
	}

	/**
	 * Opens up a dialog box with a list of available entities and returns the
	 * EntityOptions that have been selected.
	 * 
	 * @return EntityOption[] - Returns an array of EntityOption as selected
	 *         from the dialog
	 */
	public AnnotationType[] browseAvailableAnnotationTypes(Shell parentShell)
			throws TigerstripeException {

		ElementListSelectionDialog elsd = new ElementListSelectionDialog(
				parentShell, new AnnotationTypeLabelProvider());

		elsd.setTitle(getTitle());
		elsd.setMessage(getMessage());

		Object[] availableAnnotationTypes = getAvailableAnnotationTypesList();
		elsd.setElements(availableAnnotationTypes);

		if (elsd.open() == Window.OK) {

			Object[] objects = elsd.getResult();
			if (objects != null && objects.length != 0) {
				AnnotationType[] result = new AnnotationType[objects.length];
				for (int i = 0; i < result.length; i++) {
					result[i] = (AnnotationType) objects[i];
				}

				return result;
			}
		}
		return new AnnotationType[0];
	}

	/**
	 * 
	 * @return
	 */
	private Object[] getAvailableAnnotationTypesList()
			throws TigerstripeException {

		List<AnnotationType> annotationTypes = new ArrayList<AnnotationType>();

		annotationTypes.addAll(Arrays.asList(AnnotationPlugin.getManager()
				.getTypes()));

		if (annotationTypes.size() == 0)
			return new Object[0];

		// now go thru the list and remove those that can't be re added
		for (AnnotationType instance : existingAnnotationTypes) {
			int target = -1;
			for (int i = 0; i < annotationTypes.size(); i++) {
				if (annotationTypes.get(i).getId().equals(instance.getId())) {
					target = i;
					break;
				}
			}
			if (target != -1)
				annotationTypes.remove(target);
		}

		Collections.sort(annotationTypes, new Comparator<AnnotationType>() {

			public int compare(AnnotationType o1, AnnotationType o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});

		return annotationTypes.toArray();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
