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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.tigerstripe.metamodel.impl.IFieldImpl;
import org.eclipse.tigerstripe.metamodel.impl.ILiteralImpl;
import org.eclipse.tigerstripe.metamodel.impl.IMethodImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.repository.internal.IModelComponentMetadata;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.NodePattern;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.patterns.IEnumPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IQueryPattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ElementTypeMapper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.TigerstripeElementTypes;

/**
 * @generated
 */
public class TigerstripePaletteFactory {

	/**
	 * 
	 */
	public void fillPalette(PaletteRoot paletteRoot) {
		paletteRoot.add(createArtifacts1Group());
		paletteRoot.add(createFeatures2Group());
		/*
		 * Remove the connection group from the palette (so that users can only
		 * create connections using handles on objects). This will give us more
		 * control over the types of connections that can be made between
		 * artifacts.
		 */
		// paletteRoot.add(createConnections3Group());
	}

	/**
	 * 
	 */
	private PaletteContainer createArtifacts1Group() {
		PaletteContainer paletteContainer = new PaletteDrawer("Artifacts");
		paletteContainer.setDescription("Tigerstripe Artifact Palette");
		/*
		 * based on the profile being used, add the appropriate artifacts to the
		 * palette (only those artifacts that should be in the model for a given
		 * profile)
		 */
		for (String patternName : PatternFactory.getInstance().getRegisteredPatterns().keySet()){
			
			IPattern pattern = PatternFactory.getInstance().getPattern(patternName);
			if (pattern instanceof NodePattern){
				paletteContainer.add(createPatternBasedCreationTool((NodePattern)pattern));
			}

			
		}
		
//		IWorkbenchProfile profile = TigerstripeCore
//				.getWorkbenchProfileSession().getActiveProfile();
//		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
//				.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
//		if (prop.getDetailsForType(IManagedEntityArtifact.class.getName())
//				.isEnabled()) {
//			paletteContainer.add(createManagedEntity1CreationTool());
//		}
//		if (prop.getDetailsForType(IDatatypeArtifact.class.getName())
//				.isEnabled()) {
//			paletteContainer.add(createDatatype2CreationTool());
//		}
//		if (prop.getDetailsForType(IEnumArtifact.class.getName()).isEnabled()) {
//			paletteContainer.add(createEnumeration3CreationTool());
//		}
//		if (prop.getDetailsForType(IAssociationClassArtifact.class.getName())
//				.isEnabled()) {
//			// paletteContainer.add(createAssociationClass8CreationTool());
//		}
//		if (prop.getDetailsForType(IQueryArtifact.class.getName()).isEnabled()) {
//			paletteContainer.add(createQuery5CreationTool());
//		}
//		if (prop.getDetailsForType(IUpdateProcedureArtifact.class.getName())
//				.isEnabled()) {
//			paletteContainer.add(createUpdateProcedure6CreationTool());
//		}
//		if (prop.getDetailsForType(IEventArtifact.class.getName()).isEnabled()) {
//			paletteContainer.add(createNotification4CreationTool());
//		}
//		if (prop.getDetailsForType(ISessionArtifact.class.getName())
//				.isEnabled()) {
//			paletteContainer.add(createSessionFacade8CreationTool());
//		}
//		if (prop.getDetailsForType(IExceptionArtifact.class.getName())
//				.isEnabled()) {
//			paletteContainer.add(createException7CreationTool());
//		}
		return paletteContainer;
	}

	/**
	 * @generated
	 */
	private PaletteContainer createFeatures2Group() {
		PaletteContainer paletteContainer = new PaletteDrawer("Features");
		paletteContainer.add(createAttribute1CreationTool());
		paletteContainer.add(createMethod2CreationTool());
		paletteContainer.add(createLiteral3CreationTool());
		return paletteContainer;
	}

//	/**
//	 * @generated
//	 */
//	private PaletteContainer createConnections3Group() {
//		PaletteContainer paletteContainer = new PaletteDrawer("Connections");
//		paletteContainer.add(createExtends1CreationTool());
//		
//		paletteContainer.add(createAssociation2CreationTool());
//		paletteContainer.add(createDependency3CreationTool());
//		paletteContainer.add(createAssociationClass5CreationTool());
//		paletteContainer.add(createAssociationClassClass6CreationTool());
//		paletteContainer.add(createAssociationClassConnection7CreationTool());
//		
//		
//		paletteContainer.add(createReference4CreationTool());
//		paletteContainer.add(createManages8CreationTool());
//		paletteContainer.add(createEmits9CreationTool());
//		paletteContainer.add(createSupports10CreationTool());
//		paletteContainer.add(createExposes11CreationTool());
//		paletteContainer.add(createReturns12CreationTool());
//		paletteContainer.add(createImplements13CreationTool());
//		return paletteContainer;
//	}

