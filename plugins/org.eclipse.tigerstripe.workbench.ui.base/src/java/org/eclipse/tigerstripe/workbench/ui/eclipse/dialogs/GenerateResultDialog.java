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
package org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.core.generation.PluginRunResult;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.ColorUtils;

/**
 * Display the log of a Generate
 * 
 * @author Eric Dillon
 * @since 2.1
 */
public class GenerateResultDialog extends Dialog {

	private Text pluginResultText;

	private Button copyToClipboardButton;

	private PluginRunResult[] result;

	public GenerateResultDialog(IShellProvider parentShell,
			PluginRunResult[] result) {
		super(parentShell);
		this.result = result;
	}

	public GenerateResultDialog(Shell parentShell, PluginRunResult[] result) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.result = result;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		getShell().setSize(500, 300);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		area.setLayout(layout);
		area.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(area);

		Label overallResult = new Label(area, SWT.NONE);
		if (result.length == 0) {
			overallResult.setText("No plugin enabled.");
			overallResult.setForeground(ColorUtils.TS_ORANGE);
		} else if (hasErrors(result)) {
			overallResult.setText("Generation Failed.");
			overallResult.setForeground(ColorUtils.TS_ORANGE);
		} else {
			overallResult.setText("Generation Successful.");
			overallResult.setForeground(ColorUtils.BLACK);
		}
		overallResult.setFont(new Font(null, "Arial", 12, SWT.BOLD));

		Label versionLabel = new Label(area, SWT.NONE);
		versionLabel.setText("Run Log");

		pluginResultText = new Text(area, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		pluginResultText.setEnabled(true);
		// pluginResultText.setText(asText(result, true), true, false);
		pluginResultText.setText(asText(result, false));
		GridData gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.heightHint = 70;
		pluginResultText.setLayoutData(gd);
		// pluginResultText.setColor("red", ColorUtils.TS_ORANGE);

		copyToClipboardButton = new Button(area, SWT.PUSH);
		copyToClipboardButton.setText("Copy to Clipboard");
		copyToClipboardButton.setToolTipText("Copy log to clipboard.");
		copyToClipboardButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				copyToClipboard();
			}
		});

		getShell().setText("Generate Result");
		return area;
	}

	private void copyToClipboard() {
		Clipboard clipboard = new Clipboard(getShell().getDisplay());
		clipboard.setContents(new Object[] { asText(result, false) },
				new Transfer[] { TextTransfer.getInstance() });
	}

	private String asText(PluginRunResult[] result, boolean includeHTML) {
		StringBuffer buffer = new StringBuffer();

		if (includeHTML)
			buffer.append("<form>");
		for (PluginRunResult res : result) {
			buffer.append(res.toString(includeHTML));
		}

		if (includeHTML)
			buffer.append("</form>");
		return buffer.toString();
	}

	private boolean hasErrors(PluginRunResult[] result) {

		boolean errorFound = false;
		for (PluginRunResult res : result) {
			if (res.errorExists() || res.warningExists()) {
				errorFound = true;
				continue;
			}
		}

		return errorFound;
	}
}
