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

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjSessionFacadeSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.artifacts.SessionArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ide.IDE;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 */
public class SessionFacadeArtifact extends AbstractArtifact implements
		ISessionArtifact, IAbstractArtifactInternal {

	// ===== populated during artifact extraction
	private final Collection<IManagedEntityDetails> managedEntities;

	private final Collection namedQueries;

	private final Collection exposedUpdateProcedures;

	private final Collection emittedEvents;

	private static final String MANAGED_ENTITY_TAG = "tigerstripe.managed-entity";

	private static final String NAMED_QUERY_TAG = "tigerstripe.named-query";

	private static final String EXPOSED_UPDATEPROCEDURE_TAG = "tigerstripe.exposed-updateProcedure";

	private static final String EMITTED_EVENT_TAG = "tigerstripe.emitted-event";

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.SESSIONFACADE;

	public String getArtifactType() {
		return ISessionArtifact.class.getName();
	}

	/**
	 * The static MODEL for this type of artifact. This is used by the artifact
	 * manager when extracting the artifacts.
	 */
	public final static SessionFacadeArtifact MODEL = new SessionFacadeArtifact(
			null);

	@Override
	public String getMarkingTag() {
		return SessionFacadeArtifact.MARKING_TAG;
	}

	@Override
	public IAbstractArtifactInternal getModel() {
		return MODEL;
	}

	public String getLabel() {
		return ArtifactMetadataFactory.INSTANCE.getMetadata(
				ISessionArtifactImpl.class.getName()).getLabel(this);
	}

	@Override
	public IAbstractArtifactInternal extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		SessionFacadeArtifact result = new SessionFacadeArtifact(javaClass,
				artifactMgr, monitor);

		// #216
		// because there might be session artifacts out there defined with
		// prior versions, we need to set the instance flag properly for
		// compatibility
		for (IMethod method : getMethods()) {
			method.setInstanceMethod(false);
		}

		// Extracts the list of ManagedEntities
		Collection tags = result.getTagsByName(MANAGED_ENTITY_TAG);
		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			Properties prop = tag.getProperties();
			ManagedEntityDetails me = new ManagedEntityDetails(result
					.getArtifactManager());
			me.setFullyQualifiedName(prop.getProperty("managedEntity"));

			me.setIncludeDescendants(prop.getProperty("include-descendants",
					"true"));
			result.addManagedEntity(me);
		}

		tags = result.getTagsByName(NAMED_QUERY_TAG);
		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			Properties prop = tag.getProperties();
			NamedQuery query = new NamedQuery(result.getArtifactManager());
			query.setFullyQualifiedName(prop.getProperty("query"));
			result.addNamedQuery(query);
		}

		tags = result.getTagsByName(EXPOSED_UPDATEPROCEDURE_TAG);
		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			Properties prop = tag.getProperties();
			ExposedUpdateProcedure proc = new ExposedUpdateProcedure(result
					.getArtifactManager());
			proc.setFullyQualifiedName(prop.getProperty("procedure"));
			result.addExposedUpdateProcedure(proc);
		}

		tags = result.getTagsByName(EMITTED_EVENT_TAG);
		for (Iterator iter = tags.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			Properties prop = tag.getProperties();
			EmittedEvent eevent = new EmittedEvent(result.getArtifactManager());
			eevent.setFullyQualifiedName(prop.getProperty("event"));
			result.addEmittedEvent(eevent);
		}

		OssjSessionFacadeSpecifics specifics = (OssjSessionFacadeSpecifics) result
				.getIStandardSpecifics();
		specifics.buildOverides();

		return result;
	}

	public void addManagedEntity(ManagedEntityDetails me) {
		if (!this.managedEntities.contains(me)) {
			this.managedEntities.add(me);
		}
	}

	public void addNamedQuery(NamedQuery query) {
		if (!this.namedQueries.contains(query)) {
			this.namedQueries.add(query);
		}
	}

	public void addExposedUpdateProcedure(ExposedUpdateProcedure proc) {
		if (!this.exposedUpdateProcedures.contains(proc)) {
			this.exposedUpdateProcedures.add(proc);
		}
	}

	public SessionFacadeArtifact(ArtifactManager artifactMgr) {
		super(artifactMgr);

		this.managedEntities = new ArrayList();
		this.namedQueries = new ArrayList();
		this.emittedEvents = new ArrayList();
		this.exposedUpdateProcedures = new ArrayList();
		OssjSessionFacadeSpecifics specifics = new OssjSessionFacadeSpecifics(
				this);
		setIStandardSpecifics(specifics);
	}

	public SessionFacadeArtifact(JavaClass javaClass,
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		super(javaClass, artifactMgr, monitor);

		this.managedEntities = new ArrayList();
		this.namedQueries = new ArrayList();
		this.emittedEvents = new ArrayList();
		this.exposedUpdateProcedures = new ArrayList();
		OssjSessionFacadeSpecifics specifics = new OssjSessionFacadeSpecifics(
				this);
		specifics.build();
		setIStandardSpecifics(specifics);
	}

	public Collection<IManagedEntityDetails> getManagedEntityDetails() {
		return Collections.unmodifiableCollection(this.managedEntities);
	}

	public Collection<INamedQuery> getNamedQueries() {
		return Collections.unmodifiableCollection(this.namedQueries);
	}

	public Collection<IEmittedEvent> getEmittedEvents() {
		return Collections.unmodifiableCollection(this.emittedEvents);
	}

	public Collection<IExposedUpdateProcedure> getExposedUpdateProcedures() {
		return Collections.unmodifiableCollection(this.exposedUpdateProcedures);
	}

	public class NamedQuery implements INamedQuery {

		private final ArtifactManager artifactManager;

		public NamedQuery(ArtifactManager artifactManager) {
			this.artifactManager = artifactManager;
		}

		protected String fullyQualifiedName;

		public void setFullyQualifiedName(String fullyQualifiedName) {
			this.fullyQualifiedName = fullyQualifiedName;
		}

		public String getFullyQualifiedName() {
			return this.fullyQualifiedName;
		}

		public IQueryArtifact getArtifact() {
			IQueryArtifact result = (IQueryArtifact) this.artifactManager
					.getArtifactByFullyQualifiedName(getFullyQualifiedName(),
							true, new NullProgressMonitor());

			return result;
		}

		public String getName() {
			if (this.fullyQualifiedName.indexOf(".") == -1)
				return this.fullyQualifiedName;
			else {
				String result = fullyQualifiedName.substring(
						this.fullyQualifiedName.lastIndexOf(".") + 1,
						this.fullyQualifiedName.length());
				return result;
			}
		}

		@Override
		public boolean equals(Object arg0) {
			if (arg0 instanceof NamedQuery) {
				NamedQuery other = (NamedQuery) arg0;
				return other.getFullyQualifiedName() != null
						&& other.getFullyQualifiedName().equals(
								getFullyQualifiedName());
			}
			return false;
		}
	}

	public class ExposedUpdateProcedure implements IExposedUpdateProcedure {

		private final ArtifactManager artifactManager;

		public ExposedUpdateProcedure(ArtifactManager artifactManager) {
			this.artifactManager = artifactManager;
		}

		protected String fullyQualifiedName;

		public void setFullyQualifiedName(String fullyQualifiedName) {
			this.fullyQualifiedName = fullyQualifiedName;
		}

		public String getFullyQualifiedName() {
			return this.fullyQualifiedName;
		}

		public IUpdateProcedureArtifact getArtifact() {
			IUpdateProcedureArtifact result = (IUpdateProcedureArtifact) this.artifactManager
					.getArtifactByFullyQualifiedName(getFullyQualifiedName(),
							true, new NullProgressMonitor());

			return result;
		}

		public String getName() {
			if (this.fullyQualifiedName.indexOf(".") == -1)
				return this.fullyQualifiedName;
			else {
				String result = fullyQualifiedName.substring(
						this.fullyQualifiedName.lastIndexOf(".") + 1,
						this.fullyQualifiedName.length());
				return result;
			}
		}

		@Override
		public boolean equals(Object arg0) {
			if (arg0 instanceof ExposedUpdateProcedure) {
				ExposedUpdateProcedure other = (ExposedUpdateProcedure) arg0;
				return other.getFullyQualifiedName() != null
						&& other.getFullyQualifiedName().equals(
								getFullyQualifiedName());
			}
			return false;
		}

	}

	public class EmittedEvent implements IEmittedEvent {
		private final ArtifactManager artifactManager;

		public EmittedEvent(ArtifactManager artifactManager) {
			this.artifactManager = artifactManager;
		}

		protected String fullyQualifiedName;

		public void setFullyQualifiedName(String fullyQualifiedName) {
			this.fullyQualifiedName = fullyQualifiedName;
		}

		public String getFullyQualifiedName() {
			return this.fullyQualifiedName;
		}

		public IEventArtifact getArtifact() {
			IEventArtifact result = (IEventArtifact) this.artifactManager
					.getArtifactByFullyQualifiedName(getFullyQualifiedName(),
							true, new NullProgressMonitor());

			return result;
		}

		public String getName() {
			if (this.fullyQualifiedName.indexOf(".") == -1)
				return this.fullyQualifiedName;
			else {
				String result = fullyQualifiedName.substring(
						this.fullyQualifiedName.lastIndexOf(".") + 1,
						this.fullyQualifiedName.length());
				return result;
			}
		}

		@Override
		public boolean equals(Object arg0) {
			if (arg0 instanceof EmittedEvent) {
				EmittedEvent other = (EmittedEvent) arg0;
				return other.getFullyQualifiedName() != null
						&& other.getFullyQualifiedName().equals(
								getFullyQualifiedName());
			}
			return false;
		}
	}

	@Override
	protected AbstractArtifactPersister getPersister(Writer writer) {
		return new SessionArtifactPersister(this, writer);
	}

	public void addManagedEntityDetails(IManagedEntityDetails details) {
		// if details for the given entity already exist, we replace it
		IManagedEntityDetails existingToRemove = null;
		for (IManagedEntityDetails mDetails : managedEntities) {
			if (mDetails.getFullyQualifiedName().equals(
					details.getFullyQualifiedName())) {
				existingToRemove = mDetails;
			}
		}

		// If we've found an existing one, we remove now.
		if (existingToRemove != null) {
			managedEntities.remove(existingToRemove);
		}
		managedEntities.add(details);
	}

	public void removeManagedEntityDetails(IManagedEntityDetails[] details) {
		managedEntities.removeAll(Arrays.asList(details));
	}

	public void addNamedQuery(INamedQuery details) {
		namedQueries.add(details);
	}

	public void removeNamedQuery(INamedQuery[] details) {
		namedQueries.removeAll(Arrays.asList(details));
	}

	public void addExposedUpdateProcedure(IExposedUpdateProcedure proc) {
		exposedUpdateProcedures.add(proc);
	}

	public void removeExposedUpdateProcedure(IExposedUpdateProcedure[] proc) {
		exposedUpdateProcedures.removeAll(Arrays.asList(proc));
	}

	public void addEmittedEvent(IEmittedEvent details) {
		emittedEvents.add(details);
	}

	public void removeEmittedEvent(IEmittedEvent[] details) {
		emittedEvents.removeAll(Arrays.asList(details));
	}

	public IManagedEntityDetails makeManagedEntityDetails() {
		return new ManagedEntityDetails(getArtifactManager());
	}

	public INamedQuery makeNamedQuery() {
		return new NamedQuery(getArtifactManager());
	}

	public IEmittedEvent makeEmittedEvent() {
		return new EmittedEvent(getArtifactManager());
	}

	public IExposedUpdateProcedure makeExposedUpdateProcedure() {
		return new ExposedUpdateProcedure(getArtifactManager());
	}

	@Override
	protected IAbstractArtifact makeArtifact() {
		return new SessionFacadeArtifact(getArtifactManager());
	}

	public IManagedEntityDetails getManagedEntityByName(String fqn) {
		for (IManagedEntityDetails entity : managedEntities) {
			if (entity.getFullyQualifiedName().equals(fqn))
				return entity;
		}
		return null;
	}

	/*
	 * This is commented out because the new Collection version of
	 * getReferencedArtifacts only takes Artifacts and not Strings. We would
	 * need to look up the Artifact and add that. Canm add back in if required.
	 * 
	 * 
	 * @Override public Collection<IAbstractArtifact> getReferencedIArtifacts() {
	 * Collection<IAbstractArtifact> result = super.getReferencedIArtifacts();
	 * 
	 * Collection<IManagedEntityDetails> managedEntityDetails =
	 * getManagedEntityDetails(); for (IManagedEntityDetails details :
	 * managedEntityDetails) { result.add(details.get); }
	 * 
	 * Collection<INamedQuery> queries = getNamedQueries(); for (INamedQuery
	 * query : queries) { result.add(query); }
	 * 
	 * Collection<IExposedUpdateProcedure> procs =
	 * getExposedUpdateProcedures(); for (IExposedUpdateProcedure proc : procs) {
	 * result.add(proc); }
	 * 
	 * Collection<IEmittedEvent> events = getEmittedEvents(); for
	 * (IEmittedEvent event : events) { result.add(event); }
	 * 
	 * return result; }
	 */

	@Override
	public void addMethod(IMethod method) {
		super.addMethod(method);

		// #216 Make sure the method is not an instance method for a Session
		// artifact
		method.setInstanceMethod(false);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) throws TigerstripeException {
		super.doSave(monitor);
		
		IResource resource = (IResource) this.getAdapter(IResource.class);
		final String EDITOR_ID = "org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.session.SessionArtifactEditor";
		if (resource instanceof IFile ) {
			IFile file = (IFile) resource;
			IEditorDescriptor editorDescriptor = IDE.getDefaultEditor(file);
			if (editorDescriptor==null || (!EDITOR_ID.equals(editorDescriptor))) 
				IDE.setDefaultEditor((IFile)resource, EDITOR_ID);
		}
	}

}