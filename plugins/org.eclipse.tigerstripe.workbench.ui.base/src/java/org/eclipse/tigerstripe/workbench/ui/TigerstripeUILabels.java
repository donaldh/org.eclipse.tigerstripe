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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.preferences.ExplorerPreferencePage;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.RelationshipAnchor;
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
		} else if (object instanceof IRelationshipEnd) {
			return getStyledString((IRelationshipEnd) object, flags);
		} else if (object instanceof RelationshipAnchor) {
			return getStyledString((RelationshipAnchor) object, flags);
		} else if (object instanceof IAbstractTigerstripeProject) {
			return getStyledString((IAbstractTigerstripeProject) object, flags);
		} else if (object instanceof AbstractLogicalExplorerNode) {
			return getStyledString((AbstractLogicalExplorerNode) object, flags);
		}

		return new StyledString();
	}

	private static StyledString getStyledString(final RelationshipAnchor anchor, long flags) {
		return new StyledString(anchor.getLabel(), new StyledString.Styler() {
			@Override
			public void applyStyles(TextStyle textStyle) {
				if (anchor.isInherited()) {
					textStyle.foreground = Display.getCurrent().getSystemColor(
							SWT.COLOR_DARK_GRAY);
				}
			}
		});
	}
	
	private static StyledString getStyledString(IRelationshipEnd end, long flags) {
		return new StyledString(end.getType().getName());
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
					String activeName = project.getName();
					
					if ((tsProject!=null) && (tsProject.getActiveFacet()!=null)) 
						activeName = activeName + " {" + tsProject.getActiveFacet().resolve().getName() + "}";
					
					return new StyledString(activeName,	new LabelStyler(project));
				}
			} catch (TigerstripeException e) {
				// Upon import the tigerstripe.xml may not be
				// there yet, so all we want is to display
				// the project name. There won't be any active
				// facet at that stage anyway
			}
		}

		return new StyledString(project.getName());
	}

	public static class LabelStyler extends StyledString.Styler {

		protected Object component;

		public LabelStyler(Object component) {
			this.component = component;
		}

		@Override
		public void applyStyles(TextStyle textStyle) {
			if (textStyle instanceof StyleRange) {
				StyleRange range = (StyleRange) textStyle;

				range.foreground = ColorUtils.BLACK;

				if (component instanceof IAbstractArtifact) {
					IAbstractArtifact artifact = (IAbstractArtifact) component;
					if (artifact.isAbstract()) {
						range.fontStyle = SWT.ITALIC;
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

	public static class StereotypeLabelStyler extends LabelStyler {

		public StereotypeLabelStyler(Object component) {
			super(component);
		}

		@Override
		public void applyStyles(TextStyle textStyle) {
			super.applyStyles(textStyle);

			if (textStyle instanceof StyleRange) {
				StyleRange range = (StyleRange) textStyle;
				if (range.foreground == ColorUtils.BLACK) {
					range.foreground = ColorUtils.DARK_GREY;
				}
			}
		}
	}

	protected static StyledString getStyledString(IModelComponent component,
			long flags) {

		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();

		StyledString result = new StyledString("");

		String label = component.getName();
		String stereotypeString = component.getStereotypeString();
		StereotypeLabelStyler stereoStyler = new StereotypeLabelStyler(
				component);
		StyledString stereoStyledString = new StyledString(stereotypeString,
				stereoStyler);

		String stereoPrefsLabel = null;

		if (component instanceof IAbstractArtifact) {
			stereoPrefsLabel = ExplorerPreferencePage.P_LABEL_STEREO_ARTIFACT;
		} else if (component instanceof IMethod) {
			boolean stereoArgs = store
					.getBoolean(ExplorerPreferencePage.P_LABEL_STEREO_METHARGS);
			label = ((IMethod) component).getLabelString(stereoArgs);
			stereoPrefsLabel = ExplorerPreferencePage.P_LABEL_STEREO_METH;
		} else if (component instanceof ILiteral) {
			label = ((ILiteral) component).getLabelString();
			stereoPrefsLabel = ExplorerPreferencePage.P_LABEL_STEREO_LIT;
		} else if (component instanceof IField) {
			label = ((IField) component).getLabelString();
			stereoPrefsLabel = ExplorerPreferencePage.P_LABEL_STEREO_ATTR;
		} else if (component instanceof IAssociationEnd) {
			label = ((IAssociationEnd) component).getLabelString();
			stereoPrefsLabel = ExplorerPreferencePage.P_LABEL_STEREO_END;
		}

		if (store.getBoolean(stereoPrefsLabel)) {
			result.append(stereoStyledString);
		}

		result.append(new StyledString(label, new LabelStyler(component)));
		return result;
	}
}
