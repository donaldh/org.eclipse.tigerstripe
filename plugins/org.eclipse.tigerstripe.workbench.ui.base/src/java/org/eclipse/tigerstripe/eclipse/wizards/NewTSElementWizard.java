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
package org.eclipse.tigerstripe.eclipse.wizards;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.internal.ui.actions.WorkbenchRunnableAdapter;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.workbench.internal.api.project.IProjectSession;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions.TSOpenAction;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * Top level class for all TS Wizard.
 * 
 * This is inspired by some original code from Eclipse
 * 
 * @author Eric Dillon
 * 
 */
public abstract class NewTSElementWizard extends Wizard implements INewWizard {

	private IWorkbench fWorkbench;

	private IStructuredSelection fSelection;

	public NewTSElementWizard() {
		setNeedsProgressMonitor(true);
	}

	protected void openResource(final IFile resource) {
		final Display display = Display.getCurrent();
		if (display != null) {
			display.asyncExec(new Runnable() {
				public void run() {
					try {
						IProjectSession session = TigerstripeCore
								.getDefaultProjectSession();
						ITigerstripeProject project = (ITigerstripeProject) session
								.makeTigerstripeProject(resource.getProject()
										.getLocation().toFile().toURI(), null);

						IArtifactManagerSession mgrSession = project
								.getArtifactManagerSession();
						FileReader reader = new FileReader(resource
								.getLocation().toFile());
						IAbstractArtifact artifact = mgrSession
								.extractArtifact(reader,
										new TigerstripeNullProgressMonitor());

						// FIXME: This is when the artifact manager is notified
						// that a new
						// artifact has been added.
						mgrSession.addArtifact(artifact);

						IWorkbenchPage page = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage();

						if (artifact instanceof ManagedEntityArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.ENTITY_EDITOR);
						} else if (artifact instanceof ExceptionArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.EXCEPTION_EDITOR);
						} else if (artifact instanceof QueryArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.QUERY_EDITOR);
						} else if (artifact instanceof EventArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.EVENT_EDITOR);
						} else if (artifact instanceof DatatypeArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.DATATYPE_EDITOR);
						} else if (artifact instanceof SessionFacadeArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.SESSION_EDITOR);
						} else if (artifact instanceof EnumArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.ENUM_EDITOR);
						} else if (artifact instanceof UpdateProcedureArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.UPDATEPROC_EDITOR);
						} else if (artifact instanceof AssociationClassArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.ASSOCIATIONCLASS_EDITOR);
						} else if (artifact instanceof AssociationArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.ASSOCIATION_EDITOR);
						} else if (artifact instanceof DependencyArtifact) {
							page.openEditor(new FileEditorInput(resource),
									TSOpenAction.DEPENDENCY_EDITOR);
						}
					} catch (PartInitException e) {
						EclipsePlugin.log(e);
					} catch (FileNotFoundException e) {
						EclipsePlugin.log(e);
					} catch (TigerstripeLicenseException e) {
						EclipsePlugin.log(e);
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			});
		}
	}

	/**
	 * Subclasses should override to perform the actions of the wizard. This
	 * method is run in the wizard container's context as a workspace runnable.
	 * 
	 * @param monitor
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	protected abstract void finishPage(IProgressMonitor monitor)
			throws InterruptedException, CoreException;

	/**
	 * Returns the scheduling rule for creating the element.
	 */
	protected ISchedulingRule getSchedulingRule() {
		return ResourcesPlugin.getWorkspace().getRoot(); // look all by
		// default
	}

	protected boolean canRunForked() {
		return true;
	}

	// public abstract IJavaElement getCreatedElement();

	protected void handleFinishException(Shell shell,
			InvocationTargetException e) {
		String title = NewWizardMessages.NewElementWizard_op_error_title;
		String message = NewWizardMessages.NewElementWizard_op_error_message;
		ExceptionHandler.handle(e, shell, title, message);
	}

	/*
	 * @see Wizard#performFinish
	 */
	@Override
	public boolean performFinish() {
		IWorkspaceRunnable op = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException,
					OperationCanceledException {
				try {
					finishPage(monitor);
				} catch (InterruptedException e) {
					throw new OperationCanceledException(e.getMessage());
				}
			}
		};
		try {
			ISchedulingRule rule = null;
			Job job = Platform.getJobManager().currentJob();
			if (job != null)
				rule = job.getRule();
			IRunnableWithProgress runnable = null;
			if (rule != null)
				runnable = new WorkbenchRunnableAdapter(op, rule, true);
			else
				runnable = new WorkbenchRunnableAdapter(op, getSchedulingRule());
			getContainer().run(canRunForked(), true, runnable);
		} catch (InvocationTargetException e) {
			handleFinishException(getShell(), e);
			return false;
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		fWorkbench = workbench;
		fSelection = currentSelection;
	}

	public IStructuredSelection getSelection() {
		return fSelection;
	}

	public IWorkbench getWorkbench() {
		return fWorkbench;
	}

	protected void selectAndReveal(IResource newResource) {
		BasicNewResourceWizard.selectAndReveal(newResource, fWorkbench
				.getActiveWorkbenchWindow());
	}

}
