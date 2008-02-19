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
package org.eclipse.tigerstripe.workbench.internal.core.util;

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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;

/**
 * 
 * @author Eric Dillon
 * @since 1.1
 */
public class Misc {

	private final static String[][] artifactLabels = {
			{ IManagedEntityArtifact.class.getName(),
					ManagedEntityArtifact.LABEL },
			{ IDatatypeArtifact.class.getName(), DatatypeArtifact.LABEL },
			{ IAssociationArtifact.class.getName(), AssociationArtifact.LABEL },
			{ IAssociationClassArtifact.class.getName(),
					AssociationClassArtifact.LABEL },
			{ IEventArtifact.class.getName(), EventArtifact.LABEL },
			{ IExceptionArtifact.class.getName(), ExceptionArtifact.LABEL },
			{ ISessionArtifact.class.getName(), SessionFacadeArtifact.LABEL },
			{ IQueryArtifact.class.getName(), QueryArtifact.LABEL },
			{ IUpdateProcedureArtifact.class.getName(),
					UpdateProcedureArtifact.LABEL },
			{ IPrimitiveTypeArtifact.class.getName(),
					PrimitiveTypeArtifact.LABEL },
			{ IDependencyArtifact.class.getName(), DependencyArtifact.LABEL },
			{ IEnumArtifact.class.getName(), EnumArtifact.LABEL } };

	public Misc() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Since 1.1 no reference to java.lang.String anymore but rather to String
	 * only
	 * 
	 * @param type
	 * @return
	 */
	public static String removeJavaLangString(String type) {
		if ("java.lang.String".equals(type))
			return "String";
		else
			return type;
	}

	public static String artifactTypeToLabel(String artifactType) {
		for (String[] entry : artifactLabels) {
			String type = entry[0];
			String label = entry[1];
			if (type.equals(artifactType))
				return label;
		}
		return artifactType;
	}

	// public static Image artifactTypeddToImage( String artifactType ) {
	// for( String[] entry : artifactLabels ) {
	// String type = entry[0];
	// String image = entry[1];
	// if ( type.equals(artifactType)) {
	// return TigerstripePluginImages.get(image);
	// }
	// }
	// return null;
	// }
}
