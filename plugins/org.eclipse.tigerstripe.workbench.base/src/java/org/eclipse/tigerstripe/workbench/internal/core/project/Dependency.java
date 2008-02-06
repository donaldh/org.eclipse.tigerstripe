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
package org.eclipse.tigerstripe.workbench.internal.core.project;

import java.io.File;
import java.io.IOException;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ModuleProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.internal.api.modules.ITigerstripeModuleProject;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.module.InvalidModuleException;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleRef;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleRefFactory;
import org.eclipse.tigerstripe.workbench.internal.core.util.FileUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * A dependency as declared in a Project Definition
 * 
 * @author Eric Dillon
 * 
 */
public class Dependency implements IDependency {

	private boolean validated = false;

	private String path;

	private ModuleRef moduleRef;

	private boolean isValid = false;

	private TigerstripeProject project;

	public Dependency(TigerstripeProject project, String relativePath) {
		setProject(project);
		setPath(relativePath);
	}

	public Dependency(String absolutePath) {
		setPath(absolutePath);
	}

	private void setProject(TigerstripeProject project) {
		this.project = project;
	}

	protected TigerstripeProject getProject() {
		return project;
	}

	public boolean isValid() {
		return isValid(new TigerstripeNullProgressMonitor());
	}

	public boolean isValid(ITigerstripeProgressMonitor monitor) {

		if (monitor == null) {
			monitor = new TigerstripeNullProgressMonitor();
		}

		try {
			validate(monitor, null);
			isValid = true;
		} catch (InvalidModuleException e) {
			isValid = false;
		}
		return isValid;
	}

	// =============================================================
	// Since 2.1: Module generation
	/**
	 * 
	 * @param containingProject -
	 *            the project that contains the module
	 */
	public ITigerstripeModuleProject makeModuleProject(
			ITigerstripeProject containingProject) throws TigerstripeException {
		ITigerstripeModuleProject result = new ModuleProjectHandle(
				containingProject.getLocation().toFile().toURI(), this);
		return result;
	}

	// =============================================================
	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public ModuleRef getModuleRef() {
		return this.moduleRef;
	}

	// =============================================================

	/**
	 * Validates the dependency by trying to load the corresponding Module
	 * 
	 * If the load is successful, a model is created for this module.
	 * 
	 */
	public void validate(ITigerstripeProgressMonitor monitor,
			TigerstripeProjectVisitor visitor) throws InvalidModuleException {

		if (validated)
			return;

		String absPath = path;

		if (getProject() != null) {
			absPath = getProject().getBaseDir().getAbsolutePath()
					+ File.separator + path;
		}
		// String absPath = getProject().getBaseDir().getAbsolutePath()
		// + File.separator + path;
		File jarFile = new File(absPath);

		if (!jarFile.exists())
			throw new InvalidModuleException("Cannot find module '" + absPath
					+ "'.");

		ModuleRefFactory factory = ModuleRefFactory.getInstance();
		moduleRef = factory.parseModule(jarFile.toURI(), monitor);

		isValid = moduleRef.isValid();
		validated = true;
	}

	public IModuleHeader parseIModuleHeader() throws InvalidModuleException {
		if (validated && moduleRef != null)
			return moduleRef.getModuleHeader();

		File jarFile = checkModuleExists();

		ModuleRefFactory factory = ModuleRefFactory.getInstance();
		return factory.parseModuleHeader(jarFile.toURI(),
				new TigerstripeNullProgressMonitor());
	}

	private File checkModuleExists() throws InvalidModuleException {
		String absPath = path;

		if (getProject() != null) {
			absPath = getProject().getBaseDir().getAbsolutePath()
					+ File.separator + path;
		}
		// String absPath = getProject().getBaseDir().getAbsolutePath()
		// + File.separator + path;
		File jarFile = new File(absPath);

		if (!jarFile.exists())
			throw new InvalidModuleException("Cannot find module '" + absPath
					+ "'.");

		return jarFile;
	}

	public IProjectDetails parseIProjectDetails() throws InvalidModuleException {
		if (validated && moduleRef != null)
			return moduleRef.getProjectDetails();

		File jarFile = checkModuleExists();

		ModuleRefFactory factory = ModuleRefFactory.getInstance();
		return factory.parseModuleDetails(jarFile.toURI(),
				new TigerstripeNullProgressMonitor());
	}

	public ArtifactManager getArtifactManager(
			ITigerstripeProgressMonitor monitor) {
		if (isValid(monitor)) {
			if (moduleRef.getArtifactManager().getTSProject() == null)
				((ModuleArtifactManager) moduleRef.getArtifactManager())
						.setEmbeddedProject(moduleRef.getEmbeddedProject());
			return moduleRef.getArtifactManager();
		} else
			return null;
	}

	public IProjectDetails getIProjectDetails() {
		if (isValid())
			return moduleRef.getProjectDetails();
		else
			return null;
	}

	public IModuleHeader getIModuleHeader() {
		if (isValid())
			return moduleRef.getModuleHeader();
		else
			return null;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Dependency) {
			Dependency dep = (Dependency) arg0;

			return (getPath() == null && dep.getPath() == null)
					|| getPath().equals(dep.getPath());
		} else
			return false;
	}

	// Core dependencies don't exist anymore @see #299
	// /**
	// * Returns the default CoreModel Dependency as shipped in this version. If
	// * none was found, returns null
	// *
	// * @return the default core model dependency from the install dir, or null
	// * is none found.
	// * @since 1.0.3
	// */
	// public static IDependency getDefaultCoreModelDependency() {
	// ExternalModules extM = ExternalModules.getInstance();
	// IDependency[] baseDeps = extM.getShippedBaseModules();
	//
	// for (IDependency dep : baseDeps) {
	// if (DEFAULT_CORE_MODEL_DEPENDENCY.equals(dep.getIModuleHeader()
	// .getModuleID())) {
	// return dep;
	// }
	// }
	// return null;
	// }
	//
	public static IDependency copyToAsLocalDependency(
			TigerstripeProject tsProject, IDependency remoteDep)
			throws TigerstripeException {

		File copyDir = tsProject.getBaseDir();
		File srcDep = new File(remoteDep.getPath());
		// Copy the modules into the project folder
		try {
			FileUtils.copy(srcDep.getAbsolutePath(), copyDir.getAbsolutePath(),
					true);
		} catch (IOException e) {
			throw new TigerstripeException("Error while copying dependency: "
					+ e.getMessage(), e);
		}
		IDependency result = new Dependency(tsProject, srcDep.getName());
		return result;
	}
}
