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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.segment;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewContractSegmentWizardPage extends WizardNewFileCreationPage {

	public NewContractSegmentWizardPage(String pageName,
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
				|| !IContractSegment.FILE_EXTENSION.endsWith(extension)) {
			filePath = filePath
					.addFileExtension(IContractSegment.FILE_EXTENSION);
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
		String xmlString = "";

		try {
			IContractSegment emptySegment = InternalTigerstripeCore.getIContractSession()
					.makeIContractSegment(null);

			String name = this.getFileName();
			if (name.indexOf(".") != -1) {
				name = name.substring(0, name.lastIndexOf("."));
			}
			emptySegment.setName(name);
			xmlString = emptySegment.asText();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		StringBufferInputStream iStream = new StringBufferInputStream(xmlString);
		return iStream;
	}

}
