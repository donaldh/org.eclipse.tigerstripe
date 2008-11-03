package org.eclipse.tigerstripe.sample;

public class Helper {

	public String capitalize(String str) {
		if (str == null || str.length() == 0) {
			return str;
		} else {
			return str.substring(0, 1).toUpperCase()
					+ str.substring(1, str.length());
		}
	}
}
