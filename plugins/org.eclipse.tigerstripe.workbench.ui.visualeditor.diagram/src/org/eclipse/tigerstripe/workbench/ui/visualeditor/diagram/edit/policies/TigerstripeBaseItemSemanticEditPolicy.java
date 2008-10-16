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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IEditHelperContext;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ComponentNameProvider;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ClassDiagramDragDropEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.helpers.TigerstripeBaseEditHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AbstractArtifactExtendsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.expressions.TigerstripeAbstractExpression;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.expressions.TigerstripeOCLFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorPlugin;

/**
 * @generated
 */
public class TigerstripeBaseItemSemanticEditPolicy extends SemanticEditPolicy {

	private static int attrIndex = 0;

	private static int methIndex = 0;

	private static int refIndex = 0;

	private static int litIndex = 0;

	
	/**
	 * Set default values on an EObject. This is called as part of the creation
	 * of any new Artifact, association, dependency in the MAP to ensure; - a
	 * non-null name is provided by default - the correct default package is
	 * provided by default - any additional required values are provided
	 * 
	 * @param eObject
	 * 
	 * 
	 */
	protected static void setDefaults(EObject eObject,
			IPattern pattern, 
			CreateElementRequest createRequest,
			ITigerstripeModelProject tsProject) throws TigerstripeException {

		
		// We only need defaults values if not a drag-n-drop of an artifact
		if (Boolean.TRUE.equals(createRequest
				.getParameter(ClassDiagramDragDropEditPolicy.DRAGGED_ARTIFACT)))
			return;

		// DiagramEditPart diagramEditPart = (DiagramEditPart) getHost();
		// IEditorPart editorPart = ((DiagramEditDomain) diagramEditPart
		// .getParent().getViewer().getEditDomain()).getEditorPart();
		//
		// DiagramEditorHelper helper = new DiagramEditorHelper(editorPart);
		// ITigerstripeProject tsProject = helper
		// .getCorrespondingTigerstripeProject();

		String defaultPackage = null;
		if (eObject.eContainer() instanceof org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) {
			org.eclipse.tigerstripe.workbench.ui.visualeditor.Map map = (org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) eObject
					.eContainer();
			if (map != null) {
				defaultPackage = map.getBasePackage();
			}
		}
		if (defaultPackage == null) {
			defaultPackage = tsProject.getProjectDetails().getProperty(
					"defaultArtifactPackage", "");
		}

		//INameProvider provider = ((TigerstripeProjectHandle) tsProject)
		//		.getNameProvider();

		
		ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();
		
		String defaultUniqueName = null;
		if (eObject instanceof ManagedEntityArtifact) {
			defaultUniqueName = nameFactory.getNewArtifactName( pattern,
					IManagedEntityArtifact.class, tsProject,defaultPackage);
		} else if (eObject instanceof DatatypeArtifact) {
			defaultUniqueName = nameFactory.getNewArtifactName(pattern,
					IDatatypeArtifact.class,tsProject,defaultPackage);
		} else if (eObject instanceof Enumeration) {
			defaultUniqueName = nameFactory.getNewArtifactName(pattern,
					IEnumArtifact.class,tsProject,defaultPackage);
		
		} else if (eObject instanceof NamedQueryArtifact) {
			defaultUniqueName = nameFactory.getNewArtifactName(pattern,
					IQueryArtifact.class,tsProject,defaultPackage);
		} else if (eObject instanceof UpdateProcedureArtifact) {
			defaultUniqueName = nameFactory.getNewArtifactName(pattern,
					IUpdateProcedureArtifact.class, tsProject,defaultPackage);
		} else if (eObject instanceof SessionFacadeArtifact) {
			defaultUniqueName = nameFactory.getNewArtifactName(pattern,
					ISessionArtifact.class,	tsProject,defaultPackage);
		} else if (eObject instanceof ExceptionArtifact) {
			defaultUniqueName = nameFactory.getNewArtifactName(pattern,
					IExceptionArtifact.class, tsProject,defaultPackage);
		} else if (eObject instanceof NotificationArtifact) {
			defaultUniqueName = nameFactory.getNewArtifactName(pattern,
					IEventArtifact.class, tsProject,defaultPackage);
			
			// Note these last three will potentially be overwritten below
		} else if (eObject instanceof Dependency) {
			defaultUniqueName = nameFactory.getNewArtifactName(pattern,
					IDependencyArtifact.class, tsProject,defaultPackage);
		} else if (eObject instanceof AssociationClass) {
			defaultUniqueName = nameFactory.getNewArtifactName(pattern,
					IAssociationClassArtifact.class, tsProject,defaultPackage);
		} else if (eObject instanceof Association) {
			defaultUniqueName = nameFactory.getNewArtifactName(pattern,
					IAssociationArtifact.class, tsProject,defaultPackage);
		}


		// Additional Specific stuff for Enumeration
		if (eObject instanceof Enumeration) {
			if (defaultUniqueName != null) {
				Enumeration en = (Enumeration) eObject;
				en.setName(defaultUniqueName);
				en.setPackage(defaultPackage);
				en.setBaseType("int");
			} else
				throw new TigerstripeException("Couldn't set default values on "
						+ eObject);
		} else if (eObject instanceof Association) {
			Association assoc = (Association) eObject;
			

			Class<?> artifactType;

			if (eObject instanceof AssociationClass){
				artifactType = IAssociationClassArtifact.class;
			} else {
				artifactType = IAssociationArtifact.class;
			}
			String newName = nameFactory.getNewRelationshipName(pattern,
					artifactType, tsProject, defaultPackage, 
					assoc.getAEnd().getFullyQualifiedName(), 
					assoc.getZEnd().getFullyQualifiedName());
			assoc.setName(newName);
			assoc.setPackage(defaultPackage);

			//IAbstractArtifact iArtifact = assoc.getCorrespondingIArtifact();
			//String aName = nameFactory.getNewAssociationEndName(pattern,iArtifact, AEND);
			//String zName = nameFactory.getNewAssociationEndName(pattern,iArtifact, ZEND);
			//assoc.setAEndName(aName);
			//assoc.setZEndName(zName);
			
			
		} else if (eObject instanceof Dependency){
			Dependency dep = (Dependency) eObject;

			String newName = nameFactory.getNewRelationshipName(pattern,
					IDependencyArtifact.class, tsProject, defaultPackage, 
					dep.getAEnd().getFullyQualifiedName(), 
					dep.getZEnd().getFullyQualifiedName());
			dep.setName(newName);
			dep.setPackage(defaultPackage);
			
		} else if (defaultUniqueName != null) {
			if (eObject instanceof QualifiedNamedElement) {
				QualifiedNamedElement art = (QualifiedNamedElement) eObject;
				art.setName(defaultUniqueName);
				art.setPackage(defaultPackage);

				// NOTE: these 2 set method calls trigger notifications to
				// EAdapters
				// that will condition the propagation to the Tigerstripe domain
				// THE ORDER MATTERS! the package needs to be set second.
			}
		} else
			throw new TigerstripeException("Couldn't set default values on "
					+ eObject);
	}

