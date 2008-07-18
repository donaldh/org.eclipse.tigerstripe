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
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.core.properties.AnnotationPropertiesSection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * @author Yuri Strot
 *
 */
public class ColorSection extends AnnotationPropertiesSection {
	
	private Composite right;
	private Slider red;
	private Slider green;
	private Slider blue;
	
	private Annotation annotation;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		parent.setLayout(new GridLayout());
		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage.getWidgetFactory();
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
	
	protected Slider getColorSpinner(Composite parent, TabbedPropertySheetWidgetFactory factory,
			final String feature) {
		factory.createLabel(parent, feature.substring(0, 1).toUpperCase() + feature.substring(1));
		final Slider slider = new Slider(parent, SWT.NONE);
		slider.setMinimum(0);
		slider.setMaximum(255);
		final Spinner spinner = new Spinner(parent, SWT.BORDER);
		spinner.setMinimum(0);
		spinner.setMaximum(255);
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
		spinner.addModifyListener(new ModifyListener() {
		
			public void modifyText(ModifyEvent e) {
				int value = spinner.getSelection();
				if (value != slider.getSelection())
					slider.setSelection(value);
				updateColor(feature, value);
				updateColor();
			}
		});
		return slider;
	}
	
	private void updateColor() {
		Color color = new Color(right.getDisplay(), red.getSelection(),
				green.getSelection(), blue.getSelection());
		right.setBackground(color);
	}
	
	private void updateColor(String feature, int value) {
		EStructuralFeature efeature = annotation.getContent(
				).eClass().getEStructuralFeature(feature);
		annotation.getContent().eSet(efeature, new Integer(value));
	}
	
	private void updateColor(Annotation annotation) {
		if (right != null && !right.isDisposed()) {
			org.eclipse.tigerstripe.annotation.ui.example.customview.styles.Color color = 
				(org.eclipse.tigerstripe.annotation.ui.example.customview.styles.Color)annotation.getContent();
			if (color != null) {
				red.setSelection(color.getRed());
				green.setSelection(color.getGreen());
				blue.setSelection(color.getBlue());
				updateColor();
			}
		}
	}
	
	protected void updateSection(Annotation annotation) {
		this.annotation = annotation;
		updateColor(annotation);
	}

}
