package org.eclipse.tigerstripe.workbench.base.test.model;

import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

public class TestArtifacts extends TestCase {

	protected IAbstractTigerstripeProject createModelProject(String projectName)
			throws TigerstripeException {
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		IAbstractTigerstripeProject aProject = TigerstripeCore.createProject(
				projectName, details, null, ITigerstripeModelProject.class,
				null, new NullProgressMonitor());
		return aProject;
	}

	public final void testCreateRemoveEachArtifactType()
			throws TigerstripeException {
		IAbstractTigerstripeProject aProject = createModelProject("testCreateRemoveEachArtifactType");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artifactHelper = new ArtifactTestHelper(project);

		IArtifactManagerSession mgrSession = project
				.getArtifactManagerSession();
		Collection<String> supportedArtifacts = mgrSession
				.getSupportedArtifacts();
		for (String supportedArtifact : supportedArtifacts) {
			String name = "Name";
			String pack = "com.test";
			IAbstractArtifact artifact = artifactHelper.createArtifact(
					supportedArtifact, name, pack);
			String fqn = artifact.getFullyQualifiedName();
			IAbstractArtifact created = mgrSession
					.getArtifactByFullyQualifiedName(fqn);

			assertNotNull("Failed to retrieve artifact for Mgr Session",
					created);
			assertTrue("Name doesn't match ", created.getName().equals(name));
			assertTrue("FQN doesn't match " + fqn, created
					.getFullyQualifiedName().equals(fqn));

			artifactHelper.remove(artifact);
			IAbstractArtifact removed = mgrSession
					.getArtifactByFullyQualifiedName(fqn);
			assertTrue("Artifact still exists after removal", removed == null);
		}

		project.delete(true, new NullProgressMonitor());
	}

