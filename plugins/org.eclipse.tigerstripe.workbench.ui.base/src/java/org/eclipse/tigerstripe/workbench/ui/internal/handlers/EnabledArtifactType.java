/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.handlers;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IActiveWorkbenchProfileChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;

public class EnabledArtifactType extends PropertyTester implements IActiveWorkbenchProfileChangeListener {

	private static final String ENTITY_ENABLED   = "entityEnabled";
	private static final String EVENT_ENABLED = "eventEnabled";
	private static final String DATATYPE_ENABLED   = "datatypeEnabled";
	private static final String ENUMERATION_ENABLED = "enumerationEnabled";
	private static final String UPDATENOTIFICATION_ENABLED   = "updateEnabled";
	private static final String QUERY_ENABLED = "queryEnabled";
	private static final String EXCEPTION_ENABLED   = "exceptionEnabled";
	private static final String SESSION_ENABLED = "sessionEnabled";
	private static final String ASSOCIATION_ENABLED   = "associationEnabled";
	private static final String ASSOCIATIONCLASS_ENABLED = "associationClassEnabled";
	private static final String DEPENDENCY_ENABLED = "dependencyEnabled";
	private static final String PACKAGE_ENABLED = "packageEnabled";
	
	private CoreArtifactSettingsProperty prop;
	
	private boolean entityEnabled = true;
	private boolean eventEnabled = true;
	private boolean datatypeEnabled = true;
	private boolean enumerationEnabled = true;
	private boolean updateEnabled = true;
	private boolean queryEnabled = true;
	private boolean exceptionEnabled = true;
	private boolean sessionEnabled = true;
	private boolean associationEnabled = true;
	private boolean associationClassEnabled = true;
	private boolean dependencyEnabled = true;
	private boolean  packageEnabled = true;
	
	
	
	
	public EnabledArtifactType() {
		IWorkbenchProfile profile = TigerstripeCore
			.getWorkbenchProfileSession()
			.getActiveProfile();
		prop = (CoreArtifactSettingsProperty) profile
			.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
		setFlags();
		TigerstripeCore
		.getWorkbenchProfileSession().addActiveProfileListener(this);
	}

	public void profileChanged(IWorkbenchProfile newActiveProfile) {
		prop = (CoreArtifactSettingsProperty) newActiveProfile
		.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
		setFlags();

	}

	private void setFlags(){
		if (prop.getDetailsForType(IManagedEntityArtifact.class.getName()).isEnabled()){
			entityEnabled = true;
		 } else {
			 entityEnabled = false;
		 }
		if (prop.getDetailsForType(IDatatypeArtifact.class.getName()).isEnabled()){
			datatypeEnabled = true;
		 } else {
			 datatypeEnabled = false;
		 }
		if (prop.getDetailsForType(IEventArtifact.class.getName()).isEnabled()){
			eventEnabled = true;
		 } else {
			 eventEnabled = false;
		 }
		if (prop.getDetailsForType(IEnumArtifact.class.getName()).isEnabled()){
			enumerationEnabled = true;
		 } else {
			 enumerationEnabled = false;
		 }
		if (prop.getDetailsForType(IUpdateProcedureArtifact.class.getName()).isEnabled()){
			updateEnabled = true;
		 } else {
			 updateEnabled = false;
		 }
		if (prop.getDetailsForType(IQueryArtifact.class.getName()).isEnabled()){
			queryEnabled = true;
		 } else {
			 queryEnabled = false;
		 }
		if (prop.getDetailsForType(IExceptionArtifact.class.getName()).isEnabled()){
			exceptionEnabled = true;
		 } else {
			 exceptionEnabled = false;
		 }
		if (prop.getDetailsForType(ISessionArtifact.class.getName()).isEnabled()){
			sessionEnabled = true;
		 } else {
			 sessionEnabled = false;
		 }
		if (prop.getDetailsForType(IAssociationArtifact.class.getName()).isEnabled()){
			associationEnabled = true;
		 } else {
			 associationEnabled = false;
		 }
		if (prop.getDetailsForType(IAssociationClassArtifact.class.getName()).isEnabled()){
			associationClassEnabled = true;
		 } else {
			 associationClassEnabled = false;
		 }
		if (prop.getDetailsForType(IDependencyArtifact.class.getName()).isEnabled()){
			dependencyEnabled = true;
		 } else {
			 dependencyEnabled = false;
		 }
		if (prop.getDetailsForType(IPackageArtifact.class.getName()).isEnabled()){
			packageEnabled = true;
		 } else {
			 packageEnabled = false;
		 }
		
	}
	
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		
		if (ENTITY_ENABLED.equals(property) ){
			return entityEnabled;
		} else if (DATATYPE_ENABLED.equals(property) ){
			return datatypeEnabled;
		} else if (EVENT_ENABLED.equals(property) ){
			return eventEnabled;
		} else if (EXCEPTION_ENABLED.equals(property) ){
			return exceptionEnabled;
		} else if (UPDATENOTIFICATION_ENABLED.equals(property) ){
			return updateEnabled;
		} else if (QUERY_ENABLED.equals(property) ){
			return queryEnabled;
		} else if (ENUMERATION_ENABLED.equals(property) ){
			return enumerationEnabled;
		} else if (SESSION_ENABLED.equals(property) ){
			return sessionEnabled;
		} else if (ASSOCIATION_ENABLED.equals(property) ){
			return associationEnabled;
		} else if (ASSOCIATIONCLASS_ENABLED.equals(property) ){
			return associationClassEnabled;
		} else if (DEPENDENCY_ENABLED.equals(property) ){
			return dependencyEnabled;
		} else if (PACKAGE_ENABLED.equals(property) ){
			return packageEnabled;
		}
		return false;
	}

}
