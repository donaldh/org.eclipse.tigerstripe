/*******************************************************************************
 * Copyright (c) 2010 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.thirdparty.tmf.sid;

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
		if (printdebug)
			System.out.println("Return " + string);
		return string;
	}
}