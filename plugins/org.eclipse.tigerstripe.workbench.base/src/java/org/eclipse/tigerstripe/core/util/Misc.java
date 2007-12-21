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
package org.eclipse.tigerstripe.core.util;

import org.eclipse.tigerstripe.api.artifacts.model.IAssociationArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IDependencyArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEventArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IExceptionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IQueryArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IUpdateProcedureArtifact;
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

	// private final static String[][] artifactImages = {
	// { IManagedEntityArtifact.class.getName(),
	// TigerstripePluginImages.ENTITY_ICON},
	// { IDatatypeArtifact.class.getName(),
	// TigerstripePluginImages.DATATYPE_ICON},
	// { IAssociationArtifact.class.getName(),
	// TigerstripePluginImages.ASSOCIATION_ICON },
	// { IAssociationClassArtifact.class.getName(),
	// TigerstripePluginImages.ASSOCIATIONCLASS_ICON },
	// { IEventArtifact.class.getName(),
	// TigerstripePluginImages.NOTIFICATION_ICON},
	// { IExceptionArtifact.class.getName(),
	// TigerstripePluginImages.EXCEPTION_ICON },
	// { ISessionArtifact.class.getName(), TigerstripePluginImages.SESSION_ICON
	// },
	// { IQueryArtifact.class.getName(), TigerstripePluginImages.QUERY_ICON },
	// { IUpdateProcedureArtifact.class.getName(),
	// TigerstripePluginImages.UPDATEPROC_ICON },
	// { IPrimitiveTypeArtifact.class.getName(),
	// TigerstripePluginImages.PRIMITIVE_ICON },
	// { IEnumArtifact.class.getName(), TigerstripePluginImages.ENUM_ICON }
	// };
	//	
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