	private final static int AEND = 0;

	private final static int ZEND = 1;

	

	protected static void setAttributeDefaults(Attribute attribute)
			throws TigerstripeException {

		if (attribute != null) {
			AbstractArtifact artifact = (AbstractArtifact) attribute
					.eContainer();

			IAbstractArtifact iArtifact = artifact.getCorrespondingIArtifact();
			
			ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();
			String tentative = nameFactory.getNewFieldName(iArtifact);

			attribute.setName(tentative);
			attribute.setType(getDefaultTypeName());
		}
	}

	/**
	 * Gets the default attribute type from the active profile.
	 */
	private static String getDefaultTypeName() throws TigerstripeException {
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		return profile.getDefaultPrimitiveTypeString();
	}

	protected static void setLiteralDefaults(Literal literal, String baseType)
			throws TigerstripeException {

		if (literal != null) {
			AbstractArtifact artifact = (AbstractArtifact) literal.eContainer();
			List<Literal> literals = artifact.getLiterals();
			boolean found = false;
			
			IAbstractArtifact iArtifact = artifact.getCorrespondingIArtifact();
			
			ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();
			String tentative = nameFactory.getNewLiteralName(iArtifact);

			literal.setName(tentative);
			literal.setType(baseType);

			if ("String".equals(baseType)) {
				int count = literals.size();
				// make sure this is not a duplicate..
				
				
				literal.setValue(getLiteralValue(iArtifact));
			} else {
				int valIndex = 0;
				found = false;
				for (Literal lit : literals) {
					if (lit != literal) {
						String val = lit.getValue();
						int iVal = Integer.parseInt(val);
						if (valIndex == iVal) {
							found = true;
						}
					}
				}

				while (found) {
					found = false;
					valIndex++;
					for (Literal lit : literals) {
						if (lit != literal) {
							String val = lit.getValue();
							int iVal = Integer.parseInt(val);
							if (valIndex == iVal) {
								found = true;
							}
						}
					}
				}


				literal.setValue(String.valueOf(valIndex));
			}
		}

	}

	
	private static String getLiteralValue(IAbstractArtifact artifact){
		Collection<ILiteral> existingLiterals = artifact.getLiterals();
		int count = existingLiterals.size();
		
		String newValue = "\"" + String.valueOf(count) +"\"";
		// make sure we're not creating a duplicate
		boolean ok ;
		do {
			ok = true;
			for (ILiteral exists : existingLiterals){
				if (exists.getValue().equals(newValue)){
					count++;
					newValue = "\"" + String.valueOf(count) +"\"";
					ok = false;
					break ;
				}
			}
		} 
		while ( ! ok);
		return newValue;
	}
	
