/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - jworrell
 *******************************************************************************//**
 * 
 */
package org.eclipse.tigerstripe.workbench.base.test.annotation;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.internal.core.AnnotationManager;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.SomeTestAnnotsFactory;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot2;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot3;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.annotation.AnnotationHelper;
import org.eclipse.tigerstripe.workbench.model.annotation.IAnnotationCapable;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * @author jworrell
 *
 */
public class TestAnnotationCreationAPI extends AbstractTigerstripeTestCase {

	private static final String SOME_TEST_ANNOTS = "org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots";

	private static final String TS_SCHEME = "tigerstripe";

	private static final String GENERATOR = "TestM1GenerationGenerator";

	private ITigerstripeModelProject project;
	private ITigerstripeM1GeneratorProject generator;

	private IAnnotationManager annotationManager;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		annotationManager = AnnotationPlugin.getManager();
		IAbstractTigerstripeProject aProject = createModelProject("AnnotationTestProject");
		assertTrue(aProject instanceof ITigerstripeModelProject);
		project = (ITigerstripeModelProject) aProject;
		
		createEachArtifactType(project);

//		TigerstripeCore.findProject(GENERATOR).delete(true, new NullProgressMonitor());	
//		generator = M1ProjectHelper.createM1Project(GENERATOR, true);
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		removeEachArtifactType(project);
		deleteModelProject(project);
		project = null;
		if(generator != null)
		{
			generator.delete(false, new NullProgressMonitor());
			generator = null;
		}
	}

	public final void testAnnotateArtifactsAnnotationMgr() throws AnnotationException, TigerstripeException
	{
		
		try {
			int count = 0;
			for(IAbstractArtifact artifact : getArtifacts(project))
			{
				TestAnnot1 type = SomeTestAnnotsFactory.eINSTANCE.createTestAnnot1();
				type.setTwine("Hawser("+(++count)+")");
				annotationManager.addAnnotation(artifact, type);
			}

			for(IAbstractArtifact artifact : getArtifacts(project))
			{
				TestAnnot3 type = SomeTestAnnotsFactory.eINSTANCE.createTestAnnot3();
				type.setN(++count);
				annotationManager.addAnnotation(artifact, type);
			}
			
			for(IAbstractArtifact artifact : getArtifacts(project))
			{
				System.out.println("Tigerstripe Annotations: "+artifact.getAnnotations(TS_SCHEME));
			}
		}
		finally {
			removeAnnotations(project);
		}
	}

