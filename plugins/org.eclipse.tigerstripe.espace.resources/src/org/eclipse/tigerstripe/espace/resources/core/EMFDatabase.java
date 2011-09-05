/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.espace.resources.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.tigerstripe.espace.core.IEMFDatabase;
import org.eclipse.tigerstripe.espace.core.Mode;
import org.eclipse.tigerstripe.espace.resources.DeferredResourceSaver;
import org.eclipse.tigerstripe.espace.resources.IResourceSaver;
import org.eclipse.tigerstripe.espace.resources.IResourceTimestampManager;
import org.eclipse.tigerstripe.espace.resources.ResourceHelper;
import org.eclipse.tigerstripe.espace.resources.ResourceLocation;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;
import org.eclipse.tigerstripe.espace.resources.internal.core.ClassifierIndexer;
import org.eclipse.tigerstripe.espace.resources.internal.core.CompositeIndexer;
import org.eclipse.tigerstripe.espace.resources.internal.core.FeatureIndexer;
import org.eclipse.tigerstripe.espace.resources.internal.core.IndexStorage;
import org.eclipse.tigerstripe.espace.resources.internal.core.ResourceStorage;

/**
 * @author Yuri Strot
 * 
 */
public class EMFDatabase implements IEMFDatabase, IResourceTimestampManager {

	private static final String ROUTER_EXTPT = "org.eclipse.tigerstripe.annotation.core.router";
	private static final String ROUTER_ATTR_CLASS = "class";

	private static final String ANNOTATION_MARKER = "org.eclipse.tigerstripe.annotation";
	private static final String ANNOTATION_ID = "id";

	private EObjectRouter[] routers;
	private ResourceSet resourceSet;
	private ResourceHelper resourceHelper;

	private IndexStorage indexStorage;
	private FeatureIndexer fIndexer;
	private ClassifierIndexer cIndexer;
	private CompositeIndexer indexer;

	private ResourceStorage resourceStorage;
	private IDatabaseConfiguration idManager;

	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	public EMFDatabase(IDatabaseConfiguration idManager) {
		this.idManager = idManager;
		DeferredResourceSaver.getInstance().setTimestampManager(this);
		init();
	}

	protected void init() {
		resourceSet = new ResourceSetImpl();
		initResources();
		initRouters();
	}

	protected void initResources() {
		indexStorage = new IndexStorage(resourceSet);
		fIndexer = new FeatureIndexer(indexStorage);
		cIndexer = new ClassifierIndexer(indexStorage);

		indexer = new CompositeIndexer();
		indexer.addIndexer(fIndexer);
		indexer.addIndexer(cIndexer);

		resourceHelper = new ResourceHelper(indexer, resourceSet);
		resourceStorage = new ResourceStorage(resourceHelper);
	}

