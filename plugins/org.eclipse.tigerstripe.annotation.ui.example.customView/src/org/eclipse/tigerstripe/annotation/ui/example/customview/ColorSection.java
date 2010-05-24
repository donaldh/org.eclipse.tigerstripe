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
package org.eclipse.tigerstripe.annotation.ui.example.customview;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertiesSection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * @author Yuri Strot
 * 
 */
public class ColorSection extends EPropertiesSection {

	private Composite right;
	private EditorBunch red;
	private EditorBunch green;
	private EditorBunch blue;

	private EObject content;

	private class EditorBunch {

		private Slider slider;
		private Spinner spinner;
		private String feature;

		public EditorBunch(Slider slider, Spinner spinner, String feature) {
			this.slider = slider;
			this.spinner = spinner;
			this.feature = feature;
			init();
		}

		public void setSelection(int selection) {
			if (slider.getSelection() != selection)
				slider.setSelection(selection);
			if (spinner.getSelection() != selection)
				spinner.setSelection(selection);
		}

		public int getSelection() {
			return spinner.getSelection();
		}

		private void init() {
			slider.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					int value = slider.getSelection();
					if (spinner.getSelection() != value)
						spinner.setSelection(value);
					updateColor(feature, value);
					updateColor();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			slider.setMinimum(0);
			slider.setMaximum(255);
			spinner.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					int value = spinner.getSelection();
					if (value != slider.getSelection())
						slider.setSelection(value);
					updateColor(feature, value);
					updateColor();
				}
			});
			spinner.setMinimum(0);
			spinner.setMaximum(255);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls
	 * (org.eclipse.swt.widgets.Composite,
	 * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		parent.setLayout(new GridLayout());
		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage
				.getWidgetFactory();
		Group group = factory.createGroup(parent, "Color");
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite left = factory.createComposite(group);
		left.setLayout(new GridLayout(3, false));
		red = getColorSpinner(left, factory, "red");
		green = getColorSpinner(left, factory, "green");
		blue = getColorSpinner(left, factory, "blue");
		right = factory.createComposite(group, SWT.BORDER);
		updateColor();
	}

	protected EditorBunch getColorSpinner(Composite parent,
			TabbedPropertySheetWidgetFactory factory, final String feature) {
		factory.createLabel(parent, feature.substring(0, 1).toUpperCase()
				+ feature.substring(1));
		return new EditorBunch(new Slider(parent, SWT.NONE), new Spinner(
				parent, SWT.BORDER), feature);
	}

	private void updateColor() {
		Color color = new Color(right.getDisplay(), red.getSelection(), green
				.getSelection(), blue.getSelection());
		right.setBackground(color);
	}

	private void updateColor(String feature, int value) {
		EStructuralFeature efeature = content.eClass().getEStructuralFeature(
				feature);
		Object oldValue = content.eGet(efeature);
		Object newValue = new Integer(value);
		if (oldValue == null || !oldValue.equals(newValue))
			content.eSet(efeature, newValue);
	}

	private void updateColor(EObject content) {
		if (right != null && !right.isDisposed()) {
			org.eclipse.tigerstripe.annotation.ui.example.customview.styles.Color color = (org.eclipse.tigerstripe.annotation.ui.example.customview.styles.Color) content;
			if (color != null) {
				red.setSelection(color.getRed());
				green.setSelection(color.getGreen());
				blue.setSelection(color.getBlue());
				updateColor();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertiesSection
	 * #updateSection(org.eclipse.emf.ecore.EObject, boolean)
	 */
	@Override
	protected void updateSection(EObject content, boolean readOnly) {
		this.content = content;
		updateColor(content);
	}

}
