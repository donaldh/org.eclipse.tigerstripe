/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.core.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationAdapter2;
import org.eclipse.tigerstripe.annotation.core.AnnotationException;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationManager;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;
import org.eclipse.tigerstripe.espace.core.Mode;
import org.eclipse.tigerstripe.espace.resources.DeferredResourceSaver;

/**
 * This test check Annotation Framework rebuild ability via changing annotation
 * files outside framework. In case of deferred annotation file saving we need 2
 * operations:<br>
 * 1. Before annotation file modification wait until annotations will be
 * persisted (<code>waitForChanges()</code> method)<br>
 * . 2. After annotation file modification wait until framework will be notified
 * about changes (<code>waitForNotification()</code> method)
 * 
 * @author Yuri Strot
 */
public class IndexRebuildingTest extends AbstractResourceTestCase {

	protected IProject project1;
	protected IProject project2;

	protected long MAX_TEST_TIME = 10000;

	@Override
	protected void setUp() throws Exception {
		project1 = createProject("project1");
		project2 = createProject("project2");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		deleteProject(project1);
		deleteProject(project2);
	}

	public void test1() throws CoreException, AnnotationException {
		// Test is INDEX will be rebuilt after annotation file deletion
		IAnnotationManager manager = AnnotationPlugin.getManager();
		try {
			manager.addAnnotation(project1, createMimeType("text/html"));
			waitForChanges(null);
			waitForNotification(new IWorkspaceRunnable() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.core.resources.IWorkspaceRunnable#run(org.eclipse
				 * .core.runtime.IProgressMonitor)
				 */
				public void run(IProgressMonitor monitor) throws CoreException {
					removeAnnotationFile(project1);
				}
			});
			// waitForChanges();
			Annotation[] annotations = manager.getAnnotations(project1, false);
			assertEquals(annotations.length, 0);
		} finally {
			manager.removeAnnotations(project1);
		}
	}

	public void test2() throws CoreException, AnnotationException {
		// Test is annotations would be kept after another annotations
		// file will be corrupted
		IAnnotationManager manager = AnnotationPlugin.getManager();
		try {
			manager.addAnnotation(project1, createMimeType("text/html"));
			manager.addAnnotation(project2, createMimeType("text/xml"));
			manager.addAnnotation(project2, createMimeType("text/plain"));
			waitForChanges(null);
			waitForNotification(new IWorkspaceRunnable() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.core.resources.IWorkspaceRunnable#run(org.eclipse
				 * .core.runtime.IProgressMonitor)
				 */
				public void run(IProgressMonitor monitor) throws CoreException {
					removeAnnotationFile(project1);
				}
			});
			// waitForChanges();
			Annotation[] annotations = manager.getAnnotations(project1, false);
			assertEquals(0, annotations.length);
			annotations = manager.getAnnotations(project2, false);
			assertEquals(2, annotations.length);
		} finally {
			manager.removeAnnotations(project1);
			manager.removeAnnotations(project2);
		}
	}

	public void test3() throws Exception {
		// Test is INDEX will be rebuilt after annotation file modification
		IAnnotationManager manager = AnnotationPlugin.getManager();
		try {
			manager.addAnnotation(project1, createMimeType("text/html"));
			manager.addAnnotation(project2, createMimeType("text/xml"));
			manager.addAnnotation(project2, createMimeType("text/plain"));
			waitForChanges(getAnnotationFile(project1));

			replaceAnnotationFile(project1, project2);

			Annotation[] annotations = manager.getAnnotations(project1, false);
			assertEquals(2, annotations.length);
			annotations = manager.getAnnotations(project2, false);
			assertEquals(2, annotations.length);
		} finally {
			manager.removeAnnotations(project1);
			manager.removeAnnotations(project2);
		}
	}

	protected void waitForNotification(IWorkspaceRunnable runnable)
			throws CoreException {
		final boolean[] changesOccured = new boolean[] { false };
		AnnotationPlugin.getManager().addAnnotationListener(
				new AnnotationAdapter2() {
					public void annotationsAdded(Resource resource, Mode option) {
						notifyChanges();
					}

					public void annotationsRemoved(Resource resource) {
						notifyChanges();
					}

					protected void notifyChanges() {
						synchronized (changesOccured) {
							changesOccured[0] = true;
							AnnotationPlugin.getManager()
									.removeAnnotationListener(this);
						}
					}
				});
		runnable.run(new NullProgressMonitor());
		long startTime = System.currentTimeMillis();
		while (true) {
			synchronized (changesOccured) {
				if (changesOccured[0])
					break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - startTime > MAX_TEST_TIME)
				throw new IllegalStateException(
						"Some problem with test performance...");
		}
	}

	/**
	 * Wait while all modified resources will be saved. If input resource isn't
	 * null wait one second after the resource save. It's required for resource
	 * modification determination by local time stamp value because in some
	 * environments local time stamp for resources is defined in seconds.
	 * 
	 * @param resource
	 *            a resource to wait or null
	 */
	public void waitForChanges(IResource resource) {
		long startTime = System.currentTimeMillis();
		while (true) {
			if (!DeferredResourceSaver.getInstance().isDirty()) {
				if (resource != null) {
					long time = System.currentTimeMillis()
							- resource.getLocalTimeStamp();
					if (time > 1000) {
						return;
					}
				} else {
					return;
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - startTime > MAX_TEST_TIME)
				throw new IllegalStateException(
						"Some problem with test performance...");
		}
	}

	/**
	 * Remove annotations file from the project
	 * 
	 * @param project
	 * @throws CoreException
	 */
	private void removeAnnotationFile(IProject project) throws CoreException {
		IFile file = getAnnotationFile(project);
		if (file != null) {
			file.delete(true, new NullProgressMonitor());
		}
	}

	/**
	 * Replace project1 annotations file with project2 annotations file (with
	 * URI replacing)
	 * 
	 * @param project1
	 * @param project2
	 * @throws IOException
	 */
	private void replaceAnnotationFile(IProject project1, IProject project2)
			throws CoreException {
		try {
			IFile file1 = getAnnotationFile(project1);
			IFile file2 = getAnnotationFile(project2);
			if (file1 == null || file2 == null)
				return;
			String content = getContent(file2);
			content = content.replaceAll("resource:/project2",
					"resource:/project1");
			saveContent(content, file1);
		} catch (Exception e) {
			Status status = new Status(Status.ERROR,
					AnnotationPlugin.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		}
	}

	private IFile getAnnotationFile(IProject project) {
		return project.getFile(".ann");
	}

	private String getContent(IFile file) throws IOException, CoreException {
		InputStream stream = file.getContents();
		StringBuffer content = new StringBuffer();
		byte[] buffer = new byte[1024 * 1024];
		int size = 0;
		while ((size = stream.read(buffer)) >= 0) {
			content.append(new String(buffer, 0, size));
		}
		stream.close();
		return content.toString();
	}

	private void saveContent(String content, IFile file) throws CoreException {
		file.setContents(new ByteArrayInputStream(content.getBytes()), true,
				true, new NullProgressMonitor());
	}

	private MimeType createMimeType(String text) {
		MimeType type = ModelFactory.eINSTANCE.createMimeType();
		type.setMimeType(text);
		return type;
	}

}
