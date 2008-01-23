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
package org.eclipse.tigerstripe.core.model;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.tigerstripe.api.model.IMethod;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.model.ossj.SessionArtifactPersister;
import org.eclipse.tigerstripe.core.model.ossj.specifics.OssjSessionFacadeSpecifics;
import org.eclipse.tigerstripe.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SessionFacadeArtifact extends AbstractArtifact implements
		ISessionArtifact {

	/** logger for output */
	private static Logger log = Logger.getLogger(SessionFacadeArtifact.class);

	// ===== populated during artifact extraction
	private Collection<IManagedEntityDetails> managedEntities;

	private Collection namedQueries;

	private Collection exposedUpdateProcedures;

	private Collection emittedEvents;

	private static final String MANAGED_ENTITY_TAG = "tigerstripe.managed-entity";

	private static final String NAMED_QUERY_TAG = "tigerstripe.named-query";

	private static final String EXPOSED_UPDATEPROCEDURE_TAG = "tigerstripe.exposed-updateProcedure";

	private static final String EMITTED_EVENT_TAG = "tigerstripe.emitted-event";

	public final static String MARKING_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.SESSIONFACADE;

	public final static String LABEL = "Session Facade Artifact";

	public String getArtifactType() {
		return ISessionArtifact.class.getName();
	}

	public String getIArtifactType() {
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
	public AbstractArtifact getModel() {
		return MODEL;
	}

	public String getLabel() {
		return LABEL;
	}

	@Override
	public AbstractArtifact extractFromClass(JavaClass javaClass,
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
		log.debug(" Extracting Session from "
				+ javaClass.getFullyQualifiedName());
		SessionFacadeArtifact result = new SessionFacadeArtifact(javaClass,
				artifactMgr, monitor);

		// #216
		// because there might be session artifacts out there defined with
		// prior versions, we need to set the instance flag properly for
		// compatibility
		for (IMethod method : getIMethods()) {
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
			result.addIEmittedEvent(eevent);
		}

		OssjSessionFacadeSpecifics specifics = (OssjSessionFacadeSpecifics) result
				.getIStandardSpecifics();
		specifics.buildOverides();

		return result;
	}

	public void addManagedEntity(ManagedEntityDetails me) {
		if (!this.managedEntities.contains(me)) {
			log.debug("added " + me.getFullyQualifiedName());
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
			ArtifactManager artifactMgr, ITigerstripeProgressMonitor monitor) {
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

	public Collection getManagedEntities() {
		log.debug("returning " + this.managedEntities.size());
		return this.managedEntities;
	}

	public Collection getNamedQueries() {
		return this.namedQueries;
	}

	public Collection getEmittedEvents() {
		return this.emittedEvents;
	}

	public Collection getExposedUpdateProcedures() {
		return this.exposedUpdateProcedures;
	}

	public class NamedQuery implements INamedQuery {

		private ArtifactManager artifactManager;

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

		public QueryArtifact getArtifact() {
			QueryArtifact result = (QueryArtifact) this.artifactManager
					.getArtifactByFullyQualifiedName(getFullyQualifiedName(),
							true, new TigerstripeNullProgressMonitor());

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

		private ArtifactManager artifactManager;

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

		public UpdateProcedureArtifact getArtifact() {
			UpdateProcedureArtifact result = (UpdateProcedureArtifact) this.artifactManager
					.getArtifactByFullyQualifiedName(getFullyQualifiedName(),
							true, new TigerstripeNullProgressMonitor());

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
		private ArtifactManager artifactManager;

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

		public EventArtifact getArtifact() {
			EventArtifact result = (EventArtifact) this.artifactManager
					.getArtifactByFullyQualifiedName(getFullyQualifiedName(),
							true, new TigerstripeNullProgressMonitor());

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

	public IManagedEntityDetails[] getIManagedEntityDetails() {
		IManagedEntityDetails[] result = new IManagedEntityDetails[managedEntities
				.size()];
		return managedEntities.toArray(result);
	}

	public INamedQuery[] getINamedQueries() {
		INamedQuery[] result = new INamedQuery[namedQueries.size()];
		return (INamedQuery[]) namedQueries.toArray(result);
	}

	public IExposedUpdateProcedure[] getIExposedUpdateProcedures() {
		IExposedUpdateProcedure[] result = new IExposedUpdateProcedure[exposedUpdateProcedures
				.size()];
		return (IExposedUpdateProcedure[]) exposedUpdateProcedures
				.toArray(result);
	}

	public IEmittedEvent[] getIEmittedEvents() {
		IEmittedEvent[] result = new IEmittedEvent[emittedEvents.size()];
		return (IEmittedEvent[]) emittedEvents.toArray(result);
	}

	public void addIManagedEntityDetails(IManagedEntityDetails details) {
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

	public void removeIManagedEntityDetails(IManagedEntityDetails[] details) {
		managedEntities.removeAll(Arrays.asList(details));
	}

	public void addINamedQuery(INamedQuery details) {
		namedQueries.add(details);
	}

	public void removeINamedQuery(INamedQuery[] details) {
		namedQueries.removeAll(Arrays.asList(details));
	}

	public void addIExposedUpdateProcedure(IExposedUpdateProcedure proc) {
		exposedUpdateProcedures.add(proc);
	}

	public void removeIExposedUpdateProcedure(IExposedUpdateProcedure[] proc) {
		exposedUpdateProcedures.removeAll(Arrays.asList(proc));
	}

	public void addIEmittedEvent(IEmittedEvent details) {
		emittedEvents.add(details);
	}

	public void removeIEmittedEvent(IEmittedEvent[] details) {
		emittedEvents.removeAll(Arrays.asList(details));
	}

	public IManagedEntityDetails makeIManagedEntityDetails() {
		return new ManagedEntityDetails(getArtifactManager());
	}

	public INamedQuery makeINamedQuery() {
		return new NamedQuery(getArtifactManager());
	}

	public IEmittedEvent makeIEmittedEvent() {
		return new EmittedEvent(getArtifactManager());
	}

	public IExposedUpdateProcedure makeIExposedUpdateProcedure() {
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

	@Override
	public Collection getReferencedArtifacts() {
		Collection result = super.getReferencedArtifacts();

		Collection<ManagedEntityDetails> managedEntityDetails = getManagedEntities();
		for (ManagedEntityDetails details : managedEntityDetails) {
			result.add(details.getFullyQualifiedName());
		}

		Collection<NamedQuery> queries = getNamedQueries();
		for (NamedQuery query : queries) {
			result.add(query.getFullyQualifiedName());
		}

		Collection<ExposedUpdateProcedure> procs = getExposedUpdateProcedures();
		for (ExposedUpdateProcedure proc : procs) {
			result.add(proc.getFullyQualifiedName());
		}

		Collection<EmittedEvent> events = getEmittedEvents();
		for (EmittedEvent event : events) {
			result.add(event.getFullyQualifiedName());
		}

		return result;
	}

	@Override
	public void addIMethod(IMethod method) {
		super.addIMethod(method);

		// #216 Make sure the method is not an instance method for a Session
		// artifact
		method.setInstanceMethod(false);
	}


}