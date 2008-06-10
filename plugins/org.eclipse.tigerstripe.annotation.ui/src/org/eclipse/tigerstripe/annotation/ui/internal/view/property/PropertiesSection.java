/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;
import org.eclipse.tigerstripe.annotation.ui.internal.view.IProperty;
import org.eclipse.tigerstripe.annotation.ui.internal.view.PropertyContentProvider;
import org.eclipse.tigerstripe.annotation.ui.internal.view.PropertyLabelProvider;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author Yuri Strot
 *
 */
public class PropertiesSection extends AbstractPropertySection {

	private Tree table;
	private TreeEditor editor;
	private TreeViewer viewer;
	private CellEditor cellEditor;
    private ICellEditorListener editorListener;
	
    private static String[] columnLabels = { "Property", "Value" };

    // Cell editor support.
    private int columnToEdit = 1;
	  
    /* (non-Javadoc)
     * @see org.eclipse.ui.views.properties.tabbed.ISection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
     */
    public void createControls(final Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = getWidgetFactory()
                .createFlatFormComposite(parent);
        FormData data = null;

        String tableLabelStr = null;
        CLabel tableLabel = null;
        if (tableLabelStr != null && tableLabelStr.length() > 0) {
            tableLabel = getWidgetFactory().createCLabel(composite,
                    tableLabelStr);
            data = new FormData();
            data.left = new FormAttachment(0, 0);
            data.top = new FormAttachment(0, 0);
            tableLabel.setLayoutData(data);
        }
        
		createProperties(composite);

        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        if (tableLabel == null) {
            data.top = new FormAttachment(0, 0);
        } else {
            data.top = new FormAttachment(tableLabel, 0, SWT.BOTTOM);
        }
        data.bottom = new FormAttachment(100, 0);
        data.height = 100;
        data.width = 100;
        
        table.setLayoutData(data);
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
    
    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        updateView(selection);
    }
	
	protected void updateView() {
		ISelection selection = AnnotationUIPlugin.getManager().getSelection();
		if (selection != null)
			updateView(selection);
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		updateView(selection);
	}
	
	protected void updateView(final ISelection selection) {
		Annotation annotation = getAnnotation(selection);
		if (annotation != null) {
			viewer.setContentProvider(new PropertyContentProvider());
			viewer.setInput(annotation.getContent());
		}
		else {
			viewer.setInput(null);
		}
	}
	
