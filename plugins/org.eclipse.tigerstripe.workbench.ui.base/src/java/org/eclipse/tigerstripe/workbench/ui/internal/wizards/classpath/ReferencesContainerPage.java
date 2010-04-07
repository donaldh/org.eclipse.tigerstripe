package org.eclipse.tigerstripe.workbench.ui.internal.wizards.classpath;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.internal.core.classpath.IReferencesConstants;
import org.eclipse.tigerstripe.workbench.internal.core.classpath.ReferencesInitializer;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * This class represent classpath container page for tigerstripe model
 * references
 */
public class ReferencesContainerPage extends WizardPage implements
		IClasspathContainerPage, IClasspathContainerPageExtension {

	private IClasspathEntry entry;
	private TableViewer viewer;
	private Image projectImage;
	private Image libraryImage;
	private Image slibraryImage;
	private IClasspathEntry[] realEntries;
	private IJavaProject javaProject;

	class EntryLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getText(Object obj) {
			IClasspathEntry entry = (IClasspathEntry) obj;
			int kind = entry.getEntryKind();
			if (kind == IClasspathEntry.CPE_PROJECT)
				return entry.getPath().segment(0);
			IPath path = entry.getPath();
			String name = path.lastSegment();
			return name + " - " //$NON-NLS-1$
					+ path.uptoSegment(path.segmentCount() - 1).toOSString();
		}

		public Image getImage(Object obj) {
			IClasspathEntry entry = (IClasspathEntry) obj;
			int kind = entry.getEntryKind();
			if (kind == IClasspathEntry.CPE_PROJECT)
				return projectImage;
			else if (kind == IClasspathEntry.CPE_LIBRARY) {
				IPath sourceAtt = entry.getSourceAttachmentPath();
				return sourceAtt != null ? slibraryImage : libraryImage;
			}
			return null;
		}

		public String getColumnText(Object obj, int col) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int col) {
			return getImage(obj);
		}
	}

	class EntryContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object parent) {
			if (realEntries != null)
				return realEntries;
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	/**
	 * The constructor.
	 */
	public ReferencesContainerPage() {
		super("referencesContainerPage"); //$NON-NLS-1$
		setTitle(IReferencesConstants.CONTAINER_DESCRIPTION);
		setDescription("This read-only container dynamically manages the model project references");
		projectImage = PlatformUI.getWorkbench().getSharedImages().getImage(
				IDE.SharedImages.IMG_OBJ_PROJECT);
		libraryImage = JavaUI.getSharedImages().getImage(
				org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_EXTERNAL_ARCHIVE);
		slibraryImage = JavaUI
				.getSharedImages()
				.getImage(
						org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_EXTERNAL_ARCHIVE_WITH_SOURCE);
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @see WizardPage#createControl
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());
		Label label = new Label(container, SWT.NULL);
		label.setText("&Resolved classpath:");
		viewer = new TableViewer(container, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		viewer.setContentProvider(new EntryContentProvider());
		viewer.setLabelProvider(new EntryLabelProvider());
		viewer.setComparator(new ViewerComparator());

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 400;
		gd.heightHint = 300;
		viewer.getTable().setLayoutData(gd);

		setControl(container);
		Dialog.applyDialogFont(container);
		if (realEntries != null)
			initializeView();
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @see WizardPage#finish
	 */
	public boolean finish() {
		return true;
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @see WizardPage#getSelection
	 */
	public IClasspathEntry getSelection() {
		return entry;
	}

	public void initialize(IJavaProject project,
			IClasspathEntry[] currentEntries) {
		javaProject = project;
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @see WizardPage#setSelection
	 */
	public void setSelection(IClasspathEntry containerEntry) {
		this.entry = containerEntry;
		createRealEntries();
		if (viewer != null)
			initializeView();
	}

	private void createRealEntries() {
		IJavaProject javaProject = getJavaProject();
		if (javaProject == null) {
			realEntries = new IClasspathEntry[0];
			return;
		}

		try {
			if (entry == null) {
				entry = JavaCore
						.newContainerEntry(IReferencesConstants.REFERENCES_CONTAINER_PATH);
				IClasspathContainer container = ReferencesInitializer
						.createContainer(javaProject);
				if (container != null)
					realEntries = container.getClasspathEntries();
			} else {
				IClasspathContainer container = JavaCore.getClasspathContainer(
						entry.getPath(), javaProject);
				if (container != null)
					realEntries = container.getClasspathEntries();
			}
		} catch (Exception e) {
		}
		if (realEntries == null)
			realEntries = new IClasspathEntry[0];
	}

	private IJavaProject getJavaProject() {
		return javaProject;
	}

	private void initializeView() {
		viewer.setInput(entry);
	}
}
