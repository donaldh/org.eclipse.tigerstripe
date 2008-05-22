package org.eclipse.tigerstripe.repository.metamodel.pojo.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.repository.metamodel.pojo.PojoResourceImpl;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;

/**
 * This class allows to find all out of sync Pojos from the repository. It
 * creates 3 lists:
 * <ul>
 * <li> newPojos: a list of all resources on disk that have no corresponding
 * resource in the repository.</li>
 * <li> pojosToRefresh: a list of all resources that are newer on disk than the
 * version we currently have in memory.
 * <li> deletedPojos: a list of all the pojos that are in the repository but not
 * on disk anymore.
 * </ul>
 * 
 * @author erdillon
 * 
 */
public class AllOutOfSyncPojosVisitor implements IResourceVisitor {

	private boolean initialized = false;
	private boolean finalized = false;

	private final MultiFileArtifactRepository repository;

	private List<IResource> newPojos = new ArrayList<IResource>();
	private List<URI> deletedPojos = new ArrayList<URI>();
	private List<IResource> pojosToRefresh = new ArrayList<IResource>();

	private List<URI> knownURIs = new ArrayList<URI>();

	public AllOutOfSyncPojosVisitor(MultiFileArtifactRepository repository) {
		this.repository = repository;
	}

	/**
	 * Builds up a list of all known resources so we can remove from it as we go
	 * and figure out those that disappeared from disk.
	 * 
	 */
	protected void initialize() {
		knownURIs.clear();
		pojosToRefresh.clear();
		newPojos.clear();
		deletedPojos.clear();

		for (Resource res : repository.getResources()) {
			knownURIs.add(res.getURI());
		}

		initialized = true;
	}

	public boolean visit(IResource resource) throws CoreException {

		if (!initialized) {
			initialize();
		}

		if (resource instanceof IFolder)
			return true;
		else {
			if (MultiFileArtifactRepository.POJO_EXTENSION.equals(resource
					.getFileExtension())) {
				handlePojo(resource);
				return false;
			}
		}

		return false;
	}

	/**
	 * This method is meant to be called after the traversal is completed to
	 * gather results before the
	 */
	protected void finalize() {
		// Go over all the known URIs that haven't been touched
		// These need to be removed from the repository as the corresponding
		// files don't exist anymore
		for (URI uri : knownURIs) {
			deletedPojos.add(uri);
		}

		finalized = true;
	}

	protected void handlePojo(IResource resource) {
		URI uri = URI.createPlatformResourceURI(resource.getFullPath()
				.toString(), true);

		if (knownURIs.remove(uri)) {
			// Ok, so this was a known URI, check TStamp now
			long tStamp = resource.getModificationStamp();
			PojoResourceImpl correspondingResource = (PojoResourceImpl) repository
					.getURIResourceMap().get(uri);

			if (correspondingResource.getStamp() != tStamp) {
				pojosToRefresh.add(resource);
			} else {
				// nothing to do about this one!, we're up2date
			}
		} else {
			// Ok, this was not a known URI.
			newPojos.add(resource);
		}
	}

	public List<IResource> getNewPojos() {
		return newPojos;
	}

	public List<URI> getDeletedPojos() {
		if (!finalized)
			finalize();

		return deletedPojos;
	}

	public List<IResource> getPojosToRefresh() {
		return pojosToRefresh;
	}

	public void reset() {
		initialized = false;
		finalized = false;
	}

	public MultiFileArtifactRepository getRepository() {
		return this.repository;
	}
}