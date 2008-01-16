/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    erdillon (Cisco Systems, Inc.) - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations;

/**
 * This interface is to be implemented by classes acting as selectors for
 * Annotation namespaces or forms.
 * 
 */
public interface ISelector {

	// A default selector for convenience.
	public static ISelector DEFAULT = new ISelector() {
		public boolean isEnabled(IAnnotationSpecification spec, String URI)
				throws AnnotationCoreException {
			return true;
		}

		public boolean select(String URI) {
			return true;
		}

		public void setContext(Object context) {
		}
	};

	/**
	 * Context initialization for this selector.
	 * 
	 * This method is called upon instantiation of the selector. The relevant
	 * object will be passed whether it is used as a Namespace selector or a
	 * Form Selector.
	 * 
	 * @param context
	 */
	public void setContext(Object context);

	public boolean select(String URI);

	public boolean isEnabled(IAnnotationSpecification spec, String URI)
			throws AnnotationCoreException;
}
