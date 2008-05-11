package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.IStatusHandler;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.tigerstripe.annotation.ui.util.WorkbenchUtil;

/**
 * @author Yuri Strot
 *
 */
public class ValidationStatusHandler implements IStatusHandler {

	public Object handleStatus(IStatus status, Object source) throws CoreException {
		PropertySheet property = (PropertySheet)WorkbenchUtil.getPage().findView(PropertySheet.ID);
		if (property != null) {
			IStatusLineManager manager = property.getViewSite().getActionBars().getStatusLineManager();
			if (status.isOK()) {
				manager.setErrorMessage(null);
			}
			else {
				manager.setErrorMessage(status.getMessage());
			}
		}
	    return null;
    }

}
