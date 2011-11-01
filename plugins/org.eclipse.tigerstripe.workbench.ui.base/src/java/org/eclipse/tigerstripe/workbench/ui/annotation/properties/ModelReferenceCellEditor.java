package org.eclipse.tigerstripe.workbench.ui.annotation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference;
import org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReferenceFactory;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.dialogs.PatternFilter;

/**
 * Cell editor for IModelComponent annotations' attributes.
 * 
 * Allows customizations by overriding protected methods.
 */
public class ModelReferenceCellEditor extends DialogCellEditor {
	private static final Object[] NO_CHILDREN = new Object[0];

	protected class ModelComponentsContentProvider implements
			ITreeContentProvider {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof ITigerstripeModelProject) {
				try {
					final ITigerstripeModelProject project = (ITigerstripeModelProject) parentElement;
					final IArtifactManagerSession session = project
							.getArtifactManagerSession();

					final IQueryAllArtifacts query = (IQueryAllArtifacts) session
							.makeQuery(IQueryAllArtifacts.class.getName());
					return getVisible(session.queryArtifact(query)).toArray();
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
					return NO_CHILDREN;
				}
			} else if (parentElement instanceof IAbstractArtifact
					&& !(parentElement instanceof IPackageArtifact)) {
				return getVisible(
						((IAbstractArtifact) parentElement)
								.getContainedModelComponents()).toArray();
			} else {
				return NO_CHILDREN;
			}
		}

		private List<Object> getVisible(Collection<? extends Object> objects) {
			List<Object> result = new ArrayList<Object>();
			for (final Object artifact : objects) {
				if (isElementVisible(artifact)) {
					result.add(artifact);
				}
			}
			return result;
		}

		public Object getParent(Object element) {
			if (element instanceof IModelComponent) {
				((IModelComponent) element).getContainingModelComponent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

	private static final Status OK = new Status(IStatus.OK,
			EclipsePlugin.PLUGIN_ID, "");

	// unused in the base class, reserved for customizations
	@SuppressWarnings("unused")
	private EStructuralFeature feature;

	private IModelComponent annotatedObject;
	private IModelComponent[] initialSelection;
	private ILabelProvider labelProvider;

	private String title = "Select Model Component";
	private String message = "Select model component";

	private List<Class<?>> visibleClasses = null;
	private List<Class<?>> validClasses = null;

	public void setFeature(final EStructuralFeature feature) {
		this.feature = feature;
	}

	public void setAnnotatedObject(IModelComponent annotatedObject) {
		this.annotatedObject = annotatedObject;
	}

	protected IModelComponent getAnnotatedObject() {
		return annotatedObject;
	}

	public static final IModelComponent[] NO_COMPONENTS = new IModelComponent[0];

	public void setInitialSelection(Object initialSelection) {
		this.initialSelection = valueToComponents(initialSelection);
	}

	/**
	 * Converts model value to a viewer selection
	 * 
	 * @param value
	 * @return
	 */
	protected IModelComponent[] valueToComponents(final Object value) {
		if (value instanceof ModelReference) {
			final IModelComponent component = ((ModelReference) value)
					.resolve();
			return component != null ? new IModelComponent[] { component }
					: NO_COMPONENTS;
		} else {
			return NO_COMPONENTS;
		}
	}

	/**
	 * Converts viewer selection to the model value
	 * 
	 * @param components
	 * @return
	 */
	protected Object componentsToValue(final IModelComponent[] components) {
		return components.length > 0 ? componentToValue(components[0]) : null;
	}

	/**
	 * Converts a single object from the viewer selection to the model value
	 * 
	 * @param component
	 * @return
	 */
	protected Object componentToValue(final IModelComponent component) {
		try {
			final URI uri = TigerstripeURIAdapterFactory.toURI(component);
			final ModelReference reference = ModelReferenceFactory.eINSTANCE
					.createModelReference();
			reference.setUri(uri.toString());
			return reference;
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			return null;
		}
	}

	/**
	 * Override to customize dialog title
	 */
	protected String getTitle() {
		return title;
	}

	/**
	 * Override to customize dialog message
	 */
	protected String getMessage() {
		return message;
	}

	/**
	 * Override to customize tree label provider
	 */
	protected ILabelProvider createLabelProvider() {
		return new ModelComponentsLabelProvider();
	}

	/**
	 * Override to customize tree content provider
	 */
	protected ITreeContentProvider createContentProvider() {
		return new ModelComponentsContentProvider();
	}

	/**
	 * Override to customize selection validator. Use only if overriding
	 * {@link #isElementValid(Object)} is not enough.
	 */
	protected ISelectionStatusValidator createSelectionValidator() {
		return new ISelectionStatusValidator() {

			public IStatus validate(Object[] selection) {
				if (selection.length > 1) {
					return error("Multiple selection not allowed");
				}

				for (final Object o : selection) {
					if (!isElementValid(o)) {
						return error("Invalid selection: "
								+ ((IModelComponent) o).getName());
					}
				}
				return OK;
			}
		};
	}

	private IStatus error(final String message) {
		return new Status(IStatus.ERROR, EclipsePlugin.PLUGIN_ID, message);
	}

	/**
	 * Use this to perform any addition configuration on viewer or its widget.
	 * For example, creating custom viewer filter.
	 * 
	 * @param viewer
	 */
	protected void configureViewer(TreeViewer viewer) {
	}

	private boolean ofClass(final List<Class<?>> classes, final Object o,
			final boolean defaultValue) {
		if (classes == null) {
			return defaultValue;
		}

		for (final Class<?> clazz : classes) {
			if (clazz.isAssignableFrom(o.getClass())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		if (annotatedObject == null) {
			return null;
		}
		ElementTreeSelectionDialog selectionDialog = createDialog(cellEditorWindow);
		selectionDialog.setTitle(getTitle());
		selectionDialog.setMessage(getMessage());
		selectionDialog.setValidator(createSelectionValidator());

		// actual validation will be handled by dialog validator
		selectionDialog.setAllowMultiple(true);

		try {
			selectionDialog.setInput(annotatedObject.getProject());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		if (initialSelection != null) {
			selectionDialog.setInitialSelections(initialSelection);
		}
		selectionDialog.setValidator(createSelectionValidator());
		if (selectionDialog.open() == Window.OK) {
			final IModelComponent[] components = new IModelComponent[selectionDialog
					.getResult().length];
			System.arraycopy(selectionDialog.getResult(), 0, components, 0,
					components.length);
			return componentsToValue(components);
		}
		return null;
	}

	/**
	 * Override to provide custom implementation of the dialog.
	 * {@link #configureViewer(TreeViewer)} hook will not be called
	 * automatically then.
	 * 
	 * @param cellEditorWindow
	 * @return
	 */
	protected ElementTreeSelectionDialog createDialog(
			Control cellEditorWindow) {
		return new ModelReferenceSelectionDialog(cellEditorWindow.getShell(),
				getLabelProvider(), createContentProvider());
	}

	private class ModelReferenceSelectionDialog extends
			ElementTreeSelectionDialog {

		public ModelReferenceSelectionDialog(Shell parent,
				ILabelProvider labelProvider,
				ITreeContentProvider contentProvider) {
			super(parent, labelProvider, contentProvider);
		}

		@Override
		protected TreeViewer doCreateTreeViewer(Composite parent, int style) {
			FilteredTree tree = new FilteredTree(parent, style,
					new PatternFilter(), true);
			tree.setLayoutData(new GridData(GridData.FILL_BOTH));
			applyDialogFont(tree);
			configureViewer(tree.getViewer());
			return tree.getViewer();
		}
	}

	@Override
	protected void updateContents(Object object) {
		if (getDefaultLabel() != null && getLabelProvider() != null) {
			getDefaultLabel().setText(getLabelProvider().getText(object));
		}
	}

	protected ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = createLabelProvider();
		}
		return labelProvider;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public void setVisibleClasses(List<Class<?>> visibleClasses) {
		this.visibleClasses = visibleClasses;
	}

	public void setValidClasses(List<Class<?>> validClasses) {
		this.validClasses = validClasses;
	}

	/**
	 * Override to customize element visibility
	 * 
	 * @param element
	 * @return
	 */
	protected boolean isElementVisible(final Object element) {
		return ofClass(visibleClasses, element, true);
	}

	/**
	 * Override to customize element validness
	 * 
	 * @param element
	 * @return
	 */
	protected boolean isElementValid(final Object element) {
		return ofClass(validClasses, element, true);
	}
}
