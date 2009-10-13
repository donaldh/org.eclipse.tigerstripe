package org.eclipse.tigerstripe.workbench.optional.buckminster.internal;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.buckminster.core.cspec.AbstractResolutionBuilder;
import org.eclipse.buckminster.core.reader.IComponentReader;
import org.eclipse.buckminster.core.reader.IStreamConsumer;
import org.eclipse.buckminster.core.version.VersionType;
import org.eclipse.buckminster.core.version.VersionHelper;
import org.eclipse.buckminster.runtime.BuckminsterException;
import org.eclipse.buckminster.runtime.MonitorUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.internal.provisional.p2.core.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class AbstractTigerstripeResolutionBuilder extends AbstractResolutionBuilder implements IStreamConsumer<Document> {

	public AbstractTigerstripeResolutionBuilder() {
		super();
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
		} catch (SAXException e) {
			throw BuckminsterException.wrap(e);
		} catch (ParserConfigurationException e) {
			throw BuckminsterException.wrap(e);
		} finally {
			MonitorUtils.worked(monitor, 1);
			monitor.done();
		}
	}

	protected Version getOSGiVersionFromDocument(Document tsxml, VersionQueryEnum query) throws CoreException {

		Node version = null;
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			version = (Node) xpath.evaluate(query.getQuery(), tsxml, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw BuckminsterException.wrap(e);
		}

		if (version != null) {
			return VersionHelper.createVersion(VersionType.OSGI, version.getTextContent().trim());
		}
		throw BuckminsterException.fromMessage("Invalid tigerstripe project file - no version defined.");
	}

}