	protected static void setReferenceDefaults(Reference reference)
			throws TigerstripeException {

		if (reference != null) {
			AbstractArtifact artifact = (AbstractArtifact) reference
					.eContainer();

			List<Reference> references = artifact.getReferences();

			String tentative = "reference" + refIndex;

			boolean found = false;
			for (Reference ref : references) {
				if (tentative.equals(ref.getName())) {
					found = true;
				}
			}

			while (found) {
				found = false;
				refIndex++;
				tentative = "reference" + refIndex;
				for (Reference ref : references) {
					if (tentative.equals(ref.getName())) {
						found = true;
					}
				}
			}

			reference.setName(tentative);
		}
	}

	protected static void setMethodDefaults(Method method)
			throws TigerstripeException {
		if (method != null) {
			AbstractArtifact artifact = (AbstractArtifact) method.eContainer();

			IAbstractArtifact iArtifact = artifact.getCorrespondingIArtifact();
			
			ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();
			String tentative = nameFactory.getNewMethodName(iArtifact);
			
			method.setName(tentative);
			method.setType("void");
		}
	}

	/**
	 * Figure out the corresponding TSProject based on the affected file
	 * 
	 * @param affectedFiles
	 */
	protected static ITigerstripeModelProject getCorrespondingTSProject(
			List affectedFiles) {
		if (affectedFiles == null || affectedFiles.size() == 0)
			return null;

		if (affectedFiles.get(0) instanceof IResource) {
			IResource res = (IResource) affectedFiles.get(0);
			IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) res
					.getProject().getAdapter(IAbstractTigerstripeProject.class);
			if (tsProject instanceof ITigerstripeModelProject)
				return (ITigerstripeModelProject) tsProject;
		}

