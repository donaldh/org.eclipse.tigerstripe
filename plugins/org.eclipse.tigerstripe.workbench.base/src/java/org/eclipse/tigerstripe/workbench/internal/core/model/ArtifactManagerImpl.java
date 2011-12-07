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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.impl.QueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.api.project.IPhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext.ICycle;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.profile.WorkbenchProfile;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.ArtifactRepository;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectFactory;
import org.eclipse.tigerstripe.workbench.internal.core.util.Predicate;
import org.eclipse.tigerstripe.workbench.internal.core.util.PredicatedList;
import org.eclipse.tigerstripe.workbench.internal.core.util.PredicatedMap;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.parser.ParseException;


/**
 * Default implementation of {@link ArtifactManager}
 * 
 * @see {@link ArtifactManager}
 */
public class ArtifactManagerImpl implements ITigerstripeChangeListener, ArtifactManager {

	private final static int DEFAULT_BROADCASTMASK = IArtifactChangeListener.NOTIFY_ALL;

	private long localTimeStamp = 0;

	private int broadcastMask = DEFAULT_BROADCASTMASK;

	private final static int DONT_IGNORE_ACTIVEFACET = 0;

	private final static int IGNORE_ACTIVEFACET = 1;

	private IArtifactManagerSession phantomArtifactMgrSession = null;

	private DependenciesContentCache depContentCache;

	/** A Reentrant Read/Write lock for this */
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private final ReadLock readLock = lock.readLock();

	private final WriteLock writeLock = lock.writeLock();

	private boolean mgrChanged = true;

	/** The collection of artifacts types that can be discovered */
	private Collection<IAbstractArtifact> discoverableArtifacts;

	/** A map of extracted Artifacts grouped by Artifact Model */
	private Map extractedMap = new HashMap();

	/** A map of extracted Artifacts grouped by their fully qualified name */
	private PredicatedMap namedArtifactsMap = new PredicatedMap();

	/** A map of JavaSource grouped by filename */
	// ED: this doesn't need to be a predicated map (would need a different
	// predicate as
	// keys are not FQNs)
	private Map filenameMap;

	/** A map of Artifacts grouped by JavaSource */
	// ED: this doesn't need to be a predicated map (would need a different
	// predicate as
	// keys are not FQNs)
	private Map sourceMap;

	private TigerstripeProject tsProject;

	// Internal
	private boolean shouldNotify = true; // controls whether listeners should

	private IFacetReference activeFacet;

	private int defaultFacetBehavior = IGNORE_ACTIVEFACET;

	private static ArrayList<IPrimitiveTypeArtifact> reservedPrimitiveTypeArtifacts = null;

	// be notified or not

	/**
	 * a cache for relationships hosted in this, originiting and terminating
	 * from/to artifact
	 */
	private ArtifactRelationshipCache relationshipCache;

