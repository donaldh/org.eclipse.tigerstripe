package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MemberAccess {
	
	public static int FIELD_TYPE = 0;
	public static int INHERETED_FIELD_TYPE = 1;
	public static int LITERAL_TYPE = 2;
	public static int INHERETED_LITERAL_TYPE = 3;
	public static int METHOD_TYPE = 4;
	public static int INHERETED_METHOD_TYPE = 5;
	
	 int type();
	 boolean filter();
}
