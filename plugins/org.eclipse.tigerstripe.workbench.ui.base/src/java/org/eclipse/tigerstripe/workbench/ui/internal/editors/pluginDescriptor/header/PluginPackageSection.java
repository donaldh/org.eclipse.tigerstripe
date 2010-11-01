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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.header;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable.GeneratorProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.PluggablePluginProjectPackager;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchPluggablePluginRole;
import org.eclipse.tigerstripe.workbench.project.GeneratorDeploymentHelper;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorSectionPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class PluginPackageSection extends GeneratorDescriptorSectionPart {

	public PluginPackageSection(TigerstripeFormPage page, Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Packaging");
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		createProjectComponents(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private boolean operationSucceeded = false;

	private void createProjectComponents(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;

		FormText rtext = toolkit.createFormText(parent, true);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		rtext.setLayoutData(td);
		StringBuffer buf = new StringBuffer();

		buf.append("<form>");
		buf.append("<li><a href=\"package\">Package up this generator.</a></li>");
		buf.append("</form>");
		rtext.setText(buf.toString(), true, false);
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if ("package".equals(e.getHref())) {

					if (projectHasErrors())
						return;

					if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT) {
						String errMessage = "You cannot package a Tigerstripe generator\n\n"
								+ "Your Tigerstripe license has insufficient privileges for this operation, "
								+ "please contact Tigerstripe if you wish to be able to package " + "generator";
						MessageDialog.openError(getBody().getShell(), "Package Generator Error", errMessage);
					} else {
						if (getPage().getEditor().isDirty()) {
							MessageDialog.openWarning(getBody().getShell(), "Un-saved changes",
									"This generator descriptor has un-saved changes.\n\nPlease save this file before trying to package this generator.");
							return;
						}

						DirectoryDialog dialog = new DirectoryDialog(getBody().getShell());
						dialog.setMessage("Select the desitination directory for the generator package:");

						final String dir = dialog.open();
						if (dir != null) {
							final ITigerstripeGeneratorProject projectHandle = getIPluggablePluginProject();
							IRunnableWithProgress op = new IRunnableWithProgress() {
								public void run(IProgressMonitor monitor) {
									try {
										monitor.beginTask("Packaging generator", 10);
										
										GeneratorDeploymentHelper helper = new GeneratorDeploymentHelper();
										String lPath = dir + File.separator + helper.getDefaultPluginFileName(projectHandle);
										if (!lPath.endsWith(".zip")) {
											lPath += ".zip";
										}

										PluggablePluginProjectPackager packager = new PluggablePluginProjectPackager(
												((GeneratorProjectHandle) projectHandle).getDescriptor());
										packager.packageUpProject(monitor, new Path(lPath));

										monitor.done();
										operationSucceeded = true;
									} catch (TigerstripeException ee) {
										EclipsePlugin.log(ee);
										operationSucceeded = false;
									}
								}
							};

							IWorkbench wb = PlatformUI.getWorkbench();
							IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
							Shell shell = win != null ? win.getShell() : null;

							try {
								ProgressMonitorDialog pDialog = new ProgressMonitorDialog(shell);
								pDialog.run(true, false, op);
							} catch (InterruptedException ee) {
								EclipsePlugin.log(ee);
							} catch (InvocationTargetException ee) {
								EclipsePlugin.log(ee);
							}

//							try {
//								if (operationSucceeded) {
//									MessageDialog.openInformation(getBody().getShell(), projectHandle.getName() + " Generator", "Plugin '"
//											+ projectHandle.getName() + "(" + projectHandle.getProjectDetails().getVersion()
//											+ ") was successfully packaged up as \n '" + lPath + "'.");
//								} else {
//									MessageDialog.openError(getBody().getShell(), projectHandle.getName() + " Generator", "Plugin '"
//											+ projectHandle.getName() + "(" + projectHandle.getProjectDetails().getVersion()
//											+ ") could not be packaged up. See Error log for more details.\n");
//								}
//							} catch (TigerstripeException ee) {
//								EclipsePlugin.log(ee);
//							}
						}
					}
				}
			}
		});
	}

}
