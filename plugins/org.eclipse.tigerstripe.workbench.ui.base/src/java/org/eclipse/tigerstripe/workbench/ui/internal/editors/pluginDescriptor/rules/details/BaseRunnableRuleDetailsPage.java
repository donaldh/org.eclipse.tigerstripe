/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableRule;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.RulesSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public abstract class BaseRunnableRuleDetailsPage extends BaseRuleDetailsPage {

	protected Text runnableClassText;

	private Button runnableClassBrowseButton;

	public BaseRunnableRuleDetailsPage(RulesSectionPart master,
			FormToolkit formToolkit, Composite parent) {
		super(master, formToolkit, parent);
	}

	protected IRunnableRule getIRunnableRule() {
		return (IRunnableRule) getIRunRule();
	}

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class RunnableDetailsPageListener implements ModifyListener,
			KeyListener, SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			handleRunnableWidgetSelected(e);
		}

		public void keyPressed(KeyEvent e) {
			// empty
		}

		public void modifyText(ModifyEvent e) {
			handleRunnableModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	public void handleRunnableModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			IRunnableRule rule = (IRunnableRule) getIRunRule();
			if (e.getSource() == runnableClassText) {
				rule.setRunnableClassName(runnableClassText.getText().trim());
				pageModified();
			}
		}
	}

	protected void handleRunnableWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			if (e.getSource() == runnableClassBrowseButton) {
				runnableClassBrowsePressed(e);
			}
		}
	}

	// ============================================================
	protected Composite createRuleInfo(Composite parent) {

		FormToolkit toolkit = getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		layout.bottomMargin = layout.topMargin = 0;
		sectionClient.setLayout(layout);
		sectionClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		// Add the common details for a rule
		createRuleCommonInfo(sectionClient, toolkit);
		createRunnableDefinitions(sectionClient);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		return sectionClient;
	}

	protected void createRunnableDefinitions(Composite parent) {
		getToolkit().createLabel(parent, "Runnable Class Name:");

		RunnableDetailsPageListener adapter = new RunnableDetailsPageListener();

		Composite rcComposite = getToolkit().createComposite(parent);
		TableWrapLayout twLayout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		twLayout.horizontalSpacing = 5;
		rcComposite.setLayout(twLayout);
		rcComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		runnableClassText = getToolkit().createText(rcComposite, "");
		runnableClassText.setEnabled(GeneratorDescriptorEditor.isEditable());
		runnableClassText.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		runnableClassText.addModifyListener(adapter);
		runnableClassText
				.setToolTipText("An instance of this class will be created per run, and initialized with the current artifact instance.");

		runnableClassBrowseButton = getToolkit().createButton(rcComposite,
				"Browse", SWT.PUSH);
		runnableClassBrowseButton.setEnabled(GeneratorDescriptorEditor
				.isEditable());
		if (GeneratorDescriptorEditor.isEditable())
			runnableClassBrowseButton.addSelectionListener(adapter);

	}

	protected void runnableClassBrowsePressed(SelectionEvent e) {
		String runnableClass = chooseType("Runnable Class Selection",
				"Please select the runnable class for this rule.");

		if (runnableClass != null) {
			runnableClassText.setText(runnableClass);
			pageModified();
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

	@Override
	protected void updateForm() {

		super.updateForm();
		setSilentUpdate(true);
		IRunnableRule rule = getIRunnableRule();
		runnableClassText.setText(rule.getRunnableClassName());
		setSilentUpdate(false);
	}

}
