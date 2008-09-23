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

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot2;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot3;
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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPatternResult;
import org.eclipse.tigerstripe.workbench.patterns.IEnumPattern;
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
	
	private final static String SOME_TEST_ANNOTS = "org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots";

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

		testEnumPatternArtifact( patternname, targetPackage,  entityName, "String");
		
		

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
	
	private IAbstractArtifact testNodePatternArtifact(String patternname,String targetPackage, String entityName) throws Exception{
		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNotNull("No patterns with name '"+patternname +"' returned", pattern);
		INodePattern mePattern = (INodePattern) pattern;
		//mePattern.executeRequests(project, targetPackage, entityName, "",true);
		
		IArtifactPatternResult artifact = mePattern.createArtifact(project, targetPackage, entityName, "");
		mePattern.addToManager(project, artifact.getArtifact());
		mePattern.annotateArtifact(project, artifact);
		
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
		return created;
	}

	private IAbstractArtifact testEnumPatternArtifact(String patternname,String targetPackage, String entityName,String baseType) throws Exception{
		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNotNull("No patterns with name '"+patternname +"' returned", pattern);
		IEnumPattern mePattern = (IEnumPattern) pattern;
		
		IArtifactPatternResult artifact = mePattern.createArtifact(project, targetPackage, entityName, "",baseType);
		mePattern.addToManager(project, artifact.getArtifact());
		mePattern.annotateArtifact(project, artifact);

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
		
		IEnumArtifact enumArtifact = (IEnumArtifact) created;
		assertEquals("Enum Base Type does not match", "String",enumArtifact.getBaseTypeStr());
		
		assertFalse(created.isAbstract());
		assertTrue(created.getFields().isEmpty());
		assertTrue(created.getLiterals().isEmpty());
		assertTrue(created.getMethods().isEmpty());
		return created;
	}
	
	private void testRelationPatternArtifact(String patternname,String targetPackage, String entityName, String aEndType, String zEndType) throws Exception{
		IPattern pattern = PatternFactory.getInstance().getPattern(patternname);
		assertNotNull("No patterns with name '"+patternname +"' returned", pattern);
		IRelationPattern mePattern = (IRelationPattern) pattern;
		//mePattern.executeRequests(project, targetPackage, entityName, "",aEndType,zEndType,true);
		IArtifactPatternResult artifact =  mePattern.createArtifact(project, targetPackage, entityName, "",aEndType,zEndType);
		mePattern.addToManager(project, artifact.getArtifact());
		mePattern.annotateArtifact(project, artifact);

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
		//mePattern.executeRequests(project, targetPackage, entityName, extendedArtifact,true);
		
		IArtifactPatternResult artifact =  mePattern.createArtifact(project, targetPackage, entityName, "");
		mePattern.addToManager(project, artifact.getArtifact());
		mePattern.annotateArtifact(project, artifact);
		
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
		
		assertEquals("Wrong number of Annotations on artifact",2,created.getAnnotations().size());
		List<Object> test2Annos = created.getAnnotations(TestAnnot2.class);
		assertEquals("Wrong number of TestAnnot2 on artifact", 1, test2Annos.size());
		List<Object> test1Annos = created.getAnnotations(TestAnnot1.class);
		assertEquals("Wrong number of TestAnnot1 on artifact", 1, test2Annos.size());
		for (Object ann : test1Annos){
			if (ann instanceof TestAnnot1){
				TestAnnot1 ta1 = (TestAnnot1) ann;
				assertEquals("TestAnnot1 value on artifact did not match expected value",ta1.getTwine(),"old rope");
			} else {
				fail("Object is not a TestAnnot1");
			}
		}
		
		
		assertEquals("Wrong number of Fields",1,created.getFields().size());
		for (IField field :created.getFields()){
			assertTrue("Wrong Field name", field.getName().equals(attributeName));
			assertEquals("Wrong number of Stereotypes on field", 1,field.getStereotypeInstances().size());
			assertNotNull(GENERAL_STEREOTYPE+" stereotype not present on field",field.getStereotypeInstanceByName(GENERAL_STEREOTYPE));
			assertEquals("Wrong number of Annotations on field",1,field.getAnnotations().size());
			List<Object> test3Annos = field.getAnnotations(TestAnnot3.class);
			assertEquals("Wrong number of TestAnnot3 on field", 1, test3Annos.size());
			for (Object ann : test3Annos){
				if (ann instanceof TestAnnot3){
					TestAnnot3 ta3 = (TestAnnot3) ann;
					assertEquals("TestAnnot3 value on field did not match expected value",ta3.getN(),666);
				} else {
					fail("Object is not a TestAnnot3");
				}
			}
			
		}
		assertTrue("Wrong number of Literals",created.getLiterals().size() == 1);
		for (ILiteral literal :created.getLiterals()){
			assertTrue("Wrong Literal name", literal.getName().equals(literalName));
			assertEquals("Wrong number of Stereotypes on literal", 1,literal.getStereotypeInstances().size());
			assertNotNull(GENERAL_STEREOTYPE+" stereotype not present on literal",literal.getStereotypeInstanceByName(GENERAL_STEREOTYPE));
			List<Object> test3Annos = literal.getAnnotations(TestAnnot3.class);
			assertEquals("Wrong number of TestAnnot3 on literal", 1, test3Annos.size());
			for (Object ann : test3Annos){
				if (ann instanceof TestAnnot3){
					TestAnnot3 ta3 = (TestAnnot3) ann;
					assertEquals("TestAnnot3 value on literal did not match expected value",ta3.getN(),333);
				} else {
					fail("Object is not a TestAnnot3");
				}
			}
			
		}
		assertTrue("Wrong number of methods",created.getMethods().size() == 1);
		for (IMethod method :created.getMethods()){
			assertTrue("Wrong Method name", method.getName().equals(methodName));
			assertEquals("Wrong number of Stereotypes on method", 1,method.getStereotypeInstances().size());
			assertNotNull(GENERAL_STEREOTYPE+" stereotype not present on method",method.getStereotypeInstanceByName(GENERAL_STEREOTYPE));
			assertEquals("Wrong number of *return* Stereotypes on method", 1,method.getReturnStereotypeInstances().size());
			assertNotNull(GENERAL_STEREOTYPE+" *return* stereotype not present on method",method.getReturnStereotypeInstanceByName(GENERAL_STEREOTYPE));
			List<Object> test3Annos = method.getAnnotations(TestAnnot3.class);
			assertEquals("Wrong number of TestAnnot3 on method", 1, test3Annos.size());
			for (Object ann : test3Annos){
				if (ann instanceof TestAnnot3){
					TestAnnot3 ta3 = (TestAnnot3) ann;
					assertEquals("TestAnnot3 value on method did not match expected value",ta3.getN(),999);
				} else {
					fail("Object is not a TestAnnot3");
				}
			}
			
			
			
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
		
//		TestAnnot1 t1 = SomeTestAnnotsFactory.eINSTANCE.createTestAnnot1();
//		TestAnnot2 t2 = SomeTestAnnotsFactory.eINSTANCE.createTestAnnot2();
//		TestAnnot3 t3 = SomeTestAnnotsFactory.eINSTANCE.createTestAnnot3();
//
//		t1.setTwine("old rope");
//		t3.setN(666);
//
//		ResourceSet resourceSet = new ResourceSetImpl();
//		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().
//		put("anno", new XMIResourceFactoryImpl());
//
//		URI uri = URI.createURI("http://testAnno.anno");
//		Resource resource = resourceSet.createResource(uri);
//
//		resource.getContents().add(t1);
//		resource.getContents().add(t2);
//		resource.getContents().add(t3);
//
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		resource.save(bos, null);
//
//		String x = new String(bos.toByteArray());
//		System.out.println(x);
//
//		String newX = x.replace("old rope", "lots of loot").replace("666", "911");
//
//		ByteArrayInputStream bis = new ByteArrayInputStream(newX.getBytes());
//		Resource newResource = resourceSet.createResource(uri);
//		newResource.load(bis, null);
//
//		System.out.println("New annotations: "+newResource.getContents());

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
		//relPattern.executeRequests(project, targetPackage, entityName, extendedArtifact,
		//		aEndType,zEndType,true);
		
		IArtifactPatternResult artifact =  relPattern.createArtifact(project, targetPackage, entityName, extendedArtifact,
				aEndType,zEndType);
		relPattern.addToManager(project, artifact.getArtifact());
		relPattern.annotateArtifact(project, artifact);

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
				
				List<Object> test3Annos = association.getAEnd().getAnnotations(TestAnnot3.class);
				assertEquals("Wrong number of TestAnnot3 on aEnd", 1, test3Annos.size());
				for (Object ann : test3Annos){
					if (ann instanceof TestAnnot3){
						TestAnnot3 ta3 = (TestAnnot3) ann;
						assertEquals("TestAnnot3 value on aEnd did not match expected value",ta3.getN(),111);
					} else {
						fail("Object is not a TestAnnot3");
					}
				}
				
				
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
			
			List<Object> test3Annos = association.getZEnd().getAnnotations(TestAnnot3.class);
			assertEquals("Wrong number of TestAnnot3 on zEnd", 1, test3Annos.size());
			for (Object ann : test3Annos){
				if (ann instanceof TestAnnot3){
					TestAnnot3 ta3 = (TestAnnot3) ann;
					assertEquals("TestAnnot3 value on zEnd did not match expected value",ta3.getN(),222);
				} else {
					fail("Object is not a TestAnnot3");
				}
			}
		}
		
		
		
	}
	
}
