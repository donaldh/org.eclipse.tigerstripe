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
package org.eclipse.tigerstripe.workbench.model.deprecated_;

import java.util.Collection;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;

public interface ISessionArtifact extends IAbstractArtifact{

	public interface IEntityMethodFlavorDetails  {

		/**
		 * Sets the comment (plain text description) for this FlavorDetails.
		 * @param description
		 */
		public void setComment(String description);

		public void setFlag(String flag);

		/**
		 * Add an exception to this flavor details.
		 * @param fqn
		 */
		public void addException(String fqn);

		/**
		 * Renmove an exception form this flavor details.
		 * @param fqn
		 */
		public void removeException(String fqn);

		/**
		 * Return the comment (plain text description) for this FlavorDetails.
		 * @return string.
		 */
		public String getComment();

		/**
		 * Returns a Collection of String - each of which is the Fully Qualified
		 * Name of any exceptions attached to this Flavor. If there are no
		 * exceptions, an empty Collection is returned.
		 * 
		 * @return Collection<String> - Fully Qualified Names of Exceptions
		 */
		public Collection<String> getExceptions();

		/**
		 * Returns a String value indicating the status of the Flavor.
		 * 
		 * @return String - "true", "false" or "optional"
		 */
		public String getFlag();
	}

	public interface IManagedEntityDetails  {

		/**
		 * Set the Fully Qualified Name of the  ManagedEntityDetails.
		 * This should be the fqn of a Managed Entity Artifact in the project. 
		 * 
		 * @param fqn
		 */
		public void setFullyQualifiedName(String fqn);

		/**
		 * Clone this ManagedEntityDetails.
		 * @return
		 */
		public IManagedEntityDetails clone();

		/**
		 * Returns the Flavor Details for the *Create* variant of this flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getCreateFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for the *Create* variant of this flavor.
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavor -
		 *            String name of the Flavor
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getCreateFlavorDetailsStr(
				String flavor);

		/**
		 * Returns the Flavor Details for this flavor of a custom defined
		 * method.
		 * 
		 * @param methodId -
		 *            String identifier of the method
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getCustomMethodFlavorDetails(
				String methodId, OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for this flavor of a custom defined
		 * method.
		 * 
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param methodId -
		 *            String - unique Id of the method.
		 * @param flavor -
		 *            String - name of the Flavor
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getCustomMethodFlavorDetailsStr(
				String methodId, String flavor);

		/**
		 * Returns the *Default* Flavor Details for the *Create* variant of this
		 * flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getDefaultCreateFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the *Default* Flavor Details for the *Create* variant of this
		 * flavor.
		 * 
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavorStr -
		 *            String - name of the Flavor
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getDefaultCreateFlavorDetailsStr(
				String flavorStr);

		/**
		 * Returns the Flavor Details for this flavor of a custom defined
		 * method. Note the Default Details are the same for all custom methods -
		 * there is no need to supply a MethodId.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getDefaultCustomMethodFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for this flavor of a custom defined
		 * method. Note the Default Details are the same for all custom methods -
		 * there is no need to supply a MethodId.
		 * 
		 * @param flavorStr -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getDefaultCustomMethodFlavorDetailsStr(
				String flavorStr);

		/**
		 * Returns the *Default* Flavor Details for the *Get* variant of this
		 * flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getDefaultGetFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the *Default* Flavor Details for the *Get* variant of this
		 * flavor.
		 * 
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavorStr -
		 *            String - name of the Flavor
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getDefaultGetFlavorDetailsStr(
				String flavorStr);

		/**
		 * Returns the *Default* Flavor Details for the *Remove* variant of this
		 * flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getDefaultRemoveFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the *Default* Flavor Details for the *Remove* variant of this
		 * flavor.
		 * 
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavorStr -
		 *            String - name of the Flavor
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getDefaultRemoveFlavorDetailsStr(
				String flavorStr);

		/**
		 * Returns the *Default* Flavor Details for the *Set* variant of this
		 * flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getDefaultSetFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the *Default* Flavor Details for the *Set* variant of this
		 * flavor.
		 * 
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavorStr -
		 *            flavorStr - name of the Flavor
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getDefaultSetFlavorDetailsStr(
				String flavorStr);

		/**
		 * Returns the fully qualified name (ie. package + name) of this Managed
		 * Entity Detail.
		 * 
		 * @return String - fully qualified name of this Managed Entity Detail
		 */
		public String getFullyQualifiedName();

		/**
		 * Returns the Flavor Details for the *Get* variant of this flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getGetFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for the *Get* variant of this flavor. This
		 * method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavor -
		 *            String name of the Flavor
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getGetFlavorDetailsStr(
				String flavor);

		/**
		 * Returns the name associated with this Managed Entity Detail
		 * 
		 * @return String - the name of the Managed Entity Detail
		 */
		public String getName();

		/**
		 * Returns the Flavor Details for the *Remove* variant of this flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getRemoveFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for the *Remove* variant of this flavor.
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavor -
		 *            String name of the Flavor
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getRemoveFlavorDetailsStr(
				String flavor);

		/**
		 * Returns the Flavor Details for the *Set* variant of this flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getSetFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for the *Set* variant of this flavor. This
		 * method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavor -
		 *            String name of the Flavor
		 * 
		 * @return IEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IEntityMethodFlavorDetails getSetFlavorDetailsStr(
				String flavor);

		/**
		 * returns true if this managed entity can be resolved to a
		 * corresponding artifact in the project.
		 * 
		 * If not, it's either because the artifact manager hasn't been built up
		 * yet, or because the corresponding artifact doesn't exist.
		 * 
		 * @return boolean - indicates resolve status
		 */
		public boolean isResolvedToArtifact();

	}

