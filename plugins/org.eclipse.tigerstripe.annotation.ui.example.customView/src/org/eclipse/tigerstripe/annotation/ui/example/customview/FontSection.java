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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.core.properties.AnnotationPropertiesSection;
import org.eclipse.tigerstripe.annotation.ui.example.customview.styles.StylesPackage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * @author Yuri Strot
 *
 */
public class FontSection extends AnnotationPropertiesSection {
	
	private static final String BOLD = "Bold";
	private static final String ITALIC = "Italic";
	private static final String NORMAL = "Normal";
	
	private FontProperty nameProperty;
	private FontProperty heightProperty;
	private FontProperty styleProperty;
	
	private Annotation annotation;
	private CLabel preview;
	private FontData data;
	
	private class FontProperty {
		
		private static final int NAME = 1;
		private static final int HEIGHT = 2;
		private static final int STYLE = 3;
		
		private int property;
		private CCombo propertyCombo;
		
		public FontProperty(int property) {
			this.property = property;
		}
		
		private EAttribute getFeature() {
			switch (property) {
				case STYLE:
					return StylesPackage.eINSTANCE.getFont_Style();
				case HEIGHT:
					return StylesPackage.eINSTANCE.getFont_Height();
				default:
					return StylesPackage.eINSTANCE.getFont_Name();
			}
		}
		
		private String getDefault() {
			Font defaultFont = JFaceResources.getDefaultFont();
			FontData defaultData = defaultFont.getFontData()[0];
			switch (property) {
				case STYLE:
					return getOppositeValue(defaultData.getStyle());
				case HEIGHT:
					return getOppositeValue(defaultData.getHeight());
				default:
					return defaultData.getName();
			}
			
		}
		
		private String getOppositeValue(int value) {
			switch (property) {
				case STYLE:
					if (value == SWT.BOLD)
						return BOLD;
					if (value == SWT.ITALIC)
						return ITALIC;
					return NORMAL;
				default:
					return value + "";
			}
		}
		
		private int getValue(String value) {
			switch (property) {
				case STYLE:
					if (value.equals(BOLD))
						return SWT.BOLD;
					if (value.equals(ITALIC))
						return SWT.ITALIC;
					return SWT.NORMAL;
				default:
					return Integer.parseInt(value);
			}
		}
		
		public void update() {
			EAttribute attr = getFeature();
			if (annotation.getContent() == null)
				return;
			Object value = annotation.getContent().eGet(attr);
			String text;
			if ((value == null && attr.getDefaultValue() == null) ||
					(value != null && attr.getDefaultValue() != null &&
							value.equals(attr.getDefaultValue()))) {
				text = getDefault();
			}
			else {
				if (property == STYLE)
					text = getOppositeValue( ((Integer)value).intValue() );
				else
					text = value.toString();
			}
			setPreview(text);
			propertyCombo.setText(text);
		}
		
		private void setPreview(String value) {
			switch (property) {
				case NAME:
					data.setName(value);
					break;
				case HEIGHT:
					data.setHeight(getValue(value));
					break;
				case STYLE:
					data.setStyle(getValue(value));
					break;
			}
			Font font = new Font(preview.getDisplay(), data);
			preview.setFont(font);
			preview.getParent().layout();
		}
		
		private void set(String value) {
			setPreview(value);
			if (annotation != null) {
				if (property == NAME)
					annotation.getContent().eSet(getFeature(), value);
				else {
					annotation.getContent().eSet(
							getFeature(), new Integer(getValue(value)));
				}
			}
		}
		
		public void create(Composite parent, TabbedPropertySheetWidgetFactory factory, String[] items) {
			propertyCombo = factory.createCCombo(parent, SWT.READ_ONLY | SWT.BORDER);
			propertyCombo.setItems(items);
			propertyCombo.addSelectionListener(new SelectionListener() {
			
				public void widgetSelected(SelectionEvent e) {
					set(propertyCombo.getText());
				}
			
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		parent.setLayout(new GridLayout());
		
		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage.getWidgetFactory();
		
		Group group = factory.createGroup(parent, "Font");
		group.setLayout(new GridLayout(3, false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		nameProperty = new FontProperty(FontProperty.NAME);
		nameProperty.create(group, factory, FontHelper.getFontNames());
		
		heightProperty = new FontProperty(FontProperty.HEIGHT);
		heightProperty.create(group, factory, FontHelper.getFontSizes());
		
		styleProperty = new FontProperty(FontProperty.STYLE);
		styleProperty.create(group, factory, new String[] { NORMAL, BOLD, ITALIC});
		
		preview = factory.createCLabel(group, "Example Text");
		data = new FontData();
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		gridData.verticalAlignment = SWT.BEGINNING;
		preview.setLayoutData(gridData);
	}
	
	protected void updateSection(Annotation annotation) {
		this.annotation = annotation;
		nameProperty.update();
		heightProperty.update();
		styleProperty.update();
	}

}
