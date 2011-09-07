package org.eclipse.tigerstripe.workbench.utils;

/**
 * Utility methods for manipulating bit masks
 */
public class BitMask {
	/** 
	 * Returns true if all of the bits indicated by the mask are set.
	 */
	public static boolean isSet(int flags, int mask) {
		return (flags & mask) == mask;
	}
}
