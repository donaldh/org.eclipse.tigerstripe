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
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.buckminster.core.cspec.AbstractResolutionBuilder;
import org.eclipse.buckminster.core.cspec.builder.CSpecBuilder;
import org.eclipse.buckminster.core.metadata.model.BOMNode;
import org.eclipse.buckminster.core.query.model.ComponentQuery;
import org.eclipse.buckminster.core.reader.ICatalogReader;
import org.eclipse.buckminster.core.reader.IComponentReader;
import org.eclipse.buckminster.core.reader.IStreamConsumer;
import org.eclipse.buckminster.core.version.ProviderMatch;
import org.eclipse.buckminster.core.version.VersionFactory;
import org.eclipse.buckminster.runtime.BuckminsterException;
import org.eclipse.buckminster.runtime.MonitorUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TigerstripeProjectCSpecBuilder extends AbstractResolutionBuilder implements IStreamConsumer<Document> {

	public Document consumeStream(IComponentReader reader, String streamName, InputStream stream, IProgressMonitor monitor)
			throws CoreException, IOException {

		monitor.beginTask(streamName, 1);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource source = new InputSource(stream);
			source.setSystemId(streamName);
			return builder.parse(source);
		} catch (SAXException e) {
			throw BuckminsterException.wrap(e);
		} catch (ParserConfigurationException e) {
			throw BuckminsterException.wrap(e);
		} finally {
			MonitorUtils.worked(monitor, 1);
			monitor.done();
		}
	}

	public BOMNode build(IComponentReader[] readerHandle, boolean forResolutionAidOnly, IProgressMonitor monitor)
			throws CoreException {

		IComponentReader reader = readerHandle[0];
		ProviderMatch ri = reader.getProviderMatch();

		monitor.beginTask(null, 3000);
		monitor.subTask("Generating cspec from " + TigerstripeProjectComponentType.TIGERSTRIPE_XML_FILE);

		// Not currently using Tigerstripe.xml, but will need for generator support
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
		if(tsxml != null) {
			TigerstripeProjectComponentType.addDependencies(reader, cspecBld, tsxml);
		}
				
		return createNode(reader, cspecBld, null);
	}
}
