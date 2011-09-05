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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.commands;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.Stereotype;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;

public class AssociationUpdateCommand extends AbstractTransactionalCommand {

	private IArtifactManagerSession artMgrSession;
	private final Association association;
	private final Map<String, Object> changedValuesMap;

	public AssociationUpdateCommand(TransactionalEditingDomain domain,
			MapEditPart mapEditPart, Association association,
			Map<String, Object> changedValuesMap) {
		super(domain, "Update association " + association, null);
		this.association = association;
		this.changedValuesMap = changedValuesMap;
		DiagramGraphicalViewer mapViewer = (DiagramGraphicalViewer) mapEditPart
				.getViewer();
		DiagramEditDomain diagramDomain = (DiagramEditDomain) mapViewer
				.getEditDomain();
		IResource res = (IResource) diagramDomain.getEditorPart()
				.getEditorInput().getAdapter(IResource.class);
		IAbstractTigerstripeProject aProject = (IAbstractTigerstripeProject) res
				.getProject().getAdapter(IAbstractTigerstripeProject.class);
		if (!(aProject instanceof ITigerstripeModelProject))
			throw new RuntimeException("non-Tigerstripe Project found");
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		try {
			artMgrSession = project.getArtifactManagerSession();
		} catch (TigerstripeException e) {
			throw new RuntimeException("IArtifactManagerSession not found");
		}
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	protected CommandResult doExecuteWithResult(
			IProgressMonitor progressMonitor, IAdaptable info)
			throws ExecutionException {
		if (!changedValuesMap.isEmpty()) {
			try {
				IAssociationArtifact iAssociation = (IAssociationArtifact) artMgrSession
						.getArtifactByFullyQualifiedName(association
								.getFullyQualifiedName()).makeWorkingCopy(null);
				IAssociationEnd iAssociationAEnd = (IAssociationEnd) iAssociation
						.getAEnd();
				IAssociationEnd iAssociationZEnd = (IAssociationEnd) iAssociation
						.getZEnd();
				
				// check to see if the stereotypes for the association have
				// changed
				if (changedValuesMap.containsKey("assocStereotypes")) {
					// have to set the stereotypes from the iArtifact side;
					// there is
					// no concept of a "Stereotype" object on the EObject side
					// of
					// things
					List<Stereotype> newAssocStereotypeVals = (List<Stereotype>) changedValuesMap
							.get("assocStereotypes");
					String newAssocStereotypeLabel = getStereotypeLabel(newAssocStereotypeVals);
					List assocSteoreotypes = association.getStereotypes();
					String oldStereotypeLabel = getStereotypeLabel(assocSteoreotypes);
					if (!assocSteoreotypes.equals(newAssocStereotypeLabel)) {
						removeStereotypes(iAssociation);
						for (Stereotype newAssocStereotypeVal : newAssocStereotypeVals) {
							iAssociation
									.addStereotypeInstance(newAssocStereotypeVal
											.makeInstance());
						}
					}
				}
				// then, check to see which aEnd properties have changed, first
				// the
				// aEnd
				// role name
				if (changedValuesMap.containsKey("aEndName")) {
					String newName = (String) changedValuesMap.get("aEndName");
					if ((association.getAEndName()==null) || (!association.getAEndName().equals(newName)))
						iAssociationAEnd.setName(newName);
				}
				// next, the aEnd multiplicity value
				if (changedValuesMap.containsKey("aEndMultiplicity")) {
					String newMultiplicityStr = (String) changedValuesMap
							.get("aEndMultiplicity");
					AssocMultiplicity assocMult = association
							.getAEndMultiplicity();
					if (!assocMult.toString().equals(newMultiplicityStr)) {
						iAssociationAEnd
								.setMultiplicity(IModelComponent.EMultiplicity
										.parse(newMultiplicityStr));
					}
				}
				// next, the aEnd visibility value
				if (changedValuesMap.containsKey("aEndVisibility")) {
					String newVisibilityStr = (String) changedValuesMap
							.get("aEndVisibility");
					Visibility oldVisibility = association.getAEndVisibility();
					if (!oldVisibility.toString().equals(newVisibilityStr)) {
						iAssociationAEnd.setVisibility(EVisibility
								.parse(newVisibilityStr));
					}
				}
				// next, the aEnd aggregation value
				if (changedValuesMap.containsKey("aEndAggregation")) {
					String newAggregationStr = (String) changedValuesMap
							.get("aEndAggregation");
					AggregationEnum oldAggregation = association
							.getAEndAggregation();
					if (!oldAggregation.toString().equals(newAggregationStr)) {
						iAssociationAEnd.setAggregation(EAggregationEnum
								.parse(newAggregationStr));
					}
				}
				// and finally, the flags for the aEnd (isNavigable, isUnique,
				// isOrdered)
				if (changedValuesMap.containsKey("aEndIsNavigable")) {
					Boolean newIsNavigable = (Boolean) changedValuesMap
							.get("aEndIsNavigable");
					if (association.isAEndIsNavigable() != newIsNavigable
							.booleanValue())
						iAssociationAEnd.setNavigable(newIsNavigable
								.booleanValue());
				}
				if (changedValuesMap.containsKey("aEndIsUnique")) {
					Boolean newIsUnique = (Boolean) changedValuesMap
							.get("aEndIsUnique");
					if (association.isAEndIsUnique() != newIsUnique
							.booleanValue())
						iAssociationAEnd.setUnique(newIsUnique.booleanValue());
				}
				if (changedValuesMap.containsKey("aEndIsOrdered")) {
					Boolean newIsOrdered = (Boolean) changedValuesMap
							.get("aEndIsOrdered");
					if (association.isAEndIsOrdered() != newIsOrdered
							.booleanValue())
						iAssociationAEnd
								.setOrdered(newIsOrdered.booleanValue());
				}

				// then, check to see which zEnd properties have changed, first
				// the
				// zEnd
				// role name
				if (changedValuesMap.containsKey("zEndName")) {
					String newName = (String) changedValuesMap.get("zEndName");
					if ((association.getZEndName()==null) || (!association.getZEndName().equals(newName)))
						iAssociationZEnd.setName(newName);
				}
				// next, the zEnd multiplicity value
				if (changedValuesMap.containsKey("zEndMultiplicity")) {
					String newMultiplicityStr = (String) changedValuesMap
							.get("zEndMultiplicity");
					AssocMultiplicity assocMult = association
							.getZEndMultiplicity();
					if (!assocMult.toString().equals(newMultiplicityStr)) {
						iAssociationZEnd
								.setMultiplicity(IModelComponent.EMultiplicity
										.parse(newMultiplicityStr));
					}
				}
				// next, the zEnd visibility value
				if (changedValuesMap.containsKey("zEndVisibility")) {
					String newVisibilityStr = (String) changedValuesMap
							.get("zEndVisibility");
					Visibility oldVisibility = association.getZEndVisibility();
					if (!oldVisibility.toString().equals(newVisibilityStr)) {
						iAssociationZEnd.setVisibility(EVisibility
								.parse(newVisibilityStr));
					}
				}
				// next, the zEnd aggregation value
				if (changedValuesMap.containsKey("zEndAggregation")) {
					String newAggregationStr = (String) changedValuesMap
							.get("zEndAggregation");
					AggregationEnum oldAggregation = association
							.getZEndAggregation();
					if (!oldAggregation.toString().equals(newAggregationStr)) {
						iAssociationZEnd.setAggregation(EAggregationEnum
								.parse(newAggregationStr));
					}
				}
				// and finally, the flags for the zEnd (isNavigable, isUnique,
				// isOrdered)
				if (changedValuesMap.containsKey("zEndIsNavigable")) {
					Boolean newIsNavigable = (Boolean) changedValuesMap
							.get("zEndIsNavigable");
					if (association.isZEndIsNavigable() != newIsNavigable
							.booleanValue())
						iAssociationZEnd.setNavigable(newIsNavigable
								.booleanValue());
				}
				if (changedValuesMap.containsKey("zEndIsUnique")) {
					Boolean newIsUnique = (Boolean) changedValuesMap
							.get("zEndIsUnique");
					if (association.isZEndIsUnique() != newIsUnique
							.booleanValue())
						iAssociationZEnd.setUnique(newIsUnique.booleanValue());
				}
				if (changedValuesMap.containsKey("zEndIsOrdered")) {
					Boolean newIsOrdered = (Boolean) changedValuesMap
							.get("zEndIsOrdered");
					if (association.isZEndIsOrdered() != newIsOrdered
							.booleanValue())
						iAssociationZEnd
								.setOrdered(newIsOrdered.booleanValue());
				}
				// finally, need to save the changes that have been made to the
				// iArtifact...this should
				// trigger an update of the associated eObject in the diagram
				try {
					iAssociation.doSave(new NullProgressMonitor());
				} catch (TigerstripeException e) {
					TigerstripeRuntime.logInfoMessage(
							"TigerstripeException detected", e);
				}
				// first check to see if the name has changed...if so, then set
				// the
				// association name to the new name
				if (changedValuesMap.containsKey("associationName")) {
					String newName = (String) changedValuesMap
							.get("associationName");
					if (!association.getName().equals(newName)) {
						if (association instanceof AssociationClass) {
							// if it's an AssociationClass instance, then need
							// to
							// rename the
							// AssociationClassClass too...order is important
							// here,
							// so make sure
							// that the ETAdapter is ignoring notifications, set
							// the
							// AssociationClass
							// name, turn on notifications for the ETAdapter
							// again,
							// and then set the
							// AssociationClassClass name...
							BaseETAdapter.setIgnoreNotify(true);
							association.setName(newName);
							BaseETAdapter.setIgnoreNotify(false);
							AssociationClassClass assocClassClass = ((AssociationClass) association)
									.getAssociatedClass();
							assocClassClass.setName(newName);
						} else {
							// otherwise, it's an association, so just rename it
							association.setName(newName);
						}
					}
				}
				
			} catch (TigerstripeException t) {
				return CommandResult.newErrorCommandResult(t);
			}
		}
		return CommandResult.newOKCommandResult();
	}

	private void removeStereotypes(IStereotypeCapable capable) {
		IStereotypeInstance[] existingInstances = getStereotypes(capable);
		for (IStereotypeInstance instance : existingInstances) {
			capable.removeStereotypeInstance(instance);
		}
	}

	private IStereotypeInstance[] getStereotypes(IStereotypeCapable capable) {
		Collection<IStereotypeInstance> instances = capable
				.getStereotypeInstances();
		return instances.toArray(new IStereotypeInstance[instances.size()]);
	}

	private String getStereotypeLabel(List stereotypeVals) {
		if (stereotypeVals.size() > 0) {
			StringBuffer buff = new StringBuffer();
			buff.append("<<");
			int count = 0;
			for (Object stereotypeVal : stereotypeVals) {
				buff.append(stereotypeVal.toString());
				if (++count < stereotypeVals.size())
					buff.append(", ");
			}
			buff.append(">>");
			return buff.toString();
		}
		return "";
	}

}
