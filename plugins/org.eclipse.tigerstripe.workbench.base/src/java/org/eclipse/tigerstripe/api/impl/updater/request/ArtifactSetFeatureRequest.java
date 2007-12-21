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
package org.eclipse.tigerstripe.api.impl.updater.request;

import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationEnd;
import org.eclipse.tigerstripe.api.artifacts.model.IDependencyArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IType;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IQueryArtifact;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextModelComponent;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class ArtifactSetFeatureRequest extends BaseArtifactElementRequest
		implements IArtifactSetFeatureRequest {

	private String featureId;

	private String featureValue;

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		IArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());

		if (EXTENDS_FEATURE.equals(featureId))
			return art != null;
		else if (ISABSTRACT_FEATURE.equals(featureId))
			return art != null;
		else if (RETURNED_TYPE.equals(featureId))
			return art instanceof IQueryArtifact;
		else if (BASE_TYPE.equals(featureId))
			return art instanceof IEnumArtifact;
		else if (AEND.equals(featureId) || AENDName.equals(featureId)
				|| AENDAGGREGATION.equals(featureId)
				|| AENDISCHANGEABLE.equals(featureId)
				|| AENDNAVIGABLE.equals(featureId)
				|| AENDISORDERED.equals(featureId)
				|| AENDVISIBILITY.equals(featureId)
				|| AENDISUNIQUE.equals(featureId)
				|| AENDMULTIPLICITY.equals(featureId) || ZEND.equals(featureId)
				|| ZENDName.equals(featureId)
				|| ZENDAGGREGATION.equals(featureId)
				|| ZENDNAVIGABLE.equals(featureId)
				|| ZENDISCHANGEABLE.equals(featureId)
				|| ZENDISORDERED.equals(featureId)
				|| ZENDMULTIPLICITY.equals(featureId)
				|| ZENDVISIBILITY.equals(featureId)
				|| ZENDISUNIQUE.equals(featureId))
			return (art instanceof IAssociationArtifact)
					|| (art instanceof IDependencyArtifact)
					&& featureValue != null;
		else
			return false;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IArtifact art = mgrSession
				.getIArtifactByFullyQualifiedName(getArtifactFQN());
		if (EXTENDS_FEATURE.equals(featureId)) {
			IAbstractArtifact artifact = (IAbstractArtifact) art;

			IAbstractArtifact target = null;
			if (featureValue == null) {
				// this is to remove the extends
				target = null;
			} else {
				target = mgrSession
						.getArtifactByFullyQualifiedName(featureValue);
			}
			artifact.setExtendedIArtifact(target);
			artifact.doSave(new TigerstripeNullProgressMonitor());
		} else if (ISABSTRACT_FEATURE.equals(featureId)) {
			IAbstractArtifact artifact = (IAbstractArtifact) art;
			artifact.setAbstract("true".equals(featureValue));
			artifact.doSave(new TigerstripeNullProgressMonitor());
		} else if (RETURNED_TYPE.equals(featureId)
				&& art instanceof IQueryArtifact) {
			IQueryArtifact artifact = (IQueryArtifact) art;

			IType type = null;

			if (featureValue != null) {
				type = artifact.makeIType();
				type.setFullyQualifiedName(featureValue);
			}
			artifact.setReturnedType(type);
			artifact.doSave(new TigerstripeNullProgressMonitor());
		} else if (BASE_TYPE.equals(featureId) && art instanceof IEnumArtifact) {
			IEnumArtifact artifact = (IEnumArtifact) art;

			IType type = null;

			if (featureValue != null) {
				type = artifact.makeILabel().makeIType();
				type.setFullyQualifiedName(featureValue);
			}
			artifact.setBaseType(type);
			artifact.doSave(new TigerstripeNullProgressMonitor());
		} else if (AEND.equals(featureId) || AENDName.equals(featureId)
				|| AENDAGGREGATION.equals(featureId)
				|| AENDISCHANGEABLE.equals(featureId)
				|| AENDNAVIGABLE.equals(featureId)
				|| AENDISORDERED.equals(featureId)
				|| AENDISUNIQUE.equals(featureId)
				|| AENDMULTIPLICITY.equals(featureId) || ZEND.equals(featureId)
				|| ZENDName.equals(featureId)
				|| ZENDISCHANGEABLE.equals(featureId)
				|| ZENDNAVIGABLE.equals(featureId)
				|| ZENDAGGREGATION.equals(featureId)
				|| ZENDISORDERED.equals(featureId)
				|| ZENDMULTIPLICITY.equals(featureId)
				|| AENDVISIBILITY.equals(featureId)
				|| ZENDVISIBILITY.equals(featureId)
				|| ZENDISUNIQUE.equals(featureId)) {
			if (art instanceof IAssociationArtifact) {
				IAssociationArtifact artifact = (IAssociationArtifact) art;
				IAssociationEnd targetEnd = null;
				if (featureId.startsWith("aEnd")) {
					targetEnd = (IAssociationEnd) artifact.getAEnd();
				} else {
					targetEnd = (IAssociationEnd) artifact.getZEnd();
				}

				if (featureId.endsWith("Aggregation")) {
					targetEnd.setAggregation(EAggregationEnum
							.parse(featureValue));
				} else if (featureId.endsWith("Multiplicity")) {
					targetEnd
							.setMultiplicity(EMultiplicity.parse(featureValue));
				} else if (featureId.endsWith("Navigable")) {
					boolean bool = Boolean.valueOf(featureValue);
					targetEnd.setNavigable(bool);
				} else if (featureId.endsWith("Ordered")) {
					boolean bool = Boolean.valueOf(featureValue);
					targetEnd.setOrdered(bool);
				} else if (featureId.endsWith("Changeable")) {
					targetEnd
							.setChangeable(EChangeableEnum.parse(featureValue));
				} else if (featureId.endsWith("Name")) {
					targetEnd.setName(featureValue);
				} else if (featureId.endsWith("Unique")) {
					targetEnd.setUnique(Boolean.valueOf(featureValue));
				} else if (featureId.endsWith("Visibility")) {
					if ("PUBLIC".equals(featureValue))
						targetEnd
								.setVisibility(IextModelComponent.VISIBILITY_PUBLIC);
					else if ("PROTECTED".equals(featureValue))
						targetEnd
								.setVisibility(IextModelComponent.VISIBILITY_PROTECTED);
					else if ("PRIVATE".equals(featureValue))
						targetEnd
								.setVisibility(IextModelComponent.VISIBILITY_PRIVATE);
					else if ("PACKAGE".equals(featureValue))
						targetEnd
								.setVisibility(IextModelComponent.VISIBILITY_PACKAGE);
				} else {
					// It's the end itself that is changing!!
					IType type = targetEnd.makeIType();
					type.setFullyQualifiedName(featureValue);
					type.setTypeMultiplicity(targetEnd.getMultiplicity());
					targetEnd.setType(type);
				}
				artifact.doSave(new TigerstripeNullProgressMonitor());
			} else if (art instanceof IDependencyArtifact) {
				IDependencyArtifact dep = (IDependencyArtifact) art;
				if (AEND.equals(featureId)) {
					IType type = (IType) dep.getAEndType();
					type.setFullyQualifiedName(featureValue);
					dep.setAEndType(type);
				} else {
					IType type = (IType) dep.getZEndType();
					type.setFullyQualifiedName(featureValue);
					dep.setZEndType(type);
				}
				dep.doSave(new TigerstripeNullProgressMonitor());
			}
		}

	}

	public void setFeatureId(String featureID) {
		this.featureId = featureID;
	}

	public void setFeatureValue(String value) {
		this.featureValue = value;
	}

}
