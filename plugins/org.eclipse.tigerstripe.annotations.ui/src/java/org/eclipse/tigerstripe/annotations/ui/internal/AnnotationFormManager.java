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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationStore;
import org.eclipse.tigerstripe.annotations.IAnnotationForm;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IAnnotationSpecificationLiteral;
import org.eclipse.tigerstripe.annotations.IBigStringAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IBooleanAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IEnumerationAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.IStringAnnotationSpecification;
import org.eclipse.tigerstripe.annotations.ui.Activator;
import org.eclipse.tigerstripe.annotations.ui.views.AnnotationsView;
import org.eclipse.ui.themes.ColorUtil;

public class AnnotationFormManager {

	private static final String ANNOTATION_SPEC = "ANNOTATION_SPEC";

	private static Font ERROR_FONT = new Font(null, "arial", 8, SWT.BOLD);
	private static Font REGULAR_FOND = new Font(null, "arial", 8, SWT.None);

	private static Color ERROR_COLOR = new Color(null, 232, 123, 20);
	private static Color REGULAR_COLOR = new Color(null, 0, 0, 0);

	public static Composite createFormComposite(Composite parent,
			IAnnotationForm form, final AnnotationsView view) {

		GridData gridData;

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		IAnnotationSpecification[] specifications = form.getSpecifications();
		Arrays.sort(specifications, new AnnotationSpecificationComparator());
		for (IAnnotationSpecification spec : specifications) {

			if (spec instanceof IBigStringAnnotationSpecification) {

				Label label = new Label(composite, SWT.LEFT);
				label.setText(spec.getUserLabel());
				Text text = new Text(composite, SWT.WRAP | SWT.MULTI
						| SWT.V_SCROLL | SWT.BORDER);
				gridData = new GridData();
				gridData.horizontalAlignment = GridData.FILL;
				gridData.grabExcessHorizontalSpace = true;
				gridData.heightHint = 50;
				text.setLayoutData(gridData);
				text.setData(ANNOTATION_SPEC, spec);
				final Text fText = text;
				final IAnnotationSpecification fSpec = spec;
				final Label fLabel = label;
				text.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						view.markModified();
						try {
							IStatus status = fSpec.validateValue(fText
									.getText().trim());
							if (status.isOK()) {
								fLabel.setForeground(REGULAR_COLOR);
								fLabel.setFont(REGULAR_FOND);
								fText.setToolTipText("");
							} else {
								fLabel.setForeground(ERROR_COLOR);
								fLabel.setFont(ERROR_FONT);
								fText.setToolTipText(status.getMessage());
							}
						} catch (AnnotationCoreException ee) {
							Activator.log(ee);
						}
					}

				});

			} else if (spec instanceof IStringAnnotationSpecification) {

				Label label = new Label(composite, SWT.LEFT);
				label.setText(spec.getUserLabel());
				Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
				gridData = new GridData();
				gridData.horizontalAlignment = GridData.FILL;
				gridData.grabExcessHorizontalSpace = true;
				text.setLayoutData(gridData);
				text.setData(ANNOTATION_SPEC, spec);
				text.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						view.markModified();
					}

				});

			} else if (spec instanceof IBooleanAnnotationSpecification) {

				Button checkbox = new Button(composite, SWT.CHECK);
				gridData = new GridData();
				gridData.horizontalSpan = 2;
				gridData.verticalIndent = 3;
				checkbox.setLayoutData(gridData);
				checkbox.setData(ANNOTATION_SPEC, spec);
				checkbox.setText(spec.getUserLabel());
				checkbox.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);
					}

					@Override
					public void widgetSelected(SelectionEvent e) {
						view.markModified();
					}

				});

			} else if (spec instanceof IEnumerationAnnotationSpecification) {

				Label label = new Label(composite, SWT.LEFT);
				label.setText(spec.getUserLabel());
				ComboViewer comboViewer = new ComboViewer(composite,
						SWT.DROP_DOWN | SWT.READ_ONLY);
				comboViewer.setLabelProvider(new ILabelProvider() {

					public Image getImage(Object element) {
						return null;
					}

					public String getText(Object element) {
						IAnnotationSpecificationLiteral literal = (IAnnotationSpecificationLiteral) element;
						return literal.getValue();
					}

					public void addListener(ILabelProviderListener listener) {
					}

					public void dispose() {
					}

					public boolean isLabelProperty(Object element,
							String property) {
						return false;
					}

					public void removeListener(ILabelProviderListener listener) {
					}

				});

				gridData = new GridData();
				gridData.horizontalAlignment = GridData.FILL;
				gridData.grabExcessHorizontalSpace = true;
				comboViewer.getCombo().setLayoutData(gridData);
				IAnnotationSpecificationLiteral[] literals = ((IEnumerationAnnotationSpecification) spec)
						.getLiterals();
				Arrays.sort(literals,
						new AnnotationSpecificationLiteralComparator());
				comboViewer.add(literals);
				comboViewer.getCombo().setData(ANNOTATION_SPEC, spec);
				comboViewer.getCombo().addSelectionListener(
						new SelectionListener() {

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
								widgetSelected(e);
							}

							@Override
							public void widgetSelected(SelectionEvent e) {
								view.markModified();
							}

						});

			}

		}
		return composite;

	}

	public static void setFormCompositeData(Composite composite,
			AnnotationStore store, String URI, boolean useDefault) {

		IAnnotationSpecification spec;

		Control[] controls = composite.getChildren();
		for (Control control : controls) {

			try {
				spec = (IAnnotationSpecification) control
						.getData(ANNOTATION_SPEC);
				if (control instanceof Text) {

					String annotation = (String) store.getAnnotation(spec, URI);
					if (annotation == null || useDefault) {
						String value = (spec.getDefaultValue() != null) ? spec
								.getDefaultValue() : "";
						((Text) control).setText(value);
					} else {
						((Text) control).setText(annotation);
					}

				} else if (control instanceof Button) {

					Boolean annotation = (Boolean) store.getAnnotation(spec,
							URI);
					if (annotation == null || useDefault) {
						String value = (spec.getDefaultValue() != null) ? spec
								.getDefaultValue() : "false";
						((Button) control).setSelection(Boolean
								.getBoolean(value));
					} else {
						((Button) control).setSelection(annotation);
					}

				} else if (control instanceof Combo) {

					String annotation = (String) store.getAnnotation(spec, URI);
					if (annotation == null || useDefault) {
						String value = (spec.getDefaultValue() != null) ? spec
								.getDefaultValue() : "";
						((Combo) control).setText(value);
					} else {
						((Combo) control).setText(annotation);
					}

				}
			} catch (AnnotationCoreException e) {
				Activator.log(e);
			}
		}
	}

	public static void writeFormCompositeData(Composite composite,
			AnnotationStore store, String URI) {

		IAnnotationSpecification spec;

		Control[] controls = composite.getChildren();
		try {
			for (Control control : controls) {

				spec = (IAnnotationSpecification) control
						.getData(ANNOTATION_SPEC);
				if (control instanceof Text) {
					if (((Text) control).getText() != null
							|| !((Text) control).getText().equals("")) {
						store.setAnnotation(spec, URI, ((Text) control)
								.getText());
					}
				} else if (control instanceof Button) {
					store.setAnnotation(spec, URI, ((Button) control)
							.getSelection());
				} else if (control instanceof Combo) {
					if (((Combo) control).getText() != null
							|| !((Combo) control).getText().equals("")) {
						store.setAnnotation(spec, URI, ((Combo) control)
								.getText());
					}
				}
			}
			store.store();
		} catch (AnnotationCoreException e) {
			Activator.log(e);
		}

	}

	public static void clearFormCompositeData(Composite composite) {

		for (Control control : composite.getChildren()) {
			if (control instanceof Text) {
				((Text) control).setText("");
			} else if (control instanceof Button) {
				((Button) control).setSelection(false);
			} else if (control instanceof Combo) {
				// temp!
				((Combo) control).setText("<empty>");
			}
		}
	}

}
