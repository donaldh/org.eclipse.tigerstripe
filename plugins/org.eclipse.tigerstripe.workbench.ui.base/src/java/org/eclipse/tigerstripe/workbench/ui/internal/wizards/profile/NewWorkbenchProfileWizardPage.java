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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.profile;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.core.profile.WorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewWorkbenchProfileWizardPage extends WizardNewFileCreationPage {

	public NewWorkbenchProfileWizardPage(String pageName,
			IStructuredSelection selection) {
		super(pageName, selection);
	}

	/**
	 * Makes sure the filehandle as the right file extension to be a Workbench
	 * Profile
	 */
	@Override
	protected IFile createFileHandle(IPath filePath) {

		String extension = filePath.getFileExtension();
		if (extension == null || extension.length() == 0
				|| !IWorkbenchProfile.FILE_EXTENSION.endsWith(extension)) {
			filePath = filePath
					.addFileExtension(IWorkbenchProfile.FILE_EXTENSION);
		}

		IFile file = super.createFileHandle(filePath);
		return file;
	}

	/**
	 * Creates a new IWorkbenchProfile content based on a default Workbench
	 * profile
	 * 
	 */
	@Override
	protected InputStream getInitialContents() {
		WorkbenchProfile emptyProfile = (WorkbenchProfile) TigerstripeCore.getWorkbenchProfileSession()
				.makeWorkbenchProfile();

		String name = this.getFileName();
		if (name.indexOf(".") != -1) {
			name = name.substring(0, name.lastIndexOf("."));
		}
		emptyProfile.setName(name);
		String xmlString = emptyProfile.asText();

		StringBufferInputStream iStream = new StringBufferInputStream(xmlString);
		return iStream;
	}

}
