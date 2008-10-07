package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
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
        if (e1 instanceof ILiteral && e2 instanceof ILiteral){
        	ILiteral lit1 = (ILiteral) e1;
        	ILiteral lit2 = (ILiteral) e2;
        	if (lit1.getType().getName().equals("int") && lit2.getType().getName().equals("int")){
        		int i1 = Integer.valueOf(lit1.getValue());
        		int i2 = Integer.valueOf(lit2.getValue());
        		if (i1 > i2)
        			returnValue = 1;
        		else
        			returnValue = -1;
        	} else {
        		returnValue = lit1.getValue().compareToIgnoreCase(
					lit2.getValue());
        	}
        	
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