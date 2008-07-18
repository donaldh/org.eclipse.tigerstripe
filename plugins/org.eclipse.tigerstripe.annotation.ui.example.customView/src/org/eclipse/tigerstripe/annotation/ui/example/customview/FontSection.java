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
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * @author Yuri Strot
 *
 */
public class FontSection extends AnnotationPropertiesSection {
	
	private CLabel preview;
	private FontData data;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		parent.setLayout(new GridLayout());
		Font defaultFont = JFaceResources.getDefaultFont();
		FontData defaultData = defaultFont.getFontData()[0];
		
		TabbedPropertySheetWidgetFactory factory = tabbedPropertySheetPage.getWidgetFactory();
		
		Group group = factory.createGroup(parent, "Font");
		group.setLayout(new GridLayout(3, false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		final CCombo fontCombo = factory.createCCombo(group, SWT.READ_ONLY | SWT.BORDER);
		fontCombo.setItems(FontHelper.getFontNames());
		fontCombo.addSelectionListener(new SelectionListener() {
		
			public void widgetSelected(SelectionEvent e) {
				data.setName(fontCombo.getText());
				Font font = new Font(preview.getDisplay(), data);
				preview.setFont(font);
			}
		
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		fontCombo.setText(defaultData.getName());
		
		final CCombo sizeCombo = factory.createCCombo(group, SWT.READ_ONLY | SWT.BORDER);
		sizeCombo.setItems(FontHelper.getFontSizes());
		sizeCombo.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				data.setHeight(Integer.parseInt(sizeCombo.getText()));
				Font font = new Font(preview.getDisplay(), data);
				preview.setFont(font);
			}
		
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		sizeCombo.setText(defaultData.getHeight() + "");
		
		final CCombo styleCombo = factory.createCCombo(group, SWT.READ_ONLY | SWT.BORDER);
		styleCombo.setItems(new String[] { "Normal", "Bold", "Italic"});
		styleCombo.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				String text = styleCombo.getText();
				int style = SWT.NORMAL;
				if (text.equals("Bold")) {
					style = SWT.BOLD;
				}
				if (text.equals("Italic")) {
					style = SWT.ITALIC;
				}
				data.setStyle(style);
				Font font = new Font(preview.getDisplay(), data);
				preview.setFont(font);
			}
		
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		styleCombo.setText(defaultData.getStyle() + "");
		
		
		preview = factory.createCLabel(group, "Example Text");
		data = new FontData();
		data.setHeight(defaultData.getHeight());
		data.setName(defaultData.getName());
		data.setStyle(defaultData.getStyle());
		preview.setFont(new Font(preview.getDisplay(), data));
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		preview.setLayoutData(gridData);
	}
	
	protected void updateSection(Annotation annotation) {
	}

}
