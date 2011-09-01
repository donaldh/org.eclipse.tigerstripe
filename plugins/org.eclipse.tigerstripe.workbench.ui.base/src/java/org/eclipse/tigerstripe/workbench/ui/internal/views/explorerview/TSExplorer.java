package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.ICommonActionConstants;

public class TSExplorer extends CommonNavigator {
	private static TSExplorer instance;
	
	public static TSExplorer getInstance() {
		return instance;
	}
	
	private final TigerstripeContentProvider contentProvider;
	private final TigerstripeLabelProvider labelProvider;
	
	public TSExplorer() {
		super();
		contentProvider = new TigerstripeContentProvider();
		labelProvider = new TigerstripeLabelProvider(contentProvider);
		instance = this;
	}
	
	public TigerstripeContentProvider getContentProvider() {
		return contentProvider;
	}
	
	public TigerstripeLabelProvider getLabelProvider() {
		return labelProvider;
	}

	@Override
	public String getFrameToolTipText(Object anElement) {
		return "Tigerstripe Explorer";
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
}