	protected void initRouters() {
		IConfigurationElement[] configs = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(ROUTER_EXTPT);
		List<EObjectRouter> routers = new ArrayList<EObjectRouter>();
		for (IConfigurationElement config : configs) {
			try {
				EObjectRouter provider = (EObjectRouter) config
						.createExecutableExtension(ROUTER_ATTR_CLASS);
				routers.add(provider);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		this.routers = routers.toArray(new EObjectRouter[routers.size()]);
	}

	protected void clear() {
		initResources();
	}

	protected ResourceSet getResourceSet() {
		return resourceSet;
	}

	protected IndexStorage getIndexStorage() {
		return indexStorage;
	}

	protected FeatureIndexer getFeatureIndexer() {
		return fIndexer;
	}

	protected ClassifierIndexer getClassifierIndexer() {
		return cIndexer;
	}

	protected CompositeIndexer getIndexer() {
		return indexer;
	}

	protected ResourceHelper getResourceHelper() {
		return resourceHelper;
	}

	protected ResourceStorage getResourceStorage() {
		return resourceStorage;
	}

	protected EObjectRouter[] getRouters() {
		return routers;
	}

	private void lockChanges(boolean write) {
		if (write)
			rwl.writeLock().lock();
		else
			rwl.readLock().lock();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.espace.resources.IResourceTimespampManager#
	 * updateTimestamps(java.lang.Runnable,
	 * org.eclipse.emf.ecore.resource.Resource[])
	 */
	public void updateTimestamps(IResourceSaver saver) {
		try {
			lockChanges(true);
			Resource[] resources = saver.saveResources();
			for (Resource resource : resources) {
				getResourceStorage().addResource(resource, Mode.READ_WRITE);
			}
		} finally {
			unlockChanges(true);
		}
	}

	private void unlockChanges(boolean write) {
		if (write) {
			rwl.writeLock().unlock();
		} else
			rwl.readLock().unlock();
	}

	private boolean lockAndUpdate(boolean write) {
		boolean resultWriteLock = write;
		if (write)
			rwl.writeLock().lock();
		else
			rwl.readLock().lock();

		boolean needUpdate = getResourceStorage().needUpdate();
		if (needUpdate) {
			if (!write) {
				// Must release read lock before acquiring write lock
				rwl.readLock().unlock();
				rwl.writeLock().lock();
				resultWriteLock = true;
				needUpdate = getResourceStorage().needUpdate();
			}
			if (needUpdate) {
				doRebuildIndex();
				Status status = new Status(IStatus.WARNING,
						ResourcesPlugin.PLUGIN_ID,
						"Index corrupted and was rebuild.");
				ResourcesPlugin.getDefault().getLog().log(status);
			}
		}
		return resultWriteLock;
	}

	public boolean isReadOnly(EObject object) {
		boolean writeLock = false;
		try {
			writeLock = lockAndUpdate(writeLock);
			return doCheckReadOnly(object);
		} finally {
			unlockChanges(writeLock);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.espace.core.IEMFDatabase#update(org.eclipse.emf
	 * .ecore.EObject, org.eclipse.emf.ecore.change.ChangeDescription)
	 */
	public void update(EObject object, ChangeDescription changes) {
		try {
			lockAndUpdate(true);
			changes.applyAndReverse();
			Resource[] resource = new Resource[1];
			boolean readOnly = doRemove(object, resource);
			changes.apply();
			if (!readOnly)
				doWrite(object, resource[0]);
		} finally {
			unlockChanges(true);
		}
	}

	public EObject[] query(EClassifier classifier) {
		boolean writeLock = false;
		try {
			writeLock = lockAndUpdate(writeLock);
			return copy(getClassifierIndexer().read(classifier));
		} finally {
			unlockChanges(writeLock);
		}
	}

	public void write(EObject object) {
		try {
			lockAndUpdate(true);
			createID(object);
			doWrite(object, null);
		} finally {
			unlockChanges(true);
		}
	}

	public void remove(EObject object) {
		try {
			lockAndUpdate(true);
			doRemove(object, null);
		} finally {
			unlockChanges(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.espace.core.IEMFDatabase#get(org.eclipse.emf.
	 * ecore.EStructuralFeature, java.lang.Object)
	 */
	public EObject[] get(EStructuralFeature feature, Object value) {
		boolean writeLock = false;
		try {
			writeLock = lockAndUpdate(writeLock);
			try {
				return copy(doGet(feature, value, false));
			} catch (Exception e) {
				if (!writeLock) {
					rwl.readLock().unlock();
					rwl.writeLock().lock();
					writeLock = true;
				}
				doRebuildIndex();
				return copy(doGet(feature, value, false));
			}
		} finally {
			unlockChanges(writeLock);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.espace.core.IEMFDatabase#getPostfixes(org.eclipse
	 * .emf.ecore.EStructuralFeature, java.lang.Object)
	 */
	public EObject[] getPostfixes(EStructuralFeature feature, Object value) {
		boolean writeLock = false;
		try {
			writeLock = lockAndUpdate(writeLock);
			return copy(doGet(feature, value, true));
		} finally {
			unlockChanges(writeLock);
		}
	}

	public EObject[] getPostfixesRaw(EStructuralFeature feature, Object value) {
		boolean writeLock = false;
		try {
			writeLock = lockAndUpdate(writeLock);
			return doGet(feature, value, true);
		} finally {
			unlockChanges(writeLock);
		}
	}
	
	public EObject[] read() {
		boolean writeLock = false;
		try {
			writeLock = lockAndUpdate(writeLock);
			return doRead();
		} finally {
			unlockChanges(writeLock);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.espace.core.IEMFDatabase#addResource(org.eclipse
	 * .emf.ecore.resource.Resource,
	 * org.eclipse.tigerstripe.espace.core.ReadWriteOption)
	 */
	public void addResource(Resource resource, Mode option) {
		try {
			lockAndUpdate(true);
			if (getResourceStorage().addResource(resource, option)) {
				resource = preProcessResource(resource);
				doRebuildIndex();
			}
		} finally {
			unlockChanges(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.espace.core.IEMFDatabase#removeResource(org.eclipse
	 * .emf.ecore.resource.Resource)
	 */
	public void removeResource(Resource resource, boolean removeFromSavingList) {
		try {
			lockAndUpdate(true);
			resource = preProcessResource(resource);
			if (getResourceStorage().removeResource(resource,
					removeFromSavingList))
				doRebuildIndex();
		} finally {
			unlockChanges(true);
		}
	}

	protected Resource preProcessResource(Resource resource) {
		Resource ownResource = getResourceHelper().getResource(
				resource.getURI(), false);
		if (ownResource != null) {
			ownResource.unload();
			return ownResource;
		}
		return resource;
	}

	public void rebuildIndex() {
		lockChanges(true);
		try {
			doRebuildIndex();
		} finally {
			unlockChanges(true);
		}
	}

	protected void doRebuildIndex() {
		getIndexStorage().removeIndex();
		clear();
		EObject[] objects = doRead();
		try {
			getResourceHelper().rebuildIndex(objects);
		} catch (IOException e) {
			ResourcesPlugin.log(e);
		}
		getResourceStorage().updateTimes();
	}

	protected static String generateID() {
		return EcoreUtil.generateUUID();
	}

	protected void createID(EObject object) {
		List<Resource> resources = new ArrayList<Resource>();
		while (object.eContainer() != null)
			object = object.eContainer();
		idManager.setId(object, generateID());
		if (object.eResource() != null)
			resources.add(object.eResource());
		saveAfterIdChanges(resources);
	}

	protected EObject[] updateIDs(EObject[] objects) {
		List<Resource> resources = new ArrayList<Resource>();
		Map<String, EObject> map = new HashMap<String, EObject>();
		for (int i = 0; i < objects.length; i++) {
			EObject cur = objects[i];
			while (cur.eContainer() != null)
				cur = cur.eContainer();
			String id = idManager.getId(cur);
			if (id == null) {
				id = generateID();
				idManager.setId(cur, id);
				Resource res = cur.eResource();
				if (res != null && !resources.contains(res))
					resources.add(res);
			}
			EObject obj = map.get(id);
			if (obj == null) {
				map.put(id, objects[i]);
			} else {
				Resource resource = objects[i].eResource();
				if (resource != null) {
					ResourceLocation location = getResourceStorage()
							.getLocation(resource);
					if (location != null) {
						if (Mode.READ_WRITE.equals(location.getOption())) {
							map.put(id, objects[i]);
						}
					}
				}
			}
		}
		saveAfterIdChanges(resources);
		return map.values().toArray(new EObject[map.size()]);
	}

	protected void saveAfterIdChanges(List<Resource> resources) {
		if (resources.size() > 0) {
			for (Resource resource : resources) {
				ResourceHelper.save(resource);
			}
		}
	}

	protected EObject[] copy(EObject[] objects) {
		objects = updateIDs(objects);
		return copy(Arrays.asList(objects));
	}

	protected EObject[] copy(Collection<EObject> collection) {
		Collection<EObject> copy = EcoreUtil.copyAll(collection);
		return copy.toArray(new EObject[copy.size()]);
	}

	protected void doWrite(EObject object, Resource resource) {
		object = EcoreUtil.copy(object);
		if (resource == null)
			resource = getResource(object);
		try {
			getResourceStorage().addResource(resource, object);
		} catch (IOException exception) {
			doRebuildIndex();
			try {
				getResourceStorage().addResource(resource, object);
			} catch (IOException e) {
				ResourcesPlugin.log(e);
			}
		}
	}

	protected boolean doCheckReadOnly(EObject object) {
		EStructuralFeature feature = getIDFeature(object);
		if (feature != null) {
			EObject[] objects = doGet(feature, object.eGet(feature), false);
			for (int i = 0; i < objects.length; i++) {
				EObject candidate = objects[i];
				if (EcoreUtil.equals(object, candidate)) {
					Resource resource = candidate.eResource();
					if (resource != null) {
						ResourceLocation location = getResourceStorage()
								.getLocation(resource);
						if (location != null) {
							Mode option = location.getOption();
							if (Mode.READ_ONLY.equals(option))
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	protected boolean doRemove(EObject object, Resource[] oldResource) {
		boolean readOnly = false;
		EStructuralFeature feature = getIDFeature(object);
		if (feature != null) {
			EObject[] objects = doGet(feature, object.eGet(feature), false);
			for (int i = 0; i < objects.length; i++) {
				EObject candidate = objects[i];
				Resource resource = candidate.eResource();
				Mode option = Mode.READ_WRITE;
				if (resource != null) {
					ResourceLocation location = getResourceStorage()
							.getLocation(resource);
					if (location != null) {
						option = location.getOption();
						if (Mode.READ_WRITE.equals(option)
								&& oldResource != null)
							oldResource[0] = resource;
						if (Mode.READ_ONLY.equals(option))
							readOnly = true;
						if (!Mode.READ_WRITE.equals(option))
							continue;
					}
				}
				if (resource == null)
					resource = getResource(object);
				try {
					getResourceStorage().removeAndSave(candidate, resource);
				} catch (IOException exc) {
					doRebuildIndex();
					try {
						// copy-paste bug fixed
						getResourceStorage().removeAndSave(candidate, resource);
					} catch (IOException e) {
						ResourcesPlugin.log(e);
					}
				}
			}
		}
		return readOnly;
	}

	protected EStructuralFeature getIDFeature(EObject object) {
		for (EStructuralFeature feature : object.eClass()
				.getEStructuralFeatures())
			if (isIDFeature(feature) && object.eIsSet(feature))
				return feature;
		return null;
	}

	protected Resource getResource(EObject object) {
		return getResourceHelper().getResource(getUri(object));
	}

	protected URI getUri(EObject object) {
		EObjectRouter[] routers = getRouters();
		for (int i = 0; i < routers.length; i++) {
			EObjectRouter elem = routers[i];
			try {
				URI uri = elem.route(object);
				if (uri != null)
					return uri;
			} catch (Exception e) {
				ResourcesPlugin.log(e);
			}
		}
		return getResourceStorage().defaultRoute(object);
	}

	public boolean isIDFeature(EStructuralFeature feature) {
		EAnnotation annotation = feature.getEAnnotation(ANNOTATION_MARKER);
		if (annotation != null) {
			String value = annotation.getDetails().get(ANNOTATION_ID);
			if (value != null && Boolean.valueOf(value))
				return true;
		}
		return false;
	}

	protected EObject[] doGet(EStructuralFeature feature, Object value,
			boolean postfixes) {
		if (getFeatureIndexer().isFeatureIndexed(feature)) {
			return postfixes ? getFeatureIndexer()
					.readPostfixes(feature, value) : getFeatureIndexer().read(
					feature, value);
		} else {
			List<EObject> list = new ArrayList<EObject>();
			EClass clazz = (EClass) feature.eContainer();
			EObject[] objects = doRead();
			for (int i = 0; i < objects.length; i++) {
				if (objects[i].eClass().equals(clazz)) {
					try {
						Object oValue = objects[i].eGet(feature);
						if (value == null) {
							if (oValue == null && !postfixes)
								list.add(objects[i]);
						} else {
							if (!postfixes && value.equals(oValue))
								list.add(objects[i]);
							if (postfixes
									&& oValue.toString().startsWith(
											value.toString()))
								list.add(objects[i]);
						}
					} catch (Exception e) {
					}
				}
			}
			return list.toArray(new EObject[list.size()]);
		}
	}

	protected EObject[] doRead() {
		ArrayList<EObject> contents = new ArrayList<EObject>();
		Resource[] resources = getResourceStorage().loadResources();
		for (int i = 0; i < resources.length; i++) {
			try {
				resources[i].load(null);
			} catch (IOException e) {
				// ignore
			}
			contents.addAll(resources[i].getContents());
		}
		return contents.toArray(new EObject[contents.size()]);
	}

}
