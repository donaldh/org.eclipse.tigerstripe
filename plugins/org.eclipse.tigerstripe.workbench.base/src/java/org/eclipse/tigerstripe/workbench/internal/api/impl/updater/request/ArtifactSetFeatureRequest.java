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
package org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;

public class ArtifactSetFeatureRequest extends BaseArtifactElementRequest
		implements IArtifactSetFeatureRequest {

	private String featureId;

	private String featureValue;

	private String oldFeatureValue;

	public String getOldValue() {
		return this.oldFeatureValue;
	}

	public void setOldValue(String oldValue) {
		this.oldFeatureValue = oldValue;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		super.canExecute(mgrSession);
		try {
			IAbstractArtifact art = mgrSession
					.getArtifactByFullyQualifiedName(getArtifactFQN());

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
					|| AENDMULTIPLICITY.equals(featureId)
					|| ZEND.equals(featureId) || ZENDName.equals(featureId)
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
		} catch (TigerstripeException t) {
			return false;
		}
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		super.execute(mgrSession);
		IAbstractArtifact art = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());
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
			artifact.setExtendedArtifact(target);
			artifact.doSave(new NullProgressMonitor());
		} else if (ISABSTRACT_FEATURE.equals(featureId)) {
			IAbstractArtifact artifact = (IAbstractArtifact) art;
			artifact.setAbstract("true".equals(featureValue));
			artifact.doSave(new NullProgressMonitor());
		} else if (RETURNED_TYPE.equals(featureId)
				&& art instanceof IQueryArtifact) {
			IQueryArtifact artifact = (IQueryArtifact) art;

			IType type = null;

			if (featureValue != null) {
				type = artifact.makeType();
				type.setFullyQualifiedName(featureValue);
			}
			artifact.setReturnedType(type);
			artifact.doSave(new NullProgressMonitor());
		} else if (BASE_TYPE.equals(featureId) && art instanceof IEnumArtifact) {
			IEnumArtifact artifact = (IEnumArtifact) art;

			IType type = null;

			if (featureValue != null) {
				type = artifact.makeLiteral().makeType();
				type.setFullyQualifiedName(featureValue);
			}
			artifact.setBaseType(type);
			artifact.doSave(new NullProgressMonitor());
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
					targetEnd.setMultiplicity(IModelComponent.EMultiplicity
							.parse(featureValue));
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
						targetEnd.setVisibility(EVisibility.PUBLIC);
					else if ("PROTECTED".equals(featureValue))
						targetEnd.setVisibility(EVisibility.PROTECTED);
					else if ("PRIVATE".equals(featureValue))
						targetEnd.setVisibility(EVisibility.PRIVATE);
					else if ("PACKAGE".equals(featureValue))
						targetEnd.setVisibility(EVisibility.PACKAGE);
				} else {
					// It's the end itself that is changing!!
					IType type = targetEnd.makeType();
					type.setFullyQualifiedName(featureValue);
					type.setTypeMultiplicity(targetEnd.getMultiplicity());
					targetEnd.setType(type);
				}
				artifact.doSave(new NullProgressMonitor());
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
				dep.doSave(new NullProgressMonitor());
			}
		}

	}

	public void setFeatureId(String featureID) {
		this.featureId = featureID;
	}

	public void setFeatureValue(String value) {
		this.featureValue = value;
	}

	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = makeDelta(IModelChangeDelta.SET);
		delta.setFeature(featureId);
		delta.setNewValue(this.featureValue);
		delta.setOldValue(this.oldFeatureValue);
		return delta;
	}

}
