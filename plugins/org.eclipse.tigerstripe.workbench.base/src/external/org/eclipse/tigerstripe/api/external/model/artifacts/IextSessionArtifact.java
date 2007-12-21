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
package org.eclipse.tigerstripe.api.external.model.artifacts;

import java.util.Collection;

import org.eclipse.tigerstripe.api.external.model.IextMethod.OssjEntityMethodFlavor;

/**
 * This class represents a Session Facade Artifact.
 * 
 * A Session Facade exposes a number of things: Managed Entities Named Queries
 * Update Procedures Events
 * 
 */
public interface IextSessionArtifact extends IArtifact {
	/**
	 * This class represents details of a Flavor of a Method of an attached
	 * Managed Entity. It is designed specifically for OSS/J usage.
	 * 
	 */
	public interface IextEntityMethodFlavorDetails {

		/**
		 * 
		 */
		public String getComment();

		/**
		 * Returns a String value indicating the status of the Flavor.
		 * 
		 * @return String - "true", "false" or "optional"
		 */
		public String getFlag();

		/**
		 * Returns a Collection of String - each of which is the Fully Qualified
		 * Name of any exceptions attached to this Flavor. If there are no
		 * exceptions, an empty Collection is returned.
		 * 
		 * @return Collection<String> - Fully Qualified Names of Exceptions
		 */
		public Collection<String> getExceptions();

	}

	/**
	 * This class represents details of an attached Managed Entity. It is
	 * required to allow overriding of information within the session.
	 * 
	 * In most cases there will be a simple 1:1 relationship between an
	 * ManagedEntity and a ManagedEntityDetails.
	 * 
	 */
	public interface IextManagedEntityDetails {
		/**
		 * Returns the fully qualified name (ie. package + name) of this Managed
		 * Entity Detail.
		 * 
		 * @return String - fully qualified name of this Managed Entity Detail
		 */
		public String getFullyQualifiedName();

		/**
		 * Returns the name associated with this Managed Entity Detail
		 * 
		 * @return String - the name of the Managed Entity Detail
		 */
		public String getName();

		/**
		 * Returns the Flavor Details for the *Create* variant of this flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getCreateFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for the *Set* variant of this flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getSetFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for the *Get* variant of this flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getGetFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for the *Remove* variant of this flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getRemoveFlavorDetails(
				OssjEntityMethodFlavor flavor);

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
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getCustomMethodFlavorDetails(
				String methodId, OssjEntityMethodFlavor flavor);

		/**
		 * Returns the *Default* Flavor Details for the *Create* variant of this
		 * flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getDefaultCreateFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the *Default* Flavor Details for the *Set* variant of this
		 * flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getDefaultSetFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the *Default* Flavor Details for the *Get* variant of this
		 * flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getDefaultGetFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the *Default* Flavor Details for the *Remove* variant of this
		 * flavor.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getDefaultRemoveFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for this flavor of a custom defined
		 * method. Note the Default Details are the same for all custom methods -
		 * there is no need to supply a MethodId.
		 * 
		 * @param flavor -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getDefaultCustomMethodFlavorDetails(
				OssjEntityMethodFlavor flavor);

		/**
		 * Returns the Flavor Details for the *Create* variant of this flavor.
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavor -
		 *            String name of the Flavor
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getCreateFlavorDetailsStr(
				String flavor);

		/**
		 * Returns the Flavor Details for the *Set* variant of this flavor. This
		 * method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavor -
		 *            String name of the Flavor
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getSetFlavorDetailsStr(
				String flavor);

		/**
		 * Returns the Flavor Details for the *Get* variant of this flavor. This
		 * method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavor -
		 *            String name of the Flavor
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getGetFlavorDetailsStr(
				String flavor);

		/**
		 * Returns the Flavor Details for the *Remove* variant of this flavor.
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavor -
		 *            String name of the Flavor
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getRemoveFlavorDetailsStr(
				String flavor);

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
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getCustomMethodFlavorDetailsStr(
				String methodId, String flavor);

		/**
		 * Returns the *Default* Flavor Details for the *Create* variant of this
		 * flavor.
		 * 
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavorStr -
		 *            String - name of the Flavor
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getDefaultCreateFlavorDetailsStr(
				String flavorStr);

		/**
		 * Returns the *Default* Flavor Details for the *Set* variant of this
		 * flavor.
		 * 
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavorStr -
		 *            flavorStr - name of the Flavor
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getDefaultSetFlavorDetailsStr(
				String flavorStr);

		/**
		 * Returns the *Default* Flavor Details for the *Get* variant of this
		 * flavor.
		 * 
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavorStr -
		 *            String - name of the Flavor
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getDefaultGetFlavorDetailsStr(
				String flavorStr);

		/**
		 * Returns the *Default* Flavor Details for the *Remove* variant of this
		 * flavor.
		 * 
		 * This method takes the String name of the flavor as a parameter.
		 * 
		 * @param flavorStr -
		 *            String - name of the Flavor
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getDefaultRemoveFlavorDetailsStr(
				String flavorStr);

		/**
		 * Returns the Flavor Details for this flavor of a custom defined
		 * method. Note the Default Details are the same for all custom methods -
		 * there is no need to supply a MethodId.
		 * 
		 * @param flavorStr -
		 *            OssjEntityMethodFlavor object
		 * 
		 * @return IextEntityMethodFlavorDetails - An object containing details
		 *         of this flavor
		 */
		public IextEntityMethodFlavorDetails getDefaultCustomMethodFlavorDetailsStr(
				String flavorStr);

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

	/**
	 * This class represents a Named Query that is attached to the Session
	 * Facade.
	 * 
	 * 
	 */
	public interface IextNamedQuery {
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

	/**
	 * This class represents an Event that is attached to the Session Facade.
	 * 
	 * 
	 */
	public interface IextEmittedEvent {
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

	/**
	 * This class represents an Update Procedure that is attached to the Session
	 * Facade.
	 * 
	 * 
	 */
	public interface IextExposedUpdateProcedure {
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
	 * Returns an array of the ManagedEntityDetails that represent the Managed
	 * Entities attached to this Session Facade. If there are no attached
	 * Managed Entities then an empty array is returned.
	 * 
	 * @return IextManagedEntityDetails[] - Array of objects representing the
	 *         attached Managed Entities.
	 */
	public IextManagedEntityDetails[] getIextManagedEntityDetails();

	/**
	 * Returns an array of the Named Queries attached to this Session Facade. If
	 * there are no attached Named Queries then an empty array is returned.
	 * 
	 * @return IextNamedQuery[] - Array of objects representing the attached
	 *         Named Queries.
	 */
	public IextNamedQuery[] getIextNamedQueries();

	/**
	 * Returns an array of the Update Procedures attached to this Session
	 * Facade. * If there are no attached Update Procedures then an empty array
	 * is returned.
	 * 
	 * @return IextExposedUpdateProcedure[] - Array of objects representing the
	 *         attached Update Procedures.
	 */
	public IextExposedUpdateProcedure[] getIextExposedUpdateProcedures();

	/**
	 * Returns an array of the Events attached to this Session Facade. * If
	 * there are no attached Events then an empty array is returned.
	 * 
	 * @return IextEmittedEvent[] - Array of objects representing the attached
	 *         Events.
	 */
	public IextEmittedEvent[] getIextEmittedEvents();

}
