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
package org.eclipse.tigerstripe.eclipse.wizards.artifacts;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jdt.ui.wizards.NewContainerWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.eclipse.wizards.TSRuntimeContext;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class TSRuntimeBasedWizardPage extends NewContainerWizardPage {

	private TSRuntimeContext tsRuntimeContext;

	protected IStatus tsRuntimeContextStatus;

	public TSRuntimeBasedWizardPage(String pageName) {
		super(pageName);
	}

	protected abstract void initFromContext();

	/**
	 * This tries to initialize the TSRuntime context based on the given
	 * IJavaElement
	 * 
	 * @param jElement
	 */
	protected void initTSRuntimeContext(IJavaElement jElement) {
		this.tsRuntimeContext = new TSRuntimeContext();
		this.tsRuntimeContext.readTSDescriptor(jElement);
		tsRuntimeContextStatus = tsRuntimeContextChanged();
	}

	protected TSRuntimeContext getTSRuntimeContext() {
		return this.tsRuntimeContext;
	}

	protected IStatus tsRuntimeContextChanged() {
		StatusInfo status = new StatusInfo();

		this.tsRuntimeContext = new TSRuntimeContext();
		this.tsRuntimeContext.readTSDescriptor(getPackageFragmentRoot());
		initFromContext();

		if (getTSRuntimeContext() == null
				|| !getTSRuntimeContext().isValidProject()) {
			status.setError("Invalid Tigerstripe Project");
		}
		return status;
	}

	/**
	 * Creates a separator line. Expects a <code>GridLayout</code> with at
	 * least 1 column.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(
				composite, nColumns, convertHeightInCharsToPixels(1));
	}

	public IProject getIProject() {
		String str = getPackageFragmentRootText();

		if (str != null && str.length() != 0) {
			IPath path = new Path(str);
			IResource res = getWorkspaceRoot().findMember(path);

			if (res != null)
				return res.getProject();
		}
		return null;
	}
}