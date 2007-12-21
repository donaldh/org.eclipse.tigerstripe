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
package org.eclipse.tigerstripe.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;

public class ZipFileUnzipper {

	/**
	 * Unzips the given file in the given targetDir
	 * 
	 * @param zipFile
	 * @param targetDir
	 * @throws TigerstripeException
	 */
	public static void unzip(String zipFileStr, String targetDir)
			throws TigerstripeException {

		File zipFile = new File(zipFileStr);
		ZipFile zf = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			zf = new ZipFile(zipFile);
			Enumeration zipEnum = zf.entries();

			File targetDirFile = new File(targetDir);
			if (!targetDirFile.exists()) {
				targetDirFile.mkdirs();
			}

			if (targetDir.charAt(targetDir.length() - 1) != File.separatorChar)
				targetDir += File.separator;

			while (zipEnum.hasMoreElements()) {
				ZipEntry item = (ZipEntry) zipEnum.nextElement();

				if (item.isDirectory()) // Directory
				{
					File newdir = new File(targetDir + item.getName());
					newdir.mkdirs();
				} else // File
				{
					String newfile = targetDir + item.getName();
					File file = new File(newfile);
					File dir = new File(file.getParent());
					if (!dir.exists()) {
						dir.mkdirs();
					}

					try {
						is = zf.getInputStream(item);
						fos = new FileOutputStream(newfile);

						int ch;

						while ((ch = is.read()) != -1) {
							fos.write(ch);
						}

					} catch (Exception e) {
						// ignore
					} finally {
						if (is != null) {
							try {
								is.close();

							} catch (IOException e) {
								// ignore
							}
						}
						if (fos != null) {
							try {
								fos.close();

							} catch (IOException e) {
								// ignore
							}
						}
					}
				}
			}

		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			throw new TigerstripeException("Error while unzipping '"
					+ zipFile.getAbsolutePath() + "': " + e.getMessage(), e);
		} finally {
			if (zf != null) {
				try {
					zf.close();

				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

}
