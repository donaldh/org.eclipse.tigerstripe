/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal;

import java.util.Collection;

import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationExplicitFileRouterContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationPackageLabelContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationPropertyProviderContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationTypeContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ArtifactIconContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ArtifactMetadataContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AuditContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DecoratorContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.DisabledPatternContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.ModelComponentIconProviderContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.NamingContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.PatternFileContribution;

public interface ISDKProvider {

	public void findAll();
	
	public Collection<IPluginModelBase> getAllContributers();
	
	public Collection<IPluginModelBase> getWriteableContributers();
	
	public Collection<AuditContribution> getAuditContributions();
	
	public Collection<NamingContribution> getNamingContributions();
	
	public Collection<DecoratorContribution> getDecoratorContributions();

	public  Collection<PatternFileContribution> getPatternFileContributions();

	public  Collection<DisabledPatternContribution> getDisabledPatternContributions();

	public  Collection<ArtifactMetadataContribution> getArtifactMetadataContributions();

	public  Collection<ArtifactIconContribution> getArtifactIconContributions();

	public  Collection<ModelComponentIconProviderContribution> getModelComponentIconProviderContributions();
	
	public Collection<AnnotationTypeContribution> getAnnotationTypeContributions();
	
	public Collection<AnnotationPackageLabelContribution> getAnnotationPackageLabelContributions();
	
	public Collection<AnnotationExplicitFileRouterContribution> getAnnotationExplicitFileRouterContributions();
	
	public Collection<AnnotationPropertyProviderContribution> getAnnotationPropertyProviderContributions();
	
	public IPattern getPattern(IPluginModelBase contributor,String fileName);
	
	public void addListener(IContributionListener listener);
	
	public void removeListener(IContributionListener listener);
	
}