	/**
	 * @generated NOT
	 */
	private ToolEntry createPatternBasedCreationTool(NodePattern pattern) {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = pattern.getDescriptor();
		largeImage = pattern.getDescriptor();

		final List<IElementType> elementTypes = new ArrayList<IElementType>();
		elementTypes.add(ElementTypeMapper.mapClassStringToElementType(pattern.getTargetArtifactType()));
		ToolEntry result = new PatternBasedNodeToolEntry(pattern.getUILabel(),
				pattern.getDescription(), smallImage,
				largeImage, elementTypes,pattern);

		return result;
	}
	
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createManagedEntity1CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.ENTITY_ICON);
//		largeImage = Images.getDescriptor(Images.ENTITY_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(IManagedEntityArtifactImpl.class.getName());
//
//		final List<IElementType> elementTypes = new ArrayList<IElementType>();
//		elementTypes.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
//		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, elementTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createDatatype2CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.DATATYPE_ICON);
//		largeImage = Images.getDescriptor(Images.DATATYPE_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(IDatatypeArtifactImpl.class.getName());
//
//		final List<IElementType> elementTypes = new ArrayList<IElementType>();
//		elementTypes.add(TigerstripeElementTypes.DatatypeArtifact_1005);
//		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, elementTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createEnumeration3CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.ENUM_ICON);
//		largeImage = Images.getDescriptor(Images.ENUM_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(IEnumArtifactImpl.class.getName());
//
//		final List<IElementType> elementTypes = new ArrayList<IElementType>();
//		elementTypes.add(TigerstripeElementTypes.Enumeration_1006);
//		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, elementTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createNotification4CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.NOTIFICATION_ICON);
//		largeImage = Images.getDescriptor(Images.NOTIFICATION_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(IEventArtifactImpl.class.getName());
//
//		final List<IElementType> elementTypes = new ArrayList<IElementType>();
//		elementTypes.add(TigerstripeElementTypes.NotificationArtifact_1004);
//		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, elementTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createQuery5CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.QUERY_ICON);
//		largeImage = Images.getDescriptor(Images.QUERY_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(IQueryArtifactImpl.class.getName());
//
//		final List<IElementType> elementTypes = new ArrayList<IElementType>();
//		elementTypes.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
//		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, elementTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createUpdateProcedure6CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.UPDATEPROC_ICON);
//		largeImage = Images.getDescriptor(Images.UPDATEPROC_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(IUpdateProcedureArtifactImpl.class.getName());
//
//		final List<IElementType> elementTypes = new ArrayList<IElementType>();
//		elementTypes.add(TigerstripeElementTypes.UpdateProcedureArtifact_1007);
//		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, elementTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createException7CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.EXCEPTION_ICON);
//		largeImage = Images.getDescriptor(Images.EXCEPTION_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(IExceptionArtifactImpl.class.getName());
//
//		final List<IElementType> elementTypes = new ArrayList<IElementType>();
//		elementTypes.add(TigerstripeElementTypes.ExceptionArtifact_1002);
//		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, elementTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createSessionFacade8CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.SESSION_ICON);
//		largeImage = Images.getDescriptor(Images.SESSION_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(ISessionArtifactImpl.class.getName());
//
//		final List<IElementType> elementTypes = new ArrayList<IElementType>();
//		elementTypes.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
//		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, elementTypes);
//
//		return result;
//	}

	/**
	 * @generated NOT
	 */
	private ToolEntry createAttribute1CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = Images.getDescriptor(Images.FIELD_ICON);
		largeImage = Images.getDescriptor(Images.FIELD_ICON);

		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
				.getMetadata(IFieldImpl.class.getName());

		final List<IElementType> elementTypes = new ArrayList<IElementType>();
		elementTypes.add(TigerstripeElementTypes.Attribute_2001);
		elementTypes.add(TigerstripeElementTypes.Attribute_2002);
		elementTypes.add(TigerstripeElementTypes.Attribute_2003);
		elementTypes.add(TigerstripeElementTypes.Attribute_2005);
		elementTypes.add(TigerstripeElementTypes.Attribute_2006);
		elementTypes.add(TigerstripeElementTypes.Attribute_2009);
		elementTypes.add(TigerstripeElementTypes.Attribute_2011);
		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
				"Create new " + metadata.getLabel(null), smallImage,
				largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated NOT
	 */
	private ToolEntry createMethod2CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = Images.getDescriptor(Images.METHOD_ICON);
		largeImage = Images.getDescriptor(Images.METHOD_ICON);

		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
				.getMetadata(IMethodImpl.class.getName());

		final List<IElementType> elementTypes = new ArrayList<IElementType>();
		elementTypes.add(TigerstripeElementTypes.Method_2004);
		elementTypes.add(TigerstripeElementTypes.Method_2007);
		elementTypes.add(TigerstripeElementTypes.Method_2010);
		elementTypes.add(TigerstripeElementTypes.Method_2012);
		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
				"Create new " + metadata.getLabel(null), smallImage,
				largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated NOT
	 */
	private ToolEntry createLiteral3CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = Images.getDescriptor(Images.LITERAL_ICON);
		largeImage = Images.getDescriptor(Images.LITERAL_ICON);

		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
				.getMetadata(ILiteralImpl.class.getName());

		final List<IElementType> elementTypes = new ArrayList<IElementType>();
		elementTypes.add(TigerstripeElementTypes.Literal_2008);
		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
				"Create new " + metadata.getLabel(null), smallImage,
				largeImage, elementTypes);

		return result;
	}

