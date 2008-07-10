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

import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;

public interface IAssociationEnd extends IModelComponent, IRelationshipEnd {

	/**
	 * An enumeration of the possible values for the aggregation of an end.
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
	 * Returns the Association that is the "container" for the end. This is an
	 * alias for getContainingArtifact()
	 * 
	 * @return the containing artifact.
	 */
	public IAssociationArtifact getContainingAssociation();

	/**
	 * Returns the navigabilty of the End.
	 * 
	 * @return true if navigable.
	 */
	public boolean isNavigable();

	/**
	 * Sets the navigable attribute of the End.
	 * 
	 * @param isNavigable
	 */
	public void setNavigable(boolean isNavigable);

	/**
	 * Returns the aggregation type of the end.
	 * 
	 * @return enumeration of the aggregation type.
	 */
	public EAggregationEnum getAggregation();

	/**
	 * Sets the aggregation type of the End.
	 * 
	 * @param aggregation
	 */
	public void setAggregation(EAggregationEnum aggregation);

	/**
	 * Returns the changeability type of the end.
	 * 
	 * @return enumeration of the changeability type.
	 */
	public EChangeableEnum getChangeable();

	/** 
	 * Sets the changeable attribute of the End.
	 * 
	 * @param changeable
	 */
	public void setChangeable(EChangeableEnum changeable);

	/**
	 * Returns the multiplicity of the end.
	 * 
	 * @return enumeration of the multiplicity type.
	 */
	public IModelComponent.EMultiplicity getMultiplicity();

	public void setMultiplicity(IModelComponent.EMultiplicity multiplicity);

	/**
	 * Returns the IArtifact that is the "container" for the end. This will
	 * always be an association or associationClass artifact.
	 * 
	 * @return the containing artifact.
	 */
	public IAbstractArtifact getContainingArtifact();

	/**
	 * Sets the ordered attribute.
	 * 
	 * @param isOrdered
	 */
	public void setOrdered(boolean isOrdered);

	/**
	 * Returns the ordered attribute of the End.
	 * 
	 * @return true if ordered.
	 */
	public boolean isOrdered();

	/**
	 * Sets the unique attribute of the End.
	 * @param isUnique
	 */
	public void setUnique(boolean isUnique);
	
	/**
	 * Returns the unique attribute of the End.
	 * 
	 * @return true if unique.
	 */
	public boolean isUnique();

	/**
	 * Sets the type of the artifact at the End.
	 * 
	 * @param type
	 */
	public void setType(IType type);

	/**
	 * Make a new blank type.
	 * 
	 * 
	 * @return a new IType.
	 */
	public IType makeType();

	/**
	 * Returns a String that describes the AssociationEnd.
	 * 
	 * This is the presentation used in the Explorer view.
	 * 
	 * The format is : 
	 * 		name::typeName[]
	 * 		name::typeName
	 * 
	 * 
	 * @return formatted string
	 */
	public String getLabelString();


}