	public final void testExtendsRelationship() throws TigerstripeException {
		IAbstractTigerstripeProject aProject = createModelProject("testExtendsRelationship");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artHelper = new ArtifactTestHelper(project);
		IArtifactManagerSession session = project.getArtifactManagerSession();

		IArtifactQuery allArtifactQuery = session
				.makeQuery(IQueryAllArtifacts.class.getName());
		Collection<IAbstractArtifact> allArtifacts = session
				.queryArtifact(allArtifactQuery);
		int startingArtifacts = allArtifacts.size();

		IManagedEntityArtifact top = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(), "Top",
						"com.test");
		IManagedEntityArtifact subTop = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(),
						"SubTop", "com.test");
		IManagedEntityArtifact other1 = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(),
						"Other1", "com.test");
		IManagedEntityArtifact other2 = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(),
						"Other2", "com.test");

		allArtifacts = session.queryArtifact(allArtifactQuery);
		// This is now 6 , since some packages are added as well
		int expected = startingArtifacts + 6;
		assertTrue("Incorrect number of Artifacts (" + allArtifacts.size()
				+ ") instead of "+expected, allArtifacts.size() == expected);

		subTop.setExtendedArtifact(top);
		subTop.doSave(new NullProgressMonitor());
		assertTrue("has Extends not set ", subTop.hasExtends());
		assertTrue("Extended Artifact not correctly set", subTop
				.getExtendedArtifact().getFullyQualifiedName().equals(
						top.getFullyQualifiedName()));

		Collection<IAbstractArtifact> extendingTop = top
				.getExtendingArtifacts();
		assertTrue("Extending Artifact list does not include any artifact",
				extendingTop.size() == 1);
		assertTrue("Extending Artifact list does not include correct artifact",
				extendingTop.iterator().next().getFullyQualifiedName().equals(
						subTop.getFullyQualifiedName()));

		other1.setExtendedArtifact(top);
		other1.doSave(new NullProgressMonitor());

		other2.setExtendedArtifact(subTop);
		other2.doSave(new NullProgressMonitor());

		Collection<IAbstractArtifact> extendingSubTop = subTop
				.getExtendingArtifacts();
		assertTrue("Extending Artifact list does not include any artifact",
				extendingSubTop.size() == 1);
		assertTrue("Extending Artifact list does not include correct artifact",
				extendingSubTop.iterator().next().getFullyQualifiedName()
						.equals(other2.getFullyQualifiedName()));

		extendingTop = top.getExtendingArtifacts();
		assertTrue(
				"Extending Artifact list does not include correct number of artifacts",
				extendingTop.size() == 2);

		// Check the full list of ancestors
		Collection<IAbstractArtifact> ancestors = other2.getAncestors();
		assertTrue("Ancestors does not include correct number of artifacts",
				ancestors.size() == 2);
		assertTrue("Ancestors is missing an artifact", ancestors
				.contains(subTop));
		assertTrue("Ancestors is missing an artifact", ancestors.contains(top));

		other1.setExtendedArtifact((IAbstractArtifact) null);
		other1.doSave(new NullProgressMonitor());
		assertTrue("hasExtends incorrectly set ", !other1.hasExtends());
		assertTrue("Extended Artifact still set after set to null", subTop
				.getExtendedArtifact() != null);

		extendingTop = top.getExtendingArtifacts();
		assertTrue(
				"Extending Artifact list does not include correct number of artifacts after deletion of realtionship",
				extendingTop.size() == 1);

		artHelper.remove(other2);

		allArtifacts = session.queryArtifact(allArtifactQuery);
		assertTrue("Incorrect number of Artifacts after a remove", allArtifacts
				.size() == startingArtifacts + 5);

		extendingSubTop = subTop.getExtendingArtifacts();
		assertTrue("Extending Artifact list is wrong size ("
				+ extendingSubTop.size()
				+ ") after the artifact has been removed.", extendingSubTop
				.size() == 0);

		project.delete(true, new NullProgressMonitor());
	}

	public final void testArtifactFields() throws TigerstripeException {
		IAbstractTigerstripeProject aProject = createModelProject("testArtifactFields");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artHelper = new ArtifactTestHelper(project);

		// Try adding stuff to the artifact
		IManagedEntityArtifact top = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(), "Top",
						"com.test");

		IField field1 = top.makeField();
		String fieldName = "field1";
		field1.setName(fieldName);
		top.addField(field1);
		top.doSave(new NullProgressMonitor());

		Collection<IField> fields = top.getFields();
		assertTrue("Field collection size is incorrect", fields.size() == 1);
		assertTrue("Field collection  does not include correct field", fields
				.iterator().next().getName().equals(fieldName));

		// Try adding stuff to the artifact
		IManagedEntityArtifact subTop = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(),
						"subTop", "com.test");
		subTop.setExtendedArtifact(top);
		subTop.doSave(new NullProgressMonitor());

		Collection<IField> inheritedFields = subTop.getInheritedFields();
		assertTrue("Inherited Field collection size is incorrect",
				inheritedFields.size() == 1);
		assertTrue("Inherited Field collection does not include correct field",
				inheritedFields.iterator().next().getName().equals(fieldName));

		top.removeFields(fields);

		top.doSave(new NullProgressMonitor());

		Collection<IField> newFields = top.getFields();
		assertTrue(
				"Field collection size is incorrect after removal of fields",
				newFields.size() == 0);

		Collection<IField> newInheritedFields = subTop.getInheritedFields();
		assertTrue("Inherited Field collection size is incorrect ("
				+ newInheritedFields.size() + ") after removal of fields",
				newInheritedFields.size() == 0);

		project.delete(true, new NullProgressMonitor());
	}

	public final void testArtifactLiterals() throws TigerstripeException {
		IAbstractTigerstripeProject aProject = createModelProject("testArtifactLiterals");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artHelper = new ArtifactTestHelper(project);

		// Try adding stuff to the artifact
		IManagedEntityArtifact top = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(), "Top",
						"com.test");

		ILiteral literal1 = top.makeLiteral();
		String literalName = "literal1";
		literal1.setName(literalName);
		top.addLiteral(literal1);
		top.doSave(new NullProgressMonitor());

		Collection<ILiteral> literals = top.getLiterals();
		assertTrue("Literal collection size is incorrect", literals.size() == 1);
		assertTrue("Literal collection  does not include correct field",
				literals.iterator().next().getName().equals(literalName));

		// Try adding stuff to the artifact
		IManagedEntityArtifact subTop = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(),
						"subTop", "com.test");
		subTop.setExtendedArtifact(top);
		subTop.doSave(new NullProgressMonitor());

		Collection<ILiteral> inheritedLiterals = subTop.getInheritedLiterals();
		assertTrue("Inherited Literal collection size is incorrect",
				inheritedLiterals.size() == 1);
		assertTrue(
				"Inherited Literal collection does not include correct field",
				inheritedLiterals.iterator().next().getName().equals(
						literalName));

		top.removeLiterals(literals);

		top.doSave(new NullProgressMonitor());

		Collection<ILiteral> newLiterals = top.getLiterals();
		assertTrue(
				"Literal collection size is incorrect after removal of fields",
				newLiterals.size() == 0);

		Collection<ILiteral> newInheritedFields = subTop.getInheritedLiterals();
		assertTrue("Inherited Literal collection size is incorrect ("
				+ newInheritedFields.size() + ") after removal of fields",
				newInheritedFields.size() == 0);

		project.delete(true, new NullProgressMonitor());
	}

	public final void testArtifactMethods() throws TigerstripeException {
		IAbstractTigerstripeProject aProject = createModelProject("testArtifactMethods");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artHelper = new ArtifactTestHelper(project);

		// Try adding stuff to the artifact
		IManagedEntityArtifact top = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(), "Top",
						"com.test");

		IMethod method1 = top.makeMethod();
		String methodName = "method1";
		method1.setName(methodName);
		IType type = method1.makeType();
		type.setFullyQualifiedName("void");
		method1.setReturnType(type);
		top.addMethod(method1);
		top.doSave(new NullProgressMonitor());

		Collection<IMethod> methods = top.getMethods();
		assertTrue("Method collection size is incorrect", methods.size() == 1);
		assertTrue("Method collection  does not include correct field", methods
				.iterator().next().getName().equals(methodName));

		// Try adding stuff to the artifact
		IManagedEntityArtifact subTop = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(),
						"subTop", "com.test");
		subTop.setExtendedArtifact(top);
		subTop.doSave(new NullProgressMonitor());

		Collection<IMethod> inheritedMethods = subTop.getInheritedMethods();
		assertTrue("Inherited Method collection size is incorrect",
				inheritedMethods.size() == 1);
		assertTrue(
				"Inherited Method collection does not include correct field",
				inheritedMethods.iterator().next().getName().equals(methodName));

		top.removeMethods(methods);

		top.doSave(new NullProgressMonitor());

		Collection<IMethod> newMethods = top.getMethods();
		assertTrue(
				"Method collection size is incorrect after removal of fields",
				newMethods.size() == 0);

		Collection<IMethod> newInheritedMethods = subTop.getInheritedMethods();
		assertTrue("Inherited Method collection size is incorrect ("
				+ newInheritedMethods.size() + ") after removal of methods",
				newInheritedMethods.size() == 0);

		project.delete(true, new NullProgressMonitor());
	}

	public final void testReferencedArtifacts() throws TigerstripeException {
		IAbstractTigerstripeProject aProject = createModelProject("testReferencedArtifacts");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
		ArtifactTestHelper artHelper = new ArtifactTestHelper(project);

		// Make two artifacts
		IManagedEntityArtifact left = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(), "Left",
						"com.test");
		IManagedEntityArtifact right = (IManagedEntityArtifact) artHelper
				.createArtifact(IManagedEntityArtifact.class.getName(), "Left",
						"com.test");

		IField reference = left.makeField();
		reference.setName("refersToRight");
		IType refType = reference.makeType();
		refType.setFullyQualifiedName(right.getFullyQualifiedName());
		reference.setType(refType);
		left.addField(reference);

		// Add a non-reference as well!
		IField nonReference = left.makeField();
		nonReference.setName("refersToRight");
		IType simpleType = nonReference.makeType();
		refType.setFullyQualifiedName("primitive.int");
		nonReference.setType(simpleType);
		left.addField(nonReference);

		left.doSave(new NullProgressMonitor());

		Collection<IAbstractArtifact> referencedArtifacts = left
				.getReferencedArtifacts();
		assertTrue("Referenced Artifact collection size is incorrect ("
				+ referencedArtifacts.size() + ") after addition of Fields",
				referencedArtifacts.size() == 1);

		Collection<IAbstractArtifact> referencingArtifacts = right
				.getReferencingArtifacts();
		assertTrue("Referencing Artifact collection size is incorrect ("
				+ referencingArtifacts.size() + ") after addition of Fields",
				referencingArtifacts.size() == 1);

		// Now remove the reference again
		left.removeFields(left.getFields());
		left.doSave(new NullProgressMonitor());

		referencedArtifacts = left.getReferencedArtifacts();
		assertTrue("Referenced Artifact collection size is incorrect ("
				+ referencedArtifacts.size() + ") after removal of Fields",
				referencedArtifacts.size() == 0);

		referencingArtifacts = right.getReferencingArtifacts();
		assertTrue("Referencing Artifact collection size is incorrect ("
				+ referencingArtifacts.size() + ") after removal of Fields",
				referencingArtifacts.size() == 0);

	}

}
