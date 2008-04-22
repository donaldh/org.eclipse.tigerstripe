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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.xmi;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.AbstractElementListSelectionDialog;
import org.eclipse.ui.dialogs.FilteredList;

/**
 * A Generic Artifact selection dialog.
 * 
 * 
 * @author Eric Dillon
 * 
 */
public class AnnotableElementSelectorDialog extends
		AbstractElementListSelectionDialog {

	private Object[] fElements;

	private AnnotableLabelProvider fRenderer;
	private Text fText;

	/**
	 * Creates a list selection dialog.
	 * 
	 * @param parent
	 *            the parent widget.
	 * @param renderer
	 *            the label renderer.
	 */
	public AnnotableElementSelectorDialog(Shell parent,
			AnnotableLabelProvider renderer) {
		super(parent, renderer);
		fRenderer = renderer;
		setMultipleSelection(true);
	}

	/**
	 * Sets the elements of the list.
	 * 
	 * @param elements
	 *            the elements of the list.
	 */
	public void setElements(Object[] elements) {
		fElements = elements;
	}

	/*
	 * @see SelectionStatusDialog#computeResult()
	 */
	@Override
	protected void computeResult() {
		setResult(Arrays.asList(getSelectedElements()));
	}

	/*
	 * @see Dialog#createDialogArea(Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite contents = (Composite) super.createDialogArea(parent);

		createMessageArea(contents);
		createFilterText(contents);
		FilteredList list = createFilteredList(contents);
		list.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				handleListSelectionChanged(e);
			}

			public void widgetSelected(SelectionEvent e) {
				handleListSelectionChanged(e);
			}
		});
		createPackageArea(contents);

		setListElements(fElements);

		setSelection(getInitialElementSelections().toArray());

		return contents;
	}

	protected void handleListSelectionChanged(SelectionEvent e) {
		Object[] newSelection = fFilteredList.getSelection();

		if (newSelection.length != 0) {
			fText.setText(fRenderer.getPackage(newSelection[0]));
		} else {
			fText.setText("");
		}
	}

	protected Text createPackageArea(Composite parent) {
		Text text = new Text(parent, SWT.BORDER);

		GridData data = new GridData();
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		text.setLayoutData(data);
		text.setFont(parent.getFont());

		fText = text;

		return text;
	}
}
