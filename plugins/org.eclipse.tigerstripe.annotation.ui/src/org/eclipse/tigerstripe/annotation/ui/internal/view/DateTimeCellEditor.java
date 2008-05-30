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
package org.eclipse.tigerstripe.annotation.ui.internal.view;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

/**
 * @author Yuri Strot
 * 
 */
public class DateTimeCellEditor extends ExtendedDialogCellEditor {

	private String name;
	private Date initDate;
	private boolean isNull;
	
	public static final Date NULL_DATE = new Date();

	/**
	 * @param composite
	 * @param labelProvider
	 */
	public DateTimeCellEditor(Composite composite, String name, Date date) {
		super(composite, new DefaultLabelProvider());
		this.name = name;
		this.initDate = date;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		Object value = super.doGetValue();
		return value == NULL_DATE ? null : value;
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		final DateTime dateTime[] = new DateTime[1];
		final Date[] date = new Date[1];
		Dialog d = new Dialog(cellEditorWindow.getShell()) {
			protected Control createDialogArea(Composite parent) {
				getShell().setText(name);
				Composite dialogArea = (Composite) super
						.createDialogArea(parent);
				addCheck(dialogArea);
				
				dateTime[0] = new DateTime(dialogArea, SWT.CALENDAR | SWT.BORDER);
				isNull = initDate == null;
				if (isNull)
					dateTime[0].setEnabled(false);
				else
					setDate(dateTime[0], initDate);
				date[0] = getDate(dateTime[0]);
				dateTime[0].addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						date[0] = getDate(dateTime[0]);
						super.widgetSelected(e);
					}
				});
				return dialogArea;
			}
			
			private void addCheck(Composite parent) {
				final Button check = new Button(parent, SWT.CHECK);
				check.setText("Empty Date");
				check.setSelection(initDate == null);
				check.addSelectionListener(new SelectionListener() {
				
					public void widgetSelected(SelectionEvent e) {
						enableDate();
					}
				
					public void widgetDefaultSelected(SelectionEvent e) {
						enableDate();
					}
					
					private void enableDate() {
						isNull = check.getSelection();
						dateTime[0].setEnabled(!isNull);
					}
				
				});
			}
		};
		d.setBlockOnOpen(true);
		d.open();
		if (d.getReturnCode() == Dialog.OK) {
			return isNull ? NULL_DATE : date[0];
		}
		return null;
	}
	
	private static Date getDate(DateTime dt) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, dt.getYear() + 1900);
		calendar.set(Calendar.MONTH, dt.getMonth());
		calendar.set(Calendar.DAY_OF_MONTH, dt.getDay());
		return calendar.getTime();
	}
	
	private static void setDate(DateTime dt, Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		dt.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		dt.setMonth(calendar.get(Calendar.MONTH));
		dt.setYear(calendar.get(Calendar.YEAR) - 1900);
	}

}
