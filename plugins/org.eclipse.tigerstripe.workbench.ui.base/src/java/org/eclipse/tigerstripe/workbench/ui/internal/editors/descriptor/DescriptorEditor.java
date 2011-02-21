/******************************************************************************* * Copyright (c) 2007 Cisco Systems, Inc. * All rights reserved. This program and the accompanying materials * are made available under the terms of the Eclipse Public License v1.0 * which accompanies this distribution, and is available at * http://www.eclipse.org/legal/epl-v10.html * * Contributors: *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing *******************************************************************************/package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor;import java.io.StringReader;import java.util.ArrayList;import java.util.Collection;import java.util.HashSet;import java.util.Iterator;import java.util.Set;import org.eclipse.core.resources.IFile;import org.eclipse.core.resources.IMarker;import org.eclipse.core.resources.IProject;import org.eclipse.core.resources.IResource;import org.eclipse.core.resources.IResourceChangeEvent;import org.eclipse.core.resources.IResourceDelta;import org.eclipse.core.runtime.CoreException;import org.eclipse.core.runtime.IProgressMonitor;import org.eclipse.core.runtime.IStatus;import org.eclipse.core.runtime.Status;import org.eclipse.jdt.core.IJavaProject;import org.eclipse.jdt.core.JavaCore;import org.eclipse.tigerstripe.workbench.TigerstripeException;import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.advanced.AdvancedConfigurationPage;import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.dependencies.DescriptorDependenciesPage;import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.depsdiagram.ITigerstripeProjectProvider;import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.depsdiagram.TigerstripeDependenciesDiagram;import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.facetRefs.FacetReferencesPage;import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.header.OverviewPage;import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.plugins.PluginConfigurationPage;import org.eclipse.ui.IEditorInput;import org.eclipse.ui.IEditorPart;import org.eclipse.ui.IFileEditorInput;import org.eclipse.ui.PartInitException;import org.eclipse.ui.part.FileEditorInput;/** * Main editor for module project descriptor file (tigersripe.xml file) <br/> * <br/> *  * <b>History of changes</b> (Name: Modification): <br/> * Eric Dillon : Initial Creation <br/> * Navid Mehregani: Bugzilla 319768: Switch to dependencies tab, if there are * related issues. <br/> * Navid Mehregani: Bugzilla 323860 - [Form Editor] In some cases Tigerstripe * files cannot be saved <br/> * Navid Mehregani: Bugzilla 313726 - Tigerstripe.XML Editor doesn't save <br/> */public class DescriptorEditor extends TigerstripeFormEditor {	private int sourcePageIndex;	private boolean previousPageWasModel = true;	private Collection<TigerstripeFormPage> modelPages = new ArrayList<TigerstripeFormPage>();	private DescriptorSourcePage sourcePage;	private ITigerstripeModelProject workingHandle;	private void updateTitle() {		IEditorInput input = getEditorInput();		if (input instanceof IFileEditorInput) {			setPartName(((IResource) input.getAdapter(IResource.class))					.getProject().getName() + "/" + input.getName());		} else {			setPartName(input.getName());		}	}	@Override	public Object getViewPartInput() {		IJavaProject jProject = null;		IResource res = (IResource) getEditorInput()				.getAdapter(IResource.class);		if (res != null) {			IProject project = res.getProject();			jProject = JavaCore.create(project);		}		return jProject;	}	@Override	public void addPages() {		int headerIndex = -1;		IFile file = null;		int dependencyPageIndex = -1;		try {			PluginConfigurationPage pluginPage = new PluginConfigurationPage(					this);			OverviewPage overPage = new OverviewPage(this);			headerIndex = addPage(overPage);			addModelPage(overPage);			addPage(pluginPage);			addModelPage(pluginPage);			if (getEditorInput() instanceof IFileEditorInput) {				IFileEditorInput fileEditorInput = (IFileEditorInput) getEditorInput();				file = fileEditorInput.getFile();				DescriptorDependenciesPage depPage = new DescriptorDependenciesPage(						this);				dependencyPageIndex = addPage(depPage);				addDependencyDiagramPage();				addModelPage(depPage);				AdvancedConfigurationPage advPage = new AdvancedConfigurationPage(						this);				addPage(advPage);				addModelPage(advPage);				FacetReferencesPage facetPage = new FacetReferencesPage(this);				addPage(facetPage);				addModelPage(facetPage);				addSourcePage();			}		} catch (PartInitException e) {			EclipsePlugin.log(e);		}		// N.M: Bugzilla 319768: Switch to dependencies tab, if there are		// related issues.		if (areThereDependencyProblems(file) && dependencyPageIndex != -1)			setActivePage(dependencyPageIndex);		else			setActivePage(headerIndex);		updateTitle();	}	private void addDependencyDiagramPage() {		diagramEditor = new TigerstripeDependenciesDiagram();		diagramEditor.setProjectProvider(new ITigerstripeProjectProvider() {			public ITigerstripeModelProject getProject() {				return getTSProject();			}		});		try {			setPageText(addPage(diagramEditor, getEditorInput()),					"Dependency Diagram");		} catch (PartInitException e) {			EclipsePlugin.log(e);		}	}	// N.M: Bugzilla 319768: Switch to dependencies tab, if there are related	// issues.	private boolean areThereDependencyProblems(IFile file) {		try {			if (file != null) {				final String DEPENDENCY_ERROR = "Unresolved model reference";				IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true,						IResource.DEPTH_INFINITE);				if (markers != null) {					for (int i = 0; i < markers.length; i++) {						if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(								IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {							final Object errorMessage = markers[i]									.getAttribute(IMarker.MESSAGE);							if ((errorMessage instanceof String)									&& (((String) errorMessage)											.contains(DEPENDENCY_ERROR))) {								return true;							}						}					}				}			}		} catch (CoreException e) {			EclipsePlugin					.logErrorMessage(							"An error occurred while looking for dependency errors.  Exception Message (if any): "									+ e.getMessage(), e);		}		return false;	}	protected void addSourcePage() throws PartInitException {		sourcePage = new DescriptorSourcePage(this, "id", "Source");		sourcePageIndex = addPage(sourcePage, getEditorInput());		setPageText(sourcePageIndex, "Source");	}	@Override	public void doSave(IProgressMonitor monitor) {		// N.M: Bugzilla 323860 - [Form Editor] In some cases Tigerstripe files		// cannot be saved		if ((sourcePage.isDirty()) || (getActivePage() == sourcePageIndex)) {			try {				updateModelFromTextEditor();			} catch (TigerstripeException ee) {				Status status = new Status(IStatus.WARNING,						EclipsePlugin.getPluginId(), 111,						"Unexpected Exception", ee);				EclipsePlugin.log(status);			}			sourcePage.doSave(monitor);		} else {			try {				getTSProject().commit(monitor);			} catch (TigerstripeException ee) {				EclipsePlugin.log(ee);			}		}		diagramEditor.doSave(monitor);		if (getActivePage() != sourcePageIndex) {			((DescriptorSourcePage) getEditor(sourcePageIndex))					.firePropertyChange(IEditorPart.PROP_DIRTY);		}		firePropertyChange(IEditorPart.PROP_DIRTY);	}	@Override	public void pageChange(int newPageIndex) {		if (newPageIndex == sourcePageIndex) {			if (isPageModified)				updateTextEditorFromModel();			previousPageWasModel = false;		} else {			if (!previousPageWasModel && isDirty()) {				try {					updateModelFromTextEditor();				} catch (TigerstripeException ee) {					Status status = new Status(IStatus.WARNING,							EclipsePlugin.getPluginId(), 111,							"Unexpected Exception", ee);					EclipsePlugin.log(status);				}			}			previousPageWasModel = true;		}		super.pageChange(newPageIndex);	}	private boolean isPageModified;	private TigerstripeDependenciesDiagram diagramEditor;	private final Set<ITigerstripeModelProject> toCleanUp = new HashSet<ITigerstripeModelProject>();	public void pageModified() {		isPageModified = true;		if (!super.isDirty())			firePropertyChange(IEditorPart.PROP_DIRTY);	}	@Override	protected void handlePropertyChange(int propertyId) {		super.handlePropertyChange(propertyId);		if (propertyId == IEditorPart.PROP_DIRTY)			isPageModified = super.isDirty();	}	@Override	public boolean isDirty() {		// N.M: Bugzilla 323860 - [Form Editor] In some cases Tigerstripe files		// cannot be saved		if (getActivePage() == sourcePageIndex) {			if (sourcePage == null)				return false;			return sourcePage.isDirty();		}		return isPageModified || super.isDirty();	}	protected void addModelPage(TigerstripeFormPage page) {		modelPages.add(page);	}	private void refreshModelPages() {		for (TigerstripeFormPage page : modelPages) {			page.refresh();		}	}	private void updateTextEditorFromModel() {		try {			if (getEditorInput() instanceof FileEditorInput) {				TigerstripeProjectHandle projectHandle = (TigerstripeProjectHandle) getTSProject();				TigerstripeProject project = projectHandle.getTSProject();				sourcePage.getDocumentProvider().getDocument(getEditorInput())						.set(project.asText());			}		} catch (TigerstripeException e) {			Status status = new Status(IStatus.ERROR,					EclipsePlugin.getPluginId(), 222,					"Error refreshing source page for Tigerstripe descriptor",					e);			EclipsePlugin.log(status);		}	}	private void updateModelFromTextEditor() throws TigerstripeException {		if (getEditorInput() instanceof FileEditorInput) {			TigerstripeProjectHandle projectHandle = (TigerstripeProjectHandle) getTSProject();			TigerstripeProject originalProject = projectHandle.getTSProject();			String text = sourcePage.getDocumentProvider()					.getDocument(getEditorInput()).get();			StringReader reader = new StringReader(text);			try {				originalProject.parse(reader);				refreshModelPages();			} catch (TigerstripeException e) {				Status status = new Status(						IStatus.ERROR,						EclipsePlugin.getPluginId(),						222,						"Error refreshing model pages for Tigerstripe descriptor",						e);				EclipsePlugin.log(status);			}		}	}	public ITigerstripeModelProject getTSProject() {		if (workingHandle == null) {			IEditorInput input = getEditorInput();			ITigerstripeModelProject handle = null;			if (input instanceof IFileEditorInput) {				IFileEditorInput fileInput = (IFileEditorInput) input;				handle = (ITigerstripeModelProject) fileInput.getFile()						.getAdapter(ITigerstripeModelProject.class);				if (handle != null) {					// Create a working Copy where we substitute a new object					// for the underlying TigerstripeProject					try {						workingHandle = (ITigerstripeModelProject) handle								.makeWorkingCopy(null);						toCleanUp.add(workingHandle);					} catch (TigerstripeException e) {						return null;					}				}			} else if (input instanceof ReadOnlyDescriptorEditorInput) {				ReadOnlyDescriptorEditorInput descInput = (ReadOnlyDescriptorEditorInput) input;				workingHandle = descInput.getTSProject();			}		}		return workingHandle;	}	@Override	public void dispose() {		super.dispose();		cleanUp();	}	private void cleanUp() {		final Iterator<ITigerstripeModelProject> it = toCleanUp.iterator();		while (it.hasNext()) {			ITigerstripeModelProject next = it.next();			try {				next.dispose();			} catch (Exception e) {				EclipsePlugin.log(e);			}			it.remove();		}	}	@Override	public void resourceChanged(IResourceChangeEvent event) {		super.resourceChanged(event);		if (getEditorInput() instanceof IFileEditorInput) {			IResourceDelta selfDelta = lookforSelf(event.getDelta());			if (selfDelta != null) {				if (selfDelta.getKind() != IResourceDelta.REMOVED) {					workingHandle = null;					cleanUp();					refreshModelPages();				}			}		}	}}