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
package org.eclipse.tigerstripe.eclipse.wizards.imports.xmi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.importing.AnnotableDatatype;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementAttribute;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementConstant;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementOperation;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElementOperationParameter;
import org.eclipse.tigerstripe.core.model.importing.AnnotableModel;
import org.eclipse.tigerstripe.core.model.importing.mapper.UmlDatatypeMapper;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.enums.LabelRef;
import org.eclipse.tigerstripe.eclipse.wizards.imports.IImportFromWizardPage;
import org.eclipse.tigerstripe.eclipse.wizards.model.ArtifactAttributeModel;
import org.eclipse.tigerstripe.eclipse.wizards.model.ArtifactMethodModel;
import org.eclipse.tigerstripe.eclipse.wizards.model.ArtifactMethodModel.ParameterRef;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AnnotateFromXMIWizardPage extends WizardPage {

	private static final String PAGE_NAME = "AnnotateFromXMIWizardPage"; //$NON-NLS-1$

	private class AnnotationFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
			entityPageCustomButtonPressed(field, index);
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	private ListDialogField entitiesDialogField;

	private ListDialogField datatypesDialogField;

	private ListDialogField enumerationsDialogField;

	private final static int ADD_BUTTON_IDX = 0;

	private final static int REMOVE_BUTTON_IDX = 1;

	/**
	 * Creates a new <code>NewPackageWizardPage</code>
	 */
	public AnnotateFromXMIWizardPage() {
		super(PAGE_NAME);

		setTitle("Model annotation");
		setDescription("Please annotate the imported classes.");

		AnnotationFieldsAdapter adapter = new AnnotationFieldsAdapter();

		String[] addButtons = new String[] { /* 0 */"Add", //$NON-NLS-1$
				/* 1 */"Remove" //$NON-NLS-1$
		};

		entitiesDialogField = new ListDialogField(adapter, addButtons,
				new AnnotableLabelProvider());
		entitiesDialogField.setDialogFieldListener(adapter);
		entitiesDialogField.setLabelText("Managed Entities");
		entitiesDialogField.setRemoveButtonIndex(REMOVE_BUTTON_IDX);

		datatypesDialogField = new ListDialogField(adapter, addButtons,
				new AnnotableLabelProvider());
		datatypesDialogField.setDialogFieldListener(adapter);
		datatypesDialogField.setLabelText("Datatypes");
		datatypesDialogField.setRemoveButtonIndex(REMOVE_BUTTON_IDX);

		// String[] enumButtons = new String[] { /* 0 */"Remove" //$NON-NLS-1$
		// };
		enumerationsDialogField = new ListDialogField(adapter, addButtons,
				new AnnotableLabelProvider());
		enumerationsDialogField.setDialogFieldListener(adapter);
		enumerationsDialogField.setLabelText("Enumerations");
		enumerationsDialogField.setRemoveButtonIndex(REMOVE_BUTTON_IDX);
	}

	// -------- UI Creation ---------

	/*
	 * @see WizardPage#createControl
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createEntitiesListControls(composite, nColumns);
		createDatatypesListControls(composite, nColumns);
		createEnumerationsListControls(composite, nColumns);

		setControl(composite);

		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		// pre-populate the list of classes from the XMI
		// TODO add error handling here on the file
		getAvailableClasses();

		initFieldsWithPreMappings();
	}

	/**
	 * Sets the focus to the package name input field.
	 */
	protected void setFocus() {
	}

	/**
	 * Creates a separator line. Expects a <code>GridLayout</code> with at
	 * least 1 column.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(
				composite, nColumns, convertHeightInCharsToPixels(1));
	}

	private void createEntitiesListControls(Composite composite, int nColumns) {
		entitiesDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd = (GridData) entitiesDialogField.getListControl(null)
				.getLayoutData();
		gd.heightHint = convertHeightInCharsToPixels(3);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
	}

	private void createDatatypesListControls(Composite composite, int nColumns) {
		datatypesDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd = (GridData) datatypesDialogField.getListControl(null)
				.getLayoutData();
		gd.heightHint = convertHeightInCharsToPixels(3);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
	}

	private void createEnumerationsListControls(Composite composite,
			int nColumns) {
		enumerationsDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd = (GridData) enumerationsDialogField.getListControl(null)
				.getLayoutData();
		gd.heightHint = convertHeightInCharsToPixels(3);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
	}

	/**
	 * Transforms the AnnotableElements to the corresponding models so the POJOs
	 * can be generated.
	 * 
	 * @return a list of chosen super interfaces. The list's elements are of
	 *         type <code>String</code>
	 */
	public List getEntities() {
		try {
			return mapToAnnotatedElements(entitiesDialogField.getElements());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			return new ArrayList();
		}
	}

	protected ITigerstripeProject getTargetProject()
			throws TigerstripeException {
		return ((IImportFromWizardPage) getPreviousPage()).getTSProject();
	}

	private List mapToAnnotatedElements(List annotables)
			throws TigerstripeException {
		final List annotated = new ArrayList();

		UmlDatatypeMapper mapper = new UmlDatatypeMapper(getTargetProject());

		for (Iterator iter = annotables.iterator(); iter.hasNext();) {
			AnnotableElement srcElem = (AnnotableElement) iter.next();

			String srcElemPackageName = "";
			if (srcElem.getAnnotableElementPackage() != null) {
				srcElemPackageName = srcElem.getAnnotableElementPackage()
						.getFullyQualifiedName();
			}
			AnnotatedElement elem = new AnnotatedElement(srcElemPackageName,
					srcElem.getName());
			elem.setType(srcElem.getAnnotationType());
			elem.setDescription(srcElem.getDescription());

			// Set the extended artifact
			if (srcElem.getParentAnnotableElement() == null) {
				elem.setExtendedArtifact("java.lang.Object");
			} else {
				elem.setExtendedArtifact(srcElem.getParentAnnotableElement()
						.getFullyQualifiedName());
			}
			annotated.add(elem);

			// Map all attributes
			for (Iterator iterAttr = srcElem.getAnnotableElementAttributes()
					.iterator(); iterAttr.hasNext();) {
				AnnotableElementAttribute attribute = (AnnotableElementAttribute) iterAttr
						.next();
				ArtifactAttributeModel ref = new ArtifactAttributeModel();
				ref.setName(attribute.getName());

				// Set the type
				String attributeTypeStr = "Object";
				AnnotableDatatype attributeType = attribute.getType();
				if (attributeType != null) {
					attributeTypeStr = attributeType.getFullyQualifiedName();
					if (attributeType instanceof AnnotableElement) {
						// Set ref by key by default for now
						// TODO change this to not by default
						AnnotableElement aElem = (AnnotableElement) attributeType;
						if (AnnotableElement.AS_ENTITY.equals(aElem
								.getAnnotationType())) {
							ref.setRefBy(ArtifactAttributeModel.REFBY_KEY);
						}
					}
				}
				ref.setAttributeClass(attributeTypeStr);
				ref.setDimensions(attribute.getDimensions());
				ref.setDescription(attribute.getDescription());
				elem.addAttributeRef(ref);
			}

			// Map all Labels
			int counter = 0;
			for (Iterator iterConstant = srcElem.getAnnotableElementConstants()
					.iterator(); iterConstant.hasNext();) {
				AnnotableElementConstant constant = (AnnotableElementConstant) iterConstant
						.next();
				LabelRef labelRef = new LabelRef();
				labelRef.setName(constant.getName());

				if (constant.getValue() == null) {
					labelRef.setValue("\"Default" + counter++ + "\"");
				} else {
					labelRef.setValue(constant.getValue());
				}

				if (constant.getType() == null) {
					labelRef.setLabelClass("String");
				} else {
					labelRef.setLabelClass(mapper
							.mapUmlDatatypeToJavaDatatype(constant.getType()
									.getFullyQualifiedName()));
				}
				labelRef.setDescription(constant.getDescription());
				elem.addLabelRef(labelRef);
			}

			// Map all Operations
			for (Iterator iterOp = srcElem.getAnnotableElementOperations()
					.iterator(); iterOp.hasNext();) {
				AnnotableElementOperation op = (AnnotableElementOperation) iterOp
						.next();

				// TODO add Operation mapping here
				ArtifactMethodModel mModel = new ArtifactMethodModel();
				mModel.setMethodName(op.getName());
				mModel.setDescription(op.getDescription());

				if (op.getReturnType() != null) {
					mModel.setReturnClass(mapper
							.mapUmlDatatypeToJavaDatatype(op.getReturnType()
									.getFullyQualifiedName()));
				} else {
					mModel.setVoid(true);
				}

				List paramList = new ArrayList();
				for (Iterator iterParam = op
						.getAnnotableElementOperationParameters().iterator(); iterParam
						.hasNext();) {
					AnnotableElementOperationParameter param = (AnnotableElementOperationParameter) iterParam
							.next();
					ParameterRef paramRef = mModel.new ParameterRef();
					paramRef.setName(param.getName());

					if (param.getType() != null) {
						paramRef.setParameterClass(mapper
								.mapUmlDatatypeToJavaDatatype(param.getType()
										.getFullyQualifiedName()));
					} else {
						paramRef.setParameterClass("java.lang.Object");
					}
					paramList.add(paramRef);
				}
				mModel.setParameterList(paramList);
				elem.addMethodModel(mModel);

			}
		}

		return annotated;
	}

	public List getDatatype() {
		try {
			return mapToAnnotatedElements(datatypesDialogField.getElements());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			return new ArrayList();
		}
	}

	public List getEnumerations() {
		try {
			return mapToAnnotatedElements(enumerationsDialogField.getElements());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			return new ArrayList();
		}
	}

	/**
	 * Sets the super interfaces.
	 * 
	 * @param interfacesNames
	 *            a list of super interface. The method requires that the list's
	 *            elements are of type <code>String</code>
	 * @param canBeModified
	 *            if <code>true</code> the super interface field is editable;
	 *            otherwise it is read-only.
	 */
	public void setEntities(List entities, boolean canBeModified) {
		entitiesDialogField.setElements(entities);
		entitiesDialogField.setEnabled(canBeModified);
	}

	private void initFieldsWithPreMappings() {
		IImportFromWizardPage firstPage = (IImportFromWizardPage) getWizard()
				.getPreviousPage(this);
		AnnotableModel model = firstPage.getModelImportResult().getModel();
		Collection<AnnotableElement> elements = model.getAnnotableElements();
		for (AnnotableElement elm : elements) {
			if (AnnotableElement.AS_ENTITY.equals(elm.getAnnotationType())) {
				entitiesDialogField.addElement(elm);
			} else if (AnnotableElement.AS_DATATYPE.equals(elm
					.getAnnotationType())) {
				datatypesDialogField.addElement(elm);
			}
		}

		enumerationsDialogField.setElements(Arrays
				.asList(getAvailableEnumerations()));
	}

	protected void entityPageCustomButtonPressed(ListDialogField field,
			int index) {
		if (index == ADD_BUTTON_IDX) {
			// Add entities to the selection
			AnnotableElement[] selectedElements = browseAvailableClasses(field);
			ArrayList list = new ArrayList();

			for (int i = 0; i < selectedElements.length; i++) {
				list.add(selectedElements[i]);
			}

			field.addElements(list);
		}
	}

	protected void openPropertiesForEntity(ListDialogField field) {
		// List selectedElements = field.getSelectedElements();
		// Object[] entityOptions = selectedElements.toArray();
		//
		// EntityOption target = (EntityOption) ((EntityOption)
		// entityOptions[0])
		// .clone();
		// EntityOptionsCompleteDialog dialog = new EntityOptionsCompleteDialog(
		// getShell(), target);
		// if (dialog.open() == InputDialog.OK) {
		// ((EntityOption) entityOptions[0]).setProperties(target
		// .getProperties());
		// }
		//
	}

	private AnnotableElement[] browseAvailableClasses(ListDialogField field) {

		String targetAnnotation = AnnotableElement.AS_UNKNOWN;
		String message = "Select a set of Classes to annotate as ";

		if (field == entitiesDialogField) {
			targetAnnotation = AnnotableElement.AS_ENTITY;
			message = "Entity Artifacts.";
		} else if (field == datatypesDialogField) {
			targetAnnotation = AnnotableElement.AS_DATATYPE;
			message = "Datatype Artifacts.";
		} else if (field == enumerationsDialogField) {
			targetAnnotation = AnnotableElement.AS_ENUMERATION;
			message = "Enumeration Artifacts.";
		}

		// ElementListSelectionDialog elsd = new ElementListSelectionDialog(
		AnnotableElementSelectorDialog elsd = new AnnotableElementSelectorDialog(
				getShell(), new AnnotableLabelProvider());

		elsd.setTitle("Imported Classes");
		elsd.setMessage(message);

		Collection availableClasses = getAvailableClasses();

		List limitedList = new ArrayList();
		for (Iterator iter = availableClasses.iterator(); iter.hasNext();) {
			AnnotableElement elem = (AnnotableElement) iter.next();

			if (!entitiesDialogField.getElements().contains(elem)
					&& !datatypesDialogField.getElements().contains(elem)
					&& !enumerationsDialogField.getElements().contains(elem)) {
				limitedList.add(elem);
			}
		}

		elsd.setElements(limitedList.toArray());

		if (elsd.open() == Window.OK) {

			Object[] objects = elsd.getResult();
			if (objects != null && objects.length != 0) {
				AnnotableElement[] result = new AnnotableElement[objects.length];
				for (int i = 0; i < objects.length; i++) {
					AnnotableElement elem = (AnnotableElement) objects[i];
					elem.setAnnotationType(targetAnnotation);
					result[i] = elem;
				}

				return result;
			}
		}
		return new AnnotableElement[0];
	}

	private Collection getAvailableClasses() {
		IImportFromWizardPage firstPage = (IImportFromWizardPage) getWizard()
				.getPreviousPage(this);
		AnnotableModel model = firstPage.getModelImportResult().getModel();
		return model.getAnnotableElements();
	}

	private AnnotableElement[] getAvailableEnumerations() {
		IImportFromWizardPage firstPage = (IImportFromWizardPage) getWizard()
				.getPreviousPage(this);
		AnnotableModel model = firstPage.getModelImportResult().getModel();
		Collection result = model.getAnnotableEnumerations();
		AnnotableElement[] resultArray = new AnnotableElement[result.size()];
		return (AnnotableElement[]) result.toArray(resultArray);
	}

}