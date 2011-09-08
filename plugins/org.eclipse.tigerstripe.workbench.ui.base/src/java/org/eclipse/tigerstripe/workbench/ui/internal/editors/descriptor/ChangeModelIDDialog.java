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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.refactor.ChangeModelIdJob;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

/**
 * This class represents dialog for changing tigerstripe model id
 */
public class ChangeModelIDDialog extends Dialog {

	private final ITigerstripeModelProject project;
	private Text modelIdText;
	private Button updateAnnotationsButton;
	private String oldModelId;

	public ChangeModelIDDialog(Shell parentShell,
			ITigerstripeModelProject project) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.project = project;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		((GridLayout) composite.getLayout()).numColumns = 2;
		Label label = new Label(composite, SWT.LEFT);
		label.setText("New &model ID:");
		modelIdText = new Text(composite, SWT.BORDER);
		oldModelId = getModelId();
		if (oldModelId == null) {
			oldModelId = "";
		}
		modelIdText.setText(oldModelId);
		modelIdText.selectAll();
		modelIdText.forceFocus();
		modelIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		modelIdText.setLayoutData(data);
		updateAnnotationsButton = new Button(composite, SWT.CHECK);
		updateAnnotationsButton.setText("Update &annotations");
		updateAnnotationsButton.setSelection(true);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		updateAnnotationsButton.setLayoutData(data);
		return composite;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control result = super.createContents(parent);
		validate();
		return result;
	}

	private void validate() {
		getButton(IDialogConstants.OK_ID).setEnabled(isValid());
	}

	private boolean isValid() {
		String text = modelIdText.getText();
		if (text == null || text.length() == 0)
			return false;
		return !text.equals(oldModelId);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Change Model ID");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 180);
	}

	private String getModelId() {
		try {
			return project.getProjectDetails().getModelId();
		} catch (Exception e) {
			EclipsePlugin.log(e);
			return null;
		}
	}

	@Override
	protected void okPressed() {
		ChangeModelIdJob job = new ChangeModelIdJob(project, modelIdText
				.getText(), updateAnnotationsButton.getSelection());
		job.schedule();
		super.okPressed();
	}
}
