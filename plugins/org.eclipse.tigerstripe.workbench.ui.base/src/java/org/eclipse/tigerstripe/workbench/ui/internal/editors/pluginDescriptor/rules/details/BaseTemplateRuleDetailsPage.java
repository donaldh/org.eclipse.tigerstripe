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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable.GeneratorProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.VelocityContextDefinition;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.VMFileSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.VelocityContextDefinitionEditDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.generator.GeneratorDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.pluginDescriptor.rules.RulesSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.FileEditorInput;

public abstract class BaseTemplateRuleDetailsPage extends BaseRuleDetailsPage {

	protected TableViewer viewer;

	protected TableViewer macroViewer;

	protected Button macroAddButton;

	protected Button macroRemoveButton;

	protected Button addButton;

	protected Button removeButton;

	private Text templateText;

	private Button templateBrowseButton;

	private Text outputFile;

	class ContextDefinitionContentProvider implements
			IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ITemplateBasedRule) {
				ITemplateBasedRule rule = (ITemplateBasedRule) inputElement;
				return rule.getVelocityContextDefinitions();
			}
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class ContextDefinitionLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			VelocityContextDefinition def = (VelocityContextDefinition) obj;
			switch (index) {
			case 1:
				return def.getClassname();
			default:
				return def.getName();
			}
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	class macroContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ITemplateBasedRule) {
				ITemplateBasedRule rule = (ITemplateBasedRule) inputElement;
				return rule.getMacroLibraries();
			}
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	class macroLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String macro = (String) obj;
			switch (index) {
			case 1:
				return macro;
			default:
				return macro;
			}
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class DetailsPageListener implements ModifyListener, KeyListener,
			SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

		public void keyPressed(KeyEvent e) {
			// empty
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	public BaseTemplateRuleDetailsPage(RulesSectionPart master,
			FormToolkit formToolkit, Composite parent) {
		super(master, formToolkit, parent);
	}

	@Override
	protected void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);

		createRuleInfo(parent);
		createContextDefinitions(parent);

		getToolkit().paintBordersFor(parent);
	}

	@Override
	protected void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == templateBrowseButton) {
			VMFileSelectionDialog dialog = new VMFileSelectionDialog(null,
					false, false);
			IFile baseFile = ((FileEditorInput) getMaster().getPage()
					.getEditorInput()).getFile();
			dialog.setInput(baseFile.getProject().findMember("templates")
					.getLocation().toFile());
			dialog.setDoubleClickSelects(true);
			dialog.setTitle("Select Velocity Template");

			if (dialog.open() == Window.OK) {
				Object[] toAdd = dialog.getResult();
				for (int i = 0; i < toAdd.length; i++) {
					File file = (File) toAdd[i];

					try {
						String relative = Util.getRelativePath(file,
								((GeneratorProjectHandle) getPPProject())
										.getDescriptor().getBaseDir());

						IPath path = new Path(relative);
						IResource res = baseFile.getParent().findMember(path);

						templateText.setText(res.getProjectRelativePath()
								.toOSString());
					} catch (TigerstripeException ee) {
						TigerstripeRuntime.logErrorMessage(
								"TigerstripeException detected", ee);
					} catch (IOException ee) {
						TigerstripeRuntime.logErrorMessage(
								"IOException detected", ee);
					}
				}
			}
		} else {
			super.handleWidgetSelected(e);
		}
	}

	// ============================================================
	protected Composite createRuleInfo(Composite parent) {

		FormToolkit toolkit = getToolkit();

		DetailsPageListener adapter = new DetailsPageListener();

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

		toolkit.createLabel(sectionClient, "Template:");

		Composite templComposite = toolkit.createComposite(sectionClient);
		TableWrapLayout twLayout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		twLayout.horizontalSpacing = 5;
		templComposite.setLayout(twLayout);
		templComposite
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		templateText = toolkit.createText(templComposite, "");
		templateText.setEnabled(GeneratorDescriptorEditor.isEditable());
		templateText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		templateText.addModifyListener(adapter);
		templateText
				.setToolTipText("This template will be used when running this rule.");

		templateBrowseButton = getToolkit().createButton(templComposite,
				"Browse", SWT.PUSH);
		// Support for testing
		templateBrowseButton.setData("name", "Browse_Template");
		templateBrowseButton.setEnabled(GeneratorDescriptorEditor.isEditable());
		if (GeneratorDescriptorEditor.isEditable())
			templateBrowseButton.addSelectionListener(adapter);

		toolkit.createLabel(sectionClient, "Output File:");

		outputFile = getToolkit().createText(sectionClient, "");
		outputFile.setEnabled(GeneratorDescriptorEditor.isEditable());
		outputFile.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		outputFile.addModifyListener(adapter);
		outputFile.setToolTipText("This name of the generated file.");

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);

		return sectionClient;
	}

	protected void createContextDefinitions(Composite parent) {

		ITemplateBasedRule rule = getITemplateRunRule();
		FormToolkit toolkit = getToolkit();
		Composite sectionClient = toolkit.createComposite(parent);
		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.numColumns = 2;
		sectionClient.setLayout(twlayout);
		TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.colspan = 2;
		sectionClient.setLayoutData(twd);

		toolkit.createLabel(sectionClient, "Velocity Context Definitions:");
		toolkit.createLabel(sectionClient, "");

		Table t = toolkit.createTable(sectionClient, SWT.SINGLE
				| SWT.FULL_SELECTION);
		t.setEnabled(GeneratorDescriptorEditor.isEditable());
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 2;
		td.heightHint = 100;
		t.setLayoutData(td);

		TableColumn nameColumn = new TableColumn(t, SWT.NULL);
		nameColumn.setText("Id");
		nameColumn.pack();
		nameColumn.setWidth(50);

		TableColumn valueColumn = new TableColumn(t, SWT.NULL);
		valueColumn.setText("Class");
		valueColumn.pack();
		valueColumn.setWidth(80);

		t.setHeaderVisible(true);
		t.setLinesVisible(true);

		viewer = new TableViewer(t);
		viewer.setContentProvider(new ContextDefinitionContentProvider());
		viewer.setLabelProvider(new ContextDefinitionLabelProvider());

		// try {
		viewer.setInput(rule);
		// } catch (TigerstripeException e) {
		// TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
		// e);
		// EclipsePlugin.log(e);
		// }

		ViewerSorter nameSorter = new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object label1, Object label2) {
				return ((VelocityContextDefinition) label1).getName()
						.compareToIgnoreCase(
								((VelocityContextDefinition) label2).getName());
			}
		};
		viewer.setSorter(nameSorter);

		addButton = toolkit.createButton(sectionClient, "Add", SWT.PUSH);
		addButton.setEnabled(GeneratorDescriptorEditor.isEditable());
		addButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		if (GeneratorDescriptorEditor.isEditable()) {
			addButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					addButtonSelected(event);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}
		removeButton = toolkit.createButton(sectionClient, "Remove", SWT.PUSH);
		removeButton.setEnabled(GeneratorDescriptorEditor.isEditable());
		removeButton.setLayoutData(new TableWrapData());
		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				removeButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		if (GeneratorDescriptorEditor.isEditable()) {
			viewer.addDoubleClickListener(new IDoubleClickListener() {
				public void doubleClick(DoubleClickEvent event) {
					doubleClicked(event);
				}
			});
		}

		toolkit.paintBordersFor(sectionClient);
	}

	protected void createMacros(Composite parent) {
		ITemplateBasedRule rule = getITemplateRunRule();
		FormToolkit toolkit = getToolkit();
		Composite sectionClient = toolkit.createComposite(parent);
		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.numColumns = 2;
		sectionClient.setLayout(twlayout);
		TableWrapData twd = new TableWrapData(TableWrapData.FILL_GRAB);
		twd.colspan = 2;
		sectionClient.setLayoutData(twd);

		toolkit.createLabel(sectionClient, "Velocity Macros :");
		toolkit.createLabel(sectionClient, "");

		Table m = toolkit.createTable(sectionClient, SWT.SINGLE
				| SWT.FULL_SELECTION);
		m.setEnabled(GeneratorDescriptorEditor.isEditable());
		TableWrapData tdm = new TableWrapData(TableWrapData.FILL_GRAB);
		tdm.rowspan = 2;
		tdm.heightHint = 100;
		m.setLayoutData(tdm);

		TableColumn macroNameColumn = new TableColumn(m, SWT.NULL);
		macroNameColumn.setText("Macro File");
		macroNameColumn.pack();
		macroNameColumn.setWidth(80);

		m.setHeaderVisible(true);
		m.setLinesVisible(true);

		macroViewer = new TableViewer(m);
		macroViewer.setContentProvider(new macroContentProvider());
		macroViewer.setLabelProvider(new macroLabelProvider());

		// try {
		macroViewer.setInput(rule);
		// } catch (TigerstripeException e) {
		// TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
		// e);
		// EclipsePlugin.log(e);
		// }

		ViewerSorter macroNameSorter = new ViewerSorter() {
			@Override
			public int compare(Viewer macroViewer, Object label1, Object label2) {
				return ((String) label1).compareToIgnoreCase(((String) label2));
			}
		};
		macroViewer.setSorter(macroNameSorter);

		macroAddButton = toolkit.createButton(sectionClient, "Add", SWT.PUSH);
		macroAddButton.setEnabled(GeneratorDescriptorEditor.isEditable());
		macroAddButton.setLayoutData(new TableWrapData(TableWrapData.FILL));
		if (GeneratorDescriptorEditor.isEditable()) {
			macroAddButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					macroAddButtonSelected(event);
				}

				public void widgetDefaultSelected(SelectionEvent event) {
					// empty
				}
			});
		}
		macroRemoveButton = toolkit.createButton(sectionClient, "Remove",
				SWT.PUSH);
		macroRemoveButton.setEnabled(GeneratorDescriptorEditor.isEditable());
		macroRemoveButton.setLayoutData(new TableWrapData());
		macroRemoveButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				macroRemoveButtonSelected(event);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		toolkit.paintBordersFor(sectionClient);
	}

	private int newEntryCount;

	/**
	 * Finds a new field name
	 */
	private String findNewEntryName() {
		String result = "anEntry" + newEntryCount++;

		// make sure we're not creating a duplicate
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			String name = ((VelocityContextDefinition) items[i].getData())
					.getName();
			if (result.equals(name))
				return findNewEntryName();
		}
		return result;
	}

	protected void addButtonSelected(SelectionEvent event) {
		VelocityContextDefinition def = new VelocityContextDefinition();
		def.setName(findNewEntryName());
		def.setClassname("");

		ITemplateBasedRule rule = getITemplateRunRule();

		IJavaProject jProj = (IJavaProject) getPPProject().getAdapter(
				IJavaProject.class);
		VelocityContextDefinitionEditDialog dialog = new VelocityContextDefinitionEditDialog(
				null, def, Arrays.asList(rule.getVelocityContextDefinitions()),
				jProj, rule);
		if (dialog.open() == Window.OK) {
			viewer.add(def);
			rule.addVelocityContextDefinition(def);
			pageModified();
		}
	}

	protected ITemplateBasedRule getITemplateRunRule() {
		return (ITemplateBasedRule) getIRunRule();
	}

	protected void removeButtonSelected(SelectionEvent event) {

		ITemplateBasedRule rule = getITemplateRunRule();

		TableItem[] selectedItems = viewer.getTable().getSelection();
		VelocityContextDefinition[] selectedLabels = new VelocityContextDefinition[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (VelocityContextDefinition) selectedItems[i]
					.getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message + "these " + selectedLabels.length
					+ " context entries?";
		} else {
			message = message + "this entry?";
		}

		MessageDialog msgDialog = new MessageDialog(getShell(),
				"Remove Context Entry", null, message, MessageDialog.QUESTION,
				new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			viewer.remove(selectedLabels);
			rule.removeVelocityContextDefinitions(selectedLabels);
			pageModified();
		}
	}

	protected void doubleClicked(DoubleClickEvent event) {
		ITemplateBasedRule rule = getITemplateRunRule();
		TableItem[] selectedItems = viewer.getTable().getSelection();

		if (selectedItems.length != 0) {
			VelocityContextDefinition def = (VelocityContextDefinition) selectedItems[0]
					.getData();
			IJavaProject jProj = (IJavaProject) getPPProject().getAdapter(
					IJavaProject.class);
			VelocityContextDefinitionEditDialog dialog = new VelocityContextDefinitionEditDialog(
					null, def, Arrays.asList(rule
							.getVelocityContextDefinitions()), jProj, rule);
			if (dialog.open() == Window.OK) {
				viewer.refresh(true);
				pageModified();
			}
		}
	}

	protected void macroAddButtonSelected(SelectionEvent event) {
		ITemplateBasedRule rule = getITemplateRunRule();
		VMFileSelectionDialog dialog = new VMFileSelectionDialog(null, false,
				false);
		IFile baseFile = ((FileEditorInput) getMaster().getPage()
				.getEditorInput()).getFile();
		dialog.setInput(baseFile.getProject().findMember("templates")
				.getLocation().toFile());
		dialog.setDoubleClickSelects(true);
		dialog.setTitle("Select Velocity Macro");

		if (dialog.open() == Window.OK) {
			Object[] toAdd = dialog.getResult();
			for (int i = 0; i < toAdd.length; i++) {
				File file = (File) toAdd[i];

				try {
					String relative = Util.getRelativePath(file,
							((GeneratorProjectHandle) getPPProject())
									.getDescriptor().getBaseDir());

					IPath path = new Path(relative);
					IResource res = baseFile.getParent().findMember(path);

					macroViewer.add(res.getProjectRelativePath().toOSString());
					rule.addMacroLibrary(res.getProjectRelativePath()
							.toOSString());
					pageModified();
				} catch (TigerstripeException ee) {
					TigerstripeRuntime.logErrorMessage(
							"TigerstripeException detected", ee);
				} catch (IOException ee) {
					TigerstripeRuntime.logErrorMessage("IOException detected",
							ee);
				}
			}
		}
	}

	protected void macroRemoveButtonSelected(SelectionEvent event) {
		ITemplateBasedRule rule = getITemplateRunRule();
		TableItem[] selectedItems = macroViewer.getTable().getSelection();
		String[] selectedLabels = new String[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (String) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message + "these " + selectedLabels.length + " macros?";
		} else {
			message = message + "this macro?";
		}

		MessageDialog msgDialog = new MessageDialog(getShell(), "Remove Macro",
				null, message, MessageDialog.QUESTION, new String[] { "Yes",
						"No" }, 1);

		if (msgDialog.open() == 0) {
			macroViewer.remove(selectedLabels);
			rule.removeMacroLibraries(selectedLabels);
			pageModified();
		}
	}

	// ===================================================================
	@Override
	protected void updateForm() {
		setSilentUpdate(true);
		ITemplateBasedRule rule = getITemplateRunRule();
		templateText.setText(getITemplateRunRule().getTemplate());
		outputFile.setText(getITemplateRunRule().getOutputFile());
		viewer.setInput(rule);
		viewer.refresh(true);
		macroViewer.setInput(rule);
		macroViewer.refresh(true);
		setSilentUpdate(false);
		super.updateForm();
	}

	@Override
	public void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.
			if (e.getSource() == templateText) {
				getITemplateRunRule()
						.setTemplate(templateText.getText().trim());
				pageModified();
			} else if (e.getSource() == outputFile) {
				getITemplateRunRule()
						.setOutputFile(outputFile.getText().trim());
				pageModified();
			} else {
				super.handleModifyText(e);
			}
		}
	}
}