	private final Set<IDisposeListener> disposeListeners = new LinkedHashSet<ArtifactManager.IDisposeListener>();

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getReservedPrimitiveTypeArtifacts()
	 */
	public Collection<IPrimitiveTypeArtifact> getReservedPrimitiveTypeArtifacts() {
		if (reservedPrimitiveTypeArtifacts == null) {
			Collection<IPrimitiveTypeDef> defs = WorkbenchProfile
					.getReservedPrimitiveTypes();
			reservedPrimitiveTypeArtifacts = new ArrayList<IPrimitiveTypeArtifact>();
			try {
				IArtifactManagerSession phantomSession = TigerstripeProjectFactory.INSTANCE
						.getPhantomProject().getArtifactManagerSession();
				ArtifactManager mgr = ((ArtifactManagerSessionImpl) phantomSession)
						.getArtifactManager();

				for (IPrimitiveTypeDef def : defs) {
					if (!"void".equals(def.getName())) {
						PrimitiveTypeArtifact newPrimitive = new PrimitiveTypeArtifact(
								mgr);
						newPrimitive.setFullyQualifiedName(def.getName());
						reservedPrimitiveTypeArtifacts.add(newPrimitive);
					}
				}
				PrimitiveTypeArtifact reservedPrimitive = new PrimitiveTypeArtifact(
						mgr);
				reservedPrimitive.setFullyQualifiedName("String");
				reservedPrimitiveTypeArtifacts.add(reservedPrimitive);

			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);

			}
		}
		return reservedPrimitiveTypeArtifacts;
	}

	/**
	 * 
	 */
	public ArtifactManagerImpl(TigerstripeProject tsProject) {
		this.tsProject = tsProject;
		initManager();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#dispose()
	 */
	public void dispose() {
		if (wasDisposed) {
			return;
		}
		try {
			TigerstripeWorkspaceNotifier.INSTANCE
					.removeTigerstripeChangeListener(this);
			RefactoringChangeListener.getInstance().removeArtifactManager(this);
			fireDispose();
			clear(extractedMap);
			clear(namedArtifactsMap);
			clear(filenameMap);
			clear(sourceMap);
			clear(listeners);
			clear(disposeListeners);
			if (depContentCache != null) {
				depContentCache.dispose();
			}
			if (relationshipCache != null) {
				relationshipCache.clearCache();
			}
			clear(pojosMap);
			extractedMap = null;
			namedArtifactsMap = null;
			filenameMap = null;
			sourceMap = null;
			listeners = null;
			discoverableArtifacts = null;
			activeFacet = null;
			depContentCache = null;
			facetListeners = null;
			listeners = null;
			phantomArtifactMgrSession = null;
			pojosMap = null;
			relationshipCache = null;
			tsProject = null;
		} finally {
			wasDisposed = true;
		}
	}

	protected void clear(Map<?, ?> map) {
		if (map != null) {
			map.clear();
		}
	}

	protected void clear(Collection<?> collection) {
		if (collection != null) {
			collection.clear();
		}
	}

	private boolean wasDisposed = false;

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#wasDisposed()
	 */
	public boolean wasDisposed() {
		return wasDisposed;
	}

	private void clearExtractedMap() {
		for (Object obj : extractedMap.values()) {
			if (obj instanceof PredicatedList) {
				PredicatedList bucket = (PredicatedList) obj;
				bucket.clear();
			}
		}
	}

	private void initManager() {

		try {
			writeLock.lock();
			this.discoverableArtifacts = new ArrayList<IAbstractArtifact>();
			clearExtractedMap();
			this.namedArtifactsMap.clear();
			this.filenameMap = new HashMap();
			this.sourceMap = new HashMap();
			this.relationshipCache = new ArtifactRelationshipCache(this);

			registerDefaultArtifacts();

			this.depContentCache = new DependenciesContentCache(this);

			// Since 1.2, all artifact mgr have a reference into the artifact
			// manager of the Tigerstripe Phantom project
			// @see PhantomTigerstripeProject for more details
			//
			// It's a bit of a hack but we need to check that this is not the
			// ArtifactManager of the phantom project itself! We can tell by the
			// type of the tsProject that is passed
			if (!(getTSProject() instanceof PhantomTigerstripeProject)) {
				try {
					IPhantomTigerstripeProject phantomProject = TigerstripeProjectFactory.INSTANCE
							.getPhantomProject();
					phantomArtifactMgrSession = phantomProject
							.getArtifactManagerSession();

				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);

				}
			}

			// Register for changes of the profile or model

		} finally {
			// TODO Is this sufficent changes.. NO not yet!
			TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(
					this, ITigerstripeChangeListener.ARTIFACT_RESOURCES);
			RefactoringChangeListener.getInstance().addArtifactManager(this);
			writeLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#reset(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void reset(IProgressMonitor monitor) {
		if (wasDisposed) {
			return;
		}

		if (monitor == null)
			monitor = new NullProgressMonitor();

		clearExtractedMap();
		this.namedArtifactsMap.clear();
		this.filenameMap = new HashMap();
		this.sourceMap = new HashMap();

		depContentCache.updateCache(newContext(monitor));
		relationshipCache.updateCache(monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getRegisteredArtifacts()
	 */
	public Collection<IAbstractArtifact> getRegisteredArtifacts() {
		if (wasDisposed) {
			return Collections.emptySet();
		}
		return this.discoverableArtifacts;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#updateDependenciesContentCache(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void updateDependenciesContentCache(IProgressMonitor monitor) {
		if (wasDisposed) {
			return;
		}
		if (monitor == null)
			monitor = new NullProgressMonitor();

		// This is called by the TigerstripeProject each time the list of
		// dependencies is changed.
		this.depContentCache.updateCache(newContext(monitor));
	}

	/**
	 * Adds the default artifacts to the list of discoverable Artifacts
	 * 
	 * These are the default artifacts that Tigerstripe defines. Any plugin can
	 * extend the hierarchy and register them as well.
	 * 
	 * @see ArtifactManager#registerDiscoverableArtifact(AbstractArtifact)
	 */
	private void registerDefaultArtifacts() {

		// @since 1.2
		// All core artifacts are conditioned by the active profile
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
				.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);

		try {
			if (prop.getDetailsForType(IManagedEntityArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(ManagedEntityArtifact.MODEL);
			}
			if (prop.getDetailsForType(IDatatypeArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(DatatypeArtifact.MODEL);
			}
			if (prop.getDetailsForType(ISessionArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(SessionFacadeArtifact.MODEL);
			}
			if (prop.getDetailsForType(IEventArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(EventArtifact.MODEL);
			}
			if (prop.getDetailsForType(IEnumArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(EnumArtifact.MODEL);
			}
			if (prop.getDetailsForType(IQueryArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(QueryArtifact.MODEL);
			}
			if (prop.getDetailsForType(IExceptionArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(ExceptionArtifact.MODEL);
			}
			if (prop.getDetailsForType(IUpdateProcedureArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(UpdateProcedureArtifact.MODEL);
			}
			if (prop.getDetailsForType(IAssociationArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(AssociationArtifact.MODEL);
			}
			if (prop.getDetailsForType(
					IAssociationClassArtifact.class.getName()).isEnabled()) {
				registerDiscoverableArtifact(AssociationClassArtifact.MODEL);
			}
			if (prop.getDetailsForType(IPackageArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(PackageArtifact.MODEL);
			}

			if (prop.getDetailsForType(IPrimitiveTypeArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(PrimitiveTypeArtifact.MODEL);
			}
			if (prop.getDetailsForType(IDependencyArtifact.class.getName())
					.isEnabled()) {
				registerDiscoverableArtifact(DependencyArtifact.MODEL);
			}
		} catch (IllegalArgumentException e) {
			// shouldn't happen here
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#registerDiscoverableArtifact(org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal)
	 */
	public void registerDiscoverableArtifact(IAbstractArtifactInternal artifact)
			throws IllegalArgumentException {
		if (wasDisposed) {
			return;
		}
		if (artifact == null)
			throw new IllegalArgumentException(
					"Trying to register invalid artifact (null)");

		if (this.discoverableArtifacts.contains(artifact))
			throw new IllegalArgumentException("Artifact "
					+ artifact.getMarkingTag() + " already registered.");

		this.discoverableArtifacts.add(artifact);
	}

	/**
	 * Validates all artifacts.
	 * 
	 * At this point, all artifacts are loaded, so semantic validation can
	 * occur.
	 * 
	 * @throws TigerstripeException
	 */
	private void validateArtifacts(IProgressMonitor monitor)
			throws TigerstripeException {

		if (monitor == null)
			monitor = new NullProgressMonitor();

		try {
			writeLock.lock();
			long startTime = System.currentTimeMillis();
			Collection artifacts = this.namedArtifactsMap.values();

			for (Iterator iter = artifacts.iterator(); iter.hasNext();) {
				IAbstractArtifactInternal artifact = (IAbstractArtifactInternal) iter
						.next();
				artifact.resolveReferences(monitor);
			}

			long endTime = System.currentTimeMillis();
			TigerstripeRuntime.logTraceMessage(" ["
					+ getTSProject().getProjectLabel() + "] Validated ("
					+ (endTime - startTime) + " ms)");

		} finally {
			writeLock.unlock();
		}
	}

	private void addToExtractedMap(IAbstractArtifactInternal model,
			IAbstractArtifactInternal extracted,
			IAbstractArtifactInternal oldArtifact) {
		PredicatedList<IAbstractArtifact> bucket = null;
		if (this.extractedMap.containsKey(model)) {
			bucket = (PredicatedList<IAbstractArtifact>) extractedMap
					.get(model);
		} else {
			bucket = new PredicatedList<IAbstractArtifact>();
			// make sure the predicate is there if there is an active facet
			if (namedArtifactsMap.getPredicate() != null) {
				bucket.setPredicate(namedArtifactsMap.getPredicate());
			}
			this.extractedMap.put(model, bucket);
		}

		// HERE: replace with new one if already exists.
		if (oldArtifact != null) {
			bucket.remove(oldArtifact);
		}

		if (!bucket.getBackingList().contains(extracted))
			bucket.add(extracted);
	}

	private void addToNamedArtifactsMap(String fullyQualifiedName,
			IAbstractArtifactInternal extracted,
			IAbstractArtifactInternal oldArtifact) {
		this.namedArtifactsMap.put(fullyQualifiedName, extracted);
	}

	private void addToSourceMap(JavaSource source,
			IAbstractArtifactInternal artifact,
			IAbstractArtifactInternal oldArtifact) {
		this.sourceMap.put(source, artifact);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getArtifactsByModel(org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List<IAbstractArtifact> getArtifactsByModel(
			IAbstractArtifactInternal model,
			boolean includeDependencies, IProgressMonitor monitor) {
		if (wasDisposed) {
			return Collections.emptyList();
		}
		return getArtifactsByModel(model, includeDependencies,
				shouldOverridePredicate(), monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getArtifactsByModel(org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal, boolean, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List<IAbstractArtifact> getArtifactsByModel(
			IAbstractArtifactInternal model,
			boolean includeDependencies, boolean overridePredicate,
			IProgressMonitor monitor) {
		if (wasDisposed) {
			return Collections.emptyList();
		}
		return getArtifactsByModel(model, includeDependencies,
				overridePredicate, newContext(monitor));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getArtifactsByModel(org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal, boolean, org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext)
	 */
	public List<IAbstractArtifact> getArtifactsByModel(
			IAbstractArtifactInternal model,
			boolean includeDependencies, ExecutionContext context) {
		if (wasDisposed) {
			return Collections.emptyList();
		}
		return getArtifactsByModel(model, includeDependencies,
				shouldOverridePredicate(), context);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getArtifactsByModel(org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal, boolean, boolean, org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext)
	 */
	public List<IAbstractArtifact> getArtifactsByModel(
			IAbstractArtifactInternal model,
			boolean includeDependencies, boolean overridePredicate,
			ExecutionContext context) {
		if (wasDisposed) {
			return Collections.emptyList();
		}

		try {
			lock(true);
			readLock.lock();
			List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
			PredicatedList<IAbstractArtifact> bucket = (PredicatedList<IAbstractArtifact>) this.extractedMap
					.get(model);
			if (bucket != null) {
				List<IAbstractArtifact> extracted = null;
				if (overridePredicate)
					extracted = bucket.getBackingList();
				else
					extracted = bucket;

				if (extracted != null) {
					result.addAll(extracted);
				}
			}

			if (includeDependencies) {
				Collection<IAbstractArtifact> arts = getArtifactsByModelInChained(
						model, context);
				for (IAbstractArtifact toAdd : arts) {
					boolean found = false;
					for (Object obj : result) {
						IAbstractArtifact inAlready = (IAbstractArtifact) obj;
						if (inAlready.getFullyQualifiedName().equals(
								toAdd.getFullyQualifiedName())) {
							found = true;
						}
					}
					if (!found) {
						result.add(toAdd);
					}
				}

				if (phantomArtifactMgrSession != null
						&& model instanceof IPrimitiveTypeArtifact) {

					if (context.addToCycle(Cycles.PHANTOM,
							phantomArtifactMgrSession)) {

						ArtifactManager phantomMgr = ((ArtifactManagerSessionImpl) phantomArtifactMgrSession)
								.getArtifactManager();

						result.addAll(phantomMgr.getArtifactsByModel(model,
								false, context));
					}
				}
			}
			return result;
		} finally {
			lock(false);
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getArtifactByFilename(java.lang.String)
	 */
	public IAbstractArtifactInternal getArtifactByFilename(String filename) {
		if (wasDisposed) {
			return null;
		}
		try {
			readLock.lock();
			return (IAbstractArtifactInternal) this.filenameMap.get(filename);
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getAllArtifacts(boolean, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List<IAbstractArtifact> getAllArtifacts(boolean includeDependencies,
			boolean isOverridePredicate, IProgressMonitor monitor) {
		return getAllArtifacts(includeDependencies, isOverridePredicate,
				newContext(monitor));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getAllArtifacts(boolean, org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext)
	 */
	public List<IAbstractArtifact> getAllArtifacts(boolean includeDependencies,
			ExecutionContext context) {
		return getAllArtifacts(includeDependencies, shouldOverridePredicate(),
				context);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getAllArtifacts(boolean, boolean, org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext)
	 */
	public List<IAbstractArtifact> getAllArtifacts(boolean includeDependencies,
			boolean isOverridePredicate, ExecutionContext context) {
		if (wasDisposed) {
			return Collections.emptyList();
		}
		try {
			lock(true);
			readLock.lock();
			List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();

			if (isOverridePredicate)
				result.addAll(this.namedArtifactsMap.getBackingMap().values());
			else
				result.addAll(this.namedArtifactsMap.values());
			if (includeDependencies) {
				Collection<IAbstractArtifact> allChained = getAllChainedArtifacts(context);
				// Bug 752 making sure the artifacts from Phantom are not added
				// from dependencies. They will be added locally right after
				// that.
				// If phantom session doesn't exist simply add everything.
				for (IAbstractArtifact art : allChained) {
					boolean found = false;
					for (Object inResult : result) {
						IAbstractArtifact arti = (IAbstractArtifact) inResult;
						if (arti.getFullyQualifiedName().equals(
								art.getFullyQualifiedName())) {
							found = true;
							break;
						}
					}
					if (!found) {
						if (phantomArtifactMgrSession != null) {

							ArtifactManager phantomMgr = ((ArtifactManagerSessionImpl) phantomArtifactMgrSession)
									.getArtifactManager();
							if (((IAbstractArtifactInternal) art)
									.getArtifactManager() != phantomMgr) {
								result.add(art);
							}
						} else {
							result.add(art);
						}
					}
				}
				try {
					if (phantomArtifactMgrSession != null) {
						IQueryAllArtifacts query = new QueryAllArtifacts();
						query.setExecutionContext(context);
						Collection<IAbstractArtifact> res = phantomArtifactMgrSession
								.queryArtifact(query);
						for (IAbstractArtifact art : res) { // Bug 752 don't add
							// these multiple
							// times
							boolean found = false;
							for (Object inResult : result) {
								IAbstractArtifact arti = (IAbstractArtifact) inResult;
								if (arti.getFullyQualifiedName().equals(
										art.getFullyQualifiedName())) {
									found = true;
									break;
								}
							}
							if (!found) {
								result.add(art);
							}
						}
					}
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
			return result;
		} finally {
			lock(false);
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getAllArtifacts(boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Collection<IAbstractArtifact> getAllArtifacts(
			boolean includeDependencies, IProgressMonitor monitor) {
		if (wasDisposed) {
			return Collections.emptyList();
		}
		if (monitor == null)
			monitor = new NullProgressMonitor();

		return getAllArtifacts(includeDependencies, shouldOverridePredicate(),
				monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getModelArtifacts(boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Collection getModelArtifacts(boolean includeDependencies,
			IProgressMonitor monitor) {
		if (wasDisposed) {
			return Collections.emptyList();
		}
		if (monitor == null)
			monitor = new NullProgressMonitor();

		return getModelArtifacts(includeDependencies,
				shouldOverridePredicate(), monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getModelArtifacts(boolean, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Collection getModelArtifacts(boolean includeDependencies,
			boolean overridePredicate, IProgressMonitor monitor) {
		if (wasDisposed) {
			return Collections.emptyList();
		}
		if (monitor == null)
			monitor = new NullProgressMonitor();

		try {
			readLock.lock();
			Collection result = new ArrayList();

			result.addAll(getArtifactsByModel(ManagedEntityArtifact.MODEL,
					includeDependencies, overridePredicate, monitor));
			result.addAll(getArtifactsByModel(DatatypeArtifact.MODEL,
					includeDependencies, overridePredicate, monitor));
			result.addAll(getArtifactsByModel(EnumArtifact.MODEL,
					includeDependencies, overridePredicate, monitor));

			return result;
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getCapabilitiesArtifacts(boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Deprecated
	public Collection getCapabilitiesArtifacts(boolean includeDependencies,
			IProgressMonitor monitor) {
		if (wasDisposed) {
			return Collections.emptyList();
		}
		return getCapabilitiesArtifacts(includeDependencies,
				shouldOverridePredicate(), monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getCapabilitiesArtifacts(boolean, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Deprecated
	public Collection getCapabilitiesArtifacts(boolean includeDependencies,
			boolean overridePredicate, IProgressMonitor monitor) {
		return getCapabilitiesArtifacts(includeDependencies, overridePredicate,
				newContext(monitor));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getCapabilitiesArtifacts(boolean, boolean, org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext)
	 */
	@Deprecated
	public Collection getCapabilitiesArtifacts(boolean includeDependencies,
			boolean overridePredicate, ExecutionContext context) {
		if (wasDisposed) {
			return Collections.emptyList();
		}
		try {
			readLock.lock();
			Collection result = new ArrayList();

			result.addAll(getArtifactsByModel(QueryArtifact.MODEL,
					includeDependencies, overridePredicate, context));
			result.addAll(getArtifactsByModel(UpdateProcedureArtifact.MODEL,
					includeDependencies, overridePredicate, context));
			result.addAll(getArtifactsByModel(EventArtifact.MODEL,
					includeDependencies, overridePredicate, context));
			result.addAll(getArtifactsByModel(ExceptionArtifact.MODEL,
					includeDependencies, overridePredicate, context));

			return result;
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getArtifactByFullyQualifiedName(java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IAbstractArtifactInternal getArtifactByFullyQualifiedName(
			String name,
			boolean includeDependencies, IProgressMonitor monitor) {
		if (monitor == null)
			monitor = new NullProgressMonitor();
		return getArtifactByFullyQualifiedName(name, includeDependencies,
				newContext(monitor));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getArtifactByFullyQualifiedName(java.lang.String, boolean, org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext)
	 */
	public IAbstractArtifactInternal getArtifactByFullyQualifiedName(
			String name,
			boolean includeDependencies, ExecutionContext context) {
		if (wasDisposed) {
			return null;
		}

		return getArtifactByFullyQualifiedName(name, includeDependencies,
				shouldOverridePredicate(), context);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getArtifactByFullyQualifiedName(java.lang.String, boolean, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IAbstractArtifactInternal getArtifactByFullyQualifiedName(
			String name,
			boolean includeDependencies, boolean isOverridePredicate,
			IProgressMonitor monitor) {
		if (monitor == null)
			monitor = new NullProgressMonitor();

		return getArtifactByFullyQualifiedName(name, includeDependencies,
				isOverridePredicate, newContext(monitor));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getArtifactByFullyQualifiedName(java.lang.String, boolean, boolean, org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext)
	 */
	public IAbstractArtifactInternal getArtifactByFullyQualifiedName(
			String name,
			boolean includeDependencies, boolean isOverridePredicate,
			ExecutionContext context) {

		if (wasDisposed) {
			return null;
		}

		try {
			readLock.lock();
			IAbstractArtifactInternal local = null;
			if (isOverridePredicate) {
				local = (IAbstractArtifactInternal) this.namedArtifactsMap
						.getBackingMap().get(name);
			} else {
				local = (IAbstractArtifactInternal) this.namedArtifactsMap
						.get(name);
			}
			if (local != null)
				return local;
			else {
				if (includeDependencies) {
					IAbstractArtifact result = getArtifactByFullyQualifiedNameInChained(
							name, context);
					if (result == null) {
						try {
							if (phantomArtifactMgrSession != null)
								result = phantomArtifactMgrSession
										.getArtifactByFullyQualifiedName(name,
												context);
						} catch (TigerstripeException e) {
							TigerstripeRuntime.logErrorMessage(
									"TigerstripeException detected", e);
						}
					}
					return (IAbstractArtifactInternal) result;
				} else
					return null;
			}
		} finally {
			readLock.unlock();
		}
	}

	// ======================================================================
	// ======================================================================
	// ======================================================================

	// A map of String->AbstractArtifact for each pojo
	private Map pojosMap;

	private boolean isLocked = false;

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#lock(boolean)
	 */
	public void lock(boolean isLocked) {
		this.isLocked = isLocked;

		if (isLocked()) {
			TigerstripeRuntime
					.logTraceMessage(" Locking down Artifact Manager.");
		} else {
			TigerstripeRuntime.logTraceMessage(" Un-Locking Artifact Manager.");
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#isLocked()
	 */
	public boolean isLocked() {
		return isLocked;
	}

	// =======================================================================
	// Upon generation the Artifact mgr needs to change state. In particular,
	// the following should happen:
	// - locking: nothing can be added/removed.
	// - Active facet cannot be ignored: the default behavior when non
	// generate is to ignore facets for lookups/queries.
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#generationStart()
	 */
	public void generationStart() {
		if (wasDisposed) {
			return;
		}
		lock(true);
		setDefaultFacetBehavior(DONT_IGNORE_ACTIVEFACET);

		// Bug: 928: also, we need to propagate that state change to all
		// referenced projects/dependencies
		for (ITigerstripeModelProject project : getTSProject().getEnabledReferencedProjects()) {
			try {
				project.getArtifactManagerSession().generationStart();
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}

		if (phantomArtifactMgrSession != null)
			phantomArtifactMgrSession.generationStart();

	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#generationComplete()
	 */
	public void generationComplete() {
		if (wasDisposed) {
			return;
		}
		setDefaultFacetBehavior(IGNORE_ACTIVEFACET);
		lock(false);

		// Bug: 928: also, we need to propagate that state change to all
		// referenced projects/dependencies
		for (ITigerstripeModelProject project : getTSProject().getEnabledReferencedProjects()) {
			try {
				project.getArtifactManagerSession().generationComplete();
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}

		if (phantomArtifactMgrSession != null)
			phantomArtifactMgrSession.generationComplete();
	}

	// =======================================================================
	// =======================================================================

	// =======================================================================
	// Default facet behavior:
	// by default active facets are ignored for lookups/queries except during
	// project generation. See #generationStart()
	private void setDefaultFacetBehavior(int defaultFacetBehavior) {
		this.defaultFacetBehavior = defaultFacetBehavior;
	}

	private int getDefaultFacetBehavior() {
		return this.defaultFacetBehavior;
	}

	private boolean shouldOverridePredicate() {
		return IGNORE_ACTIVEFACET == getDefaultFacetBehavior();
	}

	// =======================================================================
	// =======================================================================

	/**
	 * Checks if the underlying project for this artifact mgr still exists. The
	 * underlying directory structure may have been deleted manually so we need
	 * to check.
	 * 
	 * @return
	 */
	private boolean checkProjectStillExists() {

		boolean stillExists = true;
		Collection repositories = getTSProject().getArtifactRepositories();

		for (Iterator iter = repositories.iterator(); iter.hasNext();) {
			ArtifactRepository repo = (ArtifactRepository) iter.next();

			File repoDir = repo.getBaseDirectory();
			if (repoDir != null && !repoDir.exists()) { // repoDir==null when
				// module project
				stillExists = false;
			}
		}

		return stillExists;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#refresh(boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void refresh(boolean forceReload, IProgressMonitor monitor) {
		if (wasDisposed) {
			return;
		}
		if (monitor == null)
			monitor = new NullProgressMonitor();

		// The underlying project for this Artifact Manager may have been
		// deleted manually, in which case the directory structure will
		// have disappeared.
		if (!checkProjectStillExists()) {
			clean();
			return;
		}

		if (isLocked())
			return;

		try {
			writeLock.lock();

			if (!mgrChanged && !forceReload)
				// TigerstripeRuntime.logTraceMessage("Wouldn't reload "
				// + getTSProject().getProjectLabel());
				return;

			if (forceReload) {
				clean();
				pojoStateMap.clear();
			}

			long startTime = System.currentTimeMillis();
			List<String> allResources = findAllResourcesFromPath(monitor);
			// TigerstripeRuntime.logInfoMessage(" All resources " +
			// allResources.size());
			List<PojoState> changedPojos = pojosHaveChanged(allResources,
					monitor);

			// allPojos = findAllPojosFromPath();
			// List<PojoState> changedPojos = pojosHaveChanged(allPojos);
			parsePojos(changedPojos);

			// if (forceReload || changedPojos.size() != 0) {
			// TigerstripeRuntime.logInfoMessage("---------->>>>> Reloading! "
			// + getTSProject().getProjectLabel()
			// + " <<<<<<<<<<--------------");
			// clean();

			// TigerstripeRuntime.logInfoMessage("Extracting: ");

			if (changedPojos.size() != 0) {
				extractFromPojos(changedPojos, monitor);
				validateArtifacts(monitor);

				updateLocalTimeStamp();

				long endTime = System.currentTimeMillis();
				TigerstripeRuntime.logTraceMessage("Refreshed ("
						+ (endTime - startTime) + " ms)"
						+ getTSProject().getProjectLabel());

			} else {
				long endTime = System.currentTimeMillis();
				TigerstripeRuntime.logTraceMessage("Skipped a Refresh ("
						+ (endTime - startTime) + " ms)"
						+ getTSProject().getProjectLabel());
			}
			// TigerstripeRuntime.logInfoMessage(" -- Done reloading");
			// }

		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		} finally {
			mgrChanged = false;
			writeLock.unlock();
		}

		// Notify listeners that the manager has just been reloaded.
		notifyReload();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#refreshReferences(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void refreshReferences(IProgressMonitor monitor) {
		if (wasDisposed) {
			return;
		}
		try {
			writeLock.lock();
			if (monitor == null)
				monitor = new NullProgressMonitor();

			for (ITigerstripeModelProject project : getTSProject().getEnabledReferencedProjects()) {
				try {
					IArtifactManagerSession session = project
							.getArtifactManagerSession();
					session.refresh(monitor);
				} catch (TigerstripeException e) {

				}
			}
		} finally {
			writeLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#updateCaches(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void updateCaches(IProgressMonitor monitor) {
		if (wasDisposed) {
			return;
		}
		try {
			writeLock.lock();
			if (monitor == null)
				monitor = new NullProgressMonitor();

			updateDependenciesContentCache(monitor);
			relationshipCache.updateCache(monitor);
			updateLocalTimeStamp();
		} finally {
			writeLock.unlock();
		}
	}

	private class PojoState {
		String filename;

		JavaSource javaSource;

		long lastModified;

		public String getFilename() {
			return this.filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public long getLastModified() {
			return this.lastModified;
		}

		public void setLastModified(long lastModified) {
			this.lastModified = lastModified;
		}

		public void setJavaSource(JavaSource javaSource) {
			this.javaSource = javaSource;
		}

		public JavaSource getJavaSource() {
			return this.javaSource;
		}
	}

	private HashMap<String, PojoState> buildPojoState(List<String> resources,
			IProgressMonitor monitor) {
		HashMap<String, PojoState> result = new HashMap<String, PojoState>();

		monitor.beginTask("Building initial list of POJOs", resources.size());
		for (String resource : resources) {
			PojoState state = new PojoState();
			File file = new File(resource);
			state.setFilename(file.getAbsolutePath());
			state.setLastModified(file.lastModified());
			result.put(file.getAbsolutePath(), state);
			monitor.worked(1);
		}
		monitor.done();

		return result;
	}

	private HashMap<String, PojoState> pojoStateMap = new HashMap<String, PojoState>();

	// private PojoState[] previousPojoList = null;

	/**
	 * Returns true if the list of pojos has changed
	 * 
	 * @param pojos
	 * @return
	 */
	private List<PojoState> pojosHaveChanged(List<String> resources,
			IProgressMonitor monitor) {
		List<PojoState> changedPojos = new ArrayList<PojoState>();
		// TigerstripeRuntime.logInfoMessage("PojosHaveChanged is running on "
		// + getTSProject().getProjectLabel());
		if (pojoStateMap.isEmpty()) {
			// This is the first time around
			pojoStateMap = buildPojoState(resources, monitor);
			changedPojos.addAll(pojoStateMap.values());
		} else {
			HashMap<String, PojoState> newMap = buildPojoState(resources,
					monitor);
			for (String filename : newMap.keySet()) {
				PojoState newState = newMap.get(filename);
				PojoState oldState = pojoStateMap.get(filename);
				if (oldState == null
						|| (oldState.getLastModified() != newState
								.getLastModified())) {
					changedPojos.add(newState);
				}
			}
			pojoStateMap.clear();
			pojoStateMap = newMap;
		}
		return changedPojos;
	}

	private void listFilesRecursive(File rootFile, List<String> foundResources) {
		if (rootFile != null && rootFile.isDirectory()) {
			File[] content = rootFile.listFiles();
			for (File file : content) {
				if (file.isFile()
						&& (file.getName().endsWith(".java") || file.getName()
								.equals(".package"))) {
					foundResources.add(file.toString());
				} else if (file.isDirectory()) {
					listFilesRecursive(file, foundResources);
				}
			}
		}
	}

	protected List<String> findAllResourcesFromPath(IProgressMonitor monitor) {
		if (wasDisposed) {
			return Collections.emptyList();
		}
		long startTime = System.currentTimeMillis();
		List<String> allResources = new ArrayList<String>();

		String baseRep = getTSProject().getBaseDir() + File.separator
				+ getTSProject().getBaseRepository();
		File baseRepFile = new File(baseRep);
		if (baseRepFile != null && baseRepFile.exists()
				&& baseRepFile.isDirectory()) {
			listFilesRecursive(baseRepFile, allResources);
		}
		TigerstripeRuntime.logTraceMessage(" ["
				+ getTSProject().getProjectLabel() + "] found "
				+ allResources.size() + " in "
				+ (System.currentTimeMillis() - startTime)
				+ " ms (without scanner)");

		return allResources;
	}

	private void parsePojos(List<PojoState> pojos) {
		JavaDocBuilder builder = new JavaDocBuilder();

		int addedResourceCount = 0;
		for (PojoState pojo : pojos) {
			// TigerstripeRuntime.logInfoMessage("Parsing " +
			// pojo.getFilename());
			try {
				JavaSource source = null;
				try {
					source = builder.addSource(new FileReader(pojo
							.getFilename()));
					java.net.URI uri = new File(pojo.getFilename()).toURI();
					source.setURL(uri.toURL());
					pojo.setJavaSource(source);
				} catch (ParseException e) {
					BasePlugin.log(e);
					continue;
				} catch (MalformedURLException e) {
					// should not happen
				}

				if (source != null) {
					// filenameMap.put(pojo.getFilename(), source);
					addedResourceCount++;
				}

			} catch (FileNotFoundException e) {
				BasePlugin.log(e);
			}
		}
	}

	// /**
	// * Figure out what is the list of POJOs we have to load from and return
	// the
	// * corresponding JavaSource to be extracted.
	// *
	// * @return the array of all JavaSources based on the current src path
	// */
	// protected JavaSource[] findAllPojosFromPath(List<Resource> resources) {
	//
	// JavaDocBuilder builder = new JavaDocBuilder();
	//
	// int addedResourceCount = 0;
	// for (Iterator iter = resources.iterator(); iter.hasNext();) {
	// Resource res = (Resource) iter.next();
	// try {
	// log.debug("adding resource: " + res.getName());
	// JavaSource source = builder.addSource(new FileReader(tsProject
	// .getBaseDir()
	// + File.separator + res.getName()));
	// try {
	// URI uri = new File(tsProject.getBaseDir() + File.separator
	// + res.getName()).toURI();
	// source.setURL(uri.toURL());
	// } catch (MalformedURLException e) {
	// // should not happen
	// }
	// if (source != null) {
	// filenameMap.put(tsProject.getBaseDir() + File.separator
	// + res.getName(), source);
	// }
	//
	// addedResourceCount++;
	// } catch (FileNotFoundException e) {
	// log.error("File " + res.getName() + " not found.");
	// } catch (ParseException e) {
	// TigerstripeRuntime.logErrorMessage("File " + res.getName() + ": Parse
	// exception.", e);
	// log.error("File " + res.getName() + ": Parse exception.");
	// }
	// }
	// log.debug("Added " + addedResourceCount + " resources.");
	//
	// return builder.getSources();
	// }
	//
	/**
	 * Populates this Manager based on the array of sources
	 * 
	 * @param sources
	 */
	protected void extractFromPojos(List<PojoState> changedPojos,
			IProgressMonitor monitor) throws TigerstripeException {

		long startTime = System.currentTimeMillis();

		monitor.beginTask("Extracting POJOs", changedPojos.size());
		for (PojoState state : changedPojos) {
			// System.out.println("Extracting " +
			// state.getFilename());

			if (state.javaSource != null) {
				monitor.subTask(state.javaSource.getURL().toString());
				IAbstractArtifactInternal artifact = extractArtifact(
						state.javaSource,
						monitor);

				if (artifact != null)
					addArtifact(artifact, monitor);
			}
			monitor.worked(1);
		}
		monitor.done();

		TigerstripeRuntime.logTraceMessage(" ["
				+ getTSProject().getProjectLabel() + "] extracted "
				+ changedPojos.size() + " in "
				+ (System.currentTimeMillis() - startTime) + " ms");
		// System.err.println(" ["
		// + getTSProject().getProjectLabel() + "] extracted "
		// + changedPojos.size() + " in "
		// + (System.currentTimeMillis() - startTime) + " ms");
	}

	/**
	 * Clean up all artifacts in this Artifact Manager and let all listeners
	 * know.
	 * 
	 * 
	 */
	protected void clean() {

		// TigerstripeRuntime.logInfoMessage(" Cleaning now.");
		if (pojosMap != null) {
			pojosMap.clear();
		}

		relationshipCache.clearCache();
		clearExtractedMap();
		this.namedArtifactsMap.clear();
		this.filenameMap = new HashMap();
		this.sourceMap = new HashMap();

		pojosMap = new HashMap();
	}

	// ==========================================================================
	// ==========================================================================
	private Collection<IArtifactChangeListener> listeners = new CopyOnWriteArrayList<IArtifactChangeListener>();

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#addArtifactManagerListener(org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener)
	 */
	public void addArtifactManagerListener(IArtifactChangeListener listener) {
		if (wasDisposed) {
			return;
		}
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#removeArtifactManagerListener(org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener)
	 */
	public void removeArtifactManagerListener(IArtifactChangeListener listener) {
		if (wasDisposed) {
			return;
		}
		listeners.remove(listener);
	}

	protected void notifyReload() {
		if (shouldNotify
				&& (broadcastMask & IArtifactChangeListener.NOTIFY_RELOADED) == IArtifactChangeListener.NOTIFY_RELOADED) {
			for (IArtifactChangeListener listener : listeners) {
				try {
					listener.managerReloaded();
				} catch (Exception e) {
					// finish the loop even if exception raised in handler
					BasePlugin.log(e);
				}
			}
		}
	}

	protected void notifyArtifactAdded(IAbstractArtifact artifact) {
		if (shouldNotify
				&& (broadcastMask & IArtifactChangeListener.NOTIFY_ADDED) == IArtifactChangeListener.NOTIFY_ADDED) {
			for (IArtifactChangeListener listener : listeners) {
				try {
					listener.artifactAdded(artifact);
				} catch (Exception e) {
					// finish the loop even if exception raised in handler
					BasePlugin.log(e);
				}
			}
		}
	}

	// public void notifyArtifactSaved(IAbstractArtifact artifact,
	// IProgressMonitor monitor) {
	//
	// try {
	// writeLock.lock();
	// addArtifact(artifact, monitor); // replace the current value with
	// // this new one
	//
	// // At this point we need to update the PojoState for this artifact
	// // or else it will parsed again upon next refresh
	// ojoState(artifact);
	// } catch (TigerstripeException e) {
	// TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
	// e);
	// } finally {
	// writeLock.unlock();
	// }
	// }

	/**
	 * Updates the pojo state for this artifact, i.e. updates the lastModified
	 * tstamp.
	 * 
	 * @param artifact
	 */
	private void pojoState(IAbstractArtifact artifact)
			throws TigerstripeException {
		String path = ((IAbstractArtifactInternal) artifact).getArtifactPath();
		if (path != null) {
			File baseDir = getTSProject().getBaseDir();
			String fullPath = (new File(baseDir.getAbsolutePath()
					+ File.separator + path)).getAbsolutePath();
			PojoState state = pojoStateMap.get(fullPath);
			if (state == null) {
				state = new PojoState();
				state.setFilename(fullPath);
				pojoStateMap.put(fullPath, state);
			}
			state.setLastModified((new File(fullPath)).lastModified());
			updateLocalTimeStamp();
		}
	}

	protected void notifyArtifactChanged(IAbstractArtifact artifact,
			IAbstractArtifact oldArtifact) {
		// FIXME: the notification should really be coming from the refresh
		// based on what was actually reloaded?
		if (shouldNotify
				&& (broadcastMask & IArtifactChangeListener.NOTIFY_CHANGED) == IArtifactChangeListener.NOTIFY_CHANGED) {
			for (IArtifactChangeListener listener : listeners) {
				try {
					// System.out.println("Notify Artifact Changed "+listener);
					listener.artifactChanged(artifact, oldArtifact);
				} catch (Exception e) {
					// finish the loop even if exception raised in handler
					BasePlugin.log(e);
				}
			}
		}
	}

	protected void notifyArtifactRemoved(IAbstractArtifact artifact) {
		if (shouldNotify
				&& (broadcastMask & IArtifactChangeListener.NOTIFY_REMOVED) == IArtifactChangeListener.NOTIFY_REMOVED) {
			for (IArtifactChangeListener listener : listeners) {
				try {
					listener.artifactRemoved(artifact);
				} catch (Exception e) {
					// finish the loop even if exception raised in handler
					BasePlugin.log(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getTSProject()
	 */
	public TigerstripeProject getTSProject() {
		return this.tsProject;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#extractArtifact(com.thoughtworks.qdox.model.JavaSource, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IAbstractArtifactInternal extractArtifact(JavaSource source,
			IProgressMonitor monitor) throws TigerstripeException {
		if (wasDisposed) {
			return null;
		}

		IAbstractArtifactInternal extracted = null;
		for (Iterator<IAbstractArtifact> iter = this.discoverableArtifacts
				.iterator(); iter.hasNext();) {
			IAbstractArtifactInternal model = (IAbstractArtifactInternal) iter
					.next();

			JavaClass[] classes = source.getClasses();
			for (int j = 0; j < classes.length; j++) {
				DocletTag[] tags = classes[j].getTags();
				IAbstractArtifactInternal matched = null;
				for (int k = 0; k < tags.length; k++) {
					if (tags[k].getName().equals(model.getMarkingTag())) {
						if (matched == null) {
							// It's a match let's extract it
							extracted = model.extractFromClass(classes[j],
									this, monitor);
							extracted.setTSProject(getTSProject());
							extracted.setJavaSource(source);

							matched = model;
						} else {
							BasePlugin
									.log(new Status(IStatus.WARNING, BasePlugin
											.getPluginId(), "Class "
											+ classes[j]
													.getFullyQualifiedName()
											+ " contains multiple markingTags"));
						}

					}
				}
			}
		}
		return extracted;

	}

	private IAbstractArtifactInternal extractArtifactModel(JavaSource source)
			throws TigerstripeException {
		for (Iterator<IAbstractArtifact> iter = this.discoverableArtifacts
				.iterator(); iter.hasNext();) {
			IAbstractArtifactInternal model = (IAbstractArtifactInternal) iter
					.next();

			JavaClass[] classes = source.getClasses();
			for (int j = 0; j < classes.length; j++) {
				DocletTag[] tags = classes[j].getTags();
				IAbstractArtifactInternal matched = null;
				for (int k = 0; k < tags.length; k++) {
					if (tags[k].getName().equals(model.getMarkingTag())) {
						if (matched == null)
							return model;
						else {
							BasePlugin
									.log(new Status(IStatus.WARNING, BasePlugin
											.getPluginId(), "Class "
											+ classes[j]
													.getFullyQualifiedName()
											+ " contains multiple markingTags"));
						}
					}
				}
			}
		}
		return null;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#extractArtifact(java.io.Reader, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IAbstractArtifactInternal extractArtifact(Reader reader,
			IProgressMonitor monitor) throws TigerstripeException {
		if (wasDisposed) {
			return null;
		}

		try {
			JavaDocBuilder builder = new JavaDocBuilder();
			JavaSource source = builder.addSource(reader);
			return extractArtifact(source, monitor);
		} catch (ParseException e) {
			throw new TigerstripeException("Error trying to parse Artifact", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#extractArtifactModel(java.io.Reader)
	 */
	public IAbstractArtifactInternal extractArtifactModel(Reader reader)
			throws TigerstripeException {
		if (wasDisposed) {
			return null;
		}

		try {
			JavaDocBuilder builder = new JavaDocBuilder();
			JavaSource source = builder.addSource(reader);
			return extractArtifactModel(source);
		} catch (ParseException e) {
			throw new TigerstripeException(
					"Error trying to parse Artifact Model", e);
		}
	}

	// ==================================================
	// Artifact management

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#addArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void addArtifact(IAbstractArtifact iartifact,
			IProgressMonitor monitor) throws TigerstripeException {
		if (wasDisposed) {
			return;
		}

		if (iartifact == null)
			return;

		IAbstractArtifactInternal oldArtifact = null;
		IAbstractArtifactInternal artifact = null;
		try {
			writeLock.lock();
			// Is there already an entry here for this artifact? If so, we
			// need
			// to replace all the occurences in the caches.
			// Bug #690: make sure we look in the back store.
			oldArtifact = (IAbstractArtifactInternal) namedArtifactsMap
					.getBackingMap()
					.get(iartifact.getFullyQualifiedName());

			artifact = (IAbstractArtifactInternal) iartifact;

			if (artifact.getArtifactManager() != this) {
				artifact.setArtifactManager(this);
			}

			artifact.resolveReferences(monitor);

			IAbstractArtifactInternal model = artifact.getModel();
			JavaSource source = artifact.getJavaSource();

			// Add it to the right bucket...
			addToExtractedMap(model, artifact, oldArtifact);
			addToNamedArtifactsMap(artifact.getFullyQualifiedName(), artifact,
					oldArtifact);
			addToSourceMap(source, artifact, oldArtifact);
			addToFilenameMap(artifact);

			if (iartifact instanceof IRelationship) {
				IRelationship rel = (IRelationship) iartifact;

				String aEndFQN = null;
				String zEndFQN = null;

				IRelationshipEnd aEnd = rel.getRelationshipAEnd();
				if (aEnd != null && aEnd.getType() != null) {
					aEndFQN = aEnd.getType().getFullyQualifiedName();
				}

				IRelationshipEnd zEnd = rel.getRelationshipZEnd();
				if (zEnd != null && zEnd.getType() != null) {
					zEndFQN = zEnd.getType().getFullyQualifiedName();
				}

				if (aEndFQN != null && zEndFQN != null) {
					if (oldArtifact instanceof IRelationship) {
						relationshipCache.addRelationship(rel,
								(IRelationship) oldArtifact);
					} else {
						relationshipCache.addRelationship(rel, null);
					}
				}
			}

			// // since 2.2-beta making sure that references to old artifact
			// are updated properly
			if (oldArtifact != null && oldArtifact != iartifact) {
				oldArtifact.updateExtendingArtifacts(artifact);
				oldArtifact.removePackageContainment();
				oldArtifact.dispose();
			}
		} finally {
			mgrChanged = true;
			writeLock.unlock();
		}

		// Notify the listeners only if it is a true addition
		if (artifact != null)
			if (oldArtifact == null)
				notifyArtifactAdded(artifact);
			else {
				notifyArtifactChanged(artifact, oldArtifact);
			}
	}

	private void addToFilenameMap(IAbstractArtifact artifact)
			throws TigerstripeException {
		String artifactPath = artifact.getArtifactPath();
		if (artifactPath != null) {
			filenameMap.put(getAbsolutePath(artifactPath), artifact);
		}
	}

	private String getAbsolutePath(String artifactPath) {
		String baseDir = getTSProject().getBaseDir().toString();
		String fullPath = baseDir + File.separator + artifactPath;
		File f = new File(fullPath);
		return f.getAbsolutePath();
	}

	private void removeFromFilenameMap(IAbstractArtifactInternal artifact)
			throws TigerstripeException {
		String artifactPath = artifact.getArtifactPath();
		if (artifactPath != null) {
			filenameMap.remove(getAbsolutePath(artifactPath));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#removeArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact)
	 */
	public void removeArtifact(IAbstractArtifact artifact)
			throws TigerstripeException {
		removeArtifact(artifact,
				Collections.<ITigerstripeModelProject> emptySet());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#removeArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact, java.util.Set)
	 */
	public void removeArtifact(IAbstractArtifact artifact,
			Set<ITigerstripeModelProject> ignoreProjects)
			throws TigerstripeException {

		if (wasDisposed) {
			return;
		}

		if (artifact == null)
			return;

		try {
			writeLock.lock();
			removeFromExtractedMap(artifact);
			removeFromNamedArtifactsMap(artifact);
			removeFromSourceMap(artifact);
			removeFromFilenameMap((IAbstractArtifactInternal) artifact);

			((IAbstractArtifactInternal) artifact).removeReferences();

			if (artifact instanceof IRelationship) {
				getRelationshipCache().removeRelationship(
						(IRelationship) artifact, ignoreProjects);
			}

			((IAbstractArtifactInternal) artifact).dispose();
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		} finally {
			mgrChanged = true;
			writeLock.unlock();
		}
		notifyArtifactRemoved(artifact);
	}

	private void removeFromExtractedMap(IAbstractArtifact artifact) {
		IAbstractArtifact model = ((IAbstractArtifactInternal) artifact)
				.getModel();
		PredicatedList<IAbstractArtifact> bucket = (PredicatedList<IAbstractArtifact>) extractedMap
				.get(model);
		if (bucket != null) {
			IAbstractArtifact target = null;
			for (IAbstractArtifact obj : bucket.getBackingList()) {
				if (obj instanceof IAbstractArtifact
						&& (obj).getFullyQualifiedName().equals(
								artifact.getFullyQualifiedName())) {
					target = obj;
				}
			}

			if (target != null)
				bucket.remove(target);
		}
	}

	private void removeFromNamedArtifactsMap(IAbstractArtifact artifact) {
		namedArtifactsMap.remove(artifact.getFullyQualifiedName());
	}

	private void removeFromSourceMap(IAbstractArtifact artifact) {
		Object source = ((IAbstractArtifactInternal) artifact).getJavaSource();
		if (source != null) {
			sourceMap.remove(source);
		}
	}

	// ==================================================
	// Logic for Chained ArtifactMgrs
	protected Collection<IAbstractArtifact> getArtifactsByModelInChained(
			IAbstractArtifactInternal model, ExecutionContext context) {
		try {
			readLock.lock();
			ArrayList<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
			result.addAll(depContentCache.getArtifactsByModelInChained(model,
					context));
			result.addAll(toContextProjectAwareArtifacts(getArtifactsByModelInReferences(
					model, context)));
			result.addAll(toContextProjectAwareArtifacts(getArtifactsByModelInInstalledModules(
					model, context)));
			return result;
		} finally {
			readLock.unlock();
		}
	}

	protected Collection<IAbstractArtifact> getAllChainedArtifacts(
			ExecutionContext context) {
		try {
			readLock.lock();
			ArrayList<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
			result.addAll(depContentCache.getAllChainedArtifacts(context));
			result.addAll(toContextProjectAwareArtifacts(getAllArtifactsFromReferences(context)));
			result.addAll(toContextProjectAwareArtifacts(getAllArtifactsFromInstalledModules(context)));
			return result;
		} finally {
			readLock.unlock();
		}
	}

	private <T> Collection<T> toContextProjectAwareArtifacts(
			Collection<T> source) {
		ITigerstripeModelProject context = getTSProject().getTSProject();
		if (source.size() > 0 && context != null) {
			Collection<T> result = new ArrayList<T>(
					source.size());
			for (T element : source) {
				if (!(element instanceof IContextProjectAware)) {
					element = ContextProjectAwareProxy
							.newInstance(element, context);
				}
				result.add(element);
			}
			return result;
		}
		return source;
	}

	protected IAbstractArtifactInternal getArtifactByFullyQualifiedNameInChained(
			String name, ExecutionContext context) {
		try {
			readLock.lock();
			IAbstractArtifactInternal result = depContentCache
					.getArtifactByFullyQualifiedNameInChained(name, context);
			if (result != null)
				return result;
			result = getArtifactByFullyQualifiedNameInReferences(name, context);
			if (result == null) {
				result = getArtifactByFullyQualifiedNameInInstalledModules(
						name, context);
			}
			ITigerstripeModelProject contextProject = getTSProject()
					.getTSProject();
			if (result != null && contextProject != null
					&& !(result instanceof IContextProjectAware)) {
				result = ContextProjectAwareProxy
						.newInstance(result, contextProject);
			}
			return result;
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getProjectDependencies()
	 */
	public IDependency[] getProjectDependencies() {
		if (wasDisposed) {
			return new IDependency[0];
		}

		return getTSProject().getEnabledDependencies();
	}

	// Access to artifacts living in the Referenced projects
	protected Collection<IAbstractArtifact> getAllArtifactsFromReferences(
			ExecutionContext context) {
		try {
			readLock.lock();
			addSelfToCycle(context, Cycles.REFERENCES_ALL);
			ArrayList<IAbstractArtifact> list = new ArrayList<IAbstractArtifact>();
			for (ITigerstripeModelProject project : getTSProject().getEnabledReferencedProjects()) {
				try {
					if (!context.addToCycle(Cycles.REFERENCES_ALL,
							project.getModelId())) {
						continue;
					}
					IArtifactManagerSession session = project
							.getArtifactManagerSession();
					IArtifactQuery query = session
							.makeQuery(IQueryAllArtifacts.class.getName());
					query.setIncludeDependencies(false); // NM: Exclude transitive dependencies 			
					query.setExecutionContext(context);
					list.addAll(project.getArtifactManagerSession()
							.queryArtifact(query));
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
			return list;
		} finally {
			readLock.unlock();
		}
	}

	protected Collection<IAbstractArtifact> getAllArtifactsFromInstalledModules(
			ExecutionContext context) {

		try {
			readLock.lock();
			addSelfToCycle(context, Cycles.INSTALLED_MODULES_ALL);
			ArrayList<IAbstractArtifact> list = new ArrayList<IAbstractArtifact>();
			for (ModelReference ref : getTSProject().getEnabledModelReferences()) {
				try {
					if (!ref.isInstalledModuleReference())
						continue;
					ITigerstripeModelProject project = ref.getResolvedModel();

					if (!context.addToCycle(Cycles.INSTALLED_MODULES_ALL,
							project.getModelId())) {
						continue;
					}

					IArtifactManagerSession session = project
							.getArtifactManagerSession();
					IArtifactQuery query = session
							.makeQuery(IQueryAllArtifacts.class.getName());
					query.setIncludeDependencies(false); // NM: Exclude transitive dependencies	
					query.setExecutionContext(context);
					list.addAll(project.getArtifactManagerSession()
							.queryArtifact(query));
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
			return list;
		} finally {
			readLock.unlock();
		}
	}

	private boolean addSelfToCycle(ExecutionContext ctx, ICycle cycle) {
		ITigerstripeModelProject mp = getTSProject().getTSProject();
		try {
			if (mp != null) {
				return ctx.addToCycle(cycle, mp.getModelId());
			} else {
				return false;
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage(
					"Can't get model id for project", e);
			return false;
		}
	}

	protected IAbstractArtifactInternal getArtifactByFullyQualifiedNameInReferences(
			String name, ExecutionContext context) {
		try {
			readLock.lock();
			addSelfToCycle(context, Cycles.REFERENCES_FQN);
			IAbstractArtifact result = null;
			for (ITigerstripeModelProject project : getTSProject().getEnabledReferencedProjects()) {
				try {
					if (!context.addToCycle(Cycles.REFERENCES_FQN,
							project.getModelId())) {
						continue;
					}
					IArtifactManagerSession session = project
							.getArtifactManagerSession();
					// do not include dependencies
					result = session.getArtifactByFullyQualifiedName(name,
							context);
					if (result != null)
						return (IAbstractArtifactInternal) result;
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
			return (IAbstractArtifactInternal) result;
		} finally {
			readLock.unlock();
		}
	}

	protected Collection<IAbstractArtifact> getArtifactsByModelInReferences(
			IAbstractArtifactInternal model, ExecutionContext context) {
		try {
			readLock.lock();
			addSelfToCycle(context, Cycles.REFERENCES_BY_MODEL);
			ArrayList<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
			for (ITigerstripeModelProject project : getTSProject().getEnabledReferencedProjects()) {
				try {
					if (!context.addToCycle(Cycles.REFERENCES_BY_MODEL,
							project.getModelId())) {
						continue;
					}
					IArtifactManagerSession session = project
							.getArtifactManagerSession();
					IQueryArtifactsByType query = (IQueryArtifactsByType) session
							.makeQuery(IQueryArtifactsByType.class.getName());
					query.setArtifactType(model.getClass().getName());
					query.setIncludeDependencies(false); // NM: Exclude transitive dependencies
					query.setExecutionContext(context);
					// referenced project
					// shall not be included

					result.addAll(project.getArtifactManagerSession()
							.queryArtifact(query));
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
			return result;
		} finally {
			readLock.unlock();
		}
	}

	protected IAbstractArtifactInternal getArtifactByFullyQualifiedNameInInstalledModules(
			String name, ExecutionContext context) {
		try {
			readLock.lock();
			addSelfToCycle(context, Cycles.INSTALLED_MODULES_FQN);
			IAbstractArtifact result = null;
			for (ModelReference ref : getTSProject().getEnabledModelReferences()) {
				try {
					if (!ref.isInstalledModuleReference())
						continue;
					ITigerstripeModelProject project = ref.getResolvedModel();

					if (!context.addToCycle(Cycles.INSTALLED_MODULES_FQN,
							project.getModelId())) {
						continue;
					}

					IArtifactManagerSession session = project
							.getArtifactManagerSession();
					// do not include dependencies
					result = session.getArtifactByFullyQualifiedName(name,
							context);
					if (result != null)
						return (IAbstractArtifactInternal) result;
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
			return (IAbstractArtifactInternal) result;
		} finally {
			readLock.unlock();
		}
	}

	protected Collection<IAbstractArtifact> getArtifactsByModelInInstalledModules(
			IAbstractArtifactInternal model, ExecutionContext context) {
		try {
			readLock.lock();
			addSelfToCycle(context, Cycles.INSTALLED_MODULES_BY_MODEL);
			ArrayList<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
			for (ModelReference ref : getTSProject().getEnabledModelReferences()) {
				try {
					if (!ref.isInstalledModuleReference())
						continue;
					ITigerstripeModelProject project = ref.getResolvedModel();

					if (!context.addToCycle(Cycles.INSTALLED_MODULES_BY_MODEL,
							project.getModelId())) {
						continue;
					}

					IArtifactManagerSession session = project
							.getArtifactManagerSession();
					IQueryArtifactsByType query = (IQueryArtifactsByType) session
							.makeQuery(IQueryArtifactsByType.class.getName());
					query.setArtifactType(model.getClass().getName());
					query.setIncludeDependencies(false); // NM: Exclude transitive dependencies				
					query.setExecutionContext(context);
					// referenced project
					// shall not be included

					result.addAll(project.getArtifactManagerSession()
							.queryArtifact(query));
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
			return result;
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getAllKnownArtifactsByFullyQualifiedName(java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
			String fqn, IProgressMonitor monitor) {
		return getAllKnownArtifactsByFullyQualifiedName(fqn,
				newContext(monitor));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getAllKnownArtifactsByFullyQualifiedName(java.lang.String, org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext)
	 */
	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
			String fqn, ExecutionContext context) {
		if (wasDisposed) {
			return Collections.emptyList();
		}

		try {
			readLock.lock();
			ArrayList<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
			// Returns any local definition first
			IAbstractArtifact localArt = getArtifactByFullyQualifiedName(fqn,
					false, context);
			if (localArt != null) {
				result.add(localArt);
			}
			
			// get modules references then
			Collection<IAbstractArtifact> moduleArts = getAllKnownArtifactsByFullyQualifiedNameInModules(
					fqn, context);
			if (moduleArts.size() != 0) {
				for (IAbstractArtifact art : moduleArts)
					result.add(art);
			}
			// get the references projects last
			Collection<IAbstractArtifact> projectsArts = getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(
					fqn, context);
			if (projectsArts.size() != 0) {
				for (IAbstractArtifact art : projectsArts)
					result.add(art);
			}
			return result;
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getAllKnownArtifactsByFullyQualifiedNameInModules(java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInModules(
			String fqn, IProgressMonitor monitor) {
		return getAllKnownArtifactsByFullyQualifiedNameInModules(fqn,
				newContext(monitor));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getAllKnownArtifactsByFullyQualifiedNameInModules(java.lang.String, org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext)
	 */
	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInModules(
			String fqn, ExecutionContext context) {
		if (wasDisposed) {
			return Collections.emptyList();
		}

		try {
			readLock.lock();
			Collection<IAbstractArtifact> list = depContentCache
					.getAllKnownArtifactsByFullyQualifiedName(fqn, context);
			return list;
		} finally {
			readLock.unlock();
		}
	}

	private ExecutionContext newContext() {
		return newContext(new NullProgressMonitor());
	}

	private ExecutionContext newContext(IProgressMonitor monitor) {
		if (monitor == null) {
			return new ExecutionContext(new NullProgressMonitor());
		} else {
			return new ExecutionContext(monitor);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(java.lang.String)
	 */
	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(
			String fqn) {
		return getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(
				fqn, newContext());

	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(java.lang.String, org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext)
	 */
	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(
			String fqn, ExecutionContext context) {
		if (wasDisposed) {
			return Collections.emptyList();
		}

		try {
			readLock.lock();
			addSelfToCycle(context, Cycles.REFERENCES_FQN_ALL);
			ArrayList<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();
			for (ITigerstripeModelProject project : getTSProject().getEnabledReferencedProjects()) {
				try {
					if (!context.addToCycle(Cycles.REFERENCES_FQN_ALL,
							project.getModelId())) {
						continue;
					}
					ArtifactManager manager = project
							.getArtifactManagerSession().getArtifactManager();
					result.addAll(toContextProjectAwareArtifacts(manager
							.getAllKnownArtifactsByFullyQualifiedName(fqn,
									context)));
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
			return result;
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getOriginatingRelationshipForFQN(java.lang.String, boolean)
	 */
	public List<IRelationship> getOriginatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies) throws TigerstripeException {
		if (wasDisposed) {
			return Collections.emptyList();
		}

		try {
			readLock.lock();
			return getOriginatingRelationshipForFQN(fqn,
					includeProjectDependencies, false);
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getOriginatingRelationshipForFQN(java.lang.String, boolean, boolean)
	 */
	public List<IRelationship> getOriginatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies, boolean ignoreFacets)
			throws TigerstripeException {
		if (wasDisposed) {
			return Collections.emptyList();
		}

		try {
			readLock.lock();
			List<IRelationship> result = new ArrayList<IRelationship>();
			result.addAll(relationshipCache.getRelationshipsOriginatingFromFQN(
					fqn, ignoreFacets));

			if (includeProjectDependencies) {
				for (ITigerstripeModelProject project : collectReferencedProjectsAndDependencies()) {
					/*
					 * Do not check for cycles. This method can't raise
					 * stackoverflow, because it is not recursive
					 */
					ArtifactManagerImpl mgr = (ArtifactManagerImpl) ((ArtifactManagerSessionImpl) project
							.getArtifactManagerSession()).getArtifactManager();
					result.addAll(toContextProjectAwareArtifacts(mgr.getRelationshipCache()
							.getRelationshipsOriginatingFromFQN(fqn,
									ignoreFacets)));
				}
			}
			return result;
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getTerminatingRelationshipForFQN(java.lang.String, boolean)
	 */
	public List<IRelationship> getTerminatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies) throws TigerstripeException {
		if (wasDisposed) {
			return Collections.emptyList();
		}

		try {
			readLock.lock();
			return getTerminatingRelationshipForFQN(fqn,
					includeProjectDependencies, false);
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getTerminatingRelationshipForFQN(java.lang.String, boolean, boolean)
	 */
	public List<IRelationship> getTerminatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies, boolean ignoreFacet)
			throws TigerstripeException {
		if (wasDisposed) {
			return Collections.emptyList();
		}

		try {
			readLock.lock();
			List<IRelationship> result = new ArrayList<IRelationship>();
			result.addAll(relationshipCache.getRelationshipsTerminatingInFQN(
					fqn, ignoreFacet));

			if (includeProjectDependencies) {
				
				for (ITigerstripeModelProject project : collectReferencedProjectsAndDependencies()) {
					/*
					 * Do not check for cycles. This method can't raise
					 * stackoverflow, because it is not recursive
					 */
					ArtifactManagerImpl mgr = (ArtifactManagerImpl) ((ArtifactManagerSessionImpl) project
							.getArtifactManagerSession()).getArtifactManager();
					result.addAll(toContextProjectAwareArtifacts(mgr.getRelationshipCache()
							.getRelationshipsTerminatingInFQN(fqn, ignoreFacet)));
				}
			}
			return result;
		} finally {
			readLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#collectReferencedProjectsAndDependencies()
	 */
	public List<ITigerstripeModelProject> collectReferencedProjectsAndDependencies()
			throws TigerstripeException {

		ArrayList<ITigerstripeModelProject> models = new ArrayList<ITigerstripeModelProject>();
		
		for (ITigerstripeModelProject project : tsProject.getEnabledReferencedProjects()) {
			models.add(project);
		}
		for (IDependency d : tsProject.getEnabledDependencies()) {
			models.add(d.makeModuleProject(tsProject.getTSProject()));
		}
		return models;
	}
	
	protected ArtifactRelationshipCache getRelationshipCache() {
		return relationshipCache;
	}

	// This is a backdoor used in the TSDeleteAction to let the Art Mgr know
	// that an artifact was deleted after the fact.
	// Really the Art Mgr should be listenning for Workspace Changes here
	// and figure it out on its own.
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#notifyArtifactDeleted(org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact)
	 */
	public void notifyArtifactDeleted(IAbstractArtifact artifact) {
		if (wasDisposed) {
			return;
		}

		try {
			URI oldURI = (URI) artifact.getAdapter(URI.class);
			String simpleName = artifact.getClass().getSimpleName();

			removeArtifact(artifact);

			// push a notif
			ModelChangeDelta delta = new ModelChangeDelta(
					IModelChangeDelta.REMOVE);
			delta.setFeature(simpleName);
			delta.setOldValue(oldURI);
			delta.setProject(artifact.getProject());
			delta.setSource(this);

			TigerstripeWorkspaceNotifier.INSTANCE.signalModelChange(delta);

		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#notifyArtifactRenamed(org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact, java.lang.String)
	 */
	public void notifyArtifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		if (wasDisposed) {
			return;
		}
		if (shouldNotify
				&& (broadcastMask & IArtifactChangeListener.NOTIFY_RENAMED) == IArtifactChangeListener.NOTIFY_RENAMED) {
			for (IArtifactChangeListener listener : listeners) {
				try {
					listener.artifactRenamed(artifact, fromFQN);
				} catch (Exception e) {
					// We need to make sure that all listeners will be
					// notified even if one handler fails.
					BasePlugin.log(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#renameArtifact(org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact, java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void renameArtifact(IAbstractArtifact artifact, String toFQN,
			IProgressMonitor monitor) throws TigerstripeException {
		if (wasDisposed) {
			return;
		}

		String fromFQN = artifact.getFullyQualifiedName();
		try {
			writeLock.lock();
			shouldNotify = false; // we don't want to trigger del+add
			// notifications, only a Ren at the end
			removeArtifact(artifact);
			((IAbstractArtifactInternal) artifact).setProxy(false); // a side
																		// effect
																		// of
			// the remove is to
			// setIsProxy. don't
			// want that here.
			artifact.setFullyQualifiedName(toFQN);
			addArtifact(artifact, monitor);
		} finally {
			shouldNotify = true;
			writeLock.unlock();
		}
		notifyArtifactRenamed(artifact, fromFQN);
	}

	// =====================================================================
	// =====================================================================
	private ArrayList<IActiveFacetChangeListener> facetListeners = new ArrayList<IActiveFacetChangeListener>();

	private final ReadWriteLock facetListenersLock = new ReentrantReadWriteLock();

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#addActiveFacetListener(org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener)
	 */
	public void addActiveFacetListener(IActiveFacetChangeListener listener) {
		if (wasDisposed) {
			return;
		}
		Lock lwriteLock = facetListenersLock.writeLock();
		try {
			lwriteLock.lock();
			if (!facetListeners.contains(listener))
				facetListeners.add(listener);
		} finally {
			lwriteLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#removeActiveFacetListener(org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener)
	 */
	public void removeActiveFacetListener(IActiveFacetChangeListener listener) {
		if (wasDisposed) {
			return;
		}
		Lock lwriteLock = facetListenersLock.writeLock();
		try {
			lwriteLock.lock();
			facetListeners.remove(listener);
		} finally {
			lwriteLock.unlock();
		}
	}

	private void notifyFacetChanged(IFacetReference oldFacet) {

		// Make sure we invalidate any filtered list within artifacts of this
		// Mgr
		clearFacetCacheInArtifacts();

		Lock freadLock = facetListenersLock.readLock();
		try {
			freadLock.lock();
			if (shouldNotify && !isLocked) { // no notification on during
				// generation
				for (IActiveFacetChangeListener listener : facetListeners) {
					try {
						listener.facetChanged(oldFacet, activeFacet);
					} catch (Exception e) {
						// this will ensure we finish the loop in case of
						// Exception raised in the handler
						BasePlugin.log(e);
					}
				}
			}
		} finally {
			freadLock.unlock();
		}
	}

	/**
	 * Bug 928: When a facet is applied here, it must be propagated to all
	 * referenced projects and dependencies. The dependencies cache needs to be
	 * refreshed too.
	 * 
	 * @param oldFacet
	 * @param newFacet
	 * @param monitor
	 * @throws TigerstripeException
	 */
	private void propagateFacetChangeToDependencies(IFacetReference oldFacet,
			IFacetReference newFacet, IProgressMonitor monitor)
			throws TigerstripeException {
		if (newFacet == null) {
			// reseting all referenced projects
			for (ITigerstripeModelProject project : getTSProject().getEnabledReferencedProjects()) {
				try {
					project.getArtifactManagerSession().resetActiveFacet();
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}

			// need to take care of modules too
			depContentCache.resetActiveFacet();
			relationshipCache.resetActiveFacet();

			if (phantomArtifactMgrSession != null)
				phantomArtifactMgrSession.resetActiveFacet();

		} else {
			// this is a new facet being set
			for (ITigerstripeModelProject project : getTSProject().getEnabledReferencedProjects()) {
				try {
					project.getArtifactManagerSession().setActiveFacet(
							newFacet, monitor);
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", e);
				}
			}
			depContentCache.setActiveFacet(newFacet);
			relationshipCache.setActiveFacet(newFacet);

			if (phantomArtifactMgrSession != null)
				phantomArtifactMgrSession.setActiveFacet(newFacet, monitor);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getActiveFacet()
	 */
	public IFacetReference getActiveFacet() throws TigerstripeException {
		return activeFacet;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#resetActiveFacet()
	 */
	public void resetActiveFacet() throws TigerstripeException {
		if (wasDisposed) {
			return;
		}
		IFacetReference oldFacet = activeFacet;
		activeFacet = null;
		try {
			writeLock.lock();
			resetScopingPredicate();
		} finally {
			writeLock.unlock();
		}
		propagateFacetChangeToDependencies(oldFacet, null,
				new NullProgressMonitor()); // FIXME: monitor?
		notifyFacetChanged(oldFacet);
	}

	// although facets are handled with Predicated lists in the artifact manager
	// to handle artifact-level
	// scoping, inside the artifacts artifact-component-level scoping is
	// handling in a lazy way: i.e. upon
	// first request, the lists of Fields/Methods/Labels that need to be
	// filtered out are built and then
	// cached. This method allow to clear the cache when the facet is reset or
	// changed.
	private void clearFacetCacheInArtifacts() {
		for (AbstractArtifact art : (Collection<AbstractArtifact>) this.namedArtifactsMap
				.getBackingMap().values()) {
			art.resetFacetFilteredLists();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#setActiveFacet(org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void setActiveFacet(IFacetReference facetRef,
			IProgressMonitor monitor) throws TigerstripeException {
		if (wasDisposed) {
			return;
		}
		if (facetRef.canResolve()) {
			IFacetReference oldFacet = activeFacet;
			activeFacet = facetRef;
			try {
				writeLock.lock();
				setScopingPredicate(facetRef.getFacetPredicate());
			} finally {
				writeLock.unlock();
			}
			propagateFacetChangeToDependencies(oldFacet, activeFacet, monitor);
			notifyFacetChanged(oldFacet);
			return;
		}
		throw new TigerstripeException("Can't resolve Facet ("
				+ facetRef.getURI().toASCIIString() + ")");
	}

	private void setScopingPredicate(Predicate predicate)
			throws TigerstripeException {

		// The case of the extracted Map is slightly different in that it is a
		// regular
		// Map of PredicatedLists
		for (Object obj : extractedMap.values()) {
			PredicatedList<IAbstractArtifactInternal> bucket = (PredicatedList<IAbstractArtifactInternal>) obj;
			bucket.setPredicate(predicate);
		}

		namedArtifactsMap.setPredicate(predicate);
		// No predicate for filenameMap
		// No predicate for sourceMap

		// Notify listeners of change of predicate
		// TODO
	}

	private void resetScopingPredicate() throws TigerstripeException {
		setScopingPredicate(null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#isInActiveFacet(org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact)
	 */
	public boolean isInActiveFacet(IAbstractArtifact artifact)
			throws TigerstripeException {
		if (wasDisposed) {
			return false;
		}
		if (getActiveFacet() == null)
			return true;

		// Can't use isInScope() because artifact may have not been extracted
		// yet
		// and the Explorer needs it right away...
		Predicate activePredicate = namedArtifactsMap.getPredicate();
		return activePredicate == null || activePredicate.evaluate(artifact);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#resetBroadcastMask()
	 */
	public void resetBroadcastMask() {
		setBroadcastMask(DEFAULT_BROADCASTMASK);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#setBroadcastMask(int)
	 */
	public void setBroadcastMask(int broadcastMask) {
		this.broadcastMask = broadcastMask;
	}

	private void updateLocalTimeStamp() {
		this.localTimeStamp = System.currentTimeMillis();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#getLocalTimeStamp()
	 */
	public long getLocalTimeStamp() {
		return this.localTimeStamp;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#annotationChanged(org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta[])
	 */

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#descriptorChanged(org.eclipse.core.resources.IResource)
	 */
	public void descriptorChanged(IResource changedDescriptor) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#modelChanged(org.eclipse.tigerstripe.workbench.IModelChangeDelta[])
	 */
	public void modelChanged(IModelChangeDelta[] delta) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#projectAdded(org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject)
	 */
	public void projectAdded(IAbstractTigerstripeProject project) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#projectDeleted(java.lang.String)
	 */
	public void projectDeleted(String projectName) {
		// TODO Auto-generated method stub

	}

	// This will be proviked by changes to .java or .project file changes
	// in the underlying fle system
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#artifactResourceChanged(org.eclipse.core.resources.IResource)
	 */
	public void artifactResourceChanged(IResource changedArtifactResource) {
		if (wasDisposed) {
			return;
		}
		try {
			IProject p = (IProject) getTSProject().getAdapter(IProject.class);
			if (changedArtifactResource.getProject().equals(p)) {
				if (changedArtifactResource instanceof IFile) {
					Reader reader;

					reader = new InputStreamReader(
							((IFile) changedArtifactResource).getContents());
					IAbstractArtifactInternal aArtifact = extractArtifact(
							reader, null);
					// An Add replaces the existing
					addArtifact(aArtifact, null);

				}
			}
		} catch (Exception e) {
			BasePlugin.logErrorMessage(
					"Failed to update ArtifactManager from changed Resource "
							+ changedArtifactResource.getFullPath(), e);
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#artifactResourceAdded(org.eclipse.core.resources.IResource)
	 */
	public void artifactResourceAdded(IResource addedArtifactResource) {
		if (wasDisposed) {
			return;
		}
		try {
			IProject p = (IProject) getTSProject().getAdapter(IProject.class);
			if (addedArtifactResource.getProject().equals(p)) {
				if (addedArtifactResource instanceof IFile) {
					Reader reader;

					reader = new InputStreamReader(
							((IFile) addedArtifactResource).getContents());
					IAbstractArtifactInternal aArtifact = extractArtifact(
							reader, null);
					// An Add replaces the existing
					addArtifact(aArtifact, null);

				}
			}
		} catch (Exception e) {
			BasePlugin.logErrorMessage(
					"Failed to update ArtifactManager from added Resource "
							+ addedArtifactResource.getFullPath(), e);
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#artifactResourceRemoved(org.eclipse.core.resources.IResource)
	 */
	public void artifactResourceRemoved(IResource removedArtifactResource) {
		/*
		 * Move delete artifact logic to class WorkspaceListener. See comments
		 * in this method about artifact deletion.
		 */
	}
    
    /* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#activeFacetChanged(org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject)
	 */
    public void activeFacetChanged(ITigerstripeModelProject project) {
        // TODO Auto-generated method stub
        
    }

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#addDisposeListener(org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager1.IDisposeListener)
	 */
	public void addDisposeListener(IDisposeListener listener) {
		if (wasDisposed) {
			return;
		}
		disposeListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager#removeDisposeListener(org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager1.IDisposeListener)
	 */
	public void removeDisposeListener(IDisposeListener listener) {
		if (wasDisposed) {
			return;
		}
		disposeListeners.remove(listener);
	}

	private void fireDispose() {
		for (IDisposeListener listener : disposeListeners) {
			listener.onDispose();
		}
	}

	/**
	 * Used for avoid cyclic references in projects, installed modules,
	 * dependencies
	 */
	private static enum Cycles implements ICycle {
		REFERENCES_ALL, REFERENCES_FQN, REFERENCES_BY_MODEL, REFERENCES_FQN_ALL,

		INSTALLED_MODULES_ALL, INSTALLED_MODULES_FQN, INSTALLED_MODULES_BY_MODEL,

		PHANTOM;
	}
}