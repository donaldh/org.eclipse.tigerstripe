package org.eclipse.tigerstripe.workbench.base.test.patterns;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.base.test.model.ArtifactTestHelper;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.patterns.INodePattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class PatternTest extends TestCase {
	
	private ITigerstripeModelProject project;
	private final static String PROJECTNAME = "TestPatterns";

	@Override
	protected void setUp() throws Exception {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName(PROJECTNAME);
		project = (ITigerstripeModelProject) TigerstripeCore.createProject(
				projectDetails, null, ITigerstripeModelProject.class, null,
				null);
	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);
	}

	public void testManagedEntityArtifactPattern() throws Exception {
		
		String targetPackage = "org.eclipse";
		String entityName = "mynewEntity";
		String patternname = "org.eclipse.tigerstripe.workbench.base.ManagedEntity";
		
		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNotNull("No patterns with name '"+patternname +"' returned", pattern);
		INodePattern mePattern = (INodePattern) pattern;
		mePattern.executeRequests(project, targetPackage, entityName, "");
		
		IArtifactManagerSession mgrSession = project
			.getArtifactManagerSession();
		ArtifactTestHelper artifactHelper = new ArtifactTestHelper(project);
		String fqn = targetPackage+"."+entityName;
		IAbstractArtifact created = mgrSession
				.getArtifactByFullyQualifiedName(fqn);

		assertNotNull("Failed to retrieve artifact for Mgr Session",
				created);
		assertTrue("Name doesn't match ", created.getName().equals(entityName));
		assertTrue("FQN doesn't match " + fqn, created
				.getFullyQualifiedName().equals(fqn));
		assertFalse(created.isAbstract());
		
	}
	
	// These two tests assume the presence of a suitable test plugin.
	// I'm not sure how the JUnit tets set up allows us to do that.
//	
//	public void testDisabledPattern() throws Exception {
//
//		String patternname = "org.eclipse.tigerstripe.workbench.base.Datatype";
//
//		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
//		assertNull("Patterns with name '"+patternname +"'was returned", pattern);
//
//	}
//	
//
//	public void testIRDEntityPattern() throws Exception {
//
//		String targetPackage = "org.eclipse";
//		String entityName = "anIRD";
//		String patternname = "com.test.IRDEntity";
//
//		String attributeName = "mandatoryField";
//		String literalName = "mandatoryLiteral";
//		String methodName = "mandatoryMethod";
//		String extendedArtifact = "org.eclipse.anotherIRD";
//		
//		
//		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
//		assertNotNull("No patterns with name '"+patternname +"' returned", pattern);
//		INodePattern mePattern = (INodePattern) pattern;
//		mePattern.executeRequests(project, targetPackage, entityName, extendedArtifact);
//		
//		IArtifactManagerSession mgrSession = project
//			.getArtifactManagerSession();
//		ArtifactTestHelper artifactHelper = new ArtifactTestHelper(project);
//		String fqn = targetPackage+"."+entityName;
//		IAbstractArtifact created = mgrSession
//				.getArtifactByFullyQualifiedName(fqn);
//
//		assertNotNull("Failed to retrieve artifact for Mgr Session",
//				created);
//		assertTrue("Name doesn't match ", created.getName().equals(entityName));
//		assertTrue("FQN doesn't match " + fqn, created
//				.getFullyQualifiedName().equals(fqn));
//		assertTrue(created.isAbstract());
//		
//		assertTrue("Too many Fields",created.getFields().size() == 1);
//		for (IField field :created.getFields()){
//			assertTrue("Wrong Field name", field.getName().equals(attributeName));
//			
//		}
//		assertTrue("Too many Literals",created.getLiterals().size() == 1);
//		for (ILiteral literal :created.getLiterals()){
//			assertTrue("Wrong Literal name", literal.getName().equals(literalName));
//			
//		}
//		assertTrue("Too many methods",created.getMethods().size() == 1);
//		for (IMethod method :created.getMethods()){
//			assertTrue("Wrong Method name", method.getName().equals(methodName));
//			
//		}
//	}
//	
//	public void testIRDAssocPattern() throws Exception {	
//		
//		String targetPackage = "org.eclipse";
//		String entityName = "anIRDAssoc";
//		String patternname = "com.test.IRDAssoc";
//		String extendedArtifact = "org.eclipse.mynewAssociation";
//		String aEndType = "org.eclipse.wobble";
//		String zEndType = "org.eclipse.anIRD";
//
//		
//		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
//		assertNotNull("No patterns with name '"+patternname +"' returned", pattern);
//		IRelationPattern relPattern = (IRelationPattern) pattern;
//		relPattern.executeRequests(project, targetPackage, entityName, extendedArtifact,
//				aEndType,zEndType);
//		
//		IArtifactManagerSession mgrSession = project
//			.getArtifactManagerSession();
//		ArtifactTestHelper artifactHelper = new ArtifactTestHelper(project);
//		String fqn = targetPackage+"."+entityName;
//		IAbstractArtifact created = mgrSession
//				.getArtifactByFullyQualifiedName(fqn);
//
//		assertNotNull("Failed to retrieve artifact for Mgr Session",
//				created);
//		assertTrue("Name doesn't match ", created.getName().equals(entityName));
//		assertTrue("FQN doesn't match " + fqn, created
//				.getFullyQualifiedName().equals(fqn));
//		assertTrue(created.isAbstract());
//		
//		assertEquals("Extended Artifact does not match",extendedArtifact,created.getExtendedArtifact().getFullyQualifiedName());
//		
//		assertTrue("Created artrifact is not an Association : "+created.getClass().getName(), created instanceof IAssociationArtifact);
//		if (created instanceof IAssociationArtifact){
//			IAssociationArtifact association = (IAssociationArtifact) created;
//			if (association.getAEnd() == null){
//				fail("AEnd not set");
//			} else {
//				if (association.getAEnd().getType() == null){
//					fail("AEnd Type not set");
//				}
//			}
//			if (association.getZEnd() == null){
//				fail("ZEnd not set");
//			} else {
//				if (association.getAEnd().getType() == null){
//					fail("ZEnd Type not set");
//				}
//			}
//			assertEquals("AEndType does not match", aEndType,association.getAEnd().getType().getFullyQualifiedName());
//			assertEquals("ZEndType does not match", zEndType,association.getZEnd().getType().getFullyQualifiedName());
//		}
//		
//		
//		
//	}
	
}
