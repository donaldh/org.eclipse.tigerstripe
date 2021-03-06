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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.io.File;
import java.io.Writer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.metamodel.impl.IPackageImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.PackageArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ide.IDE;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 */
public class PackageArtifact extends AbstractArtifact implements
		IPackageArtifact, IAbstractArtifactInternal {

	private String _artifactPath = null;

	/**
	 * This allows for a create "behind-the-scenes" of any packageArtifact that
	 * is missing when we try to open or access a package.
	 * 
	 * @param packageName
	 */
	public static IPackageArtifact makeArtifactForPackage(
			IArtifactManagerSession mgr, String packageName)
			throws TigerstripeException {
		// Split the "proper" package name up into
		// a package and name
		String name = "";
		String falsePackageName = "";
		if (packageName.contains(".")) {
			name = packageName.substring(packageName.lastIndexOf(".") + 1);
			falsePackageName = packageName.substring(0, packageName
					.lastIndexOf("."));
		} else {
			name = packageName;
			falsePackageName = "";
		}

		IModelUpdater updater = mgr.getIModelUpdater();

		IArtifactDeleteRequest deleteRequest = null;
		IArtifactCreateRequest createRequest = null;
		try {
			deleteRequest = (IArtifactDeleteRequest) updater
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ARTIFACT_DELETE);
			deleteRequest.setArtifactName(name);
			deleteRequest.setArtifactPackage(falsePackageName);

			updater.handleChangeRequest(deleteRequest);

			createRequest = (IArtifactCreateRequest) updater
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ARTIFACT_CREATE);
			createRequest.setArtifactType(IPackageArtifact.class.getName());
			createRequest.setArtifactName(name);
			createRequest.setArtifactPackage(falsePackageName);

			updater.handleChangeRequest(createRequest);

			return (IPackageArtifact) mgr
					.getArtifactByFullyQualifiedName(packageName);

		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return null;
		}

	}

	/**
	 * This just makes one without creating the .package file It is NOT added to
	 * the ArtifactManager - WHY NOT?
	 * 
	 * @param mgr
	 * @param packageName
	 * @return
	 * @throws TigerstripeException
	 */
	public static PackageArtifact makeVolatileArtifactForPackage(
			IArtifactManagerSession mgr, String packageName)
			throws TigerstripeException {
		// Split the "proper" package name up into
		// a package and name
		String name = "";
		String falsePackageName = "";
		if (packageName.contains(".")) {
			name = packageName.substring(packageName.lastIndexOf(".") + 1);
			falsePackageName = packageName.substring(0, packageName
					.lastIndexOf("."));
		} else {
			name = packageName;
			falsePackageName = "";
		}

		PackageArtifact newArtifact = (PackageArtifact) mgr
				.makeArtifact(IPackageArtifact.class.getName());

		newArtifact.setName(name);
		newArtifact.setPackage(falsePackageName);

		return newArtifact;
	}

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.PACKAGE;

	public String getArtifactType() {
		return IPackageArtifact.class.getName();
	}

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static PackageArtifact MODEL = new PackageArtifact(null);

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	@Override
	public String getMarkingTag() {
		return PackageArtifact.MARKING_TAG;
	}

	@Override
	public IAbstractArtifactInternal getModel() {
		return MODEL;
	}

	public String getLabel() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
		// TODO - is this correct in the metamodel ?
				IPackageImpl.class.getName()).getLabel(this);
	}

	@Override
	public IAbstractArtifactInternal extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		PackageArtifact result = new PackageArtifact(javaClass, artifactMgr,
				monitor);

		return result;
	}

	public PackageArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);
		setIStandardSpecifics(new StandardSpecifics(this));
	}

	public PackageArtifact(JavaClass javaClass, ArtifactManager artifactMgr,
			IProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);
		StandardSpecifics specifics = new StandardSpecifics(this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new PackageArtifactPersister(this, writer);
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new PackageArtifact(getArtifactManager());
	}

	/**
	 * Returns true if this artifact extends another artifact
	 * 
	 * @return this is always false for a package
	 * 
	 */
	@Override
	public boolean hasExtends() {
		return false;
	}

	/**
	 * Returns the artifact path relative to the project directory
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	@Override
	public String getArtifactPath() throws TigerstripeException {
		if (_artifactPath == null)
			updateArtifactPath();

		return _artifactPath;
	}

	@Override
	protected void updateArtifactPath() {

		// Determine the path for this artifact
		// This will be the package PLUS the extension
		String packageName = getPackage().replace('.', File.separatorChar);

		if (getTSProject() == null || getTSProject().getBaseDir() == null) {
			_artifactPath = null; // this is part of a module
			return;
		}

		String baseDir = getTSProject().getBaseDir().toString();

		String repoLocation = "";
		try {
			repoLocation = getTSProject().getRepositoryLocation();
		} catch (Exception e) {

		}

		String artifactPath = repoLocation + File.separator + packageName
				+ File.separator + getName() + File.separator + ".package";

		_artifactPath = artifactPath;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter == IResource.class) {
			try {
				IResource res = getIResource();
				return res;
			} catch (TigerstripeException e) {
				// Do nothing
			}
		} else if (adapter == IJavaElement.class) {
			try {
				IResource res = getIResource();
				return JavaCore.create(res);
			} catch (TigerstripeException e) {
				// Do nothing
			}
		}

		return super.getAdapter(adapter);
	}

	/**
	 * Returns the IResource that this Artifact is saved in
	 * 
	 * This relies on the fact that each artifact is a pojo. This will need to
	 * be updated as we migrate to EMF.
	 * 
	 * @return
	 */
	private IResource getIResource() throws TigerstripeException {
		String artifactPath = getArtifactPath();

		if (artifactPath == null)
			throw new TigerstripeException("Unknown path for "
					+ getFullyQualifiedName()); // this happens for
		// artifacts in modules.

		IProject iProject = (IProject) getProject().getAdapter(IProject.class);
		if (iProject == null)
			// This will happen when considering artifact from Phantom Project
			throw new TigerstripeException("Unknown path for "
					+ getFullyQualifiedName());
		return iProject.findMember(artifactPath);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) throws TigerstripeException {
		super.doSave(monitor);
		
		IResource resource = (IResource) this.getAdapter(IResource.class);
		final String EDITOR_ID = "org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.packageArtifact.PackageArtifactEditor";
		if (resource instanceof IFile ) {
			IFile file = (IFile) resource;
			IEditorDescriptor editorDescriptor = IDE.getDefaultEditor(file);
			if (editorDescriptor==null || (!EDITOR_ID.equals(editorDescriptor))) 
				IDE.setDefaultEditor((IFile)resource, EDITOR_ID);
		}
	}

}