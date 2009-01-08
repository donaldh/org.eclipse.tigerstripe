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
import org.eclipse.jdt.internal.ui.dialogs.TypeSelectionDialog2;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ArtifactRunnableRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.RunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableRule;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.PluginDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.RulesSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public abstract class BaseRunnableRuleDetailsPage extends BaseRuleDetailsPage {

	protected Text runnableClassText;

	private Button runnableClassBrowseButton;
	
	public BaseRunnableRuleDetailsPage(RulesSectionPart master) {
		super(master);
	}
	
	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		td.heightHint = 200;
		parent.setLayoutData(td);

		form.getToolkit().paintBordersFor(parent);
	}

	
	protected IRunnableRule getIRunnableRule() {
		return (IRunnableRule) getIRunRule();
	}
	
	/**
	 * An adapter that will listen for changes on the form
	 */
	private class RunnableDetailsPageListener implements ModifyListener, KeyListener,
			SelectionListener {

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

		FormToolkit toolkit = form.getToolkit();

		RunnableDetailsPageListener adapter = new RunnableDetailsPageListener();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);

		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		sectionClient.setLayout(gLayout);
		sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Add the common details for a rule
		createRuleCommonInfo(sectionClient, toolkit);
		createRunnableDefinitions(sectionClient);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		return sectionClient;
	}
	
	protected void createRunnableDefinitions(Composite parent) {
		Label label = form.getToolkit().createLabel(parent, "Runnable Class Name:");

		RunnableDetailsPageListener adapter = new RunnableDetailsPageListener();
		
		runnableClassText = form.getToolkit().createText(parent, "");
		runnableClassText.setEnabled(PluginDescriptorEditor.isEditable());
		runnableClassText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL));
		runnableClassText.addModifyListener(adapter);
		runnableClassText
				.setToolTipText("An instance of this class will be created per run, and initialized with the current artifact instance.");

		runnableClassBrowseButton = form.getToolkit().createButton(parent,
				"Browse", SWT.PUSH);
		runnableClassBrowseButton.setEnabled(PluginDescriptorEditor.isEditable());
		if (PluginDescriptorEditor.isEditable())
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

	@Override
	protected void updateForm() {

		super.updateForm();
		setSilentUpdate(true);
		IRunnableRule rule = (IRunnableRule) getIRunnableRule();
		runnableClassText.setText(rule.getRunnableClassName());
		setSilentUpdate(false);
	}	

	
	
}