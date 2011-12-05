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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.tigerstripe.workbench.TigerstripeException;

/**
 * Package up resources into a zip file
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class ZipFilePackager {
	private final ZipOutputStream outputStream;

	private boolean useCompression = true;

	private final Set<String> zipped = new HashSet<String>();

	/**
	 * Create an instance of this class.
	 * 
	 * @param filename
	 *            java.lang.String
	 * @param compress
	 *            boolean
	 * @exception java.io.IOException
	 */
	public ZipFilePackager(String filename, boolean compress)
			throws IOException {
		outputStream = new ZipOutputStream(new FileOutputStream(filename));
		useCompression = compress;
	}

	/**
	 * Do all required cleanup now that we're finished with the currently-open
	 * .zip
	 * 
	 * @exception java.io.IOException
	 */
	public void finished() throws IOException {
		outputStream.close();
	}

	public void write(File[] files, String rootDir, FileFilter filter)
			throws IOException, TigerstripeException {
		String baseDir = rootDir;
		if (rootDir.length() != 0) {
			baseDir = rootDir + File.separator;
		}

		if (files == null)
			return; // ignore

		for (File file : files) {
			if (file.isFile()) {
				if (filter.accept(file))
					write(file, (baseDir + file.getName()).replace(
							File.separatorChar, '/'));
			} else {
				File[] content = file.listFiles();
				write(content, (baseDir + file.getName()).replace(
						File.separatorChar, '/'), filter);
			}
		}
	}

	public void write(File[] files, String rootDir, String cutOffPath)
			throws IOException, TigerstripeException {
		String baseDir = rootDir;
		if (rootDir.length() != 0) {
			baseDir = rootDir + File.separator;
		}

		if (files == null)
			return; // ignore

		for (File file : files) {
			String targetPath = file.getAbsolutePath().replace(cutOffPath, "");
			if (file.isFile()) {
				write(file,
						(baseDir + targetPath).replace(File.separatorChar, '/'));
			} else {
				File[] content = file.listFiles();
				write(content,
						(baseDir + targetPath).replace(File.separatorChar, '/'));
			}
		}
	}

	public void write(File[] files, String rootDir) throws IOException,
			TigerstripeException {

		String baseDir = rootDir;
		if (rootDir.length() != 0) {
			baseDir = rootDir + File.separator;
		}

		if (files == null)
			return; // ignore

		for (File file : files) {
			if (file.isFile()) {
				write(file, (baseDir + file.getName()).replace(
						File.separatorChar, '/'));
			} else {
				File[] content = file.listFiles();
				write(content, (baseDir + file.getName()).replace(
						File.separatorChar, '/'));
			}
		}
	}

	/**
	 * Write the contents of the file to the tar archive.
	 * 
	 * @param entry
	 * @param contents
	 * @exception java.io.IOException
	 */
	private void write(ZipEntry entry, File contents) throws IOException,
			TigerstripeException {
		byte[] readBuffer = new byte[4096];

		// If the contents are being compressed then we get the below for free.
		if (!useCompression) {
			entry.setMethod(ZipEntry.STORED);
			InputStream contentStream = new FileInputStream(contents);
			int length = 0;
			CRC32 checksumCalculator = new CRC32();
			try {
				int n;
				while ((n = contentStream.read(readBuffer)) > 0) {
					checksumCalculator.update(readBuffer, 0, n);
					length += n;
				}
			} finally {
				if (contentStream != null)
					contentStream.close();
			}

			entry.setSize(length);
			entry.setCrc(checksumCalculator.getValue());
		}

		outputStream.putNextEntry(entry);
		InputStream contentStream = new FileInputStream(contents);
		try {
			int n;
			while ((n = contentStream.read(readBuffer)) > 0) {
				outputStream.write(readBuffer, 0, n);
			}
		} finally {
			if (contentStream != null)
				contentStream.close();
		}
		outputStream.closeEntry();
	}

	/**
	 * Write the passed resource to the current archive.
	 * 
	 * @param resource
	 * @param destinationPath
	 *            java.lang.String
	 * @exception java.io.IOException
	 */
	public void write(File resource, String destinationPath)
			throws IOException, TigerstripeException {
		// Check if a file with the same destination path was already added
		if (!zipped.add(destinationPath))
			return;
		ZipEntry newEntry = new ZipEntry(destinationPath);
		write(newEntry, resource);
	}
}
