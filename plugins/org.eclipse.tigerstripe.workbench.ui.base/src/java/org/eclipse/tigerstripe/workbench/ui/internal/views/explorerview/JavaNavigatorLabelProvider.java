package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.navigator.IExtensionStateConstants.Values;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerContentProvider;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.eclipse.ui.navigator.IExtensionStateModel;

/**
 * Provides the labels for the Project Explorer.
 * <p>
 * It provides labels for the packages in hierarchical layout and in all other
 * cases delegates it to its super class.
 * </p>
 *
 * @since 3.2
 */
public class JavaNavigatorLabelProvider implements ICommonLabelProvider, IStyledLabelProvider {

	private final long LABEL_FLAGS = JavaElementLabels.DEFAULT_QUALIFIED
			| JavaElementLabels.ROOT_POST_QUALIFIED
			| JavaElementLabels.APPEND_ROOT_PATH
			| JavaElementLabels.M_PARAMETER_TYPES
			| JavaElementLabels.M_PARAMETER_NAMES
			| JavaElementLabels.M_APP_RETURNTYPE
			| JavaElementLabels.M_EXCEPTIONS
			| JavaElementLabels.F_APP_TYPE_SIGNATURE
			| JavaElementLabels.T_TYPE_PARAMETERS;

	private CustomPackageExplorerLabelProvider delegeteLabelProvider;

	private PackageExplorerContentProvider fContentProvider;

	private IExtensionStateModel fStateModel;

	private IPropertyChangeListener fLayoutPropertyListener;

	public JavaNavigatorLabelProvider() {

	}
	public void init(ICommonContentExtensionSite commonContentExtensionSite) {
		fStateModel = commonContentExtensionSite.getExtensionStateModel();
		init((PackageExplorerContentProvider) commonContentExtensionSite
				.getExtension().getContentProvider());
		delegeteLabelProvider.setIsFlatLayout(fStateModel
				.getBooleanProperty(Values.IS_LAYOUT_FLAT));
		fLayoutPropertyListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (Values.IS_LAYOUT_FLAT.equals(event.getProperty())) {
					if (event.getNewValue() != null) {
						boolean newValue = ((Boolean) event.getNewValue())
								.booleanValue() ? true : false;
						delegeteLabelProvider.setIsFlatLayout(newValue);
					}
				}

			}
		};
		fStateModel.addPropertyChangeListener(fLayoutPropertyListener);
	}

	public void init(PackageExplorerContentProvider contentProvider) {
		fContentProvider = contentProvider;
		delegeteLabelProvider = createLabelProvider();
	}
	
	public String getDescription(Object element) {
		return formatMessage(element);
	}

	private CustomPackageExplorerLabelProvider createLabelProvider() {
		return new CustomPackageExplorerLabelProvider(fContentProvider);
	}

	public void dispose() {
		if (delegeteLabelProvider != null) {
			delegeteLabelProvider.dispose();
		}
		if (fStateModel != null) {
			fStateModel.removePropertyChangeListener(fLayoutPropertyListener);
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (delegeteLabelProvider == null) {
			return;
		}
		delegeteLabelProvider.propertyChange(event);
	}

	public void addLabelDecorator(ILabelDecorator decorator) {
		if (delegeteLabelProvider == null) {
			return;
		}
		delegeteLabelProvider.addLabelDecorator(decorator);
	}

	public void addListener(ILabelProviderListener listener) {
		if (delegeteLabelProvider == null) {
			return;
		}
		delegeteLabelProvider.addListener(listener);
	}

	public Color getBackground(Object element) {
		if (delegeteLabelProvider == null) {
			return null;
		}
		return delegeteLabelProvider.getBackground(element);
	}

	public Color getForeground(Object element) {
		if (delegeteLabelProvider == null) {
			return null;
		}
		return delegeteLabelProvider.getForeground(element);
	}

	public Image getImage(Object element) {
		if (delegeteLabelProvider == null) {
			return null;
		}
		return delegeteLabelProvider.getImage(element);
	}

	public boolean isLabelProperty(Object element, String property) {
		if (delegeteLabelProvider == null) {
			return false;
		}
		return delegeteLabelProvider.isLabelProperty(element, property);
	}

	public void removeListener(ILabelProviderListener listener) {
		if (delegeteLabelProvider == null) {
			return;
		}
		delegeteLabelProvider.removeListener(listener);
	}

	public boolean equals(Object obj) {
		if (delegeteLabelProvider == null) {
			return super.equals(obj);
		}
		return delegeteLabelProvider.equals(obj);
	}

	public int hashCode() {
		if (delegeteLabelProvider == null) {
			return super.hashCode();
		}
		return delegeteLabelProvider.hashCode();
	}

	public String toString() {
		if (delegeteLabelProvider == null) {
			return super.toString();
		}
		return delegeteLabelProvider.toString();
	}

	public String getText(Object element) {
		if (delegeteLabelProvider == null) {
			return "";
		}
		return delegeteLabelProvider.getText(element);
	}

	public StyledString getStyledText(Object element) {
		if (delegeteLabelProvider == null) {
			return new StyledString("");
		}
		return delegeteLabelProvider.getStyledText(element);
	}

	public void setIsFlatLayout(boolean state) {
		if (delegeteLabelProvider == null) {
			return;
		}
		delegeteLabelProvider.setIsFlatLayout(state);
	}

	// Taken from StatusBarUpdater

	private String formatMessage(Object element) {
		if (element instanceof IJavaElement) {
			return formatJavaElementMessage((IJavaElement) element);
		} else if (element instanceof IResource) {
			return formatResourceMessage((IResource) element);
		}
		return ""; //$NON-NLS-1$
	}

	private String formatJavaElementMessage(IJavaElement element) {
		return JavaElementLabels.getElementLabel(element, LABEL_FLAGS);
	}

	private String formatResourceMessage(IResource element) {
		IContainer parent = element.getParent();
		if (parent != null && parent.getType() != IResource.ROOT)
			return BasicElementLabels.getResourceName(element.getName()) + JavaElementLabels.CONCAT_STRING
					+ BasicElementLabels.getPathLabel(parent.getFullPath(), false);
		else
			return BasicElementLabels.getResourceName(element.getName());
	}

	public void restoreState(IMemento memento) {

	}

	public void saveState(IMemento memento) {

	}

	public CustomPackageExplorerLabelProvider getDelegeteLabelProvider() {
		return delegeteLabelProvider;
	}
}
