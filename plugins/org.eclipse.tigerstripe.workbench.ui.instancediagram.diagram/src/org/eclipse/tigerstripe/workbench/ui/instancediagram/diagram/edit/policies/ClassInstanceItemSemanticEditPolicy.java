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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateRelationshipCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.dialogs.AssociationInstanceEditDialog;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers.InstanceElementTypes;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

/**
 * @generated
 */
public class ClassInstanceItemSemanticEditPolicy extends
		InstanceBaseItemSemanticEditPolicy {

	/**
	 * @generated NOT
	 */
	@Override
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		return getMSLWrapper(new DestroyElementCommand(req) {

			@Override
			protected EObject getElementToDestroy() {
				View view = (View) getHost().getModel();
				EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
				if (annotation != null)
					return view;
				return super.getElementToDestroy();
			}

			/*
			 * added this method to the auto-generated inner class so that the
			 * association instances that are related to an association class
			 * instance will be removed from the underlying model when the
			 * association class instance is removed (without this additional
			 * method, the association class instance is removed from the view
			 * and the underlying model, but the association instances are only
			 * removed from the view). (non-Javadoc)
			 * 
			 * @see org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand#doExecuteWithResult(org.eclipse.core.runtime.IProgressMonitor,
			 *      org.eclipse.core.runtime.IAdaptable)
			 */
			@Override
			protected CommandResult doExecuteWithResult(
					IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				ClassInstanceEditPart classEditPart = (ClassInstanceEditPart) getHost();
				View view = (View) classEditPart.getModel();
				ClassInstance classInstance = (ClassInstance) view.getElement();
				View mapView = (View) classEditPart.getParent().getModel();
				InstanceMap map = (InstanceMap) mapView.getElement();
				if (classInstance.isAssociationClassInstance()) {
					String aEndName = classInstance.getArtifactName()
							+ "::aEnd";
					String zEndName = classInstance.getArtifactName()
							+ "::zEnd";
					EList assocInstances = map.getAssociationInstances();
					List associations = new ArrayList();
					associations.addAll(assocInstances);
					for (Object obj : associations) {
						AssociationInstance assocInstance = (AssociationInstance) obj;
						if (assocInstance.getArtifactName() != null
								&& (assocInstance.getArtifactName().equals(
										aEndName) || assocInstance
										.getArtifactName().equals(zEndName))) {
							map.getAssociationInstances().remove(assocInstance);
						}
					}
				}
				// check to see if any of the association instances linked to
				// this class
				// are part of an association class, if so then need to remove
				// the parts
				// "by hand" (the two association instances and the association
				// class' class
				// instance) since the autogenerated code won't do that
				EList associations = map.getAssociationInstances();
				Set<String> assocClassNames = new HashSet<String>();
				for (Object obj : associations) {
					AssociationInstance association = (AssociationInstance) obj;
					String artifactName = association.getArtifactName();
					if (association.getAEnd() == classInstance
							&& artifactName != null
							&& artifactName.endsWith("::aEnd")) {
						int pos = artifactName.lastIndexOf("::aEnd");
						assocClassNames.add(artifactName.substring(0, pos));
					} else if (association.getZEnd() == classInstance
							&& artifactName != null
							&& artifactName.endsWith("::zEnd")) {
						int pos = artifactName.lastIndexOf("::zEnd");
						assocClassNames.add(artifactName.substring(0, pos));
					}
				}
				// loop through the association classes that this class is a
				// part of (i.e. those
				// association classes that this class is an endpoint for)
				for (String assocClassName : assocClassNames) {
					// and for each of them, remove the entire association class
					// (all three peices)
					Map<String, AssociationInstance> assocMap = new HashMap<String, AssociationInstance>();
					String aEndName = assocClassName + "::aEnd";
					String zEndName = assocClassName + "::zEnd";
					// first, put together a map of the association instances
					// that are part of the
					// association class in question
					for (Object obj : associations) {
						AssociationInstance association = (AssociationInstance) obj;
						if (association.getArtifactName() != null
								&& (association.getArtifactName().equals(
										aEndName) || association
										.getArtifactName().equals(zEndName)))
							assocMap.put(association.getArtifactName(),
									association);
					}
					// then find the class instance that is part of that
					// association class
					// and remove it
					EList classInstances = map.getClassInstances();
					for (Object obj : classInstances) {
						ClassInstance lclClassInstance = (ClassInstance) obj;
						if (lclClassInstance.getArtifactName().equals(
								assocClassName)) {
							map.getClassInstances().remove(lclClassInstance);
							break;
						}
					}
					// and remove the association instances that are part of the
					// association class
					// from the map
					map.getAssociationInstances().removeAll(assocMap.values());
				}
				return super.doExecuteWithResult(monitor, info);
			}

		});
	}

	/**
	 * @generated
	 */
	@Override
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
		if (InstanceElementTypes.AssociationInstance_3001 == req
				.getElementType())
			return req.getTarget() == null ? getCreateStartOutgoingAssociationInstance3001Command(req)
					: getCreateCompleteIncomingAssociationInstance3001Command(req);
		return super.getCreateRelationshipCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getCreateStartOutgoingAssociationInstance3001Command(
			CreateRelationshipRequest req) {
		return new Command() {
		};
	}

	/**
	 * @generated NOT
	 */
	protected Command getCreateCompleteIncomingAssociationInstance3001Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof Instance))
			return UnexecutableCommand.INSTANCE;
		final InstanceMap element = (InstanceMap) getRelationshipContainer(req
				.getSource(),
				InstancediagramPackage.eINSTANCE.getInstanceMap(), req
						.getElementType());
		if (element == null)
			return UnexecutableCommand.INSTANCE;
		if (req.getContainmentFeature() == null) {
			req.setContainmentFeature(InstancediagramPackage.eINSTANCE
					.getInstanceMap_AssociationInstances());
		}
		return getMSLWrapper(new CreateIncomingAssociationInstance3001Command(
				req, this) {

			/**
			 * @generated
			 */
			@Override
			protected EObject getElementToEdit() {
				return element;
			}
		});
	}

	/**
	 * @generated
	 */
	private static class CreateIncomingAssociationInstance3001Command extends
			CreateRelationshipCommand {

		ClassInstanceItemSemanticEditPolicy policy;

		/**
		 * @generated NOT
		 */
		public CreateIncomingAssociationInstance3001Command(
				CreateRelationshipRequest req,
				ClassInstanceItemSemanticEditPolicy policy) {
			super(req);
			this.policy = policy;
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return InstancediagramPackage.eINSTANCE.getInstanceMap();
		};

		/**
		 * @generated
		 */
		@Override
		protected void setElementToEdit(EObject element) {
			throw new UnsupportedOperationException();
		}

		/**
		 * @generated NOT
		 */
		@Override
		protected EObject doDefaultElementCreation() {
			CreateRelationshipRequest request = (CreateRelationshipRequest) getRequest();
			ClassInstanceEditPart classInstanceEditPart = (ClassInstanceEditPart) policy
					.getHost();
			InstanceMapEditPart mapEditPart = (InstanceMapEditPart) classInstanceEditPart
					.getParent();
			ClassInstance source = (ClassInstance) request.getSource();
			ClassInstance target = (ClassInstance) request.getTarget();
			View view = (View) mapEditPart.getModel();
			InstanceMap instanceMap = (InstanceMap) view.getElement();
			Shell shell = EclipsePlugin.getActiveWorkbenchShell();
			AssociationInstanceEditDialog ied = new AssociationInstanceEditDialog(
					shell, mapEditPart, source, target);
			int retVal = ied.open();
			if (retVal != IDialogConstants.OK_ID)
				throw new OperationCanceledException(
						ArtifactMetadataFactory.INSTANCE.getMetadata(
								IAssociationArtifactImpl.class.getName())
								.getLabel()
								+ " Instance Creation Cancelled");
			IRelationship rel = ied.getSelectedRelationship();
			if (rel == null)
				throw new OperationCanceledException(
						"No relationship type selected; "
								+ ArtifactMetadataFactory.INSTANCE.getMetadata(
										IAssociationArtifactImpl.class
												.getName()).getLabel()
								+ " Instance Creation Cancelled");
			/*
			 * check the multiplicity of the selected relationship, if it's
			 * limited to a single link between the two class types (aEnd and
			 * zEnd), then check for an existing association/associationClass
			 * between the source of this relationship and another instance in
			 * the diagram. Note: the order of this "if" statement is important,
			 * since an IAssociationClassArtifact is also an instance of an
			 * IAssociationArtifact
			 */
			int aEndMult = InstanceDiagramUtils
					.getRelationshipMuliplicity(((IAssociationArtifact) rel)
							.getAEnd().getMultiplicity());
			int zEndMult = InstanceDiagramUtils
					.getRelationshipMuliplicity(((IAssociationArtifact) rel)
							.getZEnd().getMultiplicity());
			boolean aEndIsSingle = (aEndMult == InstanceDiagramUtils.RELATIONSHIP_SINGLE);
			boolean zEndIsSingle = (zEndMult == InstanceDiagramUtils.RELATIONSHIP_SINGLE);
			// if the aEnd and zEnd multiplicities are single, look for existing
			// instances in the map
			if (rel instanceof IAssociationClassArtifact
					&& (aEndIsSingle || zEndIsSingle)) {
				IAssociationClassArtifact artifact = (IAssociationClassArtifact) rel;
				boolean matchFound = false;
				// look at class instances in map and check to see which are
				// actually
				// association class class instances
				List<ClassInstance> classesInMap = instanceMap
						.getClassInstances();
				for (ClassInstance instance : classesInMap) {
					if (instance.isAssociationClassInstance()) {
						// get the associations linked to this association class
						// class instance
						List<AssociationInstance> assocs = instance
								.getAssociations();
						for (AssociationInstance assoc : assocs) {
							// check to see if this assoc would need to be
							// replaced by the new association class
							// instance (based on the rules for multiplicity for
							// both the aEnd and zEnd)
							boolean matchingLinkExists = false;
							String relationshipStr = "";
							if (aEndIsSingle && zEndIsSingle) {
								matchingLinkExists = (assoc.getAEnd() == (Instance) getSource() || assoc
										.getZEnd() == (Instance) getTarget());
								relationshipStr = "one to one";
							} else if (aEndIsSingle) {
								matchingLinkExists = (assoc.getAEnd() != (Instance) getSource() && assoc
										.getZEnd() == (Instance) getTarget());
								relationshipStr = "one to many";
							} else if (zEndIsSingle) {
								matchingLinkExists = (assoc.getAEnd() == (Instance) getSource() && assoc
										.getZEnd() != (Instance) getTarget());
								relationshipStr = "many to one";
							}
							// if a matching link exists, check to make sure
							// that is has a matching
							// fully qualified name; if so, then have a matching
							// association class
							// that will have to be replace to maintain the
							// multiplicity constraints
							// defined for this association class
							if (matchingLinkExists
									&& instance.getName() != null
									&& instance.getFullyQualifiedName().equals(
											artifact.getFullyQualifiedName())) {
								matchFound = true;
								// warn the user that this new association class
								// will replace the existing one
								String warningStr = "An "
										+ ArtifactMetadataFactory.INSTANCE
												.getMetadata(
														IAssociationClassArtifactImpl.class
																.getName())
												.getLabel()
										+ " with '"
										+ relationshipStr
										+ "' multiplicity already exists between "
										+ "the class instance "
										+ assoc.getAEnd().getArtifactName()
										+ " and the class instance "
										+ assoc.getZEnd().getArtifactName()
										+ "; do you want to replace that "
										+ ArtifactMetadataFactory.INSTANCE
												.getMetadata(
														IAssociationClassArtifactImpl.class
																.getName())
												.getLabel()
										+ " class with this one?";
								String[] buttonLabels = new String[] { "OK",
										"Cancel" };
								int defButtonIdx = 1;
								MessageDialog warningDialog = new MessageDialog(
										shell,
										ArtifactMetadataFactory.INSTANCE
												.getMetadata(
														IAssociationClassArtifactImpl.class
																.getName())
												.getLabel()
												+ " replacement warning",
										(Image) null, warningStr,
										MessageDialog.WARNING, buttonLabels,
										defButtonIdx);
								int retIdx = warningDialog.open();
								// if they cancel the replacement, throw an
								// OperationCanceledException to abort adding
								// the new association class
								if (retIdx == defButtonIdx)
									throw new OperationCanceledException(
											ArtifactMetadataFactory.INSTANCE
													.getMetadata(
															IAssociationArtifactImpl.class
																	.getName())
													.getLabel()
													+ " Instance Replacement Cancelled");
								// if here, then need to delete the existing
								// association instance from the
								// instance map (so that it can be replaced with
								// the new one
								instanceMap.getClassInstances()
										.remove(instance);
								instanceMap.getAssociationInstances()
										.removeAll(assocs);
								break;
							}
						}
						if (matchFound)
							break;
					}
				}
			} else if (aEndIsSingle || zEndIsSingle) {
				IAssociationArtifact artifact = (IAssociationArtifact) rel;
				// loop through associations in the map and look for an existing
				// association
				// that matches this one
				List<AssociationInstance> assocsInMap = instanceMap
						.getAssociationInstances();
				for (AssociationInstance instance : assocsInMap) {
					// check to see if this assoc would need to be replaced by
					// the new association
					// instance (based on the rules for multiplicity for both
					// the aEnd and zEnd)
					boolean matchingLinkExists = false;
					boolean matchingLinkReverseExists = false; // Bug #910
					String relationshipStr = "";
					if (aEndIsSingle && zEndIsSingle) {
						matchingLinkExists = (instance.getAEnd() == (Instance) getSource() || instance
								.getZEnd() == (Instance) getTarget());

						// Bug 910: since instance diags only have one handle,
						// we must check the other
						// direction of drawing as well
						matchingLinkReverseExists = (instance.getZEnd() == (Instance) getSource() || instance
								.getAEnd() == (Instance) getTarget());
						relationshipStr = "one:one";
					} else if (aEndIsSingle) {
						matchingLinkExists = (instance.getAEnd() != (Instance) getSource() && instance
								.getZEnd() == (Instance) getTarget());
						relationshipStr = "one:many";
						matchingLinkReverseExists = (instance.getZEnd() == (Instance) getSource() && instance
								.getAEnd() != (Instance) getTarget());
					} else if (zEndIsSingle) {
						matchingLinkExists = (instance.getAEnd() == (Instance) getSource() && instance
								.getZEnd() != (Instance) getTarget());
						matchingLinkReverseExists = (instance.getZEnd() != (Instance) getSource() && instance
								.getAEnd() == (Instance) getTarget());
						relationshipStr = "many:one";
					}
					// if a matching link exists, check to make sure that is has
					// a matching
					// fully qualified name; if so, then have a matching
					// association that will
					// have to be replace to maintain the multiplicity
					// constraints defined for
					// this association
					if ((matchingLinkExists || matchingLinkReverseExists)
							&& instance.getName() != null
							&& instance.getFullyQualifiedName().equals(
									artifact.getFullyQualifiedName())) {
						// warn the user that this new association will replace
						// the existing one
						String warningStr = "An "
								+ ArtifactMetadataFactory.INSTANCE.getMetadata(
										IAssociationArtifactImpl.class
												.getName()).getLabel()
								+ " with '" + relationshipStr
								+ "' multiplicity already exists between ";
						if (matchingLinkExists) {
							warningStr += "the class instance "
									+ instance.getAEnd().getArtifactName()
									+ " and the class instance "
									+ instance.getZEnd().getArtifactName();
						} else if (matchingLinkReverseExists) {
							warningStr += "the class instance "
									+ instance.getZEnd().getArtifactName()
									+ " and the class instance "
									+ instance.getAEnd().getArtifactName();
						}
						warningStr += "; do you want to replace that "
								+ ArtifactMetadataFactory.INSTANCE.getMetadata(
										IAssociationArtifactImpl.class
												.getName()).getLabel()
								+ " with this one?";
						String[] buttonLabels = new String[] { "OK", "Cancel" };
						int defButtonIdx = 1;
						MessageDialog warningDialog = new MessageDialog(shell,
								ArtifactMetadataFactory.INSTANCE.getMetadata(
										IAssociationArtifactImpl.class
												.getName()).getLabel()
										+ " replacement warning", (Image) null,
								warningStr, MessageDialog.WARNING,
								buttonLabels, defButtonIdx);
						int retIdx = warningDialog.open();
						// if they cancel the replacement, throw an
						// OperationCanceledException to abort adding
						// the new association
						if (retIdx == defButtonIdx)
							throw new OperationCanceledException(
									ArtifactMetadataFactory.INSTANCE
											.getMetadata(
													IAssociationArtifactImpl.class
															.getName())
											.getLabel()
											+ " Instance Replacement Cancelled");
						// if here, then need to delete the existing association
						// instance from the
						// instance map (so that it can be replaced with the new
						// one
						instanceMap.getAssociationInstances().remove(instance);
						break;
					}
				}
			}
			// as was the case above, the order of this test is important, since
			// an
			// IAssociationClassArtifact is also an instance of an
			// IAssociationArtifact
			boolean isNormalInstance = !ied.isReversedInstance();
			if (rel instanceof IAssociationClassArtifact) {
				String relationshipName = ied.getInstanceName();
				IAssociationClassArtifact artifact = (IAssociationClassArtifact) rel;
				ClassInstance assocClassClass = InstancediagramFactory.eINSTANCE
						.createClassInstance();
				assocClassClass.setArtifactName(relationshipName);
				assocClassClass.setName(artifact.getName());
				assocClassClass.setPackage(artifact.getPackage());
				assocClassClass.setAssociationClassInstance(true);
				instanceMap.getClassInstances().add(assocClassClass);
				// use new element (created above) to connect source to new
				// AssociationClass class instance object
				AssociationInstance assocInstance1 = InstancediagramFactory.eINSTANCE
						.createAssociationInstance();
				initializeAssociationParams(assocInstance1);
				if (isNormalInstance) {
					// if it's a normal instance, then instance1 is set up as
					// aEnd
					// association instance
					assocInstance1.setAEnd((Instance) getSource());
					assocInstance1.setAEndName(rel.getRelationshipAEnd()
							.getName());
					assocInstance1.setZEnd(assocClassClass);
					assocInstance1.setZEndName("");
					assocInstance1.setArtifactName(relationshipName + "::aEnd");
					assocInstance1.setName(artifact.getName());
					assocInstance1.setPackage(artifact.getPackage());
					assocInstance1.setAEndIsNavigable(artifact.getAEnd()
							.isNavigable());
					AggregationEnum aEndAggregation = getAggregation(artifact
							.getAEnd().getAggregation());
					assocInstance1.setAEndAggregation(aEndAggregation);
				} else {
					// otherwise, instance1 is set up as the zEnd association
					// instance
					assocInstance1.setAEnd(assocClassClass);
					assocInstance1.setAEndName("");
					assocInstance1.setZEnd((Instance) getSource());
					assocInstance1.setZEndName(rel.getRelationshipZEnd()
							.getName());
					assocInstance1.setArtifactName(relationshipName + "::zEnd");
					assocInstance1.setName(artifact.getName());
					assocInstance1.setPackage(artifact.getPackage());
					assocInstance1.setZEndIsNavigable(artifact.getZEnd()
							.isNavigable());
					AggregationEnum zEndAggregation = getAggregation(artifact
							.getZEnd().getAggregation());
					assocInstance1.setZEndAggregation(zEndAggregation);
				}
				assocClassClass.getAssociations().add(assocInstance1);
				instanceMap.getAssociationInstances().add(assocInstance1);
				// and create a second element to connect the new
				// AssociationClass
				// class instance to the target
				AssociationInstance assocInstance2 = InstancediagramFactory.eINSTANCE
						.createAssociationInstance();
				initializeAssociationParams(assocInstance2);
				if (isNormalInstance) {
					// if it's a normal instance, then instance2 is set up as
					// zEnd
					// association instance
					assocInstance2.setAEnd(assocClassClass);
					assocInstance2.setAEndName("");
					assocInstance2.setZEnd((Instance) getTarget());
					assocInstance2.setZEndName(rel.getRelationshipZEnd()
							.getName());
					assocInstance2.setArtifactName(relationshipName + "::zEnd");
					assocInstance2.setName(artifact.getName());
					assocInstance2.setPackage(artifact.getPackage());
					assocInstance2.setZEndIsNavigable(artifact.getZEnd()
							.isNavigable());
					AggregationEnum zEndAggregation = getAggregation(artifact
							.getZEnd().getAggregation());
					assocInstance2.setZEndAggregation(zEndAggregation);
				} else {
					// otherwise, instance2 is set up as the aEnd association
					// instance
					assocInstance2.setAEnd((Instance) getTarget());
					assocInstance2.setAEndName(rel.getRelationshipAEnd()
							.getName());
					assocInstance2.setZEnd(assocClassClass);
					assocInstance2.setZEndName("");
					assocInstance2.setArtifactName(relationshipName + "::aEnd");
					assocInstance2.setName(artifact.getName());
					assocInstance2.setPackage(artifact.getPackage());
					assocInstance2.setAEndIsNavigable(artifact.getAEnd()
							.isNavigable());
					AggregationEnum aEndAggregation = getAggregation(artifact
							.getAEnd().getAggregation());
					assocInstance2.setAEndAggregation(aEndAggregation);
				}
				assocClassClass.getAssociations().add(assocInstance2);
				instanceMap.getAssociationInstances().add(assocInstance2);
				return assocInstance2;
			} else {
				// if here, need to create a new association instance
				AssociationInstance newElement = (AssociationInstance) super
						.doDefaultElementCreation();
				IAssociationArtifact artifact = (IAssociationArtifact) rel;
				// set the name and package values for the association
				newElement.setName(artifact.getName());
				newElement.setPackage(artifact.getPackage());
				// now initialize the ends with parameters from the artifact,
				// first the aEnd
				if (isNormalInstance)
					newElement.setAEnd((Instance) getSource());
				else
					newElement.setAEnd((Instance) getTarget());
				newElement.setAEndName(rel.getRelationshipAEnd().getName());
				newElement.setAEndIsNavigable(artifact.getAEnd().isNavigable());
				AggregationEnum aEndAggregation = getAggregation(artifact
						.getAEnd().getAggregation());
				newElement.setAEndAggregation(aEndAggregation);
				// then the zEnd
				if (isNormalInstance)
					newElement.setZEnd((Instance) getTarget());
				else
					newElement.setZEnd((Instance) getSource());
				newElement.setZEndName(rel.getRelationshipZEnd().getName());
				newElement.setZEndIsNavigable(artifact.getZEnd().isNavigable());
				AggregationEnum zEndAggregation = getAggregation(artifact
						.getZEnd().getAggregation());
				newElement.setZEndAggregation(zEndAggregation);
				return newElement;
			}
		}

		private void initializeAssociationParams(AssociationInstance association) {

			association.setAEndMultiplicityLowerBound("1");
			association.setAEndMultiplicityUpperBound("");

			association.setZEndMultiplicityLowerBound("1");
			association.setZEndMultiplicityUpperBound("");
		}

		private AggregationEnum getAggregation(EAggregationEnum endAggregation) {
			if (endAggregation == EAggregationEnum.COMPOSITE)
				return AggregationEnum.COMPOSITE_LITERAL;
			else if (endAggregation == EAggregationEnum.SHARED)
				return AggregationEnum.SHARED_LITERAL;
			else if (endAggregation == EAggregationEnum.NONE)
				return AggregationEnum.NONE_LITERAL;
			throw new IllegalArgumentException("Illegal value "
					+ endAggregation + " found");
		}

	}
}
