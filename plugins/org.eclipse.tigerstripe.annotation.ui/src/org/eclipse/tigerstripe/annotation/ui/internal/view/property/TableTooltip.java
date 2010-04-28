package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public abstract class TableTooltip implements Listener {

	private Table table;
	private Shell tip;
	private Label label;

	public TableTooltip(Table table) {
		this.table = table;
		table.setToolTipText("");
		table.addListener(SWT.Dispose, this);
		table.addListener(SWT.KeyDown, this);
		table.addListener(SWT.MouseMove, this);
		table.addListener(SWT.MouseHover, this);
	}

	final Listener labelListener = new Listener() {
		public void handleEvent(Event event) {
			Label label = (Label) event.widget;
			Shell shell = label.getShell();
			switch (event.type) {
			case SWT.MouseDown:
				Event e = new Event();
				e.item = (TableItem) label.getData("_TABLEITEM");
				table.setSelection(new TableItem[] { (TableItem) e.item });
				table.notifyListeners(SWT.Selection, e);
				// fall through
			case SWT.MouseExit:
				shell.dispose();
				break;
			}
		}
	};

	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.Dispose:
		case SWT.KeyDown:
		case SWT.MouseMove: {
			if (tip == null)
				break;
			tip.dispose();
			tip = null;
			label = null;
			break;
		}
		case SWT.MouseHover: {
			TableItem item = table.getItem(new Point(event.x, event.y));
			if (item != null) {
				if (tip != null && !tip.isDisposed())
					tip.dispose();
				String tooltip = getTooltip(item);
				if (tooltip == null || tooltip.length() == 0)
					return;
				tip = new Shell(table.getShell(), SWT.ON_TOP | SWT.TOOL);
				tip.setLayout(new FillLayout());
				label = new Label(tip, SWT.NONE);
				label.setForeground(table.getDisplay().getSystemColor(
						SWT.COLOR_INFO_FOREGROUND));
				label.setBackground(table.getDisplay().getSystemColor(
						SWT.COLOR_INFO_BACKGROUND));
				label.setData("_TABLEITEM", item);
				label.setText(tooltip);
				label.addListener(SWT.MouseExit, labelListener);
				label.addListener(SWT.MouseDown, labelListener);
				Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				Point location = table.toDisplay(event.x, event.y + 20);
				tip.setLocation(location);
				tip.setSize(size);
				tip.setVisible(true);
			}
		}
		}
	}

	protected abstract String getTooltip(TableItem item);

}
