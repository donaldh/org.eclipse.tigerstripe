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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.patternBased;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.patterns.INodePattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.NewTSElementWizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ISetSelectionTarget;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class NewPatternBasedArtifactWizard extends NewTSElementWizard {

	protected IPattern pattern;

	
	// The buffer used by Apache Velocity to generate the artifact
	protected ByteArrayOutputStream buffer;

	
	public NewPatternBasedArtifactWizard(IPattern pattern) {
		this.pattern = pattern;
		setDefaultPageImageDescriptor(Images.getDescriptor(Images.TS_LOGO));

		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle(pattern.getDescription());
	}
	
	public IRunnableWithProgress getRunnable(NewNodePatternBasedWizardPage mainPage) {
		
		try {
		final ITigerstripeModelProject project = mainPage.getProject();
		
		
		final String packageName      = mainPage.getPackageName();
		final String artifactName     = mainPage.getArtifactName();
		final String extendedArtifact = mainPage.getExtendedArtifact();

		String aEnd = "";
		String zEnd = "";

		final IPattern patt = (IPattern) this.pattern;

		if (mainPage instanceof NewRelationPatternBasedWizardPage){
			aEnd = ((NewRelationPatternBasedWizardPage)mainPage).getAendTypeClass();
			zEnd = ((NewRelationPatternBasedWizardPage)mainPage).getZendTypeClass();
		}

		final String aEndType = aEnd;
		final String zEndType = zEnd;

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
			throws InvocationTargetException {
				try {
					if (patt instanceof INodePattern){
						((INodePattern) patt).executeRequests(project, 
								packageName, 
								artifactName, 
								extendedArtifact, true);
					} else if (patt instanceof IRelationPattern){
						((IRelationPattern) patt).executeRequests(project, 
								packageName, 
								artifactName, 
								extendedArtifact,
								aEndType,
								zEndType, true);
					}
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		return op;
		} catch (TigerstripeException t){
			return null;
		}
	}

	/*
	 * @see Wizard#performFinish
	 */
	@Override
	public boolean performFinish() {

		IWizardPage[] pages = getPages();
		NewNodePatternBasedWizardPage mainPage = (NewNodePatternBasedWizardPage) pages[0];
		IRunnableWithProgress runnable = getRunnable(mainPage);

		try {
			getContainer().run(false, false, runnable);
		} catch (InvocationTargetException e) {
			handleFinishException(getShell(), e);
			return false;
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	// Allow override for Packages
	protected  String getFileName(String projectName, String srcDirectory, String packageName, String artifactName){
		return projectName + File.separator + srcDirectory
			+ File.separator
			+packageName.replace('.', File.separatorChar)
			+ File.separator + artifactName + ".java";
	}
	
	// Allow override for Packages
	protected String getPackageForArtifact(String packageName, String artifactName){
		return packageName;
	}
	
	
	private void createFolder(IProject project, IFolder folder, String pack) {
		if ("".equals(pack))
			return;
		if (pack.indexOf(".") == -1) {
			IFolder packFolder = folder.getFolder(pack);
			try {
				if (!packFolder.exists()) {
					packFolder.create(true, false, null);
				}
			} catch (CoreException e) {
				TigerstripeRuntime.logErrorMessage("CoreException detected", e);
			}
			return;
		}

		String[] parts = pack.split("\\.");
		IFolder fol = folder;
		for (int i = 0; i < parts.length; i++) {
			IFolder currentFolder = fol.getFolder(parts[i]);
			if (!currentFolder.exists()) {
				try {
					if (!currentFolder.exists()) {
						currentFolder.create(IResource.FORCE, true, null);
					}
				} catch (CoreException e) {
					TigerstripeRuntime.logErrorMessage(
							"CoreException detected", e);
					return;
				}
			}
			fol = currentFolder;
		}
	}


	

	@Override
	protected void finishPage(IProgressMonitor monitor)
			throws InterruptedException, CoreException {
		// This is not used, just to make the compiler happy
	}

	/**
	 * Selects and reveals the newly added resource in all parts of the active
	 * workbench window's active page.
	 * 
	 * @see ISetSelectionTarget
	 */
	@Override
	protected void selectAndReveal(IResource newResource) {
		selectAndReveal(newResource, getWorkbench().getActiveWorkbenchWindow());
	}

	/**
	 * Attempts to select and reveal the specified resource in all parts within
	 * the supplied workbench window's active page.
	 * <p>
	 * Checks all parts in the active page to see if they implement
	 * <code>ISetSelectionTarget</code>, either directly or as an adapter. If
	 * so, tells the part to select and reveal the specified resource.
	 * </p>
	 * 
	 * @param resource
	 *            the resource to be selected and revealed
	 * @param window
	 *            the workbench window to select and reveal the resource
	 * 
	 * @see ISetSelectionTarget
	 */
	public static void selectAndReveal(IResource resource,
			IWorkbenchWindow window) {
		// validate the input
		if (window == null || resource == null)
			return;
		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return;

		// get all the view and editor parts
		List parts = new ArrayList();
		IWorkbenchPartReference refs[] = page.getViewReferences();
		for (int i = 0; i < refs.length; i++) {
			IWorkbenchPart part = refs[i].getPart(false);
			if (part != null) {
				parts.add(part);
			}
		}
		refs = page.getEditorReferences();
		for (int i = 0; i < refs.length; i++) {
			if (refs[i].getPart(false) != null) {
				parts.add(refs[i].getPart(false));
			}
		}

		final ISelection selection = new StructuredSelection(resource);
		Iterator itr = parts.iterator();
		while (itr.hasNext()) {
			IWorkbenchPart part = (IWorkbenchPart) itr.next();

			// get the part's ISetSelectionTarget implementation
			ISetSelectionTarget target = null;
			if (part instanceof ISetSelectionTarget) {
				target = (ISetSelectionTarget) part;
			} else {
				target = (ISetSelectionTarget) part
						.getAdapter(ISetSelectionTarget.class);
			}

			if (target != null) {
				// select and reveal resource
				final ISetSelectionTarget finalTarget = target;
				window.getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						finalTarget.selectReveal(selection);
					}
				});
			}
		}
	}

}