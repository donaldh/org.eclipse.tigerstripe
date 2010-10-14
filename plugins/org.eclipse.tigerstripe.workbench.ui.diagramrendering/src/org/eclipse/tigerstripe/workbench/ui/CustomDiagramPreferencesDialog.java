/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui;

import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_USE_CUSTOM_PREFERENCES;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.PropertiesSetStyle;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.CustomPreferenceStore;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.PreferencesHelper;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ComponentUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractGMFDiagramNode;
import org.eclipse.ui.PlatformUI;

public class CustomDiagramPreferencesDialog extends Dialog {

	private Button useCustom;
	private final CustomPreferenceStore store;
	private final Diagram diagram;
	private final DiagramDocumentEditor editor;
	private DiagramsPreferencePage preferencePage;

	public CustomDiagramPreferencesDialog(Shell shell,
			AbstractGMFDiagramNode node) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		editor = DiagramUtils.getDiagramEditor(node);
		if (editor != null) {
			diagram = editor.getDiagram();
		} else {
			diagram = DiagramUtils.loadFromFile(node.getDiagramFile(),
					node.getModelFile());
		}

		Assert.isNotNull(diagram);

		store = new CustomPreferenceStore(
				PreferencesHelper.toMap(PreferencesHelper.findStyle(diagram)),
				DiagramsPreferences.getGlobalStore());
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(String.format("Preferences for '%s'",
				diagram.getName()));
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayoutFactory.fillDefaults().margins(3, 3).applyTo(parent);
		Composite composite = new Composite(parent, SWT.NONE);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(composite);

		GridLayoutFactory.fillDefaults().applyTo(composite);
		useCustom = new Button(composite, SWT.CHECK);
		useCustom.setText("Use Custom Preferences");
		useCustom.setSelection(store.getBoolean(P_USE_CUSTOM_PREFERENCES));
		useCustom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				store.setValue(P_USE_CUSTOM_PREFERENCES,
						useCustom.getSelection());
				checkUseCustom();
			}
		});

		preferencePage = new DiagramsPreferencePageWithConverter(store, new Runnable() {
			public void run() {
				preferencePage.performOk();
				updatePreferences();
				convert();
			}
		});
		preferencePage.init(EclipsePlugin.getDefault().getWorkbench());
		preferencePage.createControl(composite);
		preferencePage.getApplyButton().setVisible(false);
		preferencePage.getDefaultsButton().setVisible(false);
		
		checkUseCustom();

		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(preferencePage.getControl());

		return composite;
	}

	private void convert() {
		try {
			PlatformUI.getWorkbench().getProgressService()
					.busyCursorWhile(new IRunnableWithProgress() {

						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {
							DiagramUtils.transactionSafelyRun("Update Diagram",
									editor, new Runnable() {

										public void run() {

											final IPreferenceStore storeForConverter = DiagramsPreferences
													.chooseStore(store
															.getValues());
											
											DiagramUtils.convertExiting(
													diagram, storeForConverter);

											if (editor == null) {
												try {
													diagram.getElement()
															.eResource()
															.save(Collections
																	.emptyMap());
												} catch (IOException e) {
													throw new RuntimeException(
															e);
												}
											}
										}
									});
						}
					});
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void updatePreferences() {

		DiagramUtils.transactionSafelyRun("Set Custom Diagram Preferences",
				editor, new Runnable() {

					public void run() {
						PropertiesSetStyle style = PreferencesHelper
								.findOrCreateStyle(diagram);
						style.getPropertiesMap().clear();
						for (Map.Entry<String, String> e : store.getValues()
								.entrySet()) {
							String propName = e.getKey();
							if (style.hasProperty(propName)) {
								style.setProperty(propName, e.getValue());
							} else {
								style.createProperty(propName, e.getValue());
							}
						}
					}
				});

		if (editor == null) {
			try {
				diagram.eResource().save(Collections.emptyMap());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void checkUseCustom() {
		Control control = preferencePage.getControl();
		if (control instanceof Composite) {
			ComponentUtils.setEnabledAll((Composite) control,
					store.getBoolean(P_USE_CUSTOM_PREFERENCES));
		}
	}

	@Override
	protected void okPressed() {
		preferencePage.performOk();
		updatePreferences();
		super.okPressed();
	}

}
