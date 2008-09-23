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
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.CustomElementType;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.TigerstripeElementTypes;

/**
 * @generated
 */
public class MapItemSemanticEditPolicy extends
TigerstripeBaseItemSemanticEditPolicy {

	/**
	 * @generated NOT
	 */
	@Override
	protected Command getCreateCommand(CreateElementRequest req) {
		if (req.getElementType() instanceof CustomElementType){
			if (TigerstripeElementTypes.NamedQueryArtifact_1001 == ((CustomElementType) req.getElementType())
					.getBaseType()) {
				if (req.getContainmentFeature() == null) {
					req.setContainmentFeature(VisualeditorPackage.eINSTANCE
							.getMap_Artifacts());
				}
				return getMSLWrapper(new CreateCustomNamedQueryArtifact_1001Command(req));
			}
			if (TigerstripeElementTypes.ExceptionArtifact_1002 == ((CustomElementType) req.getElementType())
					.getBaseType()) {
				if (req.getContainmentFeature() == null) {
					req.setContainmentFeature(VisualeditorPackage.eINSTANCE
							.getMap_Artifacts());
				}
				return getMSLWrapper(new CreateCustomExceptionArtifact_1002Command(req));
			}

			if (TigerstripeElementTypes.ManagedEntityArtifact_1003 == ((CustomElementType) req.getElementType())
					.getBaseType()) {
				if (req.getContainmentFeature() == null) {
					req.setContainmentFeature(VisualeditorPackage.eINSTANCE
							.getMap_Artifacts());
				}
				return getMSLWrapper(new CreateCustomManagedEntityArtifact_1003Command(
						req));
			}

			if (TigerstripeElementTypes.NotificationArtifact_1004 == ((CustomElementType) req.getElementType())
					.getBaseType()) {
				if (req.getContainmentFeature() == null) {
					req.setContainmentFeature(VisualeditorPackage.eINSTANCE
							.getMap_Artifacts());
				}
				return getMSLWrapper(new CreateCustomNotificationArtifact_1004Command(req));
			}
			if (TigerstripeElementTypes.DatatypeArtifact_1005 == ((CustomElementType) req.getElementType())
					.getBaseType()) {
				if (req.getContainmentFeature() == null) {
					req.setContainmentFeature(VisualeditorPackage.eINSTANCE
							.getMap_Artifacts());
				}
				return getMSLWrapper(new CreateCustomDatatypeArtifact_1005Command(req));
			}
			if (TigerstripeElementTypes.Enumeration_1006 == ((CustomElementType) req.getElementType())
					.getBaseType()) {
				if (req.getContainmentFeature() == null) {
					req.setContainmentFeature(VisualeditorPackage.eINSTANCE
							.getMap_Artifacts());
				}
				return getMSLWrapper(new CreateCustomEnumeration_1006Command(req));
			}
			if (TigerstripeElementTypes.UpdateProcedureArtifact_1007 == ((CustomElementType) req.getElementType())
					.getBaseType()) {
				if (req.getContainmentFeature() == null) {
					req.setContainmentFeature(VisualeditorPackage.eINSTANCE
							.getMap_Artifacts());
				}
				return getMSLWrapper(new CreateCustomUpdateProcedureArtifact_1007Command(
						req));
			}
			if (TigerstripeElementTypes.SessionFacadeArtifact_1008 == ((CustomElementType) req.getElementType())
					.getBaseType()) {
				if (req.getContainmentFeature() == null) {
					req.setContainmentFeature(VisualeditorPackage.eINSTANCE
							.getMap_Artifacts());
				}
				return getMSLWrapper(new CreateCustomSessionFacadeArtifact_1008Command(
						req));
			}
			if (TigerstripeElementTypes.AssociationClassClass_1009 == ((CustomElementType) req.getElementType())
					.getBaseType()) {
				if (req.getContainmentFeature() == null) {
					req.setContainmentFeature(VisualeditorPackage.eINSTANCE
							.getMap_Artifacts());
				}
				return getMSLWrapper(new CreateCustomAssociationClassClass_1009Command(
						req));
			}
		}
		return super.getCreateCommand(req);
	}

	/**
	 * @generated NOT
	 */
	private static class CreateCustomNamedQueryArtifact_1001Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateCustomNamedQueryArtifact_1001Command(CreateElementRequest req) {
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
				if (getNewElement() instanceof NamedQueryArtifact){
					NamedQueryArtifact newElement = (NamedQueryArtifact) getNewElement();
					CustomElementType customType = ((CustomElementType) ((CreateElementRequest) getRequest()).getElementType());
					IArtifactPattern pattern = (IArtifactPattern) customType.getPattern();

					Map map = (Map) newElement.eContainer();
					try {
						IAbstractArtifact artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName());
						pattern.addToManager(tsProject, artifact);
						pattern.annotateArtifact(tsProject, artifact);
					} catch (TigerstripeException ex) {
						ex.printStackTrace();
					}
				}
			} catch (TigerstripeException e) {

			}
			return res;
		}

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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	private static class CreateCustomExceptionArtifact_1002Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateCustomExceptionArtifact_1002Command(CreateElementRequest req) {
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
				if (getNewElement() instanceof ExceptionArtifact){
					ExceptionArtifact newElement = (ExceptionArtifact) getNewElement();
					CustomElementType customType = ((CustomElementType) ((CreateElementRequest) getRequest()).getElementType());
					IArtifactPattern pattern = (IArtifactPattern) customType.getPattern();

					Map map = (Map) newElement.eContainer();
					try {
						IAbstractArtifact artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName());
						pattern.addToManager(tsProject, artifact);
						pattern.annotateArtifact(tsProject, artifact);
					} catch (TigerstripeException ex) {
						ex.printStackTrace();
					}
				}
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}
	
	/**
	 * @generated NOT
	 */
	private static class CreateCustomManagedEntityArtifact_1003Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateCustomManagedEntityArtifact_1003Command(CreateElementRequest req) {
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
				if (getNewElement() instanceof ManagedEntityArtifact){
					ManagedEntityArtifact newElement = (ManagedEntityArtifact) getNewElement();
					CustomElementType customType = ((CustomElementType) ((CreateElementRequest) getRequest()).getElementType());
					IArtifactPattern pattern = (IArtifactPattern) customType.getPattern();

					Map map = (Map) newElement.eContainer();
					try {
						IAbstractArtifact artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName());
						pattern.addToManager(tsProject, artifact);
						pattern.annotateArtifact(tsProject, artifact);
					} catch (TigerstripeException ex) {
						ex.printStackTrace();
					}
				}
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated  NOT
	 */
	private static class CreateCustomNotificationArtifact_1004Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateCustomNotificationArtifact_1004Command(CreateElementRequest req) {
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
				if (getNewElement() instanceof NotificationArtifact){
					NotificationArtifact newElement = (NotificationArtifact) getNewElement();
					CustomElementType customType = ((CustomElementType) ((CreateElementRequest) getRequest()).getElementType());
					IArtifactPattern pattern = (IArtifactPattern) customType.getPattern();

					Map map = (Map) newElement.eContainer();
					try {
						IAbstractArtifact artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName());
						pattern.addToManager(tsProject, artifact);
						pattern.annotateArtifact(tsProject, artifact);
					} catch (TigerstripeException ex) {
						ex.printStackTrace();
					}
				}
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated NOT
	 */
	private static class CreateCustomDatatypeArtifact_1005Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateCustomDatatypeArtifact_1005Command(CreateElementRequest req) {
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
				if (getNewElement() instanceof DatatypeArtifact){
					DatatypeArtifact newElement = (DatatypeArtifact) getNewElement();
					CustomElementType customType = ((CustomElementType) ((CreateElementRequest) getRequest()).getElementType());
					IArtifactPattern pattern = (IArtifactPattern) customType.getPattern();

					Map map = (Map) newElement.eContainer();
					try {
						IAbstractArtifact artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName());
						pattern.addToManager(tsProject, artifact);
						pattern.annotateArtifact(tsProject, artifact);
					} catch (TigerstripeException ex) {
						ex.printStackTrace();
					}
				}
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}
	
	/**
	 * @generated NOT
	 */
	private static class CreateCustomEnumeration_1006Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateCustomEnumeration_1006Command(CreateElementRequest req) {
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
				if (getNewElement() instanceof Enumeration){
					Enumeration newElement = (Enumeration) getNewElement();
					CustomElementType customType = ((CustomElementType) ((CreateElementRequest) getRequest()).getElementType());
					IArtifactPattern pattern = (IArtifactPattern) customType.getPattern();

					Map map = (Map) newElement.eContainer();
					try {
						IAbstractArtifact artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName());
						pattern.addToManager(tsProject, artifact);
						pattern.annotateArtifact(tsProject, artifact);
					} catch (TigerstripeException ex) {
						ex.printStackTrace();
					}
				}
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}
	
	/**
	 * @generated NOT
	 */
	private static class CreateCustomUpdateProcedureArtifact_1007Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateCustomUpdateProcedureArtifact_1007Command(
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
				if (getNewElement() instanceof UpdateProcedureArtifact){
					UpdateProcedureArtifact newElement = (UpdateProcedureArtifact) getNewElement();
					CustomElementType customType = ((CustomElementType) ((CreateElementRequest) getRequest()).getElementType());
					IArtifactPattern pattern = (IArtifactPattern) customType.getPattern();

					Map map = (Map) newElement.eContainer();
					try {
						IAbstractArtifact artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName());
						pattern.addToManager(tsProject, artifact);
						pattern.annotateArtifact(tsProject, artifact);
					} catch (TigerstripeException ex) {
						ex.printStackTrace();
					}
				}
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	
	/**
	 * @generated NOT
	 */
	private static class CreateCustomSessionFacadeArtifact_1008Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateCustomSessionFacadeArtifact_1008Command(CreateElementRequest req) {
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
				if (getNewElement() instanceof SessionFacadeArtifact){
					SessionFacadeArtifact newElement = (SessionFacadeArtifact) getNewElement();
					CustomElementType customType = ((CustomElementType) ((CreateElementRequest) getRequest()).getElementType());
					IArtifactPattern pattern = (IArtifactPattern) customType.getPattern();

					Map map = (Map) newElement.eContainer();
					try {
						IAbstractArtifact artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName());
						pattern.addToManager(tsProject, artifact);
						pattern.annotateArtifact(tsProject, artifact);
					} catch (TigerstripeException ex) {
						ex.printStackTrace();
					}
				}
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
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated NOT
	 */
	private static class CreateCustomAssociationClassClass_1009Command extends
			CreateElementCommand {

		/**
		 * @generated
		 */
		public CreateCustomAssociationClassClass_1009Command(CreateElementRequest req) {
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
