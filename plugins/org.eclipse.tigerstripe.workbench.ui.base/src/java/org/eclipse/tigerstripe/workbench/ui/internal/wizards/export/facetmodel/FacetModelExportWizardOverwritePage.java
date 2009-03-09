package org.eclipse.tigerstripe.workbench.ui.internal.wizards.export.facetmodel;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.facets.FacetModelExportInputManager;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.AbstractArtifactLabelProvider;

public class FacetModelExportWizardOverwritePage extends WizardPage {

	public static final String WIZARD_PAGE_NAME = "EXPORT_WIZARD_OVERWRITE";

	private FacetModelExportInputManager inputManager;

	private TableViewer tableViewer;

	protected FacetModelExportWizardOverwritePage() {

		super(WIZARD_PAGE_NAME);
		setTitle("Facet Scoped Model Export");
		setDescription("Verify changes below and complete the export.");
	}

	public void createControl(Composite parent) {

		inputManager = ((FacetModelExportWizard) getWizard()).getInputManager();

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		new Label(composite, SWT.LEFT).setText("The following files will be overwritten:");

		Table table = new Table(composite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new AbstractArtifactLabelProvider());
		tableViewer.setFilters(new ViewerFilter[]{ new TigerstripePackageViewerFilter()});
		tableViewer.setSorter(new ViewerSorter());

		setControl(composite);
		
	}

	public void initialize() {
		
		tableViewer.setInput(inputManager.getOverwrites());
		setPageComplete(true);
	}

	private final class TigerstripePackageViewerFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {

			if (element instanceof IPackageArtifact) {
				return false;
			}
			return true;
		}
	}

}
