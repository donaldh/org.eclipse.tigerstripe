package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.TSRuntimeBasedWizardPage;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model.ModelImporter;

public class MappingWizardPage extends TSRuntimeBasedWizardPage {

	private static final String PAGE_NAME = "MappingWizardPage";
	private ModelImporter importer;
	
	// This viewer displays all imported classes/tables in a package
	// tree form.
	private TreeViewer umlClassesTreeViewer;
	private UmlClassesTreeContentProvider contentProvider;
	private UmlClassesTreeLabelProvider labelProvider;
	private Menu fContextMenu;
	
	/**
	 * Creates a new <code>NewPackageWizardPage</code>
	 */
	public MappingWizardPage() {
		super(PAGE_NAME);

		setTitle("Model mapping");
		setDescription("Please map the UML classes to Tigerstripe Artifact types.");

	}
	
	public void init() {

	}
	
	/**
	 * Perform any required update based on the runtime context
	 * 
	 */
	@Override
	protected void initFromContext() {
	}
	
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		int nColumns = 1;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createUmClassTreeControls(composite);

		setControl(composite);

		Dialog.applyDialogFont(composite);

		// this page is "always" complete - its up to the user when to go ahead
		setPageComplete(true);
	}
	
	protected void createUmClassTreeControls(Composite composite) {

		umlClassesTreeViewer = new TreeViewer(composite, SWT.MULTI | SWT.BORDER);
		GridData gd = new GridData(GridData.CENTER | GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.heightHint = 50;
		umlClassesTreeViewer.getTree().setLayoutData(gd);
		contentProvider = new UmlClassesTreeContentProvider();
		umlClassesTreeViewer.setContentProvider(contentProvider);
		labelProvider = new UmlClassesTreeLabelProvider();
		umlClassesTreeViewer.setLabelProvider(labelProvider);

		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		fContextMenu = menuMgr
				.createContextMenu(umlClassesTreeViewer.getTree());
		menuMgr
				.addMenuListener(new UmlClassesMenuListener(
						umlClassesTreeViewer));
		umlClassesTreeViewer.getTree().setMenu(fContextMenu);
	}
	

	protected void formWidgetSelected(SelectionEvent e) {
			umlClassesTreeViewer.refresh(true);
	}
	
	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		super.setVisible(visible);
		// When I go visible, try to get the model...
		UML2ImportDetailsWizardPage detailsPage = (UML2ImportDetailsWizardPage) getPreviousPage();
		if (importer == null){
			importer = new ModelImporter( detailsPage.getModelFilename(),
					detailsPage.getTsProject(), detailsPage.getProfilesFilename());
		}
		try {
			importer.doInitialLoad();
			importer.doMapping();
		} catch (Exception e){
			setErrorMessage("Failed to read from Model File");
			setPageComplete(false);
			e.printStackTrace();
			
		}
		umlClassesTreeViewer.setInput(importer.getClassMap());
	}

	public ModelImporter getImporter() {
		return this.importer;
	}

	
}
