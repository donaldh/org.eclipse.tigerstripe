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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import org.eclipse.tigerstripe.api.model.IAssociationEnd;
import org.eclipse.tigerstripe.api.model.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.api.model.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.api.model.IAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.utils.ClassDiagramUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class AssociationEndUpdateCommand extends
		AbstractQualifiedNamedElementUpdateCommand {

	public AssociationEndUpdateCommand(Association eAssociation,
			IAssociationArtifact iArtifact) {
		super(eAssociation, iArtifact);
	}

	@Override
	public void updateQualifiedNamedElement(QualifiedNamedElement element,
			IAbstractArtifact artifact) {
		if (!(element instanceof Association))
			throw new IllegalArgumentException(
					"input QualifiedNamedElement argument " + element
							+ "is not an Association");
		else if (!(artifact instanceof IAssociationArtifact))
			throw new IllegalArgumentException(
					"input IAbstractArtifact argument " + artifact
							+ "is not an IAssociationArtifact");
		Association eAssociation = (Association) element;
		IAssociationArtifact iAssocArtifact = (IAssociationArtifact) artifact;
		IAssociationEnd aEnd = (IAssociationEnd) iAssocArtifact.getAEnd();
		IAssociationEnd zEnd = (IAssociationEnd) iAssocArtifact.getZEnd();

		if (aEnd.getType() != null) {
			String aEndType = aEnd.getType().getFullyQualifiedName();
			if (eAssociation.getAEnd() != null
					&& !aEndType.equals(eAssociation.getAEnd()
							.getFullyQualifiedName())) {
				// if we can find the corresponding type on the diagram fine
				// set it up, otherwise we need to remove the assoc
				Map map = (Map) eAssociation.eContainer();
				MapHelper helper = new MapHelper(map);
				AbstractArtifact eArt = helper
						.findAbstractArtifactFor(aEndType);
				eAssociation.setAEnd(eArt);
				// if eArt == null the dependency will be removed downstream
			}
		}

		if (zEnd.getType() != null) {
			String zEndType = zEnd.getType().getFullyQualifiedName();
			if (eAssociation.getZEnd() != null
					&& !zEndType.equals(eAssociation.getZEnd()
							.getFullyQualifiedName())) {
				// if we can find the corresponding type on the diagram fine
				// set it up, otherwise we need to remove the assoc
				Map map = (Map) eAssociation.eContainer();
				MapHelper helper = new MapHelper(map);
				AbstractArtifact eArt = helper
						.findAbstractArtifactFor(zEndType);
				eAssociation.setZEnd(eArt);
				// if eArt == null the dependency will be removed downstream
			}
		}

		if (!aEnd.getName().equals(eAssociation.getAEndName())) {
			eAssociation.setAEndName(aEnd.getName());
		}
		if (aEnd.isNavigable() != eAssociation.isAEndIsNavigable()) {
			eAssociation.setAEndIsNavigable(aEnd.isNavigable());
		}
		if (aEnd.isOrdered() != eAssociation.isAEndIsOrdered()) {
			eAssociation.setAEndIsOrdered(aEnd.isOrdered());
		}

		if (aEnd.isUnique() != eAssociation.isAEndIsUnique()) {
			eAssociation.setAEndIsUnique(aEnd.isUnique());
		}

		if (aEnd.getVisibility() != ClassDiagramUtils
				.fromVisibility(eAssociation.getAEndVisibility())) {
			eAssociation.setAEndVisibility(ClassDiagramUtils.toVisibility(aEnd
					.getVisibility()));
		}

		if (!zEnd.getName().equals(eAssociation.getZEndName())) {
			eAssociation.setZEndName(zEnd.getName());
		}
		if (zEnd.isNavigable() != eAssociation.isZEndIsNavigable()) {
			eAssociation.setZEndIsNavigable(zEnd.isNavigable());
		}
		if (zEnd.isOrdered() != eAssociation.isZEndIsOrdered()) {
			eAssociation.setZEndIsOrdered(zEnd.isOrdered());
		}

		if (zEnd.isUnique() != eAssociation.isZEndIsUnique()) {
			eAssociation.setZEndIsUnique(zEnd.isUnique());
		}

		if (zEnd.getVisibility() != ClassDiagramUtils
				.fromVisibility(eAssociation.getZEndVisibility())) {
			eAssociation.setZEndVisibility(ClassDiagramUtils.toVisibility(zEnd
					.getVisibility()));
		}

		updateAggregation(eAssociation, iAssocArtifact);
		updateMultiplicity(eAssociation, iAssocArtifact);
		updateChangeable(eAssociation, iAssocArtifact);

	}

	private void updateAggregation(Association eAssociation,
			IAssociationArtifact iArtifact) {

		IAssociationEnd aEnd = (IAssociationEnd) iArtifact.getAEnd();
		IAssociationEnd zEnd = (IAssociationEnd) iArtifact.getZEnd();

		if (aEnd.getAggregation() == EAggregationEnum.NONE
				&& eAssociation.getAEndAggregation() != AggregationEnum.NONE_LITERAL) {
			eAssociation.setAEndAggregation(AggregationEnum.NONE_LITERAL);
		} else if (aEnd.getAggregation() == EAggregationEnum.COMPOSITE
				&& eAssociation.getAEndAggregation() != AggregationEnum.COMPOSITE_LITERAL) {
			eAssociation.setAEndAggregation(AggregationEnum.COMPOSITE_LITERAL);
		} else if (aEnd.getAggregation() == EAggregationEnum.SHARED
				&& eAssociation.getAEndAggregation() != AggregationEnum.SHARED_LITERAL) {
			eAssociation.setAEndAggregation(AggregationEnum.SHARED_LITERAL);
		}

		if (zEnd.getAggregation() == EAggregationEnum.NONE
				&& eAssociation.getZEndAggregation() != AggregationEnum.NONE_LITERAL) {
			eAssociation.setZEndAggregation(AggregationEnum.NONE_LITERAL);
		} else if (zEnd.getAggregation() == EAggregationEnum.COMPOSITE
				&& eAssociation.getZEndAggregation() != AggregationEnum.COMPOSITE_LITERAL) {
			eAssociation.setZEndAggregation(AggregationEnum.COMPOSITE_LITERAL);
		} else if (zEnd.getAggregation() == EAggregationEnum.SHARED
				&& eAssociation.getZEndAggregation() != AggregationEnum.SHARED_LITERAL) {
			eAssociation.setZEndAggregation(AggregationEnum.SHARED_LITERAL);
		}

	}

	private void updateMultiplicity(Association eAssociation,
			IAssociationArtifact iArtifact) {

		IAssociationEnd aEnd = (IAssociationEnd) iArtifact.getAEnd();
		IAssociationEnd zEnd = (IAssociationEnd) iArtifact.getZEnd();

		if (aEnd.getMultiplicity() == EMultiplicity.ONE
				&& eAssociation.getAEndMultiplicity() != AssocMultiplicity.ONE_LITERAL) {
			eAssociation.setAEndMultiplicity(AssocMultiplicity.ONE_LITERAL);
		} else if (aEnd.getMultiplicity() == EMultiplicity.ONE_STAR
				&& eAssociation.getAEndMultiplicity() != AssocMultiplicity.ONE_STAR_LITERAL) {
			eAssociation
					.setAEndMultiplicity(AssocMultiplicity.ONE_STAR_LITERAL);
		} else if (aEnd.getMultiplicity() == EMultiplicity.STAR
				&& eAssociation.getAEndMultiplicity() != AssocMultiplicity.STAR_LITERAL) {
			eAssociation.setAEndMultiplicity(AssocMultiplicity.STAR_LITERAL);
		} else if (aEnd.getMultiplicity() == EMultiplicity.ZERO
				&& eAssociation.getAEndMultiplicity() != AssocMultiplicity.ZERO_LITERAL) {
			eAssociation.setAEndMultiplicity(AssocMultiplicity.ZERO_LITERAL);
		} else if (aEnd.getMultiplicity() == EMultiplicity.ZERO_ONE
				&& eAssociation.getAEndMultiplicity() != AssocMultiplicity.ZERO_ONE_LITERAL) {
			eAssociation
					.setAEndMultiplicity(AssocMultiplicity.ZERO_ONE_LITERAL);
		} else if (aEnd.getMultiplicity() == EMultiplicity.ZERO_STAR
				&& eAssociation.getAEndMultiplicity() != AssocMultiplicity.ZERO_STAR_LITERAL) {
			eAssociation
					.setAEndMultiplicity(AssocMultiplicity.ZERO_STAR_LITERAL);
		}

		if (zEnd.getMultiplicity() == EMultiplicity.ONE
				&& eAssociation.getZEndMultiplicity() != AssocMultiplicity.ONE_LITERAL) {
			eAssociation.setZEndMultiplicity(AssocMultiplicity.ONE_LITERAL);
		} else if (zEnd.getMultiplicity() == EMultiplicity.ONE_STAR
				&& eAssociation.getZEndMultiplicity() != AssocMultiplicity.ONE_STAR_LITERAL) {
			eAssociation
					.setZEndMultiplicity(AssocMultiplicity.ONE_STAR_LITERAL);
		} else if (zEnd.getMultiplicity() == EMultiplicity.STAR
				&& eAssociation.getZEndMultiplicity() != AssocMultiplicity.STAR_LITERAL) {
			eAssociation.setZEndMultiplicity(AssocMultiplicity.STAR_LITERAL);
		} else if (zEnd.getMultiplicity() == EMultiplicity.ZERO
				&& eAssociation.getZEndMultiplicity() != AssocMultiplicity.ZERO_LITERAL) {
			eAssociation.setZEndMultiplicity(AssocMultiplicity.ZERO_LITERAL);
		} else if (zEnd.getMultiplicity() == EMultiplicity.ZERO_ONE
				&& eAssociation.getZEndMultiplicity() != AssocMultiplicity.ZERO_ONE_LITERAL) {
			eAssociation
					.setZEndMultiplicity(AssocMultiplicity.ZERO_ONE_LITERAL);
		} else if (zEnd.getMultiplicity() == EMultiplicity.ZERO_STAR
				&& eAssociation.getZEndMultiplicity() != AssocMultiplicity.ZERO_STAR_LITERAL) {
			eAssociation
					.setZEndMultiplicity(AssocMultiplicity.ZERO_STAR_LITERAL);
		}

	}

	private void updateChangeable(Association eAssociation,
			IAssociationArtifact iArtifact) {

		IAssociationEnd aEnd = (IAssociationEnd) iArtifact.getAEnd();
		IAssociationEnd zEnd = (IAssociationEnd) iArtifact.getZEnd();

		if (aEnd.getChangeable() == EChangeableEnum.ADDONLY
				&& eAssociation.getAEndIsChangeable() != ChangeableEnum.ADD_ONLY_LITERAL) {
			eAssociation.setAEndIsChangeable(ChangeableEnum.ADD_ONLY_LITERAL);
		} else if (aEnd.getChangeable() == EChangeableEnum.FROZEN
				&& eAssociation.getAEndIsChangeable() != ChangeableEnum.FROZEN_LITERAL) {
			eAssociation.setAEndIsChangeable(ChangeableEnum.FROZEN_LITERAL);
		} else if (aEnd.getChangeable() == EChangeableEnum.NONE
				&& eAssociation.getAEndIsChangeable() != ChangeableEnum.NONE_LITERAL) {
			eAssociation.setAEndIsChangeable(ChangeableEnum.NONE_LITERAL);
		}

		if (zEnd.getChangeable() == EChangeableEnum.ADDONLY
				&& eAssociation.getZEndIsChangeable() != ChangeableEnum.ADD_ONLY_LITERAL) {
			eAssociation.setZEndIsChangeable(ChangeableEnum.ADD_ONLY_LITERAL);
		} else if (zEnd.getChangeable() == EChangeableEnum.FROZEN
				&& eAssociation.getZEndIsChangeable() != ChangeableEnum.FROZEN_LITERAL) {
			eAssociation.setZEndIsChangeable(ChangeableEnum.FROZEN_LITERAL);
		} else if (zEnd.getChangeable() == EChangeableEnum.NONE
				&& eAssociation.getZEndIsChangeable() != ChangeableEnum.NONE_LITERAL) {
			eAssociation.setZEndIsChangeable(ChangeableEnum.NONE_LITERAL);
		}

	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
	}

}
