/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial API and Implementation
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.test;

import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationSchemeRegistry;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.annotations.test.input.IInputConstants;

import junit.framework.TestCase;

public class TestRegistry extends TestCase {

	/**
	 * Test that all defined schemes can be retrieved without the use of
	 * selector
	 * 
	 */
	public final void testGetAllDefinedSchemes() throws AnnotationCoreException {
		IAnnotationScheme[] allSchemes = AnnotationSchemeRegistry.eINSTANCE
				.getDefinedSchemes(null);

		assertNotNull("AnnotationSchemeRegistry shouldn't return null",
				allSchemes);
		assertEquals(IInputConstants.NUMBER_OF_SCHEMES, allSchemes.length);
	}

	public final void testGetDefinedSchemes() throws AnnotationCoreException {
		// should get 2 for URI1 as scheme1 triggers on it, and scheme2 has the
		// default selector (always true)
		IAnnotationScheme[] uri1Schemes = AnnotationSchemeRegistry.eINSTANCE
				.getDefinedSchemes(IInputConstants.URI1);
		assertEquals(IInputConstants.NUMBER_OF_SCHEMES, uri1Schemes.length);

		// should only get 1 for URI2
		IAnnotationScheme[] uri2Schemes = AnnotationSchemeRegistry.eINSTANCE
				.getDefinedSchemes(IInputConstants.URI2);
		assertEquals(1, uri2Schemes.length);
	}

	public final void testGetAnnotationSchemeByID()
			throws AnnotationCoreException {
		IAnnotationScheme scheme1 = AnnotationSchemeRegistry.eINSTANCE
				.getAnnotationSchemeByID(IInputConstants.SCHEME1_ID);
		assertEquals(IInputConstants.SCHEME1_ID, scheme1.getNamespaceID());

		IAnnotationScheme scheme2 = AnnotationSchemeRegistry.eINSTANCE
				.getAnnotationSchemeByID(IInputConstants.SCHEME2_ID);
		assertEquals(IInputConstants.SCHEME2_ID, scheme2.getNamespaceID());

		IAnnotationScheme dummy = AnnotationSchemeRegistry.eINSTANCE
				.getAnnotationSchemeByID("dummy");
		assertNull(dummy);
	}

}
