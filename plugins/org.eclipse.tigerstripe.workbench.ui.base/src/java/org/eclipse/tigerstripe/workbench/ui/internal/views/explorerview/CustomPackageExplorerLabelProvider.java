package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.jdt.internal.ui.packageview.PackageExplorerContentProvider;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerLabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.ui.internal.viewers.TigerstripeDecoratorManager;
import org.eclipse.tigerstripe.workbench.ui.viewers.ITigerstripeLabelDecorator;

@SuppressWarnings("restriction")
public class CustomPackageExplorerLabelProvider extends
		PackageExplorerLabelProvider {

	public CustomPackageExplorerLabelProvider(PackageExplorerContentProvider cp) {
		super(cp);
		for (ITigerstripeLabelDecorator decorator : TigerstripeDecoratorManager
				.getDefault().getDecorators()) {
			addLabelDecorator(decorator);
		}
	}

	@Override
	public void fireLabelProviderChanged(LabelProviderChangedEvent event) {
		super.fireLabelProviderChanged(event);
	}

	@Override
	public Image decorateImage(Image image, Object element) {
		return super.decorateImage(image, element);
	}

	@Override
	public String decorateText(String text, Object element) {
		return super.decorateText(text, element);
	}

}
