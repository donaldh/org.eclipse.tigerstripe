/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial API and Implementation
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.eclipse.tigerstripe.workbench.internal.contract.ContractUtils;

public class FileUtils {

	private final static int BUF = 4096;

	/**
	 * Utility method to copy files. This doesn't have any dependency on
	 * Eclipse, simply uses Java.
	 * 
	 * @param fromFileName
	 * @param toFileName
	 * @param force
	 * @throws IOException
	 */
	public static void copy(String fromFileName, String toFileName,
			boolean force) throws IOException {
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);

		if (!fromFile.exists() || !fromFile.canRead() || !fromFile.isFile())
			throw new IOException("Can't open file: " + fromFileName);

		if (toFile.isDirectory())
			toFile = new File(toFile, fromFile.getName());

		if (toFile.exists()) {
			if (!toFile.canWrite())
				throw new IOException("Can't write to " + toFileName);
			if (!force)
				throw new IOException("Destination already exists.");
		} else {
			String parent = toFile.getParent();
			File dir = new File(parent);
			if (!dir.exists() || dir.isFile() || !dir.canWrite())
				throw new IOException("Can't write to destionation: " + parent);
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[BUF];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead); // write
		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					// Ignore
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
					// Ignore
				}
		}
	}

	public final static void copyDir(String srcDirPath, String toDirPath,
			boolean force) throws IOException {
		File srcDir = new File(srcDirPath);
		File toDir = new File(toDirPath);

		if (!srcDir.exists())
			throw new IOException("SrcDir " + srcDirPath + " cannot be found.");
		if (!toDir.exists() || !toDir.isDirectory() || !toDir.canWrite()) {
			throw new IOException("toDir " + toDirPath
					+ " must be a valid writteable directory.");
		}

		for (File srcFile : srcDir.listFiles()) {
			if (srcFile.exists() && srcFile.canRead())
				if (srcFile.isFile())
					copy(srcFile.getAbsolutePath(), toDir.getAbsolutePath(),
							force);
				else {
					String name = srcFile.getName();
					String target = toDirPath + File.separator + name;
					File targetDir = new File(target);
					targetDir.mkdir();
					copyDir(srcFile.getAbsolutePath(), targetDir
							.getAbsolutePath(), force);
				}
			else
				throw new IOException("Cannot read "
						+ srcFile.getAbsolutePath());
		}
	}

	public final static void copyFileset(String srcDirPath, String toDirPath,
			String includes, boolean force) throws IOException {
		boolean recursive = includes.indexOf("**/") != -1;
		String localFilter = null;
		if (recursive) {
			if (includes.indexOf("**/") == 0) {
				localFilter = ContractUtils.mapFromUserPattern(includes
						.substring(3));
			} else {
				throw new IOException(
						"Fileset syntax not supported. \"**/\" must be at the beginning of the 'includes')");
			}
		} else {
			localFilter = ContractUtils.mapFromUserPattern(includes);
		}

		final String regExp = localFilter;
		FileFilter filter = new FileFilter() {

			public boolean accept(File pathname) {
				String name = pathname.getName();
				return name.matches(regExp);
			}
		};
		File srcDir = new File(srcDirPath);
		File toDir = new File(toDirPath);

		if (!srcDir.exists() || !srcDir.isDirectory() || !srcDir.canRead()) {
			throw new IOException("Cannot read srcDir: " + srcDirPath);
		}

		if (!toDir.exists() || !toDir.canWrite() || !toDir.isDirectory()) {
			throw new IOException("Cannot write to toDir: " + toDirPath);
		}

		if (recursive)
			copyRecursive(srcDir, toDir, filter, force);
		else {
			for (File srcFile : srcDir.listFiles()) {
				if (srcFile.isFile() && filter.accept(srcFile)) {
					copy(srcFile.getAbsolutePath(), toDir.getAbsolutePath(),
							force);
				}
			}
		}
	}

	private static void copyRecursive(File srcDir, File destDir,
			FileFilter filter, boolean force) throws IOException {
		for (File srcFile : srcDir.listFiles()) {
			if (srcFile.isFile() && filter.accept(srcFile)) {
				copy(srcFile.getAbsolutePath(), destDir.getAbsolutePath(),
						force);
			} else if (srcFile.isDirectory()) {
				String dirName = srcFile.getName();
				String targetPath = destDir.getAbsoluteFile() + File.separator
						+ dirName;
				File targetDir = new File(targetPath);
				targetDir.mkdir();
				copyRecursive(srcFile, targetDir, filter, force);
			}
		}
	}

	public final static void createJar(String jarPath, String baseDirPath)
			throws IOException {
		if (!baseDirPath.endsWith(File.separator)) {
			baseDirPath = baseDirPath + File.separator;
		}

		FileOutputStream fos = new FileOutputStream(jarPath);
		Manifest manifest = new Manifest();
		Attributes manifestAttr = manifest.getMainAttributes();
		manifestAttr.putValue("Manifest-Version", "1.0");
		JarOutputStream jos = new JarOutputStream(fos, manifest);

		walkDirectory(baseDirPath, baseDirPath, jos);

		jos.flush();
		jos.close();
		fos.close();
	}

	private final static void walkDirectory(String dirPath, String baseDirPath,
			JarOutputStream jos) throws IOException {
		File dirobject = new File(dirPath);
		if (dirobject.exists() == true) {
			if (dirobject.isDirectory() == true) {
				File[] fileList = dirobject.listFiles();
				// Loop through the files
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].isDirectory()) {
						walkDirectory(fileList[i].getPath(), baseDirPath, jos);
					} else if (fileList[i].isFile()) {
						// Call the zipFunc function
						jarFile(fileList[i].getPath(), baseDirPath, jos);
					}
				}
			}
		}
	}

	private final static void jarFile(String filePath, String baseDirPath,
			JarOutputStream jos) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		BufferedInputStream bis = new BufferedInputStream(fis);
		JarEntry fileEntry = new JarEntry(filePath.substring(
				baseDirPath.length()).replace(File.separatorChar, '/'));
		jos.putNextEntry(fileEntry);
		byte[] data = new byte[1024];
		int byteCount;
		while ((byteCount = bis.read(data, 0, 1024)) > -1) {
			jos.write(data, 0, byteCount);
		}

	}
}
