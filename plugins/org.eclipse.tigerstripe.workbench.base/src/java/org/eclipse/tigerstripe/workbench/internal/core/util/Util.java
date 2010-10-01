/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;

/**
 * @author Eric Dillon
 * 
 *         A convenience class with a bunch of useful functions
 * 
 */
public class Util {

	public final static String YES = "yes";

	public final static String NO = "no";

	public final static String TRUE = "true";

	public final static String FALSE = "false";

	/**
	 * Returns a boolean value for the passed string for yes/no and true/false.
	 * (Case insensitive)
	 * 
	 * @param strBool
	 *            - the string
	 * @return boolean
	 */
	public static boolean strBoolean(String strBool) {
		String lowerCase = strBool.toLowerCase();
		return YES.equals(lowerCase) || TRUE.equals(lowerCase);
	}

	/**
	 * Returns the package prefix of the given fully qualified name
	 * 
	 * @param fullyQualifiedName
	 *            - String a fully qualified name
	 * @return String - the package prefix to the given fully qualified name
	 */
	public static String packageOf(String fullyQualifiedName) {
		if (fullyQualifiedName == null)
			return "";

		int lastIndexOfDot = fullyQualifiedName.lastIndexOf(".");
		if (lastIndexOfDot < 0) {
			return "";
		} else {
			return fullyQualifiedName.substring(0, lastIndexOfDot);
		}
	}

	/**
	 * Returns the name of the given fully qualified name
	 * 
	 * @param fullyQualifiedName
	 *            - String a fully qualified name
	 * @return String - the name to the given fully qualified name
	 */
	public static String nameOf(String fullyQualifiedName) {

		if (fullyQualifiedName == null)
			return "";

		int lastIndexOfDot = fullyQualifiedName.lastIndexOf(".");
		if (lastIndexOfDot < 0) {
			return fullyQualifiedName;
		} else {
			return fullyQualifiedName.substring(lastIndexOfDot + 1);
		}
	}

	public static String toHexString(byte[] v) {
		StringBuffer sb = new StringBuffer(v.length * 2);
		for (int i = 0; i < v.length; i++) {
			int b = v[i] & 0xFF;
			sb.append(HEX_DIGITS.charAt(b >>> 4)).append(
					HEX_DIGITS.charAt(b & 0xF));
		}
		return sb.toString();
	}

	private static final String HEX_DIGITS = "0123456789abcdef";

	public static String getRelativePath(File file, File relativeTo)
			throws IOException {
		file = new File(file + File.separator + "89243jmsjigs45u9w43545lkhj7")
				.getParentFile();
		relativeTo = new File(relativeTo + File.separator
				+ "984mvcxbsfgqoykj30487df556").getParentFile();
		File origFile = file;
		File origRelativeTo = relativeTo;
		ArrayList<File> filePathStack = new ArrayList<File>();
		ArrayList<File> relativeToPathStack = new ArrayList<File>();
		// build the path stack info to compare it afterwards
		file = file.getCanonicalFile();
		while (file != null) {
			filePathStack.add(0, file);
			file = file.getParentFile();
		}
		relativeTo = relativeTo.getCanonicalFile();
		while (relativeTo != null) {
			relativeToPathStack.add(0, relativeTo);
			relativeTo = relativeTo.getParentFile();
		}
		// compare as long it goes
		int count = 0;
		file = filePathStack.get(count);
		relativeTo = relativeToPathStack.get(count);
		while ((count < filePathStack.size() - 1)
				&& (count < relativeToPathStack.size() - 1)
				&& file.equals(relativeTo)) {
			count++;
			file = filePathStack.get(count);
			relativeTo = relativeToPathStack.get(count);
		}
		if (file.equals(relativeTo))
			count++;
		// up as far as necessary
		StringBuffer relString = new StringBuffer();
		for (int i = count; i < relativeToPathStack.size(); i++) {
			relString.append(".." + File.separator);
		}
		// now back down to the file
		for (int i = count; i < filePathStack.size() - 1; i++) {
			relString.append((filePathStack.get(i)).getName() + File.separator);
		}
		relString.append((filePathStack.get(filePathStack.size() - 1))
				.getName());
		// just to test
		File relFile = new File(origRelativeTo.getAbsolutePath()
				+ File.separator + relString.toString());
		if (!relFile.getCanonicalFile().equals(origFile.getCanonicalFile()))
			throw new IOException("Failed to find relative path.");
		return relString.toString();
	}

	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns
	// false.
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success)
					return false;
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	public static boolean isJavaScalarType(String type) {
		for (String[] scalar : IPrimitiveTypeArtifact.reservedPrimitiveTypes) {
			if (scalar[0].equals(type.trim()))
				return true;
		}
		return false;
	}

	public static StringBuffer readAndReplaceInFile(File file, String regex,
			String replace) throws IOException {
		InputStream contentStream = null;
		BufferedReader reader = null;
		try {
			contentStream = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(contentStream));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll(regex, replace);
				sb.append(line + "\n");
			}
			return sb;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// ignore
				}
			}
			if (contentStream != null) {
				try {
					contentStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * Fixes a windows-style path to ensure no "\" are used anywhere
	 * 
	 * @param path
	 * @return
	 */
	public static String fixWindowsPath(String fullPath) {
		fullPath = fullPath.indexOf('\\') == -1 ? fullPath : fullPath.replace(
				'\\', IPath.SEPARATOR);
		// extract device
		int i = fullPath.indexOf(IPath.DEVICE_SEPARATOR);
		if (i != -1) {
			// remove leading slash from device part to handle output of
			// URL.getFile()
			fullPath = fullPath.substring(i + 1, fullPath.length());
		}
		return fullPath;
	}
}