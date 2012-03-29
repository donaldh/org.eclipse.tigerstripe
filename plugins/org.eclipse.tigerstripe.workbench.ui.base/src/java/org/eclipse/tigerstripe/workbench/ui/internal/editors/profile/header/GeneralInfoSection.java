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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.header;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.profile.WorkbenchProfile;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchProfileRole;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.WeakRestart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.IFocusedControlProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.ProfileEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.part.FileEditorInput;

public class GeneralInfoSection extends TigerstripeSectionPart implements IFocusedControlProvider {

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class GeneralInfoPageListener implements ModifyListener,
			KeyListener {

		public void keyPressed(KeyEvent e) {
			// empty
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
			if (e.character == '\r') {
				commit(false);
			}
		}

	}

	private boolean silentUpdate;

	private Text idText;

	private Text nameText;

	private Text versionText;

	private Text descriptionText;

	public GeneralInfoSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("General Information");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		createID(getBody(), getToolkit());
		createName(getBody(), getToolkit());
		createVersion(getBody(), getToolkit());
		createDescription(getBody(), getToolkit());

		createSaveControls(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createID(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		toolkit.createLabel(parent, "ID: ");
		FileEditorInput input = (FileEditorInput) getPage().getEditorInput();
		idText = toolkit.createText(parent, input.getFile().getName());
		idText.setEnabled(false);
		idText.setEditable(false);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		idText.setLayoutData(td);
	}

	private void createName(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		toolkit.createLabel(parent, "Name: ");
		nameText = toolkit.createText(parent, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		nameText.setLayoutData(td);
		nameText.addModifyListener(new GeneralInfoPageListener());
		nameText.setEnabled(ProfileEditor.isEditable());
	}

	private void createVersion(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		toolkit.createLabel(parent, "Version: ");
		versionText = toolkit.createText(parent, "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		versionText.setLayoutData(td);
		versionText.addModifyListener(new GeneralInfoPageListener());
		versionText.setEnabled(ProfileEditor.isEditable());
	}

	private void createDescription(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		toolkit.createLabel(parent, "Description: ");
		descriptionText = toolkit.createText(parent, "", SWT.WRAP | SWT.MULTI
				| SWT.V_SCROLL);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.grabVertical = true;
		td.heightHint = 70;
		descriptionText.setLayoutData(td);
		if (ProfileEditor.isEditable())
			descriptionText.addModifyListener(new GeneralInfoPageListener());
		descriptionText.setEnabled(ProfileEditor.isEditable());
	}

	private void createSaveControls(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		FormText rtext = toolkit.createFormText(parent, true);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		rtext.setLayoutData(td);
		StringBuffer buf = new StringBuffer();

		final IWorkbenchProfileSession session = TigerstripeCore
				.getWorkbenchProfileSession();

		buf.append("<form>");
		buf.append("<p/>");
		buf
				.append("<p>A Workbench profile needs to be deployed before it can be used:</p>");
		buf
				.append("<li><a href=\"saveNReload\">Save as active profile and reload</a>: sets this profile as the active profile for this instance of Workbench, and reloads the workbench.</li>");

		buf
				.append("<li><a href=\"rollback\">Roll back to the previous active profile</a>.</li>");
		buf
				.append("<p/><p>To load the default factory profile, click <a href=\"default\">here</a>.</p>");
		buf.append("</form>");
		rtext.setText(buf.toString(), true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if ("saveNReload".equals(e.getHref())) {
					if (getPage().getEditor().isDirty()) {
						MessageDialog
								.openWarning(
										getBody().getShell(),
										"Un-saved changes",
										"This profile descriptor has un-saved changes.\n\nPlease save this file before trying to deploy this profile.");
						return;
					}
					saveNReload();
				} else if ("rollback".equals(e.getHref())) {
					rollback();
				} else if ("default".equals(e.getHref())) {
					factoryDefaults();
				}

				// and then reload all projects
				// try {
				// ResourcesPlugin.getWorkspace().run(
				// new IWorkspaceRunnable() {
				// public void run(IProgressMonitor monitor)
				// throws CoreException {
				// IWorkspace workspace = EclipsePlugin
				// .getWorkspace();
				// IProject[] projects = workspace.getRoot()
				// .getProjects();
				// for (IProject project : projects) {
				// project
				// .build(
				// IncrementalProjectBuilder.FULL_BUILD,
				// monitor);
				// }
				// }
				// }, new NullProgressMonitor());
				// } catch (CoreException ex) {
				// EclipsePlugin.log(ex);
				// }
			}
		});
	}

	private boolean rollbackCreated;

	private boolean operationSucceeded;

	/**
	 * Saves this profile as the active profile and reloads it as the active
	 * profile for this instance of Workbench.
	 * 
	 */
	protected void saveNReload() {

		if (profileHasErrors())
			return;

		if (LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.CREATE_EDIT) {
			String errMessage = "You cannot save and reload a new profile\n\n"
					+ "Your Tigerstripe license has insufficient privileges for this operation, "
					+ "please contact Tigerstripe if you wish to be able to save and reload "
					+ "workbench profiles";
			MessageDialog.openError(getBody().getShell(),
					"Save and Reload Profile Error", errMessage);
		} else {
			try {
				final IWorkbenchProfile handle = getProfile();

				if (MessageDialog
						.openConfirm(
								getBody().getShell(),
								"Save as Active Profile",
								"You are about to set this profile ('"
										+ handle.getName()
										+ "') as the active profile. \n\nThis will restart the workbench.\n\nDo you want to continue?\n\n(You will be able to rollback to the current active profile).  ")) {

					IRunnableWithProgress op = new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor) {
							try {
								monitor.beginTask(
										"Deploying new Active Profile", 10);

								IWorkbenchProfileSession session = TigerstripeCore
										.getWorkbenchProfileSession();
								monitor.subTask("Creating Profile");

								rollbackCreated = session
										.saveAsActiveProfile(handle);
								monitor.worked(2);

								monitor.subTask("Reloading workbench");
								monitor.worked(1);
								session.reloadActiveProfile();
								WeakRestart.restart(new SubProgressMonitor(monitor, 7));
								monitor.done();
								operationSucceeded = true;
							} catch (TigerstripeException e) {
								EclipsePlugin.log(e);
								operationSucceeded = false;
							}
						}
					};

					IWorkbench wb = PlatformUI.getWorkbench();
					IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
					Shell shell = win != null ? win.getShell() : null;

					try {
						ProgressMonitorDialog dialog = new ProgressMonitorDialog(
								shell);
						dialog.run(true, false, op);
					} catch (InterruptedException e) {
						EclipsePlugin.log(e);
					} catch (InvocationTargetException e) {
						EclipsePlugin.log(e);
					}

				}

			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	private IWorkbenchProfile rProfile;

	private boolean profileHasErrors() {
		FileEditorInput input = (FileEditorInput) getPage().getEditorInput();
		IFile profileRes = input.getFile();
		try {
			IMarker[] markers = profileRes.findMarkers(IMarker.PROBLEM, true,
					IResource.DEPTH_INFINITE);
			for (int i = 0; i < markers.length; i++) {
				if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(
						IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
					MessageBox dialog = new MessageBox(getSection().getShell(),
							SWT.ICON_ERROR | SWT.OK);
					dialog
							.setMessage("This profile contains errors. \nPlease fix these errors before deploying it.");
					dialog.setText("Profile Error");
					dialog.open();
					return true;
				}
			}
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
		return false;
	}

	/**
	 * When a profile is deployed, a copy of the current active profile is made
	 * to a .bak. If this file is found, a rollback is performed.
	 * 
	 */
	protected void rollback() {

		if (LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.CREATE_EDIT) {
			String errMessage = "You cannot rollback to a previous profile\n\n"
					+ "Your Tigerstripe license has insufficient privileges for this operation, "
					+ "please contact Tigerstripe if you wish to be able to rollback to previous "
					+ "workbench profiles";
			MessageDialog.openError(getBody().getShell(),
					"Rollback Profile Error", errMessage);
		} else {
			final IWorkbenchProfileSession session = TigerstripeCore
					.getWorkbenchProfileSession();
			if (!session.canRollback()) {
				MessageDialog
						.openWarning(
								getBody().getShell(),
								"No rollback possible",
								"No rollback file was found.\nImpossible to rollback to previous active profile");
				return;
			}

			if (MessageDialog
					.openConfirm(
							getBody().getShell(),
							"Rollback to previous Active Profile",
							"You are about to rollback to the previous active profile.\n\nThis will restart the workbench.\n\nDo you want to continue?\n\n(You will be able to rollback to the current active profile).  ")) {

				IRunnableWithProgress op = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {
						try {
							monitor.beginTask("Rolling back...", 10);

							monitor
									.subTask("Rolling back to previous active profile");
							rProfile = null;
							rProfile = session.rollbackActiveProfile();
							monitor.worked(2);

							monitor.subTask("Reloading Workbench");
							session.reloadActiveProfile();
							monitor.worked(1);
							WeakRestart.restart(new SubProgressMonitor(monitor, 7));
							monitor.done();
							operationSucceeded = true;
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
							operationSucceeded = false;
						}
					}
				};

				IWorkbench wb = PlatformUI.getWorkbench();
				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				Shell shell = win != null ? win.getShell() : null;

				try {
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(
							shell);
					dialog.run(true, false, op);
				} catch (InterruptedException e) {
					EclipsePlugin.log(e);
				} catch (InvocationTargetException e) {
					EclipsePlugin.log(e);
				}

			}
		}
	}

	protected void factoryDefaults() {
		if (LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.CREATE_EDIT
				&& LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.DEPLOY_UNDEPLOY) {
			String errMessage = "You cannot reset to the factory default profile\n\n"
					+ "Your Tigerstripe license has insufficient privileges for this operation, "
					+ "please contact Tigerstripe if you wish to be able to reset to the factory "
					+ "default workbench profiles";
			MessageDialog.openError(getBody().getShell(),
					"Reset Profile Error", errMessage);
		} else {
			if (MessageDialog
					.openConfirm(
							getBody().getShell(),
							"Reset Active profile to Factory Defaults?",
							"You are about to reset the active profile to factory defaults.\n\nThis will restart the workbench.\n\nDo you want to continue?\n\n(You will be able to rollback to the current active profile).  ")) {

				IRunnableWithProgress op = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {
						try {
							monitor.beginTask("Resetting profile...", 10);

							monitor
									.subTask("Resetting profile to factory defaults");
							IWorkbenchProfileSession session = TigerstripeCore
									.getWorkbenchProfileSession();
							rollbackCreated = session.setDefaultActiveProfile();
							monitor.worked(2);

							monitor.subTask("Reloading Workbench");
							session.reloadActiveProfile();
							monitor.worked(1);
							WeakRestart.restart(new SubProgressMonitor(monitor, 7));
							monitor.done();
							operationSucceeded = true;
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
							operationSucceeded = false;
						}
					}
				};

				IWorkbench wb = PlatformUI.getWorkbench();
				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				Shell shell = win != null ? win.getShell() : null;

				try {
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(
							shell);
					dialog.run(true, false, op);
				} catch (InterruptedException e) {
					EclipsePlugin.log(e);
				} catch (InvocationTargetException e) {
					EclipsePlugin.log(e);
				}

			}
		}
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	private void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	/**
	 * If silent Update is set, the form should not consider the updates to
	 * fields.
	 * 
	 * @return
	 */
	private boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			FileEditorInput input = (FileEditorInput) getPage()
					.getEditorInput();

			try {
				WorkbenchProfile handle = (WorkbenchProfile) this.getProfile();

				if (e.getSource() == nameText) {
					handle.setName(nameText.getText().trim());
				} else if (e.getSource() == versionText) {
					handle.setVersion(versionText.getText().trim());
				} else if (e.getSource() == descriptionText) {
					handle.setDescription(descriptionText.getText().trim());
				}
			} catch (TigerstripeException ee) {
				Status status = new Status(
						IStatus.ERROR,
						EclipsePlugin.getPluginId(),
						222,
						"Error refreshing form OssjDefaultForm on Tigerstripe descriptor",
						ee);
				EclipsePlugin.log(status);
			}
			markPageModified();
		}
	}

	protected void markPageModified() {
		ProfileEditor editor = (ProfileEditor) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	private IWorkbenchProfile getProfile() throws TigerstripeException {
		return ((ProfileEditor) getPage().getEditor()).getProfile();
	}

	protected void updateForm() {

		try {
			IWorkbenchProfile handle = getProfile();

			setSilentUpdate(true);

			nameText.setText(handle.getName());
			versionText.setText(handle.getVersion());
			descriptionText.setText(handle.getDescription());

			setSilentUpdate(false);
		} catch (TigerstripeException e) {
			Status status = new Status(
					IStatus.ERROR,
					EclipsePlugin.getPluginId(),
					222,
					"Error refreshing form OssjDefaultForm on Tigerstripe descriptor",
					e);
			EclipsePlugin.log(status);
		}
	}

	public Control getFocusedControl() {
		return descriptionText;
	}
}