//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createExtends1CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.EXTENDSARROW_ICON);
//		largeImage = Images.getDescriptor(Images.EXTENDSARROW_ICON);
//
//		final List<IElementType> relationshipTypes = new ArrayList<IElementType>();
//		relationshipTypes
//				.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
//		ToolEntry result = new LinkToolEntry("Extends",
//				"Create Extends Relationship", smallImage, largeImage,
//				relationshipTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createAssociation2CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.ASSOCIATION_ICON);
//		largeImage = Images.getDescriptor(Images.ASSOCIATION_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(IAssociationArtifactImpl.class.getName());
//
//		final List<IElementType> relationshipTypes = new ArrayList<IElementType>();
//		relationshipTypes.add(TigerstripeElementTypes.Association_3001);
//		ToolEntry result = new LinkToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, relationshipTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createDependency3CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.DEPENDENCY_ICON);
//		largeImage = Images.getDescriptor(Images.DEPENDENCY_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(IDependencyArtifactImpl.class.getName());
//
//		final List<IElementType> relationshipTypes = new ArrayList<IElementType>();
//		relationshipTypes.add(TigerstripeElementTypes.Dependency_3008);
//		ToolEntry result = new LinkToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, relationshipTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createReference4CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.REFERENCEARROW_ICON);
//		largeImage = Images.getDescriptor(Images.REFERENCEARROW_ICON);
//
//		final List<IElementType> relationshipTypes = new ArrayList<IElementType>();
//		relationshipTypes.add(TigerstripeElementTypes.Reference_3009);
//		ToolEntry result = new LinkToolEntry("Reference",
//				"Create a new referency relationship", smallImage, largeImage,
//				relationshipTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated NOT
//	 */
//	private ToolEntry createAssociationClass5CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = Images.getDescriptor(Images.ASSOCIATIONCLASS_ICON);
//		largeImage = Images.getDescriptor(Images.ASSOCIATIONCLASS_ICON);
//
//		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
//				.getMetadata(IAssociationClassArtifactImpl.class.getName());
//
//		final List<IElementType> relationshipTypes = new ArrayList<IElementType>();
//		relationshipTypes.add(TigerstripeElementTypes.AssociationClass_3010);
//		ToolEntry result = new LinkToolEntry(metadata.getLabel(null),
//				"Create new " + metadata.getLabel(null), smallImage,
//				largeImage, relationshipTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated
//	 */
//	private ToolEntry createAssociationClassClass6CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssocClassLinkIcon-small.png");
//
//		largeImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssocClassLinkIcon-large.png");
//
//		final List elementTypes = new ArrayList();
//		elementTypes.add(TigerstripeElementTypes.AssociationClassClass_1009);
//		ToolEntry result = new NodeToolEntry("Association Class Class",
//				"Create a new class contained within and AssociationClass",
//				smallImage, largeImage, elementTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated
//	 */
//	private ToolEntry createAssociationClassConnection7CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = TigerstripeElementTypes
//				.getImageDescriptor(TigerstripeElementTypes.AssociationClassAssociatedClass_3011);
//
//		largeImage = smallImage;
//
//		final List relationshipTypes = new ArrayList();
//		relationshipTypes
//				.add(TigerstripeElementTypes.AssociationClassAssociatedClass_3011);
//		ToolEntry result = new LinkToolEntry("Association Class Connection",
//				"Create a new Association Class Connection", smallImage,
//				largeImage, relationshipTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated
//	 */
//	private ToolEntry createManages8CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-small.png");
//
//		largeImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-large.png");
//
//		final List relationshipTypes = new ArrayList();
//		relationshipTypes
//				.add(TigerstripeElementTypes.SessionFacadeArtifactManagedEntities_3003);
//		ToolEntry result = new LinkToolEntry("Manages",
//				"Designate Managed Entities", smallImage, largeImage,
//				relationshipTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated
//	 */
//	private ToolEntry createEmits9CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-small.png");
//
//		largeImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-large.png");
//
//		final List relationshipTypes = new ArrayList();
//		relationshipTypes
//				.add(TigerstripeElementTypes.SessionFacadeArtifactEmittedNotifications_3002);
//		ToolEntry result = new LinkToolEntry("Emits",
//				"Designate Emitted Notifications", smallImage, largeImage,
//				relationshipTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated
//	 */
//	private ToolEntry createSupports10CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-small.png");
//
//		largeImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-large.png");
//
//		final List relationshipTypes = new ArrayList();
//		relationshipTypes
//				.add(TigerstripeElementTypes.SessionFacadeArtifactNamedQueries_3005);
//		ToolEntry result = new LinkToolEntry("Supports",
//				"Designate Supported Queries", smallImage, largeImage,
//				relationshipTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated
//	 */
//	private ToolEntry createExposes11CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-small.png");
//
//		largeImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-large.png");
//
//		final List relationshipTypes = new ArrayList();
//		relationshipTypes
//				.add(TigerstripeElementTypes.SessionFacadeArtifactExposedProcedures_3006);
//		ToolEntry result = new LinkToolEntry("Exposes",
//				"Designate Exposed Procedures", smallImage, largeImage,
//				relationshipTypes);
//
//		return result;
//	}
//
//	/**
//	 * @generated
//	 */
//	private ToolEntry createReturns12CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-small.png");
//
//		largeImage = TigerstripeDiagramEditorPlugin
//				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-large.png");
//
//		final List relationshipTypes = new ArrayList();
//		relationshipTypes
//				.add(TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004);
//		ToolEntry result = new LinkToolEntry("Returns", "Specify Query Return",
//				smallImage, largeImage, relationshipTypes);
//
//		return result;
//	}

