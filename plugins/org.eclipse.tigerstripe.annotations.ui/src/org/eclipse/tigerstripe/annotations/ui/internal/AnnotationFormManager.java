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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationStore;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IBooleanAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IStringAnnotationSpecification;

public class AnnotationFormManager {

	public static Map<String, Widget> addFormComposites(
			Composite parent, AnnotationStore store, String uri,
			IAnnotationForm form) {

		Map<String, Widget> map = new HashMap<String, Widget>();

		IAnnotationSpecification[] specs = form.getSpecifications();
		Arrays.sort(specs, new AnnotationSpecificationComparator());
		for (IAnnotationSpecification spec : specs) {
			Widget widget = null;
			Composite specComposite = new Composite(parent, SWT.NONE);
			specComposite.setLayout(new GridLayout(2, true));
			if (spec instanceof IStringAnnotationSpecification) {
				widget = addStringAnnotation(specComposite, spec, store, uri);
			} else if (spec instanceof IBooleanAnnotationSpecification) {
				widget = addBooleanAnnotation(specComposite, spec, store, uri);
			} else {
				continue;
			}
			map.put(spec.getID(), widget);
		}

		return map;

	}

	private static Widget addStringAnnotation(Composite specComposite,
			IAnnotationSpecification spec, AnnotationStore store, String uri) {

		Label label = new Label(specComposite, SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		label.setText(spec.getUserLabel());

		Text text = null;
		try {
			text = new Text(specComposite, SWT.SINGLE);
			text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			if (store.getAnnotation(spec, uri) != null) {
				text.setText((String) store.getAnnotation(spec, uri));
			} else if (spec.getDefaultValue() != null
					&& (!spec.getDefaultValue().equals(""))) {
				text.setText(spec.getDefaultValue());
			}
		} catch (AnnotationCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return text;
	}

	private static Widget addBooleanAnnotation(Composite specComposite,
			IAnnotationSpecification spec, AnnotationStore store, String uri) {

		Label label = new Label(specComposite, SWT.LEFT);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		label.setText(spec.getUserLabel());

		Button checkbox = null;
		try {
			checkbox = new Button(specComposite, SWT.CHECK);
			checkbox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			if (store.getAnnotation(spec, uri) != null) {
				checkbox.setSelection((Boolean) store.getAnnotation(spec, uri));  
			} else {
				checkbox.setSelection(Boolean.getBoolean(spec.getDefaultValue()));
			}
		} catch (AnnotationCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return checkbox;
	}

}
