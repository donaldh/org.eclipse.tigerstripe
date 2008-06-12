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
package org.eclipse.tigerstripe.workbench.model.deprecated_;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.tigerstripe.repository.internal.IModelComponentMetadata;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A component for a Tigerstripe Model.
 * 
 * 
 * @author Eric Dillon
 */
public interface IModelComponent extends IStereotypeCapable, IAdaptable {

	/**
	 * An enum of the possible values for the multiplicity of an component.
	 * 
	 */
	public enum EMultiplicity {
		ZERO("0"), ZERO_ONE("0..1"), ZERO_STAR("0..*"), STAR("*"), ONE("1"), ONE_STAR(
				"1..*");

		private String label;
		private static String[] labels;

		EMultiplicity(String label) {
			this.label = label;
		}

		/**
		 * Return the label for an enumeration value.
		 * 
		 * @return String - the label.
		 */
		public String getLabel() {
			return label;
		}

		// This is a convenience
		public boolean isArray() {
			if (this.equals(ONE) || this.equals(ZERO) || this.equals(ZERO_ONE)) {
				return false;
			} else {
				return true;
			}
		}

		/**
		 * Given a String label, return an enumeration value. Returns null if no
		 * enumeration corresponds to the label.
		 * 
		 * @param label
		 * @return the enumeration value that corresponds to the label.
		 */
		public static EMultiplicity parse(String label) {
			for (EMultiplicity val : values()) {
				if (val.label.equals(label))
					return val;
			}
			return EMultiplicity.ONE;
		}

		public static int indexOf(EMultiplicity mult) {
			for (int index = 0; index < values().length; index++) {
				if (mult == values()[index])
					return index;
			}
			throw new IllegalArgumentException("Illegal multiplicity literal: "
					+ mult);
		}

		public static int indexOf(String multLiteral) {
			for (int index = 0; index < values().length; index++) {
				if (values()[index].label.equals(multLiteral))
					return index;
			}
			throw new IllegalArgumentException("Illegal multiplicity literal: "
					+ multLiteral);
		}

		public static EMultiplicity at(int index) {
			if (index >= 0 && index < values().length)
				return values()[index];
			throw new IllegalArgumentException("Illegal multiplicity literal: "
					+ index);
		}

		public static String[] labels() {
			if (labels == null) {
				labels = new String[values().length];
				int i = 0;
				for (EMultiplicity mult : values()) {
					labels[i] = mult.label;
					i++;
				}
			}
			return labels;
		}
	}

	/**
	 * An enum of the possible values for the visibility of a component.
	 * 
	 */
	public enum EVisibility {

		PUBLIC("public"), PROTECTED("protected"), PRIVATE("private"), PACKAGE(
				"package");

		private String label;

		EVisibility(String label) {
			this.label = label;
		}

		/**
		 * Return the label for an enumeration value.
		 * 
		 * @return String - the label.
		 */
		public String getLabel() {
			return this.label;
		}

		public static EVisibility parse(String label) {
			for (EVisibility val : values()) {
				if (val.label.equals(label))
					return val;
			}
			return EVisibility.PUBLIC;
		}

		public static int indexOf(EVisibility visibility) {
			for (int index = 0; index < values().length; index++) {
				if (visibility == values()[index])
					return index;
			}
			throw new IllegalArgumentException("Illegal visibility literal: "
					+ visibility);
		}

		public static EVisibility at(int index) {
			if (index >= 0 && index < values().length)
				return values()[index];
			throw new IllegalArgumentException("Illegal visibility index: "
					+ index);
		}
	}

	/**
	 * Returns the name associated with this component.
	 * 
	 * @return String - the name of the component
	 */
	public String getName();

	/**
	 * Sets the name associated with this component
	 * 
	 * @return
	 */
	public void setName(String name);

	/**
	 * Returns the comment (or plain-english description) associated with this
	 * model component.
	 * 
	 * @return String - the comment
	 */
	public String getComment();

	/**
	 * Set the comment for this component.
	 * 
	 * @param comment
	 */
	public void setComment(String comment);

	/**
	 * Returns an integer value indicating the visibility of this component.
	 * Possible values are defined in the static fields of this class.
	 * 
	 * @return int - representing the visbility
	 */
	public EVisibility getVisibility();

	/**
	 * Sets the visibility of this component. Possible values are defined in the
	 * static fields of this class.
	 * 
	 * @param visibility
	 */
	public void setVisibility(EVisibility visibility);

	/**
	 * Returns true if this component is included in the current active facet.
	 * 
	 * For IFields, IMethods and ILiterals this looks at Annotation-based
	 * exclusion only.
	 * 
	 * If no facet is active, always returns true.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public boolean isInActiveFacet() throws TigerstripeException;

	public IStatus validate();

	/**
	 * Returns the metadata about this Artifact type
	 * 
	 * @return
	 */
	public IModelComponentMetadata getMetadata();

	/**
	 * Returns a list of all the annotations for this object
	 * 
	 * @return a list of all annotations defined againt this object
	 */
	public List<Object> getAnnotations();

	/**
	 * Returns a list of all the annotations for for the given scheme for this
	 * object
	 * 
	 * @param scheme -
	 *            the scheme to limit the return to
	 * @return the list of all annotations for the given scheme defined against
	 *         this object
	 */
	public List<Object> getAnnotations(String scheme);

	/**
	 * Returns the first annotation defined against this object for the given
	 * scheme and annotationType
	 * 
	 * @param scheme -
	 *            the scheme to match, e.g. "tigerstripe"
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return the first annotation for the given scheme and annotationType
	 */
	public Object getAnnotation(String scheme, String annotationType);

	/**
	 * Returns the list of annotations defined against this object for the given
	 * scheme and annotationType
	 * 
	 * @param scheme -
	 *            the scheme to match, e.g. "tigerstripe"
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return the list of annotations for the given scheme and annotationType
	 */
	public List<Object> getAnnotations(String scheme, String annotationType);

	/**
	 * Returns true if there is any annotation defined against this object
	 * 
	 * @return true - if there is any annotation defined against this object
	 */
	public boolean hasAnnotations();

	/**
	 * Returns true if there is any annotation defined against this object for
	 * the given scheme
	 * 
	 * @param scheme -
	 *            the scheme to match, for example "tigerstripe"
	 * @return true - if there is any annotation defined against this object
	 */
	public boolean hasAnnotations(String scheme);

	/**
	 * Returns true if there is any annotation defined against this object for
	 * the given scheme and given annotationType
	 * 
	 * @param scheme -
	 *            the scheme to match, for example "tigerstripe"
	 * @param annotationType -
	 *            the annotation type to match. Match is done on the fully
	 *            qualified name of the annotation type using endsWidth(..), so
	 *            "org.eclipse.tigerstripe.annotation.example.Person" or
	 *            "Person" would work, e.g.
	 * @return true - if there is any annotation defined against this object
	 */
	public boolean hasAnnotations(String scheme, String annotationType);

	/**
	 * Returns the project that this Model Component belongs to.
	 * 
	 * @throws TigerstripeException
	 *             if the project is invalid (descriptor missing, etc..)
	 * @return the project this component belongs to, null if the component has
	 *         not been stored in a Model Project
	 */
	public ITigerstripeModelProject getProject() throws TigerstripeException;

}
