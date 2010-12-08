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

import java.util.Arrays;

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
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRule;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.ArtifactRulesSection;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * Class responsible for rules page of generator descriptors for an artifact model run rule
 * 
 * * <b>History of changes</b> (Name: Modification): <br/>
 * Navid Mehregani: Bug 329229 - [Form Editor] In some cases selected artifact type is not persisted for a generator descriptor <br/>
 *
 */
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
			String artifactLabel = artifactTypeCombo.getItem(index);
			String artifactType = null;
			
			if (artifactLabel.equals(IArtifactRule.ANY_ARTIFACT_LABEL)) {
				artifactType = IArtifactRule.ANY_ARTIFACT_LABEL;
			} else {
				
				IArtifactMetadataSession session = InternalTigerstripeCore.getDefaultArtifactMetadataSession();
				artifactType = session.getArtifactType(artifactLabel);
			}
			
			if (artifactType!=null) {
				rule.setArtifactType(artifactType);
				pageModified();
			} else {
				EclipsePlugin.logErrorMessage("CRITICAL ERROR: Artifact type could not be determined");
			}
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

		IArtifactMetadataSession session = InternalTigerstripeCore.getDefaultArtifactMetadataSession();
		String artifactType = rule.getArtifactType();
		if (artifactType != null && artifactType.length()>0) {
			String artifactLabel = null;
			
			if (artifactType.equals(IArtifactRule.ANY_ARTIFACT_LABEL))
				artifactLabel = IArtifactRule.ANY_ARTIFACT_LABEL;
			else
				artifactLabel = session.getArtifactLabel(artifactType);
			
			if (artifactLabel != null) {
				String[] comboBoxItems = artifactTypeCombo.getItems();
				for (int i=0; i < comboBoxItems.length; i++) {
					if (comboBoxItems[i].equals(artifactLabel)) {
						artifactTypeCombo.select(i);
						break;
					}
				}
			} else {
				artifactTypeCombo.select(-1); // Don't select anything
			}
		} else {
			artifactTypeCombo.select(-1); // Don't select anything
		}
		
		setSilentUpdate(false);
	}

	@Override
	protected void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);

		Composite sectionClient = createRuleInfo(parent);

		FormText text = getToolkit().createFormText(sectionClient, true);
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

		getToolkit().paintBordersFor(parent);
	}

	protected void createOptionButtons(Composite parent) {
		// Put an empty label first to "Centre" the control
		getToolkit().createLabel(parent, "");
		ArtifactBasedRuleDetailsPageListener adapter = new ArtifactBasedRuleDetailsPageListener();
		suppressEmptyFilesButton = getToolkit().createButton(parent,
				"Suppress Empty Files", SWT.CHECK);
		suppressEmptyFilesButton.setEnabled(GeneratorDescriptorEditor
				.isEditable());
		suppressEmptyFilesButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		if (GeneratorDescriptorEditor.isEditable())
			suppressEmptyFilesButton.addSelectionListener(adapter);

		// Put an empty label first to "Centre" the control
		getToolkit().createLabel(parent, "");
		overwriteFilesButton = getToolkit().createButton(parent,
				"Overwrite Files", SWT.CHECK);
		overwriteFilesButton.setEnabled(GeneratorDescriptorEditor.isEditable());
		overwriteFilesButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		if (GeneratorDescriptorEditor.isEditable())
			overwriteFilesButton.addSelectionListener(adapter);

		// Pad out the section
		getToolkit().createLabel(parent, "");
		triggerOnDependenciesAndReferencesButton = getToolkit().createButton(
				parent,
				"Include artifacts from dependencies and referenced projects",
				SWT.CHECK);
		triggerOnDependenciesAndReferencesButton
				.setEnabled(GeneratorDescriptorEditor.isEditable());
		triggerOnDependenciesAndReferencesButton
				.setLayoutData(new TableWrapData(TableWrapData.FILL));
		if (GeneratorDescriptorEditor.isEditable())
			triggerOnDependenciesAndReferencesButton
					.addSelectionListener(adapter);

	}

	protected void createArtifactDefinitions(Composite sectionClient) {

		getToolkit().createLabel(sectionClient, "Artifact Type:");

		IArtifactMetadataSession session = InternalTigerstripeCore.getDefaultArtifactMetadataSession();
		String[] baseSupportedArtifacts = session.getSupportedArtifactTypeLabels();
		String[] supportedArtifacts = new String[baseSupportedArtifacts.length + 1];
		
		for (int i = 0; i < baseSupportedArtifacts.length; i++) {
			supportedArtifacts[i] = baseSupportedArtifacts[i];
		}
		
		supportedArtifacts[baseSupportedArtifacts.length] = IArtifactRule.ANY_ARTIFACT_LABEL;
		
		Arrays.sort(supportedArtifacts);
		
		artifactTypeCombo = new CCombo(sectionClient, SWT.READ_ONLY);
		artifactTypeCombo.setEnabled(GeneratorDescriptorEditor.isEditable());
		getToolkit().adapt(artifactTypeCombo, true, true);
		artifactTypeCombo.setItems(supportedArtifacts);
		artifactTypeCombo.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		if (GeneratorDescriptorEditor.isEditable()) {
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
		getToolkit().createLabel(parent, "Wrapper Class:");

		ArtifactBasedRuleDetailsPageListener adapter = new ArtifactBasedRuleDetailsPageListener();

		Composite mcComposite = getToolkit().createComposite(parent);
		TableWrapLayout twLayout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		twLayout.horizontalSpacing = 5;
		mcComposite.setLayout(twLayout);
		mcComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		modelClassText = getToolkit().createText(mcComposite, "");
		modelClassText.setEnabled(GeneratorDescriptorEditor.isEditable());
		modelClassText
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		modelClassText.addModifyListener(adapter);
		modelClassText
				.setToolTipText("An instance of this class will be created per run, and initialized with the current artifact instance.");

		modelClassBrowseButton = getToolkit().createButton(mcComposite,
				"Browse", SWT.PUSH);
		modelClassBrowseButton.setData("name", "Browse_Classes");
		modelClassBrowseButton.setEnabled(GeneratorDescriptorEditor
				.isEditable());
		if (GeneratorDescriptorEditor.isEditable())
			modelClassBrowseButton.addSelectionListener(adapter);

		getToolkit().createLabel(parent, "Wrapper Class Name:");

		modelClassNameText = getToolkit().createText(parent, "");
		modelClassNameText.setEnabled(GeneratorDescriptorEditor.isEditable());
		modelClassNameText.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		modelClassNameText.addModifyListener(adapter);
		modelClassNameText
				.setToolTipText("this name will be used to refrence instances of the model in the templates (eg using $model).");
	}

	protected void createFilterDefinitions(Composite parent) {
		getToolkit().createLabel(parent, "Artifact Filter:");

		ArtifactBasedRuleDetailsPageListener adapter = new ArtifactBasedRuleDetailsPageListener();

		Composite fcComposite = getToolkit().createComposite(parent);
		TableWrapLayout twLayout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		twLayout.horizontalSpacing = 5;
		fcComposite.setLayout(twLayout);
		fcComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		filterClassText = getToolkit().createText(fcComposite, "");
		filterClassText.setEnabled(GeneratorDescriptorEditor.isEditable());
		filterClassText
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		filterClassText.addModifyListener(adapter);
		filterClassText
				.setToolTipText("This class can be used to filter the instances of the target artifact type.");

		filterClassBrowseButton = getToolkit().createButton(fcComposite,
				"Browse", SWT.PUSH);
		filterClassBrowseButton.setEnabled(GeneratorDescriptorEditor
				.isEditable());
		if (GeneratorDescriptorEditor.isEditable())
			filterClassBrowseButton.addSelectionListener(adapter);

	}

	public ArtifactBasedRuleDetailsPage(ArtifactRulesSection master,
			FormToolkit formToolkit, Composite parent) {
		super(master, formToolkit, parent);
	}
}