		return null;
	}

	/**
	 * @generated
	 */
	@Override
	protected Command getSemanticCommand(IEditCommandRequest request) {
		IEditCommandRequest completedRequest = completeRequest(request);
		Object editHelperContext = completedRequest.getEditHelperContext();
		if (editHelperContext instanceof View
				|| (editHelperContext instanceof IEditHelperContext && ((IEditHelperContext) editHelperContext)
						.getEObject() instanceof View))
			// no semantic commands are provided for pure design elements
			return null;
		if (editHelperContext == null) {
			editHelperContext = ViewUtil
					.resolveSemanticElement((View) getHost().getModel());
		}
		IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(editHelperContext);
		if (elementType == ElementTypeRegistry.getInstance().getType(
				"org.eclipse.gmf.runtime.emf.type.core.default")) { //$NON-NLS-1$ 
			elementType = null;
		}
		Command epCommand = getSemanticCommandSwitch(completedRequest);
		if (epCommand != null) {
			ICommand command = epCommand instanceof ICommandProxy ? ((ICommandProxy) epCommand)
					.getICommand()
					: new CommandProxy(epCommand);
			completedRequest.setParameter(
					TigerstripeBaseEditHelper.EDIT_POLICY_COMMAND, command);
		}
		Command ehCommand = null;
		if (elementType != null) {
			ICommand command = elementType.getEditCommand(completedRequest);
			if (command != null) {
				if (!(command instanceof CompositeTransactionalCommand)) {
					TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
							.getEditingDomain();
					command = new CompositeTransactionalCommand(editingDomain,
							null).compose(command);
				}
				ehCommand = new ICommandProxy(command);
			}
		}
		boolean shouldProceed = true;
		if (completedRequest instanceof DestroyRequest) {
			shouldProceed = shouldProceed((DestroyRequest) completedRequest);
		}
		if (shouldProceed) {
			if (completedRequest instanceof DestroyRequest) {
				TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
						.getEditingDomain();
				Command deleteViewCommand = new ICommandProxy(
						new DeleteCommand(editingDomain, (View) getHost()
								.getModel()));
				ehCommand = ehCommand == null ? deleteViewCommand : ehCommand
						.chain(deleteViewCommand);
			}
			return ehCommand;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getSemanticCommandSwitch(IEditCommandRequest req) {
		if (req instanceof CreateRelationshipRequest)
			return getCreateRelationshipCommand((CreateRelationshipRequest) req);
		else if (req instanceof CreateElementRequest)
			return getCreateCommand((CreateElementRequest) req);
		else if (req instanceof ConfigureRequest)
			return getConfigureCommand((ConfigureRequest) req);
		else if (req instanceof DestroyElementRequest)
			return getDestroyElementCommand((DestroyElementRequest) req);
		else if (req instanceof DestroyReferenceRequest)
			return getDestroyReferenceCommand((DestroyReferenceRequest) req);
		else if (req instanceof DuplicateElementsRequest)
			return getDuplicateCommand((DuplicateElementsRequest) req);
		else if (req instanceof GetEditContextRequest)
			return getEditContextCommand((GetEditContextRequest) req);
		else if (req instanceof MoveRequest)
			return getMoveCommand((MoveRequest) req);
		else if (req instanceof ReorientReferenceRelationshipRequest)
			return getReorientReferenceRelationshipCommand((ReorientReferenceRelationshipRequest) req);
		else if (req instanceof ReorientRelationshipRequest)
			return getReorientRelationshipCommand((ReorientRelationshipRequest) req);
		else if (req instanceof SetRequest)
			return getSetCommand((SetRequest) req);
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getConfigureCommand(ConfigureRequest req) {
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req) {
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getSetCommand(SetRequest req) {
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getEditContextCommand(GetEditContextRequest req) {
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getDestroyReferenceCommand(DestroyReferenceRequest req) {
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getDuplicateCommand(DuplicateElementsRequest req) {
		System.out.println("dup");
		return null;
	}

	/**
	 * @generated NOT
	 */
	protected Command getMoveCommand(MoveRequest req) {
		Map elementsToMoveMap = req.getElementsToMove();
		EObject targetContainer = req.getTargetContainer();
		for (Object key : elementsToMoveMap.keySet()) {
			if (key instanceof Attribute
					&& targetContainer instanceof AbstractArtifact) {
				Attribute attr = (Attribute) key;

				// Forbid DnD of attributes to Enumeration/SessionFacades
				if (targetContainer instanceof Enumeration
						|| targetContainer instanceof SessionFacadeArtifact)
					return UnexecutableCommand.INSTANCE;
				AbstractArtifact artContainer = (AbstractArtifact) targetContainer;
				List<Attribute> existingAttributes = artContainer
						.getAttributes();
				for (Attribute existingAttribute : existingAttributes) {
					if (attr.getName() != null
							&& attr.getName().equals(
									existingAttribute.getName()))
						return UnexecutableCommand.INSTANCE;
				}
			} else if (key instanceof Method
					&& targetContainer instanceof AbstractArtifact) {
				Method meth = (Method) key;

				// Can only DnD to ManagedEntity, Datatype and SessionFacade
				if (!(targetContainer instanceof ManagedEntityArtifact)
						&& !(targetContainer instanceof DatatypeArtifact)
						&& !(targetContainer instanceof SessionFacadeArtifact))
					return UnexecutableCommand.INSTANCE;
				AbstractArtifact artContainer = (AbstractArtifact) targetContainer;
				List<Method> existingMethods = artContainer.getMethods();
				for (Method existingMethod : existingMethods) {
					if (meth.getName() != null
							&& meth.getName().equals(existingMethod.getName()))
						return UnexecutableCommand.INSTANCE;
				}
			} else if (key instanceof Literal
					&& targetContainer instanceof AbstractArtifact) {
				Literal lit = (Literal) key;

				if (!(targetContainer instanceof Enumeration))
					return UnexecutableCommand.INSTANCE;

				Enumeration enu = (Enumeration) targetContainer;
				List<Literal> existingLiterals = enu.getLiterals();
				for (Literal existingLiteral : existingLiterals) {
					if (lit.getName() != null
							&& lit.getName().equals(existingLiteral.getName()))
						return UnexecutableCommand.INSTANCE;
				}
			}
		}
		return null;
	}

	/**
	 * @generated NOT
	 */
	protected Command getReorientReferenceRelationshipCommand(
			ReorientReferenceRelationshipRequest req) {

		Map parameter = req.getParameters();
		// populated in getReorientReferenceRelationshipCommand( ReconnectReq )
		// below
		Object referenceID = parameter.get("referenceID");
		if (referenceID == null)
			// this means we don't which reference is being reconnected
			return UnexecutableCommand.INSTANCE;
		else if (referenceID instanceof AbstractArtifactExtendsEditPart) {
			// Reorienting an "extends" relationship

			// Only the arrow tip can be dragged
			if (req.getDirection() != ReorientRequest.REORIENT_TARGET)
				return UnexecutableCommand.INSTANCE;

			EObject oldRelationshipEnd = req.getOldRelationshipEnd();
			EObject newRelationshipEnd = req.getNewRelationshipEnd();

			// can't extend itself
			if (newRelationshipEnd == req.getReferenceOwner())
				return UnexecutableCommand.INSTANCE;

			if (!(newRelationshipEnd instanceof AbstractArtifact))
				return UnexecutableCommand.INSTANCE;
			if (newRelationshipEnd.getClass() != oldRelationshipEnd.getClass()) {
				boolean oneIsManagedEntity = newRelationshipEnd instanceof ManagedEntityArtifact
						|| oldRelationshipEnd instanceof ManagedEntityArtifact;
				boolean oneIsAssocClassClass = newRelationshipEnd instanceof AssociationClassClass
						|| oldRelationshipEnd instanceof AssociationClassClass;

				if (!(oneIsManagedEntity && oneIsAssocClassClass))
					return UnexecutableCommand.INSTANCE;
			}

			SetRequest setReq = new SetRequest(
					req.getReferenceOwner(),
					VisualeditorPackage.eINSTANCE.getAbstractArtifact_Extends(),
					newRelationshipEnd);
			return getMSLWrapper(new SetValueCommand(setReq));
		} else
			return UnexecutableCommand.INSTANCE;
	}

	/**
	 * @generated NOT
	 */
	protected Command getReorientRelationshipCommand(
			ReorientRelationshipRequest req) {
		EObject relationship = req.getRelationship();
		EObject newEnd = req.getNewRelationshipEnd();
		EObject oldEnd = req.getOldRelationshipEnd();

		int direction = req.getDirection();

		if (!(newEnd instanceof AbstractArtifact))
			return UnexecutableCommand.INSTANCE;

		if (relationship instanceof Association) {
			Association assoc = (Association) relationship;
			EStructuralFeature featureID = null;
			if (direction == ReorientRequest.REORIENT_SOURCE) {
				featureID = VisualeditorPackage.eINSTANCE.getAssociation_AEnd();
			} else {
				featureID = VisualeditorPackage.eINSTANCE.getAssociation_ZEnd();
			}

			SetRequest setRequest = new SetRequest(assoc, featureID, newEnd);
			return getMSLWrapper(new SetValueCommand(setRequest));
		} else if (relationship instanceof Dependency) {
			Dependency dep = (Dependency) relationship;
			EStructuralFeature featureID = null;
			if (direction == ReorientRequest.REORIENT_SOURCE) {
				featureID = VisualeditorPackage.eINSTANCE.getDependency_AEnd();
			} else {
				featureID = VisualeditorPackage.eINSTANCE.getDependency_ZEnd();
			}

			SetRequest setRequest = new SetRequest(dep, featureID, newEnd);
			return getMSLWrapper(new SetValueCommand(setRequest));
		}

		return UnexecutableCommand.INSTANCE;
	}

	/**
	 * @generated
	 */
	protected Command getMSLWrapper(ICommand cmd) {
		return new ICommandProxy(cmd);
	}

	/**
	 * @generated
	 */
	protected EObject getSemanticElement() {
		return ViewUtil.resolveSemanticElement((View) getHost().getModel());
	}

	/**
	 * Finds container element for the new relationship of the specified type.
	 * Default implementation goes up by containment hierarchy starting from the
	 * specified element and returns the first element that is instance of the
	 * specified container class.
	 * 
	 * @generated
	 */
	protected EObject getRelationshipContainer(EObject element,
			EClass containerClass, IElementType relationshipType) {
		for (; element != null; element = element.eContainer()) {
			if (containerClass.isSuperTypeOf(element.eClass()))
				return element;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected static class LinkConstraints {
		/**
		 * @generated
		 */
		public static final LinkConstraints Association_3001 = createAssociation_3001();

		/**
		 * @generated
		 */
		public static final LinkConstraints SessionFacadeArtifactEmittedNotifications_3002 = createSessionFacadeArtifactEmittedNotifications_3002();

		/**
		 * @generated
		 */
		public static final LinkConstraints SessionFacadeArtifactManagedEntities_3003 = createSessionFacadeArtifactManagedEntities_3003();

		/**
		 * @generated
		 */
		public static final LinkConstraints NamedQueryArtifactReturnedType_3004 = createNamedQueryArtifactReturnedType_3004();

		/**
		 * @generated
		 */
		public static final LinkConstraints SessionFacadeArtifactNamedQueries_3005 = createSessionFacadeArtifactNamedQueries_3005();

		/**
		 * @generated
		 */
		public static final LinkConstraints SessionFacadeArtifactExposedProcedures_3006 = createSessionFacadeArtifactExposedProcedures_3006();

		/**
		 * @generated
		 */
		public static final LinkConstraints AbstractArtifactExtends_3007 = createAbstractArtifactExtends_3007();

		/**
		 * @generated
		 */
		public static final LinkConstraints Dependency_3008 = createDependency_3008();

		/**
		 * @generated
		 */
		public static final LinkConstraints Reference_3009 = createReference_3009();

		/**
		 * @generated
		 */
		public static final LinkConstraints AssociationClass_3010 = createAssociationClass_3010();

		/**
		 * @generated
		 */
		public static final LinkConstraints AbstractArtifactImplements_3012 = createAbstractArtifactImplements_3012();

		/**
		 * @generated NOT
		 */
		private static LinkConstraints createAssociation_3001() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getAbstractArtifact()); //$NON-NLS-1$				
			// TigerstripeAbstractExpression sourceExpression =
			// TigerstripeOCLFactory
			// .getExpression(
			// "self <> oppositeEnd", //$NON-NLS-1$
			// VisualeditorPackage.eINSTANCE.getAbstractArtifact(),
			// sourceEnv);
			TigerstripeAbstractExpression sourceExpression = null;
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated
		 */
		private static LinkConstraints createSessionFacadeArtifactEmittedNotifications_3002() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getNotificationArtifact()); //$NON-NLS-1$				
			TigerstripeAbstractExpression sourceExpression = TigerstripeOCLFactory
					.getExpression("self <> oppositeEnd", //$NON-NLS-1$
							VisualeditorPackage.eINSTANCE
									.getSessionFacadeArtifact(), sourceEnv);
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated
		 */
		private static LinkConstraints createSessionFacadeArtifactManagedEntities_3003() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getManagedEntityArtifact()); //$NON-NLS-1$				
			TigerstripeAbstractExpression sourceExpression = TigerstripeOCLFactory
					.getExpression("self <> oppositeEnd", //$NON-NLS-1$
							VisualeditorPackage.eINSTANCE
									.getSessionFacadeArtifact(), sourceEnv);
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated
		 */
		private static LinkConstraints createNamedQueryArtifactReturnedType_3004() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getAbstractArtifact()); //$NON-NLS-1$				
			TigerstripeAbstractExpression sourceExpression = TigerstripeOCLFactory
					.getExpression("self <> oppositeEnd", //$NON-NLS-1$
							VisualeditorPackage.eINSTANCE
									.getNamedQueryArtifact(), sourceEnv);
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated
		 */
		private static LinkConstraints createSessionFacadeArtifactNamedQueries_3005() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getNamedQueryArtifact()); //$NON-NLS-1$				
			TigerstripeAbstractExpression sourceExpression = TigerstripeOCLFactory
					.getExpression("self <> oppositeEnd", //$NON-NLS-1$
							VisualeditorPackage.eINSTANCE
									.getSessionFacadeArtifact(), sourceEnv);
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated
		 */
		private static LinkConstraints createSessionFacadeArtifactExposedProcedures_3006() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getUpdateProcedureArtifact()); //$NON-NLS-1$				
			TigerstripeAbstractExpression sourceExpression = TigerstripeOCLFactory
					.getExpression("self <> oppositeEnd", //$NON-NLS-1$
							VisualeditorPackage.eINSTANCE
									.getSessionFacadeArtifact(), sourceEnv);
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated
		 */
		private static LinkConstraints createAbstractArtifactExtends_3007() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getAbstractArtifact()); //$NON-NLS-1$				
			TigerstripeAbstractExpression sourceExpression = TigerstripeOCLFactory
					.getExpression(
							"self <> oppositeEnd", //$NON-NLS-1$
							VisualeditorPackage.eINSTANCE.getAbstractArtifact(),
							sourceEnv);
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated NOT
		 */
		private static LinkConstraints createDependency_3008() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getAbstractArtifact()); //$NON-NLS-1$				
			// TigerstripeAbstractExpression sourceExpression =
			// TigerstripeOCLFactory
			// .getExpression(
			// "self <> oppositeEnd", //$NON-NLS-1$
			// VisualeditorPackage.eINSTANCE.getAbstractArtifact(),
			// sourceEnv);
			TigerstripeAbstractExpression sourceExpression = null;
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated NOT
		 */
		private static LinkConstraints createReference_3009() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getAbstractArtifact()); //$NON-NLS-1$				
			// TigerstripeAbstractExpression sourceExpression =
			// TigerstripeOCLFactory
			// .getExpression(
			// "self <> oppositeEnd", //$NON-NLS-1$
			// VisualeditorPackage.eINSTANCE.getAbstractArtifact(),
			// sourceEnv);
			TigerstripeAbstractExpression sourceExpression = null;
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated NOT
		 */
		private static LinkConstraints createAssociationClass_3010() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getAbstractArtifact()); //$NON-NLS-1$				
			// TigerstripeAbstractExpression sourceExpression =
			// TigerstripeOCLFactory
			// .getExpression(
			// "self <> oppositeEnd", //$NON-NLS-1$
			// VisualeditorPackage.eINSTANCE.getAbstractArtifact(),
			// sourceEnv);
			TigerstripeAbstractExpression sourceExpression = null;
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated
		 */
		private static LinkConstraints createAbstractArtifactImplements_3012() {
			Map sourceEnv = new HashMap(3);
			sourceEnv
					.put(
							"oppositeEnd", org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage.eINSTANCE.getAbstractArtifact()); //$NON-NLS-1$				
			TigerstripeAbstractExpression sourceExpression = TigerstripeOCLFactory
					.getExpression(
							"self <> oppositeEnd", //$NON-NLS-1$
							VisualeditorPackage.eINSTANCE.getAbstractArtifact(),
							sourceEnv);
			TigerstripeAbstractExpression targetExpression = null;
			return new LinkConstraints(sourceExpression, targetExpression);
		}

		/**
		 * @generated
		 */
		private static final String OPPOSITE_END_VAR = "oppositeEnd"; //$NON-NLS-1$

		/**
		 * @generated
		 */
		private TigerstripeAbstractExpression srcEndInv;

		/**
		 * @generated
		 */
		private TigerstripeAbstractExpression targetEndInv;

		/**
		 * @generated
		 */
		public LinkConstraints(TigerstripeAbstractExpression sourceEnd,
				TigerstripeAbstractExpression targetEnd) {
			this.srcEndInv = sourceEnd;
			this.targetEndInv = targetEnd;
		}

		/**
		 * @generated
		 */
		public boolean canCreateLink(CreateRelationshipRequest req,
				boolean isBackDirected) {
			Object source = req.getSource();
			Object target = req.getTarget();

			TigerstripeAbstractExpression sourceConstraint = isBackDirected ? targetEndInv
					: srcEndInv;
			TigerstripeAbstractExpression targetConstraint = null;
			if (req.getTarget() != null) {
				targetConstraint = isBackDirected ? srcEndInv : targetEndInv;
			}
			boolean isSourceAccepted = sourceConstraint != null ? evaluate(
					sourceConstraint, source, target, false) : true;
			if (isSourceAccepted && targetConstraint != null)
				return evaluate(targetConstraint, target, source, true);
			return isSourceAccepted;
		}

		/**
		 * @generated
		 */
		private static boolean evaluate(
				TigerstripeAbstractExpression constraint, Object sourceEnd,
				Object oppositeEnd, boolean clearEnv) {
			Map evalEnv = Collections.singletonMap(OPPOSITE_END_VAR,
					oppositeEnd);
			try {
				Object val = constraint.evaluate(sourceEnd, evalEnv);
				return (val instanceof Boolean) ? ((Boolean) val)
						.booleanValue() : false;
			} catch (Exception e) {
				TigerstripeDiagramEditorPlugin.getInstance().logError(
						"Link constraint evaluation error", e); //$NON-NLS-1$
				return false;
			}
		}
	}

	public static AssociationClassClass addAssociationClassClass(
			AssociationClass assocClass) {

		if (assocClass != null && assocClass.getName() == null)
			// Upon DnD of AssocClass the assocClassClass is created manually
			// because
			// because the assocClass doesn't have a name/package yet.
			return null;

		AssociationClassClass assocClassClass = VisualeditorFactory.eINSTANCE
				.createAssociationClassClass();
		org.eclipse.tigerstripe.workbench.ui.visualeditor.Map map = (org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) assocClass
				.eContainer();
		map.getArtifacts().add(assocClassClass);
		assocClassClass.setName(assocClass.getName());
		assocClassClass.setPackage(assocClass.getPackage());
		assocClass.setAssociatedClass(assocClassClass);
		return assocClassClass;
	}

	/**
	 * Method getReorientRefRelationshipTargetCommand. Removes the reference the
	 * ConnectionEditPart current has an add the new TargetEditPart
	 * 
	 * This method is overriden from above because the original didn't include
	 * which reference relationship was being reoriented.
	 * 
	 * @param request
	 * @return Command
	 */
	@Override
	protected Command getReorientRefRelationshipTargetCommand(
			ReconnectRequest request) {

		org.eclipse.gef.ConnectionEditPart connectionEP = (request)
				.getConnectionEditPart();

		if (connectionEP instanceof ConnectionEditPart) {
			if (!((ConnectionEditPart) connectionEP).isSemanticConnection())
				return null;
		}

		EditPart sourceEditPart = connectionEP.getSource();
		EditPart targetEditPart = connectionEP.getTarget();
		EObject referenceOwner = ViewUtil
				.resolveSemanticElement((View) sourceEditPart.getModel());
		EObject oldTarget = ViewUtil
				.resolveSemanticElement((View) targetEditPart.getModel());
		EObject newTarget = ViewUtil.resolveSemanticElement((View) request
				.getTarget().getModel());

		connectionEP.getModel();

		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
				.getEditingDomain();

		ReorientReferenceRelationshipRequest semRequest = new ReorientReferenceRelationshipRequest(
				editingDomain, referenceOwner, newTarget, oldTarget,
				ReorientRequest.REORIENT_TARGET);

		Map map = request.getExtendedData();
		map.put("referenceID", connectionEP);
		semRequest.addParameters(request.getExtendedData());

		return getSemanticCommand(semRequest);
	}

	@Override
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_DUPLICATE.equals(request.getType()))
			return UnexecutableCommand.INSTANCE;
		return super.getCommand(request);
	}
	
	public static String getArtifactName(IArtifactPattern pattern, Class clazz, ITigerstripeModelProject project,
			String packageName){
		String name = "";
		// If we move the name provider to the pattern then we only need change this line!
		
		ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();	
		return nameFactory.getNewArtifactName(pattern,
				clazz, project, packageName);
	}
	
	public static String getArtifactName(IArtifactPattern pattern, String  className, ITigerstripeModelProject project,
			String packageName){
		String artifactName = "";
		if (className.equals(IManagedEntityArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IManagedEntityArtifact.class, project,packageName); 
		} else if (className.equals(IDatatypeArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IDatatypeArtifact.class,project,packageName);
		} else if (className.equals(IEnumArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IEnumArtifact.class,project,packageName);

		} else if (className.equals(IQueryArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IQueryArtifact.class,project,packageName);
		} else if (className.equals(IUpdateProcedureArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IUpdateProcedureArtifact.class, project,packageName);
		} else if (className.equals(ISessionArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					ISessionArtifact.class,	project,packageName);
		} else if (className.equals(IExceptionArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IExceptionArtifact.class, project,packageName);
		} else if (className.equals(IEventArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IEventArtifact.class, project,packageName);

		} else if (className.equals(IDependencyArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IDependencyArtifact.class, project,packageName);
		} else if (className.equals(IAssociationClassArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IAssociationClassArtifact.class, project,packageName);
		} else if (className.equals(IAssociationArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IAssociationArtifact.class, project,packageName);
		}
			
		return artifactName;
		
	}
}
