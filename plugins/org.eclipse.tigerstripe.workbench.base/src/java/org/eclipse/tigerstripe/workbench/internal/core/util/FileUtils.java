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
		Manifest manifest = new Manifest();
		Attributes manifestAttr = manifest.getMainAttributes();
		manifestAttr.putValue("Manifest-Version", "1.0");
		createJar(jarPath, baseDirPath, manifest);
	}

	public final static void createJar(String jarPath, String targetFolder, Manifest manifest) throws IOException
	{
		JarOutputStream target = new JarOutputStream(new FileOutputStream(jarPath), manifest);
		add(new File(targetFolder), targetFolder, target);
		target.close();
	}

	private static void add(File source, String targetFolder, JarOutputStream target) throws IOException
	{
		BufferedInputStream in = null;
		try
		{
			if (source.isDirectory())
			{
				String name = source.getPath().replace("\\", "/").substring(targetFolder.length());
				if (!name.isEmpty())
				{
					if (!name.endsWith("/"))
						name += "/";
					if (name.startsWith("/"))
						name = name.substring(1);
					JarEntry entry = new JarEntry(name);
					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.closeEntry();
				}
				for (File nestedFile: source.listFiles())
					add(nestedFile, targetFolder, target);
				return;
			}

			JarEntry entry = new JarEntry(source.getPath().replace("\\", "/").substring(targetFolder.length() + 1));
			entry.setTime(source.lastModified());
			target.putNextEntry(entry);
			in = new BufferedInputStream(new FileInputStream(source));

			byte[] buffer = new byte[1024];
			while (true)
			{
				int count = in.read(buffer);
				if (count == -1)
					break;
				target.write(buffer, 0, count);
			}
			target.closeEntry();
		}
		finally
		{
			if (in != null)
				in.close();
		}
	}
}
