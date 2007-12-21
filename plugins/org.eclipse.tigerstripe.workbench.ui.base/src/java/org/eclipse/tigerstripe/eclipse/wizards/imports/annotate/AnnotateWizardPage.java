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
package org.eclipse.tigerstripe.eclipse.wizards.imports.annotate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.importing.AnnotableAssociation;
import org.eclipse.tigerstripe.core.model.importing.AnnotableDatatype;
import org.eclipse.tigerstripe.core.model.importing.AnnotableDependency;
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
import org.eclipse.tigerstripe.eclipse.wizards.imports.ImportWithCheckpointWizardPage;
import org.eclipse.tigerstripe.eclipse.wizards.imports.annotate.RawClassesTreeContentProvider.AssociationNode;
import org.eclipse.tigerstripe.eclipse.wizards.imports.annotate.RawClassesTreeContentProvider.ClassNode;
import org.eclipse.tigerstripe.eclipse.wizards.imports.annotate.RawClassesTreeContentProvider.DependencyNode;
import org.eclipse.tigerstripe.eclipse.wizards.imports.annotate.RawClassesTreeContentProvider.Node;
import org.eclipse.tigerstripe.eclipse.wizards.imports.xmi.AnnotatedAssociation;
import org.eclipse.tigerstripe.eclipse.wizards.imports.xmi.AnnotatedDependency;
import org.eclipse.tigerstripe.eclipse.wizards.imports.xmi.AnnotatedElement;
import org.eclipse.tigerstripe.eclipse.wizards.model.ArtifactAttributeModel;
import org.eclipse.tigerstripe.eclipse.wizards.model.ArtifactMethodModel;
import org.eclipse.tigerstripe.eclipse.wizards.model.ArtifactMethodModel.ParameterRef;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AnnotateWizardPage extends WizardPage {

	private FormSelectionListener listener = new FormSelectionListener();

	private static final String PAGE_NAME = "AnnotateWizardPage"; //$NON-NLS-1$

	// This viewer displays all imported classes/tables in a package
	// tree form.
	private TreeViewer rawClassesTreeViewer;

	private RawClassesTreeContentProvider contentProvider;

	private RawClassesTreeLabelProvider labelProvider;

	private Menu fContextMenu;

	private Button showDeltaOnlyButton;

	private Button selectAllUnAnnotated;

	private List<AnnotatedElement> annotatedAsEntity = null;

	private List<AnnotatedElement> annotatedAsDatatype = null;

	private List<AnnotatedElement> annotatedAsEnumeration = null;

	private List<AnnotatedElement> annotatedAsException = null;

	private List<AnnotatedElement> annotatedAsNamedQuery = null;

	private List<AnnotatedElement> annotatedAsNotification = null;

	private List<AnnotatedElement> annotatedAsUpdateProc = null;

	private List<AnnotatedElement> annotatedAsSessionFacade = null;

	private List<AnnotatedElement> annotatedAsAssociation = null;

	private List<AnnotatedElement> annotatedAsAssociationClass = null;

	private List<AnnotatedElement> annotatedAsDependency = null;

	private class FormSelectionListener implements SelectionListener {
		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			formWidgetSelected(e);
		}
	}

	/**
	 * Creates a new <code>NewPackageWizardPage</code>
	 */
	public AnnotateWizardPage() {
		super(PAGE_NAME);

		setTitle("Model annotation");
		setDescription("Please annotate the imported model.");

	}

	// -------- UI Creation ---------

	/*
	 * @see WizardPage#createControl
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		int nColumns = 1;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createRawClassTreeControls(composite);
		createSelectAllUnannotated(composite);
		createDeltaOnlyControls(composite);

		setControl(composite);

		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
	}

	protected void createRawClassTreeControls(Composite composite) {

		rawClassesTreeViewer = new TreeViewer(composite, SWT.MULTI | SWT.BORDER);
		GridData gd = new GridData(GridData.CENTER | GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.heightHint = 50;
		rawClassesTreeViewer.getTree().setLayoutData(gd);
		contentProvider = new RawClassesTreeContentProvider();
		rawClassesTreeViewer.setContentProvider(contentProvider);
		labelProvider = new RawClassesTreeLabelProvider();
		rawClassesTreeViewer.setLabelProvider(labelProvider);

		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		fContextMenu = menuMgr
				.createContextMenu(rawClassesTreeViewer.getTree());
		menuMgr
				.addMenuListener(new RawClassesMenuListener(
						rawClassesTreeViewer));
		rawClassesTreeViewer.getTree().setMenu(fContextMenu);
	}

	protected void createSelectAllUnannotated(Composite composite) {
		Label empty = new Label(composite, SWT.NULL);

		selectAllUnAnnotated = new Button(composite, SWT.PUSH);
		selectAllUnAnnotated.setText("Select Un-Annotated");
		selectAllUnAnnotated.addSelectionListener(listener);
	}

	protected void createDeltaOnlyControls(Composite composite) {

		Label empty = new Label(composite, SWT.NULL);

		showDeltaOnlyButton = new Button(composite, SWT.CHECK);
		showDeltaOnlyButton.setText("Show delta only");
		showDeltaOnlyButton.addSelectionListener(listener);
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			showDeltaOnlyButton.setEnabled(getReferenceProject() != null);
			rawClassesTreeViewer.setInput(getRawClasses());
		}
	}

	/**
	 * Sets the focus to the package name input field.
	 */
	protected void setFocus() {
	}

	protected ITigerstripeProject getTargetProject()
			throws TigerstripeException {
		return ((IImportFromWizardPage) getPreviousPage()).getTSProject();
	}

	public ITigerstripeProject getReferenceProject() {
		IWizardPage[] pages = getWizard().getPages();
		ImportWithCheckpointWizardPage initialPage = (ImportWithCheckpointWizardPage) pages[0];
		try {
			return initialPage.getReferenceTSProject();
		} catch (TigerstripeException e) {
			return null;
		}
	}

	public AnnotableElement[] getRawClasses() {
		IImportFromWizardPage firstPage = (IImportFromWizardPage) getWizard()
				.getPreviousPage(this);

		AnnotableModel model = firstPage.getModelImportResult().getModel();
		Collection res = model.getAnnotableElements();
		return (AnnotableElement[]) res
				.toArray(new AnnotableElement[res.size()]);
	}

	protected void treeSelectionChanged(SelectionChangedEvent event) {
		if (event.getSource() == rawClassesTreeViewer) {
			AnnotableElement selectedElement = updateAnnotable(event);
		}
	}

	private AnnotableElement updateAnnotable(SelectionChangedEvent event) {
		IStructuredSelection ssel = (IStructuredSelection) event.getSelection();
		Node selectedNode = (Node) ssel.getFirstElement();

		if (selectedNode instanceof ClassNode) {
			ClassNode classNode = (ClassNode) selectedNode;
			return classNode.getAnnotableElement();
		}

		return null;
	}

	private AnnotatedElement mapToAnnotatedElement(AnnotableElement srcElem)
			throws TigerstripeException {

		UmlDatatypeMapper mapper = new UmlDatatypeMapper(getTargetProject());

		String srcElemPackageName = "";
		if (srcElem.getAnnotableElementPackage() != null) {
			srcElemPackageName = srcElem.getAnnotableElementPackage()
					.getFullyQualifiedName();
		}

		AnnotatedElement elem = null;

		if (srcElem instanceof AnnotableAssociation) {
			elem = new AnnotatedAssociation(srcElemPackageName, srcElem
					.getName());
			((AnnotatedAssociation) elem)
					.setAEndDetails(((AnnotableAssociation) srcElem).getAEnd());
			((AnnotatedAssociation) elem)
					.setZEndDetails(((AnnotableAssociation) srcElem).getZEnd());
		} else if (srcElem instanceof AnnotableDependency) {
			elem = new AnnotatedDependency(srcElemPackageName, srcElem
					.getName());

			((AnnotatedDependency) elem)
					.setAEndType(((AnnotableDependency) srcElem).getAEnd()
							.getFullyQualifiedName());
			((AnnotatedDependency) elem)
					.setZEndType(((AnnotableDependency) srcElem).getZEnd()
							.getFullyQualifiedName());
		} else {
			elem = new AnnotatedElement(srcElemPackageName, srcElem.getName());
		}

		elem.setType(srcElem.getAnnotationType());
		elem.setDescription(srcElem.getDescription());
		elem.setAbstract(srcElem.isAbstract());

		// Set the extended artifact
		if (srcElem.getParentAnnotableElement() == null) {
			elem.setExtendedArtifact("java.lang.Object");
		} else {
			elem.setExtendedArtifact(srcElem.getParentAnnotableElement()
					.getFullyQualifiedName());
		}

		// Map all attributes
		for (Iterator iterAttr = srcElem.getAnnotableElementAttributes()
				.iterator(); iterAttr.hasNext();) {
			AnnotableElementAttribute attribute = (AnnotableElementAttribute) iterAttr
					.next();
			if (!attribute.shouldIgnore()) {
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
		}

		// Map all Labels
		int counter = 0;
		for (Iterator iterConstant = srcElem.getAnnotableElementConstants()
				.iterator(); iterConstant.hasNext();) {
			AnnotableElementConstant constant = (AnnotableElementConstant) iterConstant
					.next();
			if (!constant.shouldIgnore()) {
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
		}

		// Map all Operations
		for (Iterator iterOp = srcElem.getAnnotableElementOperations()
				.iterator(); iterOp.hasNext();) {
			AnnotableElementOperation op = (AnnotableElementOperation) iterOp
					.next();

			// TODO add Operation mapping here
			ArtifactMethodModel mModel = new ArtifactMethodModel();
			if (!op.shouldIgnore()) {
				mModel.setMethodName(op.getName());
				mModel.setDescription(op.getDescription());
				mModel.setAbstract(op.isAbstract());

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

		return elem;
	}

	private void buildLists() {

		annotatedAsEntity = new ArrayList<AnnotatedElement>();
		annotatedAsDatatype = new ArrayList<AnnotatedElement>();
		annotatedAsEnumeration = new ArrayList<AnnotatedElement>();
		annotatedAsException = new ArrayList<AnnotatedElement>();
		annotatedAsNamedQuery = new ArrayList<AnnotatedElement>();
		annotatedAsNotification = new ArrayList<AnnotatedElement>();
		annotatedAsUpdateProc = new ArrayList<AnnotatedElement>();
		annotatedAsSessionFacade = new ArrayList<AnnotatedElement>();

		annotatedAsAssociation = new ArrayList<AnnotatedElement>();
		annotatedAsAssociationClass = new ArrayList<AnnotatedElement>();
		annotatedAsDependency = new ArrayList<AnnotatedElement>();

		for (AnnotableElement elm : getRawClasses()) {
			if (AnnotableElement.AS_ENTITY.equals(elm.getAnnotationType())) {
				try {
					annotatedAsEntity.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (AnnotableElement.AS_DATATYPE.equals(elm
					.getAnnotationType())) {
				try {
					annotatedAsDatatype.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (AnnotableElement.AS_ENUMERATION.equals(elm
					.getAnnotationType())) {
				try {
					annotatedAsEnumeration.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (AnnotableElement.AS_EXCEPTION.equals(elm
					.getAnnotationType())) {
				try {
					annotatedAsException.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (AnnotableElement.AS_NOTIFICATION.equals(elm
					.getAnnotationType())) {
				try {
					annotatedAsNotification.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (AnnotableElement.AS_NAMEDQUERY.equals(elm
					.getAnnotationType())) {
				try {
					annotatedAsNamedQuery.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (AnnotableElement.AS_UPDATEPROC.equals(elm
					.getAnnotationType())) {
				try {
					annotatedAsUpdateProc.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (AnnotableElement.AS_SESSIONFACADE.equals(elm
					.getAnnotationType())) {
				try {
					annotatedAsSessionFacade.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (AnnotableElement.AS_ASSOCIATION.equals(elm
					.getAnnotationType())) {
				try {
					annotatedAsAssociation.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (AnnotableElement.AS_ASSOCIATIONCLASS.equals(elm
					.getAnnotationType())) {
				try {
					annotatedAsAssociationClass.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			} else if (AnnotableElement.AS_DEPENDENCY.equals(elm
					.getAnnotationType())) {
				try {
					annotatedAsDependency.add(mapToAnnotatedElement(elm));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	public List<AnnotatedElement> getDatatypes() {
		if (annotatedAsDatatype == null) {
			buildLists();
		}
		return annotatedAsDatatype;
	}

	public List<AnnotatedElement> getEnumerations() {
		if (annotatedAsEnumeration == null) {
			buildLists();
		}
		return annotatedAsEnumeration;
	}

	public List<AnnotatedElement> getNamedQueries() {
		if (annotatedAsNamedQuery == null) {
			buildLists();
		}
		return annotatedAsNamedQuery;
	}

	public List<AnnotatedElement> getNotifications() {
		if (annotatedAsNotification == null) {
			buildLists();
		}
		return annotatedAsNotification;
	}

	public List<AnnotatedElement> getExceptions() {
		if (annotatedAsException == null) {
			buildLists();
		}
		return annotatedAsException;
	}

	public List<AnnotatedElement> getUpdateProcedures() {
		if (annotatedAsUpdateProc == null) {
			buildLists();
		}
		return annotatedAsUpdateProc;
	}

	public List<AnnotatedElement> getSessionFacades() {
		if (annotatedAsSessionFacade == null) {
			buildLists();
		}
		return annotatedAsSessionFacade;
	}

	public List<AnnotatedElement> getAssociations() {
		if (annotatedAsAssociation == null) {
			buildLists();
		}
		return annotatedAsAssociation;
	}

	public List<AnnotatedElement> getDependencies() {
		if (annotatedAsDependency == null) {
			buildLists();
		}
		return annotatedAsDependency;
	}

	public List<AnnotatedElement> getAssociationClasses() {
		if (annotatedAsAssociationClass == null) {
			buildLists();
		}
		return annotatedAsAssociationClass;
	}

	public List<AnnotatedElement> getEntities() {
		if (annotatedAsEntity == null) {
			buildLists();
		}
		return annotatedAsEntity;
	}

	protected void formWidgetSelected(SelectionEvent e) {
		if (e.getSource() == showDeltaOnlyButton) {
			contentProvider.setDeltaOnly(showDeltaOnlyButton.getSelection());
			labelProvider.setDelta(showDeltaOnlyButton.getSelection());
			rawClassesTreeViewer.refresh(true);
		} else if (e.getSource() == selectAllUnAnnotated) {
			selectAllUnAnnotated();
		}
	}

	private void selectAllUnAnnotated() {

		List unAnnotatedList = new ArrayList();
		Object[] objects = contentProvider.getElements(null);
		for (Object obj : objects) {
			AnnotableElement elm = null;
			if (obj instanceof ClassNode) {
				ClassNode n = (ClassNode) obj;
				elm = n.getAnnotableElement();
			} else if (obj instanceof AssociationNode) {
				AssociationNode n = (AssociationNode) obj;
				elm = n.getAnnotableElement();
			} else if (obj instanceof DependencyNode) {
				DependencyNode n = (DependencyNode) obj;
				elm = n.getAnnotableElement();
			}

			if (elm.getAnnotationType() == null
					|| AnnotableElement.AS_UNKNOWN.equals(elm
							.getAnnotationType())) {
				unAnnotatedList.add(obj);
			}
		}
		StructuredSelection sel = new StructuredSelection(unAnnotatedList);
		rawClassesTreeViewer.setSelection(sel);
		// rawClassesTreeViewer.refresh();
	}

}