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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ArtifactRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRule;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.NewPPluginRuleSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.details.ArtifactBasedRuleDetailsPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.details.ArtifactRunnableRuleDetailsPage;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * 
 * @author Eric Dillon
 * 
 */
public class ArtifactRulesSection extends RulesSectionPart implements IFormPart {

	class MasterContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ITigerstripeM1GeneratorProject) {
				ITigerstripeM1GeneratorProject pPlugin = (ITigerstripeM1GeneratorProject) inputElement;
				try {
					return pPlugin.getArtifactRules();
				} catch (TigerstripeException e) {
					return new ITemplateBasedRule[0];
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

	public ArtifactRulesSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, Section.TWISTIE | Section.EXPANDED);
		setTitle("&Artifact Rules");
		setDescription("Define the rules to be run while iterating over instances of a specific Artifact type.");
		getSection().marginWidth = 10;
		getSection().marginHeight = 5;
		getSection().clientVerticalSpacing = 4;

		createContent();
		updateMaster();
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(ArtifactBasedTemplateRule.class,
				new ArtifactBasedRuleDetailsPage(this));
		detailsPart.registerPage(ArtifactRunnableRule.class,
				new ArtifactRunnableRuleDetailsPage(this));
		
	}

	@Override
	protected void addButtonSelected(SelectionEvent event) {

		try {
			ITigerstripeM1GeneratorProject pProject = (ITigerstripeM1GeneratorProject) getIPluggablePluginProject();
			NewPPluginRuleSelectionDialog dialog = new NewPPluginRuleSelectionDialog(
					getBody().getShell(), findNewRuleName(), pProject, pProject
							.getSupportedPluginArtifactRules(), pProject
							.getSupportedPluginArtifactRuleLabels(), pProject
							.getArtifactRules());

			if (dialog.open() == Window.OK) {
				IRule newRule = dialog.getNewPPluginRule();
				if (newRule != null) {
					try {
						pProject.addArtifactRule((IArtifactRule) newRule);
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
		IArtifactRule[] selectedFields = new IArtifactRule[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedFields[i] = (IArtifactRule) selectedItems[i].getData();
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
				((ITigerstripeM1GeneratorProject) getIPluggablePluginProject())
						.removeArtifactRules(selectedFields);
				getViewer().remove(selectedFields);
				markPageModified();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		updateMaster();
	}

	@Override
	protected String getTooltipText() {
		return "Define/Edit artifact template rules for this plugin.";
	}

	@Override
	protected String getDescription() {
		return "Artifact template rules:";
	}
	
	
}
