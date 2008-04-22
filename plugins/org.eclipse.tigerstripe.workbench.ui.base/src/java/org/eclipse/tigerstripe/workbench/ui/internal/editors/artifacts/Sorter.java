package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

public class Sorter extends ViewerSorter {
    
    private int dir = SWT.DOWN;
    private String column = "name";
    /**
     * @param column
     * @param dir
     */
    public Sorter(int dir) {
        super();
        this.column = "name";
        this.dir = dir;
    }
    
    public Sorter(int dir,String column){
    	super();
        this.column = column;
        this.dir = dir;
    }
    
    public int compare(Viewer viewer, Object e1, Object e2) {
        int returnValue = 0;
        if (column.equals("value")){
        	// bit of a hack - assume this one is a Literal!
        	returnValue = ((ILiteral) e1).getValue().compareToIgnoreCase(
					((ILiteral) e2).getValue());
        	
        } else {	
        	returnValue = ((IModelComponent) e1).getName().compareToIgnoreCase(
					((IModelComponent) e2).getName());
        }
        if (this.dir == SWT.DOWN) {
            returnValue = returnValue * -1;
        }
        return returnValue;
    }
    
    

}