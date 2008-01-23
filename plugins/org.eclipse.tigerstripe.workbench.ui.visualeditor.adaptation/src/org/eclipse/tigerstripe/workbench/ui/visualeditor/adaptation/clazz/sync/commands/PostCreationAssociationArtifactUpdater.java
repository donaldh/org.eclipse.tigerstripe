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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.commands;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.utils.ClassDiagramUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class PostCreationAssociationArtifactUpdater extends
		BasePostCreationElementUpdater {

	protected Association association;

	public PostCreationAssociationArtifactUpdater(IAbstractArtifact iArtifact,
			Association association, Map map, ITigerstripeProject diagramProject) {
		super(iArtifact, map, diagramProject);
		this.association = association;
	}

	@Override
	public void updateConnections() throws TigerstripeException {

		try {
			BaseETAdapter.setIgnoreNotify(true);
			IAssociationArtifact iAssoc = (IAssociationArtifact) getIArtifact();
			MapHelper helper = new MapHelper((Map) association.eContainer());

			association.setIsReadonly(getIArtifact().isReadonly());

			updateStereotype(association);

			IAssociationEnd aEnd = iAssoc.getAEnd();
			AbstractArtifact target = helper.findAbstractArtifactFor(aEnd
					.getIType().getFullyQualifiedName());
			// association.setAEnd(target); // Already done upon creation thru
			// drop
			association.setAEndIsNavigable(aEnd.isNavigable());
			association.setAEndIsOrdered(aEnd.isOrdered());
			association.setAEndIsUnique(aEnd.isUnique());
			association.setAEndName(aEnd.getName());
			association.setAEndVisibility(ClassDiagramUtils.toVisibility(aEnd
					.getVisibility()));
			if (aEnd.getMultiplicity() == EMultiplicity.ONE) {
				association.setAEndMultiplicity(AssocMultiplicity.ONE_LITERAL);
			} else if (aEnd.getMultiplicity() == EMultiplicity.ONE_STAR) {
				association
						.setAEndMultiplicity(AssocMultiplicity.ONE_STAR_LITERAL);
			} else if (aEnd.getMultiplicity() == EMultiplicity.STAR) {
				association.setAEndMultiplicity(AssocMultiplicity.STAR_LITERAL);
			} else if (aEnd.getMultiplicity() == EMultiplicity.ZERO) {
				association.setAEndMultiplicity(AssocMultiplicity.ZERO_LITERAL);
			} else if (aEnd.getMultiplicity() == EMultiplicity.ZERO_ONE) {
				association
						.setAEndMultiplicity(AssocMultiplicity.ZERO_ONE_LITERAL);
			} else if (aEnd.getMultiplicity() == EMultiplicity.ZERO_STAR) {
				association
						.setAEndMultiplicity(AssocMultiplicity.ZERO_STAR_LITERAL);
			}
			if (aEnd.getAggregation() == EAggregationEnum.NONE) {
				association.setAEndAggregation(AggregationEnum.NONE_LITERAL);
			} else if (aEnd.getAggregation() == EAggregationEnum.COMPOSITE) {
				association
						.setAEndAggregation(AggregationEnum.COMPOSITE_LITERAL);
			} else if (aEnd.getAggregation() == EAggregationEnum.SHARED) {
				association.setAEndAggregation(AggregationEnum.SHARED_LITERAL);
			}
			if (aEnd.getChangeable() == EChangeableEnum.ADDONLY) {
				association
						.setAEndIsChangeable(ChangeableEnum.ADD_ONLY_LITERAL);
			} else if (aEnd.getChangeable() == EChangeableEnum.FROZEN) {
				association.setAEndIsChangeable(ChangeableEnum.FROZEN_LITERAL);
			} else if (aEnd.getChangeable() == EChangeableEnum.NONE) {
				association.setAEndIsChangeable(ChangeableEnum.NONE_LITERAL);
			}

			IAssociationEnd zEnd = iAssoc.getZEnd();
			target = helper.findAbstractArtifactFor(aEnd.getIType()
					.getFullyQualifiedName());
			// association.setZEnd(target);// Already done upon creation thru
			// drop
			association.setZEndIsNavigable(zEnd.isNavigable());
			association.setZEndIsUnique(zEnd.isUnique());
			association.setZEndIsOrdered(zEnd.isOrdered());
			association.setZEndName(zEnd.getName());
			association.setZEndVisibility(ClassDiagramUtils.toVisibility(zEnd
					.getVisibility()));
			if (zEnd.getMultiplicity() == EMultiplicity.ONE) {
				association.setZEndMultiplicity(AssocMultiplicity.ONE_LITERAL);
			} else if (zEnd.getMultiplicity() == EMultiplicity.ONE_STAR) {
				association
						.setZEndMultiplicity(AssocMultiplicity.ONE_STAR_LITERAL);
			} else if (zEnd.getMultiplicity() == EMultiplicity.STAR) {
				association.setZEndMultiplicity(AssocMultiplicity.STAR_LITERAL);
			} else if (zEnd.getMultiplicity() == EMultiplicity.ZERO) {
				association.setZEndMultiplicity(AssocMultiplicity.ZERO_LITERAL);
			} else if (zEnd.getMultiplicity() == EMultiplicity.ZERO_ONE) {
				association
						.setZEndMultiplicity(AssocMultiplicity.ZERO_ONE_LITERAL);
			} else if (zEnd.getMultiplicity() == EMultiplicity.ZERO_STAR) {
				association
						.setZEndMultiplicity(AssocMultiplicity.ZERO_STAR_LITERAL);
			}
			if (zEnd.getAggregation() == EAggregationEnum.NONE) {
				association.setZEndAggregation(AggregationEnum.NONE_LITERAL);
			} else if (zEnd.getAggregation() == EAggregationEnum.COMPOSITE) {
				association
						.setZEndAggregation(AggregationEnum.COMPOSITE_LITERAL);
			} else if (zEnd.getAggregation() == EAggregationEnum.SHARED) {
				association.setZEndAggregation(AggregationEnum.SHARED_LITERAL);
			}
			if (zEnd.getChangeable() == EChangeableEnum.ADDONLY) {
				association
						.setZEndIsChangeable(ChangeableEnum.ADD_ONLY_LITERAL);
			} else if (zEnd.getChangeable() == EChangeableEnum.FROZEN) {
				association.setZEndIsChangeable(ChangeableEnum.FROZEN_LITERAL);
			} else if (zEnd.getChangeable() == EChangeableEnum.NONE) {
				association.setZEndIsChangeable(ChangeableEnum.NONE_LITERAL);
			}
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}
	}

}
