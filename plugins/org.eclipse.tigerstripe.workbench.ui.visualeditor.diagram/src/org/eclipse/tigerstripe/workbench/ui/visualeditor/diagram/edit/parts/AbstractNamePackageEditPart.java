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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.IconCachingCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.internal.viewers.TigerstripeDecoratorManager;
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
		IconCachingCompartmentEditPart implements
		TigerstripeEditableEntityEditPart {

	public AbstractNamePackageEditPart(EObject arg0) {
		super(arg0);

	}

	protected boolean isReadonlyArtifact() {
		Node model = (Node) getParent().getModel();
		if (model.getElement() instanceof QualifiedNamedElement)
			return ((QualifiedNamedElement) model.getElement()).isIsReadonly();
		return false;
	}

	protected boolean isAbstractArtifact() {
		Node model = (Node) getParent().getModel();
		if (model.getElement() instanceof QualifiedNamedElement)
			return ((QualifiedNamedElement) model.getElement()).isIsAbstract();
		return false;
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
		return TigerstripeDecoratorManager.getDefault().decorateText(
				text,
				(IModelComponent) this.getParent().getAdapter(
						IModelComponent.class));
	}

	protected String getLabelText() {
		String text = null;
		if (getParser() != null) {
			text = getParser().getPrintString(
					new EObjectAdapter(getParserElement()),
					getParserOptions().intValue());
		}

		if (text == null || text.length() == 0) {
			text = getDefaultText();
		} else {
			QualifiedNamedElement qualNamedElem = (QualifiedNamedElement) ((NodeImpl) this
					.getModel()).getElement();
			Map map = (Map) qualNamedElem.eContainer();
			if (map != null) {
				String packageName = qualNamedElem.getPackage();
				String elemPackageName = null;
				if (packageName == null) {
					elemPackageName = map.getBasePackage();
				} else {
					elemPackageName = packageName;
				}

				if (hideArtifactPackages(map)
						|| elemPackageName.equals(map.getBasePackage())) {
					// since the packages match, truncate to just show the name
					// or the diagram is set to hide packages anyway..
					int lastDotPos = text.lastIndexOf(".");
					if (lastDotPos > 0 && lastDotPos < (text.length() - 1)) {
						String newText = text.substring(lastDotPos + 1);
						text = newText;
					}
				}
			}
		}
		return decorateText(text);
	}

	public abstract IParser getParser();

	protected abstract EObject getParserElement();

	public abstract ParserOptions getParserOptions();

	protected abstract String getDefaultText();

}
