package org.eclipse.tigerstripe.workbench.headless.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;

public class HeadlessGenerationTest extends TestCase  {

	public void testOutput() throws Exception {
		IFile schema = TestApplication.model
				.getFile("target/tigerstripe.gen/xml/tigerstripeExportSchema.xsd");
		assertTrue(schema.exists());

		Resources resources = new Resources(Activator.getDefault());
		
		String actual = toString(schema.getContents());
		String expected = toString(new FileInputStream(resources.get("tigerstripeExportSchema.xsd")));
		
		assertEquals(expected, actual);
	}
	
	private String toString(InputStream is) throws IOException {
		try {
			return new Scanner(is).useDelimiter("\\A").next();
		} finally {
			is.close();
		}
	}
	
}
