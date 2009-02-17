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

public abstract class AbstractProvider implements ISDKProvider {

	protected  Collection<IPluginModelBase> allContributers;
	protected  Collection<IPluginModelBase> writeableContributers;
	protected  Collection<AuditContribution> auditContributions;
	protected  Collection<NamingContribution> namingContributions;
	protected  Collection<DecoratorContribution> decoratorContributions;
	protected  Collection<PatternFileContribution> patternFileContributions;
	protected  Collection<DisabledPatternContribution> disabledPatternContributions;
	protected  Collection<ArtifactMetadataContribution> artifactMetadataContributions;
	protected  Collection<ArtifactIconContribution> artifactIconContributions;
	protected  Collection<ModelComponentIconProviderContribution> modelComponentIconProviderContributions;
	protected Collection<AnnotationTypeContribution> annotationTypeContributions;
	protected Collection<AnnotationPackageLabelContribution> annotationPackageLabelContributions;
	protected Collection<AnnotationExplicitFileRouterContribution> annotationExplicitFileRouterContributions;
	protected Collection<AnnotationPropertyProviderContribution> annotationPropertyProviderContributions;
	
	public Collection<AuditContribution> getAuditContributions() {
		return auditContributions;
	}
	
	public Collection<NamingContribution> getNamingContributions() {
		return namingContributions;
	}
	
	public Collection<DecoratorContribution> getDecoratorContributions() {
		return decoratorContributions;
	}

	public  Collection<PatternFileContribution> getPatternFileContributions() {
		return patternFileContributions;
	}

	public  Collection<DisabledPatternContribution> getDisabledPatternContributions() {
		return disabledPatternContributions;
	}

	public  Collection<ArtifactMetadataContribution> getArtifactMetadataContributions() {
		return artifactMetadataContributions;
	}

	public  Collection<ArtifactIconContribution> getArtifactIconContributions() {
		return artifactIconContributions;
	}

	public  Collection<ModelComponentIconProviderContribution> getModelComponentIconProviderContributions() {
		return modelComponentIconProviderContributions;
	}

	public Collection<AnnotationTypeContribution> getAnnotationTypeContributions() {
		return annotationTypeContributions;
	}

	public Collection<AnnotationPackageLabelContribution> getAnnotationPackageLabelContributions() {
		return annotationPackageLabelContributions;
	}

	public Collection<AnnotationExplicitFileRouterContribution> getAnnotationExplicitFileRouterContributions() {
		return annotationExplicitFileRouterContributions;
	}

	public Collection<AnnotationPropertyProviderContribution> getAnnotationPropertyProviderContributions() {
		return annotationPropertyProviderContributions;
	}

	public Collection<IPluginModelBase> getAllContributers() {
		return allContributers;
	}

	public Collection<IPluginModelBase> getWriteableContributers() {
		return writeableContributers;
	}
	
	
	
}
