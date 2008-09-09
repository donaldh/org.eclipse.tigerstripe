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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.buckminster.core.cspec.AbstractResolutionBuilder;
import org.eclipse.buckminster.core.cspec.builder.CSpecBuilder;
import org.eclipse.buckminster.core.cspec.model.CSpec;
import org.eclipse.buckminster.core.ctype.MissingCSpecSourceException;
import org.eclipse.buckminster.core.metadata.model.DepNode;
import org.eclipse.buckminster.core.reader.ICatalogReader;
import org.eclipse.buckminster.core.reader.IComponentReader;
import org.eclipse.buckminster.core.reader.IStreamConsumer;
import org.eclipse.buckminster.core.version.ProviderMatch;
import org.eclipse.buckminster.runtime.BuckminsterException;
import org.eclipse.buckminster.runtime.MonitorUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TigerstripeCSpecBuilder extends AbstractResolutionBuilder implements IStreamConsumer<Document> {

	public DepNode build(IComponentReader[] readerHandle, boolean forResolutionAidOnly, IProgressMonitor monitor) throws CoreException {

		IComponentReader reader = readerHandle[0];
		ProviderMatch ri = reader.getProviderMatch();
		monitor.beginTask(null, 3000);

		try {

			Document tsXmlDoc = null;
			IProgressMonitor subMon = MonitorUtils.subMonitor(monitor, 2000);

			try {
				if (reader instanceof ICatalogReader) {
					tsXmlDoc = ((ICatalogReader) reader).readFile("tigerstripe.xml", this, subMon);
				}
			}
			catch (FileNotFoundException e) {
				throw new MissingCSpecSourceException(reader.getProviderMatch());
			}

			CSpecBuilder cspecBldr = ri.createCSpec();

			if (tsXmlDoc != null) {
				// ExpandingProperties properties = new ExpandingProperties();
				TigerstripeComponentType.addDependencies(reader, cspecBldr, tsXmlDoc);
			}

			CSpec cspec = applyExtensions(cspecBldr.createCSpec(), forResolutionAidOnly, reader, MonitorUtils.subMonitor(monitor, 1000));
			return createResolution(reader, cspec, null);

		}
		catch (IOException e) {
			throw BuckminsterException.wrap(e);
		}
		finally {
			monitor.done();
		}

	}

	public Document consumeStream(IComponentReader reader, String streamName, InputStream stream, IProgressMonitor monitor) throws CoreException,
			IOException {

		monitor.beginTask(streamName, 1);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource source = new InputSource(stream);
			source.setSystemId(streamName);
			return builder.parse(source);
		}
		catch (SAXException e) {
			throw BuckminsterException.wrap(e);
		}
		catch (ParserConfigurationException e) {
			throw BuckminsterException.wrap(e);
		}
		finally {
			MonitorUtils.worked(monitor, 1);
			monitor.done();
		}
	}

}
