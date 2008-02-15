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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;

public class AbstractArtifactRefresher extends QualifiedNamedElementRefresher
		implements IEObjectRefresher {

	public AbstractArtifactRefresher(TransactionalEditingDomain editingDomain) {
		super(editingDomain);
	}

	@Override
	protected CompoundCommand createRefreshCommand(
			QualifiedNamedElement element, IAbstractArtifact iArtifact) {
		CompoundCommand cCommand = super.createRefreshCommand(element,
				iArtifact);

		if (element instanceof AbstractArtifact) {
			// Extends
			ExtendsUpdateCommand extendsCmd = new ExtendsUpdateCommand(
					(AbstractArtifact) element, iArtifact);
			extendsCmd.setInitialRefresh(isInitialRefresh());
			cCommand.append(extendsCmd);

			// Implements
			ImplementsUpdateCommand implementsCmd = new ImplementsUpdateCommand(
					(AbstractArtifact) element, iArtifact);
			implementsCmd.setInitialRefresh(isInitialRefresh());
			cCommand.append(implementsCmd);

			// Attrs
			AttributeUpdateCommand attrCmd = new AttributeUpdateCommand(
					(AbstractArtifact) element, iArtifact);
			attrCmd.setInitialRefresh(isInitialRefresh());
			cCommand.append(attrCmd);

			// Methods
			MethodUpdateCommand methCmd = new MethodUpdateCommand(
					(AbstractArtifact) element, iArtifact);
			methCmd.setInitialRefresh(isInitialRefresh());
			cCommand.append(methCmd);
		}

		if (element instanceof Association) {
			AssociationEndUpdateCommand endCmd = new AssociationEndUpdateCommand(
					(Association) element, (IAssociationArtifact) iArtifact);
			endCmd.setInitialRefresh(isInitialRefresh());
			cCommand.append(endCmd);
		} else if (element instanceof Dependency) {
			DependencyEndUpdateCommand endCmd = new DependencyEndUpdateCommand(
					(Dependency) element, (IDependencyArtifact) iArtifact);
			endCmd.setInitialRefresh(isInitialRefresh());
			cCommand.append(endCmd);
		}
		return cCommand;
	}

	public Command refresh(QualifiedNamedElement element,
			IAbstractArtifact iArtifact) throws DiagramRefreshException {
		return createRefreshCommand(element, iArtifact);
	}

}
