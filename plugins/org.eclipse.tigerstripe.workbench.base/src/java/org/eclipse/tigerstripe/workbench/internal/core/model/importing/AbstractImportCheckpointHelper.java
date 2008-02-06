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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.project.IImportCheckpoint;
import org.eclipse.tigerstripe.workbench.internal.api.project.IImportCheckpointDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.DBImportCheckpoint;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.DBImportCheckpointDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.UML2ImportCheckpoint;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.UML2ImportCheckpointDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xmi.XmiImportCheckpoint;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xmi.XmiImportCheckpointDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

public class AbstractImportCheckpointHelper {

	protected ITigerstripeProject project;

	public AbstractImportCheckpointHelper(ITigerstripeProject project) {
		this.project = project;
	}

	protected File getCheckpointFile() {
		return new File(project.getLocation().toFile().toURI().getPath()
				+ File.separator + IImportCheckpoint.CHECKPOINT_FILE);
	}

	public IImportCheckpointDetails makeImportCheckpointDetails(
			String importCheckpointType) throws TigerstripeException {
		if (XmiImportCheckpoint.TYPE.equals(importCheckpointType))
			return new XmiImportCheckpointDetails();
		else if (DBImportCheckpoint.TYPE.equals(importCheckpointType))
			return new DBImportCheckpointDetails();
		else if (UML2ImportCheckpoint.TYPE.equals(importCheckpointType))
			return new UML2ImportCheckpointDetails();
		throw new TigerstripeException("Unknown import checkpoint type: "
				+ importCheckpointType);
	}

	/**
	 * Writes the
	 * 
	 * @param checkpoint
	 * @throws TigerstripeException
	 */
	public void createCheckpoint(IImportCheckpointDetails details,
			AnnotableElement[] annotables) throws TigerstripeException {

		if (details.getType().equals(XmiImportCheckpoint.TYPE)) {
			XmiImportCheckpoint ckpt = new XmiImportCheckpoint();

			try {
				FileWriter writer = new FileWriter(getCheckpointFile());
				ckpt.setAnnotables(annotables);
				ckpt.write(details, writer);
			} catch (IOException e) {
				throw new TigerstripeException("Couldn't write checkpoint", e);
			}
		} else if (details.getType().equals(DBImportCheckpoint.TYPE)) {
			DBImportCheckpoint ckpt = new DBImportCheckpoint();

			try {
				FileWriter writer = new FileWriter(getCheckpointFile());
				ckpt.setAnnotables(annotables);
				ckpt.write(details, writer);
			} catch (IOException e) {
				throw new TigerstripeException("Couldn't write checkpoint", e);
			}
		} else if (details.getType().equals(UML2ImportCheckpoint.TYPE)) {
			UML2ImportCheckpoint ckpt = new UML2ImportCheckpoint();

			try {
				FileWriter writer = new FileWriter(getCheckpointFile());
				ckpt.setAnnotables(annotables);
				ckpt.write(details, writer);
			} catch (IOException e) {
				throw new TigerstripeException("Couldn't write checkpoint", e);
			}
		}
	}

	/**
	 * Reads the checkpoint for the target project.
	 * 
	 * if no valid checkpoint is found, an exception is thrown.
	 * 
	 * @return
	 */
	public IImportCheckpoint readCheckpoint() throws TigerstripeException {
		File file = getCheckpointFile();

		IImportCheckpoint result = null;
		try {
			FileReader reader = new FileReader(file);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(reader);

			Element root = document.getRootElement();
			String checkpointType = root
					.attributeValue(AbstractImportCheckpoint.ROOT_TYPE_TAG);
			String checkpointVersion = root
					.attributeValue(AbstractImportCheckpoint.ROOT_TYPE_TAG);

			if (XmiImportCheckpoint.TYPE.equals(checkpointType)) {
				result = new XmiImportCheckpoint();

				((XmiImportCheckpoint) result).read(document);
			} else if (DBImportCheckpoint.TYPE.equals(checkpointType)) {
				result = new DBImportCheckpoint();

				((DBImportCheckpoint) result).read(document);
			} else if (UML2ImportCheckpoint.TYPE.equals(checkpointType)) {
				result = new UML2ImportCheckpoint();

				((UML2ImportCheckpoint) result).read(document);
			} else
				throw new TigerstripeException("Unknown checkpoint type: "
						+ checkpointType);

		} catch (IOException e) {
			throw new TigerstripeException("Error while reading checkpoint: "
					+ e.getMessage(), e);
		} catch (DocumentException e) {
			throw new TigerstripeException("Error while reading checkpoint: "
					+ e.getMessage(), e);
		}

		return result;
	}
}
