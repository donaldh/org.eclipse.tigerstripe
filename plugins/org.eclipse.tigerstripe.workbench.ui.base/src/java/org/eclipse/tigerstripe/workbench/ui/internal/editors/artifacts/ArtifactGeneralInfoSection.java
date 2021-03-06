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
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.NullAbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.ComparableArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.IFocusedControlProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.CheckButtonEditListener;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.SpellCheckingTextBox;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.TSOpenAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class ArtifactGeneralInfoSection extends ArtifactSectionPart implements
		IFocusedControlProvider {

	/**
	 * An adapter that will listen for changes on the form
	 */
	private class GeneralInfoPageListener implements ModifyListener,
			KeyListener, SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			try {
				handleWidgetSelected(e);
			} catch (TigerstripeException ee) {
				Status status = new Status(IStatus.WARNING, EclipsePlugin
						.getPluginId(), 111, "Unexpected Exception", ee);
				EclipsePlugin.log(status);
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.F3) {
				navigateToKeyPressed(e);
			}
		}

		public void modifyText(ModifyEvent e) {
			try {
				handleModifyText(e);
			} catch (TigerstripeException ee) {
				Status status = new Status(IStatus.WARNING, EclipsePlugin
						.getPluginId(), 111, "Unexpected Exception", ee);
				EclipsePlugin.log(status);
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.character == '\r') {
				commit(false);
			}
		}

	}

	private boolean silentUpdate = false;

	private Text projectText;

	private Text packageText;

	private Text extendNameText;

	private Text implementsText;

	private Button isAbstractButton;

	private Button typeBrowseButton;

	private Button implementsTextBrowseButton;

	private SpellCheckingTextBox descriptionText;

	public ArtifactGeneralInfoSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider, SWT.NONE);
		setTitle("General Information");
		createContent();
	}

	@Override
	protected void createContent() {
		getSection().setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		createProjectName(getBody(), getToolkit());
		createQualifiedName(getBody(), getToolkit());
		createDescription(getBody(), getToolkit());

		int buttonWidth = -1;
		if (getContentProvider().hasExtends()) {
			buttonWidth = createExtendName(getBody(), getToolkit());
		}
		GlobalSettingsProperty prop = (GlobalSettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.GLOBAL_SETTINGS);
		boolean implementsEnabled = prop
				.getPropertyValue(IGlobalSettingsProperty.IMPLEMENTSRELATIONSHIP);

		if (implementsEnabled && getContentProvider().hasImplements()) {
			createImplementsNames(getBody(), getToolkit(), buttonWidth);
		}

		if (getContentProvider().hasAbstract()) {
			createIsAbstractButton(getBody(), getToolkit());
		}

		// updateForm();
		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createProjectName(Composite parent, FormToolkit toolkit) {
		String projectLabel = "";
		if (getIArtifact().getTigerstripeProject() != null) {
			projectLabel = getIArtifact().getTigerstripeProject().getName();
		}
		toolkit.createLabel(parent, "Project: ", SWT.NULL);
		projectText = toolkit.createText(parent, projectLabel);
		projectText.setEditable(false);
		projectText.setEnabled(false);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		projectText.setLayoutData(gd);
	}

	private void createQualifiedName(Composite parent, FormToolkit toolkit) {
		toolkit.createLabel(parent, "Qualified Name: ", SWT.NULL);
		packageText = toolkit.createText(parent, getIArtifact()
				.getFullyQualifiedName());
		packageText.setEditable(false);
		packageText.setEnabled(true);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		packageText.setLayoutData(gd);
	}

	private void createDescription(Composite parent, FormToolkit toolkit) {
		toolkit.createLabel(parent, "Description: ", SWT.NULL);
		// Made changes to include a scroll bar in the Description box and
		// increased
		// the size to 100 from 70 - bug # 162
		Composite composite = toolkit.createComposite(parent, SWT.BORDER);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		gd.heightHint = 100;
		gd.maxWidth = 50;
		gd.maxHeight = 100;
		composite.setLayoutData(gd);
		GridLayoutFactory.fillDefaults().applyTo(composite);
		descriptionText = new SpellCheckingTextBox(composite, getIArtifact()
				.getComment());
		descriptionText.addModifyListener(new GeneralInfoPageListener());
		descriptionText.setEnabled(!this.isReadonly());
		descriptionText.addKeyListener(new GeneralInfoPageListener());
	}

	private int createExtendName(Composite parent, FormToolkit toolkit) {
		// N.M: bugzilla 328441 - provide easy navigability to artifact being extended/implemented
		Hyperlink link = toolkit.createHyperlink(parent, "Extends:", SWT.NULL);
		
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {

				IAbstractArtifact parent = getIArtifact().getExtendedArtifact();
				if (parent != null) {
					openArtifact(parent);
				} else {
					MessageDialog
							.openInformation(PlatformUI.getWorkbench()
									.getDisplay().getActiveShell(),
									"No Parent Artifact",
									"This artifact doesn't have a parent artifact to open!");
				}
			}
		});

		Composite composite = toolkit.createComposite(parent);
		TableWrapLayout layout = TigerstripeLayoutFactory
				.createClearTableWrapLayout(2, false);
		layout.horizontalSpacing = 5;
		composite.setLayout(layout);
		composite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		
		IAbstractArtifact extend = getIArtifact().getExtendedArtifact();	
		if (extend == null) {
			extendNameText = toolkit.createText(composite, "");
		} else {
			extendNameText = toolkit.createText(composite, extend
					.getFullyQualifiedName());
		}
		extendNameText
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		extendNameText.setEnabled(false);
		extendNameText.addModifyListener(new GeneralInfoPageListener());
		extendNameText.addKeyListener(new GeneralInfoPageListener());

		typeBrowseButton = toolkit.createButton(composite, "Select", SWT.PUSH);
		typeBrowseButton.setEnabled(!this.isReadonly());
		typeBrowseButton.addSelectionListener(new GeneralInfoPageListener());

		Point size = typeBrowseButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		return size.x;
	}
	
	private void openArtifact(IAbstractArtifact toOpen) {
		try {
			String fqn = toOpen.getFullyQualifiedName();
			if (fqn != null && fqn.length() != 0) {
				IAbstractArtifact target = null;
				if (getIArtifact() != null
						&& getIArtifact().getProject() != null) {
					IArtifactManagerSession session = getIArtifact()
							.getProject().getArtifactManagerSession();
					target = session.getArtifactByFullyQualifiedName(fqn);
				}
				if (target == null) {
					target = toOpen;
				}
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				TSOpenAction.openEditor(target, page);
			}
		} catch (TigerstripeException ee) {
			EclipsePlugin.log(ee);
		}
	}

	private void createImplementsNames(Composite parent, FormToolkit toolkit,
			int buttonWidth) {
		
		// N.M: bugzilla 328441 - provide easy navigability to artifact being extended/implemented
		Hyperlink link = toolkit.createHyperlink(parent, "Implements:", SWT.NULL);
		
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				Collection<IAbstractArtifact> implementedArtifacts = getIArtifact()
						.getImplementedArtifacts();
				if (implementedArtifacts != null
						&& implementedArtifacts.size() > 0) {
					Iterator<IAbstractArtifact> iterator = implementedArtifacts
							.iterator();
					if (iterator.hasNext()) {
						IAbstractArtifact artifact = iterator.next();
						openArtifact(artifact);
					}
				} else {
					MessageDialog.openInformation(PlatformUI.getWorkbench()
							.getDisplay().getActiveShell(),
							"No Implemented Artifact",
							"This artifact doesn't implement anything!");
				}
			}
		});

		Composite composite = toolkit.createComposite(parent);
		GridLayout layout = TigerstripeLayoutFactory.createClearGridLayout(2,
				false);
		layout.horizontalSpacing = 5;
		composite.setLayout(layout);
		composite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		String implStr = getIArtifact().getImplementedArtifactsAsStr();
		implementsText = toolkit.createText(composite, implStr);
		implementsText.setEnabled(false);
		implementsText.setLayoutData(new GridData(GridData.FILL_BOTH));

		implementsTextBrowseButton = toolkit.createButton(composite, "Select",
				SWT.PUSH);
		GridData gd = new GridData();
		gd.widthHint = buttonWidth;
		implementsTextBrowseButton.setLayoutData(gd);
		implementsTextBrowseButton.setEnabled(!this.isReadonly());
		implementsTextBrowseButton
				.addSelectionListener(new GeneralInfoPageListener());
	}

	private void createIsAbstractButton(Composite parent, FormToolkit toolkit) {
		isAbstractButton = toolkit.createButton(parent,
				"This artifact is abstract", SWT.CHECK);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		isAbstractButton.setLayoutData(td);
		isAbstractButton.setEnabled(!this.isReadonly());
		isAbstractButton.addSelectionListener(new GeneralInfoPageListener());

		if (!isReadonly()) {
			CheckButtonEditListener editListener = new CheckButtonEditListener(
					(TigerstripeFormEditor) getPage().getEditor(), "abstract",
					IModelChangeDelta.SET, (URI) getIArtifact().getAdapter(
							URI.class));
			isAbstractButton.addSelectionListener(editListener);
		}
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

	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void handleModifyText(ModifyEvent e) throws TigerstripeException {
		if (!isSilentUpdate()) {
			// when updating the form, the changes to all fields should be
			// ignored so that the form is not marked as dirty.

			if (e.getSource() == descriptionText.getControl()) {
				getIArtifact().setComment(descriptionText.getText().trim());
			} else if (e.getSource() == extendNameText) {

				if (extendNameText.getText().trim().length() == 0) {
					getIArtifact()
							.setExtendedArtifact((IAbstractArtifact) null);
				} else {
					ITigerstripeModelProject project = getIArtifact()
							.getTigerstripeProject();
					IArtifactManagerSession session = project
							.getArtifactManagerSession();

					IAbstractArtifact artifact = null;
					try {
						artifact = session
								.getArtifactByFullyQualifiedName(extendNameText
										.getText().trim());
					} catch (TigerstripeException ee) {
						// means that the corresponding artifact doesn't exist
						// in
						// the context
						// this should be caught by the builder when trying to
						// build
						// and validate the artifact
					} finally {
						if (artifact == null) {
							artifact = session.makeArtifact(getIArtifact());
							artifact.setFullyQualifiedName(extendNameText
									.getText().trim());
						}
					}
					getIArtifact().setExtendedArtifact(artifact);
				}
			}
			markPageModified();
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

	private void browseButtonPressed() throws TigerstripeException {
		BrowseForArtifactDialog dialog = null;
		IAbstractArtifact[] artifacts = null;

		// Bug 789, need to handle the case of AssociationClasses differently
		if (getIArtifact() instanceof IAssociationClassArtifact) {
			dialog = new BrowseForArtifactDialog(getIArtifact()
					.getTigerstripeProject(),
					new IAbstractArtifact[] { AssociationClassArtifact.MODEL,
							ManagedEntityArtifact.MODEL });
			dialog.setIncludeEmptyValue(true);
			dialog.setTitle("Super Artifact");
			dialog.setMessage("Select the artifact" + " to be extended.");

			artifacts = dialog.browseAvailableArtifacts(
					getSection().getShell(), Arrays
							.asList(new Object[] { getIArtifact() }));
		} else {
			// Fix the text for the Title and Message with the specific Artifact
			// Type
			// Bug # 124
			dialog = new BrowseForArtifactDialog(getIArtifact()
					.getTigerstripeProject(), getIArtifact());
			dialog.setIncludeEmptyValue(true);
			String name = getIArtifact().getMetadata().getLabel(getIArtifact());
			dialog.setTitle("Super " + name);
			dialog.setMessage("Select the " + name + " to be extended.");

			artifacts = dialog.browseAvailableArtifacts(
					getSection().getShell(), Arrays
							.asList(new Object[] { getIArtifact() }));
		}
		if (artifacts.length != 0) {
			IAbstractArtifact artifact = artifacts[0];
			
			if (NullAbstractArtifact.INSATNCE.equals(artifact)) {
				extendNameText.setText("");
				getIArtifact().setExtendedArtifact((IAbstractArtifact)null);
			} else {
				extendNameText.setText(artifact.getFullyQualifiedName());
				getIArtifact().setExtendedArtifact(artifact);
			}
			
			markPageModified();
		}
	}

	public void handleWidgetSelected(SelectionEvent e)
			throws TigerstripeException {
		if (e.getSource() == typeBrowseButton) {
			browseButtonPressed();
		} else if (e.getSource() == isAbstractButton) {
			isAbstractButtonPressed();
		} else if (e.getSource() == implementsTextBrowseButton) {
			implementsSelectPressed();
		}
	}

	protected void implementsSelectPressed() {
		IStructuredContentProvider artProvider = new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof IAbstractArtifact) {
					IAbstractArtifact art = (IAbstractArtifact) inputElement;
					ITigerstripeModelProject tsProject = art
							.getTigerstripeProject();
					if (tsProject != null) {
						try {
							IArtifactManagerSession session = tsProject
									.getArtifactManagerSession();
							IQueryArtifactsByType query = (IQueryArtifactsByType) session
									.makeQuery(IQueryArtifactsByType.class
											.getName());
							query.setIncludeDependencies(true);
							query.setArtifactType(ISessionArtifact.class
									.getName());

							Collection<IAbstractArtifact> sessions = session
									.queryArtifact(query);
							return sessions
									.toArray(new IAbstractArtifact[sessions
											.size()]);
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					}
				}

				return new Object[0];
			}

			public void dispose() {
				// TODO Auto-generated method stub

			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub

			}

		};

		ListSelectionDialog dialog = new ListSelectionDialog(getBody()
				.getShell(), getIArtifact(), artProvider,
				new AbstractArtifactLabelProvider(),
				"Select implemented artifacts.");
		dialog.setInitialSelections(ComparableArtifact.asComparableArtifacts(
				getIArtifact().getImplementedArtifacts()).toArray());

		if (dialog.open() == Window.OK) {
			Object[] selectedObjects = dialog.getResult();
			Collection<IAbstractArtifact> art = new ArrayList<IAbstractArtifact>();
			for (int i = 0; i < selectedObjects.length; i++) {
				art.add((IAbstractArtifact) selectedObjects[i]);
			}
			getIArtifact().setImplementedArtifacts(art);
			implementsText.setText(getIArtifact()
					.getImplementedArtifactsAsStr());
			markPageModified();
		}
	}

	protected void isAbstractButtonPressed() {
		getIArtifact().setAbstract(isAbstractButton.getSelection());
		markPageModified();
	}

	protected void updateForm() {
		setSilentUpdate(true);
		if (descriptionText != null) {
			descriptionText.setText(getIArtifact().getComment());
		}
		if (isAbstractButton != null) {
			if (getContentProvider().hasAbstract()) {
				isAbstractButton.setSelection(getIArtifact().isAbstract());
			}
		}
		if (extendNameText != null) {
			if (getContentProvider().hasExtends()) {
				if (getIArtifact().getExtendedArtifact() != null)
					extendNameText.setText(getIArtifact().getExtendedArtifact()
							.getFullyQualifiedName());
				else
					extendNameText.setText("");
			}
		}
		if (implementsText != null) {
			if (getContentProvider().hasImplements() && implementsText != null) {
				implementsText.setText(getIArtifact()
						.getImplementedArtifactsAsStr());
			}
		}

		setSilentUpdate(false);
	}

	public Control getFocusedControl() {
		return descriptionText.getControl();
	}
}