//	public final void testAnnotateArtifactsArtifactIF() throws TigerstripeException
//	{
//		IAnnotationManager annotationManager = AnnotationPlugin.getManager();
//		try {
//			int count = 100;
//			for(IAbstractArtifact artifact : getArtifacts(project))
//			{
//				Annotation annotation = artifact.addAnnotation(TS_SCHEME, SOME_TEST_ANNOTS, "TestAnnot1");
//				TestAnnot1 type = (TestAnnot1)annotation.getContent();
//				type.setTwine("Hawser("+(++count)+")");
//				artifact.saveAnnotation(annotation);
//			}
//
//			for(IAbstractArtifact artifact : getArtifacts(project))
//			{
////				TestAnnot3 type = (TestAnnot3)artifact.addAnnotation(TS_SCHEME, SOME_TEST_ANNOTS, "TestAnnot3");
//				Annotation annotation = artifact.addAnnotation(TestAnnot3.class);
//				TestAnnot3 type = (TestAnnot3)annotation.getContent();
//				type.setN(++count);
//				artifact.saveAnnotation(annotation);
//			}
//			
//			IAbstractArtifact mne = null;
//			IAbstractArtifact ass = null;
//			for(IAbstractArtifact artifact : getArtifacts(project))
//			{
//				System.out.println("Tigerstripe Annotations: "+artifact.getAnnotations(TS_SCHEME));
//				if(artifact instanceof IManagedEntityArtifact) mne = artifact;
//				if(artifact instanceof IAssociationArtifact) ass = artifact;
//			}
//			
//			TestAnnot2 type = (TestAnnot2)mne.addAnnotation(TS_SCHEME, SOME_TEST_ANNOTS, "TestAnnot2").getContent();
//			try
//			{
//				type = (TestAnnot2)ass.addAnnotation(TS_SCHEME, SOME_TEST_ANNOTS, "TestAnnot2").getContent();
//				fail("Should not be able to attach testAnnot2 to IAssociationArtifact");
//			}
//			catch(Exception e)
//			{
////				System.out.println("Well here we are for invalid target: "+e.getMessage());
//			}
//			try
//			{
//				type = (TestAnnot2)mne.addAnnotation(TS_SCHEME, SOME_TEST_ANNOTS, "TestAnnot2").getContent();
//				fail("Should not be able to attach second TestAnnot2 to IManagedEntityArtifact");
//			}
//			catch(Exception e)
//			{
////				System.out.println("Well here we are for broken uniqueness: "+e.getMessage());
//			}
//		}
//		finally {
//			removeAnnotations(project);
//		}
//	}

	public final void testAnnotationHelper() throws TigerstripeException
	{
		IAnnotationManager annotationManager = AnnotationPlugin.getManager();
		try {
			AnnotationHelper helper = AnnotationHelper.getInstance();
			int count = 1000;
			for(IAbstractArtifact artifact : getArtifacts(project))
			{
				Annotation annotation = helper.addAnnotation(artifact, SOME_TEST_ANNOTS, "TestAnnot1");
				TestAnnot1 type = (TestAnnot1)annotation.getContent();
				type.setTwine("Hawser("+(++count)+")");
				helper.saveAnnotation(annotation);
			}

			for(IAbstractArtifact artifact : getArtifacts(project))
			{
				Annotation annotation = helper.addAnnotation(artifact, TestAnnot3.class);
				TestAnnot3 type = (TestAnnot3)annotation.getContent();
				type.setN(++count);
				helper.saveAnnotation(annotation);
			}
			
			IAbstractArtifact mne = null;
			IAbstractArtifact ass = null;
			for(IAbstractArtifact artifact : getArtifacts(project))
			{
//				System.out.println("Tigerstripe Annotations: "+artifact.getAnnotations(TS_SCHEME));
				if(artifact instanceof IManagedEntityArtifact) mne = artifact;
				if(artifact instanceof IAssociationArtifact) ass = artifact;
			}
			
			TestAnnot2 type = (TestAnnot2)helper.addAnnotation(mne, SOME_TEST_ANNOTS, "TestAnnot2").getContent();
			try
			{
				type = (TestAnnot2)helper.addAnnotation(ass, SOME_TEST_ANNOTS, "TestAnnot2").getContent();
				fail("Should not be able to attach testAnnot2 to IAssociationArtifact");
			}
			catch(Exception e)
			{
//				System.out.println("Well here we are for invalid target: "+e.getMessage());
			}
			try
			{
				type = (TestAnnot2)helper.addAnnotation(mne, SOME_TEST_ANNOTS, "TestAnnot2").getContent();
				fail("Should not be able to attach second TestAnnot2 to IManagedEntityArtifact");
			}
			catch(Exception e)
			{
//				System.out.println("Well here we are for broken uniqueness: "+e.getMessage());
			}

//			removeAnnotations(project);
			
			count = 1100;
			for(IAbstractArtifact artifact : getArtifacts(project))
			{
				Annotation annotation = helper.getAnnotation(artifact, "TestAnnot1");
				if(annotation != null)
				{
					TestAnnot1 ta1 = (TestAnnot1)annotation.getContent();
					ta1.setTwine("Hawser("+(++count)+")");
					helper.saveAnnotation(annotation);
				}
			}

			for(IAbstractArtifact artifact : getArtifacts(project))
			{
				Object stuff = artifact.getAnnotations("TestAnnot1");
				TestAnnot1 ta1 = (TestAnnot1)artifact.getAnnotation("TestAnnot1");
				if(ta1 != null)
				{
					assertTrue(ta1.getTwine().startsWith("Hawser(11"));
				}
			}

			count = 1200;
			for(IAbstractArtifact artifact : getArtifacts(project))
			{
				// TestAnnot3 type = (TestAnnot3)artifact.addAnnotation(TS_SCHEME, SOME_TEST_ANNOTS, "TestAnnot3");
				Annotation annotation = helper.getAnnotation(artifact, TestAnnot3.class);
				// Annotation annotation = helper.getAnnotation(artifact, "TestAnnot3");
				if(annotation != null)
				{
					TestAnnot3 ta3 = (TestAnnot3)annotation.getContent();
					ta3.setN(++count);
					helper.saveAnnotation(annotation);
				}
			}

			for(IAbstractArtifact artifact : getArtifacts(project))
			{
				TestAnnot3 ta1 = (TestAnnot3)artifact.getAnnotation("TestAnnot3");
				if(ta1 != null)
				{
					assertEquals(12, ta1.getN()/100);
				}
			}
		}
		finally {
			removeAnnotations(project);
		}
	}
	
//	public void testGenerate() throws TigerstripeException {
//
//		ITigerstripeModelProject wProj = (ITigerstripeModelProject) project.makeWorkingCopy(null);
//		GeneratorDeploymentHelper helper = new GeneratorDeploymentHelper();
//
//		assertTrue(helper.canDeploy(generator));
//		IPath path = helper.deploy(generator, null);
//
//		List<PluggableHousing> housings = PluginManager.getManager()
//				.getRegisteredPluggableHousings();
//
//		PluggableHousing housing = null;
//		for(Iterator<PluggableHousing> i = housings.iterator(); housing == null && i.hasNext(); )
//		{
//			PluggableHousing h = i.next();
//			if(h.getBody().getPProject().getProjectDetails().getName().equals(GENERATOR))
//				housing = h;
//		}
//		
//		IPluginConfig pluginConfig = housing
//				.makeDefaultPluginConfig((TigerstripeProjectHandle) project);
//		wProj.addPluginConfig(pluginConfig);
//		wProj.commit(null);
//
//		IM1RunConfig runConfig = (IM1RunConfig) RunConfig.newGenerationConfig(
//				project, RunConfig.M1);
//		PluginRunStatus[] status = project.generate(runConfig, null);
//		assertTrue( status.length == 1);
//		if(!status[0].isOK()) System.err.println("Status: "+status[0]);
//		assertTrue( status[0].isOK() );
//		
//		// Look for generated file
//		IProject iProj = (IProject) project.getAdapter(IProject.class);
//		IResource res = iProj.findMember("target/tigerstripe.gen/list.txt");
//		assertNotNull(res);
//		assertTrue(res.exists());
//
//		helper.unDeploy(path, null);
//	}

	protected void removeAnnotations(IAbstractTigerstripeProject aProject) throws TigerstripeException
	{
		for(IAbstractArtifact a : getArtifacts(aProject))
		{
			annotationManager.removeAnnotations(a);
		}
	}
}
