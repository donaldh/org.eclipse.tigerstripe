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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.StereotypeSectionManager;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.TSMessageDialog;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MethodReturnDetailsEditDialog extends TSMessageDialog {

	private IMethod initialMethod;

	private FormEditor editor;

	public MethodReturnDetailsEditDialog(Shell parentShell, IMethod method,
			FormEditor editor) {
		super(parentShell);

		this.initialMethod = method;
		this.editor = editor;

		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

	protected void setDefaultMessage() {
		setMessage("Method Return details");
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		// area.setLayout(new FillLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 3;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

		createMessageArea(composite, nColumns);

		createAnnotationsControls(composite, nColumns);

		getShell().setText("Method Return details");
		getShell().setMinimumSize(new Point(300, 250));
		setDefaultMessage();
		return area;
	}

	/**
	 * Returns the recommended maximum width for text fields (in pixels). This
	 * method requires that createContent has been called before this method is
	 * call. Subclasses may override to change the maximum width for text
	 * fields.
	 * 
	 * @return the recommended maximum width for text fields.
	 */
	protected int getMaxFieldWidth() {
		return convertWidthInCharsToPixels(40);
	}

	private void createAnnotationsControls(Composite composite, int nColumns) {
		Group innerComposite = new Group(composite, SWT.BOLD);
		innerComposite.setText("Annotations");
		GridData gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.horizontalSpan = nColumns;
		innerComposite.setLayoutData(gd);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		innerComposite.setLayout(layout);

		Table annTable = new Table(innerComposite, SWT.BORDER);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 3;
		td.heightHint = 70;
		annTable.setLayoutData(td);

		Button addAnno = new Button(innerComposite, SWT.PUSH);
		addAnno.setText("Add");
		td = new TableWrapData(TableWrapData.FILL);
		addAnno.setLayoutData(td);

		Button editAnno = new Button(innerComposite, SWT.PUSH);
		editAnno.setText("Edit");
		td = new TableWrapData(TableWrapData.FILL);
		editAnno.setLayoutData(td);

		Button removeAnno = new Button(innerComposite, SWT.PUSH);
		removeAnno.setText("Remove");

		StereotypeSectionManager stereomgr = new StereotypeSectionManager(
				addAnno, editAnno, removeAnno, annTable,
				((Method) initialMethod).new ReturnTypeWrapper(initialMethod),
				getShell(), (ArtifactEditorBase) editor);
		stereomgr.delegate();

	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	public IMethod getIMethod() {
		return initialMethod;
	}

}
