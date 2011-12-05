/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import static org.eclipse.swt.SWT.RESIZE;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.InTransaction;
import org.eclipse.tigerstripe.annotation.core.InTransaction.Operation;
import org.eclipse.tigerstripe.annotation.ui.Images;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;
import org.eclipse.tigerstripe.annotation.ui.internal.actions.OpenAnnotationWizardAction;
import org.eclipse.tigerstripe.annotation.ui.internal.view.property.PropertyTree;
import org.eclipse.tigerstripe.annotation.ui.internal.view.property.PropertyTree.SelectionHandler;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLayoutFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.event.EventGroup.EventHandler;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.event.UIEventGroup;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class ArtifactAnnotationSection extends ArtifactSectionPart implements
		IAnnotationListener {

	public static int MASTER_TABLE_COMPONENT_WIDTH = 250;

	private TableViewer viewer;
	private Text description;
	private Composite detailsComposite;

	public ArtifactAnnotationSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit,
			IArtifactFormLabelProvider labelProvider,
			IOssjArtifactFormContentProvider contentProvider) {
		super(page, parent, toolkit, labelProvider, contentProvider, SWT.NONE);
		setTitle("Annotations");
		createContent();
	}

	@Override
	protected void createContent() {
		IManagedForm managedForm = getPage().getManagedForm();
		FormToolkit toolkit = getToolkit();

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		getSection().setLayoutData(td);

		Composite body = getToolkit().createComposite(getSection());
		body.setLayout(TigerstripeLayoutFactory.createClearGridLayout(1, false));
		SashForm sashForm = new SashForm(body, SWT.HORIZONTAL);
		toolkit.adapt(sashForm, false, false);
		sashForm.setMenu(body.getMenu());
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		createMasterPart(managedForm, sashForm);
		createDetailsPart(managedForm, sashForm);

		sashForm.setWeights(new int[] { 1, 2 });

		getSection().setClient(body);
		getToolkit().paintBordersFor(body);
	}

	UIEventGroup<Annotation> annotationsEventGroup = new UIEventGroup<Annotation>("Update Annotations", new EventHandler<Annotation>() {

		public HandleResult handle(
				List<Annotation> events) {
			viewer.refresh();
			return HandleResult.DONE;
		}
	});
	
	private void createMasterPart(final IManagedForm managedForm,
			Composite parent) {
		FormToolkit toolkit = getToolkit();

		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);

		Composite sectionClient = toolkit.createComposite(section);
		GridLayout layout = new GridLayout(2, false);
		sectionClient.setLayout(layout);

		Table table = toolkit.createTable(sectionClient, SWT.BORDER | SWT.FLAT);
		viewer = new TableViewer(table);
		table.setEnabled(!this.isReadonly());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = MASTER_TABLE_COMPONENT_WIDTH;
		table.setLayoutData(gd);

		Composite buttonClient = toolkit.createComposite(sectionClient);
		buttonClient.setLayoutData(new GridData(GridData.FILL));
		buttonClient.setLayout(TigerstripeLayoutFactory
				.createButtonsGridLayout());

		Button addAnno = toolkit.createButton(buttonClient, "Add", SWT.PUSH);
		addAnno.setEnabled(!this.isReadonly());
		addAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		addAnno.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OpenAnnotationWizardAction wizardAction = new OpenAnnotationWizardAction(
						getIArtifact(), "Add Annotation");
				wizardAction.run();
			}
		});
		
		
		Button editAnno = toolkit.createButton(buttonClient, "Edit", SWT.PUSH);
		editAnno.setEnabled(!this.isReadonly());
		editAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		editAnno.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editSelected();
			}
		});

		
		
		Button removeAnno = toolkit.createButton(buttonClient, "Remove",
				SWT.PUSH);
		removeAnno.setEnabled(!this.isReadonly());
		removeAnno.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		removeAnno.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				Object selected = selection.getFirstElement();
				if (selected != null) {
					manager().removeAnnotation((Annotation) selected);
				}
			}
		});

		toolkit.paintBordersFor(sectionClient);
		section.setClient(sectionClient);
		
		viewer.setContentProvider(new AnnotationContentProvider());
		viewer.setLabelProvider(new AnnotationLabelProvider());
		viewer.setInput(((ArtifactEditorBase) getPage().getEditor()).getIArtifact());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				
				
				Object selected = ((IStructuredSelection)event.getSelection()).getFirstElement();
				
				if (selected == null) {
					setDetailsPartVisibility(false);
					description.setText("");
				} else {
					setDetailsPartVisibility(true);
					Annotation ann = (Annotation) selected;
					AnnotationType type = manager().getType(ann);
					description.setText(type.getDescription());
				}
			}
		});
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			
			public void doubleClick(DoubleClickEvent event) {
				editSelected();
			}
		});
		manager().addAnnotationListener(this);
	}

	private void editSelected() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		Object selected = selection.getFirstElement();
		if (selected != null) {
			PropertiesDialog dialog = new PropertiesDialog(getSection()
					.getShell(), (Annotation) selected);
			dialog.open();
		}
	}
	
	private void createDetailsPart(final IManagedForm mform, Composite parent) {
		FormToolkit toolkit = getToolkit();
		Section section = toolkit.createSection(parent,
				ExpandableComposite.NO_TITLE);
		section.setLayout(new GridLayout());
		detailsComposite = toolkit.createComposite(section);
		detailsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		detailsComposite.setLayout(new GridLayout());

		Label label = new Label(detailsComposite, SWT.NONE);
		label.setText("Description:");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		description = new Text(detailsComposite, SWT.MULTI
				| SWT.READ_ONLY | SWT.V_SCROLL | SWT.WRAP);
		description.setBackground(parent.getBackground());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 350;
		gd.heightHint = 50;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		description.setLayoutData(gd);
		
		setVisibleRecursive(detailsComposite, false);

		toolkit.paintBordersFor(detailsComposite);
		section.setClient(detailsComposite);
	}

	private static IAnnotationManager manager() {
		return AnnotationPlugin.getManager();
	}
	
	private void setDetailsPartVisibility(boolean visible) {
		if (detailsComposite.isVisible() != visible) {
			setVisibleRecursive(detailsComposite, visible);
		}
	}

	private void setVisibleRecursive(Control control, boolean visible) {
		control.setVisible(visible);

		if (control instanceof Composite) {
			for (Control subControl : ((Composite) control).getChildren()) {
				setVisibleRecursive(subControl, visible);
			}
		}
	}

	public void annotationAdded(Annotation annotation) {
		annotationsEventGroup.post(annotation);
	}

	public void annotationRemoved(Annotation annotation) {
		annotationsEventGroup.post(annotation);
	}

	public void annotationChanged(Annotation annotation) {
		annotationsEventGroup.post(annotation);
	}
	
	@Override
	public void commit(boolean onSave) {
		super.commit(onSave);
	}

	protected void markPageModified() {
		ArtifactEditorBase editor = (ArtifactEditorBase) getPage().getEditor();
		editor.pageModified();
	}

	@Override
	public void dispose() {
		manager().removeAnnotationListener(this);
		super.dispose();
	}
	
	@Override
	public void refresh() {
		viewer.refresh();
	}
	
	private static class AnnotationContentProvider implements IStructuredContentProvider {

		private static final Object[] EMPTY = new Object[0]; 
		
		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement == null) {
				return EMPTY;
			}
			return manager().getAnnotations(inputElement, false);
		}
	}
	
	private static class AnnotationLabelProvider extends LabelProvider {
		
		@Override
		public Image getImage(Object element) {
			Image image = DisplayAnnotationUtil.getImage((Annotation) element);
			if (image == null) {
				image = Images.getImage(Images.ANNOTATION);
			}
			return image;
		}
		
		@Override
		public String getText(Object element) {
			return DisplayAnnotationUtil.getText((Annotation) element);
		}
	}
	
	private static class PropertiesDialog extends Dialog {

		private final Annotation input;
		private EObject copy;

		protected PropertiesDialog(Shell parentShell, Annotation input) {
			super(parentShell);
			this.input = input;
			setShellStyle(getShellStyle() | RESIZE);
		}
		
		
		@Override
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText("Properties Editor for Annotation "
					+ DisplayAnnotationUtil.getText(input));
			newShell.setSize(600, 200);
		}
		
		@Override
		protected Control createDialogArea(Composite parent) {
			parent = (Composite) super.createDialogArea(parent);
			parent.setLayout(new FillLayout());
			PropertyTree propertyTree = new PropertyTree(new SelectionHandler() {
				
				public void setNull() {
				}
				
				public void set(EProperty object, TreeViewer viewer, boolean readOnly) {
				}
			});
			propertyTree.create(parent);
			copy = EcoreUtil.copy(input.getContent());
			propertyTree.setContent(copy, manager().isReadOnly(input));
			return parent;
		}

		@Override
		protected void okPressed() {
			InTransaction.run(new Operation() {
				
				public void run() throws Throwable {
					input.setContent(copy);
				}
			});
			super.okPressed();
		}
		
	}
}
