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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.internal.help.StereotypeHelpListener;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.FilteredList;

/**
 * @author Eric Dillon
 * 
 *         A bunch of convience methods related to Entity Artifacts
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

	private final Collection<IStereotypeInstance> existingInstances;

	private String title = "Stereotype Selection";

	private String message = "Please select a Stereotype";

	private final IStereotypeCapable component;

	/**
	 * 
	 * @param initialElement
	 * @param model
	 */
	public BrowseForStereotypeDialog(IStereotypeCapable component,
			Collection<IStereotypeInstance> existingInstances) {
		this.existingInstances = existingInstances;
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

		ElementListSelectionDialog elsd = new StereotypeSelectionDialog(
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

	private class StereotypeSelectionDialog extends ElementListSelectionDialog {
		private Composite parentComposite;
		private Text descrText;
		private StereotypeHelpListener helpListener;

		public StereotypeSelectionDialog(Shell parent, ILabelProvider renderer) {
			super(parent, renderer);
		}

		@Override
		protected FilteredList createFilteredList(Composite parent) {
			FilteredList fl = super.createFilteredList(parent);
			createDescriptionArea(parent);
			return fl;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			parentComposite = parent;
			helpListener = new StereotypeHelpListener(null);
			parentComposite.addHelpListener(helpListener);
			return super.createDialogArea(parent);
		}

		@Override
		public boolean close() {
			parentComposite.removeHelpListener(helpListener);
			return super.close();
		}

		protected void createDescriptionArea(Composite composite) {
			Label descrLabel = new Label(composite, SWT.NONE);
			descrLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL));
			descrLabel.setText("Description:");
			descrText = new Text(composite, SWT.WRAP | SWT.MULTI
					| SWT.V_SCROLL | SWT.READ_ONLY);
			descrText.setFont(composite.getFont());
			descrText.setBackground(composite.getBackground());
			GridData data = new GridData(GridData.FILL_BOTH);
			data.widthHint = convertWidthInCharsToPixels(50);
			data.heightHint = convertHeightInCharsToPixels(4);
			descrText.setLayoutData(data);
		}

		@Override
		protected void handleSelectionChanged() {
			super.handleSelectionChanged();
			IStereotype stereotype = null;
			String description = "";
			Object[] elements = getSelectedElements();
			if (elements.length > 0) {
				StringBuilder builder = new StringBuilder();
				stereotype = (IStereotype) elements[0];
				if (stereotype.getDescription() != null) {
					builder.append(stereotype.getDescription());
				}
				builder.append(" Click on Help button for more details.");
				description = builder.toString();
			}
			descrText.setText(description);

			helpListener.setStereotype(stereotype);
		}
	}

	/**
	 * 
	 * @return
	 */
	private Object[] getAvailableStereotypesList() throws TigerstripeException {

		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		List<IStereotype> stereotypes = new ArrayList<IStereotype>();

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

		Collections.sort(stereotypes, new Comparator<IStereotype>() {

			public int compare(IStereotype o1, IStereotype o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});

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
