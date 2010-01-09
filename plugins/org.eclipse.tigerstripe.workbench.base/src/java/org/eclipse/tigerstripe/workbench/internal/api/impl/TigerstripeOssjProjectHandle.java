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

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModulePackager;
import org.eclipse.tigerstripe.workbench.internal.core.module.packaging.ModulePackager;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TigerstripeOssjProjectHandle extends TigerstripeProjectHandle {

	// TODO: this should be removed and merged back into
	// TigerstripeProjectHandle?

	/**
	 * 
	 * @param projectURI
	 *            - URI of the project directory
	 */
	public TigerstripeOssjProjectHandle(URI projectURI)
			throws TigerstripeException {
		super(projectURI);
	}

	public IModulePackager getPackager() {
		return new ModulePackager(this);
	}

	@Override
	public WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		TigerstripeProjectHandle copy = new TigerstripeOssjProjectHandle(
				getURI());
		return copy;
	}

	@Override
	public boolean isDirty() {
		try {
			return getTSProject().isDirty();
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject#
	 * getReferencingModels()
	 */
	public ModelReference[] getReferencingModels(int level)
			throws TigerstripeException {
		// This is an expensive method?
		List<ModelReference> result = new ArrayList<ModelReference>();

		if (level == 0)
			return result.toArray(new ModelReference[result.size()]);

		try {
			ModelReference selfRef = ModelReference.referenceFromProject(this);
			ITigerstripeModelProject[] projects = TigerstripeCore
					.allModelProjects();
			for (ITigerstripeModelProject project : projects) {
				for (ModelReference ref : project.getModelReferences()) {
					if (ref.equals(selfRef)) {
						result
								.add(ModelReference
										.referenceFromProject(project));
						if (level == ModelReference.INFINITE_LEVEL) {
							result
									.addAll(Arrays
											.asList(project
													.getReferencingModels(ModelReference.INFINITE_LEVEL)));
						} else if (level > 1) {
							result.addAll(Arrays.asList(project
									.getReferencingModels(level - 1)));
						}
					}
				}
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return result.toArray(new ModelReference[result.size()]);
	}
}
