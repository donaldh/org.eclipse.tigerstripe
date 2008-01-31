/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    J. Strawn (Cisco Systems, Inc.) - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.ui.internal;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationStore;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IBooleanAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IStringAnnotationSpecification;

public class AnnotationFormManager {

	private static final String ANNOTATION_SPEC = "ANNOTATION_SPEC";

	public static Composite createFormComposite(Composite parent, IAnnotationForm form) {

		GridData gridData;

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		IAnnotationSpecification[] specs = form.getSpecifications();
		Arrays.sort(specs, new AnnotationSpecificationComparator());
		for (IAnnotationSpecification spec : specs) {

			if (spec instanceof IStringAnnotationSpecification) {

				Label label = new Label(composite, SWT.LEFT);
				label.setText(spec.getUserLabel());
				Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
				gridData = new GridData();
				gridData.horizontalAlignment = GridData.FILL;
				gridData.grabExcessHorizontalSpace = true;
				text.setLayoutData(gridData);
				text.setData(ANNOTATION_SPEC, spec);

			} else if (spec instanceof IBooleanAnnotationSpecification) {

				Button checkbox = new Button(composite, SWT.CHECK);
				gridData = new GridData();
				gridData.horizontalSpan = 2;
				gridData.verticalIndent = 3;
				checkbox.setLayoutData(gridData);
				checkbox.setData(ANNOTATION_SPEC, spec);
				checkbox.setText(spec.getUserLabel());
			}

		}
		return composite;

	}

	public static void setFormCompositeData(Composite composite, AnnotationStore store, String URI, boolean useDefault) {

		IAnnotationSpecification spec;

		Control[] controls = composite.getChildren();
		for (Control control : controls) {

			try {
				spec = (IAnnotationSpecification) control.getData(ANNOTATION_SPEC);
				if (control instanceof Text) {
					String annotation = (String) store.getAnnotation(spec, URI);
					if (annotation == null || useDefault) {
						String value = (spec.getDefaultValue() != null) ? spec.getDefaultValue() : "";
						((Text) control).setText(value);
					} else {
						((Text) control).setText(annotation);
					}
				} else if (control instanceof Button) {
					Boolean annotation = (Boolean) store.getAnnotation(spec, URI);
					if (annotation == null || useDefault) {
						String value = (spec.getDefaultValue() != null) ? spec.getDefaultValue() : "false";
						((Button) control).setSelection(Boolean.getBoolean(value));
					} else {
						((Button) control).setSelection(annotation);
					}
				}
			} catch (AnnotationCoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void writeFormCompositeData(Composite composite, AnnotationStore store, String URI) {

		IAnnotationSpecification spec;

		Control[] controls = composite.getChildren();
		try {
			for (Control control : controls) {

				spec = (IAnnotationSpecification) control.getData(ANNOTATION_SPEC);
				if (control instanceof Text) {
					if (((Text) control).getText() != null || !((Text) control).getText().equals("")) {
						store.setAnnotation(spec, URI, ((Text) control).getText());
					}
				} else if (control instanceof Button) {
					store.setAnnotation(spec, URI, ((Button) control).getSelection());
				}
			}
			store.store();
		} catch (AnnotationCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void clearFormCompositeData(Composite composite) {

		for (Control control : composite.getChildren()) {
			if (control instanceof Text) {
				((Text) control).setText("");
			} else if (control instanceof Button) {
				((Button) control).setSelection(false);
			}
		}
	}

}
