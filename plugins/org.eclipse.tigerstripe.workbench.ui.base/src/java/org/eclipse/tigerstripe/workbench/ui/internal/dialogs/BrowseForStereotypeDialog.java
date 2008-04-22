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
import java.util.Collection;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @author Eric Dillon
 * 
 * A bunch of convience methods related to Entity Artifacts
 * 
 */
public class BrowseForStereotypeDialog {

	private class StereotypeLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof IStereotype) {
				IStereotype st = (IStereotype) element;
				return st.getName();
			} else
				return "<unkwown>";
		}
	}

	private Collection<IStereotypeInstance> existingInstances;

	private String title = "Stereotype Selection";

	private String message = "Please select a Stereotype";

	private IStereotypeCapable component;

	/**
	 * 
	 * @param initialElement
	 * @param model
	 */
	public BrowseForStereotypeDialog(IStereotypeCapable component,
			Collection<IStereotypeInstance> existingInstances) {
		this.existingInstances =  existingInstances;
		this.component = component;
	}

	/**
	 * Opens up a dialog box with a list of available entities and returns the
	 * EntityOptions that have been selected.
	 * 
	 * @return EntityOption[] - Returns an array of EntityOption as selected
	 *         from the dialog
	 */
	public IStereotype[] browseAvailableStereotypes(Shell parentShell)
			throws TigerstripeException {

		ElementListSelectionDialog elsd = new ElementListSelectionDialog(
				parentShell, new StereotypeLabelProvider());

		elsd.setTitle(getTitle());
		elsd.setMessage(getMessage());

		Object[] availableStereotypes = getAvailableStereotypesList();
		elsd.setElements(availableStereotypes);

		if (elsd.open() == Window.OK) {

			Object[] objects = elsd.getResult();
			if (objects != null && objects.length != 0) {
				IStereotype[] result = new IStereotype[objects.length];
				for (int i = 0; i < result.length; i++) {
					result[i] = (IStereotype) objects[i];
				}

				return result;
			}
		}
		return new IStereotype[0];
	}

	/**
	 * 
	 * @return
	 */
	private Object[] getAvailableStereotypesList() throws TigerstripeException {

		IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession()
				.getActiveProfile();
		Collection<IStereotype> stereotypes = new ArrayList<IStereotype>();

		if (component != null) {
			stereotypes.addAll(profile
					.getAvailableStereotypeForCapable(component));
		} else {
			stereotypes.addAll(profile.getStereotypes());
		}
		if (stereotypes.size() == 0)
			return new Object[0];

		// now go thru the list and remove those that can't be re added
		for (IStereotypeInstance instance : existingInstances) {
			IStereotype st = instance.getCharacterizingStereotype();
			if (stereotypes.contains(st)) {
				stereotypes.remove(st);
			}
		}

		return stereotypes.toArray();
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
