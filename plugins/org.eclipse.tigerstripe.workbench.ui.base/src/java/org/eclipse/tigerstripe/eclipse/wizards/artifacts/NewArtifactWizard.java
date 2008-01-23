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
package org.eclipse.tigerstripe.eclipse.wizards.artifacts;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.wizards.NewTSElementWizard;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.TigerstripeProgressMonitor;
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
public abstract class NewArtifactWizard extends NewTSElementWizard {

	// The buffer used by Apache Velocity to generate the artifact
	protected ByteArrayOutputStream buffer;

	public IRunnableWithProgress getRunnable(INewArtifactWizardPage mainPage) {
		final Properties prop = mainPage.getProperties();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					runGenerator(prop, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		return op;
	}

	/*
	 * @see Wizard#performFinish
	 */
	@Override
	public boolean performFinish() {

		IWizardPage[] pages = getPages();
		INewArtifactWizardPage mainPage = (INewArtifactWizardPage) pages[0];
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

	private void runGenerator(Properties pageProperties,
			IProgressMonitor monitor) throws CoreException {

		// TODO: the parameter passing through Properties is super-messy
		// needs to be re-done!
		String containerLocation = pageProperties
				.getProperty(NewArtifactWizardPage.CONTAINER_NAME);
		String srcDirectory = pageProperties
				.getProperty(NewArtifactWizardPage.SRCDIRECTORY_NAME);
		String packageName = pageProperties
				.getProperty(NewArtifactWizardPage.PACKAGE_NAME);
		String entityName = pageProperties
				.getProperty(NewArtifactWizardPage.ARTIFACT_NAME);
		String projectName = pageProperties
				.getProperty(NewArtifactWizardPage.CONTAINER_PATH);

		monitor.beginTask("Creating " + entityName, 2);

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = workspaceRoot.getProject(projectName);

		IFolder srcFolder = project.getFolder(srcDirectory);
		// + File.separator
		// + packageName.replace('.', File.separatorChar));

		createFolder(project, srcFolder, packageName);

		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path(projectName + File.separator + srcDirectory
						+ File.separator
						+ packageName.replace('.', File.separatorChar)
						+ File.separator + entityName + ".java"));
		try {
			InputStream stream = openContentStream(pageProperties);
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
			EclipsePlugin.log(e);
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
		monitor.worked(1);

		IAbstractTigerstripeProject tsProject = EclipsePlugin
				.getITigerstripeProjectFor(project);
		if (tsProject instanceof ITigerstripeProject) {
			try {
				((ITigerstripeProject) tsProject).getArtifactManagerSession()
						.refresh(new TigerstripeProgressMonitor(monitor));
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		selectAndReveal(file);
		openResource(file);
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

	/**
	 * We will initialize file contents with a sample text.
	 * 
	 * @param pageProperties -
	 *            the properties gathered through the wizard
	 */
	protected InputStream openContentStream(Properties pageProperties) {

		byte[] bytes = null;
		buffer = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				buffer));

		ArtifactDefinitionGenerator generator = getGenerator(pageProperties,
				writer);
		generator.applyTemplate();
		bytes = buffer.toByteArray();
		return new ByteArrayInputStream(bytes);
	}

	protected abstract ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer);

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