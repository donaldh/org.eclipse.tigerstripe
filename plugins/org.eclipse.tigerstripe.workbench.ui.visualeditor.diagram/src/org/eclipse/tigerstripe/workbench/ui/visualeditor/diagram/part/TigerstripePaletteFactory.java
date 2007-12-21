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
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEventArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IExceptionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IQueryArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.core.profile.properties.CoreArtifactSettingsProperty;
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
		IWorkbenchProfile profile = API.getIWorkbenchProfileSession()
				.getActiveProfile();
		CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
				.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
		if (prop.getDetailsForType(IManagedEntityArtifact.class.getName())
				.isEnabled()) {
			paletteContainer.add(createManagedEntity1CreationTool());
		}
		if (prop.getDetailsForType(IDatatypeArtifact.class.getName())
				.isEnabled()) {
			paletteContainer.add(createDatatype2CreationTool());
		}
		if (prop.getDetailsForType(IEnumArtifact.class.getName()).isEnabled()) {
			paletteContainer.add(createEnumeration3CreationTool());
		}
		if (prop.getDetailsForType(IAssociationClassArtifact.class.getName())
				.isEnabled()) {
			// paletteContainer.add(createAssociationClass8CreationTool());
		}
		if (prop.getDetailsForType(IQueryArtifact.class.getName()).isEnabled()) {
			paletteContainer.add(createQuery5CreationTool());
		}
		if (prop.getDetailsForType(IUpdateProcedureArtifact.class.getName())
				.isEnabled()) {
			paletteContainer.add(createUpdateProcedure6CreationTool());
		}
		if (prop.getDetailsForType(IEventArtifact.class.getName()).isEnabled()) {
			paletteContainer.add(createNotification4CreationTool());
		}
		if (prop.getDetailsForType(ISessionArtifact.class.getName())
				.isEnabled()) {
			paletteContainer.add(createSessionFacade8CreationTool());
		}
		if (prop.getDetailsForType(IExceptionArtifact.class.getName())
				.isEnabled()) {
			paletteContainer.add(createException7CreationTool());
		}
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

	/**
	 * @generated
	 */
	private PaletteContainer createConnections3Group() {
		PaletteContainer paletteContainer = new PaletteDrawer("Connections");
		paletteContainer.add(createExtends1CreationTool());
		paletteContainer.add(createAssociation2CreationTool());
		paletteContainer.add(createDependency3CreationTool());
		paletteContainer.add(createReference4CreationTool());
		paletteContainer.add(createAssociationClass5CreationTool());
		paletteContainer.add(createAssociationClassClass6CreationTool());
		paletteContainer.add(createAssociationClassConnection7CreationTool());
		paletteContainer.add(createManages8CreationTool());
		paletteContainer.add(createEmits9CreationTool());
		paletteContainer.add(createSupports10CreationTool());
		paletteContainer.add(createExposes11CreationTool());
		paletteContainer.add(createReturns12CreationTool());
		paletteContainer.add(createImplements13CreationTool());
		return paletteContainer;
	}

	/**
	 * @generated
	 */
	private ToolEntry createManagedEntity1CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_entity.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_entity-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.ManagedEntityArtifact_1003);
		ToolEntry result = new NodeToolEntry("Managed Entity",
				"Create new Managed Entity", smallImage, largeImage,
				elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createDatatype2CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_datatype.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_datatype-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.DatatypeArtifact_1005);
		ToolEntry result = new NodeToolEntry("Datatype", "Create new Datatype",
				smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createEnumeration3CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_enum.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_enum-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.Enumeration_1006);
		ToolEntry result = new NodeToolEntry("Enumeration",
				"Create new Enumeration", smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createNotification4CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_notification.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_notification-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.NotificationArtifact_1004);
		ToolEntry result = new NodeToolEntry("Notification",
				"Create new Notification", smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createNamedQuery5CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_query.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_query-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
		ToolEntry result = new NodeToolEntry("Named Query",
				"Create new Named Query", smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * 
	 */
	private ToolEntry createQuery5CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_query.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_query-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.NamedQueryArtifact_1001);
		ToolEntry result = new NodeToolEntry("Named Query",
				"Create new Named Query", smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createUpdateProcedure6CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_updateProc.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_updateProc-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.UpdateProcedureArtifact_1007);
		ToolEntry result = new NodeToolEntry("Update Procedure",
				"Create new Update Procedure", smallImage, largeImage,
				elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createException7CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_exception.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_exception-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.ExceptionArtifact_1002);
		ToolEntry result = new NodeToolEntry("Exception",
				"Create new Exception", smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createSessionFacade8CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_session.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/new_session-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.SessionFacadeArtifact_1008);
		ToolEntry result = new NodeToolEntry("Session Facade",
				"Create new Session Facade", smallImage, largeImage,
				elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createAttribute1CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/field_private_obj-small.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/field_private_obj-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.Attribute_2001);
		elementTypes.add(TigerstripeElementTypes.Attribute_2002);
		elementTypes.add(TigerstripeElementTypes.Attribute_2003);
		elementTypes.add(TigerstripeElementTypes.Attribute_2005);
		elementTypes.add(TigerstripeElementTypes.Attribute_2006);
		elementTypes.add(TigerstripeElementTypes.Attribute_2009);
		elementTypes.add(TigerstripeElementTypes.Attribute_2011);
		ToolEntry result = new NodeToolEntry("Attribute",
				"Create new Attribute", smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createMethod2CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/methpub_obj-small.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/methpub_obj-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.Method_2004);
		elementTypes.add(TigerstripeElementTypes.Method_2007);
		elementTypes.add(TigerstripeElementTypes.Method_2010);
		elementTypes.add(TigerstripeElementTypes.Method_2012);
		ToolEntry result = new NodeToolEntry("Method", "Create new Method",
				smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createLiteral3CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/literal-small.gif");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/literal-large.gif");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.Literal_2008);
		ToolEntry result = new NodeToolEntry("Literal", "Create new Literal",
				smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createExtends1CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/ExtendsIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/ExtendsIcon-large.png");

		final List relationshipTypes = new ArrayList();
		relationshipTypes
				.add(TigerstripeElementTypes.AbstractArtifactExtends_3007);
		ToolEntry result = new LinkToolEntry("Extends",
				"Create Extends Relationship", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createAssociation2CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssociationIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssociationIcon-large.png");

		final List relationshipTypes = new ArrayList();
		relationshipTypes.add(TigerstripeElementTypes.Association_3001);
		ToolEntry result = new LinkToolEntry("Association",
				"Create Association Relationship", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createDependency3CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssociationIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssociationIcon-large.png");

		final List relationshipTypes = new ArrayList();
		relationshipTypes.add(TigerstripeElementTypes.Dependency_3008);
		ToolEntry result = new LinkToolEntry("Dependency",
				"Create a new dependency relationship", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createReference4CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssociationIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssociationIcon-large.png");

		final List relationshipTypes = new ArrayList();
		relationshipTypes.add(TigerstripeElementTypes.Reference_3009);
		ToolEntry result = new LinkToolEntry("Reference",
				"Create a new referency relationship", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createAssociationClass5CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssocClassLinkIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssocClassLinkIcon-large.png");

		final List relationshipTypes = new ArrayList();
		relationshipTypes.add(TigerstripeElementTypes.AssociationClass_3010);
		ToolEntry result = new LinkToolEntry("Association Class",
				"Create new Association Class", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createAssociationClassClass6CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssocClassLinkIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/AssocClassLinkIcon-large.png");

		final List elementTypes = new ArrayList();
		elementTypes.add(TigerstripeElementTypes.AssociationClassClass_1009);
		ToolEntry result = new NodeToolEntry("Association Class Class",
				"Create a new class contained within and AssociationClass",
				smallImage, largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createAssociationClassConnection7CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeElementTypes
				.getImageDescriptor(TigerstripeElementTypes.AssociationClassAssociatedClass_3011);

		largeImage = smallImage;

		final List relationshipTypes = new ArrayList();
		relationshipTypes
				.add(TigerstripeElementTypes.AssociationClassAssociatedClass_3011);
		ToolEntry result = new LinkToolEntry("Association Class Connection",
				"Create a new Association Class Connection", smallImage,
				largeImage, relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createManages8CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-large.png");

		final List relationshipTypes = new ArrayList();
		relationshipTypes
				.add(TigerstripeElementTypes.SessionFacadeArtifactManagedEntities_3003);
		ToolEntry result = new LinkToolEntry("Manages",
				"Designate Managed Entities", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createEmits9CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-large.png");

		final List relationshipTypes = new ArrayList();
		relationshipTypes
				.add(TigerstripeElementTypes.SessionFacadeArtifactEmittedNotifications_3002);
		ToolEntry result = new LinkToolEntry("Emits",
				"Designate Emitted Notifications", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createSupports10CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-large.png");

		final List relationshipTypes = new ArrayList();
		relationshipTypes
				.add(TigerstripeElementTypes.SessionFacadeArtifactNamedQueries_3005);
		ToolEntry result = new LinkToolEntry("Supports",
				"Designate Supported Queries", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createExposes11CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-large.png");

		final List relationshipTypes = new ArrayList();
		relationshipTypes
				.add(TigerstripeElementTypes.SessionFacadeArtifactExposedProcedures_3006);
		ToolEntry result = new LinkToolEntry("Exposes",
				"Designate Exposed Procedures", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createReturns12CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-small.png");

		largeImage = TigerstripeDiagramEditorPlugin
				.findImageDescriptor("/org.eclipse.tigerstripe.workbench.ui.resources/icons/RelationshipIcon-large.png");

		final List relationshipTypes = new ArrayList();
		relationshipTypes
				.add(TigerstripeElementTypes.NamedQueryArtifactReturnedType_3004);
		ToolEntry result = new LinkToolEntry("Returns", "Specify Query Return",
				smallImage, largeImage, relationshipTypes);

		return result;
	}

	/**
	 * @generated
	 */
	private ToolEntry createImplements13CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = TigerstripeElementTypes
				.getImageDescriptor(TigerstripeElementTypes.Dependency_3008);

		largeImage = smallImage;

		final List relationshipTypes = new ArrayList();
		relationshipTypes.add(TigerstripeElementTypes.Dependency_3008);
		ToolEntry result = new LinkToolEntry("Implements",
				"Creates an Implements relationship", smallImage, largeImage,
				relationshipTypes);

		return result;
	}

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
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes);
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
