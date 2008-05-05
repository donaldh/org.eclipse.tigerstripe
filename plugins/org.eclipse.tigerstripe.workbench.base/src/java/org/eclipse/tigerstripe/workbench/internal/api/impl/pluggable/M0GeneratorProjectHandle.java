/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable;

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.M0ProjectDescriptor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM0GeneratorProject;

public class M0GeneratorProjectHandle extends GeneratorProjectHandle implements
		ITigerstripeM0GeneratorProject {

	private M0ProjectDescriptor descriptor;

	public M0GeneratorProjectHandle(URI projectContainerURI) {
		super(projectContainerURI);
	}

	@Override
	protected String getDescriptorFilename() {
		return ITigerstripeConstants.M0_GENERATOR_DESCRIPTOR;
	}

	@Override
	public void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		// TODO Auto-generated method stub

	}

	@Override
	protected WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		M0GeneratorProjectHandle copy = new M0GeneratorProjectHandle(getURI());
		return copy;
	}

	public GeneratorProjectDescriptor getDescriptor()
			throws TigerstripeException {
		if (descriptor == null) {
			File baseDir = new File(this.projectContainerURI);
			if (baseDir.isDirectory()) {
				descriptor = new M0ProjectDescriptor(baseDir);
			} else
				throw new TigerstripeException("Invalid project "
						+ baseDir.toString());
		}

		descriptor.reload(false);
		return this.descriptor;
	}
}
