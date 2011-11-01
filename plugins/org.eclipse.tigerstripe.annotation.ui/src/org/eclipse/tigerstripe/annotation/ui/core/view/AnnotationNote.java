/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.core.view;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationFactory;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.InTransaction;
import org.eclipse.tigerstripe.annotation.core.InTransaction.Operation;
import org.eclipse.tigerstripe.annotation.ui.Images;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;

public class AnnotationNote extends EObjectBasedNote implements INote {

	private final Annotation annotation;
	private final Annotation original;

	private boolean isValid = true;


	public AnnotationNote(Annotation annotation) {
		this.original = annotation;
		this.annotation = AnnotationFactory.eINSTANCE.createAnnotation();
		revert();
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public void remove() {
		AnnotationPlugin.getManager().removeAnnotation(original);
	}

	public void save() {
		
		InTransaction.write(new Operation() {
			
			public void run() throws Throwable {
				original.setUri(annotation.getUri());
				original.setContent(EcoreUtil.copy(annotation.getContent()));
			}
		});
		
	}

	public void revert() {
		annotation.setUri(original.getUri());
		annotation.setContent(EcoreUtil.copy(original.getContent()));
	}

	public String getDescription() {
		AnnotationType type = AnnotationPlugin.getManager().getType(annotation);
		if (type != null) {
			return type.getDescription();
		}
		return null;
	}

	public Image getImage() {
		Image image = DisplayAnnotationUtil.getImage(annotation);
		if (image == null) {
			image = Images.getImage(Images.ANNOTATION);
		}
		return image;
	}

	public String getLabel() {
		return DisplayAnnotationUtil.getText(annotation);
	}

	public boolean isReadOnly() {
		return AnnotationPlugin.getManager().isReadOnly(annotation);
	}

	public EObject getContent() {
		return annotation.getContent();
	}

	@Override
	public String toString() {
		return getLabel();
	}

	@Override
	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean valid) {
		isValid = valid;
	}

	public boolean isLoadable() {
		return getContent() != null;
	}

}
