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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.SessionArtifactPersister;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.OssjSessionFacadeSpecifics;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;

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
			ArtifactManager artifactMgr, IProgressMonitor monitor) {
		log.debug(" Extracting Session from "
				+ javaClass.getFullyQualifiedName());
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
 * This is commented out because the new Collection version of getReferencedArtifacts only takes Artifacts and not Strings.
 * We would need to look up the Artifact and add that.
 * Canm add back in if required.
 * 
 * 
	@Override
	public Collection<IAbstractArtifact> getReferencedIArtifacts() {
		Collection<IAbstractArtifact> result = super.getReferencedIArtifacts();

		Collection<IManagedEntityDetails> managedEntityDetails = getManagedEntityDetails();
		for (IManagedEntityDetails details : managedEntityDetails) {
			result.add(details.get);
		}

		Collection<INamedQuery> queries = getNamedQueries();
		for (INamedQuery query : queries) {
			result.add(query);
		}

		Collection<IExposedUpdateProcedure> procs = getExposedUpdateProcedures();
		for (IExposedUpdateProcedure proc : procs) {
			result.add(proc);
		}

		Collection<IEmittedEvent> events = getEmittedEvents();
		for (IEmittedEvent event : events) {
			result.add(event);
		}

		return result;
	}*/

	@Override
	public void addMethod(IMethod method) {
		super.addMethod(method);

		// #216 Make sure the method is not an instance method for a Session
		// artifact
		method.setInstanceMethod(false);
	}


}