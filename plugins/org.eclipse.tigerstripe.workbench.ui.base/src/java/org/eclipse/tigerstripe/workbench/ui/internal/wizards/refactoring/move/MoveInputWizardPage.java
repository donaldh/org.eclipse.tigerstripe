package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.move;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.model.WorkbenchViewerComparator;

public class MoveInputWizardPage extends WizardPage {

	private IAbstractArtifact artifact;

	private TreeViewer fDestinationField;

	protected MoveInputWizardPage() {
		super("MoveWizardPage1");
	}

	public void createControl(Composite parent) {

		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setFont(parent.getFont());

		Label label = new Label(composite, SWT.NONE);
		label.setText("Choose destination for '" + artifact.getName() + ": ");
		label.setLayoutData(new GridData());

		fDestinationField = new TreeViewer(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
		gd.widthHint = convertWidthInCharsToPixels(40);
		gd.heightHint = convertHeightInCharsToPixels(15);
		fDestinationField.getTree().setLayoutData(gd);
		fDestinationField.setLabelProvider(new WorkbenchLabelProvider());
		fDestinationField.setContentProvider(new BaseWorkbenchContentProvider());
		fDestinationField.setComparator(new WorkbenchViewerComparator());
		fDestinationField.setInput(ResourcesPlugin.getWorkspace());
		fDestinationField.addFilter(new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IProject) {
					IProject project = (IProject) element;
					return project.isAccessible();
				} else if (element instanceof IFolder) {
					return true;
				}
				return false;
			}
		});
		fDestinationField.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				validatePage();
			}
		});

		setPageComplete(false);
		setControl(composite);

	}

	// Need to harden this...
	private final void validatePage() {

		IStructuredSelection selection = (IStructuredSelection) fDestinationField.getSelection();
		
		setPageComplete(true);
	}

	public void init(IStructuredSelection selection) {

		if (selection == null)
			return;

		if (selection.size() > 0) {

			Object obj = selection.getFirstElement();
			if (obj instanceof IJavaElement) {

				IJavaElement element = (IJavaElement) obj;
				IAbstractArtifact artifact = (IAbstractArtifact) element.getAdapter(IAbstractArtifact.class);
				if (artifact != null) {
					this.artifact = artifact;
				}
			}
		}
	}

}
