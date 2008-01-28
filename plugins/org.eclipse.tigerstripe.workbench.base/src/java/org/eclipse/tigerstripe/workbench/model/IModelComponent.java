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
package org.eclipse.tigerstripe.workbench.model;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

/**
 * A component for a Tigerstripe Model
 * 
 * 
 * @author Eric Dillon
 */
public interface IModelComponent extends IStereotypeCapable {

	/**
	 * An enum of the possible values for the multiplicity of an end.
	 * 
	 */
	public enum EMultiplicity {
		ONE("1"), ZERO("0"), ZERO_ONE("0..1"), ZERO_STAR("0..*"), ONE_STAR(
				"1..*"), STAR("*");
	
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
			if (this.equals(ONE) ||
					this.equals(ZERO) ||
					this.equals(ZERO_ONE)){
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
	 * Static integer value for private visibility.
	 */
	public final static int VISIBILITY_PACKAGE = 3;
	/**
	 * Static integer value for private visibility.
	 */
	public final static int VISIBILITY_PRIVATE = 2;
	/**
	 * Static integer value for protected visibility.
	 */
	public final static int VISIBILITY_PROTECTED = 1;
	/**
	 * Static integer value for public visibility.
	 */
	public final static int VISIBILITY_PUBLIC = 0;

	/**
	 * Sets the name associated with this component
	 * 
	 * @return
	 */
	public void setName(String name);

	public void setComment(String comment);

	public void setVisibility(int visibility);

	public void addStereotypeInstance(IStereotypeInstance instance);

	public void removeStereotypeInstance(IStereotypeInstance instance);

	public void removeStereotypeInstances(IStereotypeInstance[] instances);

	/**
	 * Returns the comment (or plain-english description) associated with this
	 * model component.
	 * 
	 * @return String - the comment
	 */
	public String getComment();

	/**
	 * Returns the name associated with this component.
	 * 
	 * @return String - the name of the component
	 */
	public String getName();

	/**
	 * Returns an integer value indicating the visibility of this component.
	 * Possible values are defined in the static fields of this class.
	 * 
	 * @return int - representing the visbility
	 */
	public int getVisibility();

	/**
	 * Returns true if this component is included in the current active facet.
	 * 
	 * For IFields, IMethods and ILabels this looks at Annotation-based
	 * exclusion only.
	 * 
	 * If no facet is active, always returns true.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public boolean isInActiveFacet() throws TigerstripeException;

	public IStatus validate();
}
