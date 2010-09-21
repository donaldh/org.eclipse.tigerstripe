package org.eclipse.tigerstripe.workbench.internal.content.describers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;

/**
 * This is the base content describer that all others derive from.  At a minimum, the 
 * children should provide the String identifier that matches the content describer with a 
 * Tigerstripe artifact.
 * 
 * @author Navid Mehregani
 *
 */
public abstract class AbstractContentDescriber implements ITextContentDescriber {

	public int describe(Reader reader, IContentDescription description) throws IOException {

		try {
			StringBuffer content = new StringBuffer();
			char[] buffer = new char[1024 * 1024];
			int size = 0;
			if ((size = reader.read(buffer)) >= 0) {
				content.append(new String(buffer, 0, size));
				if (content.indexOf(getStringIdentifier()) != -1)
					return IContentDescriber.VALID;
					
			}
		} catch (Exception e) {
			// Ignore exception, just return an invalid content type
		} finally { 
			reader.close();
		}
		
		return IContentDescriber.INVALID;
	}

	public int describe(InputStream inputStream, IContentDescription description)
		throws IOException {
	
		InputStreamReader reader = new InputStreamReader(inputStream);
		return describe(reader, description);
	
	}
	
	public QualifiedName[] getSupportedOptions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected abstract String getStringIdentifier();

}
