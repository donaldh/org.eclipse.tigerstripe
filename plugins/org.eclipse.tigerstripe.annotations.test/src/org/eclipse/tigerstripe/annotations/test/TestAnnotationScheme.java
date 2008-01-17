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
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.annotations.test.input.IInputConstants;

import junit.framework.TestCase;

public class TestAnnotationScheme extends TestCase {

	private IAnnotationScheme scheme1 = null;
	private IAnnotationScheme scheme2 = null;

	protected void setUp() throws Exception {
		super.setUp();

		scheme1 = AnnotationSchemeRegistry.eINSTANCE
				.getAnnotationSchemeByID(IInputConstants.SCHEME1_ID);
		assertNotNull(scheme1);
		scheme2 = AnnotationSchemeRegistry.eINSTANCE
				.getAnnotationSchemeByID(IInputConstants.SCHEME2_ID);
		assertNotNull(scheme2);
	}

	public void testGetUserLabel() throws AnnotationCoreException {
		String label = scheme1.getNamespaceUserLabel();
		assertEquals(IInputConstants.SCHEME1_LABEL, label);

		String label2 = scheme2.getNamespaceUserLabel();
		assertEquals(IInputConstants.SCHEME2_LABEL, label2);
	}

	public void testSelectForm() throws AnnotationCoreException {
		IAnnotationForm form = scheme1.selectForm(IInputConstants.URI1);
		assertNotNull(form);
		assertEquals(IInputConstants.FORM1_ID, form.getID());

		form = scheme1.selectForm(IInputConstants.URI2);
		assertNotNull(form);
		assertEquals(IInputConstants.FORM2_ID, form.getID());

		form = scheme1.selectForm("dummy");
		assertNotNull(form);
		assertEquals(IInputConstants.EMPTYFORM_ID, form.getID());

		form = scheme1.selectForm(null);
		assertNull(form);

		form = scheme2.selectForm(IInputConstants.URI1);
		assertNull(form);
	}

	public void testGetDefinedForms() throws AnnotationCoreException {
		IAnnotationForm[] defForms = scheme1.getDefinedForms();
		assertNotNull(defForms);
		assertEquals(3, defForms.length);

		defForms = scheme2.getDefinedForms();
		assertNotNull(defForms);
		assertEquals(0, defForms.length);
	}
}
