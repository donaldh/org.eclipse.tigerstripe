package org.eclipse.tigerstripe.workbench.patterns;

import java.util.Map;

public interface IPatternFactory {

	public Map<String,IPattern> getRegisteredPatterns();
	
	public IPattern getPattern(String patternName);
	
}
