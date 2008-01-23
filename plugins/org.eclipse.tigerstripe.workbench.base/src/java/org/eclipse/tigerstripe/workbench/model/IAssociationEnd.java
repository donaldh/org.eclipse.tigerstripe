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

import java.util.List;

import org.eclipse.tigerstripe.workbench.internal.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.workbench.model.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

public interface IAssociationEnd extends IModelComponent, IRelationshipEnd {

	/**
	 * An enum of the possible values for the aggregation of an end.
	 * 
	 */
	public enum EAggregationEnum {
		NONE("none"), SHARED("shared"), COMPOSITE("composite");
	
		private String label;
	
		EAggregationEnum(String label) {
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
	
		/**
		 * Given a String label, return an enumeration value. Returns null if no
		 * enumeration corresponds to the label.
		 * 
		 * @param label
		 * @return the enumeration value that corresponds to the label.
		 */
		public static EAggregationEnum parse(String label) {
			for (EAggregationEnum val : values()) {
				if (val.label.equals(label))
					return val;
			}
			return null;
		}
	}

	/**
	 * An enum of the possible values for the changeability of an end.
	 * 
	 */
	public enum EChangeableEnum {
		NONE("none"), FROZEN("frozen"), ADDONLY("addOnly");
	
		private String label;
	
		EChangeableEnum(String label) {
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
	
		/**
		 * Given a String label, return an enumeration value. Returns null if no
		 * enumeration corresponds to the label.
		 * 
		 * @param label
		 * @return the enumeration value that corresponds to the label.
		 */
		public static EChangeableEnum parse(String label) {
			for (EChangeableEnum val : values()) {
				if (val.label.equals(label))
					return val;
			}
			return null;
		}
	}

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
	 * Returns the Association that is the "container" for the end. This is an
	 * alias for getContainingArtifact()
	 * 
	 * @return the containing artifact.
	 */
	public IAbstractArtifact getContainingAssociation();

	public void setNavigable(boolean isNavigable);

	public void setAggregation(EAggregationEnum aggregation);

	public void setChangeable(EChangeableEnum changeable);

	public void setMultiplicity(EMultiplicity multiplicity);

	/**
	 * Returns the IArtifact that is the "container" for the end. This will
	 * always be an association or associationClass artifact.
	 * 
	 * @return the containing artifact.
	 */
	public IAbstractArtifact getContainingArtifact();

	public boolean isUnique();

	public void setOrdered(boolean isOrdered);

	/**
	 * Returns the navigabilty of the End.
	 * 
	 * @return true if navigable.
	 */
	public boolean isNavigable();

	/**
	 * Returns the ordered attribute of the End.
	 * 
	 * @return true if ordered.
	 */
	public boolean isOrdered();

	public void setType(IType type);

	/**
	 * Returns the changeability type of the end.
	 * 
	 * @return enumeration of the changeability type.
	 */
	public EChangeableEnum getChangeable();

	public void setUnique(boolean isUnique);

	public IType makeIType();

	/**
	 * Returns the type of the end.
	 * 
	 * @return IType - the type of the end
	 */
	public IType getIType();

	/**
	 * Returns the multiplicity of the end.
	 * 
	 * @return enumeration of the multiplicity type.
	 */
	public EMultiplicity getMultiplicity();

	/**
	 * Returns the type of the end.
	 * 
	 * @return IType - the type of the end
	 * @deprecated use getIType for consistency
	 */
	@Deprecated
	public IType getType();

	public List<TigerstripeError> validate();

	/**
	 * Returns the aggregation type of the end.
	 * 
	 * @return enumeration of the aggregation type.
	 */
	public EAggregationEnum getAggregation();

}
