/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.api.project.INameProvider;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

public class NameProviderImpl implements INameProvider {

	private HashMap<Class, Integer> indexMap = null;

	private ITigerstripeModelProject project;

	public NameProviderImpl(ITigerstripeModelProject project) {
		this.project = project;
	}

	private void initialize() throws TigerstripeException {
		indexMap = new HashMap<Class, Integer>();
		Collection<Class> supportedArtifacts = project
				.getArtifactManagerSession().getSupportedArtifactClasses();
		for (Class supportedArtifact : supportedArtifacts) {
			indexMap.put(supportedArtifact, new Integer(0));
		}
	}

	public String getUniqueName(Class artifactType, String packageName)
			throws TigerstripeException {
		return getUniqueName(artifactType, packageName, false);
	}

	public String getUniqueName(Class artifactType, String packageName,
			boolean forceIncrement) throws TigerstripeException {
		IArtifactManagerSession session = project.getArtifactManagerSession();

		if (indexMap == null) {
			initialize();
		}

		Integer targetCounter = indexMap.get(artifactType);
		if (targetCounter == null)
			throw new TigerstripeException("Artifact type unknown: "
					+ artifactType.getName());
		String labelStr = Misc
				.removeIllegalCharacters(ArtifactMetadataFactory.INSTANCE
						.getMetadata(
								MigrationHelper
										.artifactMetadataMigrateClassname(artifactType
												.getName())).getLabel());

		if (forceIncrement) {
			targetCounter++;
			indexMap.put(artifactType, targetCounter);
		}

		String tentativeFQN = labelStr + targetCounter;
		if (packageName != null && packageName.length() != 0) {
			tentativeFQN = packageName + "." + tentativeFQN;
		}

		IArtifactQuery allArts = session.makeQuery(IQueryAllArtifacts.class
				.getName());
		allArts.setIncludeDependencies(false);

		Collection<IAbstractArtifact> artifacts = session
				.queryArtifact(allArts);

		while (existsInModelIgnoreCase(artifacts, tentativeFQN)) {
			targetCounter++;
			tentativeFQN = labelStr + targetCounter;
			if (packageName != null && packageName.length() != 0) {
				tentativeFQN = packageName + "." + tentativeFQN;
			}
		}

		return labelStr + targetCounter;
	}

	private boolean existsInModelIgnoreCase(
			Collection<IAbstractArtifact> artifacts, String fqn) {
		for (IAbstractArtifact artifact : artifacts) {
			if (fqn.equalsIgnoreCase(artifact.getFullyQualifiedName()))
				return true;
		}
		return false;
	}
}
