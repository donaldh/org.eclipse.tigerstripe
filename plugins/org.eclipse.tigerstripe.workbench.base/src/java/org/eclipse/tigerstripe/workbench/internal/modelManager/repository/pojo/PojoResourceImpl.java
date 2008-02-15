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
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.reader.ArtifactReaderFactory;
import org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.writer.AbstractArtifactWriter;
import org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.writer.ArtifactWriterFactory;

public class PojoResourceImpl extends ResourceImpl implements Resource {

	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options)
			throws IOException {
		InputStreamReader reader = new InputStreamReader(inputStream);
		try {
			IAbstractArtifact artifact = ArtifactReaderFactory.INSTANCE.read(
					reader, new NullProgressMonitor());
			getContents().add(artifact);
		} catch (TigerstripeException e) {
			throw new IOException(e);
		} finally {
			reader.close();
		}
	}

	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options)
			throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		IAbstractArtifact art = (IAbstractArtifact) getContents().get(0);
		try {
			AbstractArtifactWriter artWriter = ArtifactWriterFactory.INSTANCE
					.getWriter(art.getClass());
			artWriter.setArtifact(art);
			artWriter.setWriter(writer);
			artWriter.applyTemplate();
		} catch (TigerstripeException e) {
			throw new IOException(e);
		} finally {
			writer.close();
		}
	}

	@Override
	protected void doUnload() {
		super.doUnload();
	}

}
