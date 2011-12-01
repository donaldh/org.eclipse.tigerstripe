package org.eclipse.tigerstripe.workbench.ui.internal.search;

import org.eclipse.search.ui.ISearchPageScoreComputer;
import org.eclipse.tigerstripe.workbench.ui.internal.perspective.TigerstripePerspectiveFactory;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class TigerstripeSearchPageScoreComputer implements
		ISearchPageScoreComputer {

	public int computeScore(String id, Object element) {
		if (!TigerstripeSearchPage.EXTENSION_POINT_ID.equals(id)) {
			return ISearchPageScoreComputer.UNKNOWN;
		}

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			IPerspectiveDescriptor descriptor = page.getPerspective();
			if (descriptor != null
					&& TigerstripePerspectiveFactory.ID.equals(descriptor
							.getId())) {
				return 100;
			}
		}

		return ISearchPageScoreComputer.LOWEST;
	}
}
