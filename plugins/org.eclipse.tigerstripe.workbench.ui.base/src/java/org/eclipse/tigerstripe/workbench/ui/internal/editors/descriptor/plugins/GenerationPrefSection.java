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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.plugins;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.advanced.AdvancedConfigurationPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.wizards.IWizardDescriptor;

public class GenerationPrefSection extends TigerstripeSectionPart {

    public GenerationPrefSection(TigerstripeFormPage page, Composite parent,
            FormToolkit toolkit) {
        super(page, parent, toolkit, ExpandableComposite.NO_TITLE);
        createContent();
    }

    @Override
    protected void createContent() {
        TableWrapData td = new TableWrapData(TableWrapData.LEFT);
        getSection().setLayoutData(td);
        createPreferenceMsg(getBody(), getToolkit());
        getSection().setClient(getBody());
        getToolkit().paintBordersFor(getBody());
    }

    private void createPreferenceMsg(Composite parent, FormToolkit toolkit) {
        TableWrapData td = null;
        FormText rtext = toolkit.createFormText(parent, true);
        td = new TableWrapData(TableWrapData.LEFT);
        td.colspan = 2;
        rtext.setLayoutData(td);
        StringBuffer buf = new StringBuffer();
        buf.append("<form>");
        PluginManager mgr = PluginManager.getManager();
        if (mgr.getRegisteredPluggableHousings().size() <= 0) {
            buf.append("<p><b>Warning</b>: No deployed plugins were found and nothing can be generated for this model unless a plugin is"
                    + " deployed.  To create a new Tigerstripe Plugin Project, click on 'File->New->Tigerstripe Plugin Project'"
                    + " or click <a href=\"tigerstripePluginWizard\">here</a></p>");
        }
        buf.append("<p>Click <a href=\"generationPreferences\">here</a> to change the generation settings for this project.</p>");
        buf.append("</form>");
        rtext.setText(buf.toString(), true, false);
        rtext.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                if ("generationPreferences".equals(e.getHref())) {
                    getPage().getEditor().setActivePage(
                            AdvancedConfigurationPage.PAGE_ID);
                    IFormPage iFormPage = getPage().getEditor()
                            .getActivePageInstance();
                    if (iFormPage instanceof AdvancedConfigurationPage) {
                        AdvancedConfigurationPage page = (AdvancedConfigurationPage) iFormPage;
                        page.openGenerationPrefSection();
                    }
                } else if ("tigerstripePluginWizard".equals(e.getHref())) {
                    IWizardDescriptor descriptor = PlatformUI
                            .getWorkbench()
                            .getNewWizardRegistry()
                            .findWizard(
                                    "org.eclipse.tigerstripe.workbench.ui.wizards.newPluginWizard");
                    if (descriptor != null) {
                        try {
                            IWizard wizard = descriptor.createWizard();
                            WizardDialog wd = new WizardDialog(PlatformUI
                                    .getWorkbench().getDisplay()
                                    .getActiveShell(), wizard);
                            wd.setTitle(wizard.getWindowTitle());
                            wd.open();
                        } catch (CoreException coreException) {
                            MessageDialog
                                    .openError(
                                            PlatformUI.getWorkbench()
                                                    .getDisplay()
                                                    .getActiveShell(),
                                            "How Embarrassing!",
                                            "Something went wrong trying to open the new project wizard. Please select 'File->New->Tigerstripe Plugin Project' to start the wizard manually.");
                        }

                    }

                }
            }
        });

        toolkit.paintBordersFor(parent);
    }

}
