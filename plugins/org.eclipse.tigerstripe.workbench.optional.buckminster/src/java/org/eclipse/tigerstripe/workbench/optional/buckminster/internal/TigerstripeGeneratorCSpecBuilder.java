package org.eclipse.tigerstripe.workbench.optional.buckminster.internal;

import java.io.IOException;

import org.eclipse.buckminster.core.cspec.builder.CSpecBuilder;
import org.eclipse.buckminster.core.metadata.model.BOMNode;
import org.eclipse.buckminster.core.reader.ICatalogReader;
import org.eclipse.buckminster.core.reader.IComponentReader;
import org.eclipse.buckminster.core.reader.IFileReader;
import org.eclipse.buckminster.core.reader.URLFileReader;
import org.eclipse.buckminster.core.version.ProviderMatch;
import org.eclipse.buckminster.runtime.BuckminsterException;
import org.eclipse.buckminster.runtime.IFileInfo;
import org.eclipse.buckminster.runtime.MonitorUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;

public class TigerstripeGeneratorCSpecBuilder extends AbstractTigerstripeResolutionBuilder {

	public BOMNode build(IComponentReader[] readerHandle, boolean forResolutionAidOnly, IProgressMonitor monitor) throws CoreException {

		IComponentReader reader = readerHandle[0];
		ProviderMatch ri = reader.getProviderMatch();

		monitor.beginTask(null, 3000);
		monitor.subTask("Generating cspec from " + TigerstripeGeneratorComponentType.TSPLUGIN_XML_FILE);


		IProgressMonitor subMon = MonitorUtils.subMonitor(monitor, 2000);
		if (reader instanceof ICatalogReader) {
			Document xml = null;
			try {
				xml = ((ICatalogReader) reader).readFile(TigerstripeGeneratorComponentType.TSPLUGIN_XML_FILE, this, subMon);
			} catch (IOException e) {
				throw BuckminsterException.wrap(e);
			}
			if (xml == null) {
				throw BuckminsterException.wrap(new NullPointerException());
			}
			CSpecBuilder cspecBld = ri.createCSpec();
			cspecBld.setVersion(getOSGiVersionFromDocument(xml, VersionQueryEnum.GENERATOR));
			return createNode(reader, cspecBld, null);
		}
		
		if (reader instanceof IFileReader){	
			CSpecBuilder cspecBld = ri.createCSpec();
			return createNode(reader, cspecBld, null);
		}
		throw BuckminsterException.fromMessage("Don't know how to handle this reader type:"+reader.toString());
	}

}
