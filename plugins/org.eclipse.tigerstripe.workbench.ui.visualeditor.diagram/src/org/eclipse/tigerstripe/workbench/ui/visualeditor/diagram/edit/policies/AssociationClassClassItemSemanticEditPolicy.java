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
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateRelationshipCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPatternResult;
import org.eclipse.tigerstripe.workbench.patterns.IPatternBasedCreationValidator;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.CustomElementType;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.TigerstripeElementTypes;

/**
 * @generated
 */
public class AssociationClassClassItemSemanticEditPolicy extends
		TigerstripeBaseItemSemanticEditPolicy {

	/**
	 * @generated
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

		});
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {

		if (req.getElementType() instanceof CustomElementType){

			if (((CustomElementType) req.getElementType()).getTargetArtifactType().equals(IAssociationArtifact.class.getName())){
				return req.getTarget() == null ? getCreateStartOutgoingCustomAssociation3001Command(req)
						: getCreateCompleteIncomingCustomAssociation3001Command(req);
			} else if (((CustomElementType) req.getElementType()).getTargetArtifactType().equals(IAssociationClassArtifact.class.getName())){
				return req.getTarget() == null ? getCreateStartOutgoingCustomAssociationClass3010Command(req)
						: getCreateCompleteIncomingCustomAssociationClass3010Command(req);
			} else if (((CustomElementType) req.getElementType()).getTargetArtifactType().equals(IDependencyArtifact.class.getName())){
				return req.getTarget() == null ? getCreateStartOutgoingCustomDependency3008Command(req)
						: getCreateCompleteIncomingCustomDependency3008Command(req);
			} 
		}
		
		// We need these three cases to support drag & drop
		if (TigerstripeElementTypes.Association_3001 == req.getElementType())
			return req.getTarget() == null ? getCreateStartOutgoingAssociation3001Command(req)
					: getCreateCompleteIncomingAssociation3001Command(req);
		if (TigerstripeElementTypes.Dependency_3008 == req.getElementType())
			return req.getTarget() == null ? getCreateStartOutgoingDependency3008Command(req)
					: getCreateCompleteIncomingDependency3008Command(req);
		if (TigerstripeElementTypes.AssociationClass_3010 == req.getElementType())
			return req.getTarget() == null ? getCreateStartOutgoingAssociationClass3010Command(req)
					: getCreateCompleteIncomingAssociationClass3010Command(req);
			

		
		if (TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004 == req
				.getElementType())
			return req.getTarget() == null ? null
					: getCreateCompleteIncomingNamedQueryArtifact_ReturnedType3004Command(req);
		if (TigerstripeElementTypes.AbstractArtifactExtends_3007 == req
				.getElementType())
			return req.getTarget() == null ? getCreateStartOutgoingAbstractArtifact_Extends3007Command(req)
					: getCreateCompleteIncomingAbstractArtifact_Extends3007Command(req);
		if (TigerstripeElementTypes.Reference_3009 == req.getElementType())
			return req.getTarget() == null ? getCreateStartOutgoingReference3009Command(req)
					: getCreateCompleteIncomingReference3009Command(req);
		if (TigerstripeElementTypes.AbstractArtifactImplements_3012 == req
				.getElementType())
			return req.getTarget() == null ? getCreateStartOutgoingAbstractArtifact_Implements3012Command(req)
					: getCreateCompleteIncomingAbstractArtifact_Implements3012Command(req);
		return super.getCreateRelationshipCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getCreateStartOutgoingAssociation3001Command(
			CreateRelationshipRequest req) {
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.Association_3001
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		return new Command() {
		};
	}

	/**
	 * @generated
	 */
	protected Command getCreateCompleteIncomingAssociation3001Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof AbstractArtifact))
			return UnexecutableCommand.INSTANCE;
		final Map element = (Map) getRelationshipContainer(req.getSource(),
				VisualeditorPackage.eINSTANCE.getMap(), req.getElementType());
		if (element == null)
			return UnexecutableCommand.INSTANCE;
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.Association_3001
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		if (req.getContainmentFeature() == null) {
			req.setContainmentFeature(VisualeditorPackage.eINSTANCE
					.getMap_Associations());
		}
		return getMSLWrapper(new CreateIncomingAssociation3001Command(req) {

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
	private static class CreateIncomingAssociation3001Command extends
			CreateRelationshipCommand {

		/**
		 * @generated
		 */
		public CreateIncomingAssociation3001Command(
				CreateRelationshipRequest req) {
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
		protected void setElementToEdit(EObject element) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(), null, getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

		/**
		 * @generated NOT
		 */
		@Override
		protected EObject doDefaultElementCreation() {
			Association newElement = (Association) super
					.doDefaultElementCreation();
			if (newElement != null) {
				newElement.setZEnd((AbstractArtifact) getTarget());
				newElement.setAEnd((AbstractArtifact) getSource());
				newElement.setZEndIsNavigable(true);
			}
			return newElement;
		}
	}

	/**
	 * @generated
	 */
	protected Command getCreateCompleteIncomingNamedQueryArtifact_ReturnedType3004Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof NamedQueryArtifact))
			return UnexecutableCommand.INSTANCE;
		NamedQueryArtifact element = (NamedQueryArtifact) req.getSource();
		if (element.getReturnedType() != null)
			return UnexecutableCommand.INSTANCE;
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.NamedQueryArtifactReturnedType_3004
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		SetRequest setReq = new SetRequest(req.getSource(),
				VisualeditorPackage.eINSTANCE
						.getNamedQueryArtifact_ReturnedType(), req.getTarget());
		return getMSLWrapper(new SetValueCommand(setReq));
	}

	/**
	 * @generated
	 */
	protected Command getCreateStartOutgoingAbstractArtifact_Extends3007Command(
			CreateRelationshipRequest req) {
		AbstractArtifact element = (AbstractArtifact) getSemanticElement();
		if (element.getExtends() != null)
			return UnexecutableCommand.INSTANCE;
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.AbstractArtifactExtends_3007
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;

		return new Command() {
		};
	}

	/**
	 * @generated
	 */
	protected Command getCreateCompleteIncomingAbstractArtifact_Extends3007Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof AbstractArtifact))
			return UnexecutableCommand.INSTANCE;
		AbstractArtifact element = (AbstractArtifact) req.getSource();
		if (element.getExtends() != null)
			return UnexecutableCommand.INSTANCE;
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.AbstractArtifactExtends_3007
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		SetRequest setReq = new SetRequest(req.getSource(),
				VisualeditorPackage.eINSTANCE.getAbstractArtifact_Extends(),
				req.getTarget());
		return getMSLWrapper(new SetValueCommand(setReq));
	}

	/**
	 * @generated
	 */
	protected Command getCreateStartOutgoingDependency3008Command(
			CreateRelationshipRequest req) {
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.Dependency_3008
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		return new Command() {
		};
	}

	/**
	 * @generated
	 */
	protected Command getCreateCompleteIncomingDependency3008Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof AbstractArtifact))
			return UnexecutableCommand.INSTANCE;
		final Map element = (Map) getRelationshipContainer(req.getSource(),
				VisualeditorPackage.eINSTANCE.getMap(), req.getElementType());
		if (element == null)
			return UnexecutableCommand.INSTANCE;
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.Dependency_3008
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		if (req.getContainmentFeature() == null) {
			req.setContainmentFeature(VisualeditorPackage.eINSTANCE
					.getMap_Dependencies());
		}
		return getMSLWrapper(new CreateIncomingDependency3008Command(req) {

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
	private static class CreateIncomingDependency3008Command extends
			CreateRelationshipCommand {

		/**
		 * @generated
		 */
		public CreateIncomingDependency3008Command(CreateRelationshipRequest req) {
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
		protected void setElementToEdit(EObject element) {
			throw new UnsupportedOperationException();
		}

		/**
		 * @generated
		 */
		@Override
		protected EObject doDefaultElementCreation() {
			Dependency newElement = (Dependency) super
					.doDefaultElementCreation();
			if (newElement != null) {
				newElement.setZEnd((AbstractArtifact) getTarget());
				newElement.setAEnd((AbstractArtifact) getSource());
			}
			return newElement;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());

				setDefaults(getNewElement(),  null, getCreateRequest(), tsProject);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	protected Command getCreateStartOutgoingReference3009Command(
			CreateRelationshipRequest req) {
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.Reference_3009
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		return new Command() {
		};
	}

	/**
	 * @generated
	 */
	protected Command getCreateCompleteIncomingReference3009Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof AbstractArtifact))
			return UnexecutableCommand.INSTANCE;
		final AbstractArtifact element = (AbstractArtifact) req.getSource();
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.Reference_3009
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		if (req.getContainmentFeature() == null) {
			req.setContainmentFeature(VisualeditorPackage.eINSTANCE
					.getAbstractArtifact_References());
		}
		return getMSLWrapper(new CreateIncomingReference3009Command(req) {

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
	private static class CreateIncomingReference3009Command extends
			CreateRelationshipCommand {

		/**
		 * @generated
		 */
		public CreateIncomingReference3009Command(CreateRelationshipRequest req) {
			super(req);
		}

		/**
		 * @generated
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getAbstractArtifact();
		};

		/**
		 * @generated
		 */
		@Override
		protected void setElementToEdit(EObject element) {
			throw new UnsupportedOperationException();
		}

		/**
		 * 
		 */
		@Override
		protected EObject doDefaultElementCreation() {
			Reference newElement = (Reference) super.doDefaultElementCreation();
			try {
				setReferenceDefaults(newElement);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			if (newElement != null) {
				newElement.setZEnd((AbstractArtifact) getTarget());
			}
			return newElement;
		}
	}

	/**
	 * @generated
	 */
	protected Command getCreateStartOutgoingAssociationClass3010Command(
			CreateRelationshipRequest req) {
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.AssociationClass_3010
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		return new Command() {
		};
	}

	/**
	 * @generated
	 */
	protected Command getCreateCompleteIncomingAssociationClass3010Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof AbstractArtifact))
			return UnexecutableCommand.INSTANCE;
		final Map element = (Map) getRelationshipContainer(req.getSource(),
				VisualeditorPackage.eINSTANCE.getMap(), req.getElementType());
		if (element == null)
			return UnexecutableCommand.INSTANCE;
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.AssociationClass_3010
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		if (req.getContainmentFeature() == null) {
			req.setContainmentFeature(VisualeditorPackage.eINSTANCE
					.getMap_Associations());
		}
		return getMSLWrapper(new CreateIncomingAssociationClass3010Command(req) {

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
	private static class CreateIncomingAssociationClass3010Command extends
			CreateRelationshipCommand {

		/**
		 * @generated
		 */
		public CreateIncomingAssociationClass3010Command(
				CreateRelationshipRequest req) {
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
		protected void setElementToEdit(EObject element) {
			throw new UnsupportedOperationException();
		}

		/**
		 * @generated NOT
		 */
		@Override
		protected EObject doDefaultElementCreation() {
			AssociationClass newElement = (AssociationClass) super
					.doDefaultElementCreation();
			if (newElement != null) {
				newElement.setZEnd((AbstractArtifact) getTarget());
				newElement.setAEnd((AbstractArtifact) getSource());
				newElement.setZEndIsNavigable(true);
			}
			return newElement;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());
				AssociationClass assocClass = (AssociationClass) getNewElement();
				setDefaults(assocClass, null,  getCreateRequest(), tsProject);
				TigerstripeBaseItemSemanticEditPolicy
						.addAssociationClassClass(assocClass);
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}

	/**
	 * @generated
	 */
	protected Command getCreateCompleteIncomingAssociationClass_AssociatedClass3011Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof AssociationClass))
			return UnexecutableCommand.INSTANCE;
		AssociationClass element = (AssociationClass) req.getSource();
		if (element.getAssociatedClass() != null)
			return UnexecutableCommand.INSTANCE;
		SetRequest setReq = new SetRequest(req.getSource(),
				VisualeditorPackage.eINSTANCE
						.getAssociationClass_AssociatedClass(), req.getTarget());
		return getMSLWrapper(new SetValueCommand(setReq));
	}

	/**
	 * @generated
	 */
	protected Command getCreateStartOutgoingAbstractArtifact_Implements3012Command(
			CreateRelationshipRequest req) {
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.AbstractArtifactImplements_3012
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;

		return new Command() {
		};
	}

	/**
	 * @generated
	 */
	protected Command getCreateCompleteIncomingAbstractArtifact_Implements3012Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof AbstractArtifact))
			return UnexecutableCommand.INSTANCE;
		AbstractArtifact element = (AbstractArtifact) req.getSource();
		if (element.getImplements().contains(req.getTarget()))
			return UnexecutableCommand.INSTANCE;
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.AbstractArtifactImplements_3012
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		SetRequest setReq = new SetRequest(req.getSource(),
				VisualeditorPackage.eINSTANCE.getAbstractArtifact_Implements(),
				req.getTarget());
		return getMSLWrapper(new SetValueCommand(setReq));
	}
	
	
	//========================
	protected Command getCreateStartOutgoingCustomAssociation3001Command(CreateRelationshipRequest req){
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.Association_3001
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		return new Command() {
		};
	}
	//========================
	
	//========================
	protected Command getCreateCompleteIncomingCustomAssociation3001Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof AbstractArtifact)){
			return UnexecutableCommand.INSTANCE;}
		final Map element = (Map) getRelationshipContainer(req.getSource(),
				VisualeditorPackage.eINSTANCE.getMap(), req.getElementType());
		if (element == null){
			return UnexecutableCommand.INSTANCE;}
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.Association_3001
				.canCreateLink(req, false)){
			return UnexecutableCommand.INSTANCE;}
		if (req.getContainmentFeature() == null) {
			req.setContainmentFeature(VisualeditorPackage.eINSTANCE
					.getMap_Associations());
		}
		return getMSLWrapper(new CreateIncomingCustomAssociation3001Command(req) {

			/**
			 * @generated
			 */
			@Override
			protected EObject getElementToEdit() {
				return element;
			}
		});
	}
	//========================
	
	//========================
	/**
	 * @generated NOT
	 */
	private static class CreateIncomingCustomAssociation3001Command extends
			CreateRelationshipCommand {

		/**
		 * @generated NOT
		 */
		public CreateIncomingCustomAssociation3001Command(CreateRelationshipRequest req) {
			super(req);
			
		}
	

		boolean ok;
		@Override
		public boolean canExecute() {
			ok = true;
			//System.out.println("canExecute "+super.canExecute()+ " being overridden");
			CustomElementType customType = ((CustomElementType) ((CreateRelationshipRequest) getRequest()).getElementType());
			final IRelationPattern pattern = (IRelationPattern) customType.getPattern();
			ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());
			
			final IPatternBasedCreationValidator validator = pattern.getValidator();
			
			if (validator != null){
				CreateRelationshipRequest request = (CreateRelationshipRequest) getRequest();
				EObject art = request.getContainer();
				Map map = (Map) art.eContainer();
				final String packageName = map.getBasePackage();			
				final ITigerstripeModelProject modelproject = tsProject;
				final String artifactName = getArtifactName((IArtifactPattern) pattern,((IArtifactPattern) pattern).getTargetArtifactType(),modelproject,packageName);
				final String extendedArtifactFQN = ((IArtifactPattern) pattern).getExtendedArtifactName();
				
				final AbstractArtifact aArt = (AbstractArtifact) request.getSource();
				final AbstractArtifact zArt = (AbstractArtifact) request.getTarget();
				
				SafeRunner.run(new ISafeRunnable() {
					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						ok = validator.okToCreate(modelproject,(IRelationPattern) pattern,
								packageName,artifactName,extendedArtifactFQN, aArt.getFullyQualifiedName(), zArt.getFullyQualifiedName());
					}
				});
				return ok;
			}
			return true;
		}

		

		@Override
		protected IElementType getElementType() {
			return TigerstripeElementTypes.Association_3001;
		}


		/**
		 * @generated NOT
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getAbstractArtifact();
		};

		/**
		 * @generated NOT
		 */
		@Override
		protected void setElementToEdit(EObject element) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());
				CustomElementType customType = ((CustomElementType) ((CreateRelationshipRequest) getRequest()).getElementType());
				IRelationPattern pattern = (IRelationPattern) customType.getPattern();

				setDefaults(getNewElement(), pattern, getCreateRequest(), tsProject);
				if (getNewElement() instanceof Association){
					Association newElement = (Association) getNewElement();
					
					Map map = (Map) newElement.eContainer();
					try {
						IArtifactPatternResult artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName(),
								newElement.getAEnd().getFullyQualifiedName(),
								newElement.getZEnd().getFullyQualifiedName());
						pattern.addToManager(tsProject, artifact.getArtifact());
						pattern.annotateArtifact(tsProject, artifact);
					} catch (TigerstripeException ex) {
						ex.printStackTrace();
					}
				}
			} catch (TigerstripeException e) {

			}
			return res;
		}

		/**
		 * @generated NOT
		 */
		@Override
		protected EObject doDefaultElementCreation() {
			Association newElement = (Association) super
					.doDefaultElementCreation();
			if (newElement != null) {
				newElement.setZEnd((AbstractArtifact) getTarget());
				newElement.setAEnd((AbstractArtifact) getSource());
				newElement.setZEndIsNavigable(true);
					
			}
			return newElement;
		}
	}
	
	 //============
	/**
	 * @generated NOT
	 */
	protected Command getCreateStartOutgoingCustomAssociationClass3010Command(
			CreateRelationshipRequest req) {
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.AssociationClass_3010
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		return new Command() {
		};
	}

	/**
	 * @generated NOT
	 */
	protected Command getCreateCompleteIncomingCustomAssociationClass3010Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof AbstractArtifact))
			return UnexecutableCommand.INSTANCE;
		final Map element = (Map) getRelationshipContainer(req.getSource(),
				VisualeditorPackage.eINSTANCE.getMap(), req.getElementType());
		if (element == null)
			return UnexecutableCommand.INSTANCE;
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.AssociationClass_3010
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		if (req.getContainmentFeature() == null) {
			req.setContainmentFeature(VisualeditorPackage.eINSTANCE
					.getMap_Associations());
		}
		return getMSLWrapper(new CreateIncomingCustomAssociationClass3010Command(req) {

			/**
			 * @generated NOT
			 */
			@Override
			protected EObject getElementToEdit() {
				return element;
			}
		});
	}

	/**
	 * @generated NOT
	 */
	private static class CreateIncomingCustomAssociationClass3010Command extends
			CreateRelationshipCommand {

		/**
		 * @generated NOT
		 */
		public CreateIncomingCustomAssociationClass3010Command(
				CreateRelationshipRequest req) {
			super(req);
		}

		boolean ok;
		@Override
		public boolean canExecute() {
			ok = true;
			//System.out.println("canExecute "+super.canExecute()+ " being overridden");
			CustomElementType customType = ((CustomElementType) ((CreateRelationshipRequest) getRequest()).getElementType());
			final IRelationPattern pattern = (IRelationPattern) customType.getPattern();
			ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());
			
			final IPatternBasedCreationValidator validator = pattern.getValidator();
			
			if (validator != null){
				CreateRelationshipRequest request = (CreateRelationshipRequest) getRequest();
				EObject art = request.getContainer();
				Map map = (Map) art.eContainer();
				final String packageName = map.getBasePackage();			
				final ITigerstripeModelProject modelproject = tsProject;
				final String artifactName = getArtifactName((IArtifactPattern) pattern,((IArtifactPattern) pattern).getTargetArtifactType(),modelproject,packageName);
				final String extendedArtifactFQN = ((IArtifactPattern) pattern).getExtendedArtifactName();
				
				final AbstractArtifact aArt = (AbstractArtifact) request.getSource();
				final AbstractArtifact zArt = (AbstractArtifact) request.getTarget();
				
				SafeRunner.run(new ISafeRunnable() {
					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						ok = validator.okToCreate(modelproject,(IRelationPattern) pattern,
								packageName,artifactName,extendedArtifactFQN, aArt.getFullyQualifiedName(), zArt.getFullyQualifiedName());
					}
				});
				return ok;
			}
			return true;
		}

		

		@Override
		protected IElementType getElementType() {
			return TigerstripeElementTypes.AssociationClass_3010;
		}

		
		/**
		 * @generated NOT
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
		};

		/**
		 * @generated NOT
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
			AssociationClass newElement = (AssociationClass) super
					.doDefaultElementCreation();
			if (newElement != null) {
				newElement.setZEnd((AbstractArtifact) getTarget());
				newElement.setAEnd((AbstractArtifact) getSource());
				newElement.setZEndIsNavigable(true);
			}
			return newElement;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());
				CustomElementType customType = ((CustomElementType) ((CreateRelationshipRequest) getRequest()).getElementType());
				IRelationPattern pattern = (IRelationPattern) customType.getPattern();

				AssociationClass newElement = (AssociationClass) getNewElement();
				setDefaults(newElement, pattern, getCreateRequest(), tsProject);
				TigerstripeBaseItemSemanticEditPolicy
						.addAssociationClassClass(newElement);
					
					Map map = (Map) newElement.eContainer();
					try {
						IArtifactPatternResult artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName(),
								newElement.getAEnd().getFullyQualifiedName(),
								newElement.getZEnd().getFullyQualifiedName());
						pattern.addToManager(tsProject, artifact.getArtifact());
						pattern.annotateArtifact(tsProject, artifact);
					} catch (TigerstripeException ex) {
						ex.printStackTrace();
					}
				
			} catch (TigerstripeException e) {

			}
			return res;
		}

	}
	
	
	/**
	 * @generated NOT
	 */
	protected Command getCreateStartOutgoingCustomDependency3008Command(
			CreateRelationshipRequest req) {
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.Dependency_3008
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		return new Command() {
		};
	}

	/**
	 * @generated NOT
	 */
	protected Command getCreateCompleteIncomingCustomDependency3008Command(
			CreateRelationshipRequest req) {
		if (!(req.getSource() instanceof AbstractArtifact))
			return UnexecutableCommand.INSTANCE;
		final Map element = (Map) getRelationshipContainer(req.getSource(),
				VisualeditorPackage.eINSTANCE.getMap(), req.getElementType());
		if (element == null)
			return UnexecutableCommand.INSTANCE;
		if (!TigerstripeBaseItemSemanticEditPolicy.LinkConstraints.Dependency_3008
				.canCreateLink(req, false))
			return UnexecutableCommand.INSTANCE;
		if (req.getContainmentFeature() == null) {
			req.setContainmentFeature(VisualeditorPackage.eINSTANCE
					.getMap_Dependencies());
		}
		return getMSLWrapper(new CreateIncomingCustomDependency3008Command(req) {

			/**
			 * @generated NOT
			 */
			@Override
			protected EObject getElementToEdit() {
				return element;
			}
		});
	}

	/**
	 * @generated NOT
	 */
	private static class CreateIncomingCustomDependency3008Command extends
			CreateRelationshipCommand {

		/**
		 * @generated NOT
		 */
		public CreateIncomingCustomDependency3008Command(CreateRelationshipRequest req) {
			super(req);
		}

		boolean ok;
		@Override
		public boolean canExecute() {
			ok = true;
			//System.out.println("canExecute "+super.canExecute()+ " being overridden");
			CustomElementType customType = ((CustomElementType) ((CreateRelationshipRequest) getRequest()).getElementType());
			final IRelationPattern pattern = (IRelationPattern) customType.getPattern();
			ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());
			
			final IPatternBasedCreationValidator validator = pattern.getValidator();
			
			if (validator != null){
				CreateRelationshipRequest request = (CreateRelationshipRequest) getRequest();
				EObject art = request.getContainer();
				Map map = (Map) art.eContainer();
				final String packageName = map.getBasePackage();			
				final ITigerstripeModelProject modelproject = tsProject;
				final String artifactName = getArtifactName((IArtifactPattern) pattern,((IArtifactPattern) pattern).getTargetArtifactType(),modelproject,packageName);
				final String extendedArtifactFQN = ((IArtifactPattern) pattern).getExtendedArtifactName();
				
				final AbstractArtifact aArt = (AbstractArtifact) request.getSource();
				final AbstractArtifact zArt = (AbstractArtifact) request.getTarget();
				
				SafeRunner.run(new ISafeRunnable() {
					public void handleException(Throwable exception) {
						BasePlugin.log(exception);
					}

					public void run() throws Exception {
						ok = validator.okToCreate(modelproject,(IRelationPattern) pattern,
								packageName,artifactName,extendedArtifactFQN, aArt.getFullyQualifiedName(), zArt.getFullyQualifiedName());
					}
				});
				return ok;
			}
			return true;
		}

		

		@Override
		protected IElementType getElementType() {
			return TigerstripeElementTypes.Dependency_3008;
		}
		
		/**
		 * @generated NOT
		 */
		@Override
		protected EClass getEClassToEdit() {
			return VisualeditorPackage.eINSTANCE.getMap();
		};

		/**
		 * @generated NOT
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
			Dependency newElement = (Dependency) super
					.doDefaultElementCreation();
			if (newElement != null) {
				newElement.setZEnd((AbstractArtifact) getTarget());
				newElement.setAEnd((AbstractArtifact) getSource());
			}
			return newElement;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor arg0,
				IAdaptable arg1) throws ExecutionException {
			CommandResult res = super.doExecuteWithResult(arg0, arg1);
			try {
				ITigerstripeModelProject tsProject = getCorrespondingTSProject(getAffectedFiles());
				CustomElementType customType = ((CustomElementType) ((CreateRelationshipRequest) getRequest()).getElementType());
				IRelationPattern pattern = (IRelationPattern) customType.getPattern();

				setDefaults(getNewElement(), pattern, getCreateRequest(), tsProject);
				if (getNewElement() instanceof Dependency){
					Dependency newElement = (Dependency) getNewElement();
					
					Map map = (Map) newElement.eContainer();
					try {
						IArtifactPatternResult artifact = pattern.createArtifact(
								tsProject, 
								newElement.getPackage(), 
								newElement.getName(), pattern.getExtendedArtifactName(),
								newElement.getAEnd().getFullyQualifiedName(),
								newElement.getZEnd().getFullyQualifiedName());
						pattern.addToManager(tsProject, artifact.getArtifact());
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
}
