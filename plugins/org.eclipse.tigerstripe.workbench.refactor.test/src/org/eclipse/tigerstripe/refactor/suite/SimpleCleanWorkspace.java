package org.eclipse.tigerstripe.refactor.suite;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;

public class SimpleCleanWorkspace extends UITestCaseSWT {

	static void maximizeWorkbench() {
        Display.getDefault().syncExec(new Runnable()
        {
            public void run()
            {
                IWorkbench workbench = PlatformUI.getWorkbench();
                IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
                window.getShell().setMaximized(true);
            }
        });
}

	
	
	/**
	 * Main test method.
	 */
	public void testCleanWorkspace() throws Exception {
		maximizeWorkbench();
		IUIContext ui = getUI();
		ui.close(new CTabItemLocator("Welcome"));

		
	}
	
	
}