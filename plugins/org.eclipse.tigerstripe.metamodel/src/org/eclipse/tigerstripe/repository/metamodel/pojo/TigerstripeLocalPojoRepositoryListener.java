/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.metamodel.pojo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;
import org.eclipse.tigerstripe.repository.metamodel.pojo.command.RenamePojoCommand;
import org.eclipse.tigerstripe.repository.metamodel.pojo.command.SaveToPojoCommand;

public class TigerstripeLocalPojoRepositoryListener extends
		ResourceSetListenerImpl implements ResourceSetListener {

	private MultiFileArtifactRepository repository;

	public TigerstripeLocalPojoRepositoryListener() {
		this(null);
	}

	public TigerstripeLocalPojoRepositoryListener(
			MultiFileArtifactRepository repository) {
		this.repository = repository;
	}

	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event)
			throws RollbackException {
		@SuppressWarnings("unchecked")
		List<Notification> notifications = event.getNotifications();
		Set<IAbstractArtifact> processedArtifact = new HashSet<IAbstractArtifact>();

		CompoundCommand cCommand = new CompoundCommand("TX Commit");

		for (Notification notification : notifications) {
			// TODO: figure out what the notification is all about
			Object notifier = notification.getNotifier();
			Object feature = notification.getFeature();
			if (notifier instanceof IAbstractArtifact) {
				IAbstractArtifact artifact = (IAbstractArtifact) notifier;
				// an arg or a ref on an abstract artifact was changed
				if (feature == MetamodelPackage.eINSTANCE
						.getIModelComponent_Name()
						|| feature == MetamodelPackage.eINSTANCE
								.getIQualifiedNamedComponent_Package()) {
					// the name has just changed, so it's a rename

					if (!processedArtifact.contains(artifact)) {
						RenamePojoCommand rename = new RenamePojoCommand(
								artifact, repository);
						cCommand.append(rename);
						processedArtifact.add(artifact);
					}
				} else {
					if (!processedArtifact.contains(artifact)) {
						SaveToPojoCommand save = new SaveToPojoCommand(
								artifact, repository);
						cCommand.append(save);
						processedArtifact.add(artifact);
					}
				}
			}
		}

		return cCommand;
	}

}
