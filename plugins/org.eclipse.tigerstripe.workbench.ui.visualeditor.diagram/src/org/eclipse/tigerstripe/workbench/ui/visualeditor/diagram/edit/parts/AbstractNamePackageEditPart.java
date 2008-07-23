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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.IconCachingCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeExplorerLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.viewers.ITigerstripeLabelDecorator;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.DiagramPropertiesHelper;

/**
 * Commonality between all NamePackageEditPart to ensure the label is refreshed
 * when the isAbstract attribute is changed.
 * 
 * @author Eric Dillon
 * @since 2.1
 */
public abstract class AbstractNamePackageEditPart extends
		IconCachingCompartmentEditPart {

	private static List<ITigerstripeLabelDecorator> decorators = new ArrayList<ITigerstripeLabelDecorator>();

	static {
		addTigerstripeLabelDecorators();
	}

	public AbstractNamePackageEditPart(EObject arg0) {
		super(arg0);

	}

	protected boolean isAbstractArtifact() {
		Node model = (Node) getParent().getModel();
		if (model.getElement() instanceof QualifiedNamedElement)
			return ((QualifiedNamedElement) model.getElement()).isIsAbstract();
		return false;
	}

	private static void addTigerstripeLabelDecorators() {
		IConfigurationElement[] elements = Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor(
						"org.eclipse.tigerstripe.workbench.ui.base.labelDecorator");
		for (IConfigurationElement element : elements) {
			try {
				ITigerstripeLabelDecorator deco = (ITigerstripeLabelDecorator) element
						.createExecutableExtension("class");
				decorators.add(deco);
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	protected boolean hideArtifactPackages(Map map) {
		if (map != null) {
			DiagramPropertiesHelper helper = new DiagramPropertiesHelper(map);
			return Boolean
					.parseBoolean(helper
							.getPropertyValue(DiagramPropertiesHelper.HIDEARTIFACTPACKAGES));
		}
		return false;
	}

	protected String decorateText(String text) {
		String result = text;
		for (ITigerstripeLabelDecorator decorator : decorators) {
			result = decorator.decorateText(text, this.getParent());
		}
		return result;
	}
}
