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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.rules;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.plugins.pluggable.IPluggablePluginProject;
import org.eclipse.tigerstripe.api.plugins.pluggable.IRunRule;
import org.eclipse.tigerstripe.api.plugins.pluggable.ITemplateRunRule;
import org.eclipse.tigerstripe.core.project.pluggable.rules.CopyRule;
import org.eclipse.tigerstripe.core.project.pluggable.rules.SimplePPluginRule;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.NewPPluginRuleSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.rules.details.CopyRuleDetailsPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.rules.details.SimpleRuleDetailsPage;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * 
 * @author Eric Dillon
 * 
 */
public class GlobalRulesSection extends RulesSectionPart implements IFormPart {

	public GlobalRulesSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TWISTIE);
		setTitle("&Global Rules");
		setDescription("Define the rules to be run once only per generation with this plugin.");
		getSection().marginWidth = 10;
		getSection().marginHeight = 5;
		getSection().clientVerticalSpacing = 4;

		createContent();
		updateMaster();
	}

	class MasterContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IPluggablePluginProject) {
				IPluggablePluginProject pPlugin = (IPluggablePluginProject) inputElement;
				try {
					return pPlugin.getGlobalRules();
				} catch (TigerstripeException e) {
					return new ITemplateRunRule[0];
				}
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	@Override
	protected IStructuredContentProvider getRulesListContentProvider() {
		return new MasterContentProvider();
	}

	@Override
	protected void addButtonSelected(SelectionEvent event) {

		try {
			IPluggablePluginProject pProject = getIPluggablePluginProject();

			NewPPluginRuleSelectionDialog dialog = new NewPPluginRuleSelectionDialog(
					getBody().getShell(), findNewRuleName(), pProject, pProject
							.getSupportedPluginRules(), pProject
							.getSupportedPluginRuleLabels(), pProject
							.getGlobalRules());

			if (dialog.open() == Window.OK) {
				IRunRule newRule = dialog.getNewPPluginRule();
				if (newRule != null) {
					try {
						pProject.addGlobalRule(newRule);
						getViewer().add(newRule);
						getViewer().setSelection(
								new StructuredSelection(newRule), true);
						markPageModified();
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * Triggered when the remove button is pushed
	 * 
	 */
	@Override
	protected void removeButtonSelected(SelectionEvent event) {
		TableItem[] selectedItems = getViewer().getTable().getSelection();
		IRunRule[] selectedFields = new IRunRule[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IRunRule) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedFields.length > 1) {
			message = message + "these " + selectedFields.length + " rules?";
		} else {
			message = message + "this rule?";
		}

		MessageDialog msgDialog = new MessageDialog(getBody().getShell(),
				"Remove rule", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			try {
				getIPluggablePluginProject().removeGlobalRules(selectedFields);
				getViewer().remove(selectedFields);
				markPageModified();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		updateMaster();
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(SimplePPluginRule.class,
				new SimpleRuleDetailsPage(this));
		detailsPart.registerPage(CopyRule.class, new CopyRuleDetailsPage(this));
	}

	@Override
	protected String getTooltipText() {
		return "Define/Edit global template rules for this plugin.";
	}

	@Override
	protected String getDescription() {
		return "Global template rules:";
	}
}
