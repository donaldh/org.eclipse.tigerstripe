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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.modelManager.ModelManager;
import org.eclipse.tigerstripe.workbench.internal.modelManager.ModelRepository;

public class PojoModelRepository extends ModelRepository {

	private final static String POJO_EXTENSION = "java";
	private final static URI BASE_POJO_URI = URI.createURI("pojo:///");

	private Map<String, Resource> resourceMap = new HashMap<String, Resource>();
	private URIConverter converter = new URIConverterImpl();

	public PojoModelRepository(URI repository, ModelManager manager) {
		super(repository, manager);
		converter.getURIMap().put(BASE_POJO_URI, repository);
	}

	private final class AllPojosVisitor implements IResourceVisitor {

		private List<IResource> allMembers = new ArrayList<IResource>();

		@Override
		public boolean visit(IResource resource) throws CoreException {
			if (resource instanceof IFolder)
				return true;
			else {
				if (POJO_EXTENSION.equals(resource.getFileExtension()))
					allMembers.add(resource);
				return false;
			}
		}
	}

	@Override
	protected void loadResourceSet() throws TigerstripeException {
		String platformString = getRepositoryURI().toPlatformString(true);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IResource repositoryRes = root.findMember(platformString);
		AllPojosVisitor visitor = new AllPojosVisitor();
		try {
			repositoryRes.accept(visitor);
		} catch (CoreException e) {
			throw new TigerstripeException("While loading resourceSet: "
					+ e.getMessage(), e);
		}

		for (IResource pojo : visitor.allMembers) {
			Resource res = getResourceSet().createResource(
					URI.createFileURI(pojo.getFullPath().toOSString()));
			try {
				res.load(null);
				IAbstractArtifact art = (IAbstractArtifact) res.getContents()
						.get(0);
				resourceMap.put(art.getFullyQualifiedName(), res);
			} catch (IOException e) {
				throw new TigerstripeException("while loading pojo: "
						+ e.getMessage(), e);
			}
		}
	}

	@Override
	public IAbstractArtifact store(IAbstractArtifact workingCopy, boolean force)
			throws TigerstripeException {
		String fqn = workingCopy.getFullyQualifiedName();
		Resource targetResource = resourceMap.get(fqn);
		if (targetResource == null) {
			targetResource = createPojoResource(workingCopy);
			resourceMap.put(fqn, targetResource);
			EObject original = EcoreUtil.copy(workingCopy);
			targetResource.getContents().add(original);
		}

		try {
			if (!targetResource.isLoaded())
				targetResource.load(null);
			IAbstractArtifact original = (IAbstractArtifact) targetResource
					.getContents().get(0);
			applyChanges(original, workingCopy);

			targetResource.save(null);
		} catch (IOException e) {
			throw new TigerstripeException("while trying to store: " + e);
		}
		return workingCopy;
	}

	private Resource createPojoResource(IAbstractArtifact workingCopy)
			throws TigerstripeException {
		String pojoPath = workingCopy.getPackage().replace('.', '/');
		String pojoName = workingCopy.getName() + "." + POJO_EXTENSION;
		URI resourceURI = URI.createURI("pojo:///" + pojoPath + "/" + pojoName);
		Resource res = getResourceSet().createResource(
				converter.normalize(resourceURI));
		return res;
	}

	private void applyChanges(IAbstractArtifact original,
			IAbstractArtifact workingCopy) {
		EClass e = original.eClass();
		for (EStructuralFeature o : e.getEAllStructuralFeatures()) {
			if (o instanceof EAttribute) {
				original.eSet(o, workingCopy.eGet(o));
			} else if (o instanceof EReference) {
				// FIXME: todo
			}
		}

	}

	@Override
	public Collection<IAbstractArtifact> getAllArtifacts() {
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
		for (Resource resource : getResourceSet().getResources()) {
			for (EObject o : resource.getContents()) {
				result.add((IAbstractArtifact) o);
			}
		}
		return result;
	}

	@Override
	public IAbstractArtifact getArtifactByFullyQualifiedName(
			String fullyQualifiedName) {

		if (fullyQualifiedName == null || fullyQualifiedName.length() == 0)
			return null;

		for (IAbstractArtifact artifact : getAllArtifacts()) {
			if (fullyQualifiedName.equals(artifact.getFullyQualifiedName()))
				return artifact;
		}
		return null;
	}

	@Override
	public boolean isLocal() {
		return true;
	}

	public void refresh() {
		try {
			loadResourceSet();
			System.out.println("" + resourceMap);
		} catch (TigerstripeException e) {
			e.printStackTrace();
		}
	}
}
