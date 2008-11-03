/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     J. Strawn (Cisco Systems, Inc.) - Initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.optional.buckminster.internal;

import java.io.IOException;

import org.eclipse.buckminster.core.cspec.builder.CSpecBuilder;
import org.eclipse.buckminster.core.metadata.model.BOMNode;
import org.eclipse.buckminster.core.reader.ICatalogReader;
import org.eclipse.buckminster.core.reader.IComponentReader;
import org.eclipse.buckminster.core.version.ProviderMatch;
import org.eclipse.buckminster.runtime.BuckminsterException;
import org.eclipse.buckminster.runtime.MonitorUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;

public class TigerstripeProjectCSpecBuilder extends AbstractTigerstripeResolutionBuilder {

	public BOMNode build(IComponentReader[] readerHandle, boolean forResolutionAidOnly, IProgressMonitor monitor) throws CoreException {

		IComponentReader reader = readerHandle[0];
		ProviderMatch ri = reader.getProviderMatch();

		monitor.beginTask(null, 3000);
		monitor.subTask("Generating cspec from " + TigerstripeProjectComponentType.TIGERSTRIPE_XML_FILE);

		Document tsxml = null;
		IProgressMonitor subMon = MonitorUtils.subMonitor(monitor, 2000);
		if (reader instanceof ICatalogReader) {

			try {
				tsxml = ((ICatalogReader) reader).readFile(TigerstripeProjectComponentType.TIGERSTRIPE_XML_FILE, this, subMon);
			} catch (IOException e) {
				throw BuckminsterException.wrap(e);
			}
			if (tsxml == null) {
				throw BuckminsterException.wrap(new NullPointerException());
			}
		}

		CSpecBuilder cspecBld = ri.createCSpec();
		cspecBld.setVersion(getOSGiVersionFromDocument(tsxml, VersionQueryEnum.PROJECT));
		TigerstripeProjectComponentType.addDependencies(reader, cspecBld, tsxml);
		return createNode(reader, cspecBld, null);
	}

}