//	/**
//	 * @generated
//	 */
//	private ToolEntry createImplements13CreationTool() {
//		ImageDescriptor smallImage;
//		ImageDescriptor largeImage;
//
//		smallImage = TigerstripeElementTypes
//				.getImageDescriptor(TigerstripeElementTypes.Dependency_3008);
//
//		largeImage = smallImage;
//
//		final List relationshipTypes = new ArrayList();
//		relationshipTypes.add(TigerstripeElementTypes.Dependency_3008);
//		ToolEntry result = new LinkToolEntry("Implements",
//				"Creates an Implements relationship", smallImage, largeImage,
//				relationshipTypes);
//
//		return result;
//	}

	/**
	 * @generated
	 */
	private static class NodeToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List elementTypes;

		/**
		 * @generated
		 */
		private NodeToolEntry(String title, String description,
				ImageDescriptor smallIcon, ImageDescriptor largeIcon,
				List elementTypes) {
			super(title, description, smallIcon, largeIcon);
			this.elementTypes = elementTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes) {

				@Override
				protected void performCreation(int button) {
					// TODO Auto-generated method stub
					super.performCreation(button);
				}

			};
			tool.setProperties(getToolProperties());
			return tool;
		}
	}
	
	/**
	 * @generated NOT
	 */
	private static class PatternBasedNodeToolEntry extends ToolEntry {

		/**
		 * @generated NOT
		 */
		private final List elementTypes;
		private final IPattern pattern;

		/**
		 * @generated NOT
		 */
		private PatternBasedNodeToolEntry(String title, String description,
				ImageDescriptor smallIcon, ImageDescriptor largeIcon,
				List elementTypes, IPattern pattern) {
			super(title, description, smallIcon, largeIcon);
			this.elementTypes = elementTypes;
			this.pattern = pattern;
		}

		/**
		 * @generated NOT
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes) {

				@Override
				protected void performCreation(int button) {
					// TODO Auto-generated method stub
					super.performCreation(button);
					

					Object o = getCreateRequest().getNewObject();
					if (o instanceof Collection<?>) {
						Collection<?> c = (Collection<?>) o;
						Object e = c.iterator().next();
						if (e instanceof ViewAndElementDescriptor) {
							ViewAndElementDescriptor desc = (ViewAndElementDescriptor) e;
							Node node = (Node) desc.getAdapter(View.class);
							AbstractArtifact createdEArtifact = (AbstractArtifact) node
									.getElement();
							
							Map map = (Map) createdEArtifact.eContainer();
							ITigerstripeModelProject project = map.getCorrespondingITigerstripeProject();
							try {
								//IAbstractArtifact createdIArtifact = createdEArtifact
								//		.getCorrespondingIArtifact();
								//System.out.println("Just created: "
								//		+ createdIArtifact);
								
								if (pattern instanceof IEnumPattern){
									IEnumPattern enumPattern =(IEnumPattern) pattern;
									IAbstractArtifact artifact = enumPattern.createArtifact(
											project, 
											createdEArtifact.getPackage(), 
											createdEArtifact.getName(), enumPattern.getExtendedArtifactName(),
											enumPattern.getBaseType());
									enumPattern.addToManager(project, artifact);
								} else if (pattern instanceof IQueryPattern){
									IQueryPattern queryPattern =(IQueryPattern) pattern;
									IAbstractArtifact artifact = queryPattern.createArtifact(
											project, 
											createdEArtifact.getPackage(), 
											createdEArtifact.getName(), queryPattern.getExtendedArtifactName(),
											queryPattern.getReturnType());
									queryPattern.addToManager(project, artifact);
								} else {
									IArtifactPattern artifactPattern =(IArtifactPattern) pattern;
									IAbstractArtifact artifact = artifactPattern.createArtifact(
											project, 
											createdEArtifact.getPackage(), 
											createdEArtifact.getName(), artifactPattern.getExtendedArtifactName());
									artifactPattern.addToManager(project, artifact);
								}
							} catch (TigerstripeException ex) {
								ex.printStackTrace();
							}
						}
					}
					//System.out.println("Created Object="
					//		+ getCreateRequest().getNewObject());
				}

			};
			tool.setProperties(getToolProperties());
			return tool;
		}
	}

	/**
	 * @generated
	 */
	private static class LinkToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List relationshipTypes;

		/**
		 * @generated
		 */
		private LinkToolEntry(String title, String description,
				ImageDescriptor smallIcon, ImageDescriptor largeIcon,
				List relationshipTypes) {
			super(title, description, smallIcon, largeIcon);
			this.relationshipTypes = relationshipTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}
}
