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

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;

/**
 * @author Yuri Strot
 *
 */
public class PropertyTree {
	
	private Tree tree;
	private TreeEditor editor;
	private TreeViewer viewer;
	private CellEditor cellEditor;
    private ICellEditorListener editorListener;
	
    private static String[] columnLabels = { "Property", "Value" };

    // Cell editor support.
    private int columnToEdit = 1;
    private boolean readOnly;
	
	public Control create(Composite parent) {
		tree = new Tree(parent, SWT.FULL_SELECTION | SWT.SINGLE | SWT.HIDE_SELECTION | SWT.BORDER);

		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		
		viewer = new TreeViewer(tree);
		viewer.setLabelProvider(new PropertyLabelProvider());
		viewer.setContentProvider(new PropertyContentProvider());
		
		addColumns();
		hookControl();
		editor = new TreeEditor(tree);
		createEditorListener();
		
		return tree;
	}
	
	public void setContent(EObject object, boolean readOnly) {
		this.readOnly = readOnly;
		viewer.setInput(object);
		setNullSelection();
	}
	
	protected void setNullSelection() {
    	PropertiesSelectionManager.getInstance().setSelection(null);
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
            	try {
                	PropertyTree.this.applyEditorValue();
            	}
            	finally {
                    deactivateCellEditor();
            	}
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
        tree.addSelectionListener(new SelectionAdapter() {
            /* (non-Javadoc)
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
			public void widgetSelected(SelectionEvent e) {
            	// The viewer only owns the status line when there is
            	// no 'active' cell editor
            	if (cellEditor == null || !cellEditor.isActivated()) {
				}
            	updateSelection((IStructuredSelection)viewer.getSelection());
			}
            
			protected void updateSelection(IStructuredSelection sel) {
            	if (!sel.isEmpty()) {
            		Iterator<?> it = sel.iterator();
            		while (it.hasNext()) {
						Object object = it.next();
						if (object instanceof EProperty) {
			            	PropertiesSelectionManager.getInstance().setSelection(
			            			new PropertySelection((EProperty)object, viewer));
			            	return;
						}
					}
            	}
        		setNullSelection();
            }

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
                //handleSelect((TreeItem) e.item);
            }
        });
        // Part2: handle single click activation of cell editor
        tree.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                // only activate if there is a cell editor
                Point pt = new Point(event.x, event.y);
                TreeItem item = tree.getItem(pt);
                if (item != null) {
                    int x = item.getBounds().x;
                    if (tree.getColumnCount() > 0) {
                    	int width = tree.getColumn(0).getWidth();
                    	if (width + x > event.x)
                    		return;
                    }
                    handleSelect(item);
                }
            }
        });

        // Refresh the tree when F5 pressed
        tree.addKeyListener(new KeyAdapter() {
        	
        	/* (non-Javadoc)
        	 * @see org.eclipse.swt.events.KeyAdapter#keyPressed(org.eclipse.swt.events.KeyEvent)
        	 */
        	public void keyPressed(KeyEvent e) {
        		 if (e.character == SWT.CR && cellEditor == null) {
 					TreeItem[] items = tree.getSelection();
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
        EProperty entry = (EProperty) treeItem.getData();
        try {
            if (cellEditor != null) {
                entry.setValue(cellEditor.getValue());
            }
        }
        finally {
        	viewer.refresh();
        }
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
            if (object instanceof EProperty) {
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
    	if (readOnly) return;
        // ensure the cell editor is visible
        tree.showSelection();
        
        // Get the entry for this item
        EProperty property = (EProperty) item.getData();

        // Get the cell editor for the entry.
        // Note that the editor parent must be the Tree control
        cellEditor = property.getEditor(tree);

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
        TreeColumn[] columns = tree.getColumns();
        for (int i = 0; i < columnLabels.length; i++) {
            String string = columnLabels[i];
            if (string != null) {
            	TreeColumn column;
                if (i < columns.length) {
					column = columns[i];
				} else {
					column = new TreeColumn(tree, 0);
				}
                column.setText(string);
            }
        }

        tree.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                Rectangle area = tree.getClientArea();
                TreeColumn[] columns = tree.getColumns();
                if (area.width > 0) {
                    columns[0].setWidth(area.width * 40 / 100);
                    columns[1].setWidth(area.width - columns[0].getWidth() - 4);
                    tree.removeControlListener(this);
                }
            }
        });

    }

}
