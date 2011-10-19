package org.eclipse.tigerstripe.annotation.core.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.annotation.internal.core.AnnotationFilesFilter;

public class AnnotationFilesFilterTest  extends AbstractResourceTestCase {
	
	protected IProject project;
	
	@Override
	protected void setUp() throws Exception {
		project = createProject("aProject");
	}
	
	@Override
	protected void tearDown() throws Exception {
		deleteProject(project);
	}
	
	public void testFiles() throws Exception {
		AnnotationFilesFilter filesFilter = new AnnotationFilesFilter(new String[] {"core.ann", "/ice.ann"});
		assertTrue(filesFilter.isAnnotationFile(getFile("core.ann")));
		assertTrue(filesFilter.isAnnotationFile(getFile("ice.ann")));
		assertFalse(filesFilter.isAnnotationFile(getFile("test.ann")));
		assertFalse(filesFilter.isAnnotationFile(getFile("/annotations/test.ann")));
		
		filesFilter = new AnnotationFilesFilter(new String[] {"/annotations/*.ann", "/*.ann",});
		assertTrue(filesFilter.isAnnotationFile(getFile(".ann")));
		assertTrue(filesFilter.isAnnotationFile(getFile("test.ann")));
		assertTrue(filesFilter.isAnnotationFile(getFile("annotations/.ann")));
		assertTrue(filesFilter.isAnnotationFile(getFile("annotations/test.ann")));
		assertFalse(filesFilter.isAnnotationFile(getFile(".an")));
		assertFalse(filesFilter.isAnnotationFile(getFile("test.an")));
		assertFalse(filesFilter.isAnnotationFile(getFile("annotations/.an")));
		assertFalse(filesFilter.isAnnotationFile(getFile("annotations/test.an")));
		assertFalse(filesFilter.isAnnotationFile(getFile("ann/test.ann")));
	}
	
	public void testContainers() throws Exception {
		AnnotationFilesFilter filesFilter = new AnnotationFilesFilter(
				new String[] { "core.ann", "/ice.ann" });
		assertFalse(filesFilter
				.couldContainAnnotationFiles(getFolder("/annotations")));
		assertFalse(filesFilter
				.couldContainAnnotationFiles(getFolder("/annotations/test")));

		filesFilter = new AnnotationFilesFilter(
				new String[] { "/annotations/*.ann" });
		assertTrue(filesFilter
				.couldContainAnnotationFiles(getFolder("/annotations")));
		assertFalse(filesFilter
				.couldContainAnnotationFiles(getFolder("/annotations/test")));
	}

	private IFile getFile(String path) {
		return project.getFile(new Path(path));
	}

	private IFolder getFolder(String path) {
		return project.getFolder(new Path(path));
	}
}
