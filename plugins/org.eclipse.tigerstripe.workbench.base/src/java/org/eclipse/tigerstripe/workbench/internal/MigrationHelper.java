/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;

/**
 * This helper contains all the hardcoded values that may be found in XML files
 * that need to be migrated as a result of the new open-source version.
 * 
 * @author erdillon
 * 
 */
public class MigrationHelper {

	public static String profileMigrateAnnotationArtifactLevelType(String scope) {

		// Migrate from Commercial to open-source version.
		if ("com.tigerstripesoftware.api.artifacts.model.ossj.IManagedEntityArtifact"
				.equals(scope))
			return IManagedEntityArtifact.class.getName();
		else if ("com.tigerstripesoftware.api.artifacts.model.ossj.IDatatypeArtifact"
				.equals(scope))
			return IDatatypeArtifact.class.getName();
		else if ("com.tigerstripesoftware.api.artifacts.model.ossj.IExceptionArtifact"
				.equals(scope))
			return IExceptionArtifact.class.getName();
		else if ("com.tigerstripesoftware.api.artifacts.model.ossj.IEnumArtifact"
				.equals(scope))
			return IEnumArtifact.class.getName();
		else if ("com.tigerstripesoftware.api.artifacts.model.ossj.ISessionArtifact"
				.equals(scope))
			return ISessionArtifact.class.getName();
		else if ("com.tigerstripesoftware.api.artifacts.model.ossj.IQueryArtifact"
				.equals(scope))
			return IQueryArtifact.class.getName();
		else if ("com.tigerstripesoftware.api.artifacts.model.ossj.IUpdateProcedureArtifact"
				.equals(scope))
			return IUpdateProcedureArtifact.class.getName();
		else if ("com.tigerstripesoftware.api.artifacts.model.ossj.IEventArtifact"
				.equals(scope))
			return IEventArtifact.class.getName();
		else if ("com.tigerstripesoftware.api.artifacts.model.IAssociationArtifact"
				.equals(scope))
			return IAssociationArtifact.class.getName();
		else if ("com.tigerstripesoftware.api.artifacts.model.IAssociationClassArtifact"
				.equals(scope))
			return IAssociationClassArtifact.class.getName();
		else if ("com.tigerstripesoftware.api.artifacts.model.IDependencyArtifact"
				.equals(scope))
			return IDependencyArtifact.class.getName();

		return scope;
	}
}
