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
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.EntityMethodFlavorDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.EntityOveride;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjMethod;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class EntityMethodFlavorSection extends ArtifactSectionPart {

	private class ExceptionContentProvider extends ArrayContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement == null)
				return new Object[0];
			EntityMethodFlavorDetails details = (EntityMethodFlavorDetails) inputElement;
			return details.getExceptions().toArray();
		}
	}

	private class NonRemoveableExceptionContentProvider extends
			ArrayContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement == null)
				return new Object[0];
			List<String> list = (List<String>) inputElement;
			return list.toArray();
		}
	}

	private class ExceptionLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			return Images.get(Images.EXCEPTION_ICON);
		}
	}

	private class MethodContentProvider extends ArrayContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			IAbstractArtifact artifact = (IAbstractArtifact) inputElement;

			IMethod[] crudMethods = EntityOveride
					.buildVirtualCrudMethods(artifact);

			ArrayList list = new ArrayList();
			list.addAll(Arrays.asList(crudMethods));

			// Make sure we only add the non instance methods
			for (IMethod customMethod : artifact.getMethods()) {
				if (customMethod.isInstanceMethod()) {
					list.add(customMethod);
				}
			}
			return list.toArray();
		}
	}

	private class MethodLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			IMethod method = (IMethod) element;
			return method.getName();
		}

	}

	private class FlavorsContentProvider extends ArrayContentProvider {

		private IOssjMethod methodInFocus;

		@Override
		public Object[] getElements(Object inputElement) {

			if (methodInFocus == null)
				return new String[0];

			OssjEntityMethodFlavor[] flavors = methodInFocus
					.getSupportedFlavors();
			return flavors;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			methodInFocus = (IOssjMethod) newInput;
			// viewer.refresh();
		}
	}

	private class FlavorLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			OssjEntityMethodFlavor flavor = (OssjEntityMethodFlavor) element;
			EntityMethodFlavorDetails details = new EntityMethodFlavorDetails(
					((Method) currentMethod).getContainingArtifact(),
					((Method) currentMethod).getOssjMethodProperties()
							.getProperty(flavor.getPojoLabel()));
			return flavor.getPojoLabel() + " (" + details.getFlag() + ")";
		}

		// public Image getImage( Object element ) {
		// OssjEntityMethodFlavor flavor = (OssjEntityMethodFlavor) element;
		//			
		// EntityMethodFlavorDetails details = new EntityMethodFlavorDetails(
		// currentMethod.getOssjMethodProperties().getProperty(
		// flavor.getPojoLabel()));
		//
		//			
		// return null;
		// }
	}

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class EntityMethodFlavorListener implements SelectionListener,
			ModifyListener {

		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}
	}

	private boolean silentUpdate = false;

	private Text descriptionText;

	public EntityMethodFlavorSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider, 0);

		setTitle("Entity Method Variations");
		createContent();
	}

	@Override
	protected void createContent() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		getSection().setLayout(layout);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);
		getSection().clientVerticalSpacing = 5;

		Composite body = getBody();
		body.setLayout(layout);

		createMethodSelector(getBody(), getToolkit());
		createFlavorSelector(getBody(), getToolkit());
		createFlavorDetails(getBody(), getToolkit());

		// createDescription(getBody(), getToolkit());

		updateForm();
		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private TableViewer methodViewer;

	private TableViewer flavorViewer;

	private TableViewer exceptionViewer;

	private TableViewer nonRemovableExceptionViewer;

	private Button optionalButton;

	private Button trueButton;

	private Button falseButton;

	private Button exceptionRemoveButton;

	private Button exceptionAddButton;

	private FlavorsContentProvider flavorsContentProvider;

	private EntityMethodFlavorDetails currentFlavorDetails;

	private IMethod currentMethod;

	private OssjEntityMethodFlavor selectedFlavor;

	private void createMethodSelector(Composite parent, FormToolkit toolkit) {
		Composite methodComposite = toolkit.createComposite(parent);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 1;
		gd.heightHint = 50;
		gd.widthHint = 10;
		gd.minimumWidth = 10;
		gd.minimumHeight = 30;
		methodComposite.setLayoutData(gd);
		GridLayout methodLayout = new GridLayout();
		methodComposite.setLayout(methodLayout);

		Label label = toolkit.createLabel(methodComposite, "Entity Method");
		label.setEnabled(!isReadonly());
		Table table = toolkit.createTable(methodComposite, SWT.FLAT
				| SWT.BORDER);
		table.setEnabled(!isReadonly());
		GridData tgd = new GridData(GridData.FILL_BOTH);
		tgd.heightHint = 50;
		table.setLayoutData(tgd);

		methodViewer = new TableViewer(table);
		methodViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						methodSelectionChanged(event);
					}
				});

		methodViewer.setContentProvider(new MethodContentProvider());
		methodViewer.setLabelProvider(new MethodLabelProvider());
		// methodViewer.setInput(((ArtifactEditorBase) getPage().getEditor())
		// .getIArtifact());

	}

	private void createFlavorSelector(Composite parent, FormToolkit toolkit) {
		Composite flavorComposite = toolkit.createComposite(parent);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 1;
		gd.heightHint = 50;
		gd.widthHint = 5;
		gd.minimumWidth = 5;
		gd.minimumHeight = 30;
		flavorComposite.setLayoutData(gd);
		GridLayout methodLayout = new GridLayout();
		flavorComposite.setLayout(methodLayout);

		Label label = toolkit.createLabel(flavorComposite, "Method Variation");
		label.setEnabled(!isReadonly());
		Table table = toolkit.createTable(flavorComposite, SWT.FLAT
				| SWT.BORDER);
		table.setEnabled(!isReadonly());
		GridData tgd = new GridData(GridData.FILL_BOTH);
		tgd.heightHint = 50;
		table.setLayoutData(tgd);

		flavorViewer = new TableViewer(table);
		flavorViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						flavorSelectionChanged(event);
					}
				});

		flavorsContentProvider = new FlavorsContentProvider();
		flavorViewer.setContentProvider(flavorsContentProvider);
		flavorViewer.setLabelProvider(new FlavorLabelProvider());

	}

	private void createFlavorDetails(Composite parent, FormToolkit toolkit) {
		Composite detailsComposite = toolkit.createComposite(parent);
		GridLayout dcl = new GridLayout();
		dcl.numColumns = 1;
		detailsComposite.setLayout(dcl);

		GridData dcgd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL);
		dcgd.verticalSpan = 6;
		detailsComposite.setLayoutData(dcgd);

		EntityMethodFlavorListener adapter = new EntityMethodFlavorListener();

		Label label = toolkit.createLabel(detailsComposite, "Flavor Details");
		label.setEnabled(!isReadonly());
		// The true/optional/false radio buttons
		Composite visiComposite = toolkit.createComposite(detailsComposite);
		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		visiComposite.setLayout(gLayout);
		visiComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		trueButton = toolkit.createButton(visiComposite, "True", SWT.RADIO);
		trueButton.setEnabled(!isReadonly());
		trueButton.addSelectionListener(adapter);
		optionalButton = toolkit.createButton(visiComposite, "Optional",
				SWT.RADIO);
		optionalButton.setEnabled(!isReadonly());
		optionalButton.addSelectionListener(adapter);
		falseButton = toolkit.createButton(visiComposite, "False", SWT.RADIO);
		falseButton.setEnabled(!isReadonly());
		falseButton.addSelectionListener(adapter);

		// Specific comment
		label = toolkit.createLabel(detailsComposite, "Description");
		label.setEnabled(!isReadonly());
		descriptionText = toolkit.createText(detailsComposite, "", SWT.WRAP
				| SWT.MULTI | SWT.V_SCROLL);
		descriptionText.setEnabled(!isReadonly());
		GridData dGd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		dGd.heightHint = 50;
		descriptionText.setLayoutData(dGd);
		descriptionText.addModifyListener(adapter);

		// the Exception list
		Composite excComposite = toolkit.createComposite(detailsComposite);
		gLayout = new GridLayout();
		gLayout.numColumns = 2;
		excComposite.setLayout(gLayout);
		excComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

		Label manLabel = toolkit
				.createLabel(excComposite, "Custom Exceptions:");
		manLabel.setEnabled(!isReadonly());
		GridData mdg = new GridData();
		mdg.horizontalSpan = 2;
		manLabel.setLayoutData(mdg);
		Table exceptionTable = toolkit.createTable(excComposite, SWT.BORDER);
		exceptionTable.setEnabled(!isReadonly());
		GridData tgd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		tgd.verticalSpan = 3;
		tgd.heightHint = 60;
		tgd.minimumWidth = 15;
		tgd.widthHint = 100;
		exceptionTable.setLayoutData(tgd);

		exceptionViewer = new TableViewer(exceptionTable);
		exceptionViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						exceptionSelectionChanged(event);
					}
				});

		exceptionViewer.setContentProvider(new ExceptionContentProvider());
		exceptionViewer.setLabelProvider(new ExceptionLabelProvider());

		exceptionAddButton = new Button(excComposite, SWT.PUSH);
		exceptionAddButton.setText("Add");
		exceptionAddButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL));
		exceptionAddButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				addExceptionButtonPressed();
			}
		});
		exceptionRemoveButton = new Button(excComposite, SWT.PUSH);
		exceptionRemoveButton.setEnabled(!isReadonly());
		exceptionRemoveButton.setText("Remove");
		exceptionRemoveButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				removeExceptionButtonPressed();
			}
		});

		// Default non-removable exception table
		toolkit.createLabel(excComposite, "");

		manLabel = toolkit.createLabel(excComposite, "Mandatory Exceptions:");
		manLabel.setEnabled(!isReadonly());
		mdg = new GridData();
		mdg.horizontalSpan = 2;
		manLabel.setLayoutData(mdg);

		Table nonRemovableExceptionTable = toolkit.createTable(excComposite,
				SWT.BORDER);
		nonRemovableExceptionTable.setEnabled(!isReadonly());
		tgd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL);
		tgd.verticalSpan = 3;
		tgd.heightHint = 60;
		tgd.minimumWidth = 15;
		tgd.widthHint = 100;
		nonRemovableExceptionTable.setLayoutData(tgd);

		nonRemovableExceptionViewer = new TableViewer(
				nonRemovableExceptionTable);
		nonRemovableExceptionViewer
				.setContentProvider(new NonRemoveableExceptionContentProvider());
		nonRemovableExceptionViewer
				.setLabelProvider(new ExceptionLabelProvider());
	}

	private void methodSelectionChanged(SelectionChangedEvent event) {
		StructuredSelection sel = (StructuredSelection) event.getSelection();
		currentMethod = (IMethod) sel.getFirstElement();
		flavorViewer.setInput(currentMethod);
		disableFlavor();
	}

	private void flavorSelectionChanged(SelectionChangedEvent event) {
		StructuredSelection sel = (StructuredSelection) event.getSelection();
		selectedFlavor = (OssjEntityMethodFlavor) sel.getFirstElement();

		if (selectedFlavor != null) {
			String property = selectedFlavor.getPojoLabel();
			String flavorStr = ((Method) currentMethod)
					.getOssjMethodProperties().getProperty(property);
			currentFlavorDetails = new EntityMethodFlavorDetails(
					((Method) currentMethod).getContainingArtifact(), flavorStr);
			updateFlavor();
		}
	}

	protected void exceptionSelectionChanged(SelectionChangedEvent event) {
		exceptionRemoveButton.setEnabled(!isReadonly()
				&& !exceptionViewer.getSelection().isEmpty());
	}

	private void addExceptionButtonPressed() {
		BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(
				getIArtifact().getTigerstripeProject(), ExceptionArtifact.MODEL);
		dialog.setMessage("Select "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IExceptionArtifactImpl.class.getName()).getLabel(null)
				+ " Artifacts to add.");
		dialog.setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IExceptionArtifactImpl.class.getName()).getLabel(null)
				+ " Artifact Selector");
		try {
			ArtifactManager mgr = ((IAbstractArtifactInternal) getIArtifact())
					.getArtifactManager();

			// Build list of existing exceptions
			List<IAbstractArtifactInternal> existingExc = new ArrayList<IAbstractArtifactInternal>();
			for (String fqn : currentFlavorDetails.getExceptions()) {
				IAbstractArtifactInternal art = mgr
						.getArtifactByFullyQualifiedName(fqn,
						true, new NullProgressMonitor());
				if (art != null) {
					existingExc.add(art);
				}
			}

			IAbstractArtifact[] selectedExceptions = dialog
					.browseAvailableArtifacts(getSection().getShell(),
							existingExc);
			if (selectedExceptions.length != 0) {
				for (IAbstractArtifact artifact : selectedExceptions) {
					currentFlavorDetails.addException(artifact
							.getFullyQualifiedName());
				}
				markPageModified();
				updateEntity();
				updateFlavor();
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void removeExceptionButtonPressed() {
		String fqn = (String) ((IStructuredSelection) exceptionViewer
				.getSelection()).getFirstElement();
		currentFlavorDetails.removeException(fqn);
		markPageModified();
		updateEntity();
		updateFlavor();
	}

	/**
	 * Update the flavor view with what is currently selected. If nothing is
	 * selected the view should be all disabled
	 * 
	 */
	private void updateFlavor() {
		if (currentFlavorDetails == null) {
			disableFlavor();
		} else {
			exceptionAddButton.setEnabled(!isReadonly());
			exceptionViewer.setInput(currentFlavorDetails);
			nonRemovableExceptionViewer.setInput(getNonRemoveableExceptions());
			falseButton.setEnabled(!isReadonly());
			optionalButton.setEnabled(!isReadonly());
			trueButton.setEnabled(!isReadonly());
			falseButton.setSelection(EntityMethodFlavorDetails.FLAVOR_FALSE
					.equals(currentFlavorDetails.getFlag()));
			optionalButton
					.setSelection(EntityMethodFlavorDetails.FLAVOR_OPTIONAL
							.equals(currentFlavorDetails.getFlag()));
			trueButton.setSelection(EntityMethodFlavorDetails.FLAVOR_TRUE
					.equals(currentFlavorDetails.getFlag()));
			descriptionText.setEnabled(!isReadonly());
			descriptionText.setText(currentFlavorDetails.getComment());
			exceptionRemoveButton.setEnabled(!isReadonly()
					&& !exceptionViewer.getSelection().isEmpty());
		}
	}

	private List<String> getNonRemoveableExceptions() {
		ArrayList<String> result = new ArrayList<String>();
		((Method) currentMethod)
				.setContainingArtifact((IAbstractArtifactInternal) getIArtifact());
		ArtifactManager mgr = ((Method) currentMethod).getArtifactManager();
		ManagedEntityDetails details = new ManagedEntityDetails(mgr);
		String methodId = currentMethod.getMethodId();
		if (":create()".equals(methodId)) {
			result.addAll(details.getDefaultCreateFlavorDetails(selectedFlavor)
					.getExceptions());
		} else if (":get()".equals(methodId)) {
			result.addAll(details.getDefaultGetFlavorDetails(selectedFlavor)
					.getExceptions());
		} else if (":set()".equals(methodId)) {
			result.addAll(details.getDefaultSetFlavorDetails(selectedFlavor)
					.getExceptions());
		} else if (":remove()".equals(methodId)) {
			result.addAll(details.getDefaultRemoveFlavorDetails(selectedFlavor)
					.getExceptions());
		} else {
			result.addAll(details.getDefaultCustomMethodFlavorDetails(
					selectedFlavor).getExceptions());
		}

		return result;
	}

	/**
	 * Disable the flavor view
	 * 
	 */
	private void disableFlavor() {
		falseButton.setEnabled(false);
		optionalButton.setEnabled(false);
		trueButton.setEnabled(false);

		falseButton.setSelection(false);
		optionalButton.setSelection(false);
		trueButton.setSelection(false);

		descriptionText.setEnabled(false);
		exceptionAddButton.setEnabled(false);
		exceptionViewer.setInput(null);
		nonRemovableExceptionViewer.setInput(null);
	}

	/**
	 * Set the silent update flag
	 * 
	 * @param silentUpdate
	 */
	private void setSilentUpdate(boolean silentUpdate) {
		this.silentUpdate = silentUpdate;
	}

	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void handleModifyText(ModifyEvent e) {
		if (e.getSource() == descriptionText) {
			currentFlavorDetails.setComment(descriptionText.getText().trim());
			updateEntity();
			markPageModified();
			flavorViewer.refresh(true);
		}
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == trueButton || e.getSource() == falseButton
				|| e.getSource() == optionalButton) {
			flavorButtonPressed((Button) e.getSource());
		}
	}

	protected void flavorButtonPressed(Button button) {
		if (button == trueButton) {
			currentFlavorDetails.setFlag(EntityMethodFlavorDetails.FLAVOR_TRUE);
		} else if (button == falseButton) {
			currentFlavorDetails
					.setFlag(EntityMethodFlavorDetails.FLAVOR_FALSE);
		} else if (button == optionalButton) {
			currentFlavorDetails
					.setFlag(EntityMethodFlavorDetails.FLAVOR_OPTIONAL);
		}
		updateEntity();
		markPageModified();
		flavorViewer.refresh(true);
	}

	private void updateEntity() {
		String methodName = currentMethod.getName();
		IOssjEntitySpecifics specifics = (IOssjEntitySpecifics) getIArtifact()
				.getIStandardSpecifics();

		((Method) currentMethod).getOssjMethodProperties().setProperty(
				selectedFlavor.getPojoLabel(), currentFlavorDetails.toString());

		// CRUD methods need to be handled separately
		if ("create".equals(methodName)) {
			specifics.setCRUDProperties(IOssjEntitySpecifics.CREATE,
					((Method) currentMethod).getOssjMethodProperties());
		} else if ("get".equals(methodName)) {
			specifics.setCRUDProperties(IOssjEntitySpecifics.GET,
					((Method) currentMethod).getOssjMethodProperties());
		} else if ("set".equals(methodName)) {
			specifics.setCRUDProperties(IOssjEntitySpecifics.SET,
					((Method) currentMethod).getOssjMethodProperties());
		} else if ("remove".equals(methodName)) {
			specifics.setCRUDProperties(IOssjEntitySpecifics.DELETE,
					((Method) currentMethod).getOssjMethodProperties());
		}
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void refresh() {
		updateForm();
	}

	protected void updateForm() {
		setSilentUpdate(true);
		resetSelections();
		setSilentUpdate(false);
	}

	private void resetSelections() {
		currentMethod = null;
		currentFlavorDetails = null;
		selectedFlavor = null;

		disableFlavor();
		flavorViewer.setInput(null);
		methodViewer.setInput(((ArtifactEditorBase) getPage().getEditor())
				.getIArtifact());
	}

}
