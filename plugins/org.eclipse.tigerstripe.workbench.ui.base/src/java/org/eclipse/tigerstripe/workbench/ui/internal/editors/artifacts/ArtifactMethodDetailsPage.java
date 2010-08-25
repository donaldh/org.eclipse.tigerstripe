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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ComponentNameProvider;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.ArgumentEditDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.MethodReturnDetailsEditDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.TextEditListener.IURIBaseProviderPage;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.FileEditorInput;

public class ArtifactMethodDetailsPage implements IDetailsPage,
		IURIBaseProviderPage {

	private TextEditListener nameEditListener;

	private StereotypeSectionManager stereotypeMgr;

	public URI getBaseURI() {
		return (URI) getMethod().getAdapter(URI.class);
	}

	public ITigerstripeModelProject getProject() {
		try {
			if (getMethod() != null)
				return getMethod().getProject();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	private Section section;

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class MethodDetailsPageListener implements ModifyListener,
			KeyListener, SelectionListener, IDoubleClickListener {

		public void doubleClick(DoubleClickEvent event) {
			if (event.getSource() == argViewer) {
				editArgButtonPressed();
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.F3)
				navigateToKeyPressed(e);
		}

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
			if (e.character == '\r') {
				commit(false);
			}
		}

	}

	private IManagedForm form;

	private final ArtifactMethodsSection master;

	private IMethod method;

	private boolean silentUpdate = false;

	private Button refByValueButton;

	private Button refByKeyButton;

	private Button refByKeyResultButton;

	private Button iteratorReturnButton;

	private Button addAnno;

	private Button editAnno;

	private Button removeAnno;

	private Table annTable;

	private CCombo defaultReturnValue;

	private Text methodReturnNameText;

	private Button editReturnStereotypes;

	private boolean isReadOnly = false;

	private Label typeLabel;

	private Label multiplicityLabel;

	private Label returnLabel;

	private Label returnValueLabel;

	private final boolean displayDirection;

	public ArtifactMethodDetailsPage(ArtifactMethodsSection master,
			boolean isReadOnly) {
		super();
		this.master = master;
		this.isReadOnly = isReadOnly;
		// See if we should display the argument Direction
		GlobalSettingsProperty prop = (GlobalSettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.GLOBAL_SETTINGS);
		displayDirection = prop
				.getPropertyValue(IGlobalSettingsProperty.ARGUMENTDIRECTION);
	}

	public void createContents(Composite parent) {
		TableWrapLayout twLayout = new TableWrapLayout();
		twLayout.numColumns = 1;
		parent.setLayout(twLayout);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 300;
		parent.setLayoutData(td);

		int height = createMethodInfo(parent);
		/*
		 * FIXME Just workaround to avoid appearing scrolls on details part.
		 */
		master.setMinimumHeight(height);

		form.getToolkit().paintBordersFor(parent);
	}

	private void createStereotypes(Composite parent, FormToolkit toolkit) {
		toolkit.createLabel(parent, "Stereotypes:");

		Composite innerComposite = toolkit.createComposite(parent);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		innerComposite.setLayoutData(gd);
		TableWrapLayout layout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		innerComposite.setLayout(layout);

		annTable = toolkit.createTable(innerComposite, SWT.BORDER);
		annTable.setEnabled(!isReadOnly);

		Composite buttonsClient = toolkit.createComposite(innerComposite);
		buttonsClient.setLayoutData(new TableWrapData(TableWrapData.FILL));
		layout = new TableWrapLayout();
		layout.topMargin = layout.bottomMargin = 0;
		buttonsClient.setLayout(layout);

		addAnno = toolkit.createButton(buttonsClient, "Add", SWT.PUSH);
		// Support for testing
		addAnno.setData("name", "Add_Stereotype_Attribute");
		addAnno.setEnabled(!isReadOnly);
		addAnno.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		editAnno = toolkit.createButton(buttonsClient, "Edit", SWT.PUSH);
		editAnno.setData("name", "Edit_Stereotype_Attribute");
		editAnno.setEnabled(!isReadOnly);
		editAnno.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		removeAnno = toolkit.createButton(buttonsClient, "Remove", SWT.PUSH);
		// Support for testing
		removeAnno.setData("name", "Remove_Stereotype_Attribute");
		removeAnno.setEnabled(!isReadOnly);
		removeAnno.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Point p = buttonsClient.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = p.y;
		annTable.setLayoutData(td);
	}

	// ============================================================
	private void setIMethod(IMethod method) {
		this.method = method;
	}

	private IMethod getMethod() {
		return method;
	}

	// ============================================================
	private Text nameText;

	private Text typeText;

	private Button isVoid;

	private Button isInstanceMethodButton;

	private Button optionalButton;

	private Button abstractButton;

	private Button uniqueButton;

	private Button orderedButton;

	private Button typeBrowseButton;

	private CCombo multiplicityCombo;

	private Text commentText;

	private int createMethodInfo(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		MethodDetailsPageListener adapter = new MethodDetailsPageListener();

		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

		section = toolkit.createSection(parent, ExpandableComposite.NO_TITLE);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Composite sectionClient = toolkit.createComposite(section);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		layout.bottomMargin = layout.topMargin = 0;
		sectionClient.setLayout(layout);
		sectionClient.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		Label label = toolkit.createLabel(sectionClient, "Name: ");
		label.setEnabled(!isReadOnly);
		nameText = toolkit.createText(sectionClient, "");
		nameText.setEnabled(!isReadOnly);
		nameText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		nameText.addModifyListener(adapter);
		nameText.addKeyListener(adapter);
		TigerstripeFormEditor editor = (TigerstripeFormEditor) ((TigerstripeFormPage) getForm()
				.getContainer()).getEditor();
		if (!isReadOnly) {
			nameEditListener = new TextEditListener(editor, "name",
					IModelChangeDelta.SET, this);
			nameText.addModifyListener(nameEditListener);
		}

		label = toolkit.createLabel(sectionClient, "Description: ");
		label.setEnabled(!isReadOnly);
		commentText = toolkit.createText(sectionClient, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		commentText.setEnabled(!isReadOnly);
		commentText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		commentText.addModifyListener(adapter);
		commentText.addKeyListener(adapter);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		gd.heightHint = 70;
		gd.maxHeight = 70;
		commentText.setLayoutData(gd);

		label = toolkit.createLabel(sectionClient, "Visibility: ");
		label.setEnabled(!isReadOnly);
		Composite visiComposite = toolkit.createComposite(sectionClient);
		layout = new TableWrapLayout();
		layout.leftMargin = 0;
		layout.rightMargin = 0;
		layout.numColumns = 4;
		visiComposite.setLayout(layout);
		visiComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		publicButton = toolkit.createButton(visiComposite, "Public", SWT.RADIO);
		publicButton.setEnabled(!isReadOnly);
		publicButton.addSelectionListener(adapter);
		protectedButton = toolkit.createButton(visiComposite, "Protected",
				SWT.RADIO);
		protectedButton.setEnabled(!isReadOnly);
		protectedButton.addSelectionListener(adapter);
		privateButton = toolkit.createButton(visiComposite, "Private",
				SWT.RADIO);
		privateButton.setEnabled(!isReadOnly);
		privateButton.addSelectionListener(adapter);
		packageButton = toolkit.createButton(visiComposite, "Package",
				SWT.RADIO);
		packageButton.setEnabled(!isReadOnly);
		packageButton.addSelectionListener(adapter);

		label = toolkit.createLabel(sectionClient, "Qualifiers: "); // padding
		Composite optComposite = toolkit.createComposite(sectionClient);
		layout = new TableWrapLayout();
		layout.leftMargin = 0;
		layout.rightMargin = 0;
		layout.numColumns = 5;
		optComposite.setLayout(layout);
		optComposite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		abstractButton = toolkit.createButton(optComposite, "Abstract",
				SWT.CHECK);
		abstractButton.setEnabled(!isReadOnly);
		abstractButton.addSelectionListener(adapter);

		orderedButton = toolkit
				.createButton(optComposite, "Ordered", SWT.CHECK);
		orderedButton.setEnabled(!isReadOnly);
		orderedButton.addSelectionListener(adapter);

		uniqueButton = toolkit.createButton(optComposite, "Unique", SWT.CHECK);
		uniqueButton.setEnabled(!isReadOnly);
		uniqueButton.addSelectionListener(adapter);

		if (prop.getPropertyValue(IOssjLegacySettigsProperty.ENABLE_ISOPTIONAL)) {
			optionalButton = toolkit.createButton(optComposite, "Optional",
					SWT.CHECK);
			optionalButton.setEnabled(!isReadOnly);
			optionalButton.addSelectionListener(adapter);
		}

		if (prop
				.getPropertyValue(IOssjLegacySettigsProperty.ENABLE_INSTANCEMETHOD)) {
			isInstanceMethodButton = toolkit.createButton(optComposite,
					"Instance", SWT.CHECK);
			isInstanceMethodButton.setEnabled(!isReadOnly);
			isInstanceMethodButton.addSelectionListener(adapter);
		}

		Composite sepC = toolkit.createComposite(section);
		gd = new TableWrapData(TableWrapData.FILL_GRAB);
		gd.heightHint = 3;
		sepC.setLayoutData(gd);

		label = toolkit.createLabel(sectionClient, "Method Return: ");

		isVoid = toolkit.createButton(sectionClient, "isVoid", SWT.CHECK);
		isVoid.setEnabled(!isReadOnly);
		isVoid.addSelectionListener(adapter);

		typeLabel = toolkit.createLabel(sectionClient, "Type: ");
		typeLabel.setEnabled(!isReadOnly);

		Composite c = toolkit.createComposite(sectionClient);
		layout = TigerstripeLayoutFactory.createClearTableWrapLayout(2, false);
		layout.horizontalSpacing = 5;
		c.setLayout(layout);
		c.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		typeText = toolkit.createText(c, "");
		typeText.setEnabled(!isReadOnly);
		typeText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		typeBrowseButton = toolkit.createButton(c, "Browse", SWT.PUSH);
		typeBrowseButton.setEnabled(!isReadOnly);
		typeBrowseButton.addSelectionListener(adapter);
		typeText.addModifyListener(adapter);
		typeText.addKeyListener(adapter);

		multiplicityLabel = toolkit
				.createLabel(sectionClient, "Multiplicity: ");
		multiplicityLabel.setEnabled(!isReadOnly);
		multiplicityCombo = new CCombo(sectionClient, SWT.SINGLE
				| SWT.READ_ONLY | SWT.BORDER);
		multiplicityCombo.setEnabled(!isReadOnly);
		multiplicityCombo.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		toolkit.adapt(this.multiplicityCombo, true, true);
		multiplicityCombo.setItems(IModelComponent.EMultiplicity.labels());
		multiplicityCombo.addSelectionListener(adapter);
		multiplicityCombo.setVisibleItemCount(IModelComponent.EMultiplicity
				.values().length);

		if (prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEREFBY_MODIFIERS)) {
			toolkit.createLabel(sectionClient, "");
			Composite refComposite = toolkit.createComposite(sectionClient);
			layout = new TableWrapLayout();
			layout.numColumns = 3;
			refComposite.setLayout(layout);
			refComposite.setLayoutData(new TableWrapData(
					TableWrapData.FILL_GRAB));
			refByValueButton = toolkit.createButton(refComposite,
					IField.refByLabels[IField.REFBY_VALUE], SWT.RADIO);
			refByValueButton.addSelectionListener(adapter);
			refByKeyButton = toolkit.createButton(refComposite,
					IField.refByLabels[IField.REFBY_KEY], SWT.RADIO);
			refByKeyButton.addSelectionListener(adapter);
			refByKeyResultButton = toolkit.createButton(refComposite,
					IField.refByLabels[IField.REFBY_KEYRESULT], SWT.RADIO);
			refByKeyResultButton.addSelectionListener(adapter);
			toolkit.createLabel(sectionClient, "");
			// Iterator for multiplicity "*"
			toolkit.createLabel(sectionClient, "");
			iteratorReturnButton = toolkit.createButton(sectionClient,
					"Iterator for return", SWT.CHECK);
			iteratorReturnButton.setEnabled(!isReadOnly);
			iteratorReturnButton.addSelectionListener(adapter);
			label = toolkit.createLabel(sectionClient, "");
		}

		returnLabel = toolkit.createLabel(sectionClient, "Return label:");
		returnLabel.setEnabled(!isReadOnly);
		methodReturnNameText = toolkit.createText(sectionClient, "");
		methodReturnNameText.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		methodReturnNameText.addModifyListener(adapter);

		returnValueLabel = toolkit
				.createLabel(sectionClient, "Default Value: ");
		returnValueLabel.setEnabled(!isReadOnly);

		defaultReturnValue = new CCombo(sectionClient, SWT.SINGLE | SWT.BORDER);
		toolkit.adapt(this.defaultReturnValue, true, true);
		defaultReturnValue.setEnabled(!isReadOnly);
		defaultReturnValue.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		defaultReturnValue.addModifyListener(adapter);
		defaultReturnValue.addKeyListener(adapter);

		toolkit.createLabel(sectionClient, "");
		editReturnStereotypes = new Button(sectionClient, SWT.PUSH);
		editReturnStereotypes.setText("Return Stereotypes");
		editReturnStereotypes.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				MethodReturnDetailsEditDialog dialog = new MethodReturnDetailsEditDialog(
						master.getSection().getShell(), getMethod(), master
								.getPage().getEditor());
				dialog.open();
			}
		});

		createStereotypes(sectionClient, toolkit);

		createArgumentsTable(sectionClient);
		createExceptionTable(sectionClient);

		section.setClient(sectionClient);
		toolkit.paintBordersFor(sectionClient);
		toolkit.paintBordersFor(parent);

		return sectionClient.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
	}

	private Button addArgButton;

	private Button editArgButton;

	private Button upArgButton;

	private Button downArgButton;

	private Button removeArgButton;

	private TableViewer argViewer;

	private Button addExceptionButton;

	private Button removeExceptionButton;

	private TableViewer exceptionViewer;

	private void createArgumentsTable(Composite parent) {
		FormToolkit toolkit = form.getToolkit();

		toolkit.createLabel(parent, "Arguments");

		Composite composite = toolkit.createComposite(parent);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		composite.setLayoutData(gd);
		TableWrapLayout layout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		composite.setLayout(layout);

		MethodDetailsPageListener adapter = new MethodDetailsPageListener();
		Table table = toolkit.createTable(composite, SWT.NULL | SWT.H_SCROLL
				| SWT.V_SCROLL);
		table.setEnabled(!isReadOnly);
		table.addSelectionListener(adapter);

		Composite buttonsClient = toolkit.createComposite(composite);
		buttonsClient.setLayoutData(new TableWrapData(TableWrapData.FILL));
		layout = new TableWrapLayout();
		layout.topMargin = layout.bottomMargin = 0;
		buttonsClient.setLayout(layout);

		addArgButton = toolkit.createButton(buttonsClient, "Add", SWT.PUSH);
		// Support for testing
		addArgButton.setData("name", "Add_Argument");
		addArgButton.setEnabled(!isReadOnly);
		addArgButton.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		addArgButton.addSelectionListener(adapter);

		editArgButton = toolkit.createButton(buttonsClient, "Edit", SWT.PUSH);
		editArgButton.setEnabled(false);
		editArgButton.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		editArgButton.addSelectionListener(adapter);

		upArgButton = toolkit.createButton(buttonsClient, "Up", SWT.PUSH);
		upArgButton.setEnabled(false);
		upArgButton.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		upArgButton.addSelectionListener(adapter);

		downArgButton = toolkit.createButton(buttonsClient, "Down", SWT.PUSH);
		downArgButton.setEnabled(false);
		downArgButton.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		downArgButton.addSelectionListener(adapter);

		removeArgButton = toolkit.createButton(buttonsClient, "Remove",
				SWT.PUSH);
		// Support for testing
		removeArgButton.setData("name", "Remove_Argument");
		removeArgButton.setEnabled(false);
		removeArgButton
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		removeArgButton.addSelectionListener(adapter);

		argViewer = new TableViewer(table);
		argViewer.setContentProvider(new ArgumentContentProvider());
		argViewer.setLabelProvider(new ArgumentLabelProvider());
		argViewer.setInput("ccc"); // input is ignored
		argViewer.addDoubleClickListener(adapter);

		Point p = buttonsClient.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = p.y;
		table.setLayoutData(td);
	}

	private void createExceptionTable(Composite parent) {
		FormToolkit toolkit = form.getToolkit();

		toolkit.createLabel(parent, "Exceptions");

		Composite composite = toolkit.createComposite(parent);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		composite.setLayoutData(gd);
		TableWrapLayout layout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		composite.setLayout(layout);

		MethodDetailsPageListener adapter = new MethodDetailsPageListener();
		Table table = toolkit.createTable(composite, SWT.NULL);
		table.addSelectionListener(adapter);

		Composite buttonsClient = toolkit.createComposite(composite);
		buttonsClient.setLayoutData(new TableWrapData(TableWrapData.FILL));
		layout = new TableWrapLayout();
		layout.topMargin = layout.bottomMargin = 0;
		buttonsClient.setLayout(layout);

		addExceptionButton = toolkit.createButton(buttonsClient, "Add",
				SWT.PUSH);
		// Support for testing
		addExceptionButton.setData("name", "Add_Exception");
		addExceptionButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		addExceptionButton.addSelectionListener(adapter);

		removeExceptionButton = toolkit.createButton(buttonsClient, "Remove",
				SWT.PUSH);
		// Support for testing
		removeExceptionButton.setData("name", "Remove_Exception");
		removeExceptionButton.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB));
		removeExceptionButton.setEnabled(false);
		removeExceptionButton.addSelectionListener(adapter);

		exceptionViewer = new TableViewer(table);
		exceptionViewer.setContentProvider(new ExceptionContentProvider());
		exceptionViewer.setLabelProvider(new ExceptionLabelProvider());
		exceptionViewer.setInput("ccc"); // input is ignored

		Point p = buttonsClient.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = p.y;
		table.setLayoutData(td);
	}

	public class ArgumentContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			IMethod mtd = getMethod();
			if (mtd != null) {
				Object[] args = new Object[mtd.getArguments().size()];
				Iterator<IArgument> argIterator = mtd.getArguments().iterator();
				for (int i = 0; i < mtd.getArguments().size(); i++) {
					args[i] = argIterator.next();
				}
				return args;
			}
			return new IMethod.IArgument[0];
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

	}

	public class ArgumentLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getText(Object element) {
			if (element instanceof IArgument) {
				IArgument arg = (IArgument) element;

				String fqn = arg.getType().getFullyQualifiedName();
				if (((AbstractArtifact) arg.getContainingArtifact())
						.getArtifactManager() != null) {
					ArtifactManager mgr = ((AbstractArtifact) arg
							.getContainingArtifact()).getArtifactManager();
					if (mgr.getArtifactByFullyQualifiedName(fqn, true,
							new NullProgressMonitor()) instanceof IPrimitiveTypeArtifact) {
						fqn = arg.getType().getName();
					}
				}
				String directionPrefix = arg.getDirection().getLabel();

				String stereotypePrefix = "";
				boolean isFirstInstance = true;
				for (IStereotypeInstance instance : arg
						.getStereotypeInstances()) {
					if (isFirstInstance) {
						stereotypePrefix += "<<";
					}
					if (!isFirstInstance) {
						stereotypePrefix += ",";
					}
					stereotypePrefix += instance.getName();
					isFirstInstance = false;
				}
				if (stereotypePrefix.length() != 0) {
					stereotypePrefix += ">> ";
				}

				String label = "";

				// If the profile allows it, show the direction prefix

				if (displayDirection) {
					label = directionPrefix + " ";
				}

				label = label + stereotypePrefix + arg.getName() + ": "
						+ Misc.removeJavaLangString(fqn);

				if (arg.getType().getTypeMultiplicity() != IModelComponent.EMultiplicity.ONE) {
					label = label + "["
							+ arg.getType().getTypeMultiplicity().getLabel()
							+ "]";
				}

				if (arg.getDefaultValue() != null) {
					if (arg.getDefaultValue().length() != 0) {
						label = label + " = " + arg.getDefaultValue();
					} else {
						label = label + " = \"\"";
					}
				}
				return label;
			}
			return null;
		}

		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

	}

	public class ExceptionContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			IMethod mtd = getMethod();
			if (mtd != null)
				if (mtd != null) {
					Object[] excs = new Object[mtd.getExceptions().size()];
					Iterator<IException> excIterator = mtd.getExceptions()
							.iterator();
					for (int i = 0; i < mtd.getExceptions().size(); i++) {
						excs[i] = excIterator.next();
					}
					return excs;
				}
			return new IMethod.IException[0];
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

	}

	public class ExceptionLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			return Images.get(Images.EXCEPTION_ICON);
		}

		public String getText(Object element) {
			if (element instanceof IMethod.IException) {
				IMethod.IException arg = (IMethod.IException) element;
				return arg.getFullyQualifiedName();
			}
			return null;
		}

		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

	}

	// ===================================================================

	private Button publicButton;

	private Button protectedButton;

	private Button privateButton;

	private Button packageButton;

	public void initialize(IManagedForm form) {
		this.form = form;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isDirty() {
		return master.isDirty();
	}

	public void commit(boolean onSave) {
	}

	public boolean setFormInput(Object input) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setFocus() {
		nameText.setFocus();
	}

	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

	public void refresh() {
		updateForm();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		if (part instanceof ArtifactMethodsSection) {
			if (nameEditListener != null)
				nameEditListener.reset();

			Table methodsTable = master.getViewer().getTable();

			IMethod selected = (IMethod) methodsTable.getSelection()[0]
					.getData();
			setIMethod(selected);

			ArtifactEditorBase editor = (ArtifactEditorBase) master.getPage()
					.getEditor();

			if (stereotypeMgr == null) {
				stereotypeMgr = new StereotypeSectionManager(addAnno, editAnno,
						removeAnno, annTable, getMethod(), master.getSection()
								.getShell(), editor);
				stereotypeMgr.delegate();
			} else {
				stereotypeMgr.setArtifactComponent(getMethod());
			}

			updateForm();
		}
	}

	private void updateForm() {

		setSilentUpdate(true);
		nameText.setText(getMethod().getName());

		if (isInstanceMethodButton != null)
			isInstanceMethodButton.setSelection(getMethod().isInstanceMethod());

		// Instance button should only be enabled if we're part of an Artifacts
		// that supports it
		if (isInstanceMethodButton != null)
			isInstanceMethodButton.setEnabled(!isReadOnly
					&& master.getContentProvider().enabledInstanceMethods());

		// If the instanceButton is selected we shouldn't allow for exceptions
		// to be set here. (RC comment)
		// Except that we need it if the instance methods are not allowed!
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

		boolean enabled = prop
				.getPropertyValue(IOssjLegacySettigsProperty.ENABLE_INSTANCEMETHOD);

		boolean exceptionsAllowed = !isReadOnly
				&& (!getMethod().isInstanceMethod() || !enabled);
		exceptionViewer.getTable().setEnabled(exceptionsAllowed);
		addExceptionButton.setEnabled(exceptionsAllowed);
		removeExceptionButton.setEnabled(exceptionsAllowed);

		if (optionalButton != null)
			optionalButton.setSelection(getMethod().isOptional());
		abstractButton.setSelection(getMethod().isAbstract());

		if (getMethod().isVoid()
				|| !getMethod().getReturnType().getTypeMultiplicity().isArray()) {
			getMethod().setOrdered(false);
			getMethod().setUnique(false);
		}
		orderedButton.setSelection(getMethod().isOrdered());
		uniqueButton.setSelection(getMethod().isUnique());

		isVoid.setSelection(getMethod().isVoid());

		typeText.setEnabled(!isReadOnly && !getMethod().isVoid());
		methodReturnNameText.setText(getMethod().getReturnName());
		methodReturnNameText.setEnabled(!isReadOnly && !getMethod().isVoid());

		boolean refByEnabled = !isReadOnly && !getMethod().isVoid()
				&& getMethod().getReturnType() != null
				&& getMethod().getReturnType().isEntityType();

		if (refByKeyButton != null)
			refByKeyButton.setEnabled(refByEnabled);
		if (refByKeyResultButton != null)
			refByKeyResultButton.setEnabled(refByEnabled);
		if (refByValueButton != null)
			refByValueButton.setEnabled(refByEnabled);

		int returnRefBy = getMethod().getReturnRefBy();
		switch (returnRefBy) {
		case IField.REFBY_KEY:
			if (refByKeyButton != null)
				refByKeyButton.setSelection(true);
			if (refByValueButton != null)
				refByValueButton.setSelection(false);
			if (refByKeyResultButton != null)
				refByKeyResultButton.setSelection(false);
			break;
		case IField.REFBY_KEYRESULT:
			if (refByKeyButton != null)
				refByKeyButton.setSelection(false);
			if (refByValueButton != null)
				refByValueButton.setSelection(false);
			if (refByKeyResultButton != null)
				refByKeyResultButton.setSelection(true);
			break;
		case IField.REFBY_VALUE:
			if (refByKeyButton != null)
				refByKeyButton.setSelection(false);
			if (refByValueButton != null)
				refByValueButton.setSelection(true);
			if (refByKeyResultButton != null)
				refByKeyResultButton.setSelection(false);
			break;
		}

		if (isInstanceMethodButton != null) {
			multiplicityCombo.setEnabled(!isReadOnly && !getMethod().isVoid()
					&& !getMethod().isInstanceMethod());
		} else {
			multiplicityCombo.setEnabled(!isReadOnly && !getMethod().isVoid());
		}
		typeBrowseButton.setEnabled(!isReadOnly && !getMethod().isVoid());
		typeText.setText(Misc.removeJavaLangString(getMethod().getReturnType()
				.getFullyQualifiedName()));
		if (!getMethod().isVoid()) {
			multiplicityCombo
					.select(IModelComponent.EMultiplicity.indexOf(getMethod()
							.getReturnType().getTypeMultiplicity()));
			updateDefaultValueCombo();
			defaultReturnValue.setEnabled(!isReadOnly);
			editReturnStereotypes.setEnabled(!isReadOnly);
			typeLabel.setEnabled(!isReadOnly);
			multiplicityLabel.setEnabled(!isReadOnly);
			returnLabel.setEnabled(!isReadOnly);
			returnValueLabel.setEnabled(!isReadOnly);
			if (getMethod().getReturnType().getTypeMultiplicity().isArray()) {
				orderedButton.setEnabled(true);
				uniqueButton.setEnabled(true);
			} else {
				orderedButton.setEnabled(false);
				orderedButton.setSelection(false);
				getMethod().setOrdered(false);
				uniqueButton.setEnabled(false);
				uniqueButton.setSelection(true);
				getMethod().setUnique(true);
			}

		} else {
			defaultReturnValue.clearSelection();
			defaultReturnValue.setText("");
			defaultReturnValue.setEnabled(false);
			editReturnStereotypes.setEnabled(false);
			typeLabel.setEnabled(false);
			multiplicityLabel.setEnabled(false);
			returnLabel.setEnabled(false);
			returnValueLabel.setEnabled(false);
			orderedButton.setEnabled(false);
			uniqueButton.setEnabled(false);
		}

		// Enablement of Iterator result button
		// !void, multiplicty of *, returned type is entity type
		// NOTE: only care about it if displayed, i.e. it has not been disabled
		// thru
		// profile settings by removing OSSJ-isms.
		if (iteratorReturnButton != null) {
			iteratorReturnButton.setSelection(getMethod().isIteratorReturn());
			if (!getMethod().isVoid()) {
				if (getMethod().getReturnType().getTypeMultiplicity().isArray()
						&& getMethod().getReturnType().isEntityType()) {
					iteratorReturnButton.setEnabled(!isReadOnly);
				} else {
					iteratorReturnButton.setEnabled(false);
				}
			} else {
				iteratorReturnButton.setEnabled(false);
			}
		}
		setVisibility(getMethod().getVisibility());
		commentText.setText(getMethod().getComment() != null ? getMethod()
				.getComment() : "");

		if (getMethod().getDefaultReturnValue() != null) {
			if (defaultReturnValue.getItemCount() != 0) {
				String[] items = defaultReturnValue.getItems();
				int i = 0, index = -1;
				for (String item : items) {
					if (item.equals(getMethod().getDefaultReturnValue())) {
						index = i;
					}
					i++;
				}
				if (index != -1) {
					defaultReturnValue.select(index);
				} else {
					defaultReturnValue.clearSelection();
					defaultReturnValue.setText("");
				}
			} else {
				defaultReturnValue.setText(getMethod().getDefaultReturnValue());
			}
		} else {
			defaultReturnValue.clearSelection();
			defaultReturnValue.setText("");
		}

		removeArgButton.setEnabled(!isReadOnly
				&& argViewer.getTable().getSelectionCount() > 0);
		upArgButton.setEnabled(!isReadOnly
				&& argViewer.getTable().getSelectionCount() > 0);
		downArgButton.setEnabled(!isReadOnly
				&& argViewer.getTable().getSelectionCount() > 0);
		editArgButton.setEnabled(!isReadOnly
				&& argViewer.getTable().getSelectionCount() > 0);

		argViewer.refresh();
		argViewer.getTable().redraw();

		removeExceptionButton.setEnabled(!isReadOnly
				&& exceptionViewer.getTable().getSelectionCount() > 0);
		exceptionViewer.refresh();
		exceptionViewer.getTable().redraw();

		if (stereotypeMgr != null) {
			stereotypeMgr.refresh();
		}

		setSilentUpdate(false);
	}

	private void setVisibility(EVisibility visibility) {
		publicButton.setSelection(visibility.equals(EVisibility.PUBLIC));
		protectedButton.setSelection(visibility.equals(EVisibility.PROTECTED));
		privateButton.setSelection(visibility.equals(EVisibility.PRIVATE));
		packageButton.setSelection(visibility.equals(EVisibility.PACKAGE));
	}

	private EVisibility getVisibility() {
		if (publicButton.getSelection())
			return EVisibility.PUBLIC;
		else if (protectedButton.getSelection())
			return EVisibility.PROTECTED;
		else if (packageButton.getSelection())
			return EVisibility.PACKAGE;
		else
			return EVisibility.PRIVATE;
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	private void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	/**
	 * If silent Update is set, the form should not consider the updates to
	 * fields.
	 * 
	 * @return
	 */
	private boolean isSilentUpdate() {
		return this.silentUpdate;
	}

	private void handleWidgetSelected(SelectionEvent event) {
		if (event.getSource() == isVoid) {
			getMethod().setVoid(isVoid.getSelection());
			pageModified();
			refreshViewerForMethodLabel();
		} else if (event.getSource() == abstractButton) {
			getMethod().setAbstract(abstractButton.getSelection());
			pageModified();
		} else if (event.getSource() == orderedButton) {
			getMethod().setOrdered(orderedButton.getSelection());
			pageModified();
		} else if (event.getSource() == uniqueButton) {
			getMethod().setUnique(uniqueButton.getSelection());
			pageModified();
		} else if (event.getSource() == optionalButton) {
			getMethod().setOptional(optionalButton.getSelection());
			pageModified();
		} else if (event.getSource() == isInstanceMethodButton) {
			getMethod()
					.setInstanceMethod(isInstanceMethodButton.getSelection());
			pageModified();
		} else if (!isVoid.getSelection()
				&& event.getSource() == this.multiplicityCombo) {
			IType type = getMethod().makeType();
			type.setFullyQualifiedName(this.typeText.getText().trim());
			type.setTypeMultiplicity(IModelComponent.EMultiplicity
					.at(multiplicityCombo.getSelectionIndex()));
			getMethod().setReturnType(type);
			pageModified();
			refreshViewerForMethodLabel();
		} else if (event.getSource() == refByKeyButton) {
			getMethod().setReturnRefBy(IField.REFBY_KEY);
			pageModified();
		} else if (event.getSource() == refByKeyResultButton) {
			getMethod().setReturnRefBy(IField.REFBY_KEYRESULT);
			pageModified();
		} else if (event.getSource() == refByValueButton) {
			getMethod().setReturnRefBy(IField.REFBY_VALUE);
			pageModified();
		} else if (event.getSource() == publicButton
				|| event.getSource() == privateButton
				|| event.getSource() == protectedButton
				|| event.getSource() == packageButton) {
			getMethod().setVisibility(getVisibility());
			pageModified();
		} else if (event.getSource() == iteratorReturnButton) {
			getMethod().setIteratorReturn(iteratorReturnButton.getSelection());
			pageModified();
		} else if (event.getSource() == addArgButton) {
			addArgButtonPressed();
		} else if (event.getSource() == removeArgButton) {
			removeArgButtonPressed();
		} else if (event.getSource() == upArgButton) {
			upArgButtonPressed();
		} else if (event.getSource() == downArgButton) {
			downArgButtonPressed();
		} else if (event.getSource() == editArgButton) {
			editArgButtonPressed();
		} else if (event.getSource() == typeBrowseButton) {
			browseButtonPressed();
		} else if (event.getSource() == addExceptionButton) {
			addExceptionButtonPressed();
		} else if (event.getSource() == removeExceptionButton) {
			removeExceptionButtonPressed();
		}
		updateForm();
	}

	/**
	 * Opens up a dialog box to browse for Artifacts
	 * 
	 * @since 1.1
	 */
	private void browseButtonPressed() {

		try {
			BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(master
					.getIArtifact().getTigerstripeProject(), Method
					.getSuitableTypes());
			dialog.setTitle("Artifact Type Selection");
			dialog.setMessage("Enter a filter (* = any number of characters)"
					+ " or an empty string for no filtering: ");

			IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
					master.getSection().getShell(), new ArrayList());
			if (artifacts.length != 0) {
				typeText.setText(artifacts[0].getFullyQualifiedName());
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	// private void browseButtonPressed() {
	// String initialType = typeText.getText().trim();
	// FileEditorInput input = (FileEditorInput) master.getPage()
	// .getEditorInput();
	// IJavaElement elem = JavaCore.create(input.getFile());
	// try {
	// org.eclipse.jdt.core.IType type = Utils.chooseAttributeType(master
	// .getSection().getShell(), elem.getJavaProject()
	// .getAllPackageFragmentRoots()[0], initialType);
	// if (type != null) {
	// typeText.setText(type.getFullyQualifiedName());
	// }
	// } catch (JavaModelException e) {
	// EclipsePlugin.log(e);
	// }
	// }

	private void addArgButtonPressed() {
		IType type = getMethod().makeType();
		try {
			type.setFullyQualifiedName(getDefaultTypeName());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			MessageDialog.openWarning(master.getSection().getShell(),
					"Default "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									IPrimitiveTypeImpl.class.getName())
									.getLabel(null) + " For Parameter", e
							.getMessage());
			return;
		}
		type.setTypeMultiplicity(EMultiplicity.ONE);

		IArgument newArg = getMethod().makeArgument();
		ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();
		String newArgName = nameFactory.getNewArgumentName(getMethod());

		newArg.setName(newArgName);
		newArg.setType(type);

		getMethod().addArgument(newArg);
		argViewer.add(newArg);
		argViewer.setSelection(new StructuredSelection(newArg), true);
		pageModified();
		refreshViewerForMethodLabel();
		updateForm();
	}

	/**
	 * Gets the default attribute type from the active profile.
	 */
	private String getDefaultTypeName() throws TigerstripeException {
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		return profile.getDefaultPrimitiveTypeString();
	}

	private void removeArgButtonPressed() {
		TableItem[] selectedItems = this.argViewer.getTable().getSelection();
		IArgument[] selectedLabels = new IArgument[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (IArgument) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message + "these " + selectedLabels.length
					+ " arguments?";
		} else {
			message = message + "this argument?";
		}

		MessageDialog msgDialog = new MessageDialog(master.getSection()
				.getShell(), "Remove Parameter", null, message,
				MessageDialog.QUESTION, new String[] { "Yes", "No" }, 1);

		if (msgDialog.open() == 0) {
			argViewer.remove(selectedLabels);
			getMethod().removeArguments(Arrays.asList(selectedLabels));
			this.pageModified();
			refreshViewerForMethodLabel();
		}
		updateForm();
	}

	private void addExceptionButtonPressed() {

		String initialType = "";
		// FileEditorInput input = (FileEditorInput) master.getPage()
		// .getEditorInput();
		// IJavaElement elem = JavaCore.create(input.getFile());
		try {
			BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(master
					.getIArtifact().getTigerstripeProject(),
					ExceptionArtifact.MODEL);
			// Fix the text for the Title and Message with the specific Artifact
			// Type
			// Bug # 124
			dialog.setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
					IExceptionArtifactImpl.class.getName()).getLabel(null)
					+ " Selection for Method ");
			dialog.setMessage("Select the "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IExceptionArtifactImpl.class.getName()).getLabel(
							null) + " for the method " + getMethod().getName());

			IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
					master.getSection().getShell(), Arrays
							.asList(new Object[] { master.getIArtifact() }));
			if (artifacts.length != 0) {
				initialType = artifacts[0].getFullyQualifiedName();
				IMethod.IException newArg = getMethod().makeException();
				newArg.setFullyQualifiedName(initialType);

				getMethod().addException(newArg);
				exceptionViewer.add(newArg);
				exceptionViewer.setSelection(new StructuredSelection(newArg),
						true);
				pageModified();
				updateForm();
			}
		} catch (TigerstripeException e) {

		}
	}

	private void removeExceptionButtonPressed() {
		TableItem[] selectedItems = this.exceptionViewer.getTable()
				.getSelection();
		IMethod.IException[] selectedLabels = new IMethod.IException[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedLabels[i] = (IMethod.IException) selectedItems[i].getData();
		}

		String message = "Do you really want to remove ";
		if (selectedLabels.length > 1) {
			message = message
					+ "these "
					+ selectedLabels.length
					+ " "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IExceptionArtifactImpl.class.getName()).getLabel(
							null) + "(s)?";
		} else {
			message = message
					+ "this "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IExceptionArtifactImpl.class.getName()).getLabel(
							null);
		}

		MessageDialog msgDialog = new MessageDialog(master.getSection()
				.getShell(), "Remove "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IExceptionArtifactImpl.class.getName()).getLabel(null),
				null, message, MessageDialog.QUESTION, new String[] { "Yes",
						"No" }, 1);

		if (msgDialog.open() == 0) {
			exceptionViewer.remove(selectedLabels);
			getMethod().removeExceptions(Arrays.asList(selectedLabels));
			this.pageModified();
		}
		updateForm();
	}

	private void upArgButtonPressed() {
		// Added method contents - #96
		TableItem[] selectedItems = this.argViewer.getTable().getSelection();
		IArgument[] selectedArgs = new IArgument[selectedItems.length];
		for (int i = 0; i < selectedItems.length; i++) {
			selectedArgs[i] = (IArgument) selectedItems[i].getData();
		}
		TableItem[] allItems = this.argViewer.getTable().getItems();
		IArgument[] allArgs = new IArgument[allItems.length];
		IArgument[] newArgs = new IArgument[allItems.length];
		for (int i = 0; i < allArgs.length; i++) {
			newArgs[i] = (IArgument) allItems[i].getData();
			if (allItems[i].getData().equals(selectedArgs[0]) && i != 0) {
				newArgs[i] = newArgs[i - 1];
				newArgs[i - 1] = (IArgument) allItems[i].getData();
			}
		}
		method.setArguments(Arrays.asList(newArgs));
		pageModified();
		refreshViewerForMethodLabel();
		updateForm();
	}

	private void downArgButtonPressed() {
		// Added method contents - #96
		TableItem[] selectedItems = this.argViewer.getTable().getSelection();
		IArgument[] selectedArgs = new IArgument[selectedItems.length];
		for (int i = 0; i < selectedItems.length; i++) {
			selectedArgs[i] = (IArgument) selectedItems[i].getData();
		}
		TableItem[] allItems = this.argViewer.getTable().getItems();
		IArgument[] allArgs = new IArgument[allItems.length];
		IArgument[] newArgs = new IArgument[allItems.length];
		for (int i = allArgs.length - 1; i > -1; i--) {
			newArgs[i] = (IArgument) allItems[i].getData();
			if (allItems[i].getData().equals(selectedArgs[0])
					&& i != allArgs.length - 1) {
				newArgs[i] = newArgs[i + 1];
				newArgs[i + 1] = (IArgument) allItems[i].getData();
			}
		}
		method.setArguments(Arrays.asList(newArgs));
		pageModified();
		refreshViewerForMethodLabel();
		updateForm();
	}

	private void editArgButtonPressed() {
		TableItem[] selectedItems = this.argViewer.getTable().getSelection();
		IArgument[] selectedArgs = new IArgument[selectedItems.length];

		for (int i = 0; i < selectedItems.length; i++) {
			selectedArgs[i] = (IArgument) selectedItems[i].getData();
		}

		FileEditorInput input = (FileEditorInput) master.getPage()
				.getEditorInput();
		IJavaElement elem = JavaCore.create(input.getFile());

		ArgumentEditDialog dialog = new ArgumentEditDialog(master.getSection()
				.getShell(), selectedArgs[0], getMethod().getArguments(), elem,
				master.getIArtifact().getTigerstripeProject(), displayDirection);

		if (dialog.open() == 0) {
			pageModified();
			refreshViewerForMethodLabel();
			updateForm();
		}
	}

	private void handleModifyText(ModifyEvent event) {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.

			if (event.getSource() == nameText) {
				getMethod().setName(nameText.getText().trim());
				refreshViewerForMethodLabel();
			} else if (event.getSource() == typeText) {
				IType type = getMethod().makeType();
				type.setFullyQualifiedName(typeText.getText().trim());
				type.setTypeMultiplicity(IModelComponent.EMultiplicity
						.at(multiplicityCombo.getSelectionIndex()));
				getMethod().setReturnType(type);
				updateDefaultValueCombo();
			} else if (event.getSource() == commentText) {
				getMethod().setComment(commentText.getText().trim());
			} else if (event.getSource() == defaultReturnValue) {
				if (defaultReturnValue.getText().trim().length() == 0) {
					getMethod().setDefaultReturnValue(null);
				} else {
					getMethod().setDefaultReturnValue(
							defaultReturnValue.getText().trim());
				}
			} else if (event.getSource() == methodReturnNameText) {
				getMethod()
						.setReturnName(methodReturnNameText.getText().trim());
			}
			pageModified();
			refreshViewerForMethodLabel();
		}
	}

	/*
	 * Refresh the viewer for the method label (which will update the method
	 * profile)
	 */
	private void refreshViewerForMethodLabel() {
		if (master != null) {
			TableViewer viewer = master.getViewer();
			viewer.refresh(getMethod());
		}
	}

	protected void pageModified() {
		master.markPageModified();
	}

	private void navigateToKeyPressed(KeyEvent e) {
		master.navigateToKeyPressed(e);
	}

	private void updateDefaultValueCombo() {
		// Update the default value control based on the field type
		if (getMethod().getReturnType() != null) {
			Type type = (Type) getMethod().getReturnType();
			IAbstractArtifact art = type.getArtifact();
			if (art instanceof IEnumArtifact) {
				IEnumArtifact enumArt = (IEnumArtifact) art;
				String[] items = new String[enumArt.getLiterals().size()];
				int i = 0;
				for (ILiteral literal : enumArt.getLiterals()) {
					items[i] = literal.getName();
					i++;
				}
				defaultReturnValue.setItems(items);
				defaultReturnValue.setEditable(false);
			} else if (type.getFullyQualifiedName().equals("boolean")) {
				defaultReturnValue
						.setItems(new String[] { "true", "false", "" });
				defaultReturnValue.setEditable(false);
				defaultReturnValue.select(2);
			} else {
				defaultReturnValue.setItems(new String[0]);
				defaultReturnValue.setEditable(true);
			}
		}
	}

	public Table getAnnTable() {
		return annTable;
	}

	public IManagedForm getForm() {
		return form;
	}

}
