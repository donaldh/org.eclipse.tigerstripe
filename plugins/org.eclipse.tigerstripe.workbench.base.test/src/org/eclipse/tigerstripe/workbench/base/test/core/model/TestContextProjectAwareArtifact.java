/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     Anton Salnik (xored software, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.core.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.base.test.AbstractTigerstripeTestCase;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExecutionContext;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.model.IContextProjectAware;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;
import org.eclipse.tigerstripe.workbench.queries.IQueryRelationshipsByArtifact;

public class TestContextProjectAwareArtifact extends
		AbstractTigerstripeTestCase {

	private static final String PROJECT_ID = "TestProject1";

	private static final String REFERENCED_PROJECT_ID = "TestProject2";

	private ITigerstripeModelProject project;
	private ITigerstripeModelProject referencedProject;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		project = (ITigerstripeModelProject) createEmptyModelProject(
				PROJECT_ID, PROJECT_ID);

		referencedProject = (ITigerstripeModelProject) createEmptyModelProject(
				REFERENCED_PROJECT_ID, REFERENCED_PROJECT_ID);
		createEachArtifactType(referencedProject, true);
		ModelProjectHelper.createEntitiesWithAssociations(referencedProject,
				true);

		// add reference
		ITigerstripeModelProject wc = (ITigerstripeModelProject) project
				.makeWorkingCopy(new NullProgressMonitor());
		wc.addModelReference(ModelReference
				.referenceFromProject(referencedProject));
		wc.commit(new NullProgressMonitor());
		assertTrue(project.getReferencedProjects().length == 1);
	}

	@Override
	protected void tearDown() throws Exception {
		deleteModelProject(project);
		deleteModelProject(referencedProject);
		super.tearDown();
	}

	public void testArtifactSession() throws Exception {
		IArtifactManagerSession session = project
				.getArtifactManagerSession();
		testArtifactSession(session);

		IAbstractArtifact artifact = session
				.getArtifactByFullyQualifiedName(getTestEntityFQN());
		session = artifact.getProject().getArtifactManagerSession();
		testArtifactSession(session);
	}

	private void testArtifactSession(IArtifactManagerSession session)
			throws Exception {
		checkContextProjectAware(session
				.getArtifactByFullyQualifiedName(getTestEntityFQN()));
		checkContextProjectAware(session.getArtifactByFullyQualifiedName(
				getTestEntityFQN(), true), project);
		checkContextProjectAwares(
				session.getAllKnownArtifactsByFullyQualifiedName(getTestEntityFQN()),
				false, project);

		checkContextProjectAwares(session.getOriginatingRelationshipForFQN(
				ModelProjectHelper.M1, true), false, project);
		checkContextProjectAwares(session.getTerminatingRelationshipForFQN(
				ModelProjectHelper.M2, true), false, project);

		testArtifactQueries(session);

		// TODO: need to cover the cases?
		// makeArtifact
		// extractArtifact
		// extractArtifactModel
		// removePackageContent
		// getReservedPrimitiveTypes

	}

	private String getTestEntityFQN() {
		return TEST_PACKAGE_NAME + "."
				+ getArtifactName(IManagedEntityArtifact.class.getName());
	}

	public void testArtifactManager() throws Exception {
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) project
				.getArtifactManagerSession();
		ArtifactManager manager = session.getArtifactManager();
		testArtifactManager(manager, false);

		IAbstractArtifact artifact = session
				.getArtifactByFullyQualifiedName(getTestEntityFQN());
		manager = artifact.getProject().getArtifactManagerSession()
				.getArtifactManager();
		testArtifactManager(manager, true);
	}

	private void testArtifactManager(ArtifactManager manager, boolean camBeEmpty)
			throws Exception {
		// getArtifactsByModel
		IAbstractArtifactInternal[] models = new IAbstractArtifactInternal[] {
				ManagedEntityArtifact.MODEL, DatatypeArtifact.MODEL,
				EventArtifact.MODEL, EnumArtifact.MODEL,
				ExceptionArtifact.MODEL, QueryArtifact.MODEL,
				SessionFacadeArtifact.MODEL, UpdateProcedureArtifact.MODEL,
				AssociationArtifact.MODEL, DependencyArtifact.MODEL,
				AssociationClassArtifact.MODEL, PackageArtifact.MODEL };
		for (IAbstractArtifactInternal model : models) {
			checkContextProjectAwares(manager.getArtifactsByModel(model, true,
					new NullProgressMonitor()), false, project);
			checkContextProjectAwares(manager.getArtifactsByModel(model, true,
					false, new ExecutionContext(new NullProgressMonitor())),
					false, project);
			checkContextProjectAwares(manager.getArtifactsByModel(model, true,
					new ExecutionContext(new NullProgressMonitor())), false,
					project);
			checkContextProjectAwares(manager.getArtifactsByModel(model, true,
					false, new NullProgressMonitor()), false, project);
		}

		// getAllArtifacts
		checkContextProjectAwares(manager.getAllArtifacts(true,
				new ExecutionContext(new NullProgressMonitor())), false,
				project);
		checkContextProjectAwares(
				manager.getAllArtifacts(true, new NullProgressMonitor()),
				false, project);
		checkContextProjectAwares(manager.getAllArtifacts(true, false,
				new ExecutionContext(new NullProgressMonitor())), false,
				project);
		checkContextProjectAwares(
				manager.getAllArtifacts(true, false, new NullProgressMonitor()),
				false, project);

		// getModelArtifacts
		checkContextProjectAwares(
				manager.getModelArtifacts(true, new NullProgressMonitor()),
				false, project);
		checkContextProjectAwares(manager.getModelArtifacts(true, false,
				new NullProgressMonitor()), false, project);

		// getCapabilitiesArtifacts
		checkContextProjectAwares(manager.getCapabilitiesArtifacts(true,
				new NullProgressMonitor()), false, project);
		checkContextProjectAwares(manager.getCapabilitiesArtifacts(true, false,
				new NullProgressMonitor()), false, project);
		checkContextProjectAwares(manager.getCapabilitiesArtifacts(true, false,
				new ExecutionContext(new NullProgressMonitor())), false,
				project);

		// getArtifactByFullyQualifiedName
		IAbstractArtifact artifact = manager.getAllArtifacts(true, false,
				new NullProgressMonitor()).get(0);
		checkContextProjectAware(manager.getArtifactByFullyQualifiedName(
				artifact.getFullyQualifiedName(), true, new ExecutionContext(
						new NullProgressMonitor())), project);
		checkContextProjectAware(manager.getArtifactByFullyQualifiedName(
				artifact.getFullyQualifiedName(), true,
				new NullProgressMonitor()), project);
		checkContextProjectAware(manager.getArtifactByFullyQualifiedName(
				artifact.getFullyQualifiedName(), true, false,
				new ExecutionContext(new NullProgressMonitor())), project);
		checkContextProjectAware(manager.getArtifactByFullyQualifiedName(
				artifact.getFullyQualifiedName(), true, false,
				new NullProgressMonitor()), project);

		// getAllKnownArtifactsByFullyQualifiedName
		String fqn = getTestEntityFQN();
		checkContextProjectAwares(
				manager.getAllKnownArtifactsByFullyQualifiedName(fqn,
						new ExecutionContext(new NullProgressMonitor())),
				false, project);
		checkContextProjectAwares(
				manager.getAllKnownArtifactsByFullyQualifiedName(fqn,
						new NullProgressMonitor()), false, project);

		// getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects
		checkContextProjectAwares(
				manager.getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(fqn),
				camBeEmpty, project);
		checkContextProjectAwares(
				manager.getAllKnownArtifactsByFullyQualifiedNameInReferencedProjects(
						fqn, new ExecutionContext(new NullProgressMonitor())),
				camBeEmpty, project);

		// getOriginatingRelationshipForFQN
		checkContextProjectAwares(manager.getOriginatingRelationshipForFQN(
				ModelProjectHelper.M1, true), false, project);
		checkContextProjectAwares(manager.getOriginatingRelationshipForFQN(
				ModelProjectHelper.M1, true, true), false, project);

		// getTerminatingRelationshipForFQN
		checkContextProjectAwares(manager.getTerminatingRelationshipForFQN(
				ModelProjectHelper.M2, true), false, project);
		checkContextProjectAwares(manager.getTerminatingRelationshipForFQN(
				ModelProjectHelper.M2, true, true), false, project);

		// TODO
		// getAllKnownArtifactsByFullyQualifiedNameInModules
		// collectReferencedProjectsAndDependencies -> proxy projects?

		// getReservedPrimitiveTypeArtifacts
		// getRegisteredArtifacts
		// getArtifactByFilename - not related to refs
		// extractArtifact
		// extractArtifactModel
	}

	private void testArtifactQueries(IArtifactManagerSession session)
			throws Exception {
		Collection<String> supportedArtifacts = session.getSupportedArtifacts();
		for (String supportedArtifact : supportedArtifacts) {
			IQueryArtifactsByType query = (IQueryArtifactsByType) session
					.makeQuery(IQueryArtifactsByType.class.getName());
			query.setIncludeDependencies(true);
			query.setArtifactType(supportedArtifact);

			query.setArtifactType(supportedArtifact);

			Collection<IAbstractArtifact> entities = session
					.queryArtifact(query);
			checkContextProjectAwares(entities, true, project);
		}

		IArtifactQuery allArtifactQuery = session
				.makeQuery(IQueryAllArtifacts.class.getName());
		Collection<IAbstractArtifact> allArtifacts = session
				.queryArtifact(allArtifactQuery);
		checkContextProjectAwares(allArtifacts, false, project);

		IQueryRelationshipsByArtifact inQuery = (IQueryRelationshipsByArtifact) session
				.makeQuery(IQueryRelationshipsByArtifact.class.getName());
		inQuery.setIncludeDependencies(true);
		inQuery.setIncludeProjectDependencies(true);
		inQuery.setTerminatingIn(ModelProjectHelper.M2);

		Collection<IAbstractArtifact> relatedAssociations = session
				.queryArtifact(inQuery);
		checkContextProjectAwares(relatedAssociations, false, project);

		inQuery = (IQueryRelationshipsByArtifact) session
				.makeQuery(IQueryRelationshipsByArtifact.class.getName());
		inQuery.setIncludeDependencies(true);
		inQuery.setIncludeProjectDependencies(true);
		inQuery.setOriginatingFrom(ModelProjectHelper.M1);

		relatedAssociations = session.queryArtifact(inQuery);
		checkContextProjectAwares(relatedAssociations, false, project);
	}

	public void testProvideModelComponents() throws Exception {
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) project
				.getArtifactManagerSession();
		ArtifactManager manager = session.getArtifactManager();

		Collection<IAbstractArtifact> allArtifacts = manager.getAllArtifacts(
				true, false, new NullProgressMonitor());
		for (IAbstractArtifact artifact : allArtifacts) {
			testAbstractArtifact(artifact);
		}

		IAbstractArtifact extending = null;
		for (IAbstractArtifact artifact : allArtifacts) {
			if (artifact instanceof IManagedEntityArtifact
					&& artifact.getName().equals(
							getArtifactName(IManagedEntityArtifact.class
									.getName()))) {
				extending = createArtifact(project,
						IManagedEntityArtifact.class.getName(), "MyTestEntity",
						artifact, true);
				extending = session.getArtifactByFullyQualifiedName(extending
						.getFullyQualifiedName());
				break;
			}
		}

		testExtending(extending);
	}

	private void testExtending(IAbstractArtifact artifact) {
		checkContextProjectAware(artifact.getExtendedArtifact(), project);
		checkContextProjectAwares(artifact.getInheritedFields(), false, project);
		checkContextProjectAwares(artifact.getInheritedFields(true), false,
				project);
		checkContextProjectAwares(artifact.getInheritedFields(false), false,
				project);
		checkContextProjectAwares(artifact.getInheritedLiterals(), false,
				project);
		checkContextProjectAwares(artifact.getInheritedLiterals(true), false,
				project);
		checkContextProjectAwares(artifact.getInheritedLiterals(false), false,
				project);
		checkContextProjectAwares(artifact.getInheritedMethods(), false,
				project);
		checkContextProjectAwares(artifact.getInheritedMethods(true), false,
				project);
		checkContextProjectAwares(artifact.getInheritedMethods(false), false,
				project);

		checkContextProjectAwares(artifact.getAncestors(), false, project);
	}

	private void testAbstractArtifact(IAbstractArtifact artifact) {
		testModelComponent(artifact);

		checkContextProjectAwares(artifact.getChildren());
		checkContextProjectAwares(artifact.getReferencedArtifacts());
		checkContextProjectAwares(artifact.getReferencingArtifacts());

		// Literals
		checkContextProjectAwares(artifact.getLiterals());
		checkContextProjectAwares(artifact.getLiterals(true));
		checkContextProjectAwares(artifact.getLiterals(false));
		checkContextProjectAwares(artifact.getInheritedLiterals());
		checkContextProjectAwares(artifact.getInheritedLiterals(true));
		checkContextProjectAwares(artifact.getInheritedLiterals(false));

		// Fields
		checkContextProjectAwares(artifact.getFields());
		checkContextProjectAwares(artifact.getFields(true));
		checkContextProjectAwares(artifact.getFields(false));
		checkContextProjectAwares(artifact.getInheritedFields());
		checkContextProjectAwares(artifact.getInheritedFields(true));
		checkContextProjectAwares(artifact.getInheritedFields(false));

		// Methods
		checkContextProjectAwares(artifact.getMethods());
		checkContextProjectAwares(artifact.getMethods(true));
		checkContextProjectAwares(artifact.getMethods(false));
		checkContextProjectAwares(artifact.getInheritedMethods());
		checkContextProjectAwares(artifact.getInheritedMethods(true));
		checkContextProjectAwares(artifact.getInheritedMethods(false));

		if (getTestEntityFQN().equals(artifact.getFullyQualifiedName())) {
			testField(artifact.getFields().iterator().next());
			testMethod(artifact.getMethods().iterator().next());
			testLiteral(artifact.getLiterals().iterator().next());
		}

		if (artifact instanceof IRelationship) {
			IRelationship relationship = (IRelationship) artifact;
			checkContextProjectAwares(relationship.getRelationshipEnds());
			testRelationshipEnd(relationship.getRelationshipAEnd());
			testRelationshipEnd(relationship.getRelationshipZEnd());

			if (relationship instanceof IAssociationArtifact) {
				IAssociationArtifact association = (IAssociationArtifact) artifact;
				checkContextProjectAwares(association.getAssociationEnds(),
						false, project);
				testAssociationEnd(association.getAEnd());
				testAssociationEnd(association.getZEnd());
			} else if (artifact instanceof IDependencyArtifact) {
				IDependencyArtifact dependency = (IDependencyArtifact) artifact;
				testType(dependency.getAEndType());
				testType(dependency.getZEndType());
			}
		} else if (artifact instanceof IEnumArtifact) {
			testType(((IEnumArtifact) artifact).getBaseType());
		} else if (artifact instanceof IQueryArtifact) {
			testType(((IQueryArtifact) artifact).getReturnedType());
		}

		// TODO:
		// implemented for SF
	}

	private void testField(IField field) {
		checkContextProjectAware(field.getContainingArtifact());
		testModelComponent(field);
		testType(field.getType());
	}

	private void testMethod(IMethod method) {
		checkContextProjectAware(method.getContainingArtifact());
		testModelComponent(method);
		testType(method.getReturnType());

		checkContextProjectAwares(method.getArguments(), false, project);

		IArgument argument = method.getArguments().iterator().next();
		checkContextProjectAware(argument.getContainingMethod());
		checkContextProjectAware(argument.getContainingArtifact());
		testType(argument.getType());
	}

	private void testLiteral(ILiteral literal) {
		checkContextProjectAware(literal.getContainingArtifact());
		testModelComponent(literal);
		checkContextProjectAware(literal.getType());
	}

	private void testAssociationEnd(IAssociationEnd end) {
		testModelComponent(end);
		checkContextProjectAware(end.getContainingArtifact());
		checkContextProjectAware(end.getContainingAssociation());
	}

	private void testRelationshipEnd(IRelationshipEnd end) {
		checkContextProjectAware(end);
		checkContextProjectAware(end.getContainingRelationship());
		testType(end.getType());

		IRelationshipEnd otherEnd = end.getOtherEnd();
		checkContextProjectAware(otherEnd);
		checkContextProjectAware(otherEnd.getContainingRelationship());
		testType(otherEnd.getType());
	}

	private void testType(IType type) {
		checkContextProjectAware(type);
		checkContextProjectAware(type.getArtifact());
		checkContextProjectAware(type.getArtifactManager()
				.getArtifactByFullyQualifiedName(type.getFullyQualifiedName(),
						false, new NullProgressMonitor()));
	}

	private void testModelComponent(IModelComponent component) {
		if (component.getContainingModelComponent() != null) {
			checkContextProjectAware(component.getContainingModelComponent(),
					project);
		}
		checkContextProjectAwares(component.getContainedModelComponents(),
				project);
	}

	public void testEqualsHasCodeMethods() throws Exception {
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) project
				.getArtifactManagerSession();
		ArtifactManager manager = session.getArtifactManager();

		Set<Object> result = new HashSet<Object>();
		Collection<IAbstractArtifact> allArtifacts = manager.getAllArtifacts(
				true, false, new NullProgressMonitor());

		result.addAll(allArtifacts);
		for (IAbstractArtifact artifact : allArtifacts) {
			result.addAll(artifact.getChildren());
		}
		int size = result.size();

		allArtifacts = manager.getAllArtifacts(true, false,
				new NullProgressMonitor());
		result.addAll(allArtifacts);
		for (IAbstractArtifact artifact : allArtifacts) {
			result.addAll(artifact.getChildren());
		}
		assertEquals(size, result.size());
	}

	private void checkContextProjectAwares(Collection<?> modelComponents) {
		checkContextProjectAwares(modelComponents, true, project);
	}

	private void checkContextProjectAwares(Collection<?> modelComponents,
			ITigerstripeModelProject context) {
		checkContextProjectAwares(modelComponents, true, context);
	}

	private void checkContextProjectAwares(Collection<?> modelComponents,
			boolean canBeEmpty, ITigerstripeModelProject context) {
		assertNotNull(modelComponents);
		if (!canBeEmpty) {
			assertTrue(modelComponents.size() > 0);
		}
		if (modelComponents.size() > 0) {
			for (Object modelComponent : modelComponents) {
				checkContextProjectAware(modelComponent, context);

				if (modelComponent instanceof IModelComponent
						&& ((IModelComponent) modelComponent)
								.getContainingModelComponent() != null) {
					checkContextProjectAware(
							((IModelComponent) modelComponent)
									.getContainingModelComponent(),
							context);
				}

				if (modelComponent instanceof IField) {
					IField field = (IField) modelComponent;
					IType type = field.getType();
					if (type.isArtifact()) {
						checkContextProjectAware(type.getArtifact(), context);
					}
				}
			}
		}
	}

	private void checkContextProjectAware(Object modelComponent) {
		checkContextProjectAware(modelComponent, project);
	}

	private void checkContextProjectAware(Object modelComponent,
			ITigerstripeModelProject context) {
		assertNotNull(modelComponent);
		assertTrue(modelComponent instanceof IContextProjectAware);
		IContextProjectAware contextAware = (IContextProjectAware) modelComponent;
		assertNotNull(contextAware.getContextProject());
		assertEquals(context, contextAware.getContextProject());
	}
}
