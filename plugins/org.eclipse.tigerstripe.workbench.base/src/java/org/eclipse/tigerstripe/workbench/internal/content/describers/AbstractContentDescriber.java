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

	
	private final BoyerMooreMatcher matcher;

	public AbstractContentDescriber() {
		matcher = new BoyerMooreMatcher(getStringIdentifier());
	}
	
	
	public int describe(Reader reader, IContentDescription description) throws IOException {
		try {
			return matcher.contains(reader) ? IContentDescriber.VALID : IContentDescriber.INVALID;
		} finally {
			reader.close();
		}
	}

	public int describe(InputStream inputStream, IContentDescription description)
		throws IOException {
	
		InputStreamReader reader = new InputStreamReader(inputStream);
		return describe(reader, description);
	
	}
	
	public QualifiedName[] getSupportedOptions() {
		return null;
	}
	
	protected abstract String getStringIdentifier();

}
