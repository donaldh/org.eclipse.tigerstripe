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
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
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

	private final static String[] illegalStrings = { " ", ",", ";", ":", "'",
			"\"", "(", ")", "{", "}", "[", "]", "*", "&", "^", "%", "$", "#",
			"@", "!", "?", "<", ">", ".", "`", "~", "|", "/", "\\", "+", "=",
			"-" };

	private final static String[][] artifactLabels = {
			{ IManagedEntityArtifact.class.getName(),
					ManagedEntityArtifact.MODEL.getLabel() },
			{ IDatatypeArtifact.class.getName(),
					DatatypeArtifact.MODEL.getLabel() },
			{ IAssociationArtifact.class.getName(),
					AssociationArtifact.MODEL.getLabel() },
			{ IAssociationClassArtifact.class.getName(),
					AssociationClassArtifact.MODEL.getLabel() },
			{ IEventArtifact.class.getName(), EventArtifact.MODEL.getLabel() },
			{ IExceptionArtifact.class.getName(),
					ExceptionArtifact.MODEL.getLabel() },
			{ ISessionArtifact.class.getName(),
					SessionFacadeArtifact.MODEL.getLabel() },
			{ IQueryArtifact.class.getName(), QueryArtifact.MODEL.getLabel() },
			{ IUpdateProcedureArtifact.class.getName(),
					UpdateProcedureArtifact.MODEL.getLabel() },
			{ IPrimitiveTypeArtifact.class.getName(),
					PrimitiveTypeArtifact.MODEL.getLabel() },
			{ IDependencyArtifact.class.getName(),
					DependencyArtifact.MODEL.getLabel() },
			{ IEnumArtifact.class.getName(), EnumArtifact.MODEL.getLabel() },
			{ IPackageArtifact.class.getName(), 
				PackageArtifact.MODEL.getLabel() }};

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

	public static String removeIllegalCharacters(String name) {
		String result = name;
		// remove illegal characters
		for (int i = 0; i < illegalStrings.length; i++) {
			result = result.replace(illegalStrings[i], "");
		}
		return result;
	}

}