	private Annotation getAnnotation(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			return (Annotation )((IStructuredSelection)selection).getFirstElement();
		}
		return null;
	}
	
	private void createProperties(Composite parent) {
		table = new Tree(parent, SWT.FULL_SELECTION | SWT.SINGLE | SWT.HIDE_SELECTION | SWT.BORDER);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		viewer = new TreeViewer(table);
		viewer.setLabelProvider(new PropertyLabelProvider());
		
		// configure the columns
		addColumns();
		
		// add our listeners to the widget
		hookControl();
		
		// create a new tree editor
		editor = new TreeEditor(table);
		
		// create the entry and editor listener
		createEditorListener();
	}

    /**
     * Creates a new cell editor listener.
     */
    private void createEditorListener() {
        editorListener = new ICellEditorListener() {
            public void cancelEditor() {
                deactivateCellEditor();
            }

            public void editorValueChanged(boolean oldValidState, boolean newValidState) {
                //Do nothing
            }

            public void applyEditorValue() {
            	PropertiesSection.this.applyEditorValue();
                deactivateCellEditor();
            }
        };
    }

    /**
     * Establish this viewer as a listener on the control
     */
    private void hookControl() {
        // Handle selections in the Tree
        // Part1: Double click only (allow traversal via keyboard without
        // activation
        table.addSelectionListener(new SelectionAdapter() {
            /* (non-Javadoc)
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetSelected(SelectionEvent e) {
            	// The viewer only owns the status line when there is
            	// no 'active' cell editor
            	if (cellEditor == null || !cellEditor.isActivated()) {
				}
			}

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
                //handleSelect((TreeItem) e.item);
            }
        });
        // Part2: handle single click activation of cell editor
        table.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                // only activate if there is a cell editor
                Point pt = new Point(event.x, event.y);
                TreeItem item = table.getItem(pt);
                if (item != null) {
                    int x = item.getBounds().x;
                    if (table.getColumnCount() > 0) {
                    	int width = table.getColumn(0).getWidth();
                    	if (width + x > event.x)
                    		return;
                    }
                    handleSelect(item);
                }
            }
        });

        // Refresh the tree when F5 pressed
        table.addKeyListener(new KeyAdapter() {
        	
        	/* (non-Javadoc)
        	 * @see org.eclipse.swt.events.KeyAdapter#keyPressed(org.eclipse.swt.events.KeyEvent)
        	 */
        	public void keyPressed(KeyEvent e) {
        		 if (e.character == SWT.CR && cellEditor == null) {
 					TreeItem[] items = table.getSelection();
 					if (items != null && items.length > 0) {
 						handleSelect(items[0]);
 					}
 				}
        	}
        	
            public void keyReleased(KeyEvent e) {
                if (e.character == SWT.ESC) {
					deactivateCellEditor();
				} else if (e.keyCode == SWT.F5) {
					// The following will simulate a reselect
                    viewer.setInput(viewer.getInput());
				}
            }
        });
    }

    /**
     * Asks the entry currently being edited to apply its current cell editor
     * value.
     */
    private void applyEditorValue() {
    	TreeItem treeItem = editor.getItem();
        // treeItem can be null when view is opened
        if (treeItem == null || treeItem.isDisposed()) {
			return;
		}
        IProperty entry = (IProperty) treeItem.getData();
        entry.applyEditorValue();
        viewer.refresh(entry);
    }

    /**
     * Selection in the viewer occurred. Check if there is an active cell
     * editor. If yes, deactivate it and check if a new cell editor must be
     * activated.
     * 
     * @param selection
     *            the TreeItem that is selected
     */
    private void handleSelect(TreeItem selection) {
        // deactivate the current cell editor
        if (cellEditor != null) {
            applyEditorValue();
            deactivateCellEditor();
        }

        // get the new selection
        TreeItem[] sel = new TreeItem[] { selection };
        if (sel.length == 0) {
//            setMessage(null);
//            setErrorMessage(null);
        } else {
            Object object = sel[0].getData(); // assume single selection
            if (object instanceof IProperty) {
                // get the entry for this item
            	//IProperty activeEntry = (IProperty) object;
                // activate a cell editor on the selection
                activateCellEditor(sel[0]);
            }
        }
    }

    /**
     * Activate a cell editor for the given selected tree item.
     * 
     * @param item
     *            the selected tree item
     */
    private void activateCellEditor(TreeItem item) {
        // ensure the cell editor is visible
        table.showSelection();
        
        // Get the entry for this item
        IProperty property = (IProperty) item.getData();

        // Get the cell editor for the entry.
        // Note that the editor parent must be the Tree control
        cellEditor = property.getEditor(table);

        if (cellEditor == null) {
			// unable to create the editor
            return;
		}
        try {
            Object value =  property.getValue();
            if (value != null) {
            	if (cellEditor instanceof TextCellEditor)
            		cellEditor.setValue(value.toString());
            	else
            		cellEditor.setValue(value);
            }
        }
        catch (Exception e) {
		}

        // activate the cell editor
        cellEditor.activate();

        // if the cell editor has no control we can stop now
        Control control = cellEditor.getControl();
        if (control == null) {
            cellEditor.deactivate();
            cellEditor = null;
            return;
        }

        // add our editor listener
        cellEditor.addListener(editorListener);

        // set the layout of the tree editor to match the cell editor
        CellEditor.LayoutData layout = cellEditor.getLayoutData();
        editor.horizontalAlignment = layout.horizontalAlignment;
        editor.grabHorizontal = layout.grabHorizontal;
        editor.minimumWidth = layout.minimumWidth;
        editor.setEditor(control, item, columnToEdit);

        // set the error text from the cel editor
        //setErrorMessage(cellEditor.getErrorMessage());

        // give focus to the cell editor
        cellEditor.setFocus();

        // notify of activation
        //fireCellEditorActivated(cellEditor);
    }

    /**
     * Deactivate the currently active cell editor.
     */
    /* package */
    void deactivateCellEditor() {
        editor.setEditor(null);
        if (cellEditor != null) {
            cellEditor.deactivate();
            cellEditor.removeListener(editorListener);
            cellEditor = null;
        }
    }

    /**
     * Add columns to the tree and set up the layout manager accordingly.
     */
    private void addColumns() {

        // create the columns
        TreeColumn[] columns = table.getColumns();
        for (int i = 0; i < columnLabels.length; i++) {
            String string = columnLabels[i];
            if (string != null) {
            	TreeColumn column;
                if (i < columns.length) {
					column = columns[i];
				} else {
					column = new TreeColumn(table, 0);
				}
                column.setText(string);
            }
        }

        table.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                Rectangle area = table.getClientArea();
                TreeColumn[] columns = table.getColumns();
                if (area.width > 0) {
                    columns[0].setWidth(area.width * 40 / 100);
                    columns[1].setWidth(area.width - columns[0].getWidth() - 4);
                    table.removeControlListener(this);
                }
            }
        });

    }
    
    @Override
    public void dispose() {
        super.dispose();
    }

}
