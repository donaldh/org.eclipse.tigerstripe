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
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.artifactMetadata.AppearancePage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.audit.AuditPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.decorator.DecoratorPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.naming.NamingPage;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.patterns.CreationPage;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

public class ConfigEditor extends FormEditor implements IContributionListener{


	private ISDKProvider provider;

	
	private AuditPage auditPage;
//	private DecoratorPage dPage;
//	private NamingPage nPage;
	private AppearancePage artifactMetadataPage;
	private CreationPage patternsPage;
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
		//System.out.println("Type "+event.getType());
		//System.out.println("Kind "+event.getDelta().getKind());
		//System.out.println("Resource "+ event.getDelta().getResource().getName());
			if (auditPage != null && auditPage.isActive()){
				auditPage.refresh();
			}
//			if (dPage != null && dPage.isActive()){
//				dPage.refresh();
//			}
//			if (nPage != null && nPage.isActive()){
//				nPage.refresh();
//			}
			if (artifactMetadataPage != null && artifactMetadataPage.isActive()){
				artifactMetadataPage.refresh();
			}
			if (patternsPage != null && patternsPage.isActive()){
				patternsPage.refresh();
			}
			if (annotationsPage != null && annotationsPage.isActive()){
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
			
			//dPage = new DecoratorPage(this);
			//addPage(dPage);
			//nPage = new NamingPage(this);
			//addPage(nPage);
			annotationsPage  = new AnnotationPage(this);
			index = addPage(annotationsPage);
			artifactMetadataPage  = new AppearancePage(this);
			addPage(artifactMetadataPage);
			patternsPage  = new CreationPage(this);
			addPage(patternsPage);
			auditPage = new AuditPage(this);
			 addPage(auditPage);
			
		} catch (PartInitException e) {
			EclipsePlugin.log(e);
		}

		setActivePage(index);
	}


	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}



}