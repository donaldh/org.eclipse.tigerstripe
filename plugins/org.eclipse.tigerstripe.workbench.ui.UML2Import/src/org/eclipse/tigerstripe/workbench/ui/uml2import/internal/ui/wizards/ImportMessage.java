package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;

public class ImportMessage extends Message {
	
	public final static int ARTIFACT = 4;
	public final static int WARNING = 5;
	public final static int INFO = 6;


	private final static String[] messageTags = { "ERROR", "WARNING", "INFO",
			"UNKNOWN", "ARTIFACT" };
	
	
	public static String severityToString(int severity) {
		return messageTags[severity];
	}
	
}
