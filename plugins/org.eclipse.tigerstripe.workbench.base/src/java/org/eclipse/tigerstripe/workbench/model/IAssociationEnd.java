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

import org.eclipse.tigerstripe.workbench.model.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;

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
	 * Returns the Association that is the "container" for the end. This is an
	 * alias for getContainingArtifact()
	 * 
	 * @return the containing artifact.
	 */
	public IAssociationArtifact getContainingAssociation();

	public void setNavigable(boolean isNavigable);

	public void setAggregation(EAggregationEnum aggregation);

	public void setChangeable(EChangeableEnum changeable);

	public void setMultiplicity(IModelComponent.EMultiplicity multiplicity);

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

	public IType makeType();

	/**
	 * Returns the type of the end.
	 * 
	 * @return IType - the type of the end
	 */
	public IType getType();

	/**
	 * Returns the multiplicity of the end.
	 * 
	 * @return enumeration of the multiplicity type.
	 */
	public IModelComponent.EMultiplicity getMultiplicity();

	/**
	 * Returns the aggregation type of the end.
	 * 
	 * @return enumeration of the aggregation type.
	 */
	public EAggregationEnum getAggregation();

}
