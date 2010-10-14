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
package org.eclipse.tigerstripe.workbench.ui;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

public class DiagramsPreferencePageWithConverter extends DiagramsPreferencePage {

	private final Runnable runnable;

	public DiagramsPreferencePageWithConverter() {
		super();
		runnable = new Runnable() {
			public void run() {
				performOk();
				DiagramUtils.convertExitingAll();
			}
		};
	}

	public DiagramsPreferencePageWithConverter(IPreferenceStore store,
			Runnable runnable) {
		super(store);
		this.runnable = runnable;
	}

	@Override
	public void createFieldEditors() {
		super.createFieldEditors();

		Composite applyPanel = new Composite(getFieldEditorParent(), SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(3, 3).numColumns(2)
				.applyTo(applyPanel);
		Link link = new Link(applyPanel, SWT.NONE);
		link.setText("Settings can be <a>applied to existing elements</a>");
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runnable.run();
			}
		});
	}

}
