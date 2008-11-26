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
import org.eclipse.jdt.internal.ui.dialogs.TypeSelectionDialog2;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactMetadataSession;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ArtifactRunnableRule;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.ArtifactRulesSection;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ArtifactRunnableRuleDetailsPage extends BaseRunnableRuleDetailsPage {

	private CCombo artifactTypeCombo;

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
			if (e.getSource() == artifactTypeCombo) {
				artifactTypeComboChanged(e);
			} else if (e.getSource() == filterClassBrowseButton) {
				filterClassBrowseButtonPressed(e);
			} else if (e.getSource() == suppressEmptyFilesButton) {
				ArtifactRunnableRule rule = (ArtifactRunnableRule) getIRunnableRule();
				rule.setSuppressEmptyFiles(suppressEmptyFilesButton
						.getSelection());
				pageModified();
			} else if (e.getSource() == overwriteFilesButton) {
				ArtifactRunnableRule rule = (ArtifactRunnableRule) getIRunnableRule();
				rule.setOverwriteFiles(overwriteFilesButton.getSelection());
				pageModified();
			} else if (e.getSource() == triggerOnDependenciesAndReferencesButton) {
				ArtifactRunnableRule rule = (ArtifactRunnableRule) getIRunnableRule();
				rule
						.setIncludeDependencies(triggerOnDependenciesAndReferencesButton
								.getSelection());
				pageModified();
			}
		}
	}

	protected void handleArtifactRuleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			ArtifactRunnableRule rule = (ArtifactRunnableRule) getIRunnableRule();
			if (e.getSource() == filterClassText) {
				rule.setArtifactFilterClass(filterClassText.getText().trim());
				pageModified();
			}
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

			TypeSelectionDialog2 dialog = new TypeSelectionDialog2(getMaster()
					.getSection().getShell(), false, null, scope,
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
			ArtifactRunnableRule rule = (ArtifactRunnableRule) getIRunnableRule();
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
		ArtifactRunnableRule rule = (ArtifactRunnableRule) getIRunnableRule();
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
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);
		
		Composite sectionClient = createRuleInfo(parent);
		
		Label label = form
				.getToolkit()
				.createLabel(
						sectionClient,
						"This template will be run while looping over all instances of the designated Artifact Type.");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);

		createArtifactDefinitions(sectionClient);
		createFilterDefinitions(sectionClient);
		createOptionButtons(sectionClient);
		form.getToolkit().paintBordersFor(parent);
	}

	protected void createOptionButtons(Composite parent) {
		// Put an empty label first to "Centre" the control

		Label l = form.getToolkit().createLabel(parent, "");
		ArtifactBasedRuleDetailsPageListener adapter = new ArtifactBasedRuleDetailsPageListener();
		suppressEmptyFilesButton = form.getToolkit().createButton(parent,
				"Suppress Empty Files", SWT.CHECK);
		suppressEmptyFilesButton
				.setEnabled(PluginDescriptorEditor.isEditable());
		suppressEmptyFilesButton.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		if (PluginDescriptorEditor.isEditable())
			suppressEmptyFilesButton.addSelectionListener(adapter);

		// Pad out the section
		l = form.getToolkit().createLabel(parent, "");

		// Put an empty label first to "Centre" the control
		l = form.getToolkit().createLabel(parent, "");
		overwriteFilesButton = form.getToolkit().createButton(parent,
				"Overwrite Files", SWT.CHECK);
		overwriteFilesButton.setEnabled(PluginDescriptorEditor.isEditable());
		overwriteFilesButton.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		if (PluginDescriptorEditor.isEditable())
			overwriteFilesButton.addSelectionListener(adapter);

		// Pad out the section
		l = form.getToolkit().createLabel(parent, "");

		// Pad out the section
		l = form.getToolkit().createLabel(parent, "");
		triggerOnDependenciesAndReferencesButton = form
				.getToolkit()
				.createButton(
						parent,
						"Include artifacts from dependencies and referenced projects",
						SWT.CHECK);
		triggerOnDependenciesAndReferencesButton
				.setEnabled(PluginDescriptorEditor.isEditable());
		triggerOnDependenciesAndReferencesButton.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		if (PluginDescriptorEditor.isEditable())
			triggerOnDependenciesAndReferencesButton
					.addSelectionListener(adapter);

	}

	protected void createArtifactDefinitions(Composite sectionClient) {

		Label l = form.getToolkit()
				.createLabel(sectionClient, "Artifact Type:");

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
		artifactTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL));
		if (PluginDescriptorEditor.isEditable()) {
			artifactTypeCombo.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					artifactTypeComboChanged(e);
				}
			});
		}

		l = form.getToolkit().createLabel(sectionClient, "");
	}


	protected void createFilterDefinitions(Composite parent) {
		Label label = form.getToolkit().createLabel(parent, "Artifact Filter:");

		ArtifactBasedRuleDetailsPageListener adapter = new ArtifactBasedRuleDetailsPageListener();

		filterClassText = form.getToolkit().createText(parent, "");
		filterClassText.setEnabled(PluginDescriptorEditor.isEditable());
		filterClassText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL));
		filterClassText.addModifyListener(adapter);
		filterClassText
				.setToolTipText("This class can be used to filter the instances of the target artifact type.");

		filterClassBrowseButton = form.getToolkit().createButton(parent,
				"Browse", SWT.PUSH);
		filterClassBrowseButton.setEnabled(PluginDescriptorEditor.isEditable());
		if (PluginDescriptorEditor.isEditable())
			filterClassBrowseButton.addSelectionListener(adapter);

	}

	public ArtifactRunnableRuleDetailsPage(ArtifactRulesSection master) {
		super(master);
	}
}
