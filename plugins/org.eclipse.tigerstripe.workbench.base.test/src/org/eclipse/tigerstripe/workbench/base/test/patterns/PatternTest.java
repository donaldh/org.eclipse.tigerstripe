package org.eclipse.tigerstripe.workbench.base.test.patterns;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.base.test.model.ArtifactTestHelper;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
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
		
		testNodePatternArtifact( patternname, targetPackage,  entityName);
		
	}
	
	public void testDatatypeArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewDatatype";
		String patternname = "org.eclipse.tigerstripe.workbench.base.Datatype";

		testNodePatternArtifact( patternname, targetPackage,  entityName);

	}
	
	public void testEnumerationArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewEnumeration";
		String patternname = "org.eclipse.tigerstripe.workbench.base.Enumeration";

		testNodePatternArtifact( patternname, targetPackage,  entityName);

	}
	public void testEventArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewEvent";
		String patternname = "org.eclipse.tigerstripe.workbench.base.Event";

		testNodePatternArtifact( patternname, targetPackage,  entityName);

	}
	public void testExceptionArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewException";
		String patternname = "org.eclipse.tigerstripe.workbench.base.Exception";

		testNodePatternArtifact( patternname, targetPackage,  entityName);

	}
	public void testPackageArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewPackage";
		String patternname = "org.eclipse.tigerstripe.workbench.base.Package";

		testNodePatternArtifact( patternname, targetPackage,  entityName);

	}
	public void testQueryArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewQuery";
		String patternname = "org.eclipse.tigerstripe.workbench.base.Query";

		testNodePatternArtifact( patternname, targetPackage,  entityName);

	}
	public void testSessionArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewSession";
		String patternname = "org.eclipse.tigerstripe.workbench.base.Session";

		testNodePatternArtifact( patternname, targetPackage,  entityName);

	}
	public void testUpdateArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewUpdate";
		String patternname = "org.eclipse.tigerstripe.workbench.base.UpdateProcedure";

		testNodePatternArtifact( patternname, targetPackage,  entityName);

	}
	
	public void testAssociationArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewAssociation";
		String patternname = "org.eclipse.tigerstripe.workbench.base.Association";
		String aEndType = "org.eclipse.endAType";
		String zEndType = "org.eclipse.endZType";

		testRelationPatternArtifact( patternname, targetPackage,  entityName, aEndType, zEndType);

	}
	
	public void testAssociationClassArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewAssociationClass";
		String patternname = "org.eclipse.tigerstripe.workbench.base.AssociationClass";
		String aEndType = "org.eclipse.endAType";
		String zEndType = "org.eclipse.endZType";

		testRelationPatternArtifact( patternname, targetPackage,  entityName, aEndType, zEndType);

	}
	
	public void testDependencyArtifactPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "mynewDependency";
		String patternname = "org.eclipse.tigerstripe.workbench.base.Dependency";
		String aEndType = "org.eclipse.endAType";
		String zEndType = "org.eclipse.endZType";

		testRelationPatternArtifact( patternname, targetPackage,  entityName, aEndType, zEndType);

	}
	
	private void testNodePatternArtifact(String patternname,String targetPackage, String entityName) throws Exception{
		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNotNull("No patterns with name '"+patternname +"' returned", pattern);
		INodePattern mePattern = (INodePattern) pattern;
		mePattern.executeRequests(project, targetPackage, entityName, "");
		
		IArtifactManagerSession mgrSession = project
			.getArtifactManagerSession();
		String fqn = targetPackage+"."+entityName;
		IAbstractArtifact created = mgrSession
				.getArtifactByFullyQualifiedName(fqn);

		assertNotNull("Failed to retrieve artifact for Mgr Session",
				created);
		assertTrue("Name doesn't match ", created.getName().equals(entityName));
		assertTrue("FQN doesn't match " + fqn, created
				.getFullyQualifiedName().equals(fqn));
		assertFalse(created.isAbstract());
		assertTrue(created.getFields().isEmpty());
		assertTrue(created.getLiterals().isEmpty());
		assertTrue(created.getMethods().isEmpty());
	}

	private void testRelationPatternArtifact(String patternname,String targetPackage, String entityName, String aEndType, String zEndType) throws Exception{
		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNotNull("No patterns with name '"+patternname +"' returned", pattern);
		IRelationPattern mePattern = (IRelationPattern) pattern;
		mePattern.executeRequests(project, targetPackage, entityName, "",aEndType,zEndType);
		
		IArtifactManagerSession mgrSession = project
			.getArtifactManagerSession();
		String fqn = targetPackage+"."+entityName;
		IAbstractArtifact created = mgrSession
				.getArtifactByFullyQualifiedName(fqn);

		assertNotNull("Failed to retrieve artifact for Mgr Session",
				created);
		assertTrue("Name doesn't match ", created.getName().equals(entityName));
		assertTrue("FQN doesn't match " + fqn, created
				.getFullyQualifiedName().equals(fqn));
		assertFalse(created.isAbstract());
		assertTrue(created.getFields().isEmpty());
		assertTrue(created.getLiterals().isEmpty());
		assertTrue(created.getMethods().isEmpty());
		
		if (created instanceof AssociationArtifact){
			assertEquals("A end type does not match", aEndType, ((AssociationArtifact) created).getAEnd().getType().getFullyQualifiedName());
			assertEquals("Z end type does not match", zEndType, ((AssociationArtifact) created).getZEnd().getType().getFullyQualifiedName());
		}
		if (created instanceof DependencyArtifact){
			assertEquals("A end type does not match", aEndType, ((DependencyArtifact) created).getAEndType().getFullyQualifiedName());
			assertEquals("Z end type does not match", zEndType, ((DependencyArtifact) created).getZEndType().getFullyQualifiedName());
		}
		
	}
	
	// These two tests assume the presence of a suitable test plugin.
	// I'm not sure how the JUnit tests set up allows us to do that.
	
	// Note that the first test hers is incompatible with one above....
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
