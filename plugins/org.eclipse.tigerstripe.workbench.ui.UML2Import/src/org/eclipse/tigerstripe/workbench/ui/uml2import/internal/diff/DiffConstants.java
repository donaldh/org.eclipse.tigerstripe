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
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.diff;


public class DiffConstants {

	public enum DiffTypes {

		VALUE("value"), PRESENCE("presence");

		private final String label;

		private DiffTypes(String label) {
			this.label = label;
		}

		public String getPojoLabel() {
			return this.label;
		}

		public static DiffTypes valueFromlabel(String label) {
			for (DiffTypes diffType : values()) {
				if (diffType.label.equals(label))
					return diffType;
			}
			return null;
		}
	}

	public enum DiffLocation {

		ARTIFACT("Artifact"),

		ARTIFACT_FIELD("Artifact:Field"), ARTIFACT_FIELD_TYPE(
				"Artifact:Field:Type"), ARTIFACT_FIELD_MULTIPLICITY(
				"Artifact:Field:Multiplicity"), ARTIFACT_FIELD_REFBY(
				"Artifact:Field:RefBy"), ARTIFACT_FIELD_VISIBILITY(
				"Artifact:Field:Visibility"), ARTIFACT_FIELD_OPTIONAL(
				"Artifact:Field:Optional"), ARTIFACT_FIELD_READONLY(
				"Artifact:Field:ReadOnly"),

		ARTIFACT_LITERAL("Artifact:Literal"), ARTIFACT_LITERAL_VISIBILITY(
				"Artifact:Literal:Visibility"), ARTIFACT_LITERAL_TYPE(
				"Artifact:Literal:Type"), ARTIFACT_LITERAL_VALUE(
				"Artifact:Literal:Value"),

		ARTIFACT_METHOD("Artifact:Method"), ARTIFACT_METHOD_OPTIONAL(
				"Artifact:Method:Optional"), ARTIFACT_METHOD_VISIBILITY(
				"Artifact:Method:Visibility"), ARTIFACT_METHOD_INSTANCE(
				"Artifact:Method:Instance"), ARTIFACT_METHOD_RETURNTYPE(
				"Artifact:Method:ReturnType"), ARTIFACT_METHOD_RETURNTYPE_MULTIPLICITY(
				"Artifact:Method:ReturnType:Multiplicity"), ARTIFACT_METHOD_RETURNTYPE_ITERATOR(
				"Artifact:Method:ReturnType:Iterator"), ARTIFACT_METHOD_RETURNTYPE_REFBY(
				"Artifact:Method:ReturnType:RefBy"), ARTIFACT_METHOD_RETURNTYPE_VOID(
				"Artifact:Method:ReturnType:Void"), ARTIFACT_METHOD_ARGUMENT_NAME(
				"Artifact:Method:Argument:Name"), ARTIFACT_METHOD_ARGUMENT_TYPE(
				"Artifact:Method:Argument:Type"), ARTIFACT_METHOD_ARGUMENT_MULTIPLICITY(
				"Artifact:Method:Argument:Multiplicity"), ARTIFACT_METHOD_ARGUMENT_REFBY(
				"Artifact:Method:Argument:RefBy"), ARTIFACT_METHOD_ARGUMENTLIST(
				"Artifact:Method:ArgumentList"), ARTIFACT_METHOD_EXCEPTION(
				"Artifact:Method:Exception"),

		ASSOCIATION_END("Association:AssociationEnd"), ASSOCIATION_END_TYPE(
				"Association:AssociationEnd:Type"), ASSOCIATION_END_MULTIPLICITY(
				"Association:AssociationEnd:Multiplicity"), ASSOCIATION_END_CHANGEABLE(
				"Association:AssociationEnd:Changeable"), ASSOCIATION_END_AGGREGATION(
				"Association:AssociationEnd:Aggregation"), ASSOCIATION_END_NAVIGABLE(
				"Association:AssociationEnd:Navigable"), ASSOCIATION_END_ORDERED(
				"Association:AssociationEnd:Ordered"), DEPENDENCY_END(
				"Dependency:DependencyEnd");

		private final String label;

		private DiffLocation(String label) {
			this.label = label;
		}

		public String getPojoLabel() {
			return this.label;
		}

		public static DiffLocation valueFromlabel(String label) {
			for (DiffLocation diffLoc : values()) {
				if (diffLoc.label.equals(label))
					return diffLoc;
			}
			return null;
		}
	}

}
