/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.releng.downloadsite.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage;

/**
 * Base for all DownloadSite related ant tasks.
 * 
 * It is mainly there to ensure the right initializations occur in all
 * DownloadSite related ant tasks.
 * 
 * @author erdillon
 * 
 */
public abstract class BaseTask extends Task {

	@Override
	public void execute() throws BuildException {
		// Ensures the initialization of the EMF registry
		// Note: this is required because we use EMF outside of Eclipse
		@SuppressWarnings("unused")
		DownloadSitePackage packageInstance = DownloadSitePackage.eINSTANCE;

	}

}
