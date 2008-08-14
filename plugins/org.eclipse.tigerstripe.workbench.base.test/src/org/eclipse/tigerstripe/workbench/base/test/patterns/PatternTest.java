/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.patterns;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.base.test.model.ArtifactTestHelper;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.profile.WorkbenchProfile;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.Stereotype;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.StereotypeAttributeFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.patterns.INodePattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeScopeDetails;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class PatternTest extends TestCase {
	
	private ITigerstripeModelProject project;
	private final static String PROJECTNAME = "TestPatterns";
	private final static String GENERAL_STEREOTYPE = "General";
	private final static String ATTRIBUTED_STEREOTYPE = "WithAttribute";
	private final static String STEREOTYPE_ATTRIBUTE_NAME= "value";

	@Override
	protected void setUp() throws Exception {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName(PROJECTNAME);
		project = (ITigerstripeModelProject) TigerstripeCore.createProject(
				projectDetails, null, ITigerstripeModelProject.class, null,
				null);
		
		IWorkbenchProfileSession session = TigerstripeCore.getWorkbenchProfileSession();
		
		IWorkbenchProfile profile = session.makeWorkbenchProfile();
		
		WorkbenchProfile p = (WorkbenchProfile) profile;
		p.setName("testProfile");
		
		Stereotype stereotypeOne = new Stereotype(p);
		stereotypeOne.setName(GENERAL_STEREOTYPE);
		IStereotypeScopeDetails scope = stereotypeOne.getStereotypeScopeDetails();
		scope.setArgumentLevel(true);
		scope.setLiteralLevel(true);
		scope.setAttributeLevel(true);
		scope.setAssociationEndLevel(true);
		scope.setMethodLevel(true);
		scope.setArtifactLevelTypes(new String[]{IManagedEntityArtifact.class.getName(), IAssociationArtifact.class.getName()});

		p.addStereotype(stereotypeOne);

		Stereotype stereotypeTwo = new Stereotype(p);
		stereotypeTwo.setName(ATTRIBUTED_STEREOTYPE);
		IStereotypeScopeDetails scopeTwo = stereotypeOne.getStereotypeScopeDetails();
		scopeTwo.setArtifactLevelTypes(new String[]{IManagedEntityArtifact.class.getName()});
		IStereotypeAttribute attribute = StereotypeAttributeFactory.makeAttribute(IStereotypeAttribute.STRING_ENTRY_KIND);
		attribute.setName(STEREOTYPE_ATTRIBUTE_NAME);
		attribute.setArray(true);
		stereotypeTwo.addAttribute(attribute);
		
		p.addStereotype(stereotypeTwo);
		
		session.saveAsActiveProfile(profile);
		session.reloadActiveProfile();
		
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
	
//	public void testAssociationArtifactPattern() throws Exception {
//
//		String targetPackage = "org.eclipse";
//		String entityName = "mynewAssociation";
//		String patternname = "org.eclipse.tigerstripe.workbench.base.Association";
//		String aEndType = "org.eclipse.endAType";
//		String zEndType = "org.eclipse.endZType";
//
//		testRelationPatternArtifact( patternname, targetPackage,  entityName, aEndType, zEndType);
//
//	}
//	
//	public void testAssociationClassArtifactPattern() throws Exception {
//
//		String targetPackage = "org.eclipse";
//		String entityName = "mynewAssociationClass";
//		String patternname = "org.eclipse.tigerstripe.workbench.base.AssociationClass";
//		String aEndType = "org.eclipse.endAType";
//		String zEndType = "org.eclipse.endZType";
//
//		testRelationPatternArtifact( patternname, targetPackage,  entityName, aEndType, zEndType);
//
//	}
//	
//	public void testDependencyArtifactPattern() throws Exception {
//
//		String targetPackage = "org.eclipse";
//		String entityName = "mynewDependency";
//		String patternname = "org.eclipse.tigerstripe.workbench.base.Dependency";
//		String aEndType = "org.eclipse.endAType";
//		String zEndType = "org.eclipse.endZType";
//
//		testRelationPatternArtifact( patternname, targetPackage,  entityName, aEndType, zEndType);
//
//	}
	
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
	
	public void testDisabledPattern() throws Exception {

		String patternname = "com.test.shouldNotAppear";

		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNull("Patterns with name '"+patternname +"'was returned", pattern);

	}
	

	public void testIRDEntityPattern() throws Exception {

		String targetPackage = "org.eclipse";
		String entityName = "anIRD";
		String patternname = "com.test.IRDEntity";

		String attributeName = "mandatoryField";
		String literalName = "mandatoryLiteral";
		String methodName = "mandatoryMethod";
		String extendedArtifact = "org.eclipse.anotherIRD";
		String exceptionName = "org.eclipse.anException";
		String argumentName = "firstOne";
		
		
		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNotNull("No patterns with name '"+patternname +"' returned", pattern);
		INodePattern mePattern = (INodePattern) pattern;
		mePattern.executeRequests(project, targetPackage, entityName, extendedArtifact);
		
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
		assertTrue(created.isAbstract());
		
		assertEquals("Wrong number of Stereotypes on artifact", 2,created.getStereotypeInstances().size());
		assertNotNull(GENERAL_STEREOTYPE+" stereotype not present on artifact",created.getStereotypeInstanceByName(GENERAL_STEREOTYPE));
		IStereotypeInstance attributedSterotype = created.getStereotypeInstanceByName(ATTRIBUTED_STEREOTYPE);
		assertNotNull(ATTRIBUTED_STEREOTYPE+" stereotype not present on artifact",attributedSterotype);
		if (attributedSterotype != null){
			String[] values = attributedSterotype.getAttributeValues(STEREOTYPE_ATTRIBUTE_NAME);
			if ( values.length != 1){
				assertTrue(values[0].equals("firstOne"));
			} else {
				assertEquals(ATTRIBUTED_STEREOTYPE+" Wrong number of attributeValues",1,values.length);
			}
		}
		
		assertTrue("Wrong number of Fields",created.getFields().size() == 1);
		for (IField field :created.getFields()){
			assertTrue("Wrong Field name", field.getName().equals(attributeName));
			assertEquals("Wrong number of Stereotypes on field", 1,field.getStereotypeInstances().size());
			assertNotNull(GENERAL_STEREOTYPE+" stereotype not present on field",field.getStereotypeInstanceByName(GENERAL_STEREOTYPE));
			
		}
		assertTrue("Wrong number of Literals",created.getLiterals().size() == 1);
		for (ILiteral literal :created.getLiterals()){
			assertTrue("Wrong Literal name", literal.getName().equals(literalName));
			assertEquals("Wrong number of Stereotypes on literal", 1,literal.getStereotypeInstances().size());
			assertNotNull(GENERAL_STEREOTYPE+" stereotype not present on literal",literal.getStereotypeInstanceByName(GENERAL_STEREOTYPE));
			
		}
		assertTrue("Wrong number of methods",created.getMethods().size() == 1);
		for (IMethod method :created.getMethods()){
			assertTrue("Wrong Method name", method.getName().equals(methodName));
			assertEquals("Wrong number of Stereotypes on method", 1,method.getStereotypeInstances().size());
			assertNotNull(GENERAL_STEREOTYPE+" stereotype not present on method",method.getStereotypeInstanceByName(GENERAL_STEREOTYPE));
			assertEquals("Wrong number of *return* Stereotypes on method", 1,method.getReturnStereotypeInstances().size());
			assertNotNull(GENERAL_STEREOTYPE+" *return* stereotype not present on method",method.getReturnStereotypeInstanceByName(GENERAL_STEREOTYPE));
			assertEquals("Wrong number of arguments",1, method.getArguments().size());
			for (IArgument arg : method.getArguments()){
				assertTrue("Wrong argument name", arg.getName().equals(argumentName));
				assertEquals("Wrong number of Stereotypes on argument", 1,arg.getStereotypeInstances().size());
				assertNotNull(GENERAL_STEREOTYPE+" stereotype not present on argument",arg.getStereotypeInstanceByName(GENERAL_STEREOTYPE));
			}
			
			assertEquals("Wrong number of exceptions",1, method.getExceptions().size());
			for (IException ex : method.getExceptions()){
				assertTrue("Wrong exception name", ex.getFullyQualifiedName().equals(exceptionName));
			}
			
		}
	}
	
	public void testIRDAssocPattern() throws Exception {	
		
		String targetPackage = "org.eclipse";
		String entityName = "anIRDAssoc";
		String patternname = "com.test.IRDAssoc";
		String extendedArtifact = "org.eclipse.mynewAssociation";
		String aEndType = "org.eclipse.wobble";
		String zEndType = "org.eclipse.anIRD";

		
		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNotNull("No patterns with name '"+patternname +"' returned", pattern);
		IRelationPattern relPattern = (IRelationPattern) pattern;
		relPattern.executeRequests(project, targetPackage, entityName, extendedArtifact,
				aEndType,zEndType);
		
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
		assertTrue(created.isAbstract());
		
		assertEquals("Extended Artifact does not match",extendedArtifact,created.getExtendedArtifact().getFullyQualifiedName());
		
		assertTrue("Created artrifact is not an Association : "+created.getClass().getName(), created instanceof IAssociationArtifact);
		if (created instanceof IAssociationArtifact){
			IAssociationArtifact association = (IAssociationArtifact) created;
			if (association.getAEnd() == null){
				fail("AEnd not set");
			} else {
				if (association.getAEnd().getType() == null){
					fail("AEnd Type not set");
				}
				assertEquals("Wrong number of Stereotypes on aEnd", 1,association.getAEnd().getStereotypeInstances().size());
				assertNotNull(GENERAL_STEREOTYPE+" stereotype not present on aEnd",association.getAEnd().getStereotypeInstanceByName(GENERAL_STEREOTYPE));
				
			}
			if (association.getZEnd() == null){
				fail("ZEnd not set");
			} else {
				if (association.getAEnd().getType() == null){
					fail("ZEnd Type not set");
				}
				assertEquals("Wrong number of Stereotypes on zEnd", 1,association.getZEnd().getStereotypeInstances().size());
				assertNotNull(ATTRIBUTED_STEREOTYPE+" stereotype not present on zEnd",association.getZEnd().getStereotypeInstanceByName(ATTRIBUTED_STEREOTYPE));
			}
			assertEquals("AEndType does not match", aEndType,association.getAEnd().getType().getFullyQualifiedName());
			assertEquals("ZEndType does not match", zEndType,association.getZEnd().getType().getFullyQualifiedName());
		}
		
		
		
	}
	
}
