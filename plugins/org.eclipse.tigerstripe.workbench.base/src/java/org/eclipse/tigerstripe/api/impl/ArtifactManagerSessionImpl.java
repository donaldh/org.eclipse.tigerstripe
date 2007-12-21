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
package org.eclipse.tigerstripe.api.impl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.artifacts.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.api.artifacts.IArtifactChangeListener;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationEnd;
import org.eclipse.tigerstripe.api.artifacts.model.IDependencyArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IType;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEventArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IExceptionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IQueryArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IRelationship;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationClassArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextDatatypeArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextDependencyArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextEnumArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextEventArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextExceptionArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextManagedEntityArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextQueryArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextSessionArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextUpdateProcedureArtifact;
import org.eclipse.tigerstripe.api.external.queries.IArtifactQuery;
import org.eclipse.tigerstripe.api.external.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.api.external.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.api.external.queries.IQueryCapabilitiesArtifacts;
import org.eclipse.tigerstripe.api.external.queries.IQueryModelArtifacts;
import org.eclipse.tigerstripe.api.external.queries.IQueryRelationshipsByArtifact;
import org.eclipse.tigerstripe.api.external.queries.IQuerySessionArtifacts;
import org.eclipse.tigerstripe.api.impl.updater.ModelUpdaterImpl;
import org.eclipse.tigerstripe.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.contract.segment.FacetReference;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.core.model.EnumArtifact;
import org.eclipse.tigerstripe.core.model.EventArtifact;
import org.eclipse.tigerstripe.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.core.model.PrimitiveTypeArtifact;
import org.eclipse.tigerstripe.core.model.QueryArtifact;
import org.eclipse.tigerstripe.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.core.module.ModuleArtifactManager;
import org.eclipse.tigerstripe.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class ArtifactManagerSessionImpl implements IArtifactManagerSession {

	private ArtifactManager artifactManager;

	private IModelUpdater modelUpdater;

	// =======================================================================

	protected ArtifactManagerSessionImpl(ArtifactManager artifactManager) {
		super();
		this.artifactManager = artifactManager;
	}

	public Class[] getSupportedArtifactClasses() {
		Class[] potentials = { IManagedEntityArtifact.class,
				IDatatypeArtifact.class, IEventArtifact.class,
				IQueryArtifact.class, IExceptionArtifact.class,
				ISessionArtifact.class, IEnumArtifact.class,
				IUpdateProcedureArtifact.class, IAssociationArtifact.class,
				IAssociationClassArtifact.class, IPrimitiveTypeArtifact.class,
				IDependencyArtifact.class };

		ArrayList<Class> result = new ArrayList<Class>();

		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) API
				.getIWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);

		for (Class pot : potentials) {
			if (prop.getDetailsForType(pot.getName()).isEnabled()) {
				result.add(pot);
			}
		}

		return result.toArray(new Class[result.size()]);
	}

	public String[] getSupportedArtifacts() {
		Class[] classes = getSupportedArtifactClasses();
		String[] classNames = new String[classes.length];
		for (int i = 0; i < classes.length; i++) {
			classNames[i] = classes[i].getName();
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

		refresh(new TigerstripeNullProgressMonitor()); // FIXME
		return base.run(this);
	}

	public String[] getSupportedQueries() {
		return new String[] { IQueryAllArtifacts.class.getName(),
				IQueryModelArtifacts.class.getName(),
				IQueryCapabilitiesArtifacts.class.getName(),
				IQuerySessionArtifacts.class.getName(),
				IQueryArtifactsByType.class.getName(),
				IQueryRelationshipsByArtifact.class.getName() };
	}

	protected IArtifactQuery[] getQueryImplementations() {
		return new IArtifactQuery[] { new QueryAllArtifacts(),
				new QueryModelArtifacts(), new QueryCapabilitiesArtifacts(),
				new QuerySessionArtifacts(), new QueryArtifactsByType(),
				new QueryRelationshipsByArtifact() };
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
		String[] supportedArtifacts = getSupportedArtifacts();
		int index = -1;
		for (int i = 0; i < supportedArtifacts.length; i++) {
			if (supportedArtifacts[i].equals(type)) {
				index = i;
			}
		}

		if (index == -1)
			throw new IllegalArgumentException("Unknown artifact type: " + type);

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
		return null;
	}

	public AbstractArtifact getArtifactByFullyQualifiedName(String fqn) {
		return getArtifactByFullyQualifiedName(fqn, true);
	}

	public AbstractArtifact getArtifactByFullyQualifiedName(String fqn,
			boolean includeDependencies) {
		return getArtifactManager().getArtifactByFullyQualifiedName(fqn,
				includeDependencies, new TigerstripeNullProgressMonitor()); // FIXME

	}

	public AbstractArtifact getArtifactByFullyQualifiedName(String fqn,
			boolean includeDependencies, boolean isOverridePredicate) {
		return getArtifactManager().getArtifactByFullyQualifiedName(fqn,
				includeDependencies, isOverridePredicate,
				new TigerstripeNullProgressMonitor()); // FIXME
	}

	public IAbstractArtifact extractArtifact(Reader reader,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
		return artifactManager.extractArtifact(reader, monitor);
	}

	public void addArtifact(IAbstractArtifact artifact)
			throws TigerstripeException {
		artifactManager.addArtifact(artifact,
				new TigerstripeNullProgressMonitor()); // FIXME
	}

	public void removeArtifact(IAbstractArtifact artifact)
			throws TigerstripeException {
		artifactManager.removeArtifact(artifact);
	}

	public void refresh(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {
		refresh(false, monitor);
	}

	public void refresh(boolean forceReload, ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {

		// @since 2.1: when generating module no need to refresh manager
		// since this is a module.
		if (getArtifactManager() instanceof ModuleArtifactManager)
			return;
		getArtifactManager().refresh(forceReload, monitor);
	}

	public void refreshReferences(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {
		getArtifactManager().refreshReferences(monitor);
	}

	public void updateCaches(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {
		getArtifactManager().updateCaches(monitor);
	}

	public void refreshAll(ITigerstripeProgressMonitor monitor)
			throws TigerstripeException {
		refreshAll(false, monitor);
	}

	public void refreshAll(boolean forceReload,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
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
		result.setExtendedIArtifact(orig.getExtendedIArtifact());
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
			IType aType = resAEnd.makeIType();
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
			IType zType = resZEnd.makeIType();
			zType.setFullyQualifiedName(origAssoc.getZEnd().getType()
					.getFullyQualifiedName());
			resZEnd.setType(zType);
			resultAssoc.setZEnd(resZEnd);
		} else if (orig instanceof DependencyArtifact
				&& model instanceof DependencyArtifact) {
			DependencyArtifact origDep = (DependencyArtifact) orig;
			DependencyArtifact resultDep = (DependencyArtifact) result;
			IType aType = resultDep.makeIType();
			aType.setFullyQualifiedName(origDep.getAEndType()
					.getFullyQualifiedName());
			resultDep.setAEndType(aType);
			IType zType = resultDep.makeIType();
			zType.setFullyQualifiedName(origDep.getZEndType()
					.getFullyQualifiedName());
			resultDep.setZEndType(zType);
		}

		result.setIFields(orig.getIFields());
		result.setILabels(orig.getILabels());
		result.setIMethods(orig.getIMethods());
		return result;
	}

	public IAbstractArtifact[] getAllKnownArtifactsByFullyQualifiedName(
			String fqn) {
		return getArtifactManager().getAllKnownArtifactsByFullyQualifiedName(
				fqn, new TigerstripeNullProgressMonitor()); // FIXME
	}

	// //////////////////////////////////////////
	// external API stuff
	// //////////////////////////////////////////

	public IArtifact[] getAllKnownIArtifactsByFullyQualifiedName(String fqn) {
		return getAllKnownArtifactsByFullyQualifiedName(fqn);
	}

	public IArtifact getIArtifactByFullyQualifiedName(String fqn,
			boolean includeDependencies) {
		return getArtifactByFullyQualifiedName(fqn, includeDependencies);
	}

	public IArtifact getIArtifactByFullyQualifiedName(String fqn) {
		return getArtifactByFullyQualifiedName(fqn);
	}

	public String[] getSupportedIArtifacts() {
		return new String[] { IextManagedEntityArtifact.class.getName(),
				IextDatatypeArtifact.class.getName(),
				IextEventArtifact.class.getName(),
				IextQueryArtifact.class.getName(),
				IextExceptionArtifact.class.getName(),
				IextSessionArtifact.class.getName(),
				IextEnumArtifact.class.getName(),
				IextUpdateProcedureArtifact.class.getName(),
				IextAssociationArtifact.class.getName(),
				IextAssociationClassArtifact.class.getName(),
				IextPrimitiveTypeArtifact.class.getName(),
				IextDependencyArtifact.class.getName() };
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
				.getAllArtifacts(false, new TigerstripeNullProgressMonitor()); // FIXME
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
				.getAllArtifacts(false, new TigerstripeNullProgressMonitor()); // FIXME
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
				new TigerstripeNullProgressMonitor()); // FIXME
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

	public void setActiveFacet(IFacetReference facet,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException {
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

	public IPrimitiveTypeArtifact[] getReservedPrimitiveTypes()
			throws TigerstripeException {
		return getArtifactManager().getReservedPrimitiveTypeArtifacts();
	}

	public void resetBroadcastMask() throws TigerstripeException {
		getArtifactManager().resetBroadcastMask();
	}

	public void setBroadcastMask(int broadcastMask) throws TigerstripeException {
		getArtifactManager().setBroadcastMask(broadcastMask);
	}

}
