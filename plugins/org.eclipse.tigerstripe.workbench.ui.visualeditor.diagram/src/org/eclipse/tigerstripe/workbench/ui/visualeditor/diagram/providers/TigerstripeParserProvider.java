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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationAEndMultiplicityEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationAEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassAEndMultiplicityEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassAEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassZEndMultiplicityEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassZEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationZEndMultiplicityEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationZEndNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute2EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute4EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute5EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute6EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute7EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AttributeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationBaseTypeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.LiteralEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Method2EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Method3EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Method4EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MethodEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ReferenceMultiplicityEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ReferenceNameEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactStereotypesEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeVisualIDRegistry;

/**
 * @generated
 */
public class TigerstripeParserProvider extends AbstractProvider implements
		IParserProvider {

	/**
	 * @generated
	 */
	private IParser attributeAttribute_2001Parser;

	/**
	 * @generated
	 */
	private IParser getAttributeAttribute_2001Parser() {
		if (attributeAttribute_2001Parser == null) {
			attributeAttribute_2001Parser = createAttributeAttribute_2001Parser();
		}
		return attributeAttribute_2001Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAttributeAttribute_2001Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new AttributeParser(
				features);
		parser.setViewPattern("{0}:{1}");
		parser.setEditPattern("{0}:{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser attributeAttribute_2002Parser;

	/**
	 * @generated
	 */
	private IParser getAttributeAttribute_2002Parser() {
		if (attributeAttribute_2002Parser == null) {
			attributeAttribute_2002Parser = createAttributeAttribute_2002Parser();
		}
		return attributeAttribute_2002Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAttributeAttribute_2002Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new AttributeParser(
				features);
		parser.setViewPattern("{0}:{1}");
		parser.setEditPattern("{0}:{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser attributeAttribute_2003Parser;

	/**
	 * @generated
	 */
	private IParser getAttributeAttribute_2003Parser() {
		if (attributeAttribute_2003Parser == null) {
			attributeAttribute_2003Parser = createAttributeAttribute_2003Parser();
		}
		return attributeAttribute_2003Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAttributeAttribute_2003Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new AttributeParser(
				features);
		parser.setViewPattern("{0}:{1}");
		parser.setEditPattern("{0}:{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser methodMethod_2004Parser;

	/**
	 * @generated
	 */
	private IParser getMethodMethod_2004Parser() {
		if (methodMethod_2004Parser == null) {
			methodMethod_2004Parser = createMethodMethod_2004Parser();
		}
		return methodMethod_2004Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createMethodMethod_2004Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new MethodParser(features);
		parser.setViewPattern("{0}():{1}");
		parser.setEditPattern("{0}():{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser attributeAttribute_2005Parser;

	/**
	 * @generated
	 */
	private IParser getAttributeAttribute_2005Parser() {
		if (attributeAttribute_2005Parser == null) {
			attributeAttribute_2005Parser = createAttributeAttribute_2005Parser();
		}
		return attributeAttribute_2005Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAttributeAttribute_2005Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new AttributeParser(
				features);
		parser.setViewPattern("{0}:{1}");
		parser.setEditPattern("{0}:{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser attributeAttribute_2006Parser;

	/**
	 * @generated
	 */
	private IParser getAttributeAttribute_2006Parser() {
		if (attributeAttribute_2006Parser == null) {
			attributeAttribute_2006Parser = createAttributeAttribute_2006Parser();
		}
		return attributeAttribute_2006Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAttributeAttribute_2006Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new AttributeParser(
				features);
		parser.setViewPattern("{0}:{1}");
		parser.setEditPattern("{0}:{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser methodMethod_2007Parser;

	/**
	 * @generated
	 */
	private IParser getMethodMethod_2007Parser() {
		if (methodMethod_2007Parser == null) {
			methodMethod_2007Parser = createMethodMethod_2007Parser();
		}
		return methodMethod_2007Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createMethodMethod_2007Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new MethodParser(features);
		parser.setViewPattern("{0}():{1}");
		parser.setEditPattern("{0}():{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser literalLiteral_2008Parser;

	/**
	 * @generated
	 */
	private IParser getLiteralLiteral_2008Parser() {
		if (literalLiteral_2008Parser == null) {
			literalLiteral_2008Parser = createLiteralLiteral_2008Parser();
		}
		return literalLiteral_2008Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createLiteralLiteral_2008Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getLiteral()
				.getEStructuralFeature("value")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new LiteralParser(features);
		parser.setViewPattern("{0}={1}");
		parser.setEditPattern("{0}={1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser attributeAttribute_2009Parser;

	/**
	 * @generated
	 */
	private IParser getAttributeAttribute_2009Parser() {
		if (attributeAttribute_2009Parser == null) {
			attributeAttribute_2009Parser = createAttributeAttribute_2009Parser();
		}
		return attributeAttribute_2009Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAttributeAttribute_2009Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new AttributeParser(
				features);
		parser.setViewPattern("{0}:{1}");
		parser.setEditPattern("{0}:{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser methodMethod_2010Parser;

	/**
	 * @generated
	 */
	private IParser getMethodMethod_2010Parser() {
		if (methodMethod_2010Parser == null) {
			methodMethod_2010Parser = createMethodMethod_2010Parser();
		}
		return methodMethod_2010Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createMethodMethod_2010Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new MethodParser(features);
		parser.setViewPattern("{0}():{1}");
		parser.setEditPattern("{0}():{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser attributeAttribute_2011Parser;

	/**
	 * @generated
	 */
	private IParser getAttributeAttribute_2011Parser() {
		if (attributeAttribute_2011Parser == null) {
			attributeAttribute_2011Parser = createAttributeAttribute_2011Parser();
		}
		return attributeAttribute_2011Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAttributeAttribute_2011Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new AttributeParser(
				features);
		parser.setViewPattern("{0}:{1}");
		parser.setEditPattern("{0}:{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser methodMethod_2012Parser;

	/**
	 * @generated
	 */
	private IParser getMethodMethod_2012Parser() {
		if (methodMethod_2012Parser == null) {
			methodMethod_2012Parser = createMethodMethod_2012Parser();
		}
		return methodMethod_2012Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createMethodMethod_2012Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getTypedElement()
				.getEStructuralFeature("type")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new MethodParser(features);
		parser.setViewPattern("{0}():{1}");
		parser.setEditPattern("{0}():{1}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser namedQueryArtifactNamedQueryArtifactStereotypes_4001Parser;

	/**
	 * @generated
	 */
	private IParser getNamedQueryArtifactNamedQueryArtifactStereotypes_4001Parser() {
		if (namedQueryArtifactNamedQueryArtifactStereotypes_4001Parser == null) {
			namedQueryArtifactNamedQueryArtifactStereotypes_4001Parser = createNamedQueryArtifactNamedQueryArtifactStereotypes_4001Parser();
		}
		return namedQueryArtifactNamedQueryArtifactStereotypes_4001Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createNamedQueryArtifactNamedQueryArtifactStereotypes_4001Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser namedQueryArtifactNamedQueryArtifactNamePackage_4002Parser;

	/**
	 * @generated
	 */
	private IParser getNamedQueryArtifactNamedQueryArtifactNamePackage_4002Parser() {
		if (namedQueryArtifactNamedQueryArtifactNamePackage_4002Parser == null) {
			namedQueryArtifactNamedQueryArtifactNamePackage_4002Parser = createNamedQueryArtifactNamedQueryArtifactNamePackage_4002Parser();
		}
		return namedQueryArtifactNamedQueryArtifactNamePackage_4002Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createNamedQueryArtifactNamedQueryArtifactNamePackage_4002Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser exceptionArtifactExceptionArtifactStereotypes_4003Parser;

	/**
	 * @generated
	 */
	private IParser getExceptionArtifactExceptionArtifactStereotypes_4003Parser() {
		if (exceptionArtifactExceptionArtifactStereotypes_4003Parser == null) {
			exceptionArtifactExceptionArtifactStereotypes_4003Parser = createExceptionArtifactExceptionArtifactStereotypes_4003Parser();
		}
		return exceptionArtifactExceptionArtifactStereotypes_4003Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createExceptionArtifactExceptionArtifactStereotypes_4003Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser exceptionArtifactExceptionArtifactNamePackage_4004Parser;

	/**
	 * @generated
	 */
	private IParser getExceptionArtifactExceptionArtifactNamePackage_4004Parser() {
		if (exceptionArtifactExceptionArtifactNamePackage_4004Parser == null) {
			exceptionArtifactExceptionArtifactNamePackage_4004Parser = createExceptionArtifactExceptionArtifactNamePackage_4004Parser();
		}
		return exceptionArtifactExceptionArtifactNamePackage_4004Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createExceptionArtifactExceptionArtifactNamePackage_4004Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser managedEntityArtifactManagedEntityArtifactStereotypes_4005Parser;

	/**
	 * @generated
	 */
	private IParser getManagedEntityArtifactManagedEntityArtifactStereotypes_4005Parser() {
		if (managedEntityArtifactManagedEntityArtifactStereotypes_4005Parser == null) {
			managedEntityArtifactManagedEntityArtifactStereotypes_4005Parser = createManagedEntityArtifactManagedEntityArtifactStereotypes_4005Parser();
		}
		return managedEntityArtifactManagedEntityArtifactStereotypes_4005Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createManagedEntityArtifactManagedEntityArtifactStereotypes_4005Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser managedEntityArtifactManagedEntityArtifactNamePackage_4006Parser;

	/**
	 * @generated
	 */
	private IParser getManagedEntityArtifactManagedEntityArtifactNamePackage_4006Parser() {
		if (managedEntityArtifactManagedEntityArtifactNamePackage_4006Parser == null) {
			managedEntityArtifactManagedEntityArtifactNamePackage_4006Parser = createManagedEntityArtifactManagedEntityArtifactNamePackage_4006Parser();
		}
		return managedEntityArtifactManagedEntityArtifactNamePackage_4006Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createManagedEntityArtifactManagedEntityArtifactNamePackage_4006Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser notificationArtifactNotificationArtifactStereotypes_4007Parser;

	/**
	 * @generated
	 */
	private IParser getNotificationArtifactNotificationArtifactStereotypes_4007Parser() {
		if (notificationArtifactNotificationArtifactStereotypes_4007Parser == null) {
			notificationArtifactNotificationArtifactStereotypes_4007Parser = createNotificationArtifactNotificationArtifactStereotypes_4007Parser();
		}
		return notificationArtifactNotificationArtifactStereotypes_4007Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createNotificationArtifactNotificationArtifactStereotypes_4007Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser notificationArtifactNotificationArtifactNamePackage_4008Parser;

	/**
	 * @generated
	 */
	private IParser getNotificationArtifactNotificationArtifactNamePackage_4008Parser() {
		if (notificationArtifactNotificationArtifactNamePackage_4008Parser == null) {
			notificationArtifactNotificationArtifactNamePackage_4008Parser = createNotificationArtifactNotificationArtifactNamePackage_4008Parser();
		}
		return notificationArtifactNotificationArtifactNamePackage_4008Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createNotificationArtifactNotificationArtifactNamePackage_4008Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser datatypeArtifactDatatypeArtifactStereotypes_4009Parser;

	/**
	 * @generated
	 */
	private IParser getDatatypeArtifactDatatypeArtifactStereotypes_4009Parser() {
		if (datatypeArtifactDatatypeArtifactStereotypes_4009Parser == null) {
			datatypeArtifactDatatypeArtifactStereotypes_4009Parser = createDatatypeArtifactDatatypeArtifactStereotypes_4009Parser();
		}
		return datatypeArtifactDatatypeArtifactStereotypes_4009Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createDatatypeArtifactDatatypeArtifactStereotypes_4009Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser datatypeArtifactDatatypeArtifactNamePackage_4010Parser;

	/**
	 * @generated
	 */
	private IParser getDatatypeArtifactDatatypeArtifactNamePackage_4010Parser() {
		if (datatypeArtifactDatatypeArtifactNamePackage_4010Parser == null) {
			datatypeArtifactDatatypeArtifactNamePackage_4010Parser = createDatatypeArtifactDatatypeArtifactNamePackage_4010Parser();
		}
		return datatypeArtifactDatatypeArtifactNamePackage_4010Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createDatatypeArtifactDatatypeArtifactNamePackage_4010Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser enumerationEnumerationStereotypes_4011Parser;

	/**
	 * @generated
	 */
	private IParser getEnumerationEnumerationStereotypes_4011Parser() {
		if (enumerationEnumerationStereotypes_4011Parser == null) {
			enumerationEnumerationStereotypes_4011Parser = createEnumerationEnumerationStereotypes_4011Parser();
		}
		return enumerationEnumerationStereotypes_4011Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createEnumerationEnumerationStereotypes_4011Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser enumerationEnumerationNamePackage_4012Parser;

	/**
	 * @generated
	 */
	private IParser getEnumerationEnumerationNamePackage_4012Parser() {
		if (enumerationEnumerationNamePackage_4012Parser == null) {
			enumerationEnumerationNamePackage_4012Parser = createEnumerationEnumerationNamePackage_4012Parser();
		}
		return enumerationEnumerationNamePackage_4012Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createEnumerationEnumerationNamePackage_4012Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createExceptionArtifactExceptionArtifactName_4003Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeClassnameConstrainedFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("name")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser enumerationEnumerationBaseType_4013Parser;

	/**
	 * @generated
	 */
	private IParser getEnumerationEnumerationBaseType_4013Parser() {
		if (enumerationEnumerationBaseType_4013Parser == null) {
			enumerationEnumerationBaseType_4013Parser = createEnumerationEnumerationBaseType_4013Parser();
		}
		return enumerationEnumerationBaseType_4013Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createEnumerationEnumerationBaseType_4013Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeClassnameConstrainedFeatureParser(
				VisualeditorPackage.eINSTANCE.getEnumeration()
						.getEStructuralFeature("baseType")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser updateProcedureArtifactUpdateProcedureArtifactStereotypes_4014Parser;

	/**
	 * @generated
	 */
	private IParser getUpdateProcedureArtifactUpdateProcedureArtifactStereotypes_4014Parser() {
		if (updateProcedureArtifactUpdateProcedureArtifactStereotypes_4014Parser == null) {
			updateProcedureArtifactUpdateProcedureArtifactStereotypes_4014Parser = createUpdateProcedureArtifactUpdateProcedureArtifactStereotypes_4014Parser();
		}
		return updateProcedureArtifactUpdateProcedureArtifactStereotypes_4014Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createUpdateProcedureArtifactUpdateProcedureArtifactStereotypes_4014Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser updateProcedureArtifactUpdateProcedureArtifactNamePackage_4015Parser;

	/**
	 * @generated
	 */
	private IParser getUpdateProcedureArtifactUpdateProcedureArtifactNamePackage_4015Parser() {
		if (updateProcedureArtifactUpdateProcedureArtifactNamePackage_4015Parser == null) {
			updateProcedureArtifactUpdateProcedureArtifactNamePackage_4015Parser = createUpdateProcedureArtifactUpdateProcedureArtifactNamePackage_4015Parser();
		}
		return updateProcedureArtifactUpdateProcedureArtifactNamePackage_4015Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createUpdateProcedureArtifactUpdateProcedureArtifactNamePackage_4015Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser sessionFacadeArtifactSessionFacadeArtifactStereotypes_4016Parser;

	/**
	 * @generated
	 */
	private IParser getSessionFacadeArtifactSessionFacadeArtifactStereotypes_4016Parser() {
		if (sessionFacadeArtifactSessionFacadeArtifactStereotypes_4016Parser == null) {
			sessionFacadeArtifactSessionFacadeArtifactStereotypes_4016Parser = createSessionFacadeArtifactSessionFacadeArtifactStereotypes_4016Parser();
		}
		return sessionFacadeArtifactSessionFacadeArtifactStereotypes_4016Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createSessionFacadeArtifactSessionFacadeArtifactStereotypes_4016Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser sessionFacadeArtifactSessionFacadeArtifactNamePackage_4017Parser;

	/**
	 * @generated
	 */
	private IParser getSessionFacadeArtifactSessionFacadeArtifactNamePackage_4017Parser() {
		if (sessionFacadeArtifactSessionFacadeArtifactNamePackage_4017Parser == null) {
			sessionFacadeArtifactSessionFacadeArtifactNamePackage_4017Parser = createSessionFacadeArtifactSessionFacadeArtifactNamePackage_4017Parser();
		}
		return sessionFacadeArtifactSessionFacadeArtifactNamePackage_4017Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createSessionFacadeArtifactSessionFacadeArtifactNamePackage_4017Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationClassClassAssociationClassClassStereotypes_4018Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationClassClassAssociationClassClassStereotypes_4018Parser() {
		if (associationClassClassAssociationClassClassStereotypes_4018Parser == null) {
			associationClassClassAssociationClassClassStereotypes_4018Parser = createAssociationClassClassAssociationClassClassStereotypes_4018Parser();
		}
		return associationClassClassAssociationClassClassStereotypes_4018Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAssociationClassClassAssociationClassClassStereotypes_4018Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationClassClassAssociationClassClassNamePackage_4019Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationClassClassAssociationClassClassNamePackage_4019Parser() {
		if (associationClassClassAssociationClassClassNamePackage_4019Parser == null) {
			associationClassClassAssociationClassClassNamePackage_4019Parser = createAssociationClassClassAssociationClassClassNamePackage_4019Parser();
		}
		return associationClassClassAssociationClassClassNamePackage_4019Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAssociationClassClassAssociationClassClassNamePackage_4019Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationAssociationStereotypes_4020Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationAssociationStereotypes_4020Parser() {
		if (associationAssociationStereotypes_4020Parser == null) {
			associationAssociationStereotypes_4020Parser = createAssociationAssociationStereotypes_4020Parser();
		}
		return associationAssociationStereotypes_4020Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAssociationAssociationStereotypes_4020Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationAssociationNamePackage_4021Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationAssociationNamePackage_4021Parser() {
		if (associationAssociationNamePackage_4021Parser == null) {
			associationAssociationNamePackage_4021Parser = createAssociationAssociationNamePackage_4021Parser();
		}
		return associationAssociationNamePackage_4021Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAssociationAssociationNamePackage_4021Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationAssociationAEndMultiplicity_4022Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationAssociationAEndMultiplicity_4022Parser() {
		if (associationAssociationAEndMultiplicity_4022Parser == null) {
			associationAssociationAEndMultiplicity_4022Parser = createAssociationAssociationAEndMultiplicity_4022Parser();
		}
		return associationAssociationAEndMultiplicity_4022Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createAssociationAssociationAEndMultiplicity_4022Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStructuralFeatureParser(
				VisualeditorPackage.eINSTANCE.getAssociation()
						.getEStructuralFeature("aEndMultiplicity")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationAssociationZEndMultiplicity_4023Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationAssociationZEndMultiplicity_4023Parser() {
		if (associationAssociationZEndMultiplicity_4023Parser == null) {
			associationAssociationZEndMultiplicity_4023Parser = createAssociationAssociationZEndMultiplicity_4023Parser();
		}
		return associationAssociationZEndMultiplicity_4023Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createAssociationAssociationZEndMultiplicity_4023Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStructuralFeatureParser(
				VisualeditorPackage.eINSTANCE.getAssociation()
						.getEStructuralFeature("zEndMultiplicity")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationAssociationAEndName_4024Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationAssociationAEndName_4024Parser() {
		if (associationAssociationAEndName_4024Parser == null) {
			associationAssociationAEndName_4024Parser = createAssociationAssociationAEndName_4024Parser();
		}
		return associationAssociationAEndName_4024Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAssociationAssociationAEndName_4024Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeFieldnameConstrainedFeatureParser(
				VisualeditorPackage.eINSTANCE.getAssociation()
						.getEStructuralFeature("aEndName")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationAssociationZEndName_4025Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationAssociationZEndName_4025Parser() {
		if (associationAssociationZEndName_4025Parser == null) {
			associationAssociationZEndName_4025Parser = createAssociationAssociationZEndName_4025Parser();
		}
		return associationAssociationZEndName_4025Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAssociationAssociationZEndName_4025Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeFieldnameConstrainedFeatureParser(
				VisualeditorPackage.eINSTANCE.getAssociation()
						.getEStructuralFeature("zEndName")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser dependencyDependencyNamePackage_4031Parser;

	/**
	 * @generated
	 */
	private IParser getDependencyDependencyNamePackage_4031Parser() {
		if (dependencyDependencyNamePackage_4031Parser == null) {
			dependencyDependencyNamePackage_4031Parser = createDependencyDependencyNamePackage_4031Parser();
		}
		return dependencyDependencyNamePackage_4031Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createDependencyDependencyNamePackage_4031Parser() {
		List features = new ArrayList(2);
		features.add(VisualeditorPackage.eINSTANCE.getNamedElement()
				.getEStructuralFeature("name")); //$NON-NLS-1$
		features.add(VisualeditorPackage.eINSTANCE.getQualifiedNamedElement()
				.getEStructuralFeature("package")); //$NON-NLS-1$
		TigerstripeStructuralFeaturesParser parser = new TigerstripeClassnameConstrainedFeaturesParser(
				features);
		parser.setViewPattern("{1}.{0}");
		parser.setEditPattern("{0}");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser dependencyDependencyStereotypes_4038Parser;

	/**
	 * @generated
	 */
	private IParser getDependencyDependencyStereotypes_4038Parser() {
		if (dependencyDependencyStereotypes_4038Parser == null) {
			dependencyDependencyStereotypes_4038Parser = createDependencyDependencyStereotypes_4038Parser();
		}
		return dependencyDependencyStereotypes_4038Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createDependencyDependencyStereotypes_4038Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStereotypeFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("stereotypes")); //$NON-NLS-1$
		parser.setViewPattern("<<{0}>>");
		parser.setEditPattern("<<{0}>>");
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser referenceReferenceName_4032Parser;

	/**
	 * @generated
	 */
	private IParser getReferenceReferenceName_4032Parser() {
		if (referenceReferenceName_4032Parser == null) {
			referenceReferenceName_4032Parser = createReferenceReferenceName_4032Parser();
		}
		return referenceReferenceName_4032Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createReferenceReferenceName_4032Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeClassnameConstrainedFeatureParser(
				VisualeditorPackage.eINSTANCE.getNamedElement()
						.getEStructuralFeature("name")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser referenceReferenceTypeMultiplicity_4033Parser;

	/**
	 * @generated
	 */
	private IParser getReferenceReferenceTypeMultiplicity_4033Parser() {
		if (referenceReferenceTypeMultiplicity_4033Parser == null) {
			referenceReferenceTypeMultiplicity_4033Parser = createReferenceReferenceTypeMultiplicity_4033Parser();
		}
		return referenceReferenceTypeMultiplicity_4033Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createReferenceReferenceTypeMultiplicity_4033Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStructuralFeatureParser(
				VisualeditorPackage.eINSTANCE.getReference()
						.getEStructuralFeature("typeMultiplicity")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationClassAssociationClassAEndMultiplicity_4034Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationClassAssociationClassAEndMultiplicity_4034Parser() {
		if (associationClassAssociationClassAEndMultiplicity_4034Parser == null) {
			associationClassAssociationClassAEndMultiplicity_4034Parser = createAssociationClassAssociationClassAEndMultiplicity_4034Parser();
		}
		return associationClassAssociationClassAEndMultiplicity_4034Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createAssociationClassAssociationClassAEndMultiplicity_4034Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStructuralFeatureParser(
				VisualeditorPackage.eINSTANCE.getAssociation()
						.getEStructuralFeature("aEndMultiplicity")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationClassAssociationClassZEndMultiplicity_4035Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationClassAssociationClassZEndMultiplicity_4035Parser() {
		if (associationClassAssociationClassZEndMultiplicity_4035Parser == null) {
			associationClassAssociationClassZEndMultiplicity_4035Parser = createAssociationClassAssociationClassZEndMultiplicity_4035Parser();
		}
		return associationClassAssociationClassZEndMultiplicity_4035Parser;
	}

	/**
	 * @generated
	 */
	protected IParser createAssociationClassAssociationClassZEndMultiplicity_4035Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeStructuralFeatureParser(
				VisualeditorPackage.eINSTANCE.getAssociation()
						.getEStructuralFeature("zEndMultiplicity")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationClassAssociationClassAEndName_4036Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationClassAssociationClassAEndName_4036Parser() {
		if (associationClassAssociationClassAEndName_4036Parser == null) {
			associationClassAssociationClassAEndName_4036Parser = createAssociationClassAssociationClassAEndName_4036Parser();
		}
		return associationClassAssociationClassAEndName_4036Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAssociationClassAssociationClassAEndName_4036Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeFieldnameConstrainedFeatureParser(
				VisualeditorPackage.eINSTANCE.getAssociation()
						.getEStructuralFeature("aEndName")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	private IParser associationClassAssociationClassZEndName_4037Parser;

	/**
	 * @generated
	 */
	private IParser getAssociationClassAssociationClassZEndName_4037Parser() {
		if (associationClassAssociationClassZEndName_4037Parser == null) {
			associationClassAssociationClassZEndName_4037Parser = createAssociationClassAssociationClassZEndName_4037Parser();
		}
		return associationClassAssociationClassZEndName_4037Parser;
	}

	/**
	 * @generated NOT
	 */
	protected IParser createAssociationClassAssociationClassZEndName_4037Parser() {
		TigerstripeStructuralFeatureParser parser = new TigerstripeFieldnameConstrainedFeatureParser(
				VisualeditorPackage.eINSTANCE.getAssociation()
						.getEStructuralFeature("zEndName")); //$NON-NLS-1$
		return parser;
	}

	/**
	 * @generated
	 */
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case AttributeEditPart.VISUAL_ID:
			return getAttributeAttribute_2001Parser();
		case Attribute2EditPart.VISUAL_ID:
			return getAttributeAttribute_2002Parser();
		case Attribute3EditPart.VISUAL_ID:
			return getAttributeAttribute_2003Parser();
		case MethodEditPart.VISUAL_ID:
			return getMethodMethod_2004Parser();
		case Attribute4EditPart.VISUAL_ID:
			return getAttributeAttribute_2005Parser();
		case Attribute5EditPart.VISUAL_ID:
			return getAttributeAttribute_2006Parser();
		case Method2EditPart.VISUAL_ID:
			return getMethodMethod_2007Parser();
		case LiteralEditPart.VISUAL_ID:
			return getLiteralLiteral_2008Parser();
		case Attribute6EditPart.VISUAL_ID:
			return getAttributeAttribute_2009Parser();
		case Method3EditPart.VISUAL_ID:
			return getMethodMethod_2010Parser();
		case Attribute7EditPart.VISUAL_ID:
			return getAttributeAttribute_2011Parser();
		case Method4EditPart.VISUAL_ID:
			return getMethodMethod_2012Parser();
		case NamedQueryArtifactStereotypesEditPart.VISUAL_ID:
			return getNamedQueryArtifactNamedQueryArtifactStereotypes_4001Parser();
		case NamedQueryArtifactNamePackageEditPart.VISUAL_ID:
			return getNamedQueryArtifactNamedQueryArtifactNamePackage_4002Parser();
		case ExceptionArtifactStereotypesEditPart.VISUAL_ID:
			return getExceptionArtifactExceptionArtifactStereotypes_4003Parser();
		case ExceptionArtifactNamePackageEditPart.VISUAL_ID:
			return getExceptionArtifactExceptionArtifactNamePackage_4004Parser();
		case ManagedEntityArtifactStereotypesEditPart.VISUAL_ID:
			return getManagedEntityArtifactManagedEntityArtifactStereotypes_4005Parser();
		case ManagedEntityArtifactNamePackageEditPart.VISUAL_ID:
			return getManagedEntityArtifactManagedEntityArtifactNamePackage_4006Parser();
		case NotificationArtifactStereotypesEditPart.VISUAL_ID:
			return getNotificationArtifactNotificationArtifactStereotypes_4007Parser();
		case NotificationArtifactNamePackageEditPart.VISUAL_ID:
			return getNotificationArtifactNotificationArtifactNamePackage_4008Parser();
		case DatatypeArtifactStereotypesEditPart.VISUAL_ID:
			return getDatatypeArtifactDatatypeArtifactStereotypes_4009Parser();
		case DatatypeArtifactNamePackageEditPart.VISUAL_ID:
			return getDatatypeArtifactDatatypeArtifactNamePackage_4010Parser();
		case EnumerationStereotypesEditPart.VISUAL_ID:
			return getEnumerationEnumerationStereotypes_4011Parser();
		case EnumerationNamePackageEditPart.VISUAL_ID:
			return getEnumerationEnumerationNamePackage_4012Parser();
		case EnumerationBaseTypeEditPart.VISUAL_ID:
			return getEnumerationEnumerationBaseType_4013Parser();
		case UpdateProcedureArtifactStereotypesEditPart.VISUAL_ID:
			return getUpdateProcedureArtifactUpdateProcedureArtifactStereotypes_4014Parser();
		case UpdateProcedureArtifactNamePackageEditPart.VISUAL_ID:
			return getUpdateProcedureArtifactUpdateProcedureArtifactNamePackage_4015Parser();
		case SessionFacadeArtifactStereotypesEditPart.VISUAL_ID:
			return getSessionFacadeArtifactSessionFacadeArtifactStereotypes_4016Parser();
		case SessionFacadeArtifactNamePackageEditPart.VISUAL_ID:
			return getSessionFacadeArtifactSessionFacadeArtifactNamePackage_4017Parser();
		case AssociationClassClassStereotypesEditPart.VISUAL_ID:
			return getAssociationClassClassAssociationClassClassStereotypes_4018Parser();
		case AssociationClassClassNamePackageEditPart.VISUAL_ID:
			return getAssociationClassClassAssociationClassClassNamePackage_4019Parser();
		case AssociationStereotypesEditPart.VISUAL_ID:
			return getAssociationAssociationStereotypes_4020Parser();
		case AssociationNamePackageEditPart.VISUAL_ID:
			return getAssociationAssociationNamePackage_4021Parser();
		case AssociationAEndMultiplicityEditPart.VISUAL_ID:
			return getAssociationAssociationAEndMultiplicity_4022Parser();
		case AssociationZEndMultiplicityEditPart.VISUAL_ID:
			return getAssociationAssociationZEndMultiplicity_4023Parser();
		case AssociationAEndNameEditPart.VISUAL_ID:
			return getAssociationAssociationAEndName_4024Parser();
		case AssociationZEndNameEditPart.VISUAL_ID:
			return getAssociationAssociationZEndName_4025Parser();
		case DependencyNamePackageEditPart.VISUAL_ID:
			return getDependencyDependencyNamePackage_4031Parser();
		case DependencyStereotypesEditPart.VISUAL_ID:
			return getDependencyDependencyStereotypes_4038Parser();
		case ReferenceNameEditPart.VISUAL_ID:
			return getReferenceReferenceName_4032Parser();
		case ReferenceMultiplicityEditPart.VISUAL_ID:
			return getReferenceReferenceTypeMultiplicity_4033Parser();
		case AssociationClassAEndMultiplicityEditPart.VISUAL_ID:
			return getAssociationClassAssociationClassAEndMultiplicity_4034Parser();
		case AssociationClassZEndMultiplicityEditPart.VISUAL_ID:
			return getAssociationClassAssociationClassZEndMultiplicity_4035Parser();
		case AssociationClassAEndNameEditPart.VISUAL_ID:
			return getAssociationClassAssociationClassAEndName_4036Parser();
		case AssociationClassZEndNameEditPart.VISUAL_ID:
			return getAssociationClassAssociationClassZEndName_4037Parser();
		}
		return null;
	}

	/**
	 * @generated
	 */
	public IParser getParser(IAdaptable hint) {
		String vid = (String) hint.getAdapter(String.class);
		if (vid != null)
			return getParser(TigerstripeVisualIDRegistry.getVisualID(vid));
		View view = (View) hint.getAdapter(View.class);
		if (view != null)
			return getParser(TigerstripeVisualIDRegistry.getVisualID(view));
		return null;
	}

	/**
	 * @generated
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();
			if (TigerstripeElementTypes.getElement(hint) == null)
				return false;
			return getParser(hint) != null;
		}
		return false;
	}
}
