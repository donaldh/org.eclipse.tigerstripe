package org.eclipse.tigerstripe.sidmapping;

import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model.ICommentProcessor;


/**
 * This class is just intended to remove a few spurious comment characters that are found in the SID export files.
 * It is totally non-generic, and is not intended for any re-use whatsoever.
 * The debugging option requires a code change, and it is only meant for development time use.
 * 
 * @author rcraddoc
 *
 */
public class CommentProcessor implements ICommentProcessor {

	public String processString(String string)
	{
		
		boolean printdebug = false;
		

		if (printdebug) {
			if (string.matches("[<].*[>]")) {
				System.out.println("Found HTML tags " + string);
			}

			System.out.println(string);
			for (char c : string.toCharArray()) {
				System.out.println("CHAR : " + c + " " + Integer.toHexString(c));
			}
		}
		
		char open = 8220;
		char j = 8221;
		String[] patterns = { "&#xD;", "&#28;", "&#29;", Character.toString(open), Character.toString(j) };
		
		for (String pattern : patterns) {
			if (printdebug) {
				System.out.println("Pattern : " + pattern);
			}

			if (string.contains(pattern)) {
				if (printdebug)
					System.out.println("Found " + pattern + " " + string);
				string = string.replaceAll(pattern, " ");
			}
		}
		System.out.println("Return " + string);
		return string;
	}
}