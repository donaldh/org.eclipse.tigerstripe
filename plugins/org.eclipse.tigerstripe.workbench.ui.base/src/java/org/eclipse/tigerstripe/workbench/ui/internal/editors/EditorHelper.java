/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors;

import java.util.ArrayList;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Convenience class for Tigerstripe Editors
 * 
 * @author erdillon
 * 
 */
public class EditorHelper {

	public static void closeAllEditors(final boolean includeArtifactEditors,
			final boolean includeDescriptorEditors,
			final boolean includeProfileEditors,
			final boolean includePluginDescriptorEditors,
			final boolean includeDiagrams) throws TigerstripeException {

		SafeRunner.run(new SafeRunnable("Closing Tigerstripe Editors") {
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

}
