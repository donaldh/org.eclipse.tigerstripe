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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IMethod;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.api.external.model.artifacts.ossjSpecifics.IextOssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OssjMethodOptionsSection extends ArtifactSectionPart {

	private final static int COLUMN_WIDTH = 90;

	private Map methodsMap;

	/**
	 * This encapsulate a method Properties into a TableItem
	 */
	public class PropertiesTableItem {

		public class ComboSelectionListener implements SelectionListener {

			private String propertyKey;

			private CCombo combo;

			public ComboSelectionListener(CCombo combo, String propertyKey) {
				this.propertyKey = propertyKey;
				this.combo = combo;
			}

			public void widgetSelected(SelectionEvent event) {
				int index = combo.getSelectionIndex();
				getMethodProperties().setProperty(propertyKey, options[index]);
				markPageModified();
			}

			public void widgetDefaultSelected(SelectionEvent event) {

			}

		}

		private TableItem item;

		private Properties methodProperties;

		private String baseMethodSignature;

		// A map of the combos for this item by propertiesKey
		private Map ccomboMap;

		public void setMethodProperties(Properties methodProperties) {
			this.methodProperties = methodProperties;
		}

		public Properties getMethodProperties() {
			return this.methodProperties;
		}

		/**
		 * Sets the base method signature. This is used as the label to identify
		 * a method in the first column of the item.
		 * 
		 * @param baseMethodSignature
		 */
		public void setBaseMethodSignature(String baseMethodSignature) {
			this.baseMethodSignature = baseMethodSignature;
		}

		public String getBaseMethodSignature() {
			return this.baseMethodSignature;
		}

		public PropertiesTableItem(Table parent, int style, int index,
				Properties methodProperties, String baseMethodSignature) {

			this.item = new TableItem(parent, style, index);
			this.ccomboMap = new HashMap();
			setBaseMethodSignature(baseMethodSignature);
			createItemContent();
			setMethodProperties(methodProperties);
			updateItem();
		}

		public PropertiesTableItem(Table parent, int style,
				Properties methodProperties, String baseMethodSignature) {
			this.item = new TableItem(parent, style);
			this.ccomboMap = new HashMap();
			setBaseMethodSignature(baseMethodSignature);
			createItemContent();
			setMethodProperties(methodProperties);
			updateItem();
		}

		public void refresh() {
			updateItem();
		}

		/**
		 * Removes the TableItem from the corresponding table
		 * 
		 */
		public void dispose() {
			// Determine the index for that item and remove it from
			// the table
			TableItem[] items = masterTable.getItems();
			for (int index = 0; index < items.length; index++) {
				if (items[index] == this.item) {
					masterTable.remove(index);
					items[index].dispose();

					// make sure we dispose properly of the ccombos
					for (Iterator iter = this.ccomboMap.values().iterator(); iter
							.hasNext();) {
						CCombo combo = (CCombo) iter.next();
						combo.dispose();
					}
					ccomboMap.clear();
					return;
				}
			}
		}

		/**
		 * Updates the item based on the current properties
		 * 
		 */
		protected void updateItem() {
			// for (int i = 0; i < OssjEntitySpecifics.methodDetailsCount; i++)
			// {
			// CCombo combo = (CCombo)
			// ccomboMap.get(OssjEntitySpecifics.propertiesKeys[i]);
			// int index = getIndexOf(getMethodProperties().getProperty(
			// OssjEntitySpecifics.propertiesKeys[i]));
			// combo.deselectAll();
			// combo.select(index);
			// combo.setEnabled(
			// "true".equalsIgnoreCase(getMethodProperties().getProperty("instanceMethod")));
			// }
		}

		/**
		 * Create the Item with all the contained CCombos
		 * 
		 */
		private void createItemContent() {

			// int propertiesCount = OssjEntitySpecifics.methodDetailsCount;
			// Table table = item.getParent();
			//
			// // First add a column for the name of this method
			// item.setText(0, getBaseMethodSignature());
			//
			// for (int i = 0; i < propertiesCount; i++) {
			// TableEditor editor = new TableEditor(table);
			// final CCombo combo = new CCombo(table, SWT.NONE);
			// initCombo(combo);
			// editor.grabHorizontal = true;
			// editor.setEditor(combo, item, i + 1);
			// ccomboMap.put(OssjEntitySpecifics.propertiesKeys[i], combo);
			// combo.addSelectionListener(new ComboSelectionListener(combo,
			// OssjEntitySpecifics.propertiesKeys[i]));
			// }
		}
	}

	private PropertiesTableItem createItem;

	private PropertiesTableItem getItem;

	private PropertiesTableItem setItem;

	private PropertiesTableItem deleteItem;

	private Table masterTable;

	private final String[] options = { "true", "Optional", "false" };

	public OssjMethodOptionsSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, IOssjArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider,
				ExpandableComposite.TWISTIE);
		setTitle("Methods Details");

		this.methodsMap = new HashMap();

		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		getSection().setLayout(layout);

		createMethodOptionTable(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	protected Control createMethodOptionTable(Composite parent,
			FormToolkit toolkit) {
		Composite area = toolkit.createComposite(parent);

		final GridLayout optionsGridTitleLayout = new GridLayout();
		optionsGridTitleLayout.numColumns = 2;
		area.setLayout(optionsGridTitleLayout);
		area.setLayoutData(new TableWrapData(TableWrapData.FILL));

		masterTable = toolkit.createTable(area, SWT.SINGLE | SWT.BORDER);
		masterTable.setHeaderVisible(true);
		masterTable.setLinesVisible(false);

		// Create all the columns
		TableColumn methodNameColumn = new TableColumn(masterTable, SWT.WRAP);
		methodNameColumn.setText("Method");
		methodNameColumn.setAlignment(SWT.LEFT);
		methodNameColumn.setWidth(COLUMN_WIDTH);
		// methodNameColumn.pack();

		// All the options Columns
		// for (int i = 0; i < OssjEntitySpecifics.methodDetailsCount; i++) {
		// TableColumn keyColumn = new TableColumn(masterTable, SWT.WRAP);
		// keyColumn.setText(OssjEntitySpecifics.propertiesKeys[i]);
		// keyColumn.setAlignment(SWT.CENTER);
		// keyColumn.setWidth( COLUMN_WIDTH );
		// // keyColumn.pack();
		// }

		createCrudTableItems(masterTable);
		updateCustomTableItems();

		return area;
	}

	protected void createCrudTableItems(Table table) {

		IOssjEntitySpecifics specifics = (IOssjEntitySpecifics) getIArtifact()
				.getIStandardSpecifics();

		createItem = new PropertiesTableItem(table, SWT.NULL, specifics
				.getCRUDProperties(IextOssjEntitySpecifics.CREATE), "Create");
		getItem = new PropertiesTableItem(table, SWT.NULL, specifics
				.getCRUDProperties(IextOssjEntitySpecifics.GET), "Get");
		setItem = new PropertiesTableItem(table, SWT.NULL, specifics
				.getCRUDProperties(IextOssjEntitySpecifics.SET), "Set");
		deleteItem = new PropertiesTableItem(table, SWT.NULL, specifics
				.getCRUDProperties(IextOssjEntitySpecifics.DELETE), "Remove");
	}

	/**
	 * Updates the custom methods items in the table. It creates/removes them
	 * when necessary
	 * 
	 */
	protected void updateCustomTableItems() {

		// Check if any method should be removed.
		// There is not mechanism to register a listener with the artifact for
		// changes so we need to figure it out here.
		checkForRemovedMethods();

		// Associate a PropertiesItem per method signature
		IAbstractArtifact artifact = getIArtifact();
		IMethod[] methods = artifact.getIMethods();

		for (int i = 0; i < methods.length; i++) {
			IMethod method = methods[i];
			String methodId = method.getMethodId();

			// locate the item in the map of create a new one if needed
			PropertiesTableItem item = (PropertiesTableItem) methodsMap
					.get(methodId);
			if (item == null) {
				item = new PropertiesTableItem(masterTable, SWT.NULL, method
						.getOssjMethodProperties(), method.getName());
				methodsMap.put(methodId, item);
			}

			item.setMethodProperties(method.getOssjMethodProperties());
			item.refresh();
		}

		// masterTable.redraw();
	}

	/**
	 * Removes all Properties Items from the hash and remove the corresponding
	 * TableItem in the view when the corresponding methods have been removed
	 * from the target artifact
	 * 
	 */
	protected void checkForRemovedMethods() {

		IMethod[] artifactMethods = getIArtifact().getIMethods();

		String[] artifactMethodIds = new String[artifactMethods.length];
		for (int i = 0; i < artifactMethodIds.length; i++) {
			artifactMethodIds[i] = artifactMethods[i].getMethodId();
		}

		Collection methodIds = this.methodsMap.keySet();
		String[] methodIdsArray = new String[methodIds.size()]; // need the
		// array to
		// avoid
		// concurrency
		// issues
		methodIdsArray = (String[]) methodIds.toArray(methodIdsArray);
		for (int i = 0; i < methodIdsArray.length; i++) {
			if (!Arrays.asList(artifactMethodIds).contains(methodIdsArray[i])) {
				// need to be removed!
				PropertiesTableItem item = (PropertiesTableItem) methodsMap
						.get(methodIdsArray[i]);
				item.dispose();
				methodsMap.remove(methodIdsArray[i]);
			}
		}
	}

	protected void updateCrudTableItems() {

		IOssjEntitySpecifics specifics = (IOssjEntitySpecifics) getIArtifact()
				.getIStandardSpecifics();

		createItem.setMethodProperties(specifics
				.getCRUDProperties(IextOssjEntitySpecifics.CREATE));
		createItem.refresh();

		getItem.setMethodProperties(specifics
				.getCRUDProperties(IextOssjEntitySpecifics.GET));
		getItem.refresh();

		setItem.setMethodProperties(specifics
				.getCRUDProperties(IextOssjEntitySpecifics.SET));
		setItem.refresh();

		deleteItem.setMethodProperties(specifics
				.getCRUDProperties(IextOssjEntitySpecifics.DELETE));
		deleteItem.refresh();
	}

	@Override
	public void refresh() {
		updateCrudTableItems();
		updateCustomTableItems();
		masterTable.pack();
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

}
