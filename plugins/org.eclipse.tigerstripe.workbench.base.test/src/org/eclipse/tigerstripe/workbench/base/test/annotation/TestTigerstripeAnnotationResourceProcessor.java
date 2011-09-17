/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.annotation;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationFactory;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.annotation.TigerstripeAnnotationResourceProcessor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestTigerstripeAnnotationResourceProcessor extends
		AbstractTigerstripeAnnotationsTestCase {

	public static final String SCHEME_TS_MODULE = "tigerstripe_module";
	public static final String SCHEME_TS_REF_PROJECT = "tigerstripe_ref_project";

	private static final String CONTEXT_PROJECT_ID = "ContextProject";

	private static final String PROJECT_ID = CONTEXT_PROJECT_ID;
	private static final String CONTEXT_ID = CONTEXT_PROJECT_ID;
	private static final String REF_ID = "ref_id";
	private static final String RESOURCE_ID = "resource_id";
	private static final String FRAGMENT_ID = "fragment_id";

	private ITigerstripeModelProject project;
	AnnotationURIProcessor uriProcessor;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		project = (ITigerstripeModelProject) createEmptyModelProject(
				CONTEXT_PROJECT_ID, CONTEXT_PROJECT_ID);
		uriProcessor = new AnnotationURIProcessor();
	}

	@Override
	protected void tearDown() throws Exception {
		deleteModelProject(project);
		project = null;
		super.tearDown();
	}

	public void testPostLoadProcessing() throws Exception {
		// [tigerstripe_module|tigerstripe_ref_project]:/context_id:ref_id/resource_id(#fragment_id)
		// [tigerstripe_module|tigerstripe_ref_project]:/ref_id/resource_id(#fragment_id)
		moduleRefProjectPostLoadProcessing(SCHEME_TS_MODULE);
		moduleRefProjectPostLoadProcessing(SCHEME_TS_REF_PROJECT);

		// tigerstripe:/project_id/resource_id
		URI source = createURI(new String[] { PROJECT_ID, RESOURCE_ID }, null);
		uriProcessor.testPostLoadAnnotation(source, source, project);
		// tigerstripe:/project_id/resource_id#fragment_id
		source = createURI(new String[] { PROJECT_ID, RESOURCE_ID },
				FRAGMENT_ID);
		uriProcessor.testPostLoadAnnotation(source, source, project);
	}

	private void moduleRefProjectPostLoadProcessing(String scheme) {
		// [tigerstripe_module|tigerstripe_ref_project]:/context_id:ref_id/resource_id#fragment_id
		moduleRefProjectPostLoadProcessing(scheme, new String[] {
				CONTEXT_ID + ":" + REF_ID, RESOURCE_ID }, null);
		moduleRefProjectPostLoadProcessing(scheme, new String[] {
				CONTEXT_ID + ":" + REF_ID, RESOURCE_ID }, FRAGMENT_ID);

		// [tigerstripe_module|tigerstripe_ref_project]:/ref_id/resource_id
		moduleRefProjectPostLoadProcessing(scheme, new String[] { REF_ID,
				RESOURCE_ID }, null);
		moduleRefProjectPostLoadProcessing(scheme, new String[] { REF_ID,
				RESOURCE_ID }, FRAGMENT_ID);
	}

	private void moduleRefProjectPostLoadProcessing(String sourceScheme,
			String[] sourceSegments, String sourceFragment) {
		URI source = createURI(sourceScheme, sourceSegments, sourceFragment);
		URI expected = createURI(
				new String[] { CONTEXT_ID, REF_ID, RESOURCE_ID },
				sourceFragment);
		uriProcessor.testPostLoadAnnotation(source, expected, project);
	}

	public void testPreSaveProcessing() throws Exception {
		URI source = createURI(
				new String[] { CONTEXT_ID, REF_ID, RESOURCE_ID }, null);
		URI expected = createURI(new String[] { REF_ID, RESOURCE_ID }, null);
		uriProcessor.testPreSaveAnnotation(source, expected);

		source = createURI(new String[] { CONTEXT_ID, REF_ID, RESOURCE_ID },
				FRAGMENT_ID);
		expected = createURI(new String[] { REF_ID, RESOURCE_ID }, FRAGMENT_ID);
		uriProcessor.testPreSaveAnnotation(source, expected);

		source = createURI(new String[] { PROJECT_ID, RESOURCE_ID }, null);
		uriProcessor.testPreSaveAnnotation(source, source);

		source = createURI(new String[] { PROJECT_ID, RESOURCE_ID },
				FRAGMENT_ID);
		uriProcessor.testPreSaveAnnotation(source, source);
	}

	private URI createURI(String[] segments, String fragment) {
		return createURI(TigerstripeURIAdapterFactory.SCHEME_TS, segments,
				fragment);
	}

	private URI createURI(String scheme, String[] segments, String fragment) {
		return URI.createHierarchicalURI(scheme, null, null, segments, null,
				fragment);
	}

	private class AnnotationURIProcessor extends
			TigerstripeAnnotationResourceProcessor {

		public void testPostLoadAnnotation(URI source, URI expected,
				ITigerstripeModelProject context) {
			Annotation annotation = AnnotationFactory.eINSTANCE
					.createAnnotation();
			annotation.setUri(source);
			processPostLoadAnnotation(annotation, context);
			assertTrue(expected.equals(annotation.getUri()));
		}

		public void testPreSaveAnnotation(URI source, URI expected) {
			Annotation annotation = AnnotationFactory.eINSTANCE
					.createAnnotation();
			annotation.setUri(source);
			processPreSaveAnnotation(annotation);
			assertTrue(expected.equals(annotation.getUri()));
		}

		private Annotation createAnnotation(URI uri) {
			Annotation annotation = AnnotationFactory.eINSTANCE
					.createAnnotation();
			annotation.setUri(uri);
			return annotation;
		}
	}
}
