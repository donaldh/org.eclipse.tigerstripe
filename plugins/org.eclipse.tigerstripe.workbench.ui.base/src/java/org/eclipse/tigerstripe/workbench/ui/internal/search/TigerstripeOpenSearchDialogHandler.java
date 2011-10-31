package org.eclipse.tigerstripe.workbench.ui.internal.search;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class TigerstripeOpenSearchDialogHandler extends AbstractHandler  {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		NewSearchUI.openSearchDialog(HandlerUtil.getActiveWorkbenchWindow(event), TigerstripeSearchPage.EXTENSION_POINT_ID);
		return null;
	}
}
