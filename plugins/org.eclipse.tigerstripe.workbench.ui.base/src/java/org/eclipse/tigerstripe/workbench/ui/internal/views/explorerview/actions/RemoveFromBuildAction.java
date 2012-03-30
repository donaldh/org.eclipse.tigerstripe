package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.ExcludeArtifactService;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.utils.AdaptHelper;

public class RemoveFromBuildAction extends Action implements
		ISelectionChangedListener {

	private IAbstractArtifact selected;
	private boolean excluded;

	@Override
	public void run() {
		if (selected == null) {
			return;
		}
		if (excluded) {
			ExcludeArtifactService.include(selected);
			excluded = false;
		} else {
			ExcludeArtifactService.exclude(selected);
			excluded = true;
		}
		updateLabel();
	}

	public void selectionChanged(SelectionChangedEvent event) {
		selected = null;
		try {
			ISelection selection = event.getSelection();
			if (selection instanceof IStructuredSelection) {
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				if (obj instanceof IAdaptable) {
					selected = AdaptHelper.adapt((IAdaptable) obj,
							IAbstractArtifact.class);
					if (selected != null) {
						Set<String> excludes = ExcludeArtifactService
								.getExcluded(selected.getProject());
						excluded = excludes.contains(selected
								.getFullyQualifiedName());
						updateLabel();
					}
				}
			}
		} catch (Exception e) {
			BasePlugin.log(e);
		}
		setEnabled(selected != null);
	}

	private void updateLabel() {
		if (excluded) {
			setIncludeText();
		} else {
			setExcludeText();
		}
	}

	public void setExcludeText() {
		setText("Ignore Build Errors");
	}

	public void setIncludeText() {
		setText("Show Build Errors");
	}

}
