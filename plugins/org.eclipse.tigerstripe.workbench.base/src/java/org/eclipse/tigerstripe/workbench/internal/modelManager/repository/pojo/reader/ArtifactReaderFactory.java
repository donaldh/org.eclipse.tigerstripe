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
package org.eclipse.tigerstripe.workbench.internal.modelManager.repository.pojo.reader;

import java.io.Reader;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifactTag;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.parser.ParseException;

public class ArtifactReaderFactory {

	public final static ArtifactReaderFactory INSTANCE = new ArtifactReaderFactory();

	private String[] keyTags = { AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.MANAGEDENTITY

	};

	private Class[] readers = { ManagedEntityArtifactReader.class };

	private ArtifactReaderFactory() {
		registerSupportedArtifactType();
	}

	private void registerSupportedArtifactType() {

	}

	public IAbstractArtifact read(
			Reader reader, IProgressMonitor monitor)
			throws TigerstripeException {
		try {
			JavaDocBuilder builder = new JavaDocBuilder();
			JavaSource source = builder.addSource(reader);
			return readArtifact(source, monitor);
		} catch (ParseException e) {
			throw new TigerstripeException("Error trying to parse Artifact", e);
		}
	}

	protected IAbstractArtifact readArtifact(JavaSource source,
			IProgressMonitor monitor) throws TigerstripeException {
		for (String keyTag : keyTags) {
			JavaClass[] classes = source.getClasses();
			for (int j = 0; j < classes.length; j++) {
				DocletTag[] tags = classes[j].getTags();
				for (int k = 0; k < tags.length; k++) {
					if (tags[k].getName().equals(keyTag)) {
						try {
							ArtifactReader reader = (ArtifactReader) readers[k]
									.newInstance();
							reader.setClass(classes[j]);
							return reader.readArtifact();
						} catch (IllegalAccessException e) {
							throw new TigerstripeException("While reading", e);
						} catch (InstantiationException e) {
							throw new TigerstripeException("While reading", e);
						}
					}
				}
			}
		}
		throw new TigerstripeException("Couldn't read " + source);
	}

}
