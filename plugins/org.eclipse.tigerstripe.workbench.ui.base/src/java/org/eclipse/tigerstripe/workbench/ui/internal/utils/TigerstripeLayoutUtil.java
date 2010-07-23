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
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * @author Alena Repina
 */
public class TigerstripeLayoutUtil {

	public static TableWrapLayout createPageTableWrapLayout(int numColumns,
			boolean makeColumnsEqualWidth) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 5;
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 5;
		layout.verticalSpacing = 10;
		layout.horizontalSpacing = 5;

		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = makeColumnsEqualWidth;

		return layout;
	}

	public static GridLayout createPageGridLayout(int numColumns,
			boolean makeColumnsEqualWidth) {
		GridLayout layout = new GridLayout();
		layout.marginBottom = 5;
		layout.marginTop = 5;
		layout.marginLeft = 5;
		layout.marginRight = 5;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 5;
		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = makeColumnsEqualWidth;
		return layout;
	}

	public static Section createSection(Composite parent, FormToolkit toolkit,
			int style, String name, String desc) {
		Section section = toolkit.createSection(parent, style);
		section.setText(name);
		section.setDescription(desc);

		// Look and feel
		section.setTitleBarBorderColor(ColorUtils.TS_ORANGE);

		return section;
	}

	public static Composite createSectionBodyGridLayout(Section section,
			FormToolkit toolkit, int numColumns) {
		Composite body = toolkit.createComposite(section, SWT.WRAP);
		toolkit.paintBordersFor(body);
		section.setClient(body);

		// Layout
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		body.setLayout(layout);

		return body;
	}

	public static Composite createButtonsClient(Composite parent,
			FormToolkit toolkit) {
		Composite buttonsClient = toolkit.createComposite(parent);
		buttonsClient.setLayoutData(new GridData(
				GridData.VERTICAL_ALIGN_BEGINNING));
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		buttonsClient.setLayout(layout);
		return buttonsClient;
	}

	public static GridLayout createZeroGridLayout(int numColumns,
			boolean makeColumnsEqualWidth) {
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = makeColumnsEqualWidth;
		return layout;
	}

	public static TableWrapLayout createZeroTableWrapLayout(int numColumns,
			boolean makeColumnsEqualWidth) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 0;
		layout.topMargin = 0;
		layout.leftMargin = 0;
		layout.rightMargin = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;

		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = makeColumnsEqualWidth;

		return layout;
	}
}
