package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.viewsupport.IProblemChangedListener;
import org.eclipse.jdt.internal.ui.viewsupport.ProblemMarkerManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.WeakRestart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.INavigatorContentExtension;

@SuppressWarnings("restriction")
public class TSExplorer extends CommonNavigator {

	private WeakRestart weakRestart = WeakRestart.INSTANCE;

	@Override
	protected CommonViewer createCommonViewer(Composite aParent) {
		CommonViewer viewer = super.createCommonViewer(aParent);
		viewer.setComparer(new TSExplorerElementComparer());
		addProblemListener();
		addWeakRestartListener();
		return viewer;
	}

	@Override
	public String getFrameToolTipText(Object anElement) {
		return "Tigerstripe Explorer";
	}

	public TigerstripeContentProvider findContentProvider() {
		return (TigerstripeContentProvider) getContentExtension()
				.getContentProvider();
	}

	public TigerstripeLabelProvider findLabelProvider() {
		return (TigerstripeLabelProvider) getContentExtension()
				.getLabelProvider();
	}

	public INavigatorContentExtension getContentExtension() {
		return getCommonViewer()
				.getNavigatorContentService()
				.getContentExtensionById(
						"org.eclipse.tigerstripe.workbench.ui.explorer.tigerstripeContent");
	}

	@Override
	protected void handleDoubleClick(DoubleClickEvent anEvent) {

		if (!(anEvent.getSelection() instanceof ITreeSelection)) {
			super.handleDoubleClick(anEvent);
			return;
		}

		IAction openHandler = getViewSite().getActionBars()
				.getGlobalActionHandler(ICommonActionConstants.OPEN);

		if (openHandler == null || !openHandler.isEnabled()) {
			ITreeSelection selection = (ITreeSelection) anEvent.getSelection();

			TreePath[] paths = selection.getPaths();

			if (paths.length != 0) {
				TreeViewer viewer = getCommonViewer();
				if (viewer.isExpandable(paths[0])) {
					viewer.setExpandedState(paths[0],
							!viewer.getExpandedState(paths[0]));
				}
			}
		}
	}

	protected void refreshContent() {
		CommonViewer viewer = getCommonViewer();
		viewer.getControl().setRedraw(false);
		Object[] expandedObjects = viewer.getExpandedElements();
		viewer.setInput(viewer.getInput());
		viewer.setExpandedElements(expandedObjects);
		viewer.getControl().setRedraw(true);
	}

	private IProblemChangedListener problemChangedListener;

	private ProblemMarkerManager getProblemMarkerManager() {
		return JavaPlugin.getDefault().getProblemMarkerManager();
	}

	protected void addProblemListener() {
		if (problemChangedListener == null) {
			problemChangedListener = new IProblemChangedListener() {
				public void problemsChanged(final IResource[] changedResources,
						boolean isMarkerChange) {
					refreshContent();
				}
			};
			getProblemMarkerManager().addListener(problemChangedListener);
		}
	}

	protected void removeProblemListener() {
		if (problemChangedListener != null) {
			getProblemMarkerManager().removeListener(problemChangedListener);
			problemChangedListener = null;
		}
	}

	private WeakRestart.Listener weakRestartListener;

	protected void addWeakRestartListener() {
		if (weakRestartListener == null) {
			weakRestartListener = new WeakRestart.Listener() {
				public void updated() {
					PlatformUI.getWorkbench().getDisplay()
							.syncExec(new Runnable() {
								public void run() {
									refreshContent();
								}
							});
				}
			};
			weakRestart.addListener(weakRestartListener);
		}
	}

	protected void removeWeakRestartListener() {
		if (weakRestartListener != null) {
			weakRestart.removeListener(weakRestartListener);
			weakRestartListener = null;
		}
	}

	@Override
	public void dispose() {
		removeWeakRestartListener();
		removeProblemListener();
		super.dispose();
	}

}
