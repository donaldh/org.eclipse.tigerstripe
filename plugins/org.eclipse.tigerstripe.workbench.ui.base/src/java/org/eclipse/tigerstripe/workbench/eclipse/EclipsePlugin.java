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
package org.eclipse.tigerstripe.workbench.eclipse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.IJavaStatusConstants;
import org.eclipse.jdt.internal.ui.viewsupport.IViewPartInputProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.install.PostInstallActions;
import org.eclipse.tigerstripe.workbench.eclipse.utils.ProjectLocatorFacilityForEclipse;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationEnd;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact.DependencyEnd;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILiteral;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.builder.WorkspaceListener;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.PreferencesInitializer;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.AbstractLogicalExplorerNode;
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
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class EclipsePlugin extends AbstractUIPlugin implements
		TigerstripePluginConstants {

	// The shared instance.
	private static EclipsePlugin plugin;

	private WorkspaceListener listener;

	public static final String TIGERSTRIPE_SUPPORT_EMAILURL = "mailto:erdillon@cisco.com";

	public static final String TIGERSTRIPE_CONTACT_US_URL = "http://www.eclipse.org/tigerstripe/";

	public static long LICENSE_WARNING_PERIOD = 14;

	private static boolean loggerInitialized = false;

	private static Level defaultLoggingLevel = Level.ALL;

	private static final String LOG4J_FQCN = EclipsePlugin.class.getName();

	private static final String tigerstripeLoggerID = EclipsePlugin.class
			.getCanonicalName();

	private static final Logger tigerstripeLogger = Logger
			.getLogger(tigerstripeLoggerID);

	/**
	 * The constructor.
	 */
	public EclipsePlugin() {
		super();
		plugin = this;
	}

	public static void initLogger() {

		String loggingDirStr = TigerstripeRuntime.getTigerstripeRuntimeRoot();

		if (!loggerInitialized && loggingDirStr != null) {

			// First check that the loggingDir exists: upon first run
			// it would not have been created at this stage.
			File loggingDir = new File(loggingDirStr);
			if (loggingDir != null && !loggingDir.exists()) {
				loggingDir.mkdirs();
			}

			// Add logic here
			String outputPath = loggingDir.getPath() + File.separator
					+ "tigerstripe.log";
			File outputFile = new File(outputPath);
			String conversionPattern = "%-5p %C [%d{dd-MMM-yyyy HH:mm:ss.SSS}] - %m ["
					+ TigerstripeRuntime.getLogStartTime() + "]%n";
			PatternLayout patternLayout = new PatternLayout(conversionPattern);
			try {
				RollingFileAppender appender = new RollingFileAppender(
						patternLayout, outputPath);
				tigerstripeLogger.removeAllAppenders();
				tigerstripeLogger.addAppender(appender);
				tigerstripeLogger.setAdditivity(false);
				tigerstripeLogger.setLevel(defaultLoggingLevel);
				loggerInitialized = true;
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
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

		executePostInstallationActions(context);
		initLogger();

		// Initialize preferences
		PreferencesInitializer.initialize();

		initialiseAPI();
		startWorkspaceListener();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		stopWorkspaceListener();
	}

	/**
	 * A listener that gets notified when files change in the Workspace so it
	 * can propagate as appropriate to the artifact manager
	 * 
	 */
	private void startWorkspaceListener() {
		listener = new WorkspaceListener();
		JavaCore.addElementChangedListener(listener);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
	}

	private void stopWorkspaceListener() {
		if (listener != null) {
			JavaCore.removeElementChangedListener(listener);
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(
					listener);
		}
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
		internalLogMessage(Level.ERROR, message, t);
	}

	private static void internalLogInfoMessage(String message) {
		internalLogInfoMessage(message, null);
	}

	private static void internalLogInfoMessage(String message, Throwable t) {
		internalLogMessage(Level.INFO, message, t);
	}

	private static void internalLogWarnMessage(String message) {
		internalLogWarnMessage(message, null);
	}

	private static void internalLogWarnMessage(String message, Throwable t) {
		internalLogMessage(Level.WARN, message, t);
	}

	private static void internalLogMessage(Level level, String message,
			Throwable t) {
		tigerstripeLogger.log(LOG4J_FQCN, level, message, t);
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
		log(new Status(IStatus.ERROR, getPluginId(),
				IJavaStatusConstants.INTERNAL_ERROR, message, null));
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
		MultiStatus multi = new MultiStatus(getPluginId(),
				IJavaStatusConstants.INTERNAL_ERROR, message, null);
		// log the status message
		multi.add(status);
		log(multi);
	}

	public static void log(Throwable e) {

		if (e instanceof TigerstripeException) {
			TigerstripeException tse = (TigerstripeException) e;
			if (tse.getException() == null) {
				IStatus status = new Status(IStatus.ERROR, getPluginId(),
						IJavaStatusConstants.INTERNAL_ERROR,
						"Internal Error", tse); //$NON-NLS-1$
				log(status);
				return;
			} else {
				MultiStatus mStatus = new MultiStatus(getPluginId(),
						IJavaStatusConstants.INTERNAL_ERROR, "Internal Error",
						e);
				Exception ee = tse.getException();

				while (ee != null) {
					IStatus subStatus = new Status(IStatus.ERROR,
							getPluginId(), IJavaStatusConstants.INTERNAL_ERROR,
							"Internal Error", ee); //$NON-NLS-1$
					mStatus.add(subStatus);
					if (ee instanceof TigerstripeException) {
						ee = ((TigerstripeException) ee).getException();
					} else if (e.getCause() instanceof Exception) {
						ee = (Exception) ee.getCause();
					} else {
						break;
					}
				}
				log(mStatus);
				return;
			}
		} else {
			if (e.getCause() == null) {
				IStatus status = new Status(IStatus.ERROR, getPluginId(),
						IJavaStatusConstants.INTERNAL_ERROR,
						"Internal Error", e); //$NON-NLS-1$
				log(status);
				return;
			} else {
				MultiStatus mStatus = new MultiStatus(getPluginId(),
						IJavaStatusConstants.INTERNAL_ERROR, "Internal Error",
						e);
				Throwable ee = e.getCause();

				while (ee != null) {
					IStatus subStatus = new Status(IStatus.ERROR,
							getPluginId(), IJavaStatusConstants.INTERNAL_ERROR,
							"Internal Error", ee); //$NON-NLS-1$
					mStatus.add(subStatus);
					if (ee instanceof TigerstripeException) {
						ee = ((TigerstripeException) ee).getException();
					} else if (e.getCause() instanceof Exception) {
						ee = ee.getCause();
					} else {
						break;
					}
				}
				log(mStatus);
				return;
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

	/**
	 * Returns the IJavaProject for the given tsProject
	 * 
	 * @param tsProject
	 * @return
	 * @since 1.1
	 */
	public static IJavaProject getIJavaProject(
			IAbstractTigerstripeProject tsProject) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		File file = new File(tsProject.getURI());
		IPath path = new Path(file.getAbsolutePath());
		IContainer container = root.getContainerForLocation(path);

		// If the project cannot be matched to a IProject, return null
		// Note this will happen for the PhantomProject.
		if (container == null)
			return null;

		return JavaCore.create((IProject) container.getAdapter(IProject.class));
	}

	public static IProject getIProject(IAbstractTigerstripeProject tsProject)
			throws TigerstripeException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		File file = new File(tsProject.getURI());
		IPath path = new Path(file.getAbsolutePath());
		IContainer container = root.getContainerForLocation(path);
		if (container instanceof IProject)
			return (IProject) container;
		throw new TigerstripeException("Can't resolve "
				+ tsProject.getBaseDir() + " as Eclipse IProject");
	}

	public static IAbstractTigerstripeProject getITigerstripeProjectFor(
			IProject project) {

		if (!project.exists() || !project.isOpen())
			return null;

		try {
			IAbstractTigerstripeProject tsProject = TigerstripeCore
					.findProject(project.getLocation().toFile().toURI());

			return tsProject;

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	public static void closeAllEditors(final boolean includeArtifactEditors,
			final boolean includeDescriptorEditors,
			final boolean includeProfileEditors,
			final boolean includePluginDescriptorEditors,
			final boolean includeDiagrams) throws TigerstripeException {

		Platform.run(new SafeRunnable("Closing Tigerstripe Editors") {
			public void run() {
				// Collect dirtyParts
				ArrayList<IEditorPart> partsToClose = new ArrayList<IEditorPart>();
				IWorkbenchWindow windows[] = PlatformUI.getWorkbench()
						.getWorkbenchWindows();
				for (int i = 0; i < windows.length; i++) {
					IWorkbenchPage pages[] = windows[i].getPages();
					for (int j = 0; j < pages.length; j++) {
						IEditorReference[] refs = pages[j]
								.getEditorReferences();
						for (IEditorReference ref : refs) {
							IEditorPart part = ref.getEditor(false);
							if (part instanceof ArtifactEditorBase
									&& includeArtifactEditors) {
								partsToClose.add(part);
							} else if (part instanceof DescriptorEditor
									&& includeDescriptorEditors) {
								partsToClose.add(part);
							} else if (part instanceof PluginDescriptorEditor
									&& includePluginDescriptorEditors) {
								partsToClose.add(part);
							} else if (includeDiagrams && part != null) {
								String partClass = part.getClass()
										.getCanonicalName();
								if (partClass
										.endsWith("TigerstripeDiagramEditor")
										|| partClass
												.endsWith("InstanceDiagramEditor")) {
									partsToClose.add(part);
								}
							}
						}
					}
				}
				if (partsToClose.size() > 0) {
					for (IEditorPart part : partsToClose) {
						if (part instanceof TigerstripeFormEditor) {
							TigerstripeFormEditor editor = (TigerstripeFormEditor) part;
							editor.close(true);
						} else {
							Display display = Display.getDefault();
							final IEditorPart fPart = part;
							display.asyncExec(new Runnable() {
								public void run() {
									PlatformUI.getWorkbench()
											.getActiveWorkbenchWindow()
											.getActivePage().closeEditor(fPart,
													true);
								}
							});
						}
					}
				}
			}
		});
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

	private void executePostInstallationActions(BundleContext context)
			throws TigerstripeException {
		(new PostInstallActions()).run(context);
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

		if (ssel != null) {
			Object selectedElement = ssel.getFirstElement();
			if (selectedElement instanceof IField) {
				IField field = (IField) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) field
						.getContainingArtifact();
				try {
					Object obj = TSExplorerUtils.getIResourceForArtifact(art);
					if (obj != null)
						selectedElement = obj;
				} catch (TigerstripeException e) {
					// ignore
				}
			} else if (selectedElement instanceof IMethod) {
				IMethod meth = (IMethod) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) meth
						.getContainingArtifact();
				try {
					Object obj = TSExplorerUtils.getIResourceForArtifact(art);
					if (obj != null)
						selectedElement = obj;
				} catch (TigerstripeException e) {
					// ignore
				}
			} else if (selectedElement instanceof ILiteral) {
				ILiteral la = (ILiteral) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingArtifact();
				try {
					Object obj = TSExplorerUtils.getIResourceForArtifact(art);
					if (obj != null)
						selectedElement = obj;
				} catch (TigerstripeException e) {
					// ignore
				}
			} else if (selectedElement instanceof AssociationEnd) {
				AssociationEnd la = (AssociationEnd) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingArtifact();
				try {
					Object obj = TSExplorerUtils.getIResourceForArtifact(art);
					if (obj != null)
						selectedElement = obj;
				} catch (TigerstripeException e) {
					// ignore
				}
			} else if (selectedElement instanceof DependencyEnd) {
				DependencyEnd la = (DependencyEnd) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingRelationship();
				try {
					Object obj = TSExplorerUtils.getIResourceForArtifact(art);
					if (obj != null)
						selectedElement = obj;
				} catch (TigerstripeException e) {
					// ignore
				}
			} else if (selectedElement instanceof DependencyEnd) {
				DependencyEnd la = (DependencyEnd) selectedElement;
				IAbstractArtifact art = (IAbstractArtifact) la
						.getContainingRelationship();
				try {
					Object obj = TSExplorerUtils.getIResourceForArtifact(art);
					if (obj != null) {
						selectedElement = obj;
					}
				} catch (TigerstripeException e) {
					// ignore
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
			return getITigerstripeProjectFor(iProject);
		return null;
	}

	/**
	 * Finds the TigerstripeExplorerPart in the workbench.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public static TigerstripeExplorerPart findTigerstripeExplorer()
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
						if (part instanceof TigerstripeExplorerPart)
							return (TigerstripeExplorerPart) part;
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

}
