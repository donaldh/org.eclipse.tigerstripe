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
package org.eclipse.tigerstripe.workbench.ui;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Level;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.jdt.internal.ui.viewsupport.IViewPartInputProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact.DependencyEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.ProfileDetailsDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization.DiagramSynchronizationManager;
import org.eclipse.tigerstripe.workbench.ui.internal.preferences.PreferencesInitializer;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ProjectLocatorFacilityForEclipse;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorer;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class EclipsePlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.workbench.ui.base"; //$NON-NLS-1$

	// The shared instance.
	private static EclipsePlugin plugin;

	public static final String TIGERSTRIPE_SUPPORT_EMAILURL = "mailto:erdillon@cisco.com";

	public static final String TIGERSTRIPE_CONTACT_US_URL = "http://www.eclipse.org/tigerstripe/";

	public static long LICENSE_WARNING_PERIOD = 14;

	/**
	 * The constructor.
	 */
	public EclipsePlugin() {
		super();
		plugin = this;
	}

	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null)
			return window.getShell();
		return null;
	}

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);

		// Initialize preferences
		PreferencesInitializer.initialize();

		initialiseAPI();
		startDiagramSynchronizerManager();
		// This is necessary to make sure the extension point is read
		// and menu contributions added
		PatternFactory.getInstance();

		checkForFactoryProfile();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		stopDiagramSynchronizerManager();
	}

	/**
	 * A listener that gets notified when files change in the Workspace so it
	 * can propagate as appropriate to the artifact manager
	 * 
	 */
	private void startDiagramSynchronizerManager() {
		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(
				DiagramSynchronizationManager.getInstance(), ITigerstripeChangeListener.PROJECT);
	}

	private void stopDiagramSynchronizerManager() {
		TigerstripeWorkspaceNotifier.INSTANCE.removeTigerstripeChangeListener(
				DiagramSynchronizationManager.getInstance());
	}

	/**
	 * Returns the shared instance.
	 */
	public static EclipsePlugin getDefault() {
		return plugin;
	}

	public static IWorkbenchPage getActivePage() {
		return getDefault().internalGetActivePage();
	}

	private IWorkbenchPage internalGetActivePage() {
		IWorkbenchWindow window = getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

	// duplicates of logXXXMessage methods from TigerstripRuntime class
	// that allow for reporting of proper locations when the underlying
	// log4j "log" message is called
	private static void internalLogErrorMessage(String message) {
		internalLogErrorMessage(message, null);
	}

	private static void internalLogErrorMessage(String message, Throwable t) {
		BasePlugin.internalLogMessage(Level.ERROR, message, t);
	}

	private static void internalLogInfoMessage(String message) {
		internalLogInfoMessage(message, null);
	}

	private static void internalLogInfoMessage(String message, Throwable t) {
		BasePlugin.internalLogMessage(Level.INFO, message, t);
	}

	private static void internalLogWarnMessage(String message) {
		internalLogWarnMessage(message, null);
	}

	private static void internalLogWarnMessage(String message, Throwable t) {
		BasePlugin.internalLogMessage(Level.WARN, message, t);
	}

	public static void log(IStatus status) {
		// add the status message to the "Problems" view
		getDefault().getLog().log(status);
		// and then add a message to the Tigerstripe logfile containing the same
		// information
		// that appears in the "Problems" view
		if (status.getSeverity() == IStatus.ERROR) {
			if (status.getException() != null)
				internalLogErrorMessage(status.getMessage(), status
						.getException());
			else
				internalLogErrorMessage(status.getMessage());
		} else if (status.getSeverity() == IStatus.WARNING) {
			if (status.getException() != null)
				internalLogWarnMessage(status.getMessage(), status
						.getException());
			else
				internalLogWarnMessage(status.getMessage());
		} else if (status.getSeverity() == IStatus.INFO) {
			if (status.getException() != null)
				internalLogInfoMessage(status.getMessage(), status
						.getException());
			else
				internalLogInfoMessage(status.getMessage());
		}
	}

	public static void logErrorMessage(String message) {
		// calls through to the static EclipsePlugin.log(IStatus):void method
		// (above)
		// passing this message as an "internal error" status message
		log(new Status(IStatus.ERROR, getPluginId(), 222, message, null));
	}
	
	public static void logErrorMessage(String message, Throwable e) {
		log(new Status(IStatus.ERROR, getPluginId(), 222, message, e));
	}

	public static void logErrorStatus(String message, IStatus status) {
		if (status == null) {
			// log the message as an error message (will add the error to the
			// list
			// of errors maintained in the "Problems" view and the Tigerstripe
			// logfile)
			logErrorMessage(message);
			return;
		}
		MultiStatus multi = new MultiStatus(getPluginId(), 222, message, null);
		// log the status message
		multi.add(status);
		log(multi);
	}

	public static void log(Throwable e) {
		log(getStatus(e));
	}

	public static IStatus getStatus(Throwable e) {
		if (e instanceof TigerstripeException) {
			TigerstripeException tse = (TigerstripeException) e;
			if (tse.getException() == null) {
				return new Status(IStatus.ERROR, getPluginId(), 222,
						"Internal Error", tse); //$NON-NLS-1$
			} else {
				MultiStatus mStatus = new MultiStatus(getPluginId(), 222,
						"Internal Error", e);
				Exception ee = tse.getException();

				while (ee != null) {
					IStatus subStatus = new Status(IStatus.ERROR,
							getPluginId(), 222, "Internal Error", ee); //$NON-NLS-1$
					mStatus.add(subStatus);
					if (ee instanceof TigerstripeException) {
						ee = ((TigerstripeException) ee).getException();
					} else if (e.getCause() instanceof Exception) {
						ee = (Exception) ee.getCause();
					} else {
						break;
					}
				}
				return mStatus;
			}
		} else {
			if (e.getCause() == null) {
				return new Status(IStatus.ERROR, getPluginId(), 222,
						"Internal Error", e); //$NON-NLS-1$
			} else {
				MultiStatus mStatus = new MultiStatus(getPluginId(), 222,
						"Internal Error", e);
				Throwable ee = e.getCause();

				while (ee != null) {
					IStatus subStatus = new Status(IStatus.ERROR,
							getPluginId(), 222, "Internal Error", ee); //$NON-NLS-1$
					mStatus.add(subStatus);
					if (ee instanceof TigerstripeException) {
						ee = ((TigerstripeException) ee).getException();
					} else if (e.getCause() instanceof Exception) {
						ee = ee.getCause();
					} else {
						break;
					}
				}
				return mStatus;
			}
		}
	}

	public static String getPluginId() {
		return PLUGIN_ID;
	}

	/**
	 * Initialize the Tigerstripe API. In particular sets up the specific
	 * facilities for the Eclipse IDE
	 * 
	 */
	protected void initialiseAPI() {
		try {
			InternalTigerstripeCore.registerFacility(
					InternalTigerstripeCore.PROJECT_LOCATOR_FACILITY,
					new ProjectLocatorFacilityForEclipse());
		} catch (TigerstripeException e) {
			log(e);
		}
	}

	public static IEditorPart[] getEditorPartsForResource(
			final IResource resource) {
		// Collect dirtyParts
		ArrayList<IEditorPart> partsToClose = new ArrayList<IEditorPart>();
		IWorkbenchWindow windows[] = PlatformUI.getWorkbench()
				.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage pages[] = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				IEditorReference[] refs = pages[j].getEditorReferences();
				for (IEditorReference ref : refs) {
					IEditorPart part = ref.getEditor(false);
					if (part != null
							&& part.getEditorInput() instanceof IFileEditorInput) {
						IFileEditorInput input = (IFileEditorInput) part
								.getEditorInput();
						if (input.getFile().equals(resource)) {
							partsToClose.add(part);
						}
					}
				}
			}
		}

		return partsToClose.toArray(new IEditorPart[partsToClose.size()]);
	}

	public static void closeEditorForResource(final IResource resource)
			throws TigerstripeException {

		// Collect dirtyParts
		ArrayList<IEditorPart> partsToClose = new ArrayList<IEditorPart>();
		IWorkbenchWindow windows[] = PlatformUI.getWorkbench()
				.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage pages[] = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				IEditorReference[] refs = pages[j].getEditorReferences();
				for (IEditorReference ref : refs) {
					IEditorPart part = ref.getEditor(false);
					if (part != null
							&& part.getEditorInput() instanceof IFileEditorInput) {
						IFileEditorInput input = (IFileEditorInput) part
								.getEditorInput();
						if (input.getFile().equals(resource)) {
							partsToClose.add(part);
						}
					}
				}
			}
		}
		if (partsToClose.size() > 0) {
			for (IEditorPart part : partsToClose) {
				TigerstripeFormEditor editor = (TigerstripeFormEditor) part;
				editor.close(true);
			}
		}
	}

	public static IProject getProjectInFocus() {
		IStructuredSelection ssel = null;
		IWorkbenchWindow window = EclipsePlugin.getActiveWorkbenchWindow();
		if (window != null) {
			ISelection selection = window.getSelectionService().getSelection();
			if (selection instanceof IStructuredSelection) {
				ssel = (IStructuredSelection) selection;
			}
		}

		if (ssel != null && ssel.getFirstElement() != null) {
			Object selectedElement = ssel.getFirstElement();
			if (selectedElement instanceof IField) {
				IField field = (IField) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) field
						.getContainingArtifact();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null)
					selectedElement = obj;
			} else if (selectedElement instanceof IMethod) {
				IMethod meth = (IMethod) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) meth
						.getContainingArtifact();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null)
					selectedElement = obj;
			} else if (selectedElement instanceof ILiteral) {
				ILiteral la = (ILiteral) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingArtifact();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null)
					selectedElement = obj;
			} else if (selectedElement instanceof IAssociationEnd) {
				IAssociationEnd la = (IAssociationEnd) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingArtifact();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null)
					selectedElement = obj;
			} else if (selectedElement instanceof DependencyEnd) {
				DependencyEnd la = (DependencyEnd) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingRelationship();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null)
					selectedElement = obj;
			} else if (selectedElement instanceof DependencyEnd) {
				DependencyEnd la = (DependencyEnd) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingRelationship();
				Object obj = art.getAdapter(IResource.class);
				if (obj != null) {
					selectedElement = obj;
				}
			} else if (selectedElement instanceof DiagramEditPart) {
				DiagramEditPart part = (DiagramEditPart) selectedElement;
				if (part.getDiagramEditDomain() instanceof DiagramEditDomain) {
					DiagramEditDomain deom = (DiagramEditDomain) part
							.getDiagramEditDomain();
					IEditorInput input = deom.getEditorPart().getEditorInput();
					if (input instanceof IFileEditorInput) {
						IFileEditorInput fInput = (IFileEditorInput) input;
						selectedElement = fInput.getFile();
					}
				}
			} else if (selectedElement instanceof AbstractLogicalExplorerNode) {
				AbstractLogicalExplorerNode node = (AbstractLogicalExplorerNode) selectedElement;
				selectedElement = node.getKeyResource();
			} else if (selectedElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) selectedElement;
				selectedElement = adaptable.getAdapter(IResource.class);
			}

			if (selectedElement instanceof IResource) {
				IResource res = (IResource) selectedElement;
				return res.getProject();
			}
		} else {
			IWorkbenchPart part = EclipsePlugin.getActivePage().getActivePart();
			IResource res = null;
			if (part instanceof EditorPart) {
				EditorPart ePart = (EditorPart) part;
				IEditorInput input = ePart.getEditorInput();
				if (input instanceof IFileEditorInput) {
					IFileEditorInput fInput = (IFileEditorInput) input;
					if (fInput.getFile() != null)
						return fInput.getFile().getProject();
				}
			} else if (part instanceof IViewPartInputProvider) {
				Object elem = ((IViewPartInputProvider) part)
						.getViewPartInput();
				if (elem instanceof IAdaptable) {
					IAdaptable adap = (IAdaptable) elem;
					res = (IResource) adap.getAdapter(IResource.class);
					if (res != null)
						return res.getProject();
				}
			}
		}

		return null;
	}

	/**
	 * Returns the TS project that is in focus, i.e. that has one of its
	 * resource selected or open somehow.
	 * 
	 * @return
	 */
	public static IAbstractTigerstripeProject getTSProjectInFocus() {
		IProject iProject = getProjectInFocus();
		if (iProject != null && iProject.isOpen())
			return (IAbstractTigerstripeProject) iProject
					.getAdapter(IAbstractTigerstripeProject.class);
		return null;
	}

	/**
	 * Finds the TigerstripeExplorerPart in the workbench.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public static TSExplorer findTigerstripeExplorer()
			throws TigerstripeException {
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench()
				.getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			IWorkbenchPage[] pages = window.getPages();
			for (IWorkbenchPage page : pages) {
				IViewReference[] viewRefs = page.getViewReferences();
				for (IViewReference viewRef : viewRefs) {
					if (TigerstripeExplorerPart.AEXPLORER_ID.equals(viewRef
							.getId())) {
						IWorkbenchPart part = viewRef.getPart(true);
						if (part instanceof TSExplorer)// TigerstripeExplorerPart)
							return (TSExplorer) part;
					}
				}
			}
		}

		throw new TigerstripeException(
				"Couldn't find Tigerstripe Explorer Part");
	}

	/**
	 * Returns a section in the Tigerstripe plugin's dialog settings. If the
	 * section doesn't exist yet, it is created.
	 * 
	 * @param name
	 *            the name of the section
	 * @return the section of the given name
	 * @since 2.2.4
	 */
	public IDialogSettings getDialogSettingsSection(String name) {
		IDialogSettings dialogSettings = getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(name);
		if (section == null) {
			section = dialogSettings.addNewSection(name);
		}
		return section;
	}

	private void checkForFactoryProfile() {

		try {
			IConfigurationElement[] elements = Platform
					.getExtensionRegistry()
					.getConfigurationElementsFor(
							"org.eclipse.tigerstripe.workbench.base.defaultProfile");

			for (IConfigurationElement element : elements) {
				if (element.getName().equals("profile")) {
					String checkONStartupString = element
							.getAttribute("checkOnStartup");
					String profileFileName = element
							.getAttribute("profileFile");
					IContributor contributor = ((IExtension) element
							.getParent()).getContributor();

					if (elements.length > 1) {
						BasePlugin
								.logErrorMessage("More than one contribution to "
										+ "defaultProfile Extension Point : "
										+ "using "
										+ profileFileName
										+ " from "
										+ contributor.getName());
					}

					if (checkONStartupString != null) {
						Boolean checkONStartup = Boolean
								.parseBoolean(checkONStartupString);
						if (checkONStartup) {

						} else {
							return;
						}
					}
					// Need to get the file from the contributing plugin

					Bundle bundle = org.eclipse.core.runtime.Platform
							.getBundle(contributor.getName());
					File bundleFile = FileLocator.getBundleFile(bundle);
					String bundleRoot = bundleFile.getAbsolutePath();
					String pathname = bundleRoot + File.separator
							+ profileFileName;
					IWorkbenchProfileSession session = TigerstripeCore
							.getWorkbenchProfileSession();
					IWorkbenchProfile contributedProfile = session
							.getWorkbenchProfileFor(pathname);
					IWorkbenchProfile activeProfile = session
							.getActiveProfile();

					if (!contributedProfile.equals(activeProfile)) {
						// Prompt the user
						MessageBox box = new MessageBox(
								getActiveWorkbenchShell(), SWT.YES | SWT.NO);
						box.setText("Tigerstipe profile not set to default");
						// Bugzilla 327008 -  "Default profile" message is not as clear as it could be
						box.setMessage("The current Tigerstripe profile is not the specified default profile for this installation.\n Do you wish to return to the default profile?");

						int selection = box.open();
						if (selection == SWT.YES) {
							ProfileDetailsDialog.internalDeploy(
									getActiveWorkbenchShell(), pathname);
						}
					}

					break; // IN case > 1!

				}
			}

		} catch (Exception e) {
			BasePlugin
					.logErrorMessage("Failed to correctly load defaultProfile from Extension Point");
			BasePlugin.log(e);

		}
	}
}
