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

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.ModelUpdaterImpl;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.contract.segment.FacetReference;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.queries.IQueryRelationshipsByArtifact;

public class ArtifactManagerSessionImpl implements IArtifactManagerSession {

	private ArtifactManager artifactManager;

	private IModelUpdater modelUpdater;
	
	// =======================================================================

	protected ArtifactManagerSessionImpl(ArtifactManager artifactManager) {
		super();
		this.artifactManager = artifactManager;
	}

	public Collection<Class> getSupportedArtifactClasses() {
		Class[] potentials = { IManagedEntityArtifact.class,
				IDatatypeArtifact.class, IEventArtifact.class,
				IQueryArtifact.class, IExceptionArtifact.class,
				ISessionArtifact.class, IEnumArtifact.class,
				IUpdateProcedureArtifact.class, IAssociationArtifact.class,
				IAssociationClassArtifact.class, IPrimitiveTypeArtifact.class,
				IDependencyArtifact.class };

		ArrayList<Class> result = new ArrayList<Class>();

		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);

		for (Class pot : potentials) {
			if (prop.getDetailsForType(pot.getName()).isEnabled()) {
				result.add(pot);
			}
		}

		return Collections.unmodifiableCollection(result);
	}

	public Collection<String> getSupportedArtifacts() {
		Collection<Class> classes = getSupportedArtifactClasses();
		Collection<String> classNames = new ArrayList<String>();
		for (Class clazz : classes) {
			classNames.add(clazz.getName());
		}
		return classNames;
	}

	// =======================================================================
	// =======================================================================
	// =======================================================================

	/**
	 * Returns the Artifact Manager for this session
	 * 
	 * @return ArtifactManager - The artifact manager for this session
	 */
	public ArtifactManager getArtifactManager() {
		return this.artifactManager;
	}
	
	public Collection queryArtifact(IArtifactQuery query)
			throws IllegalArgumentException, TigerstripeException {
		if (!(query instanceof ArtifactQueryBase))
			throw new IllegalArgumentException();

		ArtifactQueryBase base = (ArtifactQueryBase) query;

		refresh(new NullProgressMonitor()); // FIXME
		return base.run(this);
	}

	public String[] getSupportedQueries() {
		return new String[] { IQueryAllArtifacts.class.getName(),

		IQueryArtifactsByType.class.getName(),
				IQueryRelationshipsByArtifact.class.getName() };
	}

	protected IArtifactQuery[] getQueryImplementations() {
		return new IArtifactQuery[] { new QueryAllArtifacts(),
				new QueryArtifactsByType(), new QueryRelationshipsByArtifact() };
	}

	public IArtifactQuery makeQuery(String queryType)
			throws IllegalArgumentException {

		String[] supportedQueries = getSupportedQueries();
		int index = -1;
		for (int i = 0; i < supportedQueries.length; i++) {
			if (supportedQueries[i].equals(queryType)) {
				index = i;
			}
		}

		if (index == -1)
			throw new IllegalArgumentException();
		return getQueryImplementations()[index];
	}

	public IAbstractArtifact makeArtifact(IAbstractArtifact artifact) {
		if (artifact instanceof IManagedEntityArtifact)
			return new ManagedEntityArtifact(getArtifactManager());
		else if (artifact instanceof IDatatypeArtifact)
			return new DatatypeArtifact(getArtifactManager());
		else if (artifact instanceof IExceptionArtifact)
			return new ExceptionArtifact(getArtifactManager());
		else if (artifact instanceof IQueryArtifact)
			return new QueryArtifact(getArtifactManager());
		else if (artifact instanceof ISessionArtifact)
			return new SessionFacadeArtifact(getArtifactManager());
		else if (artifact instanceof IEnumArtifact)
			return new EnumArtifact(getArtifactManager());
		else if (artifact instanceof IEventArtifact)
			return new EventArtifact(getArtifactManager());
		else if (artifact instanceof IUpdateProcedureArtifact)
			return new UpdateProcedureArtifact(getArtifactManager());
		else if (artifact instanceof IAssociationClassArtifact)
			return new AssociationClassArtifact(getArtifactManager());
		else if (artifact instanceof IAssociationArtifact)
			return new AssociationArtifact(getArtifactManager());
		else if (artifact instanceof IPrimitiveTypeArtifact)
			return new PrimitiveTypeArtifact(getArtifactManager());
		else if (artifact instanceof IDependencyArtifact)
			return new DependencyArtifact(getArtifactManager());
		throw new IllegalArgumentException();
	}

	public IAbstractArtifact makeArtifact(String type)
			throws IllegalArgumentException {

		if (IManagedEntityArtifact.class.getName().equals(type))
			return new ManagedEntityArtifact(getArtifactManager());
		else if (IDatatypeArtifact.class.getName().equals(type))
			return new DatatypeArtifact(getArtifactManager());
		else if (IExceptionArtifact.class.getName().equals(type))
			return new ExceptionArtifact(getArtifactManager());
		else if (IQueryArtifact.class.getName().equals(type))
			return new QueryArtifact(getArtifactManager());
		else if (ISessionArtifact.class.getName().equals(type))
			return new SessionFacadeArtifact(getArtifactManager());
		else if (IEnumArtifact.class.getName().equals(type))
			return new EnumArtifact(getArtifactManager());
		else if (IEventArtifact.class.getName().equals(type))
			return new EventArtifact(getArtifactManager());
		else if (IUpdateProcedureArtifact.class.getName().equals(type))
			return new UpdateProcedureArtifact(getArtifactManager());
		else if (IAssociationClassArtifact.class.getName().equals(type))
			return new AssociationClassArtifact(getArtifactManager());
		else if (IAssociationArtifact.class.getName().equals(type))
			return new AssociationArtifact(getArtifactManager());
		else if (IPrimitiveTypeArtifact.class.getName().equals(type))
			return new PrimitiveTypeArtifact(getArtifactManager());
		else if (IDependencyArtifact.class.getName().equals(type))
			return new DependencyArtifact(getArtifactManager());
		else
			throw new IllegalArgumentException("Unknown artifact type: " + type);
	}

	public AbstractArtifact getArtifactByFullyQualifiedName(String fqn) {
		return getArtifactByFullyQualifiedName(fqn, true);
	}

	public AbstractArtifact getArtifactByFullyQualifiedName(String fqn,
			boolean includeDependencies) {
		return getArtifactManager().getArtifactByFullyQualifiedName(fqn,
				includeDependencies, new NullProgressMonitor()); // FIXME

	}

	public AbstractArtifact getArtifactByFullyQualifiedName(String fqn,
			boolean includeDependencies, boolean isOverridePredicate) {
		return getArtifactManager().getArtifactByFullyQualifiedName(fqn,
				includeDependencies, isOverridePredicate,
				new NullProgressMonitor()); // FIXME
	}

	public IAbstractArtifact extractArtifact(Reader reader,
			IProgressMonitor monitor) throws TigerstripeException {
		return artifactManager.extractArtifact(reader, monitor);
	}

	public void addArtifact(IAbstractArtifact artifact)
			throws TigerstripeException {
		artifactManager.addArtifact(artifact, new NullProgressMonitor()); // FIXME
	}

	public void removeArtifact(IAbstractArtifact artifact)
			throws TigerstripeException {
		artifactManager.removeArtifact(artifact);
	}

	public void refresh(IProgressMonitor monitor) throws TigerstripeException {
		refresh(false, monitor);
	}

	public void refresh(boolean forceReload, IProgressMonitor monitor)
			throws TigerstripeException {

		// @since 2.1: when generating module no need to refresh manager
		// since this is a module.
		if (getArtifactManager() instanceof ModuleArtifactManager)
			return;
		getArtifactManager().refresh(forceReload, monitor);
	}

	public void refreshReferences(IProgressMonitor monitor)
			throws TigerstripeException {
		getArtifactManager().refreshReferences(monitor);
	}

	public void updateCaches(IProgressMonitor monitor)
			throws TigerstripeException {
		getArtifactManager().updateCaches(monitor);
	}

	public void refreshAll(IProgressMonitor monitor)
			throws TigerstripeException {
		refreshAll(false, monitor);
	}

	public void refreshAll(boolean forceReload, IProgressMonitor monitor)
			throws TigerstripeException {
		refreshReferences(monitor);
		refresh(forceReload, monitor);
	}

	public IAbstractArtifact extractArtifactModel(Reader reader)
			throws TigerstripeException {
		return getArtifactManager().extractArtifactModel(reader);
	}

	public IAbstractArtifact makeArtifact(IAbstractArtifact model,
			IAbstractArtifact orig) throws TigerstripeException {
		IAbstractArtifact result = makeArtifact(model);

		result.setComment(orig.getComment());
		result.setFullyQualifiedName(orig.getFullyQualifiedName());
		result.setExtendedArtifact(orig.getExtendedArtifact());
		result.setVisibility(orig.getVisibility());

		if (orig.getIStandardSpecifics() instanceof IOssjArtifactSpecifics) {
			IOssjArtifactSpecifics origISS = (IOssjArtifactSpecifics) orig
					.getIStandardSpecifics();
			IOssjArtifactSpecifics resultISS = (IOssjArtifactSpecifics) result
					.getIStandardSpecifics();

			resultISS.applyDefaults();
			resultISS
					.mergeInterfaceProperties(origISS.getInterfaceProperties());
		}

		if (orig instanceof AssociationArtifact
				&& model instanceof AssociationArtifact) {
			AssociationArtifact origAssoc = (AssociationArtifact) orig;
			AssociationArtifact resultAssoc = (AssociationArtifact) result;

			IAssociationEnd resAEnd = resultAssoc.makeAssociationEnd();
			resAEnd.setAggregation(origAssoc.getAEnd().getAggregation());
			resAEnd.setChangeable(origAssoc.getAEnd().getChangeable());
			resAEnd.setComment(origAssoc.getAEnd().getComment());
			resAEnd.setMultiplicity(origAssoc.getAEnd().getMultiplicity());
			resAEnd.setName(origAssoc.getAEnd().getName());
			resAEnd.setNavigable(origAssoc.getAEnd().isNavigable());
			resAEnd.setOrdered(origAssoc.getAEnd().isOrdered());
			resAEnd.setVisibility(origAssoc.getAEnd().getVisibility());
			IType aType = resAEnd.makeType();
			aType.setFullyQualifiedName(origAssoc.getAEnd().getType()
					.getFullyQualifiedName());
			resAEnd.setType(aType);
			resultAssoc.setAEnd(resAEnd);

			IAssociationEnd resZEnd = resultAssoc.makeAssociationEnd();
			resZEnd.setAggregation(origAssoc.getZEnd().getAggregation());
			resZEnd.setChangeable(origAssoc.getZEnd().getChangeable());
			resZEnd.setComment(origAssoc.getZEnd().getComment());
			resZEnd.setMultiplicity(origAssoc.getZEnd().getMultiplicity());
			resZEnd.setName(origAssoc.getZEnd().getName());
			resZEnd.setNavigable(origAssoc.getZEnd().isNavigable());
			resZEnd.setOrdered(origAssoc.getZEnd().isOrdered());
			resZEnd.setVisibility(origAssoc.getZEnd().getVisibility());
			IType zType = resZEnd.makeType();
			zType.setFullyQualifiedName(origAssoc.getZEnd().getType()
					.getFullyQualifiedName());
			resZEnd.setType(zType);
			resultAssoc.setZEnd(resZEnd);
		} else if (orig instanceof DependencyArtifact
				&& model instanceof DependencyArtifact) {
			DependencyArtifact origDep = (DependencyArtifact) orig;
			DependencyArtifact resultDep = (DependencyArtifact) result;
			IType aType = resultDep.makeType();
			aType.setFullyQualifiedName(origDep.getAEndType()
					.getFullyQualifiedName());
			resultDep.setAEndType(aType);
			IType zType = resultDep.makeType();
			zType.setFullyQualifiedName(origDep.getZEndType()
					.getFullyQualifiedName());
			resultDep.setZEndType(zType);
		}

		result.setFields(orig.getFields());
		result.setLiterals(orig.getLiterals());
		result.setMethods(orig.getMethods());
		return result;
	}

	public Collection<IAbstractArtifact> getAllKnownArtifactsByFullyQualifiedName(
			String fqn) {
		return getArtifactManager().getAllKnownArtifactsByFullyQualifiedName(
				fqn, new NullProgressMonitor());
		// FIXME
	}

	public void addArtifactChangeListener(IArtifactChangeListener listener) {
		getArtifactManager().addArtifactManagerListener(listener);
	}

	public void removeArtifactChangeListener(IArtifactChangeListener listener) {
		getArtifactManager().removeArtifactManagerListener(listener);
	}

	public IModelUpdater getIModelUpdater() {
		if (modelUpdater == null) {
			modelUpdater = new ModelUpdaterImpl(this);
		}
		return modelUpdater;
	}

	public void setLockForGeneration(boolean isLocked) {
		getArtifactManager().lock(isLocked);
	}

	public Set<IRelationship> removePackageContent(String packageName) {

		// As we delete the content of the package, we assemble a list of all
		// the relationship that now have a dangling end. This will be returned
		// so that the caller may decide to cascade delete them or not.
		Set<IRelationship> relToCascadeDelete = new HashSet<IRelationship>();

		if (packageName == null || packageName.length() == 0)
			return Collections.EMPTY_SET;

		List<IAbstractArtifact> forRemovalArtifacts = new ArrayList<IAbstractArtifact>();
		Collection<IAbstractArtifact> artifacts = getArtifactManager()
				.getAllArtifacts(false, new NullProgressMonitor()); // FIXME
		for (IAbstractArtifact artifact : artifacts) {
			if (packageName.equals(artifact.getPackage())) {
				forRemovalArtifacts.add(artifact);
				try {
					relToCascadeDelete.addAll(getOriginatingRelationshipForFQN(
							artifact.getFullyQualifiedName(), false));
					relToCascadeDelete.addAll(getTerminatingRelationshipForFQN(
							artifact.getFullyQualifiedName(), false));
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logErrorMessage(
							"While trying to assess cascade delete list:"
									+ e.getMessage(), e);
				}
			}
		}

		for (IAbstractArtifact artifact : forRemovalArtifacts) {
			try {
				removeArtifact(artifact);
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}

		return relToCascadeDelete;
	}

	public void renamePackageContent(String fromPackageName,
			String toPackageName) {

		if (fromPackageName == null || fromPackageName.length() == 0)
			return;

		if (toPackageName == null || toPackageName.length() == 0) {
			removePackageContent(fromPackageName);
		}

		List<IAbstractArtifact> forRenamingArtifacts = new ArrayList<IAbstractArtifact>();
		Collection<IAbstractArtifact> artifacts = getArtifactManager()
				.getAllArtifacts(false, new NullProgressMonitor()); // FIXME
		for (IAbstractArtifact artifact : artifacts) {
			if (fromPackageName.equals(artifact.getPackage())) {
				forRenamingArtifacts.add(artifact);
			}
		}

		for (IAbstractArtifact artifact : forRenamingArtifacts) {
			try {
				String toFQN = artifact.getName();
				if (toPackageName != null && toPackageName.length() != 0) {
					toFQN = toPackageName + "." + toFQN;
				}
				renameArtifact(artifact, toFQN);
				// removeArtifact(artifact);
				// artifact.setPackage(toPackageName);
				// addArtifact(artifact);
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}
	}

	public void renameArtifact(IAbstractArtifact artifact, String toFQN)
			throws TigerstripeException {
		getArtifactManager().renameArtifact(artifact, toFQN,
				new NullProgressMonitor()); // FIXME
	}

	// ====================================================
	// Facet Management
	public IFacetReference getActiveFacet() throws TigerstripeException {
		return getArtifactManager().getActiveFacet();
	}

	public void resetActiveFacet() throws TigerstripeException {
		if (getActiveFacet() != null) {
			((FacetReference) getActiveFacet()).stopListeningToManager();
		}
		getArtifactManager().resetActiveFacet();
	}

	public void setActiveFacet(IFacetReference facet, IProgressMonitor monitor)
			throws TigerstripeException {
		getArtifactManager().setActiveFacet(facet, monitor);
		((FacetReference) facet).startListeningToManager(getArtifactManager());
	}

	public void addActiveFacetListener(IActiveFacetChangeListener listener) {
		getArtifactManager().addActiveFacetListener(listener);
	}

	public void removeActiveFacetListener(IActiveFacetChangeListener listener) {
		getArtifactManager().removeActiveFacetListener(listener);
	}

	// =======================================================================
	// Upon generation the Artifact mgr needs to change state. In particular,
	// the following should happen:
	// - locking: nothing can be added/removed.
	// - Active facet cannot be ignored: the default behavior when non
	// generate is to ignore facets for lookups/queries.
	public void generationStart() {
		getArtifactManager().generationStart();
	}

	public void generationComplete() {
		getArtifactManager().generationComplete();
	}

	// =======================================================================
	// =======================================================================

	public List<IRelationship> getOriginatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies) throws TigerstripeException {
		return getArtifactManager().getOriginatingRelationshipForFQN(fqn,
				includeProjectDependencies);
	}

	public List<IRelationship> getTerminatingRelationshipForFQN(String fqn,
			boolean includeProjectDependencies) throws TigerstripeException {
		return getArtifactManager().getTerminatingRelationshipForFQN(fqn,
				includeProjectDependencies);
	}

	public Collection<IPrimitiveTypeArtifact> getReservedPrimitiveTypes()
			throws TigerstripeException {
		return getArtifactManager().getReservedPrimitiveTypeArtifacts();
	}

	public void resetBroadcastMask() throws TigerstripeException {
		getArtifactManager().resetBroadcastMask();
	}

	public void setBroadcastMask(int broadcastMask) throws TigerstripeException {
		getArtifactManager().setBroadcastMask(broadcastMask);
	}

	@Override
	public long getLocalTimeStamp() throws TigerstripeException {
		return getArtifactManager().getLocalTimeStamp();
	}

}
