/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.eclipse.utils;

import org.eclipse.jdt.ui.JavaElementSorter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact.DependencyEnd;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILabel;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;

public class TSElementSorter extends JavaElementSorter {

	private static final int ASSOCIATION_END_AEND = 9995;
	private static final int ASSOCIATION_END_ZEND = 9996;
	private static final int FIELD = 9997;
	private static final int CONSTANT = 9998;
	private static final int METHOD = 9999;

	@Override
	public int category(Object element) {
		if (element instanceof IRelationshipEnd) {
			if (element instanceof DependencyEnd) {
				// if it's a dependency end, check to see if it's the aEnd or
				// not
				IRelationshipEnd irel = (IRelationshipEnd) element;
				String relName = irel.getNameForType(irel.getIType()
						.getFullyQualifiedName());
				// if it's the aEnd, return the appropriate static int
				if (relName.equals("aEnd"))
					return ASSOCIATION_END_AEND;
			} else if (element instanceof IAssociationEnd) {
				// else, if it's an association end, check to see whether it's
				// the aEnd or zEnd
				// int he containing association
				IAssociationEnd assocEnd = (IAssociationEnd) element;
				IAssociationArtifact assoc = (IAssociationArtifact) assocEnd
						.getContainingAssociation();
				// if it's the aEnd, return the appropriate static int
				if (assoc.getAEnd().equals(assocEnd))
					return ASSOCIATION_END_AEND;
			}
			// if we get to here, it's the zEnd, so return the appropriate
			// static int
			return ASSOCIATION_END_ZEND;
		} else if (element instanceof IField)
			return FIELD;
		else if (element instanceof ILabel)
			return CONSTANT;
		else if (element instanceof IMethod)
			return METHOD;
		return super.category(element);
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		// if the categories are different, return the difference, else compare
		// names
		int cat1 = category(e1);
		int cat2 = category(e2);
		if (cat1 != cat2)
			return cat1 - cat2;
		/*
		 * if it's an IModelComponent comparison (IField, IMethod, ILabel, and
		 * IAssociationEnd all implement this interface) or if it's a Dependency
		 * end, then handle the comparison here
		 */
		if ((e1 instanceof IModelComponent && e2 instanceof IModelComponent)
				|| (e1 instanceof DependencyArtifact.DependencyEnd && e2 instanceof DependencyArtifact.DependencyEnd))
			return getElementName(e1).compareTo(getElementName(e2));

		// otherwise pass the comparison off to the superclass for handling
		return super.compare(viewer, e1, e2);
	}

	private String getElementName(Object element) {
		if (element instanceof IField)
			// for an IField, just return the label string
			return ((IField) element).getLabelString();
		else if (element instanceof IMethod)
			// for an IMethod, just return the label string
			return ((IMethod) element).getLabelString();
		else if (element instanceof ILabel)
			// for an ILabel, just return the label string
			return ((ILabel) element).getLabelString();
		else if (element instanceof IAssociationEnd) {
			// if here, it's an association end, so need to determine whether
			// it's the aEnd or the zEnd
			IAssociationEnd end = (IAssociationEnd) element;
			IAssociationArtifact containingAssoc = (IAssociationArtifact) end
					.getContainingAssociation();
			if (containingAssoc.getAEnd().equals(end))
				return "aEnd";
			else
				return "zEnd";
		} else if (element instanceof DependencyArtifact.DependencyEnd) {
			// if here, it's a dependency end, so just return the name of the
			// end
			DependencyArtifact.DependencyEnd end = (DependencyArtifact.DependencyEnd) element;
			return end.getName();
		}
		// shouldn't get here, but just in case we'll just return the
		// "toString()" result for
		// the element
		return element.toString();
	}

}
