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
package org.eclipse.tigerstripe.workbench.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.EntityMethodFlavorDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics.EntityOveride;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjMethod;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.TSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

/**
 * This dialog allows to edit the operation overide on a managed entity.
 * 
 * @author Eric Dillon
 * 
 */
public class ManagedEntityOverideDialog extends TSMessageDialog {

	private final static int UNOVERRIDE_ID = 2048;

	private boolean changedMade = false;

	private IManagedEntityDetails managedEntity;
	private ISessionArtifact parentSessionArtifact;

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

	private class ExceptionContentProvider extends ArrayContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement == null)
				return new Object[1];
			EntityMethodFlavorDetails details = (EntityMethodFlavorDetails) inputElement;
			return details.getExceptions().toArray();
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
			EntityOveride overide = (EntityOveride) inputElement;
			IOssjMethod[] methods = overide.getMethods();
			return methods;
		}
	}

	private class MethodLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			IMethod method = (IMethod) element;
			boolean hasOverride = getOverride()
					.hasOveride((IOssjMethod) method);

			return (hasOverride ? "> " : "") + method.getName();
		}

	}

	private class FlavorsContentProvider extends ArrayContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {

			if (inputElement == null)
				return new String[0];

			IOssjMethod mthod = (IOssjMethod) inputElement;
			OssjEntityMethodFlavor[] flavors = mthod.getSupportedFlavors();
			return flavors;
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

			boolean hasOverride = getOverride().hasOveride(
					(IOssjMethod) currentMethod, flavor);
			if (hasOverride) {
				details = getOverride().getFlavorDetails(
						(IOssjMethod) currentMethod, flavor);
			}

			return (hasOverride ? "> " : "") + flavor.getPojoLabel() + " ("
					+ details.getFlag() + ")";
		}
	}

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class EntityMethodFlavorListener implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}

	}

	public ManagedEntityOverideDialog(Shell parentShell,
			IManagedEntityDetails managedEntity,
			ISessionArtifact parentSessionArtifact) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.managedEntity = managedEntity;
		this.parentSessionArtifact = parentSessionArtifact;
	}

	protected void setDefaultMessage() {
		setMessage("Please select method of override locally on this Facade.");
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());
		GridData gdl = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gdl.widthHint = 800;
		area.setLayoutData(gdl);

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 3;
		GridLayout layout = new GridLayout();

		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createMessageArea(composite, nColumns);
		createMethodSelector(composite);
		createFlavorSelector(composite);
		createFlavorDetails(composite);
		initDialog();

		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	protected void initDialog() {
		getShell().setText("Operations Overide Details");
		methodViewer.setInput(((ManagedEntityDetails) managedEntity)
				.getOveride());
		flavorViewer.setInput(null);
		updateFlavor();
	}

	/**
	 * Returns the recommended maximum width for text fields (in pixels). This
	 * method requires that createContent has been called before this method is
	 * call. Subclasses may override to change the maximum width for text
	 * fields.
	 * 
	 * @return the recommended maximum width for text fields.
	 */
	protected int getMaxFieldWidth() {
		return convertWidthInCharsToPixels(40);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		Button unOverrideButton = createButton(parent, UNOVERRIDE_ID,
				"Un-Override", false);
		unOverrideButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				unOverrideButtonPressed();
			}
		});
		unOverrideButton
				.setToolTipText("Revert local Overrides to their definitions\n in the original Entity Artifact");

		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, true);
	}

	private void unOverrideButtonPressed() {
		String currentMethodName = null;
		if (currentMethod != null) {
			currentMethodName = currentMethod.getName();
		}
		UnOverrideDialog dialog = new UnOverrideDialog(getShell(),
				currentMethodName, selectedFlavor);

		if (dialog.open() == Window.OK) {
			int selectedOveride = dialog.getSelectedOverride();
			switch (selectedOveride) {
			case UnOverrideDialog.OVERRIDE_ALL:
				unOverrideAll();
				break;
			case UnOverrideDialog.OVERRIDE_FLAVOR:
				unOverrideFlavor();
				break;
			case UnOverrideDialog.OVERRIDE_METHOD:
				unOverrideMethod();
			}
		}
	}

	protected EntityOveride getOverride() {
		return ((ManagedEntityDetails) this.managedEntity).getOveride();
	}

	protected void unOverrideAll() {
		getOverride().unOverrideAll();
		currentFlavorDetails = null;
		selectedFlavor = null;
		currentMethod = null;
		initDialog();
		markChangeMade();
	}

	protected void unOverrideFlavor() {
		getOverride().unOverrideFlavor(currentMethod.getMethodId(),
				selectedFlavor);
		currentFlavorDetails = null;
		selectedFlavor = null;
		currentMethod = null;
		initDialog();
		markChangeMade();
	}

	protected void unOverrideMethod() {
		getOverride().unOverrideMethod(currentMethod.getMethodId());
		currentFlavorDetails = null;
		selectedFlavor = null;
		currentMethod = null;
		initDialog();
		markChangeMade();
	}

	@Override
	protected void okPressed() {
		super.okPressed();

	}

	private TableViewer methodViewer;

	private TableViewer flavorViewer;

	private TableViewer exceptionViewer;

	private TableViewer nonRemovableExceptionViewer;

	private Button optionalButton;

	private Button trueButton;

	private Button falseButton;

	private Button exceptionAddButton;

	private Button exceptionRemoveButton;

	private FlavorsContentProvider flavorsContentProvider;

	private EntityMethodFlavorDetails currentFlavorDetails;

	private IMethod currentMethod;

	private OssjEntityMethodFlavor selectedFlavor;

	private void createMethodSelector(Composite parent) {
		Composite methodComposite = new Composite(parent, SWT.LEFT);
		GridData gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.verticalSpan = 3;
		gd.heightHint = 50;
		gd.widthHint = 10;
		gd.minimumWidth = 10;
		gd.minimumHeight = 30;
		methodComposite.setLayoutData(gd);
		GridLayout methodLayout = new GridLayout();
		methodLayout.numColumns = 1;
		methodComposite.setLayout(methodLayout);

		Label label = new Label(methodComposite, SWT.NULL);
		label.setText("Entity Method");

		Table table = new Table(methodComposite, SWT.BORDER);
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
	}

	private void createFlavorSelector(Composite parent) {
		Composite flavorComposite = new Composite(parent, SWT.LEFT);
		GridData gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.verticalSpan = 3;
		gd.heightHint = 50;
		gd.widthHint = 5;
		gd.minimumWidth = 5;
		gd.minimumHeight = 30;
		flavorComposite.setLayoutData(gd);
		GridLayout methodLayout = new GridLayout();
		methodLayout.numColumns = 1;
		flavorComposite.setLayout(methodLayout);

		Label label = new Label(flavorComposite, SWT.NULL);
		label.setText("Method Variation");

		Table table = new Table(flavorComposite, SWT.FLAT | SWT.BORDER);
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

	private void createFlavorDetails(Composite parent) {
		Composite detailsComposite = new Composite(parent, SWT.RIGHT);
		GridLayout dcl = new GridLayout();
		dcl.numColumns = 1;
		detailsComposite.setLayout(dcl);

		GridData dcgd = new GridData(GridData.FILL_BOTH);
		detailsComposite.setLayoutData(dcgd);

		EntityMethodFlavorListener adapter = new EntityMethodFlavorListener();

		Label lab = new Label(detailsComposite, SWT.NULL);
		lab.setText("Flavor Details");

		// The true/optional/false radio buttons
		Composite visiComposite = new Composite(detailsComposite, SWT.NULL);
		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 3;
		visiComposite.setLayout(gLayout);
		visiComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

		trueButton = new Button(visiComposite, SWT.RADIO);
		trueButton.setText("True");
		trueButton.addSelectionListener(adapter);
		optionalButton = new Button(visiComposite, SWT.RADIO);
		optionalButton.setText("Optional");
		optionalButton.addSelectionListener(adapter);
		falseButton = new Button(visiComposite, SWT.RADIO);
		falseButton.setText("False");
		falseButton.addSelectionListener(adapter);

		// the Exception list
		Composite excComposite = new Composite(detailsComposite, SWT.NULL);
		gLayout = new GridLayout();
		gLayout.numColumns = 2;
		excComposite.setLayout(gLayout);
		excComposite.setLayoutData(new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

		Label exLab = new Label(excComposite, SWT.NULL);
		exLab.setText("Custom Exceptions");
		GridData mdg = new GridData();
		mdg.horizontalSpan = 2;
		exLab.setLayoutData(mdg);

		Table exceptionTable = new Table(excComposite, SWT.BORDER);
		GridData tgd = new GridData(GridData.FILL_BOTH
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
		exceptionRemoveButton.setText("Remove");
		exceptionRemoveButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				removeExceptionButtonPressed();
			}
		});
		// Default non-removable exception table
		new Label(excComposite, SWT.NULL);

		exLab = new Label(excComposite, SWT.NULL);
		exLab.setText("Mandatory Exceptions");
		mdg = new GridData();
		mdg.horizontalSpan = 2;
		exLab.setLayoutData(mdg);

		Table nonRemovableExceptionTable = new Table(excComposite, SWT.BORDER);
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
		setMessage("Please select the interaction flavor to override for this method.");
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
			if (getOverride().hasOveride((IOssjMethod) currentMethod,
					selectedFlavor)) {
				currentFlavorDetails = getOverride().getFlavorDetails(
						(IOssjMethod) currentMethod, selectedFlavor);
			}
			updateFlavor();
		}
	}

	protected void exceptionSelectionChanged(SelectionChangedEvent event) {
		exceptionRemoveButton.setEnabled(!exceptionViewer.getSelection()
				.isEmpty());
	}

	private void addExceptionButtonPressed() {
		BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(
				parentSessionArtifact.getTigerstripeProject(),
				ExceptionArtifact.MODEL);
		dialog.setMessage("Select "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IExceptionArtifactImpl.class.getName()).getLabel(
						managedEntity) + " Artifacts to add.");
		dialog.setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IExceptionArtifactImpl.class.getName()).getLabel(managedEntity)
				+ " Artifact Selector");
		try {
			ArtifactManager mgr = ((ManagedEntityDetails) managedEntity)
					.getArtifact().getArtifactManager();

			// Build list of existing exceptions
			List<AbstractArtifact> existingExc = new ArrayList<AbstractArtifact>();
			for (String fqn : currentFlavorDetails.getExceptions()) {
				AbstractArtifact art = mgr.getArtifactByFullyQualifiedName(fqn,
						true, new NullProgressMonitor());
				if (art != null) {
					existingExc.add(art);
				}
			}

			IAbstractArtifact[] selectedExceptions = dialog
					.browseAvailableArtifacts(getShell(), existingExc);
			if (selectedExceptions.length != 0) {
				EntityOveride overide = ((ManagedEntityDetails) managedEntity)
						.getOveride();

				for (IAbstractArtifact artifact : selectedExceptions) {
					currentFlavorDetails.addException(artifact
							.getFullyQualifiedName());
				}
				overide.overideMethod(currentMethod.getMethodId(),
						selectedFlavor, currentFlavorDetails);
				updateFlavor();
				markChangeMade();
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	private void removeExceptionButtonPressed() {
		EntityOveride overide = ((ManagedEntityDetails) managedEntity)
				.getOveride();
		String fqn = (String) ((IStructuredSelection) exceptionViewer
				.getSelection()).getFirstElement();
		currentFlavorDetails.removeException(fqn);
		overide.overideMethod(currentMethod.getMethodId(), selectedFlavor,
				currentFlavorDetails);
		markChangeMade();
		updateFlavor();
	}

	/**
	 * Update the flavor view with what is currently selected. If nothing is
	 * selected the view should be all disabled
	 * 
	 */
	private void updateFlavor() {
		if (getButton(IDialogConstants.OK_ID) != null) {
			getButton(IDialogConstants.OK_ID).setEnabled(changedMade);
		}

		if (currentFlavorDetails == null) {
			disableFlavor();
		} else {
			setMessage("Please specify details to override for this flavor.");
			exceptionAddButton.setEnabled(true);
			exceptionViewer.setInput(currentFlavorDetails);
			nonRemovableExceptionViewer.setInput(getNonRemoveableExceptions());
			falseButton.setEnabled(true);
			optionalButton.setEnabled(true);
			trueButton.setEnabled(true);
			falseButton.setSelection(EntityMethodFlavorDetails.FLAVOR_FALSE
					.equals(currentFlavorDetails.getFlag()));
			optionalButton
					.setSelection(EntityMethodFlavorDetails.FLAVOR_OPTIONAL
							.equals(currentFlavorDetails.getFlag()));
			trueButton.setSelection(EntityMethodFlavorDetails.FLAVOR_TRUE
					.equals(currentFlavorDetails.getFlag()));
			exceptionRemoveButton.setEnabled(!exceptionViewer.getSelection()
					.isEmpty());
		}
	}

	private List<String> getNonRemoveableExceptions() {
		ArrayList<String> result = new ArrayList<String>();
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
		if (getButton(IDialogConstants.OK_ID) != null) {
			getButton(IDialogConstants.OK_ID).setEnabled(changedMade);
		}

		falseButton.setEnabled(false);
		optionalButton.setEnabled(false);
		trueButton.setEnabled(false);

		falseButton.setSelection(false);
		optionalButton.setSelection(false);
		trueButton.setSelection(false);
		exceptionAddButton.setEnabled(false);
		exceptionViewer.setInput(null);
		nonRemovableExceptionViewer.setInput(null);
		setDefaultMessage();
	}

	private void handleWidgetSelected(SelectionEvent event) {
		if (event.getSource() == trueButton || event.getSource() == falseButton
				|| event.getSource() == optionalButton) {
			buttonPressed((Button) event.getSource());
		}
	}

	protected void buttonPressed(Button button) {
		EntityOveride overide = ((ManagedEntityDetails) managedEntity)
				.getOveride();

		String flag = null;
		boolean updated = false;
		if (button == trueButton && trueButton.getSelection()) {
			flag = EntityMethodFlavorDetails.FLAVOR_TRUE;
			updated = true;
		} else if (button == falseButton && falseButton.getSelection()) {
			flag = EntityMethodFlavorDetails.FLAVOR_FALSE;
			updated = true;
		} else if (button == optionalButton && optionalButton.getSelection()) {
			flag = EntityMethodFlavorDetails.FLAVOR_OPTIONAL;
			updated = true;
		}

		if (updated) {
			currentFlavorDetails.setFlag(flag);
			overide.overideMethod(currentMethod.getMethodId(), selectedFlavor,
					currentFlavorDetails);
			markChangeMade();
		}
	}

	/**
	 * Marks that changes have been made in the dialog, and as a result the OK
	 * button should be enabled.
	 * 
	 */
	protected void markChangeMade() {
		changedMade = true;
		methodViewer.refresh(true);
		flavorViewer.refresh(true);
		getButton(IDialogConstants.OK_ID).setEnabled(changedMade);
	}
}
