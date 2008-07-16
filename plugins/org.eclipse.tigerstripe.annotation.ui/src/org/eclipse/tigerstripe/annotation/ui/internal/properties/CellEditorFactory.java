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
package org.eclipse.tigerstripe.annotation.ui.internal.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Yuri Strot
 *
 */
public class CellEditorFactory {

    /*
     * @param composite @return
     */
    public static CellEditor createComboBoxCellEditor(Composite composite,
    		final List<Object> choiceOfValues) {

        return new ExtendedComboBoxCellEditor(composite, new ArrayList<Object>(
        		choiceOfValues), new DefaultLabelProvider(), true);

    }
    
    /**
	 * Creates a dialog cell editor for editing multivalued features.
	 * 
	 * @param composite
	 *            the composite to contain the new cell editor
	 * @param feature
	 *            the feature being edited
	 * @param choiceOfValues
	 *            the possible values for that feature
	 * @return the new cell editor
	 */
    public static CellEditor createDialogCellEditor(Composite composite,
			final EStructuralFeature feature, final List<String> values) {
        return new StringListCellEditor(composite, values, feature.getName());
	}

    /*
	 * @param composite @return
	 */
    public static CellEditor createBooleanCellEditor(Composite composite) {
        return new ExtendedComboBoxCellEditor(composite,
                Arrays.asList(new Object[] { Boolean.FALSE,
                          Boolean.TRUE }), new DefaultLabelProvider(), true);
    }
	
	public static CellEditor createDateTimeCellEditor(Composite composite,
			EStructuralFeature feature, Date value) {
		return new DateTimeCellEditor(composite, feature.getName(), value);
	}	

}
