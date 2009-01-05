/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.facet;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopeAnnotationPattern;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.model.annotation.AnnotationHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestFacetWithAnnotations extends TestCase {

	private ITigerstripeModelProject project;

	@Override
	protected void setUp() throws Exception {
		project = ModelProjectHelper.createModelProject(
				"TestFacetWithAnnotations", true);

	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);
	}

	public void testDefineIncludeAnnotationPatterns() throws Exception {
		testDefineAnnotationPatterns("includeFacet", ISegmentScope.INCLUDES);
	}

	public void testDefineExcludeAnnotationPatterns() throws Exception {
		testDefineAnnotationPatterns("excludeFacet", ISegmentScope.EXCLUDES);
	}

	/**
	 * Include M2 by Annotation. See it's the only one in facet
	 * 
	 * @throws Exception
	 */
	public void testResolveFacetWithAnnotationInclude() throws Exception {
		IProject iProject = (IProject) project.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile("facetWithAnnotationInclude");
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(
				facetFile, null);
		ISegmentScope scope = facet.getISegmentScope();

		AnnotationType[] annotationTypes = AnnotationPlugin.getManager()
				.getTypes();

		ScopeAnnotationPattern pattern = new ISegmentScope.ScopeAnnotationPattern();
		pattern.type = ISegmentScope.INCLUDES;
		pattern.annotationID = annotationTypes[1].getId();
		scope.addAnnotationPattern(pattern);
		facet.doSave();

		IManagedEntityArtifact m1 = (IManagedEntityArtifact) project
				.getArtifactManagerSession().getArtifactByFullyQualifiedName(
						ModelProjectHelper.M1);
		IManagedEntityArtifact m2 = (IManagedEntityArtifact) project
				.getArtifactManagerSession().getArtifactByFullyQualifiedName(
						ModelProjectHelper.M2);
		
		IManagedEntityArtifact m3 = (IManagedEntityArtifact) project
		.getArtifactManagerSession().getArtifactByFullyQualifiedName(
				ModelProjectHelper.M3);

		IAbstractArtifact as1 = project.getArtifactManagerSession()
			.getArtifactByFullyQualifiedName(ModelProjectHelper.AS1);
		
		IAbstractArtifact ac1 = project.getArtifactManagerSession()
			.getArtifactByFullyQualifiedName(ModelProjectHelper.AC1);
		
		Assert.assertNotNull(m1);
		EObject obj = annotationTypes[1].createInstance();
		Annotation ann = AnnotationPlugin.getManager().addAnnotation(m2, obj);
//		m2.saveAnnotation(ann);
		AnnotationHelper.getInstance().saveAnnotation(ann);

		IFacetReference ref = project.makeFacetReference(facetFile
				.getProjectRelativePath().toOSString());

		project.setActiveFacet(ref, null);

		Assert.assertTrue(!m1.isInActiveFacet());
		Assert.assertTrue(m2.isInActiveFacet());
		Assert.assertTrue(!m3.isInActiveFacet());
		Assert.assertTrue(!as1.isInActiveFacet());
		Assert.assertTrue(!ac1.isInActiveFacet());
	}

	/**
	 * Exclude AS1 by annotation and see that M3 is not in facet anymore
	 * Needs M1 to be included for this to work!
	 * 
	 * @throws Exception
	 */
	public void testResolveFacetWithAnnotationExclude() throws Exception {
		IProject iProject = (IProject) project.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile("facetWithAnnotationExclude");
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(
				facetFile, null);
		ISegmentScope scope = facet.getISegmentScope();

		AnnotationType[] annotationTypes = AnnotationPlugin.getManager()
				.getTypes();

		ScopeAnnotationPattern pattern = new ISegmentScope.ScopeAnnotationPattern();
		pattern.type = ISegmentScope.EXCLUDES;
		pattern.annotationID = annotationTypes[1].getId();

		ScopePattern pat = new ISegmentScope.ScopePattern();
		pat.pattern = ModelProjectHelper.M1;
		scope.addPattern(pat);
		scope.addAnnotationPattern(pattern);
		facet.doSave();

		IManagedEntityArtifact m1 = (IManagedEntityArtifact) project
				.getArtifactManagerSession().getArtifactByFullyQualifiedName(
						ModelProjectHelper.M1);
		IManagedEntityArtifact m2 = (IManagedEntityArtifact) project
				.getArtifactManagerSession().getArtifactByFullyQualifiedName(
						ModelProjectHelper.M2);

		IManagedEntityArtifact m3 = (IManagedEntityArtifact) project
				.getArtifactManagerSession().getArtifactByFullyQualifiedName(
						ModelProjectHelper.M3);

		IAbstractArtifact as1 = project.getArtifactManagerSession()
				.getArtifactByFullyQualifiedName(ModelProjectHelper.AS1);

		IAbstractArtifact ac1 = project.getArtifactManagerSession()
			.getArtifactByFullyQualifiedName(ModelProjectHelper.AC1);
		
		Assert.assertNotNull(as1);

		EObject obj = annotationTypes[1].createInstance();
		Annotation ann = AnnotationPlugin.getManager().addAnnotation(as1, obj);
		//AnnotationPlugin.getManager().save(ann);
		AnnotationHelper.getInstance().saveAnnotation(ann);
		IFacetReference ref = project.makeFacetReference(facetFile
				.getProjectRelativePath().toOSString());

		project.setActiveFacet(ref, null);

		
		Assert.assertTrue(m1.isInActiveFacet());
		Assert.assertTrue(ac1.isInActiveFacet());
		Assert.assertTrue(m2.isInActiveFacet());
		Assert.assertTrue(!m3.isInActiveFacet());
		Assert.assertTrue(!as1.isInActiveFacet());
		
	}

	private void testDefineAnnotationPatterns(String facetName, int type)
			throws Exception {
		IProject iProject = (IProject) project.getAdapter(IProject.class);
		IFile facetFile = iProject.getFile(facetName);
		IContractSegment facet = InternalTigerstripeCore.createModelFacet(
				facetFile, null);
		ISegmentScope scope = facet.getISegmentScope();

		AnnotationType[] annotationTypes = AnnotationPlugin.getManager()
				.getTypes();

		ScopeAnnotationPattern pattern = new ISegmentScope.ScopeAnnotationPattern();
		pattern.type = type;
		pattern.annotationID = annotationTypes[0].getId();
		scope.addAnnotationPattern(pattern);
		facet.doSave();

		IFacetReference ref = project.makeFacetReference(facetFile
				.getProjectRelativePath().toOSString());

		IContractSegment resolvedFacet = ref.resolve();
		ScopeAnnotationPattern[] resolvedPatterns = resolvedFacet
				.getISegmentScope().getAnnotationPatterns();

		Assert.assertTrue(resolvedPatterns.length == 1);
		Assert.assertTrue(resolvedPatterns[0].type == type);
		Assert.assertTrue(resolvedPatterns[0].annotationID
				.equals(annotationTypes[0].getId()));
	}

}
