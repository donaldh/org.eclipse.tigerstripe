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
package org.eclipse.tigerstripe.repository.metamodel.pojo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;
import org.eclipse.tigerstripe.repository.metamodel.pojo.reader.ArtifactReaderFactory;
import org.eclipse.tigerstripe.repository.metamodel.pojo.writer.AbstractArtifactWriter;
import org.eclipse.tigerstripe.repository.metamodel.pojo.writer.ArtifactWriterFactory;

public class PojoResourceImpl extends ResourceImpl implements Resource {

	private long stamp = -1;

	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options)
			throws IOException {
		InputStreamReader reader = new InputStreamReader(inputStream);
		try {
			IAbstractArtifact artifact = ArtifactReaderFactory.INSTANCE.read(
					reader, new NullProgressMonitor());
			getContents().add(artifact);
		} catch (ModelCoreException e) {
			throw new IOException(e.getMessage());
		} finally {
			reader.close();
		}
	}

	@Override
	public void load(Map<?, ?> options) throws IOException {
		super.load(options);
		setStamp();
	}

	@Override
	public void save(Map<?, ?> options) throws IOException {
		super.save(options);
		setStamp();
	}

	private void setStamp() {
		URI uri = getURI();
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
				uri.toPlatformString(true));
		stamp = res.getModificationStamp();
	}

	public long getStamp() {
		return this.stamp;
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
		} catch (ModelCoreException e) {
			throw new IOException(e.getMessage());
		} finally {
			writer.close();
		}
	}

	@Override
	protected void doUnload() {
		super.doUnload();
	}
}
