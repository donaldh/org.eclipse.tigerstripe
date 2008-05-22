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
package org.eclipse.tigerstripe.repository.metamodel.pojo.reader;

import java.io.Reader;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.parser.ParseException;

public class ArtifactReaderFactory {

	public final static ArtifactReaderFactory INSTANCE = new ArtifactReaderFactory();

	private String[] keyTags = { ManagedEntityArtifactReader.MARKING_TAG,
			DatatypeArtifactReader.MARKING_TAG,
			ExceptionArtifactReader.MARKING_TAG,
			EnumArtifactReader.MARKING_TAG,
			AssociationArtifactReader.MARKING_TAG,
			AssociationClassArtifactReader.MARKING_TAG,
			EventArtifactReader.MARKING_TAG, QueryArtifactReader.MARKING_TAG,
			SessionArtifactReader.MARKING_TAG,
			PrimitiveTypeArtifactReader.MARKING_TAG,
			UpdateProcedureArtifactReader.MARKING_TAG,
			DependencyArtifactReader.MARKING_TAG };

	@SuppressWarnings("unchecked")
	private Class[] readers = { ManagedEntityArtifactReader.class,
			DatatypeArtifactReader.class, ExceptionArtifactReader.class,
			EnumArtifactReader.class, AssociationArtifactReader.class,
			AssociationClassArtifactReader.class, EventArtifactReader.class,
			QueryArtifactReader.class, SessionArtifactReader.class,
			PrimitiveTypeArtifactReader.class,
			UpdateProcedureArtifactReader.class, DependencyArtifactReader.class };

	private ArtifactReaderFactory() {
		registerSupportedArtifactType();
	}

	private void registerSupportedArtifactType() {

	}

	public IAbstractArtifact read(Reader reader, IProgressMonitor monitor)
			throws ModelCoreException {
		try {
			JavaDocBuilder builder = new JavaDocBuilder();
			JavaSource source = builder.addSource(reader);
			return readArtifact(source, monitor);
		} catch (ParseException e) {
			throw new ModelCoreException("Error trying to parse Artifact", e);
		}
	}

	protected IAbstractArtifact readArtifact(JavaSource source,
			IProgressMonitor monitor) throws ModelCoreException {
		int index = -1;
		for (String keyTag : keyTags) {
			index++;
			JavaClass[] classes = source.getClasses();
			for (int j = 0; j < classes.length; j++) {
				DocletTag[] tags = classes[j].getTags();
				for (int k = 0; k < tags.length; k++) {
					if (tags[k].getName().equals(keyTag)) {
						try {
							ArtifactReader reader = (ArtifactReader) readers[index]
									.newInstance();
							reader.setClass(classes[j]);
							return reader.readArtifact();
						} catch (IllegalAccessException e) {
							throw new ModelCoreException("While reading", e);
						} catch (InstantiationException e) {
							throw new ModelCoreException("While reading", e);
						}
					}
				}
			}
		}
		throw new ModelCoreException("Couldn't read " + source);
	}

}
