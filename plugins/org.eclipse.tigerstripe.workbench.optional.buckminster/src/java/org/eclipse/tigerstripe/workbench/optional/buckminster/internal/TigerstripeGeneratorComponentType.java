package org.eclipse.tigerstripe.workbench.optional.buckminster.internal;

import org.eclipse.buckminster.core.ctype.AbstractComponentType;
import org.eclipse.buckminster.core.ctype.IResolutionBuilder;
import org.eclipse.buckminster.core.reader.IComponentReader;
import org.eclipse.buckminster.runtime.MonitorUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class TigerstripeGeneratorComponentType extends AbstractComponentType {

	public static final String TSPLUGIN_XML_FILE = "ts-plugin.xml";

	private static final TigerstripeGeneratorCSpecBuilder builder = new TigerstripeGeneratorCSpecBuilder();

	public IResolutionBuilder getResolutionBuilder(IComponentReader reader, IProgressMonitor monitor) throws CoreException {

		MonitorUtils.complete(monitor);
		return builder;
	}

}