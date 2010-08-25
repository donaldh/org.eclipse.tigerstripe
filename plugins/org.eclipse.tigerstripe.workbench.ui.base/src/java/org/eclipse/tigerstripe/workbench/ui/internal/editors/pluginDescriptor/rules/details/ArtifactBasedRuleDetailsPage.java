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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.details;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactMetadataSession;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.ArtifactRulesSection;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ArtifactBasedRuleDetailsPage extends BaseTemplateRuleDetailsPage {

	private CCombo artifactTypeCombo;

	private Text modelClassText;

	private Text modelClassNameText;

	private Button modelClassBrowseButton;

	private Text filterClassText;

	private Button filterClassBrowseButton;

	private Button suppressEmptyFilesButton;

	private Button overwriteFilesButton;

	private Button triggerOnDependenciesAndReferencesButton;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class ArtifactBasedRuleDetailsPageListener implements
			ModifyListener, SelectionListener {
		public void modifyText(ModifyEvent e) {
			handleArtifactRuleModifyText(e);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			handleArtifactWidgetSelected(e);
		}
	}

	protected void handleArtifactWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == modelClassBrowseButton) {
				modelClassBrowsePressed(e);
			} else if (e.getSource() == artifactTypeCombo) {
				artifactTypeComboChanged(e);
			} else if (e.getSource() == filterClassBrowseButton) {
				filterClassBrowseButtonPressed(e);
			} else if (e.getSource() == suppressEmptyFilesButton) {
				ArtifactBasedTemplateRule rule = (ArtifactBasedTemplateRule) getITemplateRunRule();
				rule.setSuppressEmptyFiles(suppressEmptyFilesButton
						.getSelection());
				pageModified();
			} else if (e.getSource() == overwriteFilesButton) {
				ArtifactBasedTemplateRule rule = (ArtifactBasedTemplateRule) getITemplateRunRule();
				rule.setOverwriteFiles(overwriteFilesButton.getSelection());
				pageModified();
			} else if (e.getSource() == triggerOnDependenciesAndReferencesButton) {
				ArtifactBasedTemplateRule rule = (ArtifactBasedTemplateRule) getITemplateRunRule();
				rule
						.setIncludeDependencies(triggerOnDependenciesAndReferencesButton
								.getSelection());
				pageModified();
			}
		}
	}

	protected void handleArtifactRuleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			ArtifactBasedTemplateRule rule = (ArtifactBasedTemplateRule) getITemplateRunRule();
			if (e.getSource() == modelClassText) {
				rule.setModelClass(modelClassText.getText().trim());
				pageModified();
			} else if (e.getSource() == modelClassNameText) {
				rule.setModelClassName(modelClassNameText.getText().trim());
				pageModified();
			} else if (e.getSource() == filterClassText) {
				rule.setArtifactFilterClass(filterClassText.getText().trim());
				pageModified();
			}
		}
	}

	protected void modelClassBrowsePressed(SelectionEvent e) {
		String modelClass = chooseType("Wrapper Class Selection",
				"Please select the wrapper class to instantiate for this rule.");

		if (modelClass != null) {
			modelClassText.setText(modelClass);
		}
	}

	private String chooseType(String dialogTitle, String dialogMessage) {
		IJavaProject jProject = (IJavaProject) getPPProject().getAdapter(
				IJavaProject.class);
		try {
			IPackageFragmentRoot[] roots = jProject
					.getAllPackageFragmentRoots();
			IJavaElement[] elements = roots;

			// TODO restrict the elements to those implementing
			// ITigerstripeContextEntry

			IJavaSearchScope scope = SearchEngine
					.createJavaSearchScope(elements);

			FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(
					getMaster().getSection().getShell(), false, null, scope,
					IJavaSearchConstants.TYPE);
			dialog.setTitle(dialogTitle);
			dialog.setMessage(dialogMessage);

			if (dialog.open() == Window.OK)
				return ((IType) dialog.getFirstResult())
						.getFullyQualifiedName();
		} catch (JavaModelException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	protected void artifactTypeComboChanged(SelectionEvent e) {
		if (e.getSource() == artifactTypeCombo) {
			ArtifactBasedTemplateRule rule = (ArtifactBasedTemplateRule) getITemplateRunRule();
			int index = artifactTypeCombo.getSelectionIndex();

			IArtifactMetadataSession session = InternalTigerstripeCore
					.getDefaultArtifactMetadataSession();
			String[] baseSupportedArtifacts = session
					.getSupportedArtifactTypes();
			String[] supportedArtifacts = new String[baseSupportedArtifacts.length + 1];
			for (int i = 0; i < baseSupportedArtifacts.length; i++) {
				supportedArtifacts[i] = baseSupportedArtifacts[i];
			}
			supportedArtifacts[baseSupportedArtifacts.length] = ArtifactBasedTemplateRule.ANY_ARTIFACT_LABEL;

			rule.setArtifactType(supportedArtifacts[index]);
			pageModified();
		}
	}

	protected void filterClassBrowseButtonPressed(SelectionEvent e) {
		String filterClass = chooseType("Filter Class Selection",
				"Please select a filter class to limit the scope of this rule.");

		if (filterClass != null) {
			filterClassText.setText(filterClass);
		}
	}

	@Override
	protected void updateForm() {
		super.updateForm();
		setSilentUpdate(true);
		ArtifactBasedTemplateRule rule = (ArtifactBasedTemplateRule) getITemplateRunRule();
		modelClassText.setText(rule.getModelClass());
		modelClassNameText.setText(rule.getModelClassName());
		filterClassText.setText(rule.getArtifactFilterClass());
		suppressEmptyFilesButton.setSelection(rule.isSuppressEmptyFiles());
		overwriteFilesButton.setSelection(rule.isOverwriteFiles());
		triggerOnDependenciesAndReferencesButton.setSelection(rule
				.isIncludeDependencies());

		IArtifactMetadataSession session = InternalTigerstripeCore
				.getDefaultArtifactMetadataSession();
		String[] baseSupportedArtifacts = session.getSupportedArtifactTypes();
		String[] supportedArtifacts = new String[baseSupportedArtifacts.length + 1];
		for (int i = 0; i < baseSupportedArtifacts.length; i++) {
			supportedArtifacts[i] = baseSupportedArtifacts[i];
		}
		supportedArtifacts[baseSupportedArtifacts.length] = ArtifactBasedTemplateRule.ANY_ARTIFACT_LABEL;

		int index = -1;
		for (int i = 0; i < supportedArtifacts.length; i++) {
			if (supportedArtifacts[i].equals(rule.getArtifactType())) {
				index = i;
			}
		}
		// if ( index != -1 ) {
		artifactTypeCombo.select(index);
		// }
		setSilentUpdate(false);
	}

	@Override
	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		parent.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = createRuleInfo(parent);

		FormText text = form.getToolkit().createFormText(sectionClient, true);
		TableWrapData twData = new TableWrapData(TableWrapData.FILL_GRAB);
		twData.colspan = 2;
		text.setLayoutData(twData);
		text
				.setText(
						"This template will be run while looping over all instances of the designated Artifact Type.",
						false, false);

		createArtifactDefinitions(sectionClient);
		createModelDefinitions(sectionClient);
		createFilterDefinitions(sectionClient);
		createOptionButtons(sectionClient);
		createContextDefinitions(sectionClient);
		createMacros(sectionClient);

		int height = sectionClient.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		master.setMinimumHeight(height);

		form.getToolkit().paintBordersFor(parent);
	}

	protected void createOptionButtons(Composite parent) {
		// Put an empty label first to "Centre" the control
		form.getToolkit().createLabel(parent, "");
		ArtifactBasedRuleDetailsPageListener adapter = new ArtifactBasedRuleDetailsPageListener();
		suppressEmptyFilesButton = form.getToolkit().createButton(parent,
				"Suppress Empty Files", SWT.CHECK);
		suppressEmptyFilesButton
				.setEnabled(PluginDescriptorEditor.isEditable());
		suppressEmptyFilesButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		if (PluginDescriptorEditor.isEditable())
			suppressEmptyFilesButton.addSelectionListener(adapter);

		// Put an empty label first to "Centre" the control
		form.getToolkit().createLabel(parent, "");
		overwriteFilesButton = form.getToolkit().createButton(parent,
				"Overwrite Files", SWT.CHECK);
		overwriteFilesButton.setEnabled(PluginDescriptorEditor.isEditable());
		overwriteFilesButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		if (PluginDescriptorEditor.isEditable())
			overwriteFilesButton.addSelectionListener(adapter);

		// Pad out the section
		form.getToolkit().createLabel(parent, "");
		triggerOnDependenciesAndReferencesButton = form
				.getToolkit()
				.createButton(
						parent,
						"Include artifacts from dependencies and referenced projects",
						SWT.CHECK);
		triggerOnDependenciesAndReferencesButton
				.setEnabled(PluginDescriptorEditor.isEditable());
		triggerOnDependenciesAndReferencesButton
				.setLayoutData(new TableWrapData(TableWrapData.FILL));
		if (PluginDescriptorEditor.isEditable())
			triggerOnDependenciesAndReferencesButton
					.addSelectionListener(adapter);

	}

	protected void createArtifactDefinitions(Composite sectionClient) {

		form.getToolkit().createLabel(sectionClient, "Artifact Type:");

		IArtifactMetadataSession session = InternalTigerstripeCore
				.getDefaultArtifactMetadataSession();
		String[] baseSupportedArtifacts = session
				.getSupportedArtifactTypeLabels();
		String[] supportedArtifacts = new String[baseSupportedArtifacts.length + 1];
		for (int i = 0; i < baseSupportedArtifacts.length; i++) {
			supportedArtifacts[i] = baseSupportedArtifacts[i];
		}
		supportedArtifacts[baseSupportedArtifacts.length] = ArtifactBasedTemplateRule.ANY_ARTIFACT_LABEL;
		artifactTypeCombo = new CCombo(sectionClient, SWT.READ_ONLY);
		artifactTypeCombo.setEnabled(PluginDescriptorEditor.isEditable());
		form.getToolkit().adapt(artifactTypeCombo, true, true);
		artifactTypeCombo.setItems(supportedArtifacts);
		artifactTypeCombo.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		if (PluginDescriptorEditor.isEditable()) {
			artifactTypeCombo.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					artifactTypeComboChanged(e);
				}
			});
		}
	}

	protected void createModelDefinitions(Composite parent) {
		form.getToolkit().createLabel(parent, "Wrapper Class:");

		ArtifactBasedRuleDetailsPageListener adapter = new ArtifactBasedRuleDetailsPageListener();

		Composite mcComposite = form.getToolkit().createComposite(parent);
		TableWrapLayout twLayout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		twLayout.horizontalSpacing = 5;
		mcComposite.setLayout(twLayout);
		mcComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		modelClassText = form.getToolkit().createText(mcComposite, "");
		modelClassText.setEnabled(PluginDescriptorEditor.isEditable());
		modelClassText
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		modelClassText.addModifyListener(adapter);
		modelClassText
				.setToolTipText("An instance of this class will be created per run, and initialized with the current artifact instance.");

		modelClassBrowseButton = form.getToolkit().createButton(mcComposite,
				"Browse", SWT.PUSH);
		modelClassBrowseButton.setData("name", "Browse_Classes");
		modelClassBrowseButton.setEnabled(PluginDescriptorEditor.isEditable());
		if (PluginDescriptorEditor.isEditable())
			modelClassBrowseButton.addSelectionListener(adapter);

		form.getToolkit().createLabel(parent, "Wrapper Class Name:");

		modelClassNameText = form.getToolkit().createText(parent, "");
		modelClassNameText.setEnabled(PluginDescriptorEditor.isEditable());
		modelClassNameText.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		modelClassNameText.addModifyListener(adapter);
		modelClassNameText
				.setToolTipText("this name will be used to refrence instances of the model in the templates (eg using $model).");
	}

	protected void createFilterDefinitions(Composite parent) {
		form.getToolkit().createLabel(parent, "Artifact Filter:");

		ArtifactBasedRuleDetailsPageListener adapter = new ArtifactBasedRuleDetailsPageListener();

		Composite fcComposite = form.getToolkit().createComposite(parent);
		TableWrapLayout twLayout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		twLayout.horizontalSpacing = 5;
		fcComposite.setLayout(twLayout);
		fcComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		filterClassText = form.getToolkit().createText(fcComposite, "");
		filterClassText.setEnabled(PluginDescriptorEditor.isEditable());
		filterClassText
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		filterClassText.addModifyListener(adapter);
		filterClassText
				.setToolTipText("This class can be used to filter the instances of the target artifact type.");

		filterClassBrowseButton = form.getToolkit().createButton(fcComposite,
				"Browse", SWT.PUSH);
		filterClassBrowseButton.setEnabled(PluginDescriptorEditor.isEditable());
		if (PluginDescriptorEditor.isEditable())
			filterClassBrowseButton.addSelectionListener(adapter);

	}

	public ArtifactBasedRuleDetailsPage(ArtifactRulesSection master) {
		super(master);
	}
}
