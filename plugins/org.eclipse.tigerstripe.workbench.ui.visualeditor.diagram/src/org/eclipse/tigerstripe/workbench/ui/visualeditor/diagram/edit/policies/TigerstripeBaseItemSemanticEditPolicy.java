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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
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
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.project.INameProvider;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
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
	 */
	protected static void setDefaults(EObject eObject,
			CreateElementRequest createRequest, ITigerstripeProject tsProject)
			throws TigerstripeException {

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

		INameProvider provider = tsProject.getNameProvider();

		String defaultUniqueName = null;
		if (eObject instanceof ManagedEntityArtifact) {
			defaultUniqueName = provider.getUniqueName(
					IManagedEntityArtifact.class, defaultPackage);
		} else if (eObject instanceof DatatypeArtifact) {
			defaultUniqueName = provider.getUniqueName(IDatatypeArtifact.class,
					defaultPackage);
		} else if (eObject instanceof Enumeration) {
			defaultUniqueName = provider.getUniqueName(IEnumArtifact.class,
					defaultPackage);
		} else if (eObject instanceof AssociationClass) {
			defaultUniqueName = provider.getUniqueName(
					IAssociationClassArtifact.class, defaultPackage);
		} else if (eObject instanceof Association) {
			defaultUniqueName = provider.getUniqueName(
					IAssociationArtifact.class, defaultPackage);
		} else if (eObject instanceof Dependency) {
			defaultUniqueName = provider.getUniqueName(
					IDependencyArtifact.class, defaultPackage);
		} else if (eObject instanceof NamedQueryArtifact) {
			defaultUniqueName = provider.getUniqueName(IQueryArtifact.class,
					defaultPackage);
		} else if (eObject instanceof UpdateProcedureArtifact) {
			defaultUniqueName = provider.getUniqueName(
					IUpdateProcedureArtifact.class, defaultPackage);
		} else if (eObject instanceof SessionFacadeArtifact) {
			defaultUniqueName = provider.getUniqueName(ISessionArtifact.class,
					defaultPackage);
		} else if (eObject instanceof ExceptionArtifact) {
			defaultUniqueName = provider.getUniqueName(
					IExceptionArtifact.class, defaultPackage);
		} else if (eObject instanceof NotificationArtifact) {
			defaultUniqueName = provider.getUniqueName(IEventArtifact.class,
					defaultPackage);
		}

		if (defaultUniqueName != null) {
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

		// Additional Specific stuff for Enumeration
		if (eObject instanceof Enumeration) {
			Enumeration en = (Enumeration) eObject;
			en.setBaseType("int");
		} else if (eObject instanceof Association) {
			Association assoc = (Association) eObject;
			String aName = getUniqueAssociationEndName(assoc.getAEnd(), assoc
					.getZEnd(), AEND);
			String zName = getUniqueAssociationEndName(assoc.getAEnd(), assoc
					.getZEnd(), ZEND);
			assoc.setAEndName(aName);
			assoc.setZEndName(zName);
		}
	}

	private final static int AEND = 0;

	private final static int ZEND = 1;

	private static String getUniqueAssociationEndName(
			AbstractArtifact aEndType, AbstractArtifact zEndType, int whichEnd) {

		ITigerstripeProject tsProject = null;
		if (aEndType.eContainer() instanceof org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) {
			org.eclipse.tigerstripe.workbench.ui.visualeditor.Map map = (org.eclipse.tigerstripe.workbench.ui.visualeditor.Map) aEndType
					.eContainer();
			tsProject = map.getCorrespondingITigerstripeProject();
		}

		// compute aEnd name
		String aEndName = unCapitalize(aEndType.getName());
		if (tsProject != null) {
			try {
				IArtifactManagerSession session = tsProject
						.getArtifactManagerSession();
				Set<IRelationship> existingA = new HashSet<IRelationship>();
				existingA.addAll(session.getOriginatingRelationshipForFQN(
						aEndType.getFullyQualifiedName(), true));
				Set<IRelationship> existingZ = new HashSet<IRelationship>();
				existingZ.addAll(session.getTerminatingRelationshipForFQN(
						aEndType.getFullyQualifiedName(), true));
				boolean found = false;
				int index = 0;
				String tmpName = aEndName;
				do {
					found = false;
					for (IRelationship rel : existingA) {
						if (tmpName.equals(rel.getRelationshipAEnd().getName())) {
							found = true;
							tmpName = aEndName + "_" + index++;
						}
					}

					if (!found) {
						for (IRelationship rel : existingZ) {
							if (tmpName.equals(rel.getRelationshipZEnd()
									.getName())) {
								found = true;
								tmpName = aEndName + "_" + index++;
							}
						}
					}

				} while (found);
				aEndName = tmpName;
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		String zEndName = unCapitalize(zEndType.getName());
		if (zEndType == aEndType) {
			zEndName = zEndName + "_";
		}
		if (tsProject != null) {
			try {
				IArtifactManagerSession session = tsProject
						.getArtifactManagerSession();
				Set<IRelationship> existingA = new HashSet<IRelationship>();
				existingA.addAll(session.getOriginatingRelationshipForFQN(
						zEndType.getFullyQualifiedName(), true));
				Set<IRelationship> existingZ = new HashSet<IRelationship>();
				existingZ.addAll(session.getTerminatingRelationshipForFQN(
						zEndType.getFullyQualifiedName(), true));
				boolean found = false;
				int index = 0;
				String tmpName = zEndName;
				do {
					found = false;
					for (IRelationship rel : existingZ) {
						if (tmpName.equals(rel.getRelationshipZEnd().getName())) {
							found = true;
							tmpName = zEndName + "_" + index++;
						}
					}
					if (!found) {
						for (IRelationship rel : existingA) {
							if (tmpName.equals(rel.getRelationshipAEnd()
									.getName())) {
								found = true;
								tmpName = zEndName + "_" + index++;
							}
						}
					}
				} while (found);
				zEndName = tmpName;
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}

		switch (whichEnd) {
		case AEND:
			return aEndName;
		case ZEND:
			return zEndName;
		default:
			return aEndName;
		}

	}

	private static String unCapitalize(String str) {
		if (str == null || str.length() == 0)
			return str;
		else if (str.length() == 1)
			return str.toLowerCase();

		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	protected static void setAttributeDefaults(Attribute attribute)
			throws TigerstripeException {

		if (attribute != null) {
			AbstractArtifact artifact = (AbstractArtifact) attribute
					.eContainer();

			List<Attribute> attributes = artifact.getAttributes();

			String tentative = "attribute" + attrIndex;

			boolean found = false;
			for (Attribute attr : attributes) {
				if (tentative.equals(attr.getName())) {
					found = true;
				}
			}

			while (found) {
				found = false;
				attrIndex++;
				tentative = "attribute" + attrIndex;
				for (Attribute attr : attributes) {
					if (tentative.equals(attr.getName())) {
						found = true;
					}
				}
			}

			attribute.setName(tentative);
			attribute.setType(getDefaultTypeName());
		}
	}

	/**
	 * Gets the default attribute type from the active profile.
	 */
	private static String getDefaultTypeName() throws TigerstripeException {
		IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession()
				.getActiveProfile();
		return profile.getDefaultPrimitiveTypeString();
	}

	protected static void setLiteralDefaults(Literal literal, String baseType)
			throws TigerstripeException {

		if (literal != null) {
			AbstractArtifact artifact = (AbstractArtifact) literal.eContainer();

			List<Literal> literals = artifact.getLiterals();

			String tentative = "literal" + litIndex;

			boolean found = false;
			for (Literal lit : literals) {
				if (tentative.equals(lit.getName())) {
					found = true;
				}
			}

			while (found) {
				found = false;
				litIndex++;
				tentative = "literal" + litIndex;
				for (Literal lit : literals) {
					if (tentative.equals(lit.getName())) {
						found = true;
					}
				}
			}

			literal.setName(tentative);
			literal.setType(baseType);
			if ("String".equals(baseType)) {
				literal.setValue("\"" + String.valueOf(litIndex) + "\"");
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

			List<Method> methods = artifact.getMethods();

			String tentative = "method" + methIndex;

			boolean found = false;
			for (Method meth : methods) {
				if (tentative.equals(meth.getName())) {
					found = true;
				}
			}

			while (found) {
				found = false;
				methIndex++;
				tentative = "method" + methIndex;
				for (Method meth : methods) {
					if (tentative.equals(meth.getName())) {
						found = true;
					}
				}
			}

			method.setName(tentative);
			method.setType("void");
		}
	}

	/**
	 * Figure out the corresponding TSProject based on the affected file
	 * 
	 * @param affectedFiles
	 */
	protected static ITigerstripeProject getCorrespondingTSProject(
			List affectedFiles) {
		if (affectedFiles == null || affectedFiles.size() == 0)
			return null;

		if (affectedFiles.get(0) instanceof IResource) {
			IResource res = (IResource) affectedFiles.get(0);
			IAbstractTigerstripeProject tsProject = EclipsePlugin
					.getITigerstripeProjectFor(res.getProject());
			if (tsProject instanceof ITigerstripeProject)
				return (ITigerstripeProject) tsProject;
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

}
