/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
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

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * @author Alena Repina
 */
public class TigerstripeLayoutFactory {

	public static TableWrapLayout createPageTableWrapLayout(int numColumns,
			boolean makeColumnsEqualWidth) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 6;
		layout.topMargin = 6;
		layout.leftMargin = 6;
		layout.rightMargin = 6;
		layout.verticalSpacing = 10;
		layout.horizontalSpacing = 10;

		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = makeColumnsEqualWidth;

		return layout;
	}

	public static GridLayout createPageGridLayout(int numColumns,
			boolean makeColumnsEqualWidth) {
		GridLayout layout = new GridLayout();
		layout.marginBottom = 6;
		layout.marginTop = 6;
		layout.marginLeft = 6;
		layout.marginRight = 6;
		layout.verticalSpacing = 10;
		layout.horizontalSpacing = 10;
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

	public static TableWrapLayout createDetailsTableWrapLayout() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 0;
		layout.topMargin = 0;
		layout.leftMargin = 10;
		layout.rightMargin = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;

		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = false;

		return layout;
	}

	public static GridLayout createClearGridLayout(int numColumns,
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

	public static TableWrapLayout createClearTableWrapLayout(int numColumns,
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

	public static TableWrapLayout createFormTableWrapLayout(int numColumns,
			boolean makeColumnsEqualWidth) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 6;
		layout.topMargin = 6;
		layout.leftMargin = 6;
		layout.rightMargin = 6;
		layout.verticalSpacing = 10;
		layout.horizontalSpacing = 6;

		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = makeColumnsEqualWidth;

		return layout;
	}

	public static GridLayout createFormGridLayout(int numColumns,
			boolean makeColumnsEqualWidth) {
		GridLayout layout = new GridLayout();
		layout.marginBottom = 6;
		layout.marginTop = 6;
		layout.marginLeft = 6;
		layout.marginRight = 6;
		layout.verticalSpacing = 10;
		layout.horizontalSpacing = 6;
		layout.marginWidth = 0;
		layout.marginHeight = 0;

		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = makeColumnsEqualWidth;

		return layout;
	}

	public static TableWrapLayout createFormPaneTableWrapLayout(int numColumns,
			boolean makeColumnsEqualWidth) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 0;
		layout.topMargin = 0;
		layout.leftMargin = 0;
		layout.rightMargin = 0;
		layout.verticalSpacing = 10;
		layout.horizontalSpacing = 6;

		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = makeColumnsEqualWidth;

		return layout;
	}

	public static GridLayout createButtonsGridLayout() {
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		return layout;
	}

	public static TableWrapLayout createButtonsTableWrapLayout() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 0;
		layout.topMargin = 0;
		layout.leftMargin = 0;
		layout.rightMargin = 0;
		layout.verticalSpacing = 6;
		layout.horizontalSpacing = 6;
		return layout;
	}
}