	public interface INamedQuery  {
		
		/**
		 * Set the Fully Qualified Name of the  Named Query.
		 * This should be the fqn of a Named Query Artifact in the project. 
		 * 
		 * @param fqn
		 */
		public void setFullyQualifiedName(String fqn);

		/**
		 * Returns the fully qualified name (ie. package + name) of this Named
		 * Query.
		 * 
		 * @return String - fully qualified name of this Named Query
		 */
		public String getFullyQualifiedName();

		/**
		 * Returns the name of this Named Query.
		 * 
		 * @return String - the name of this Named Query
		 */
		public String getName();
	}

	public interface IEmittedEvent  {
		/**
		 * Set the Fully Qualified Name of the  Emitted Event.
		 * This should be the fqn of an Event Artifact in the project. 
		 * 
		 * @param fqn
		 */
		public void setFullyQualifiedName(String fqn);

		/**
		 * Returns the fully qualified name (ie. package + name) of this Event.
		 * 
		 * @return String - fully qualified name of this Event
		 */
		public String getFullyQualifiedName();

		/**
		 * Returns the name of this Event.
		 * 
		 * @return String - the name of this Event
		 */
		public String getName();
	}

	public interface IExposedUpdateProcedure  {
		
		/**
		 * Set the Fully Qualified Name of the  Exposed Update Procedure.
		 * This should be the fqn of a Update Procedure Artifact in the project. 
		 * 
		 * @param fqn
		 */
		public void setFullyQualifiedName(String fqn);

		/**
		 * Returns the fully qualified name (ie. package + name) of this Update
		 * Procedure.
		 * 
		 * @return String - fully qualified name of this Update Procedure
		 */
		public String getFullyQualifiedName();

		/**
		 * Returns the name of this Update Procedure.
		 * 
		 * @return String - the name of this Update Procedure
		 */
		public String getName();
	}

	/** 
	 * Return the managedEntity details that are exposed through this
	 * session.
	 * 
	 * @return collection of managedEntityDetails
	 */
	public Collection<IManagedEntityDetails> getManagedEntityDetails();

	/**
	 * Add/sets IManagedEntityDetails for the target entity If such managed
	 * entity already exists, the details are overwritten.
	 * 
	 * @param details
	 */
	public void addManagedEntityDetails(IManagedEntityDetails details);

	/**
	 * Remove managedEntityDetails from the session.
	 * 
	 * @param details
	 */
	public void removeManagedEntityDetails(IManagedEntityDetails[] details);

	/**
	 * Factory class for managedEntityDetails.
	 * 
	 * @return a blank managedEntityDetails
	 */
	public IManagedEntityDetails makeManagedEntityDetails();

	/** 
	 * Return the nameQueries that are exposed through this
	 * session.
	 * 
	 * @return collection of namedQueries
	 */
	public Collection<INamedQuery> getNamedQueries();

	/**
	 * Add/sets INamedQuery for the target entity If such named
	 * query already exists, the details are overwritten.
	 * 
	 * @param details
	 */
	public void addNamedQuery(INamedQuery details);

	/**
	 * Remove namedQueries from the session.
	 * 
	 * @param details
	 */
	public void removeNamedQuery(INamedQuery[] details);

	/**
	 * Factory class for namedQuery.
	 * 
	 * @return a blank namedQuery
	 */
	public INamedQuery makeNamedQuery();

	/** 
	 * Return the updateProcedures that are exposed through this
	 * session.
	 * 
	 * @return collection of updateProcedures
	 */
	public Collection<IExposedUpdateProcedure> getExposedUpdateProcedures();

	/**
	 * Add/sets IExposedUpdateProcedure for the target entity If such update
	 * procedure already exists, the details are overwritten.
	 * 
	 * @param details
	 */
	public void addExposedUpdateProcedure(IExposedUpdateProcedure details);

	/**
	 * Remove exposedUpdateProcedures from the session.
	 * 
	 * @param details
	 */
	public void removeExposedUpdateProcedure(IExposedUpdateProcedure[] details);

	/**
	 * Factory class for exposedUpdateProcedure.
	 * 
	 * @return a blank exposedUpdateProcedure
	 */
	public IExposedUpdateProcedure makeExposedUpdateProcedure();

	/** 
	 * Return the emittedEvents that are exposed through this
	 * session.
	 * 
	 * @return collection of emittedEvents
	 */
	public Collection<IEmittedEvent> getEmittedEvents();

	/**
	 * Add/sets IEmittedEvent for the target entity If such emitted
	 * event already exists, the details are overwritten.
	 * 
	 * @param details
	 */
	public void addEmittedEvent(IEmittedEvent details);

	/**
	 * Remove emittedEvents from the session.
	 * 
	 * @param details
	 */
	public void removeEmittedEvent(IEmittedEvent[] details);

	/**
	 * Factory class for emittedEvent.
	 * 
	 * @return a blank emittedEvent
	 */
	public IEmittedEvent makeEmittedEvent();

}
