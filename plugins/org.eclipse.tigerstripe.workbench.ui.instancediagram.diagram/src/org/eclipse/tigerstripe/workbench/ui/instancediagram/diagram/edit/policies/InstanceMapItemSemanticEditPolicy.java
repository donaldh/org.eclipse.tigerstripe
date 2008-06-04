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

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.dialogs.ClassInstanceEditDialog;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers.InstanceElementTypes;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramReferenceMapper;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

/**
 * @generated
 */
public class InstanceMapItemSemanticEditPolicy extends
		InstanceBaseItemSemanticEditPolicy {

	/**
	 * @generated NOT
	 */
	@Override
	protected Command getCreateCommand(CreateElementRequest req) {
		if (InstanceElementTypes.ClassInstance_1001 == req.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(InstancediagramPackage.eINSTANCE
						.getInstanceMap_ClassInstances());
			}
			return getMSLWrapper(new CreateClassInstance_1001Command(req, this));
		}
		return super.getCreateCommand(req);
	}

	/**
	 * @generated
	 */
	private static class CreateClassInstance_1001Command extends
			CreateElementCommand {

		InstanceMapItemSemanticEditPolicy policy;

		/**
		 * @generated NOT
		 */
		public CreateClassInstance_1001Command(CreateElementRequest req,
				InstanceMapItemSemanticEditPolicy policy) {
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
		protected EObject getElementToEdit() {
			EObject container = ((CreateElementRequest) getRequest())
					.getContainer();
			if (container instanceof View) {
				container = ((View) container).getElement();
			}
			return container;
		}

		@Override
		protected EObject doDefaultElementCreation() {
			CreateElementRequest request = (CreateElementRequest) getRequest();
			IAbstractArtifact artifact = (IAbstractArtifact) request
					.getParameter("IAbstractArtifact");
			InstanceMapEditPart mapEditPart = (InstanceMapEditPart) policy
					.getHost();
			Shell shell = EclipsePlugin.getActiveWorkbenchShell();
			DiagramGraphicalViewer mapViewer = (DiagramGraphicalViewer) mapEditPart
					.getViewer();
			DiagramEditDomain diagramDomain = (DiagramEditDomain) mapViewer
					.getEditDomain();
			IResource res = (IResource) diagramDomain.getEditorPart()
					.getEditorInput().getAdapter(IResource.class);
			IAbstractTigerstripeProject aProject = (IAbstractTigerstripeProject) res
					.getProject().getAdapter(IAbstractTigerstripeProject.class);
			IArtifactManagerSession artMgrSession = null;
			if (!(aProject instanceof ITigerstripeModelProject))
				throw new RuntimeException("non-Tigerstripe Project found");
			ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
			try {
				artMgrSession = project.getArtifactManagerSession();
			} catch (TigerstripeException e) {
				throw new RuntimeException("IArtifactManagerSession not found");
			}
			if (artifact.isAbstract()) {
				String warningStr = "Cannot instantiate an abstract class; "
						+ "drag-and-drop operation cancelled";
				String[] buttonLabels = new String[] { "OK" };
				int defButtonIdx = 0;
				MessageDialog warningDialog = new MessageDialog(shell,
						"Abstract class detected", (Image) null, warningStr,
						MessageDialog.WARNING, buttonLabels, defButtonIdx);
				int retIdx = warningDialog.open();
				throw new OperationCanceledException(
						"Cannot Instantiate Abstract Class");
			} else if (!sessionFacadeInstancesEnabled()
					&& artifact instanceof ISessionArtifact) {
				String warningStr = "Your profile does not allow for instantiation "
						+ "of "
						+ ArtifactMetadataFactory.INSTANCE.getMetadata(
								ISessionArtifactImpl.class.getName()).getLabel(
								artifact)
						+ " objects; drag-and-drop operation cancelled";
				String[] buttonLabels = new String[] { "OK" };
				int defButtonIdx = 0;
				MessageDialog warningDialog = new MessageDialog(shell,
						ArtifactMetadataFactory.INSTANCE.getMetadata(
								ISessionArtifactImpl.class.getName()).getLabel(
								artifact)
								+ " detected", (Image) null, warningStr,
						MessageDialog.WARNING, buttonLabels, defButtonIdx);
				int retIdx = warningDialog.open();
				throw new OperationCanceledException("Cannot Instantiate "
						+ ArtifactMetadataFactory.INSTANCE.getMetadata(
								ISessionArtifactImpl.class.getName()).getLabel(
								artifact));
			}
			ClassInstanceEditDialog ied = new ClassInstanceEditDialog(shell,
					artifact, mapEditPart);
			int retVal = ied.open();
			if (retVal != IDialogConstants.OK_ID)
				throw new OperationCanceledException(
						"Class Instance Creation Cancelled");
			InstanceMap instanceMap = (InstanceMap) ((View) mapEditPart
					.getModel()).getElement();
			String instanceName = ied.getInstanceName();
			Instance instance = (Instance) super.doDefaultElementCreation();
			instance.setArtifactName(instanceName);
			instance.setName(artifact.getName());
			instance.setPackage(artifact.getPackage());
			if (ied.areAddingNewReferenceInstances()) {
				// need to create the requested class instances...
				HashMap<String, String> instanceNames = ied
						.getNewReferenceInstanceNames();
				HashMap<String, IType> instanceTypes = ied
						.getNewReferenceInstanceTypes();
				for (String fieldName : instanceNames.keySet()) {
					Instance newRefInstance = (Instance) super
							.doDefaultElementCreation();
					newRefInstance
							.setArtifactName(instanceNames.get(fieldName));
					IType type = instanceTypes.get(fieldName);
					newRefInstance.setName(type.getName());
					String fqn = type.getFullyQualifiedName();
					int idx = fqn.lastIndexOf(".");
					if (idx > 0)
						newRefInstance.setPackage(fqn.substring(0, idx));
					else
						newRefInstance.setPackage("");
					instanceMap.getClassInstances().add(newRefInstance);
				}
			}
			HashMap<String, List<Object>> selectionMap = ied.getSelection();
			if (instance instanceof ClassInstance) {
				ClassInstance classInstance = (ClassInstance) instance;
				EList eList = classInstance.getVariables();
				for (String name : selectionMap.keySet()) {
					List<Object> vals = selectionMap.get(name);
					IType type = (IType) vals.get(0);
					String value = (String) vals.get(1);
					if (value == null || "".equals(value))
						continue;
					// if here, have a non-empty value, so create a variable to
					// hold it
					Variable variable = InstancediagramFactory.eINSTANCE
							.createVariable();
					variable.setName(name);
					variable.setType(type.getName());
					variable.setValue(value);
					classInstance.getVariables().add(variable);
					// if the field isn't a primitive type, then make sure that
					// the
					// InstanceDiagramReferenceMapper is updated to ensure that
					// we
					// can determine which fields refer to which instances later
					if (!InstanceDiagramUtils.isPrimitive(artMgrSession, type)) {
						List<String> values = InstanceDiagramUtils
								.instanceReferencesAsList(value);
						InstanceDiagramReferenceMapper.eINSTANCE
								.addVariableReferences(variable, values);
					}
				}
			}
			return instance;
		}

	}

	/**
	 * @generated
	 */
	@Override
	protected Command getDuplicateCommand(DuplicateElementsRequest req) {
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
				.getEditingDomain();
		return getMSLWrapper(new DuplicateAnythingCommand(editingDomain, req));
	}

	private static boolean sessionFacadeInstancesEnabled() {
		GlobalSettingsProperty prop = (GlobalSettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.GLOBAL_SETTINGS);
		return prop
				.getPropertyValue(IGlobalSettingsProperty.ENABLE_SESSIONFACADE_ONINSTDIAG);
	}

	/**
	 * @generated
	 */
	private static class DuplicateAnythingCommand extends
			DuplicateEObjectsCommand {

		/**
		 * @generated
		 */
		public DuplicateAnythingCommand(
				TransactionalEditingDomain editingDomain,
				DuplicateElementsRequest req) {
			super(editingDomain, req.getLabel(), req
					.getElementsToBeDuplicated(), req
					.getAllDuplicatedElementsMap());
		}
	}
}
