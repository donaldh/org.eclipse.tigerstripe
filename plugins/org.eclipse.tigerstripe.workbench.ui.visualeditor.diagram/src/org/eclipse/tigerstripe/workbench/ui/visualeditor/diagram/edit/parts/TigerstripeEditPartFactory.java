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

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeVisualIDRegistry;

/**
 * @generated
 */
public class TigerstripeEditPartFactory implements EditPartFactory {

	/**
	 * @generated
	 */
	public static final String EXTERNAL_NODE_LABELS_LAYER = "External Node Labels"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof View) {
			View view = (View) model;
			int viewVisualID = TigerstripeVisualIDRegistry.getVisualID(view);
			switch (viewVisualID) {
			case NamedQueryArtifactEditPart.VISUAL_ID:
				return new NamedQueryArtifactEditPart(view);
			case NamedQueryArtifactStereotypesEditPart.VISUAL_ID:
				return new NamedQueryArtifactStereotypesEditPart(view);
			case NamedQueryArtifactNamePackageEditPart.VISUAL_ID:
				return new NamedQueryArtifactNamePackageEditPart(view);
			case ExceptionArtifactEditPart.VISUAL_ID:
				return new ExceptionArtifactEditPart(view);
			case ExceptionArtifactStereotypesEditPart.VISUAL_ID:
				return new ExceptionArtifactStereotypesEditPart(view);
			case ExceptionArtifactNamePackageEditPart.VISUAL_ID:
				return new ExceptionArtifactNamePackageEditPart(view);
			case ManagedEntityArtifactEditPart.VISUAL_ID:
				return new ManagedEntityArtifactEditPart(view);
			case ManagedEntityArtifactStereotypesEditPart.VISUAL_ID:
				return new ManagedEntityArtifactStereotypesEditPart(view);
			case ManagedEntityArtifactNamePackageEditPart.VISUAL_ID:
				return new ManagedEntityArtifactNamePackageEditPart(view);
			case NotificationArtifactEditPart.VISUAL_ID:
				return new NotificationArtifactEditPart(view);
			case NotificationArtifactStereotypesEditPart.VISUAL_ID:
				return new NotificationArtifactStereotypesEditPart(view);
			case NotificationArtifactNamePackageEditPart.VISUAL_ID:
				return new NotificationArtifactNamePackageEditPart(view);
			case DatatypeArtifactEditPart.VISUAL_ID:
				return new DatatypeArtifactEditPart(view);
			case DatatypeArtifactStereotypesEditPart.VISUAL_ID:
				return new DatatypeArtifactStereotypesEditPart(view);
			case DatatypeArtifactNamePackageEditPart.VISUAL_ID:
				return new DatatypeArtifactNamePackageEditPart(view);
			case EnumerationEditPart.VISUAL_ID:
				return new EnumerationEditPart(view);
			case EnumerationStereotypesEditPart.VISUAL_ID:
				return new EnumerationStereotypesEditPart(view);
			case EnumerationNamePackageEditPart.VISUAL_ID:
				return new EnumerationNamePackageEditPart(view);
			case EnumerationBaseTypeEditPart.VISUAL_ID:
				return new EnumerationBaseTypeEditPart(view);
			case UpdateProcedureArtifactEditPart.VISUAL_ID:
				return new UpdateProcedureArtifactEditPart(view);
			case UpdateProcedureArtifactStereotypesEditPart.VISUAL_ID:
				return new UpdateProcedureArtifactStereotypesEditPart(view);
			case UpdateProcedureArtifactNamePackageEditPart.VISUAL_ID:
				return new UpdateProcedureArtifactNamePackageEditPart(view);
			case SessionFacadeArtifactEditPart.VISUAL_ID:
				return new SessionFacadeArtifactEditPart(view);
			case SessionFacadeArtifactStereotypesEditPart.VISUAL_ID:
				return new SessionFacadeArtifactStereotypesEditPart(view);
			case SessionFacadeArtifactNamePackageEditPart.VISUAL_ID:
				return new SessionFacadeArtifactNamePackageEditPart(view);
			case AssociationClassClassEditPart.VISUAL_ID:
				return new AssociationClassClassEditPart(view);
			case AssociationClassClassStereotypesEditPart.VISUAL_ID:
				return new AssociationClassClassStereotypesEditPart(view);
			case AssociationClassClassNamePackageEditPart.VISUAL_ID:
				return new AssociationClassClassNamePackageEditPart(view);
			case AttributeEditPart.VISUAL_ID:
				return new AttributeEditPart(view);
			case Attribute2EditPart.VISUAL_ID:
				return new Attribute2EditPart(view);
			case Attribute3EditPart.VISUAL_ID:
				return new Attribute3EditPart(view);
			case MethodEditPart.VISUAL_ID:
				return new MethodEditPart(view);
			case Attribute4EditPart.VISUAL_ID:
				return new Attribute4EditPart(view);
			case Attribute5EditPart.VISUAL_ID:
				return new Attribute5EditPart(view);
			case Method2EditPart.VISUAL_ID:
				return new Method2EditPart(view);
			case LiteralEditPart.VISUAL_ID:
				return new LiteralEditPart(view);
			case Attribute6EditPart.VISUAL_ID:
				return new Attribute6EditPart(view);
			case Method3EditPart.VISUAL_ID:
				return new Method3EditPart(view);
			case Attribute7EditPart.VISUAL_ID:
				return new Attribute7EditPart(view);
			case Method4EditPart.VISUAL_ID:
				return new Method4EditPart(view);
			case NamedQueryArtifactAttributeCompartmentEditPart.VISUAL_ID:
				return new NamedQueryArtifactAttributeCompartmentEditPart(view);
			case ExceptionArtifactAttributeCompartmentEditPart.VISUAL_ID:
				return new ExceptionArtifactAttributeCompartmentEditPart(view);
			case ManagedEntityArtifactAttributeCompartmentEditPart.VISUAL_ID:
				return new ManagedEntityArtifactAttributeCompartmentEditPart(
						view);
			case ManagedEntityArtifactMethodCompartmentEditPart.VISUAL_ID:
				return new ManagedEntityArtifactMethodCompartmentEditPart(view);
			case NotificationArtifactAttributeCompartmentEditPart.VISUAL_ID:
				return new NotificationArtifactAttributeCompartmentEditPart(
						view);
			case DatatypeArtifactAttributeCompartmentEditPart.VISUAL_ID:
				return new DatatypeArtifactAttributeCompartmentEditPart(view);
			case DatatypeArtifactMethodCompartmentEditPart.VISUAL_ID:
				return new DatatypeArtifactMethodCompartmentEditPart(view);
			case EnumerationLiteralCompartmentEditPart.VISUAL_ID:
				return new EnumerationLiteralCompartmentEditPart(view);
			case UpdateProcedureArtifactAttributeCompartmentEditPart.VISUAL_ID:
				return new UpdateProcedureArtifactAttributeCompartmentEditPart(
						view);
			case SessionFacadeArtifactMethodCompartmentEditPart.VISUAL_ID:
				return new SessionFacadeArtifactMethodCompartmentEditPart(view);
			case AssociationClassClassAttributeCompartmentEditPart.VISUAL_ID:
				return new AssociationClassClassAttributeCompartmentEditPart(
						view);
			case AssociationClassClassMethodCompartmentEditPart.VISUAL_ID:
				return new AssociationClassClassMethodCompartmentEditPart(view);
			case MapEditPart.VISUAL_ID:
				return new MapEditPart(view);
			case AssociationEditPart.VISUAL_ID:
				return new AssociationEditPart(view);
			case AssociationStereotypesEditPart.VISUAL_ID:
				return new AssociationStereotypesEditPart(view);
			case AssociationNamePackageEditPart.VISUAL_ID:
				return new AssociationNamePackageEditPart(view);
			case AssociationAEndMultiplicityEditPart.VISUAL_ID:
				return new AssociationAEndMultiplicityEditPart(view);
			case AssociationZEndMultiplicityEditPart.VISUAL_ID:
				return new AssociationZEndMultiplicityEditPart(view);
			case AssociationAEndNameEditPart.VISUAL_ID:
				return new AssociationAEndNameEditPart(view);
			case AssociationZEndNameEditPart.VISUAL_ID:
				return new AssociationZEndNameEditPart(view);
			case SessionFacadeArtifactEmittedNotificationsEditPart.VISUAL_ID:
				return new SessionFacadeArtifactEmittedNotificationsEditPart(
						view);
			case SessionEmitsEditPart.VISUAL_ID:
				return new SessionEmitsEditPart(view);
			case SessionFacadeArtifactManagedEntitiesEditPart.VISUAL_ID:
				return new SessionFacadeArtifactManagedEntitiesEditPart(view);
			case SessionManagesEditPart.VISUAL_ID:
				return new SessionManagesEditPart(view);
			case NamedQueryArtifactReturnedTypeEditPart.VISUAL_ID:
				return new NamedQueryArtifactReturnedTypeEditPart(view);
			case QueryReturnsEditPart.VISUAL_ID:
				return new QueryReturnsEditPart(view);
			case SessionFacadeArtifactNamedQueriesEditPart.VISUAL_ID:
				return new SessionFacadeArtifactNamedQueriesEditPart(view);
			case SessionSupportsEditPart.VISUAL_ID:
				return new SessionSupportsEditPart(view);
			case SessionFacadeArtifactExposedProceduresEditPart.VISUAL_ID:
				return new SessionFacadeArtifactExposedProceduresEditPart(view);
			case SessionExposesEditPart.VISUAL_ID:
				return new SessionExposesEditPart(view);
			case AbstractArtifactExtendsEditPart.VISUAL_ID:
				return new AbstractArtifactExtendsEditPart(view);
			case DependencyEditPart.VISUAL_ID:
				return new DependencyEditPart(view);
			case DependencyNamePackageEditPart.VISUAL_ID:
				return new DependencyNamePackageEditPart(view);
			case DependencyStereotypesEditPart.VISUAL_ID:
				return new DependencyStereotypesEditPart(view);
			case ReferenceEditPart.VISUAL_ID:
				return new ReferenceEditPart(view);
			case ReferenceNameEditPart.VISUAL_ID:
				return new ReferenceNameEditPart(view);
			case ReferenceMultiplicityEditPart.VISUAL_ID:
				return new ReferenceMultiplicityEditPart(view);
			case AssociationClassEditPart.VISUAL_ID:
				return new AssociationClassEditPart(view);
			case AssociationClassAEndMultiplicityEditPart.VISUAL_ID:
				return new AssociationClassAEndMultiplicityEditPart(view);
			case AssociationClassZEndMultiplicityEditPart.VISUAL_ID:
				return new AssociationClassZEndMultiplicityEditPart(view);
			case AssociationClassAEndNameEditPart.VISUAL_ID:
				return new AssociationClassAEndNameEditPart(view);
			case AssociationClassZEndNameEditPart.VISUAL_ID:
				return new AssociationClassZEndNameEditPart(view);
			case AssociationClassAssociatedClassEditPart.VISUAL_ID:
				return new AssociationClassAssociatedClassEditPart(view);
			case AbstractArtifactImplementsEditPart.VISUAL_ID:
				return new AbstractArtifactImplementsEditPart(view);
			}
		}
		return createUnrecognizedEditPart(context, model);
	}

	/**
	 * @generated
	 */
	private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
		// Handle creation of unrecognized child node EditParts here
		return null;
	}

	/**
	 * @generated
	 */
	public static CellEditorLocator getTextCellEditorLocator(
			ITextAwareEditPart source) {
		if (source.getFigure() instanceof WrapLabel)
			return new TextCellEditorLocator((WrapLabel) source.getFigure());
		else {
			IFigure figure = source.getFigure();
			return new LabelCellEditorLocator((Label) figure);
		}
	}

	/**
	 * @generated
	 */
	static private class TextCellEditorLocator implements CellEditorLocator {

		/**
		 * @generated
		 */
		private WrapLabel wrapLabel;

		/**
		 * @generated
		 */
		public TextCellEditorLocator(WrapLabel wrapLabel) {
			super();
			this.wrapLabel = wrapLabel;
		}

		/**
		 * @generated
		 */
		public WrapLabel getWrapLabel() {
			return wrapLabel;
		}

		/**
		 * @generated
		 */
		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getWrapLabel().getTextBounds().getCopy();
			getWrapLabel().translateToAbsolute(rect);

			if (getWrapLabel().isTextWrapped()
					&& getWrapLabel().getText().length() > 0)
				rect.setSize(new Dimension(text.computeSize(rect.width,
						SWT.DEFAULT)));
			else {
				int avr = FigureUtilities.getFontMetrics(text.getFont())
						.getAverageCharWidth();
				rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT,
						SWT.DEFAULT)).expand(avr * 2, 0));
			}

			if (!rect.equals(new Rectangle(text.getBounds())))
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
		}

	}

	/**
	 * @generated
	 */
	private static class LabelCellEditorLocator implements CellEditorLocator {

		/**
		 * @generated
		 */
		private Label label;

		/**
		 * @generated
		 */
		public LabelCellEditorLocator(Label label) {
			this.label = label;
		}

		/**
		 * @generated
		 */
		public Label getLabel() {
			return label;
		}

		/**
		 * @generated
		 */
		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getLabel().getTextBounds().getCopy();
			getLabel().translateToAbsolute(rect);

			int avr = FigureUtilities.getFontMetrics(text.getFont())
					.getAverageCharWidth();
			rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT,
					SWT.DEFAULT)).expand(avr * 2, 0));

			if (!rect.equals(new Rectangle(text.getBounds())))
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
		}
	}
}
