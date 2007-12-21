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
package org.eclipse.tigerstripe.api.artifacts.model.ossj;

import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextSessionArtifact;

public interface ISessionArtifact extends IAbstractArtifact,
		IextSessionArtifact {

	public final static String DEFAULT_LABEL = "Session";

	public interface IEntityMethodFlavorDetails extends
			IextEntityMethodFlavorDetails {

		public void setComment(String description);

		public void setFlag(String flag);

		public void addException(String fqn);

		public void removeException(String fqn);
	}

	public interface IManagedEntityDetails extends IextManagedEntityDetails {

		public void setFullyQualifiedName(String fqn);

		public IManagedEntityDetails clone();

	}

	public interface INamedQuery extends IextNamedQuery {
		public void setFullyQualifiedName(String fqn);
	}

	public interface IEmittedEvent extends IextEmittedEvent {

		public void setFullyQualifiedName(String fqn);
	}

	public interface IExposedUpdateProcedure extends IextExposedUpdateProcedure {
		public void setFullyQualifiedName(String fqn);
	}

	public IManagedEntityDetails makeIManagedEntityDetails();

	/**
	 * Add/sets IManagedEntityDetails for the target entity If such managed
	 * entity already exists, the details are overwritten.
	 * 
	 * @param details
	 */
	public void addIManagedEntityDetails(IManagedEntityDetails details);

	public void removeIManagedEntityDetails(IManagedEntityDetails[] details);

	public INamedQuery makeINamedQuery();

	public void addINamedQuery(INamedQuery details);

	public void removeINamedQuery(INamedQuery[] details);

	public IExposedUpdateProcedure makeIExposedUpdateProcedure();

	public void addIExposedUpdateProcedure(IExposedUpdateProcedure details);

	public void removeIExposedUpdateProcedure(IExposedUpdateProcedure[] details);

	public IEmittedEvent makeIEmittedEvent();

	public void addIEmittedEvent(IEmittedEvent details);

	public void removeIEmittedEvent(IEmittedEvent[] details);

	public IEmittedEvent[] getIEmittedEvents();

	public IExposedUpdateProcedure[] getIExposedUpdateProcedures();

	public IManagedEntityDetails[] getIManagedEntityDetails();

	public INamedQuery[] getINamedQueries();

}
