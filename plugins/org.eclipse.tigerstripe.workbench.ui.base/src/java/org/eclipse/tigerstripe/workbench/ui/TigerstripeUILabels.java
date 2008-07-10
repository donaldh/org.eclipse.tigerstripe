/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;

/**
 * Support for Labels for all IModelComponents including StyledLabels.
 * 
 * @author erdillon
 * @since 0.4
 */
public class TigerstripeUILabels {

	/**
	 * Colorize the labels.
	 */
	public final static long COLORIZE = 1L << 0;

	public static StyledString getStyledString(Object object, long flags) {

		if (object instanceof IModelComponent) {
			return getStyledString((IModelComponent) object, flags);
		} else if (object instanceof IAbstractTigerstripeProject) {
			return getStyledString((IAbstractTigerstripeProject) object, flags);
		} else if (object instanceof AbstractLogicalExplorerNode) {
			return getStyledString((AbstractLogicalExplorerNode) object, flags);
		}

		return new StyledString();
	}

	public static String getStringLabel(Object component, long flags) {
		return getStyledString(component, flags).getString();
	}

	protected static StyledString getStyledString(
			AbstractLogicalExplorerNode node, long flags) {
		return new StyledString(node.getText());
	}

	protected static StyledString getStyledString(
			IAbstractTigerstripeProject project, long flags) {
		if (project instanceof ITigerstripeModelProject) {
			ITigerstripeModelProject tsProject = (ITigerstripeModelProject) project;
			try {
				if (tsProject.getActiveFacet() != null
						&& tsProject.getActiveFacet().canResolve()) {
					String activeName = project.getProjectLabel() + " {"
							+ tsProject.getActiveFacet().resolve().getName()
							+ "}";
					return new StyledString(activeName,
							new LabelStyler(project));
				}
			} catch (TigerstripeException e) {
				// Upon import the tigerstripe.xml may not be
				// there yet, so all we want is to display
				// the project name. There won't be any active
				// facet at that stage anyway
			}
		}

		return new StyledString(project.getProjectLabel());
	}

	public static class LabelStyler extends StyledString.Styler {

		private Object component;

		public LabelStyler(Object component) {
			this.component = component;
		}

		@Override
		public void applyStyles(TextStyle textStyle) {
			if (textStyle instanceof StyleRange) {
				StyleRange range = (StyleRange) textStyle;
				if (component instanceof IAbstractArtifact) {
					IAbstractArtifact artifact = (IAbstractArtifact) component;
					if (artifact.isAbstract()) {
						range.fontStyle = SWT.BOLD;
					}

					try {
						if (artifact.isInActiveFacet()) {
							range.foreground = ColorUtils.BLACK;
						} else {
							range.foreground = ColorUtils.LIGHT_GREY;
						}
					} catch (TigerstripeException e) {
						// ignore
					}
				} else if (component instanceof ITigerstripeModelProject) {
					try {
						ITigerstripeModelProject tsProject = (ITigerstripeModelProject) component;
						if (tsProject.getActiveFacet() != null) {
							range.foreground = ColorUtils.TS_ORANGE;
						} else {
							range.foreground = ColorUtils.BLACK;
						}
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
	}

	protected static StyledString getStyledString(IModelComponent component,
			long flags) {

		if (component instanceof IAbstractArtifact) {
			IAbstractArtifact artifact = (IAbstractArtifact) component;
			String name = artifact.getName();
			return new StyledString(name, new LabelStyler(artifact));
		} else if (component instanceof IMethod) {
			return new StyledString(((IMethod) component).getLabelString());
		} else if (component instanceof ILiteral) {
			return new StyledString(((ILiteral) component).getLabelString());
		} else if (component instanceof IField) {
			return new StyledString(((IField) component).getLabelString());
		} else if (component instanceof IAssociationEnd) {
			return new StyledString(((IAssociationEnd) component)
					.getLabelString());
		}
		return new StyledString();
	}
}
