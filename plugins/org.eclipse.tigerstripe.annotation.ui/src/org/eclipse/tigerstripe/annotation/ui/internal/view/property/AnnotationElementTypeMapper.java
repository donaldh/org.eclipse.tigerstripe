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

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationElementTypeMapper extends AbstractTypeMapper {

	/**
	 * Constructor for AnnotationElementTypeMapper.
	 */
	public AnnotationElementTypeMapper() {
		super();
	}

    @SuppressWarnings("unchecked")
	public Class mapType(Object input) {
        URI uri = AnnotationPlugin.getManager().getUri(input);
        if (uri != null)
        	return Annotation.class;
        return super.mapType(input);
    }
}
