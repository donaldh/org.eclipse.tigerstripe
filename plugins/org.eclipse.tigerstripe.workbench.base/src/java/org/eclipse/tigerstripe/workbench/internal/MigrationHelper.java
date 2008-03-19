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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IFieldImpl;
import org.eclipse.tigerstripe.metamodel.impl.ILiteralImpl;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IMethodImpl;
import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.Field;
import org.eclipse.tigerstripe.workbench.internal.core.model.Literal;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method;
import org.eclipse.tigerstripe.workbench.internal.core.model.PrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IBooleanPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ICopyRule;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IStringPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.ITablePluginProperty;

/**
 * This helper contains all the hardcoded values that may be found in XML files
 * that need to be migrated as a result of the new open-source version.
 * 
 * @author erdillon
 * 
 */
public class MigrationHelper {

	public static String profileMigrateCoreArtifactSettingsArtifactType(
			String artifactType) {
		return profileMigrateAnnotationArtifactLevelType(artifactType);
	}

	/**
	 * This method is useful to check that all the types going thru here are
	 * found, if not it means we have missed a migration case.
	 * 
	 * @param type
	 */
	private static void assertDefined(String type) {
		// This is just in place to ensure we are migrating everything
		try {
			Class theClass = MigrationHelper.class.forName(type);
		} catch (ClassNotFoundException e) {
			BasePlugin.log(new Status(IStatus.WARNING, BasePlugin.PLUGIN_ID,
					"Couldn't migrate profile property type: " + type, e));
		}
	}

	public static String profileMigratePropertyType(String type) {
		if ("com.tigerstripesoftware.core.profile.properties.GlobalSettingsProperty"
				.equals(type))
			return GlobalSettingsProperty.class.getName();
		else if ("com.tigerstripesoftware.core.profile.properties.CoreArtifactSettingsProperty"
				.equals(type))
			return CoreArtifactSettingsProperty.class.getName();
		else if ("com.tigerstripesoftware.core.profile.properties.OssjLegacySettingsProperty"
				.equals(type))
			return OssjLegacySettingsProperty.class.getName();

		assertDefined(type);
		return type;
	}

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
		else if ("com.tigerstripesoftware.api.artifacts.model.IPrimitiveTypeArtifact"
				.equals(scope))
			return IPrimitiveTypeArtifact.class.getName();

		assertDefined(scope);
		return scope;
	}

	public final static String artifactMetadataMigrateClassname(String classname) {

		if (ManagedEntityArtifact.class.getName().equals(classname)
				|| IManagedEntityArtifact.class.getName().equals(classname))
			return IManagedEntityArtifactImpl.class.getName();
		else if (DatatypeArtifact.class.getName().equals(classname)
				|| IDatatypeArtifact.class.getName().equals(classname))
			return IDatatypeArtifactImpl.class.getName();
		else if (ExceptionArtifact.class.getName().equals(classname)
				|| IExceptionArtifact.class.getName().equals(classname))
			return IExceptionArtifactImpl.class.getName();
		else if (EnumArtifact.class.getName().equals(classname)
				|| IEnumArtifact.class.getName().equals(classname))
			return IEnumArtifactImpl.class.getName();
		else if (AssociationArtifact.class.getName().equals(classname)
				|| IAssociationArtifact.class.getName().equals(classname))
			return IAssociationArtifactImpl.class.getName();
		else if (AssociationClassArtifact.class.getName().equals(classname)
				|| IAssociationClassArtifact.class.getName().equals(classname))
			return IAssociationClassArtifactImpl.class.getName();
		else if (DependencyArtifact.class.getName().equals(classname)
				|| IDependencyArtifact.class.getName().equals(classname))
			return IDependencyArtifactImpl.class.getName();
		else if (EventArtifact.class.getName().equals(classname)
				|| IEventArtifact.class.getName().equals(classname))
			return IEventArtifactImpl.class.getName();
		else if (PrimitiveTypeArtifact.class.getName().equals(classname)
				|| IPrimitiveTypeArtifact.class.getName().equals(classname))
			return IPrimitiveTypeImpl.class.getName();
		else if (QueryArtifact.class.getName().equals(classname)
				|| IQueryArtifact.class.getName().equals(classname))
			return IQueryArtifactImpl.class.getName();
		else if (SessionFacadeArtifact.class.getName().equals(classname)
				|| ISessionArtifact.class.getName().equals(classname))
			return ISessionArtifactImpl.class.getName();
		else if (UpdateProcedureArtifact.class.getName().equals(classname)
				|| IUpdateProcedureArtifact.class.getName().equals(classname))
			return IUpdateProcedureArtifactImpl.class.getName();
		else if (Field.class.getName().equals(classname)
				|| IField.class.getName().equals(classname))
			return IFieldImpl.class.getName();
		else if (Method.class.getName().equals(classname)
				|| IMethod.class.getName().equals(classname))
			return IMethodImpl.class.getName();
		else if (Literal.class.getName().equals(classname)
				|| ILiteral.class.getName().equals(classname))
			return ILiteralImpl.class.getName();

		assertDefined(classname);
		return classname;
	}

	public static String pluginMigrateRuleType(String ruleType) {
		if (ruleType.endsWith("ISimpleTemplateRunRule")) {
			return IGlobalTemplateRule.class.getName();
		} else if (ruleType.endsWith("IArtifactBasedTemplateRunRule")) {
			return IArtifactBasedTemplateRule.class.getName();
		} else if (ruleType.endsWith("ICopyRule")) {
			return ICopyRule.class.getName();
		}

		assertDefined(ruleType);
		return ruleType;
	}

	public static String pluginMigrateProperty(String propertyType) {
		if ("com.tigerstripesoftware.api.plugins.pluggable.IStringPPluginProperty"
				.equals(propertyType))
			return IStringPluginProperty.class.getName();
		else if ("com.tigerstripesoftware.api.plugins.pluggable.IBooleanPPluginProperty"
				.equals(propertyType))
			return IBooleanPluginProperty.class.getName();
		else if ("com.tigerstripesoftware.api.plugins.pluggable.IBooleanPPluginProperty"
				.equals(propertyType))
			return IBooleanPluginProperty.class.getName();
		else if ("com.tigerstripesoftware.api.plugins.pluggable.ITablePPluginProperty"
				.equals(propertyType))
			return ITablePluginProperty.class.getName();

		assertDefined(propertyType);
		return propertyType;
	}
}
