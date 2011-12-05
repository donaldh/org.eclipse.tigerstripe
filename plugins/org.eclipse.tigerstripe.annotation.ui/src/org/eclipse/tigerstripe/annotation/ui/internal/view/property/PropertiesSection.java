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
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertiesSection;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;
import org.eclipse.tigerstripe.annotation.ui.internal.view.property.PropertyTree.SelectionHandler;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author Yuri Strot
 * 
 */
public class PropertiesSection extends EPropertiesSection {

	private PropertyTree tree;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.ISection#createControls(org.eclipse
	 * .swt.widgets.Composite,
	 * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(final Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		tree = new PropertyTree(new SelectionHandler() {
			
			public void setNull() {
				PropertiesSelectionManager.getInstance().setSelection(null);	
			}
			
			public void set(EProperty object, TreeViewer viewer, boolean readOnly) {
				PropertiesSelectionManager.getInstance().setSelection(
						new PropertySelection(object, viewer, readOnly));
			}
		});
		Control control = tree.create(composite);

		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.height = 100;
		data.width = 100;

		control.setLayoutData(data);
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
		tree.setContent(content, readOnly);
	}

}
