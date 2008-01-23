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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.TigerstripeElementTypes;

/**
 * @generated
 */
public class MapItemSemanticEditPolicy extends
		TigerstripeBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	@Override
	protected Command getCreateCommand(CreateElementRequest req) {
		if (TigerstripeElementTypes.NamedQueryArtifact_1001 == req
				.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(VisualeditorPackage.eINSTANCE
						.getMap_Artifacts());
			}
			return getMSLWrapper(new CreateNamedQueryArtifact_1001Command(req));
		}
		if (TigerstripeElementTypes.ExceptionArtifact_1002 == req
				.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(VisualeditorPackage.eINSTANCE
						.getMap_Artifacts());
			}
			return getMSLWrapper(new CreateExceptionArtifact_1002Command(req));
		}
		if (TigerstripeElementTypes.ManagedEntityArtifact_1003 == req
				.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(VisualeditorPackage.eINSTANCE
						.getMap_Artifacts());
			}
			return getMSLWrapper(new CreateManagedEntityArtifact_1003Command(
					req));
		}
		if (TigerstripeElementTypes.NotificationArtifact_1004 == req
				.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(VisualeditorPackage.eINSTANCE
						.getMap_Artifacts());
			}
			return getMSLWrapper(new CreateNotificationArtifact_1004Command(req));
		}
		if (TigerstripeElementTypes.DatatypeArtifact_1005 == req
				.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(VisualeditorPackage.eINSTANCE
						.getMap_Artifacts());
			}
			return getMSLWrapper(new CreateDatatypeArtifact_1005Command(req));
		}
		if (TigerstripeElementTypes.Enumeration_1006 == req.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(VisualeditorPackage.eINSTANCE
						.getMap_Artifacts());
			}
			return getMSLWrapper(new CreateEnumeration_1006Command(req));
		}
		if (TigerstripeElementTypes.UpdateProcedureArtifact_1007 == req
				.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(VisualeditorPackage.eINSTANCE
						.getMap_Artifacts());
			}
			return getMSLWrapper(new CreateUpdateProcedureArtifact_1007Command(
					req));
		}
		if (TigerstripeElementTypes.SessionFacadeArtifact_1008 == req
				.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(VisualeditorPackage.eINSTANCE
						.getMap_Artifacts());
			}
			return getMSLWrapper(new CreateSessionFacadeArtifact_1008Command(
					req));
		}
		if (TigerstripeElementTypes.AssociationClassClass_1009 == req
				.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(VisualeditorPackage.eINSTANCE
						.getMap_Artifacts());
			}
			return getMSLWrapper(new CreateAssociationClassClass_1009Command(
					req));
		}
		return super.getCreateCommand(req);
	}

	/**
	 * @generated
	 */
	private static class CreateNamedQueryArtifact_1001Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateNamedQueryArtifact_1001Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
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
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	private static class CreateExceptionArtifact_1002Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateExceptionArtifact_1002Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
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
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	private static class CreateManagedEntityArtifact_1003Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateManagedEntityArtifact_1003Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
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
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	private static class CreateNotificationArtifact_1004Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateNotificationArtifact_1004Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
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
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	private static class CreateDatatypeArtifact_1005Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateDatatypeArtifact_1005Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
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
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	private static class CreateEnumeration_1006Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateEnumeration_1006Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
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
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	private static class CreateUpdateProcedureArtifact_1007Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateUpdateProcedureArtifact_1007Command(
				CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
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
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	private static class CreateSessionFacadeArtifact_1008Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateSessionFacadeArtifact_1008Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
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
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	private static class CreateAssociationClassClass_1009Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateAssociationClassClass_1009Command(CreateElementRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
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
