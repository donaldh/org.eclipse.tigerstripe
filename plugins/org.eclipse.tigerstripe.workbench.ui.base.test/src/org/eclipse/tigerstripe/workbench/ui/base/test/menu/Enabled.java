package org.eclipse.tigerstripe.workbench.ui.base.test.menu;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

import abbot.tester.swt.MenuItemTester;
import abbot.tester.swt.MenuTester;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.WidgetReference;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;


public class Enabled extends UITestCaseSWT{

	
    /**
     * Click an item in a menu.
     * @param ui The <code>IUIContext</code> to perform ui actions.
     * @param pathToMenu
     * @param menuItemName
     * @param mustBeEnabled
     * @throws WidgetSearchException
     */
    public static void clickMenuItem(final IUIContext ui, final String pathToMenu, final String menuItemName,
            final boolean mustBeEnabled) throws WidgetSearchException {
        MenuItemTester menuItemTester = new MenuItemTester();
        MenuItem menuItem = (MenuItem) getWidget(ui, new SWTWidgetLocator(MenuItem.class,
                pathToMenu));
        Menu menu = menuItemTester.getMenu(menuItem);

        String pathToMenuItem = pathToMenu + "/" + menuItemName;
        MenuTester menuTester = new MenuTester();
        MenuItem[] items = menuTester.getItems(menu);
        for (int i = 0; i < items.length; ++i) {
            MenuItem item = items[i];
            if (menuItemTester.getText(item).equals(menuItemName)) {
                boolean isEnabled = menuItemTester.getEnabled(item);
                if (mustBeEnabled) {
                    if (!isEnabled) {
                       fail("Menu item '" + pathToMenuItem + "' is not enabled");
                    }
                }
                if (isEnabled) {
                    MenuItemLocator menuLocator = new MenuItemLocator(pathToMenuItem);
                    ui.click(menuLocator);
                }
                break;
            }
        }

    }





/**
     * Returns the widget for the corresponding locator.
     * @param locator
     * @throws WidgetSearchException
     */
    public static Widget getWidget(final IUIContext ui, IWidgetLocator locator) throws WidgetSearchException {
    	IWidgetLocator loc = ui.find(locator);
        if (loc instanceof WidgetReference) {
            WidgetReference reference = (WidgetReference) loc;
            final Object widget = reference.getWidget();
            if (widget instanceof Widget) {
                return (Widget) widget;
            }
        }
        return null;
    }

}
	

