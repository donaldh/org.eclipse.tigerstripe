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

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.sdk.internal.IContributionListener;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.annotation.AnnotationPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.artifactMetadata.ArtifactMetadataPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.audit.AuditPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.decorator.DecoratorPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.naming.NamingPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.patterns.PatternPage;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;

public class ConfigEditor extends TigerstripeFormEditor implements IContributionListener{


	private ISDKProvider provider;

	
	private AuditPage auditPage;
	private DecoratorPage dPage;
	private NamingPage nPage;
	private ArtifactMetadataPage artifactMetadataPage;
	private PatternPage patternsPage;
	private AnnotationPage annotationsPage  = new AnnotationPage(this);
	
	public ConfigEditor() {
	}

	public ISDKProvider getIProvider() {
		return provider;
	}

	protected void setIProvider(ISDKProvider provider) {
		if (provider == null)
			System.out.println("arggg Null");
		this.provider = provider;
		this.provider.addListener(this);
		
	}
	
	


	@Override
	public void close(boolean save) {
		this.provider.removeListener(this);
		super.close(save);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (auditPage != null){
			auditPage.refresh();
		}
		if (dPage != null){
			dPage.refresh();
		}
		if (nPage != null){
			nPage.refresh();
		}
		if (artifactMetadataPage != null){
			artifactMetadataPage.refresh();
		}
		if (patternsPage != null){
			patternsPage.refresh();
		}
		if (annotationsPage != null){
			annotationsPage.refresh();
		}
	}

	@Override
	protected void setInput(IEditorInput input) {
		if (input instanceof SDKEditorInput) {
			ISDKProvider provider = ((SDKEditorInput) input).getProvider();
			setIProvider(provider);
			
		}

		super.setInput(input);
	}

	@Override
	protected void addPages() {
		int index = -1;
		try {
			//ConfigOverviewPage page = new ConfigOverviewPage(this);
			//addPage(page);
			auditPage = new AuditPage(this);
			index =  addPage(auditPage);
			dPage = new DecoratorPage(this);
			addPage(dPage);
			nPage = new NamingPage(this);
			addPage(nPage);
			artifactMetadataPage  = new ArtifactMetadataPage(this);
			addPage(artifactMetadataPage);
			patternsPage  = new PatternPage(this);
			addPage(patternsPage);
			annotationsPage  = new AnnotationPage(this);
			addPage(annotationsPage);

			
		} catch (PartInitException e) {
			EclipsePlugin.log(e);
		}

		setActivePage(index);
	}


	@Override
	public void doSave(IProgressMonitor monitor) {

	}



}